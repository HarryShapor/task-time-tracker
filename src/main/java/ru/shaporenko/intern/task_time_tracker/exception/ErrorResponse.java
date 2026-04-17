package ru.shaporenko.intern.task_time_tracker.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private String status;
    private String message;
    private LocalDateTime timestamp;

}
