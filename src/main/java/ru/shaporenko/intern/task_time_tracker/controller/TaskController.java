package ru.shaporenko.intern.task_time_tracker.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskCreateDto;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskResponse;
import ru.shaporenko.intern.task_time_tracker.entity.enums.TaskStatus;
import ru.shaporenko.intern.task_time_tracker.service.TaskService;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor

@Tag(name = "Task Controller", description = "API for managing tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Returns a single task by it's ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content)
    })
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id){
        TaskResponse response = taskService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    @Operation(summary = "Create new task", description = "Creates a new task with status NEW")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateDto task){
        TaskResponse response = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update task status", description = "Changes the status of an existing task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status value",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content)
    })
    public ResponseEntity<TaskResponse> updateStatus(@PathVariable Long id,
                                                     @RequestParam TaskStatus status)   {
        TaskResponse response = taskService.updateStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Deletes a task by it's ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
