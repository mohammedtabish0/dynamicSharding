package com.redisDemo.dynamicDatasource.RedisConfig;

import com.redisDemo.dynamicDatasource.DomainDataContextHolder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.util.Map;

public class MultiRedisJedisConnectionFactory
        implements InitializingBean, DisposableBean, RedisConnectionFactory {
    private final Map<String, JedisConnectionFactory> connectionFactoryMap;


    public MultiRedisJedisConnectionFactory(Map<String, JedisConnectionFactory> connectionFactoryMap) {
        this.connectionFactoryMap = connectionFactoryMap;
    }

    @Override
    public void destroy() throws Exception {
        connectionFactoryMap.values().forEach(JedisConnectionFactory::destroy);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        connectionFactoryMap.values().forEach(JedisConnectionFactory::afterPropertiesSet);
    }

    private JedisConnectionFactory currentLettuceConnectionFactory() {
        String currentRedis = DomainDataContextHolder.getRedisDataSourceType();
        System.out.println("Current redis selected is :" + currentRedis);
        return currentRedis==null?connectionFactoryMap.get("default"):connectionFactoryMap.get(currentRedis);
    }

    @Override
    public RedisConnection getConnection() {
        return currentLettuceConnectionFactory().getConnection();
    }

    @Override
    public RedisClusterConnection getClusterConnection() {
        return currentLettuceConnectionFactory().getClusterConnection();
    }

    @Override
    public boolean getConvertPipelineAndTxResults() {
        return currentLettuceConnectionFactory().getConvertPipelineAndTxResults();
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return currentLettuceConnectionFactory().getSentinelConnection();
    }

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return currentLettuceConnectionFactory().translateExceptionIfPossible(ex);
    }
}
