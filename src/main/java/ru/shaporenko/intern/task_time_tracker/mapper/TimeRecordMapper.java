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

    @Insert("INSERT INTO time_record(employee_id, task_id, start_time, end_time, comment_task) " +
            "VALUES(#{employeeId}, #{taskId}, #{startTime}, #{endTime}, #{comment})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(TimeRecord timeRecord);

    @Update("UPDATE time_record SET end_time=#{endTime} WHERE id=#{id}")
    void update(TimeRecordUpdateDto timeRecord);

    @Delete("DELETE FROM time_record WHERE id=#{id}")
    void delete(@Param("id") Long id);

    @Select("SELECT " +
            "id, " +
            "employee_id as employeeId, " +
            "task_id as taskId, " +
            "start_time as startTime, " +
            "end_time as endTime, " +
            "comment_task as comment " +
            "FROM time_record WHERE id = #{id}")
    TimeRecord findById(@Param("id") Long id);

    @Select("SELECT * FROM time_record WHERE employee_id=#{employeeId} " +
            "AND start_time BETWEEN #{start} AND #{end}")
    List<TimeRecord> findByEmployeeAndPeriod(@Param("employeeId") Long employeeId,
                                             @Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);

    @Select("SELECT tr.id as id, tr.start_time as startTime, " +
            "tr.end_time as endTime, tr.comment_task as comment, " +
            "t.id as task_id, t.title as task_title, " +
            "e.id as employee_id, e.firstname as employee_firstname, " +
            "e.lastname as employee_lastname " +
            "FROM time_record tr " +
            "JOIN task t ON tr.task_id = t.id " +
            "JOIN employee e ON tr.employee_id = e.id " +
            "WHERE tr.employee_id = #{employeeId} AND tr.start_time BETWEEN #{start} AND #{end}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "startTime", column = "startTime"),
            @Result(property = "endTime", column = "endTime"),
            @Result(property = "comment", column = "comment"),
            @Result(property = "task.id", column = "task_id"),
            @Result(property = "task.title", column = "task_title"),
            @Result(property = "employee.id", column = "employee_id"),
            @Result(property = "employee.firstname", column = "employee_firstname"),
            @Result(property = "employee.lastname", column = "employee_lastname")
    })
    List<TimeRecordResponse> findByEmployeeAndPeriodResponse(@Param("employeeId") Long employeeId,
                                                     @Param("start") LocalDateTime start,
                                                     @Param("end") LocalDateTime end);

}
