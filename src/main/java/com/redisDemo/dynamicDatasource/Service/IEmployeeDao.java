package com.redisDemo.dynamicDatasource.Service;

import com.redisDemo.dynamicDatasource.Data.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IEmployeeDao{

    @Select("SELECT `id`,`name`,`salary` from `employee_table` where id=#{id}")
    Employee getEmployeeDetails(Integer id);


    @Insert("INSERT INTO `employee_table`" +
            "(id,name,salary) " +
            "VALUES(#{id},#{name},#{salary})")
     int setEmployeeDetails(Employee employee);
}
