package ru.shaporenko.intern.task_time_tracker.mapper;

import org.apache.ibatis.annotations.*;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordUpdateDto;
import ru.shaporenko.intern.task_time_tracker.entity.TimeRecord;

@Mapper
public interface TimeRecordMapper {

    @Insert("INSERT INTO time_record(employee_id, task_id, start_time, comment_task) " +
            "VALUES(#{employeeId}, #{taskId}, #{startTime}, #{comment})")
    void create(TimeRecord timeRecord);

    @Update("UPDATE time_record SET end_time=#{endTime} WHERE id=#{id}")
    void update(TimeRecordUpdateDto timeRecord);

    @Delete("DELETE FROM time_record WHERE id=#{id}")
    TimeRecord delete(@Param("id") Long id);

    @Select("SELECT * FROM time_record WHERE id=#{id}")
    TimeRecord findById(@Param("id") Long id);

}
