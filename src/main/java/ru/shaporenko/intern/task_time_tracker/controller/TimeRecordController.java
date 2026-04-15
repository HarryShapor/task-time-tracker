package ru.shaporenko.intern.task_time_tracker.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordCreateDto;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordResponse;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordsResponse;
import ru.shaporenko.intern.task_time_tracker.service.TimeRecordService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/time-record")
@RequiredArgsConstructor
public class TimeRecordController {

    private final TimeRecordService timeRecordService;

    @GetMapping("{id}")
    public ResponseEntity<TimeRecordResponse> getTimeRecord(@PathVariable("id") Long id){
        TimeRecordResponse response = timeRecordService.get(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping()
    public ResponseEntity<TimeRecordsResponse> getTimeRecords(
                @RequestParam Long employeeId,
                @RequestParam LocalDate start,
                @RequestParam LocalDate end) {
        TimeRecordsResponse response = timeRecordService.getByEmployeeAndPeriod(employeeId, start, end);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<TimeRecordResponse> createTimeRecord(
            @Valid @RequestBody TimeRecordCreateDto timeRecord){
        TimeRecordResponse response = timeRecordService.create(timeRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeRecord(@PathVariable Long id){
        timeRecordService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
