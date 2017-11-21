package cn.patterncat.helper.sql;

import cn.patterncat.helper.sql.builder.NamedParamSqlBuilder;
import cn.patterncat.helper.sql.criteria.Restrictions;
import cn.patterncat.helper.sql.criteria.WhereClause;
import cn.patterncat.helper.sql.util.SqlUtils;
import cn.patterncat.helper.sql.util.ValueUtils;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.Map;

/**
 * Created by patterncat on 2017-11-21.
 */
public class BuilderTest {

    @Test
    public void testBuilderSql(){
        NamedParamSqlBuilder sql = new NamedParamSqlBuilder("select * from book ")
                .where()
                .append("title = :title","title","hello")
                .and()
                .append("name = :name","name","hello")
                .and()
                .appendIfNotNull("status = :status","status",null);
        System.out.println(sql.getParams());
        System.out.println(sql.build());
    }

    @Test
    public void testCriteria(){
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC,"createdAt"),
                new Sort.Order(Sort.Direction.ASC,"id"));
        WhereClause whereClause = WhereClause
                .newInstance("book")
                .add(Restrictions.equalTo("title","this is title"))
                .add(Restrictions.equalTo("id",123L))
                .page(new PageRequest(0,10,sort))
                .build();

        System.out.println(whereClause.getWhereSql());
        System.out.println(whereClause.getPageSql());
        System.out.println(whereClause.toSelectSql());
        System.out.println(whereClause.toCountSql());

        Map<String,Object> params = whereClause.getNamedParams();
        for(Map.Entry<String,Object> entry : params.entrySet()){
            System.out.println("key:"+entry.getKey() + "," + entry.getValue());
        }
    }

    @Test
    public void testSelectAll(){
        WhereClause whereClause = WhereClause.newInstance("book")
                .build();
        System.out.println(whereClause.toSelectSql());
    }

    @Test
    public void testInsertUpdateSql(){
        Example example = new Example();
        example.setCount(0L);
        example.setCreatedAt(new Date());
        example.setTitle("hello");
        Map<String,Object> valueMap = ValueUtils.beanToMap(example);
        System.out.println(SqlUtils.buildInsertNamedSql("example",valueMap,true));
        System.out.println(SqlUtils.buildUpdateNamedSql("example",valueMap,true));
    }

    public static class Example{
        private Long id;

        private String title;

        private Date createdAt;

        private Long count;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }
    }
}
