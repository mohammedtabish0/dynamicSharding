package com.redisDemo.dynamicDatasource.Service;

import com.redisDemo.dynamicDatasource.DomainDataContextHolder;
import com.redisDemo.dynamicDatasource.DomainDataInfo;
import com.redisDemo.dynamicDatasource.RedisConfig.CustomizeRedis;
import com.redisDemo.dynamicDatasource.SQLConfig.DataSourceConfig;
import com.redisDemo.dynamicDatasource.ShardInfoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShardService {
    @Autowired
    private IShardLookupDao shardLookupDao;


    public ShardInfoData getShardInfo(String domainLoanId) {
        DomainDataContextHolder.setDomainData(new DomainDataInfo(DataSourceConfig.SHARD_LOOKUP, CustomizeRedis.DEFAULT_REDIS));
        ShardInfoData shardInfoData= shardLookupDao.getShardNameByInstrumentId(domainLoanId);
        DomainDataContextHolder.resetDataSource();
        return  shardInfoData;
    }
}
