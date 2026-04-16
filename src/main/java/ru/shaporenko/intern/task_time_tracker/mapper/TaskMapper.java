package ru.shaporenko.intern.task_time_tracker.mapper;

import org.apache.ibatis.annotations.*;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskUpdateDto;
import ru.shaporenko.intern.task_time_tracker.entity.Task;

@Mapper
public interface TaskMapper {

    @Insert("INSERT INTO task(title, description, status) " +
            "VALUES(#{title}, #{description}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(Task task);

    @Update("UPDATE task " +
            "SET status=#{status} " +
            "WHERE id=#{id}")
    void updateStatus(TaskUpdateDto task);

    @Delete("DELETE FROM task " +
            "WHERE id=#{id}")
    void delete(@Param("id") Long id);

    @Select("SELECT *" +
            " FROM task " +
            "WHERE id=#{id}")
    Task findById(@Param("id") Long id);



}
