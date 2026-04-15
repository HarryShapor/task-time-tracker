package ru.shaporenko.intern.task_time_tracker.service;

import org.springframework.stereotype.Service;
import ru.shaporenko.intern.task_time_tracker.entity.Task;
import ru.shaporenko.intern.task_time_tracker.mapper.TaskMapper;

@Service
public class TaskService {

    private final TaskMapper taskMapper;

    public TaskService(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public void create(Task task){
        taskMapper.create(task);
    }
}
