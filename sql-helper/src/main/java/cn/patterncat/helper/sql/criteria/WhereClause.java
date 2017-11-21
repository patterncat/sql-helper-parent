package cn.patterncat.helper.sql.criteria;

import cn.patterncat.helper.sql.builder.SQL;
import cn.patterncat.helper.sql.util.ValueUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by patterncat on 2017-11-21.
 */
public class WhereClause {

    protected List<NamedCriteria> criteria = new ArrayList<>();

    protected Map<String, Object> namedParams = new HashMap<String, Object>();

    protected String table;

    protected Pageable pageable;

    protected String pageSql;

    protected String whereSql;

    public WhereClause(String table) {
        this.table = table;
    }

    public static WhereClause newInstance(String table){
        return new WhereClause(table);
    }

    public WhereClause page(Pageable pageable){
        this.pageable = pageable;
        return this;
    }

    public WhereClause add(NamedCriteria namedCriteria){
        criteria.add(namedCriteria);
        return this;
    }

    public WhereClause build(){
        this.pageSql = buildPagingSql();
        this.whereSql = criteria.stream()
                .map(e -> e.toSql(namedParams))
                .collect(Collectors.joining(" and "));
        return this;
    }

    public String toCountSql(){
        SQL sql = new SQL();
        sql.SELECT("count(*)");
        sql.FROM(table);
        sql.WHERE(whereSql);
        return sql.toString();
    }

    public String toSelectSql(){
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM(table);
        sql.WHERE(whereSql);
        StringBuilder select = new StringBuilder(sql.toString());
        select.append(pageSql);
        return select.toString();
    }

    public Map<String, Object> getNamedParams() {
        return namedParams;
    }

    protected String buildPagingSql(){
        if(pageable == null){
            return "";
        }
        StringBuilder builder = new StringBuilder(" ");
        Sort sort = pageable.getSort();
        if(sort != null){
            builder.append("order by ");
            String orderBy = StreamSupport.stream(sort.spliterator(),false)
                    .map(order -> {
                        return ValueUtils.camelhumpToUnderline(order.getProperty()) +
                                " " +
                                order.getDirection();
                    }).collect(Collectors.joining(","));
            builder.append(orderBy);
        }
        builder.append(" limit ");
        builder.append(pageable.getPageSize());
        builder.append(" offset ");
        builder.append(pageable.getOffset());
        return builder.toString();
    }

    public List<NamedCriteria> getCriteria() {
        return criteria;
    }

    public String getTable() {
        return table;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public String getWhereSql() {
        return whereSql;
    }

    public String getPageSql() {
        return pageSql;
    }
}
