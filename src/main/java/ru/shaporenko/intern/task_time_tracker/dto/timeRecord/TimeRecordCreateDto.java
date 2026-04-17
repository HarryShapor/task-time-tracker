package ru.shaporenko.intern.task_time_tracker.dto.timeRecord;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeRecordCreateDto {

    @NotBlank(message = "Employee ID is required")
    @Positive(message = "Employee ID must be positive")
    private Long employeeId;

    @NotBlank(message = "Task ID is required")
    @Positive(message = "Task ID must be positive")
    private Long taskId;

    @NotNull(message = "Start time is required")
    @Past(message = "Start time can be only in the past")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @PastOrPresent(message = "End time must be in the past or present")
    private LocalDateTime endTime;

    @Size(max = 500, message = "Comment must be less than 500 characters")
    private String comment;

}


