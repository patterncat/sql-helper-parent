package com.example.demo.config;

import cn.patterncat.jdbc.QueryDslJdbcTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by patterncat on 2017-11-21.
 */
@Configuration
public class DbConfig {

    @Bean
    @Primary
    @Qualifier("dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public QueryDslJdbcTemplate queryDslJdbcTemplate(DataSource dataSource){
        return new QueryDslJdbcTemplate(dataSource);
    }
}
