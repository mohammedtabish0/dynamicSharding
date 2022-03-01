package com.redisDemo.dynamicDatasource.SQLConfig;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ShardInfo {
    private String name;
    private boolean isWriteEnabled;
}
