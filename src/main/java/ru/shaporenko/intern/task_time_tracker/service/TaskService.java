package ru.shaporenko.intern.task_time_tracker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskCreateDto;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskResponse;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskUpdateDto;
import ru.shaporenko.intern.task_time_tracker.entity.Task;
import ru.shaporenko.intern.task_time_tracker.entity.enums.TaskStatus;
import ru.shaporenko.intern.task_time_tracker.mapper.TaskMapper;

@Service
public class TaskService {

    private final TaskMapper taskMapper;

    public TaskService(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Transactional(readOnly = true)
    public TaskResponse getById(Long id) {
        Task task = taskMapper.findById(id);
        return convertToResponse(task);
    }

    @Transactional
    public TaskResponse createTask(TaskCreateDto task) {
        return convertToResponse(taskMapper.create(task));
    }

    public TaskResponse updateStatus(Long id, TaskStatus status) {
        TaskUpdateDto taskUpdate = new TaskUpdateDto(id, status);
        return convertToResponse(taskMapper.updateStatus(taskUpdate));
    }

    @Transactional
    public void deleteTask(Long id) {
        taskMapper.delete(id);
    }

    private TaskResponse convertToResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus());
    }
}
