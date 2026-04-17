package ru.shaporenko.intern.task_time_tracker.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordResponse;
import ru.shaporenko.intern.task_time_tracker.entity.Employee;
import ru.shaporenko.intern.task_time_tracker.entity.Task;
import ru.shaporenko.intern.task_time_tracker.entity.TimeRecord;
import ru.shaporenko.intern.task_time_tracker.entity.enums.TaskStatus;
import ru.shaporenko.intern.task_time_tracker.mapper.EmployeeMapper;
import ru.shaporenko.intern.task_time_tracker.mapper.TaskMapper;
import ru.shaporenko.intern.task_time_tracker.mapper.TimeRecordMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class TimeRecordMapperTest {

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
    }

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private TimeRecordMapper timeRecordMapper;

    private Long testTaskId;
    private Long testEmployeeId;

    @BeforeEach
    void setUp() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Desc");
        task.setStatus(TaskStatus.NEW);
        taskMapper.create(task);
        testTaskId = task.getId();

        Employee employee = new Employee();
        employee.setFirstname("Ivan");
        employee.setLastname("Ivanov");
        employee.setMiddlename("Ivanovich");
        employeeMapper.create(employee);
        testEmployeeId = employee.getId();
    }

    @Test
    void create_ShouldSaveTimeRecord() {
        TimeRecord record = new TimeRecord();
        record.setTaskId(testTaskId);
        record.setEmployeeId(testEmployeeId);
        record.setStartTime(LocalDateTime.now());
        record.setEndTime(LocalDateTime.now().plusHours(2));
        record.setComment("Test work");

        timeRecordMapper.create(record);

        assertNotNull(record.getId());
    }

    @Test
    void findByEmployeeAndPeriodResponse_ShouldReturnRecordsWithDetails() {
        TimeRecord record = new TimeRecord();
        record.setTaskId(testTaskId);
        record.setEmployeeId(testEmployeeId);
        record.setStartTime(LocalDateTime.of(2026, 1, 15, 10, 0));
        record.setEndTime(LocalDateTime.of(2026, 1, 15, 12, 0));
        record.setComment("Work");
        timeRecordMapper.create(record);

        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 31, 23, 59);

        List<TimeRecordResponse> responses = timeRecordMapper.findByEmployeeAndPeriodResponse(
                testEmployeeId, start, end);

        assertFalse(responses.isEmpty());
        TimeRecordResponse response = responses.get(0);
        assertNotNull(response.getTask());
        assertNotNull(response.getEmployee());
        assertEquals("Test Task", response.getTask().getTitle());
        assertEquals("Ivan", response.getEmployee().getFirstname());
    }
}
