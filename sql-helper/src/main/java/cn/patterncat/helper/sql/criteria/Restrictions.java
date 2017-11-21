package cn.patterncat.helper.sql.criteria;

import java.util.Collection;
import static cn.patterncat.helper.sql.util.ValueUtils.camelhumpToUnderline;

/**
 * Created by patterncat on 2017-11-21.
 */
public class Restrictions {

    public static NamedCriteria and(NamedCriteria left, NamedCriteria right){
        return new NamedCriteria(left,right,"and");
    }

    public static NamedCriteria or(NamedCriteria left, NamedCriteria right){
        return new NamedCriteria(left,right,"or");
    }

    public static NamedCriteria isNull(String property) {
        return new NamedCriteria(camelhumpToUnderline(property) + " is null");
    }

    public static NamedCriteria isNotNull(String property) {
        return new NamedCriteria(camelhumpToUnderline(property) + " is not null");
    }

    public static NamedCriteria equalTo(String property, Object value) {
        return new NamedCriteria(camelhumpToUnderline(property) + " =", value, property);
    }

    public static NamedCriteria notEqualTo(String property, Object value) {
        return new NamedCriteria(camelhumpToUnderline(property) + " <>", value, property);
    }

    public static NamedCriteria greaterThan(String property, Object value) {
        return new NamedCriteria(camelhumpToUnderline(property) +" >", value, property);
    }

    public static NamedCriteria greaterThanOrEqualTo(String property, Object value) {
        return new NamedCriteria(camelhumpToUnderline(property)+" >=", value, property);
    }

    public static NamedCriteria lessThan(String property, Object value) {
        return new NamedCriteria(camelhumpToUnderline(property)+" <", value, property);
    }

    public static NamedCriteria lessThanOrEqualTo(String property, Object value) {
        return new NamedCriteria(camelhumpToUnderline(property)+" <=", value, property);
    }

    public static NamedCriteria in(String property, Collection values) {
        return new NamedCriteria(camelhumpToUnderline(property)+" in", values, property);
    }

    public static NamedCriteria notIn(String property, Collection values) {
        return new NamedCriteria(camelhumpToUnderline(property)+" not in", values, property);
    }

    public static NamedCriteria between(String property, Object value1, Object value2) {
        return new NamedCriteria(camelhumpToUnderline(property)+" between", value1, value2, property);
    }

    public static NamedCriteria notBetween(String property, Object value1, Object value2) {
        return new NamedCriteria(camelhumpToUnderline(property)+" not between", value1, value2, property);
    }
}
