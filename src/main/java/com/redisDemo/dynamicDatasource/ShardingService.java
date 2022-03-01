package com.redisDemo.dynamicDatasource;

import com.redisDemo.dynamicDatasource.Aspect.Sharded;
import com.redisDemo.dynamicDatasource.Data.Employee;
import com.redisDemo.dynamicDatasource.Service.IEmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShardingService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private SubService subService;

    @Autowired
    private IEmployeeDao employeeDao;

    @Sharded
    public void testWriteShard(String domainLoanId,Employee employee) throws Exception {
        subService.setTransactional(domainLoanId);
    }

    @Sharded(isWritable = false)
    public Employee testReadShard(String domainLoanId,int id){
        return employeeDao.getEmployeeDetails(id);
    }

}
