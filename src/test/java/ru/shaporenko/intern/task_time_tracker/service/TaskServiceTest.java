package ru.shaporenko.intern.task_time_tracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskCreateDto;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskResponse;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskUpdateDto;
import ru.shaporenko.intern.task_time_tracker.entity.Task;
import ru.shaporenko.intern.task_time_tracker.entity.enums.TaskStatus;
import ru.shaporenko.intern.task_time_tracker.mapper.TaskMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    private final Long ID = 1L;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getById_ShouldReturnTaskResponse_WhenTaskExists(){

        Task mockTask = new Task();
        mockTask.setId(ID);
        mockTask.setTitle("Test Title");
        mockTask.setDescription("Test Description");
        mockTask.setStatus(TaskStatus.NEW);

        when(taskMapper.findById(ID)).thenReturn(mockTask);

        TaskResponse response = taskService.getById(ID);

        assertNotNull(response);
        assertEquals(response.getId(), ID);
        assertEquals(response.getTitle(), "Test Title");
        assertEquals(response.getDescription(), "Test Description");
        assertEquals(response.getStatus(), TaskStatus.NEW);
        verify(taskMapper, times(1)).findById(ID);
    }


    @Test
    void createTask_ShouldCreateAndReturnTaskResponse_WhenValidDataProvided(){

        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("New task");
        dto.setDescription("Description");
        dto.setStatus(TaskStatus.NEW);

        Task taskToCreate = new Task();
        taskToCreate.setTitle(dto.getTitle());
        taskToCreate.setDescription(dto.getDescription());
        taskToCreate.setStatus(dto.getStatus());

        doAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            t.setId(ID);
            return null;
        }).when(taskMapper).create(any(Task.class));

        TaskResponse response = taskService.createTask(dto);

        assertNotNull(response);
        assertEquals(response.getId(), ID);
        assertEquals(response.getTitle(), "New task");
        assertEquals(response.getDescription(), "Description");
        assertEquals(response.getStatus(), TaskStatus.NEW);
        verify(taskMapper, times(1)).create(any(Task.class));

    }

    @Test
    void updateStatus_ShouldUpdateAndReturnTaskResponse_WhenTaskExistsAndDifferentStatus(){

        TaskStatus newStatus = TaskStatus.DONE;

        Task existingTask = new Task();
        existingTask.setId(ID);
        existingTask.setTitle("Test Title");
        existingTask.setDescription("Test Description");
        existingTask.setStatus(TaskStatus.NEW);

        Task updatedTask = new Task();
        updatedTask.setId(ID);
        updatedTask.setTitle("Test Title");
        updatedTask.setDescription("Test Description");
        updatedTask.setStatus(newStatus);

        when(taskMapper.findById(ID)).thenReturn(existingTask);
        when(taskMapper.updateStatus(any(TaskUpdateDto.class))).thenReturn(updatedTask);

        TaskResponse response = taskService.updateStatus(ID, newStatus);

        assertNotNull(response);
        assertEquals(response.getId(), ID);
        assertEquals(response.getTitle(), "Test Title");
        assertEquals(response.getDescription(), "Test Description");
        assertEquals(response.getStatus(), TaskStatus.DONE);

        verify(taskMapper, times(1)).findById(ID);
        verify(taskMapper, times(1)).updateStatus(any(TaskUpdateDto.class));
    }


    @Test
    void deleteTask_ShouldDeleteTask_WhenTaskExists(){

        Task existingTask = new Task();
        existingTask.setId(ID);
        existingTask.setTitle("Task to Delete");

        when(taskMapper.findById(ID)).thenReturn(existingTask);
        when(taskMapper.delete(ID)).thenReturn(existingTask);

        taskService.deleteTask(ID);

        verify(taskMapper, times(1)).findById(ID);
        verify(taskMapper, times(1)).delete(ID);

    }

}
