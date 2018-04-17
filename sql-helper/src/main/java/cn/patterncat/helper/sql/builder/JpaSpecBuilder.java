package cn.patterncat.helper.sql.builder;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by patterncat on 2018-04-17.
 */
public class JpaSpecBuilder<T> {

    private Specifications<T> specs;

    private JpaSpecBuilder() {
        Specification<T> start = (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.isTrue(cb.literal(true));
        this.specs = Specifications.where(start);
    }

    public static <T> JpaSpecBuilder<T> newInstance(){
        return new JpaSpecBuilder<>();
    }

    public JpaSpecBuilder<T> or(final Specification<T> spec) {
        specs = specs.or(spec);
        return this;
    }

    public JpaSpecBuilder<T> and(final Specification<T> spec) {
        specs = specs.and(spec);
        return this;
    }

    public Specification<T> build() {
        return specs;
    }
}
