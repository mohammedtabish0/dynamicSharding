package com.redisDemo.dynamicDatasource.SQLConfig;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lovepreetsingh
 */
@Configuration
public class DataSourceConfig {

    public static final String SHARD_LOOKUP = "shard_lookup";
    private static final String DATASOURCE_URL = "datasource.url";
    private static final String DATASOURCE_DRIVER_CLASS_NAME = "datasource.driver-class-name";
    private static final String DATASOURCE_USERNAME = "datasource.username";
    private static final String DATASOURCE_CREDENTIAL = "datasource.password";
    private static final String DATASOURCE_INITIAL_SIZE = "datasource.initialSize";
    private static final String DATASOURCE_MIN_IDLE = "datasource.minIdle";
    private static final String DATASOURCE_MAX_IDLE = "datasource.maxIdle";
    private static final String DATASOURCE_MAX_ACTIVE = "datasource.maxActive";
    private static final String DATASOURCE_MAX_WAIT = "datasource.maxWait";
    private static final String DATASOURCE_TIME_BETWEEN_EVICTION_RUNS_MILLIS = "datasource.timeBetweenEvictionRunsMillis";
    private static final String DATASOURCE_VALIDATION_QUERY = "datasource.validationQuery";
    private static final String DATASOURCE_TEST_ON_BORROW = "datasource.testOnBorrow";
    private static final String DATASOURCE_CONNECTION_PROPERTIES = "datasource.connectionProperties";
    private static final String WRITE_ENABLED = "datasource.writeAllowed";
    private static final String DATASOURCE_TEST_WHILE_IDLE = "datasource.testWhileIdle";
    private static final String AUTO_COMMIT = "datasource.auto-commit";

    @Value("#{'${lms.database.list}'.split(',')}")
    private List<String> dataSourceList;

    @Autowired
    private Environment environment;


    @Bean("dynamicDataSource")
    public DynamicDataSource dynamicDataSource(@Qualifier("dynamicDataSourceList") List<Pair<ShardInfo, org.apache.tomcat.jdbc.pool.DataSource>> targetDataSources) {
        Map<Object, Object> dataSourceMap = targetDataSources.stream().collect(Collectors.toMap(pair -> pair.getFirst().getName(), Pair::getSecond));
        return DynamicDataSource.builder().targetDataSources(dataSourceMap).build();
    }

    @Bean(name = "staticDataSource")
    public DataSource staticDataSource() {
        return getDataSource(SHARD_LOOKUP).getSecond();
    }

    @Bean
    public SqlSessionFactory staticSqlSessionFactory(@Qualifier("staticDataSource") DataSource staticDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(staticDataSource);
        return factoryBean.getObject();
    }
    /**
     * Create SqlSessionFactory from Data Source
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DynamicDataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dynamicDataSource);
        return factoryBean.getObject();
    }

    @Bean(name = "dynamicDataSourceList")
    public List<Pair<ShardInfo, org.apache.tomcat.jdbc.pool.DataSource>> getTableMap() {
        List<Pair<ShardInfo, org.apache.tomcat.jdbc.pool.DataSource>> datasourceList = new ArrayList<>();
        for(String database : dataSourceList) {
            if(!SHARD_LOOKUP.equalsIgnoreCase(database)) {
                datasourceList.add(getDataSource(database));
            }
        }
        datasourceList.add(getDataSource(SHARD_LOOKUP));
        return datasourceList;
    }


    //fc,fk -> 10,10
    private Pair<ShardInfo, org.apache.tomcat.jdbc.pool.DataSource> getDataSource(String shardName) {
        String shardProperty = shardName.concat(".");
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setUrl(getEnvironmentProperty(shardProperty + DATASOURCE_URL));
        dataSource.setDriverClassName(getEnvironmentProperty(shardProperty + DATASOURCE_DRIVER_CLASS_NAME));
        dataSource.setUsername(getEnvironmentProperty(shardProperty + DATASOURCE_USERNAME));
        dataSource.setPassword(getEnvironmentProperty(shardProperty + DATASOURCE_CREDENTIAL));
        dataSource.setInitialSize(Integer.parseInt(getEnvironmentProperty(shardProperty + DATASOURCE_INITIAL_SIZE)));
        dataSource.setMinIdle(Integer.parseInt(getEnvironmentProperty(shardProperty + DATASOURCE_MIN_IDLE)));
        dataSource.setMaxIdle(Integer.parseInt(getEnvironmentProperty(shardProperty + DATASOURCE_MAX_IDLE)));
        dataSource.setMaxActive(Integer.parseInt(getEnvironmentProperty(shardProperty + DATASOURCE_MAX_ACTIVE)));
        dataSource.setMaxWait(Integer.parseInt(getEnvironmentProperty(shardProperty + DATASOURCE_MAX_WAIT)));
        dataSource.setTimeBetweenEvictionRunsMillis(Integer.parseInt(getEnvironmentProperty(shardProperty + DATASOURCE_TIME_BETWEEN_EVICTION_RUNS_MILLIS)));
        dataSource.setValidationQuery(getEnvironmentProperty(shardProperty + DATASOURCE_VALIDATION_QUERY));
        dataSource.setTestOnBorrow(Boolean.valueOf(getEnvironmentProperty(shardProperty + DATASOURCE_TEST_ON_BORROW)));
        dataSource.setConnectionProperties(getEnvironmentProperty(shardProperty + DATASOURCE_CONNECTION_PROPERTIES));
        dataSource.setTestWhileIdle(Boolean.valueOf(getEnvironmentProperty(shardProperty + DATASOURCE_TEST_WHILE_IDLE)));
        dataSource.setDefaultAutoCommit(Boolean.valueOf(getEnvironmentProperty(shardProperty + AUTO_COMMIT)));
        return Pair.of(ShardInfo.builder().isWriteEnabled(Boolean.valueOf(getEnvironmentProperty(shardProperty + WRITE_ENABLED))).name(shardName).build(), dataSource);

    }

//    @Bean
//    @Primary
//    @Lazy
//    public PlatformTransactionManager transactionManager()
//    {
//        return new TransactionManager();
//    }


    @Bean(name = "DataSourceTransactionManagerList")
    public List<Pair<String,DataSourceTransactionManager>> txManager(@Qualifier("dynamicDataSourceList") List<Pair<ShardInfo, org.apache.tomcat.jdbc.pool.DataSource>> targetDataSources) {
        List<Pair<String,DataSourceTransactionManager>> pairs = new ArrayList<>();
        for(Pair<ShardInfo, org.apache.tomcat.jdbc.pool.DataSource> pair : targetDataSources)
        {
            pairs.add(Pair.of(pair.getFirst().getName(),new DataSourceTransactionManager(pair.getSecond())));
        }
        return pairs;
    }

    private String getEnvironmentProperty(String key) {
        String value = environment.getProperty(key);
        if(Objects.isNull(value)) {
            throw new NullPointerException("Key = " + key + " is not found");
        }
        return value;
    }

}
