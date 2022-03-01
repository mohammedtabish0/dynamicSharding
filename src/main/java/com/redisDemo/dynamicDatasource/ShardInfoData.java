package com.redisDemo.dynamicDatasource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShardInfoData {
    private String writeShardName;
    private String readReplicaProxyName;
    private String redisName;
}
