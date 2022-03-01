package com.redisDemo.dynamicDatasource.Service;

import com.redisDemo.dynamicDatasource.Aspect.Sharded;
import com.redisDemo.dynamicDatasource.ShardInfoData;
import org.apache.ibatis.annotations.*;

@Mapper
public interface IShardLookupDao
{
    @Select("select b.write_shard_name,b.read_replica_proxy_name,b.redis_name from  (SELECT shard_name FROM lookup_table where domain_loan_id=#{domain_loan_id}) as a " +
            " join " +
            "(SELECT write_shard_name,read_replica_proxy_name,redis_name FROM shard_info) as b " +
            " ON a.shard_name=write_shard_name")
    @Results({
            @Result(property = "writeShardName", column = "write_shard_name"),
            @Result(property = "readReplicaProxyName", column = "read_replica_proxy_name"),
            @Result(property = "redisName", column = "redis_name")
    })
    ShardInfoData getShardNameByInstrumentId(@Param("domain_loan_id") String domainLoanId);

    @Select("SELECT shard_name FROM lookup_table where domain_loan_id=#{domain_loan_id}")
    String getShardByInstrumentId(@Param("domain_loan_id") String domainLoanId);

    @Select("select * from shard_info where write_shard_name=#{shard_name}")
    Object getShardInfoData(@Param("shard_name") String shardName);
}
