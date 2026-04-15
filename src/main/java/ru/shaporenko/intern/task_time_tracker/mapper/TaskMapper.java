package ru.shaporenko.intern.task_time_tracker.mapper;

import org.apache.ibatis.annotations.*;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskCreateDto;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskUpdateDto;
import ru.shaporenko.intern.task_time_tracker.entity.Task;

@Mapper
public interface TaskMapper {

    @Insert("INSERT INTO task(title, description, status) " +
            "VALUES(#{title}, #{description}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Task create(TaskCreateDto task);

    @Update("UPDATE task " +
            "SET status=#{status} " +
            "WHERE id=#{id}")
    Task updateStatus(TaskUpdateDto task);

    @Delete("DELETE FROM task " +
            "WHERE id=#{id}")
    Task delete(@Param("id") Long id);

    @Select("SELECT *" +
            " FROM task " +
            "WHERE id=#{id}")
    Task findById(@Param("id") Long id);

    

}
