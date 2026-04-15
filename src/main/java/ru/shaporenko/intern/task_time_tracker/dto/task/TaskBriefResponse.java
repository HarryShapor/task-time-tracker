package ru.shaporenko.intern.task_time_tracker.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskBriefResponse {

    private Long id;
    private String title;

}
