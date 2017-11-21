package cn.patterncat.helper.sql.criteria;

import cn.patterncat.helper.sql.builder.SQL;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by patterncat on 2017-11-21.
 */
public class WhereClause {

    protected List<NamedCriteria> criteria = new ArrayList<>();

    protected Map<String, Object> namedParams = new HashMap<String, Object>();

    protected String table;

    protected Pageable pageable;

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

    public String toSql(){
        StringBuilder strBuilder = new StringBuilder();
        int size = criteria.size();
        for(int i = 0; i < size; i++){
            NamedCriteria namedCriteria = criteria.get(i);
            if(StringUtils.isEmpty(namedCriteria.getOp()) && size > 1 && i != 0){
                strBuilder.append(" and ");
            }
            strBuilder.append(namedCriteria.toSql(namedParams));
        }
        if(size == 0){
            strBuilder.append(" 1=1 ");
        }
        return strBuilder.toString();
    }

    public String toFullSql(){
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM(table);
        sql.WHERE(toSql());
        StringBuilder sb = new StringBuilder(sql.toString());

        //paging
        sb.append(buildPagingSql());

        return sb.toString();
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
            builder.append(sort.toString().replaceAll(":",""));
        }
        builder.append(" limit ");
        builder.append(pageable.getOffset());
        builder.append(",");
        builder.append(pageable.getPageSize());
        return builder.toString();
    }
}
