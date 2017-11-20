package cn.patterncat.helper.sql;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * Created by patterncat on 2017-11-21.
 */
public class QueryDslJdbcRepository<T, ID extends Serializable> implements QueryDslRepository<T,ID>{
    public Iterable<T> findAll(Sort sort) {
        return null;
    }

    public Page<T> findAll(Pageable pageable) {
        return null;
    }

    public <S extends T> S save(S entity) {
        return null;
    }

    public <S extends T> Iterable<S> save(Iterable<S> entities) {
        return null;
    }

    public T findOne(ID id) {
        return null;
    }

    public boolean exists(ID id) {
        return false;
    }

    public Iterable<T> findAll() {
        return null;
    }

    public Iterable<T> findAll(Iterable<ID> ids) {
        return null;
    }

    public long count() {
        return 0;
    }

    public void delete(ID id) {

    }

    public void delete(T entity) {

    }

    public void delete(Iterable<? extends T> entities) {

    }

    public void deleteAll() {

    }

    public T findOne(Predicate predicate) {
        return null;
    }

    public Iterable<T> findAll(Predicate predicate) {
        return null;
    }

    public Iterable<T> findAll(Predicate predicate, Sort sort) {
        return null;
    }

    public Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
        return null;
    }

    public Iterable<T> findAll(OrderSpecifier<?>... orders) {
        return null;
    }

    public Page<T> findAll(Predicate predicate, Pageable pageable) {
        return null;
    }

    public long count(Predicate predicate) {
        return 0;
    }

    public boolean exists(Predicate predicate) {
        return false;
    }
}
