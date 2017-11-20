package cn.patterncat.helper.sql;

import com.querydsl.core.QueryException;
import org.springframework.dao.UncategorizedDataAccessException;

/**
 * Exception thrown when we can't classify a Query into
 * one of Spring's data access exceptions.
 *
 * @author Thomas Risberg
 */
public class UncategorizedQueryException extends UncategorizedDataAccessException {

    /**
     * Constructor for UncategorizedDataAccessException.
     * @param msg   the detail message
     * @param cause the exception thrown by underlying data access API
     */
    public UncategorizedQueryException(String msg, QueryException cause) {
        super(msg, cause);
    }
}