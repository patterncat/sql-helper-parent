package cn.patterncat.jdbc.query;

import com.querydsl.sql.dml.SQLInsertClause;

/**
 * An interface used by {@link QueryDslJdbcTemplate} for insert calls
 * where you are able to provide a {@link SQLInsertClause}
 * implementation to handle the insert logic.
 *
 * @author Thomas Risberg
 * @since 1.0
 * @see SQLInsertClause
 */
public interface SqlInsertCallback {

    long doInSqlInsertClause(SQLInsertClause insert);

}
