package cn.patterncat.jdbc.query;

import com.querydsl.sql.dml.SQLDeleteClause;

/**
 * An interface used by {@link QueryDslJdbcTemplate} for delete calls
 * where you are able to provide a {@link SQLDeleteClause}
 * implementation to handle the delete.
 *
 * @author Thomas Risberg
 * @since 1.0
 * @see SQLDeleteClause
 */
public interface SqlDeleteCallback {

    long doInSqlDeleteClause(SQLDeleteClause delete);

}
