package cn.patterncat.jdbc.query;

import com.querydsl.sql.dml.SQLUpdateClause;

/**
 * An interface used by {@link QueryDslJdbcTemplate} for update calls
 * where you are able to provide a {@link SQLUpdateClause}
 * implementation to handle the update logic.
 *
 * @author Thomas Risberg
 * @since 1.0
 * @see SQLUpdateClause
 */
public interface SqlUpdateCallback {

    long doInSqlUpdateClause(SQLUpdateClause update);

}
