package com.redisDemo.dynamicDatasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//
//@Configuration
//public class CacheConfig {
//
//
//    @Bean
//    public JedisConnectionFactory jedisConnectionFactory(){
//        String host = "redis-13981.c278.us-east-1-4.ec2.cloud.redislabs.com";
//        int port = 13981;
//        RedisStandaloneConfiguration rs = new RedisStandaloneConfiguration(host, port);
//        rs.setPassword("root");
//        return new JedisConnectionFactory(rs);
//    }
//
//    @Bean
//    public RedisTemplate<String, String> redisTemplate(){
//        RedisTemplate<String, String> template = new RedisTemplate<>();
//        template.setConnectionFactory(jedisConnectionFactory());
//        return template;w
//    }
//
//}
