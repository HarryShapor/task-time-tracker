package ru.shaporenko.intern.task_time_tracker.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shaporenko.intern.task_time_tracker.dto.employee.EmployeeBriefResponse;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskBriefResponse;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordCreateDto;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordResponse;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordsResponse;
import ru.shaporenko.intern.task_time_tracker.entity.Employee;
import ru.shaporenko.intern.task_time_tracker.entity.Task;
import ru.shaporenko.intern.task_time_tracker.entity.TimeRecord;
import ru.shaporenko.intern.task_time_tracker.mapper.EmployeeMapper;
import ru.shaporenko.intern.task_time_tracker.mapper.TaskMapper;
import ru.shaporenko.intern.task_time_tracker.mapper.TimeRecordMapper;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TimeRecordService {

    private final TimeRecordMapper timeRecordMapper;
    private final TaskMapper taskMapper;
    private final EmployeeMapper employeeMapper;


    public TimeRecordResponse getTimeRecord(Long id) {
        return convertToResponse(timeRecordMapper.findById(id));
    }

    public TimeRecordsResponse getByEmployeeAndPeriod(Long employeeId, LocalDate start, LocalDate end) {
        return null;
    }

    public TimeRecordResponse createTimeRecord(TimeRecordCreateDto timeRecord) {
        return convertToResponse(timeRecordMapper.create(timeRecord));
    }

    public void deleteTimeRecord(Long id) {
        timeRecordMapper.delete(id);
    }

    private TimeRecordResponse convertToResponse(TimeRecord timeRecord) {
        Task task = taskMapper.findById(timeRecord.getTaskId());
        TaskBriefResponse taskBrief = new TaskBriefResponse(task.getId(), task.getTitle());

        Employee employee = employeeMapper.findById(timeRecord.getEmployeeId());
        EmployeeBriefResponse employeeBrief = new EmployeeBriefResponse(employee.getId(),
                employee.getFirstname(), employee.getLastname());

        TimeRecordResponse response = new TimeRecordResponse(timeRecord.getId(), employeeBrief, taskBrief,
                timeRecord.getStartTime(), timeRecord.getEndTime(), timeRecord.getComment());
        return response;
    }
}
