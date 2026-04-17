package ru.shaporenko.intern.task_time_tracker.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskUpdateDto;
import ru.shaporenko.intern.task_time_tracker.entity.Task;
import ru.shaporenko.intern.task_time_tracker.entity.enums.TaskStatus;
import ru.shaporenko.intern.task_time_tracker.mapper.TaskMapper;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class TaskMapperTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.sql.init.mode", () -> "always");
        registry.add("spring.sql.init.schema-locations", () -> "classpath:schema.sql");
    }

    @Autowired
    private TaskMapper taskMapper;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setTitle("Integration Test Task");
        testTask.setDescription("Test Description");
        testTask.setStatus(TaskStatus.NEW);
    }

    @Test
    void create_ShouldGenerateId() {
        taskMapper.create(testTask);

        assertNotNull(testTask.getId());
        assertTrue(testTask.getId() > 0);
    }

    @Test
    void findById_ShouldReturnTask_WhenExists() {
        taskMapper.create(testTask);
        Long generatedId = testTask.getId();

        Task found = taskMapper.findById(generatedId);

        assertNotNull(found);
        assertEquals("Integration Test Task", found.getTitle());
        assertEquals(TaskStatus.NEW, found.getStatus());
    }

    @Test
    void findById_ShouldReturnNull_WhenNotExists() {
        Task found = taskMapper.findById(999L);

        assertNull(found);
    }

    @Test
    void updateStatus_ShouldChangeStatus() {
        taskMapper.create(testTask);
        Long id = testTask.getId();

        taskMapper.updateStatus(new TaskUpdateDto(id, TaskStatus.IN_PROGRESS));

        Task updated = taskMapper.findById(id);
        assertEquals(TaskStatus.IN_PROGRESS, updated.getStatus());
    }

    @Test
    void delete_ShouldRemoveTask() {
        taskMapper.create(testTask);
        Long id = testTask.getId();
        assertNotNull(taskMapper.findById(id));

        taskMapper.delete(id);

        assertNull(taskMapper.findById(id));
    }
}
