package cn.patterncat.helper.sql.util;

import com.google.common.base.CaseFormat;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by patterncat on 2017-11-21.
 */
public class ValueUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValueUtils.class);

    public static String camelhumpToUnderline(String value){
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,value);
    }

    public static boolean isNumber(Object value) {
        if (value == null) {
            return false;
        }

        return value.getClass() == Byte.TYPE || value.getClass() == Byte.class ||
                value.getClass() == Short.TYPE || value.getClass() == Short.class ||
                value.getClass() == Integer.TYPE || value.getClass() == Integer.class ||
                value.getClass() == Long.TYPE || value.getClass() == Long.class ||
                value.getClass() == Float.TYPE || value.getClass() == Float.class ||
                value.getClass() == Double.TYPE || value.getClass() == Double.class;
    }

    public static Map<String, Object> beanToMap(Object bean) {
        if(bean == null){
            return Collections.emptyMap();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if ("class".equals(key)) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter.invoke(bean);
                map.put(key, value);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
        return map;
    }

    public static Map<String, Object> beanToMapWithIgnore(Object bean) {
        if (bean == null){
            return Collections.emptyMap();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> clazz = bean.getClass();
        try {
            //do not get father class fields
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Ignore shouldSkip = fields[i].getAnnotation(Ignore.class);
                if(shouldSkip != null){
                    continue;
                }
                String name = fields[i].getName();
                Method method = clazz.getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                Object value = method.invoke(bean);
                Class<?> fieldType = fields[i].getType();
                if(fieldType.isEnum()){
                    map.put(name,((Enum)value).ordinal());
                }else{
                    map.put(name,value);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return map;
    }
}
