package ru.shaporenko.intern.task_time_tracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaporenko.intern.task_time_tracker.dto.employee.EmployeeBriefResponse;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskBriefResponse;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordCreateDto;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordResponse;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordsResponse;
import ru.shaporenko.intern.task_time_tracker.entity.Employee;
import ru.shaporenko.intern.task_time_tracker.entity.Task;
import ru.shaporenko.intern.task_time_tracker.entity.TimeRecord;
import ru.shaporenko.intern.task_time_tracker.exception.ResourceNotFoundException;
import ru.shaporenko.intern.task_time_tracker.mapper.EmployeeMapper;
import ru.shaporenko.intern.task_time_tracker.mapper.TaskMapper;
import ru.shaporenko.intern.task_time_tracker.mapper.TimeRecordMapper;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeRecordService {

    private final TimeRecordMapper timeRecordMapper;
    private final TaskMapper taskMapper;
    private final EmployeeMapper employeeMapper;

    @Transactional(readOnly = true)
    public TimeRecordResponse getTimeRecord(Long id) {

        if (id == null || id <= 0) {
            throw new InvalidParameterException("Invalid time record id: " + id);
        }

        TimeRecord timeRecord = timeRecordMapper.findById(id);
        if (timeRecord == null) {
            throw new ResourceNotFoundException("TimeRecord not found with id: " + id);
        }

        return convertToResponse(timeRecord);
    }

    @Transactional(readOnly = true)
    public TimeRecordsResponse getByEmployeeAndPeriod(Long employeeId,
                                                      LocalDate start, LocalDate end) {
        if (employeeId == null || employeeId <= 0) {
            throw new InvalidParameterException("Invalid employee id: " + employeeId);
        }
        if (start == null || end == null) {
            throw new InvalidParameterException("Start date and end date cannot be null");
        }
        if (start.isAfter(end)) {
            throw new InvalidParameterException("Start date must be before or equal end date");
        }

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        List<TimeRecordResponse> timeRecords =
                timeRecordMapper.findByEmployeeAndPeriodResponse(employeeId, startDateTime, endDateTime);

        if (timeRecords.isEmpty()) {
            return new TimeRecordsResponse(BigDecimal.ZERO, 0, List.of());
        }

        BigDecimal totalHours = BigDecimal.valueOf(timeRecords.stream()
                .filter(r -> r.getStartTime() != null && r.getEndTime() != null)
                .mapToDouble(r -> Duration.between(r.getStartTime(), r.getEndTime()).toHours())
                .sum());

        return new TimeRecordsResponse(totalHours, timeRecords.size(), timeRecords);
    }

    @Transactional
    public TimeRecordResponse createTimeRecord(TimeRecordCreateDto timeRecordDto) {

        Task task = taskMapper.findById(timeRecordDto.getTaskId());
        if (task == null) {
            throw new ResourceNotFoundException("Task not found with id: " + timeRecordDto.getTaskId());
        }

        Employee employee = employeeMapper.findById(timeRecordDto.getEmployeeId());
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found with id: " + timeRecordDto.getEmployeeId());
        }

        if (timeRecordDto.getStartTime() == null) {
            throw new InvalidParameterException("Start time cannot be null");
        }

        if (timeRecordDto.getEndTime() != null
                && timeRecordDto.getStartTime().isAfter(timeRecordDto.getEndTime())) {
            throw new InvalidParameterException("Start time must be before end time");
        }

        TimeRecord timeRecord = new TimeRecord();
        timeRecord.setEmployeeId(timeRecordDto.getEmployeeId());
        timeRecord.setTaskId(timeRecordDto.getTaskId());
        timeRecord.setStartTime(timeRecordDto.getStartTime());
        timeRecord.setEndTime(timeRecordDto.getEndTime());
        timeRecord.setComment(timeRecordDto.getComment());

        timeRecordMapper.create(timeRecord);

        return convertToResponse(timeRecord);
    }

    @Transactional
    public void deleteTimeRecord(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidParameterException("Invalid time record id: " + id);
        }

        if (timeRecordMapper.findById(id) == null){
            throw new ResourceNotFoundException("Cannot delete. TimeRecord not found with id: " + id);

        }

        timeRecordMapper.delete(id);
    }

    private TimeRecordResponse convertToResponse(TimeRecord timeRecord) {
        Task task = taskMapper.findById(timeRecord.getTaskId());
        if (task == null){
            throw new ResourceNotFoundException("Task not found with id: " + timeRecord.getTaskId());
        }
        TaskBriefResponse taskBrief = new TaskBriefResponse(task.getId(), task.getTitle());

        Employee employee = employeeMapper.findById(timeRecord.getEmployeeId());
        if (employee == null){
            throw new ResourceNotFoundException("Employee not found with id: " + timeRecord.getEmployeeId());
        }
        EmployeeBriefResponse employeeBrief = new EmployeeBriefResponse(employee.getId(),
                employee.getFirstname(), employee.getLastname());

        return new TimeRecordResponse(timeRecord.getId(), employeeBrief, taskBrief,
                timeRecord.getStartTime(), timeRecord.getEndTime(), timeRecord.getComment());
    }
}
