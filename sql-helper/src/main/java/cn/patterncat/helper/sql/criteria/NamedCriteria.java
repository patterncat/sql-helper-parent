package cn.patterncat.helper.sql.criteria;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by patterncat on 2017-11-21.
 */
public class NamedCriteria {

    private NamedCriteria leftNamedCriteria;

    private NamedCriteria rightNamedCriteria;

    private String op;

    private String condition;

    private String property;

    private Object value;

    private Object secondValue;

    private boolean noValue;

    private boolean singleValue;

    private boolean betweenValue;

    private boolean listValue;

    public String getCondition() {
        return condition;
    }

    public Object getValue() {
        return value;
    }

    public Object getSecondValue() {
        return secondValue;
    }

    public boolean isNoValue() {
        return noValue;
    }

    public boolean isSingleValue() {
        return singleValue;
    }

    public boolean isBetweenValue() {
        return betweenValue;
    }

    public boolean isListValue() {
        return listValue;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public NamedCriteria(NamedCriteria left, NamedCriteria right, String op){
        this.leftNamedCriteria = left;
        this.rightNamedCriteria = right;
        this.op = op;
    }

    protected NamedCriteria(String condition) {
        super();
        this.condition = condition;
        this.property = null;
        this.noValue = true;
    }

    protected NamedCriteria(String condition, Object value, String property) {
        super();
        this.condition = condition;
        this.value = value;
        this.property = property;
        if (value instanceof Collection<?>) {
            this.listValue = true;
        } else {
            this.singleValue = true;
        }
    }

    protected NamedCriteria(String condition, Object value) {
        this(condition, value, null);
    }

    protected NamedCriteria(String condition, Object value, Object secondValue, String property) {
        super();
        this.condition = condition;
        this.value = value;
        this.secondValue = secondValue;
        this.property = property;
        this.betweenValue = true;
    }

    protected NamedCriteria(String condition, Object value, Object secondValue) {
        this(condition, value, secondValue, null);
    }

    public NamedCriteria getLeftNamedCriteria() {
        return leftNamedCriteria;
    }

    public void setLeftNamedCriteria(NamedCriteria leftNamedCriteria) {
        this.leftNamedCriteria = leftNamedCriteria;
    }

    public NamedCriteria getRightNamedCriteria() {
        return rightNamedCriteria;
    }

    public void setRightNamedCriteria(NamedCriteria rightNamedCriteria) {
        this.rightNamedCriteria = rightNamedCriteria;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String toSql(Map<String,Object> namedParams){
        StringBuilder strBuilder = new StringBuilder();
        if(StringUtils.isEmpty(op)){
            strBuilder.append(" ");
            strBuilder.append(toNamedSql(namedParams));
            strBuilder.append(" ");
        }else if("or".equals(op)){
            strBuilder.append("( ");
            strBuilder.append(leftNamedCriteria.toSql(namedParams));
            strBuilder.append(" or ");
            strBuilder.append(rightNamedCriteria.toSql(namedParams));
            strBuilder.append(" ) ");
        }else if("and".equals(op)){
            strBuilder.append("( ");
            strBuilder.append(leftNamedCriteria.toSql(namedParams));
            strBuilder.append(" and ");
            strBuilder.append(rightNamedCriteria.toSql(namedParams));
            strBuilder.append(" ) ");
        }
        return strBuilder.toString();
    }

    private String toNamedSql(Map<String,Object> namedParams){
        StringBuilder strBuilder = new StringBuilder();

        if(isNoValue()){
            strBuilder.append(condition);
            return strBuilder.toString();
        }

        if(isSingleValue()){
            strBuilder.append(condition);
            strBuilder.append(" :");
            strBuilder.append(property);
            namedParams.put(property,value);
            return strBuilder.toString();
        }

        if(isBetweenValue()){
            strBuilder.append(condition);
            strBuilder.append(" ");
            String firstProp = property + "First";
            strBuilder.append(":");strBuilder.append(firstProp);
            namedParams.put(firstProp,value);
            strBuilder.append(" and ");
            String secondProp = property + "Second";
            strBuilder.append(":");strBuilder.append(secondProp);
            namedParams.put(secondProp,secondValue);
            return strBuilder.toString();
        }

        if(isListValue()){
            strBuilder.append(condition);
            strBuilder.append("(");
            strBuilder.append(":");strBuilder.append(property);
            strBuilder.append(")");
            namedParams.put(property,listValue);
            return strBuilder.toString();
        }

        return " ";
    }
}
