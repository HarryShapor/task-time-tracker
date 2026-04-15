package ru.shaporenko.intern.task_time_tracker.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.shaporenko.intern.task_time_tracker.entity.enums.TaskStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateDto {

    private Long id;
    private TaskStatus status;

}
