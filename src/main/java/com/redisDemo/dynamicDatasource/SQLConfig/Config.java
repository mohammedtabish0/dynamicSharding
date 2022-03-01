package com.redisDemo.dynamicDatasource.SQLConfig;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rahul.kumar
 */
@Configuration
public class Config {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer bean = new MapperScannerConfigurer();
        bean.setBasePackage("com.redisDemo.dynamicDatasource");
        bean.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return bean;
    }
}
