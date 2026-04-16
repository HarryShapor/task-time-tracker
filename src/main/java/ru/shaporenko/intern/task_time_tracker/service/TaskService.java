package ru.shaporenko.intern.task_time_tracker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskCreateDto;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskResponse;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskUpdateDto;
import ru.shaporenko.intern.task_time_tracker.entity.Task;
import ru.shaporenko.intern.task_time_tracker.entity.enums.TaskStatus;
import ru.shaporenko.intern.task_time_tracker.mapper.TaskMapper;

import java.security.InvalidParameterException;

@Service
public class TaskService {

    private final TaskMapper taskMapper;

    public TaskService(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Transactional(readOnly = true)
    public TaskResponse getById(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidParameterException("Invalid task id: " + id);
        }

        Task task = taskMapper.findById(id);
        if (task == null){
            throw new RuntimeException("Task not found with id: " + id);
        }
        return convertToResponse(task);
    }

    @Transactional
    public TaskResponse createTask(TaskCreateDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());

        taskMapper.create(task);

        return convertToResponse(task);
    }

    @Transactional
    public TaskResponse updateStatus(Long id, TaskStatus status) {
        if (id == null || id <= 0) {
            throw new InvalidParameterException("Invalid task id: " + id);
        }

        if (status == null) {
            throw new InvalidParameterException("Status cannot be null");
        }

        Task existingTask = taskMapper.findById(id);
        if (existingTask == null) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        if (existingTask.getStatus() == status) {
            return convertToResponse(existingTask);
        }

        TaskUpdateDto taskUpdate = new TaskUpdateDto(id, status);
        taskMapper.updateStatus(taskUpdate);

        return convertToResponse(taskMapper.findById(id));
    }

    @Transactional
    public void deleteTask(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidParameterException("Invalid task id: " + id);
        }

        if (taskMapper.findById(id) == null) {
            throw new RuntimeException("Cannot delete. Task not found with id: " + id);
        }

        taskMapper.delete(id);
    }

    private TaskResponse convertToResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus());
    }
}
