package com.redisDemo.dynamicDatasource;

import com.redisDemo.dynamicDatasource.Data.DomainWithId;
import com.redisDemo.dynamicDatasource.Data.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    @Autowired
    ShardingService shardingService;

    @PutMapping("/employee/{domainLoanId}")
    public String setEmployeeData(@PathVariable String domainLoanId, @RequestBody Employee employee) throws Exception {
        shardingService.testWriteShard(domainLoanId,employee);
        return "Success";
    }

    @PostMapping("/employee")
    public Employee getEmployeeData(@RequestBody DomainWithId domainWithId){
        return shardingService.testReadShard(domainWithId.domainLoanId, domainWithId.id);
    }


}

