package com.redisDemo.dynamicDatasource.RedisConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CustomizeRedis {
    public static final String DEFAULT_REDIS = "default";

    @Bean
    public MultiRedisJedisConnectionFactory multiRedisLettuceConnectionFactory() {
        Map<String, JedisConnectionFactory> connectionFactoryMap = new HashMap<>();

        String host1 = "127.0.0.1";
        int port1 = 3000;
        RedisStandaloneConfiguration rs1 = new RedisStandaloneConfiguration(host1, port1);
        connectionFactoryMap.put("redis1",new JedisConnectionFactory(rs1));

        String host2 = "127.0.0.1";
        int port2 = 3001;
        RedisStandaloneConfiguration rs2 = new RedisStandaloneConfiguration(host2, port2);
        connectionFactoryMap.put("redis2",new JedisConnectionFactory(rs2));


        String host3 = "127.0.0.1";
        int port3 = 3002;
        RedisStandaloneConfiguration rs3 = new RedisStandaloneConfiguration(host3, port3);
        connectionFactoryMap.put(CustomizeRedis.DEFAULT_REDIS,new JedisConnectionFactory(rs3));
        return new MultiRedisJedisConnectionFactory(connectionFactoryMap);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(){
        RedisTemplate<String, String> template = new RedisTemplate<>();
        RedisSerializer<?> redisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setKeySerializer(redisSerializer);
        template.setValueSerializer(redisSerializer);
        template.setConnectionFactory(multiRedisLettuceConnectionFactory());
        template.setEnableTransactionSupport(true);
        return template;
    }

}
