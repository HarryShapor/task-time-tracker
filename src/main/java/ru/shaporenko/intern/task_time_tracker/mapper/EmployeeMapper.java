package ru.shaporenko.intern.task_time_tracker.mapper;


import org.apache.ibatis.annotations.*;
import ru.shaporenko.intern.task_time_tracker.entity.Employee;

@Mapper
public interface EmployeeMapper {

    @Insert("INSERT INTO employee(firstname, lastname, middlename)" +
            "VALUES(#{firstname}, #{lastname}, #{middlename})")
    void create(Employee employee);

    @Delete("DELETE FROM employee WHERE id=#{id}")
    void delete(@Param("id") Long id);

    @Select("SELECT * FROM employee WHERE id=#{id}")
    Employee findById(@Param("id") Long id);
}
