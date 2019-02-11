## changelog
### 0.0.7
revert spring boot version variable
update guava to 27.0.1-jre

### 0.0.6
extract spring boot version
```
mvn clean install -Dspring-boot.version=1.5.10.RELEASE
```

### 0.0.5
add jpa spec builder
### v0.0.4
- add built state for WhereClause make build() not mandatory

### v0.0.3
- add getter for pagable
- add build for WhereClause
- fix where clause null
- add insert and update selective sql builder

### v0.0.2
- add count sql

### v0.0.1
- migrate QueryDslJdbcTemplate from [spring-data-jdbc-ext](https://github.com/spring-projects/spring-data-jdbc-ext/tree/1.2.1.RELEASE)
- add named criteria
