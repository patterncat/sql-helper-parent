package com.example.demo.dao;

import cn.patterncat.jdbc.QueryDslJdbcRepository;
import cn.patterncat.jdbc.QueryDslJdbcTemplate;
import com.example.demo.domain.Book;
import com.example.demo.querydsl.QBook;
import com.querydsl.sql.RelationalPath;
import org.springframework.stereotype.Component;

/**
 * Created by patterncat on 2017-11-21.
 */
@Component
public class BookDao extends QueryDslJdbcRepository<Book,Long> {
    public BookDao(QueryDslJdbcTemplate queryDslJdbcTemplate) {
        super(queryDslJdbcTemplate);
    }

    @Override
    protected void setId(Book entity, Long aLong) {
        entity.setId(aLong);
    }

    @Override
    public RelationalPath<Book> getEntityPath() {
        return QBook.qBook;
    }

    @Override
    protected boolean isAutoIncrement() {
        return true;
    }
}
