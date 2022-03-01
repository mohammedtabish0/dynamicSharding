package com.redisDemo.dynamicDatasource;

import java.util.Stack;

public class DomainDataContextHolder {
    private static final ThreadLocal<Stack<DomainDataInfo>> CONTEXT_HOLDER = ThreadLocal.withInitial(Stack::new);

    public static void setDomainData(DomainDataInfo data){
        CONTEXT_HOLDER.get().push(data);
    }

    public static String getSQLDataSourceType() {
        System.out.println("using shard : "+ CONTEXT_HOLDER.get().peek().getShardName());
        return CONTEXT_HOLDER.get().peek().getShardName();
    }
    //MAP <KEY->name,value datasource

    public static String getRedisDataSourceType() {
        String redisName = CONTEXT_HOLDER.get().peek().getRedisName();
        return redisName == null? "default" : redisName;
    }

    public static void resetDataSource(){
        CONTEXT_HOLDER.get().pop();
    }

}
