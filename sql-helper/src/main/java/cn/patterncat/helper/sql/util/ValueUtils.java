package cn.patterncat.helper.sql.util;

import com.google.common.base.CaseFormat;

/**
 * Created by patterncat on 2017-11-21.
 */
public class ValueUtils {

    public static String camelhumpToUnderline(String value){
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,value);
    }

    public static boolean isNumber(Object value) {
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
