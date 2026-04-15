package ru.shaporenko.intern.task_time_tracker.dto.timeRecord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeRecordUpdateDto {

    private Long id;
    private LocalDateTime endTime;

}
