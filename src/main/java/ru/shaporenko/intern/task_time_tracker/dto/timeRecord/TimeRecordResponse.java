package ru.shaporenko.intern.task_time_tracker.dto.timeRecord;

import ru.shaporenko.intern.task_time_tracker.dto.employee.EmployeeBriefResponse;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskBriefResponse;

import java.time.LocalDateTime;

public class TimeRecordResponse {

    private Long id;
    private EmployeeBriefResponse employee;
    private TaskBriefResponse task;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String comment;

}
