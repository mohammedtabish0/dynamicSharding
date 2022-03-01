package com.redisDemo.dynamicDatasource;


import com.redisDemo.dynamicDatasource.Data.Employee;
import com.redisDemo.dynamicDatasource.Service.IEmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class SubService {
    @Autowired
    private IEmployeeDao employeeDao;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Transactional(rollbackFor = Exception.class)
    public void setTransactional(String domainId) throws Exception {
        System.out.println(TransactionSynchronizationManager.isActualTransactionActive());
        Employee emp1 = new Employee(5,"Temp1","25000");
        employeeDao.setEmployeeDetails(emp1);
        redisTemplate.opsForValue().set("random1","200");
        Employee emp2 = new Employee(6,"Temp2","4500");
        employeeDao.setEmployeeDetails(emp2);
        redisTemplate.opsForValue().set("random2","300");
        System.out.println("Inserted data in redis random 1");
        if(emp1.getId()==5)
            throw new Exception();
        Employee emp3 = new Employee(7,"Temp3","10320");
        redisTemplate.opsForValue().set("random3","400");
        employeeDao.setEmployeeDetails(emp3);
    }
}
