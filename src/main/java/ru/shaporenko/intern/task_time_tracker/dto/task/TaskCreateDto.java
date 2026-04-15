package ru.shaporenko.intern.task_time_tracker.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.shaporenko.intern.task_time_tracker.entity.enums.TaskStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDto {

    private String title;
    private String description;
    private TaskStatus status = TaskStatus.NEW;
}
