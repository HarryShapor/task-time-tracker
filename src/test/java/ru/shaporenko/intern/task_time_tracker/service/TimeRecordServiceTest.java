package ru.shaporenko.intern.task_time_tracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shaporenko.intern.task_time_tracker.dto.task.TaskResponse;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordCreateDto;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordResponse;
import ru.shaporenko.intern.task_time_tracker.dto.timeRecord.TimeRecordsResponse;
import ru.shaporenko.intern.task_time_tracker.entity.Employee;
import ru.shaporenko.intern.task_time_tracker.entity.Task;
import ru.shaporenko.intern.task_time_tracker.entity.TimeRecord;
import ru.shaporenko.intern.task_time_tracker.entity.enums.TaskStatus;
import ru.shaporenko.intern.task_time_tracker.mapper.EmployeeMapper;
import ru.shaporenko.intern.task_time_tracker.mapper.TaskMapper;
import ru.shaporenko.intern.task_time_tracker.mapper.TimeRecordMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimeRecordServiceTest {

    private static final Long ID = 1L;
    private static final Long TASK_ID = 5L;
    private static final Long EMPLOYEE_ID = 3L;

    @Mock
    private TimeRecordMapper timeRecordMapper;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private TimeRecordService timeRecordService;

    @Test
    void getTimeRecord_ShouldReturnTimeRecord_WhenItExists(){

        TimeRecord timeRecord = new TimeRecord();
        timeRecord.setId(ID);
        timeRecord.setTaskId(TASK_ID);
        timeRecord.setEmployeeId(EMPLOYEE_ID);
        timeRecord.setStartTime(LocalDateTime.now());
        timeRecord.setEndTime(LocalDateTime.now().plusHours(2));
        timeRecord.setComment("Worked on feature");

        Task task = new Task();
        task.setId(TASK_ID);
        task.setTitle("Task Title");

        Employee employee = new Employee();
        employee.setId(EMPLOYEE_ID);
        employee.setFirstname("Ivan");
        employee.setLastname("Ivanov");

        when(timeRecordMapper.findById(ID)).thenReturn(timeRecord);
        when(taskMapper.findById(TASK_ID)).thenReturn(task);
        when(employeeMapper.findById(EMPLOYEE_ID)).thenReturn(employee);

        TimeRecordResponse response = timeRecordService.getTimeRecord(ID);

        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals("Task Title", response.getTask().getTitle());
        assertEquals("Ivan", response.getEmployee().getFirstname());

        verify(timeRecordMapper, times(1)).findById(ID);
        verify(taskMapper, times(1)).findById(TASK_ID);
        verify(employeeMapper, times(1)).findById(EMPLOYEE_ID);

    }

    @Test
    void getByEmployeeAndPeriod_ShouldReturnTimeRecordsResponse_WhenExistsEmployeeAndValidPeriod(){

        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 1, 31);

        TimeRecordResponse recordResponse = new TimeRecordResponse();
        recordResponse.setId(ID);
        recordResponse.setStartTime(LocalDateTime
                .of(2026, 1, 15, 10, 0));
        recordResponse.setEndTime(LocalDateTime
                .of(2026, 1, 15, 12, 0));

        List<TimeRecordResponse> mockRecords = List.of(recordResponse);

        when(timeRecordMapper.findByEmployeeAndPeriodResponse(eq(EMPLOYEE_ID),
                any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(mockRecords);

        TimeRecordsResponse response = timeRecordService
                .getByEmployeeAndPeriod(EMPLOYEE_ID, start, end);

        assertNotNull(response);
        assertEquals(1, response.getTotalRecords());
        assertNotNull(response.getTotalHours());
        verify(timeRecordMapper, times(1))
                .findByEmployeeAndPeriodResponse(eq(EMPLOYEE_ID),
                        any(LocalDateTime.class), any(LocalDateTime.class));

    }

    @Test
    void createTimeRecord_ShouldReturnTimeRecordResponse_WhenValidDataProvided(){

        TimeRecordCreateDto dto = new TimeRecordCreateDto();
        dto.setTaskId(TASK_ID);
        dto.setEmployeeId(EMPLOYEE_ID);
        dto.setStartTime(LocalDateTime.now());
        dto.setEndTime(LocalDateTime.now().plusHours(3));
        dto.setComment("Test work");

        Task task = new Task();
        task.setId(TASK_ID);
        task.setTitle("Task for time record");

        Employee employee = new Employee();
        employee.setId(EMPLOYEE_ID);
        employee.setFirstname("Ivan");
        employee.setLastname("Ivanov");

        TimeRecord timeRecordToCreate = new TimeRecord();
        timeRecordToCreate.setTaskId(TASK_ID);
        timeRecordToCreate.setEmployeeId(EMPLOYEE_ID);
        timeRecordToCreate.setStartTime(dto.getStartTime());
        timeRecordToCreate.setEndTime(dto.getEndTime());
        timeRecordToCreate.setComment(dto.getComment());

        TimeRecord savedTimeRecord = new TimeRecord();
        savedTimeRecord.setId(ID);
        savedTimeRecord.setTaskId(TASK_ID);
        savedTimeRecord.setEmployeeId(EMPLOYEE_ID);
        savedTimeRecord.setStartTime(dto.getStartTime());
        savedTimeRecord.setEndTime(dto.getEndTime());
        savedTimeRecord.setComment(dto.getComment());

        when(taskMapper.findById(TASK_ID)).thenReturn(task);
        when(employeeMapper.findById(EMPLOYEE_ID)).thenReturn(employee);
        doAnswer(invocation -> {
            TimeRecord tr = invocation.getArgument(0);
            tr.setId(ID);
            return null;
        }).when(timeRecordMapper).create(any(TimeRecord.class));

        TimeRecordResponse response = timeRecordService.createTimeRecord(dto);

        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals("Task for time record", response.getTask().getTitle());
        assertEquals("Ivan", response.getEmployee().getFirstname());

        verify(taskMapper, atLeastOnce()).findById(TASK_ID);
        verify(employeeMapper, atLeastOnce()).findById(EMPLOYEE_ID);
        verify(timeRecordMapper, times(1)).create(any(TimeRecord.class));

    }

    @Test
    void deleteTimeRecord_ShouldDeleteTimeRecord_WhenTimeRecordExists(){

        TimeRecord existingRecord = new TimeRecord();
        existingRecord.setId(ID);
        existingRecord.setTaskId(TASK_ID);
        existingRecord.setEmployeeId(EMPLOYEE_ID);

        when(timeRecordMapper.findById(ID)).thenReturn(existingRecord);

        timeRecordService.deleteTimeRecord(ID);

        verify(timeRecordMapper, times(1)).findById(ID);

    }
}
