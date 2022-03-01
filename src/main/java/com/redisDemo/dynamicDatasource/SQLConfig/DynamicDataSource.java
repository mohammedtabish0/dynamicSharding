package com.redisDemo.dynamicDatasource.SQLConfig;

import com.redisDemo.dynamicDatasource.DomainDataContextHolder;
import lombok.Builder;
import lombok.Getter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;
import java.util.Set;

@Builder
@Getter
public class DynamicDataSource extends AbstractRoutingDataSource {
    private Map<Object, Object> targetDataSources;

    public DynamicDataSource(Map<Object, Object> targetDataSources) {
        this.setTargetDataSources(targetDataSources);
    }

    protected Object determineCurrentLookupKey() {
        return DomainDataContextHolder.getSQLDataSourceType();
    }

    public Set<Object> getRegisteredShardList() {
        return targetDataSources.keySet();
    }
}
