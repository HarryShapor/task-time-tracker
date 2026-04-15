package ru.shaporenko.intern.task_time_tracker.controller;


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
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id){
        TaskResponse response = taskService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateDto task){
        TaskResponse response = taskService.create(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateStatus(@PathVariable Long id,
                                                     @RequestParam TaskStatus status)   {
        TaskResponse response = taskService.updateStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
