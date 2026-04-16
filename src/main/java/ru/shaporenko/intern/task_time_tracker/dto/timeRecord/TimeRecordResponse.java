package ru.shaporenko.intern.task_time_tracker.dto.timeRecord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.shaporenko.intern.task_time_tracker.dto.employee.EmployeeBriefResponse;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskBriefResponse;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeRecordResponse {

    private Long id;
    private EmployeeBriefResponse employee;
    private TaskBriefResponse task;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String comment;

}
