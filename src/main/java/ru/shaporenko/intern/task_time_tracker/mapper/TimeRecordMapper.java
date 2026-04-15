package ru.shaporenko.intern.task_time_tracker.mapper;

import org.apache.ibatis.annotations.*;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordCreateDto;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordResponse;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordUpdateDto;
import ru.shaporenko.intern.task_time_tracker.entity.TimeRecord;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TimeRecordMapper {

    @Insert("INSERT INTO time_record(employee_id, task_id, start_time, comment_task) " +
            "VALUES(#{employeeId}, #{taskId}, #{startTime}, #{comment})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(TimeRecord timeRecord);

    @Update("UPDATE time_record SET end_time=#{endTime} WHERE id=#{id}")
    void update(TimeRecordUpdateDto timeRecord);

    @Delete("DELETE FROM time_record WHERE id=#{id}")
    TimeRecord delete(@Param("id") Long id);

    @Select("SELECT * FROM time_record WHERE id=#{id}")
    TimeRecord findById(@Param("id") Long id);

    @Select("SELECT * FROM time_record WHERE employee_id=#{employeeId} " +
            "AND start_time BETWEEN #{start} AND #{end}")
    List<TimeRecord> findByEmployeeAndPeriod(@Param("employeeId") Long employeeId,
                                             @Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);

    @Select("SELECT tr.id, tr.start_time, tr.end_time, tr.comment, " +
            "t.id as task_id, t.title as task_title, " +
            "e.id as employee_id, e.firstname, e.lastname " +
            "FROM time_record tr " +
            "JOIN task t ON tr.task_id = t.id " +
            "JOIN employee e ON tr.employee_id = e.id " +
            "WHERE tr.employee_id = #{employeeId} AND tr.start_time BETWEEN #{start} AND #{end}")
    List<TimeRecordResponse> findByEmployeeAndPeriodResponse(@Param("employeeId") Long employeeId,
                                                     @Param("start") LocalDateTime start,
                                                     @Param("end") LocalDateTime end);

}
