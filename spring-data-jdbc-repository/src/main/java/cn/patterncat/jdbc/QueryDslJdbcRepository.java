package cn.patterncat.jdbc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.sql.PrimaryKey;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.dml.BeanMapper;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.*;
import org.springframework.data.querydsl.QSort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by patterncat on 2017-11-21.
 */
public abstract class QueryDslJdbcRepository<T extends Persistable<ID>, ID extends Serializable> extends SqlQueryJdbcAdapter<T,ID> implements QueryDslRepository<T,ID>{

    public QueryDslJdbcRepository(QueryDslJdbcTemplate queryDslJdbcTemplate) {
        super(queryDslJdbcTemplate);
    }

    //----------------interface implementation-----------------------

    public Iterable<T> findAll(Sort sort) {
        final SQLQuery sqlQuery = newSqlQuery();
        applySort(sort, sqlQuery, getEntityPath());
        return queryForList(sqlQuery);
    }

    public Page<T> findAll(Pageable pageable) {
        return null;
    }

    public <S extends T> S save(S entity) {
        if (entity.getId() == null) { // only auto_increment
            return insert(entity);
        } else {
            return exists(entity.getId()) ? update(entity) : insert(entity);
        }
    }

    public <S extends T> Iterable<S> save(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            S updatedEntity = save(entity);
            if (updatedEntity != null) {
                result.add(updatedEntity);
            }
        }

        return result;
    }

    public T findOne(ID id) {
        return findOne(primaryKeyPredicate(id));
    }

    public boolean exists(ID id) {
        return exists(primaryKeyPredicate(id));
    }

    public Iterable<T> findAll() {
        SQLQuery sqlQuery = newSqlQuery();
        return queryDslJdbcTemplate.query(sqlQuery,beanPropertyRowMapper);
    }

    public Iterable<T> findAll(Iterable<ID> ids) {
        return queryForList((SQLQuery) newSqlQuery().where(primaryKeyPredicate(ids)));
    }

    public long count() {
        return queryDslJdbcTemplate.count(newSqlQuery());
    }

    public void delete(ID id) {
        queryDslJdbcTemplate.delete(getEntityPath(),
                delete -> delete.where(primaryKeyPredicate(id)).execute());
    }

    public void delete(T entity) {
        delete(entity.getId());
    }

    public void delete(Iterable<? extends T> entities) {
        if (entities == null || Iterables.isEmpty(entities)) {
            return;
        }
        final Iterable<T> notNullEntities = Iterables.filter(entities,new com.google.common.base.Predicate(){

            @Override
            public boolean apply(@Nullable Object input) {
                return input != null && ((Persistable)input).getId() != null;
            }
        });

        final Iterable<ID> deletedIds = Iterables.transform(notNullEntities,
                input -> {
                    assert input != null;
                    return input.getId();
                }
        );

        queryDslJdbcTemplate.delete(getEntityPath(), deleteClause ->
                deleteClause.where(primaryKeyPredicate(deletedIds)).execute());
    }

    public void deleteAll() {
        throw new UnsupportedOperationException("not support delete all");
    }

    public T findOne(Predicate predicate) {
        return queryForObject((SQLQuery) newSqlQuery().where(predicate));
    }

    public Iterable<T> findAll(Predicate predicate) {
        return queryForList((SQLQuery) newSqlQuery().where(predicate));
    }

    public Iterable<T> findAll(Predicate predicate, Sort sort) {
        final SQLQuery sqlQuery = (SQLQuery) newSqlQuery().where(predicate);
        applySort(sort,sqlQuery,getEntityPath());
        return queryForList(sqlQuery);
    }

    public Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
        final SQLQuery sqlQuery = (SQLQuery) newSqlQuery().where(predicate).orderBy(orders);
        return queryForList(sqlQuery);
    }

    public Iterable<T> findAll(OrderSpecifier<?>... orders) {
        final SQLQuery sqlQuery = (SQLQuery) newSqlQuery().orderBy(orders);
        return queryForList(sqlQuery);
    }

    public Page<T> findAll(Predicate predicate, Pageable pageable) {
        long total = count(predicate);
        List<T> content;
        if (total == 0) {
            content = ImmutableList.of();
        } else {
            final SQLQuery sqlQuery = predicate == null ?
                    newSqlQuery() : (SQLQuery) newSqlQuery().where(predicate);
            applyPage(pageable, sqlQuery, getEntityPath());
            content = queryForList(sqlQuery);
        }
        return new PageImpl<>(content, pageable, total);
    }

    public long count(Predicate predicate) {
        SQLQuery sqlQuery;
        if (predicate != null) {
            sqlQuery = (SQLQuery) newSqlQuery().where(predicate);
        } else {
            sqlQuery = newSqlQuery();
        }
        return queryDslJdbcTemplate.count(sqlQuery);
    }

    public boolean exists(Predicate predicate) {
        return queryDslJdbcTemplate.exists((SQLQuery) newSqlQuery().where(predicate));
    }
}
