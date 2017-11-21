package cn.patterncat.jdbc;

import com.google.common.collect.Iterables;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.sql.PrimaryKey;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.dml.BeanMapper;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

/**
 * Created by patterncat on 2017-11-21.
 */
public abstract class SqlQueryJdbcAdapter<T extends Persistable<ID>, ID extends Serializable> {

    protected QueryDslJdbcTemplate queryDslJdbcTemplate;

    protected Class<T> domainClass;

    protected Class<ID> idClass;

    protected BeanPropertyRowMapper beanPropertyRowMapper;

    public SqlQueryJdbcAdapter(QueryDslJdbcTemplate queryDslJdbcTemplate) {
        this.queryDslJdbcTemplate = queryDslJdbcTemplate;
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] types = type.getActualTypeArguments();
        domainClass = (Class<T>) types[0];
        idClass = (Class<ID>) types[1];
        beanPropertyRowMapper = BeanPropertyRowMapper.newInstance(domainClass);
    }

    protected abstract void setId(T entity, ID id);

    //---------------querydsl helper------------------------------

    public abstract RelationalPath<T> getEntityPath();

    protected SQLQuery newSqlQuery() {
        return (SQLQuery) queryDslJdbcTemplate.newSqlQuery().from(getEntityPath());
    }

    @SuppressWarnings("unchecked")
    protected Predicate primaryKeyPredicate(ID[] ids) {
        if (ids == null || ids.length == 0) {
            throw new IllegalArgumentException(
                    "The given primary key `ids` must not be null or empty.");
        }
        PrimaryKey<T> primaryKey = getEntityPath().getPrimaryKey();
        assert primaryKey != null;
        List<? extends Path<?>> idPaths = primaryKey.getLocalColumns();
        assert idPaths != null && idPaths.size() > 0;
        if (ids.length == 1) {
            return ((SimpleExpression<ID>) idPaths.get(0)).eq(ids[0]);
        }

        return ((SimpleExpression<ID>) idPaths.get(0)).in(ids);
    }

    protected Predicate primaryKeyPredicate(Iterable<ID> ids) {
        ID[] idArr = Iterables.toArray(ids, getIdClass());
        return primaryKeyPredicate(idArr);
    }

    @SuppressWarnings("unchecked")
    protected Predicate primaryKeyPredicate(ID id) {
        PrimaryKey<T> primaryKey = getEntityPath().getPrimaryKey();
        assert primaryKey != null;
        List<? extends Path<?>> idPaths = primaryKey.getLocalColumns();
        assert idPaths != null && idPaths.size() > 0;
        return ((SimpleExpression<ID>) idPaths.get(0)).eq(id);
    }

    public Path<?>[] columns(final RelationalPath<?> entityPath) {
        List<Path<?>> pathList = entityPath.getColumns();
        Path[] paths = new Path[pathList.size()];
        return pathList.toArray(paths);
    }

    public SQLQuery applyPage(Pageable pageable, final SQLQuery sqlQuery, final RelationalPath<?> entityPath) {
        if (pageable == null) {
            return sqlQuery;
        }

        sqlQuery.offset(pageable.getOffset());
        sqlQuery.limit(pageable.getPageSize());

        return applySort(pageable.getSort(), sqlQuery, entityPath);
    }

    public SQLQuery applySort(Sort sort, final SQLQuery sqlQuery, final RelationalPath<?> entityPath) {
        if (sort == null || sort.iterator() == null ||
                !sort.iterator().hasNext()) {
            return sqlQuery;
        }
        if (sort instanceof QSort) {
            QSort qSort = (QSort) sort;
            List<OrderSpecifier<?>> orderSpecifiers = qSort.getOrderSpecifiers();
            if (orderSpecifiers == null || orderSpecifiers.isEmpty()) {
                return sqlQuery;
            }
            qSort.getOrderSpecifiers().forEach(sqlQuery::orderBy);
        } else {
            for (Sort.Order order : sort) {
                if (order.getProperty() == null) {
                    continue;
                }
                sqlQuery.orderBy(toOrder(entityPath, order));
            }
        }
        return sqlQuery;
    }

    public OrderSpecifier<?> toOrder(final RelationalPath<?> entityPath, Sort.Order order) {
        String property;
        Expression<Object> propExp;
        if ((property = order.getProperty()) == null ||
                (propExp = (Expression<Object>) findPath(entityPath, property)) == null) {
            return null;
        }
        return new OrderSpecifier(order.isAscending() ?
                Order.ASC : Order.DESC, propExp);
    }

    public Path<?> findPath(final RelationalPath<?> entityPath, String propName) {
        for (Path<?> path : entityPath.getColumns()) {
            if (Objects.equals(propName, path.getMetadata().getElement())) {
                return path;
            }
        }
        return null;
    }


    protected List<T> queryForList(final SQLQuery sqlQuery) {
        return queryDslJdbcTemplate.query(
                sqlQuery, Projections.bean(getEntityPath(),
                        columns(getEntityPath())));
    }

    protected T queryForObject(final SQLQuery sqlQuery) {
        return queryDslJdbcTemplate.queryForObject(
                sqlQuery, Projections.bean(getEntityPath(),
                        columns(getEntityPath())));
    }

    public Class<ID> getIdClass() {
        return idClass;
    }

    public Class<T> getDomainClass() {
        return domainClass;
    }

    protected abstract boolean isAutoIncrement();

    public <S extends T> S insert(final S entity) {
        boolean withKey = false;
        if (isAutoIncrement()) {
            withKey = entity.getId() == null;
        } else { // not auto_increment for PK
            if (entity.getId() == null)
                throw new InvalidDataAccessResourceUsageException(
                        "The entity's ID must not be null.");
        }
        if (withKey) {
            ID id = queryDslJdbcTemplate.insertWithKey(getEntityPath(),
                    insert -> insert.populate(entity, BeanMapper.WITH_NULL_BINDINGS)
                            .executeWithKey(getIdClass()));
            setId(entity,id);
        } else {
            long row = queryDslJdbcTemplate.insert(getEntityPath(),
                    insert -> insert.populate(entity, BeanMapper.WITH_NULL_BINDINGS)
                            .execute()
            );
            if (row == 0) {
                return null;
            }
        }
        return entity;
    }

    public <S extends T> S update(final S entity) {
        Objects.requireNonNull(entity, "entity");
        queryDslJdbcTemplate.update(getEntityPath(),
                update -> update.populate(entity, BeanMapper.DEFAULT)
                        .where(primaryKeyPredicate(entity.getId()))
                        .execute()
        );
        return entity;
    }
}
