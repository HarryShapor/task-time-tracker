package ru.shaporenko.intern.task_time_tracker.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBriefResponse {

    private Long id;
    private String firstname;
    private String lastname;

}
