package cn.patterncat.helper.sql.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by patterncat on 2017-11-21.
 */
public class NamedParamSqlBuilder {

    protected static Logger LOGGER = LoggerFactory.getLogger(NamedParamSqlBuilder.class);

    private Map<String, Object> params = new HashMap<String, Object>();

    private StringBuilder sql = new StringBuilder();

    public static final String SPACE = " ";
    public static final String LINE_BREAK = "\n";
    public static final String WHERE = " where ";
    public static final String AND = " and ";
    public static final String OR = " or ";

    public NamedParamSqlBuilder(String clause) {
        this.sql.append(clause);
    }

    public NamedParamSqlBuilder where(){
        this.sql.append(WHERE);
        return this;
    }

    public NamedParamSqlBuilder and(){
        this.sql.append(AND);
        return this;
    }

    public NamedParamSqlBuilder or(){
        this.sql.append(OR);
        return this;
    }

    public NamedParamSqlBuilder append(String clause,String key,Object value) {
        this.sql.append(SPACE).append(clause).append(LINE_BREAK);
        params.put(key,value);
        return this;
    }

    public NamedParamSqlBuilder appendIfNotNull(String clause,String key,Object value) {
        if (value == null) {
            this.sql.append(" 1 = 1");
            return this;
        } else if (value.getClass().isArray() && ((Object[]) value).length == 0) {
            this.sql.append(" 1 = 1");
            return this;
        } else if (value instanceof Collection && ((Collection) value).size() == 0) {
            this.sql.append(" 1 = 1");
            return this;
        } else if (value instanceof Map && ((Map) value).size() == 0) {
            this.sql.append(" 1 = 1");
            return this;
        } else if (StringUtils.isEmpty(value.toString())) {
            this.sql.append(" 1 = 1");
            return this;
        }

        append(clause,key,value);

        return this;
    }

    public NamedParamSqlBuilder appendSetIfNotEmpty(String clause, String name, Collection<?> values) {
        if (values == null || values.size() == 0) return this;
        StringBuilder sbSet = new StringBuilder();

        for (Object value : values) {
            if (value == null) {
                continue;
            } else if (isNumber(value)) {
                sbSet.append(value).append(",");
            } else {
                sbSet.append("'").append(value).append("'").append(",");
            }
        }

        if (sbSet.length() > 0) {
            sbSet.setLength(sbSet.length() - 1);
            append(StringUtils.replace(clause, ":" + name, sbSet.toString()),name,values);
        }

        return this;
    }

    public String build() {
        return this.sql.toString();
    }

    @Override
    public String toString() {
        return sql.toString();
    }

    public Map<String, Object> getParams() {
        return params;
    }

    private boolean isNumber(Object value) {
        if (value == null) {
            return false;
        } else {
            return value.getClass() == Byte.TYPE || value.getClass() == Byte.class ||
                    value.getClass() == Short.TYPE || value.getClass() == Short.class ||
                    value.getClass() == Integer.TYPE || value.getClass() == Integer.class ||
                    value.getClass() == Long.TYPE || value.getClass() == Long.class ||
                    value.getClass() == Float.TYPE || value.getClass() == Float.class ||
                    value.getClass() == Double.TYPE || value.getClass() == Double.class;
        }
    }

}
