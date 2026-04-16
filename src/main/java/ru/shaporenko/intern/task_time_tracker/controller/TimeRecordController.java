package ru.shaporenko.intern.task_time_tracker.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get time record by ID", description = "Returns a single time record by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Time record found",
                    content = @Content(schema = @Schema(implementation = TimeRecordResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID format",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Time record not found",
                    content = @Content)

    })
    public ResponseEntity<TimeRecordResponse> getTimeRecord(@PathVariable("id") Long id){
        TimeRecordResponse response = timeRecordService.getTimeRecord(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping()
    @Operation(summary = "Get time records by employee and period",
            description = "Returns all time records for a specific employee within a date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Time records found (may be empty)",
                    content = @Content(schema = @Schema(implementation = TimeRecordsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters (employeeId, start date, end date)",
                    content = @Content)
    })
    public ResponseEntity<TimeRecordsResponse> getTimeRecords(
                @RequestParam Long employeeId,
                @RequestParam LocalDate start,
                @RequestParam LocalDate end) {
        TimeRecordsResponse response = timeRecordService.getByEmployeeAndPeriod(employeeId, start, end);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    @Operation(summary = "Create time record", description = "Creates a new time record for a task and employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Time record created successfully",
                    content = @Content(schema = @Schema(implementation = TimeRecordResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data (missing fields, wrong time format)",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Task or employee not found",
                    content = @Content)
    })
    public ResponseEntity<TimeRecordResponse> createTimeRecord(
            @Valid @RequestBody TimeRecordCreateDto timeRecord){
        TimeRecordResponse response = timeRecordService.createTimeRecord(timeRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete time record", description = "Deletes a time record by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Time record deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID format",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Time record not found",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteTimeRecord(@PathVariable Long id){
        timeRecordService.deleteTimeRecord(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
