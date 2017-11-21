package com.example.demo.querydsl;

import cn.patterncat.jdbc.query.QBase;
import com.example.demo.domain.Book;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;

import javax.annotation.Generated;

/**
 * Created by patterncat on 2017-11-21.
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QBook extends QBase<Book> {

    public final NumberPath<Long> id = createNumber("id",Long.class);
    public final NumberPath<Long> bookId = createNumber("bookId",Long.class);
    public final StringPath  title = createString("title");

    public static QBook qBook = new QBook(QBook.class.getSimpleName(),null,"book");

    public QBook(String path, String schema, String table){
        super(Book.class, path, schema, table);
        addMetadata(bookId, ColumnMetadata.named("book_id"));
        addMetadata(title, ColumnMetadata.named("title"));
    }
}
