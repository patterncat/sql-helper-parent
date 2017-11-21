package com.example.demo.domain;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Date;

public class Book implements Persistable<Long>{

    private Long id;

    private Long bookId;

    private String title;

    private Date createdAt;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getId() == null;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
