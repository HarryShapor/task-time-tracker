package ru.shaporenko.intern.task_time_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeRecordUpdate {

    private Long id;
    private LocalDateTime endTime;

}
