package ru.shaporenko.intern.task_time_tracker.dto.timeRecord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeRecordsResponse {

    private BigDecimal totalHours;
    private Integer totalRecords;
    private List<TimeRecordResponse> records;
}