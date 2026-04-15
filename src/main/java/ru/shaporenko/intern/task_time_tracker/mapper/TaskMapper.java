package ru.shaporenko.intern.task_time_tracker.mapper;

import org.apache.ibatis.annotations.*;
import ru.shaporenko.intern.task_time_tracker.dto.TaskUpdate;
import ru.shaporenko.intern.task_time_tracker.entity.Task;

@Mapper
public interface TaskMapper {

    @Insert("INSERT INTO task(title, description, status) VALUES(#{title}, #{description}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(Task task);

    @Update("UPDATE task SET status=#{status} WHERE id=#{id}")
    void updateStatus(TaskUpdate task);

    @Delete("DELETE FROM task WHERE id=#{id}")
    Task delete(@Param("id") Long id);

    @Select("SELECT * FROM task WHERE id=#{id}")
    Task findById(@Param("id") Long id);

}
