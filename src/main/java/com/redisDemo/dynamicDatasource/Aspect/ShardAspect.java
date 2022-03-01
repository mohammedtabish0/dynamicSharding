package com.redisDemo.dynamicDatasource.Aspect;

import com.redisDemo.dynamicDatasource.DomainDataContextHolder;
import com.redisDemo.dynamicDatasource.DomainDataInfo;
import com.redisDemo.dynamicDatasource.Service.ShardService;
import com.redisDemo.dynamicDatasource.ShardInfoData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class ShardAspect {


    @Autowired
    private ShardService shardService;

    @Around("@annotation(Sharded)")
    public Object sharded(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        Sharded myAnnotation = method.getAnnotation(Sharded.class);
        return processWithSharding(joinPoint,myAnnotation.isWritable());
    }

    private Object processWithSharding(ProceedingJoinPoint joinPoint,boolean writable) throws Throwable {
        ShardInfoData shardInfoData = shardService.getShardInfo((String) joinPoint.getArgs()[0]);
        //other business logic like assigning a shard and all
        // random write shard -> properties -> write -> read -> redis
        DomainDataInfo domainDataInfo = new DomainDataInfo();
        domainDataInfo.setShardName(writable?shardInfoData.getWriteShardName():shardInfoData.getReadReplicaProxyName());
        domainDataInfo.setRedisName(shardInfoData.getRedisName());
        DomainDataContextHolder.setDomainData(domainDataInfo);
        Object response = joinPoint.proceed();
        DomainDataContextHolder.resetDataSource();
        return response;
    }
}
