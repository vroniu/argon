package src.argon.argon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import src.argon.argon.dto.SubprojectDTO;
import src.argon.argon.dto.WorktimeDTO;
import src.argon.argon.entity.Employee;
import src.argon.argon.entity.Worktime;
import src.argon.argon.mapper.EmployeeMapper;
import src.argon.argon.mapper.ProjectMapper;
import src.argon.argon.mapper.SubprojectMapper;
import src.argon.argon.mapper.WorktimeMapper;
import src.argon.argon.repository.WorktimeRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class WorktimeServiceTest {

    @InjectMocks
    WorktimeService underTest;

    @Mock
    WorktimeRepository worktimeRepository;

    @Spy
    WorktimeMapper worktimeMapper = Mappers.getMapper(WorktimeMapper.class);

    @Spy
    EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    @Spy
    SubprojectMapper subprojectMapper = Mappers.getMapper(SubprojectMapper.class);

    @Spy
    ProjectMapper projectMapper = Mappers.getMapper(ProjectMapper.class);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(worktimeMapper, "employeeMapper", employeeMapper);
        ReflectionTestUtils.setField(worktimeMapper, "subprojectMapper", subprojectMapper);
        ReflectionTestUtils.setField(subprojectMapper, "projectMapper", projectMapper);
    }

    @Test
    void save_ShouldReturnSavedWorktime() {
        WorktimeDTO worktimeToSave = createWorktimes(1, null).get(0);
        worktimeToSave.setId(null);
        Worktime savedWorktime = worktimeMapper.toEntity(worktimeToSave);
        savedWorktime.setEmployee(createEmployee());
        savedWorktime.setId(321L);
        when(worktimeRepository.save(any(Worktime.class))).thenReturn(savedWorktime);

        WorktimeDTO result = underTest.save(worktimeToSave);

        assertEquals(321L, result.getId());
        assertEquals("Comment 0", result.getComment());
    }

    @Test
    void save_ShouldThrowException_IfSubprojectDeleted() {
        SubprojectDTO deletedSubproject = new SubprojectDTO();
        deletedSubproject.setDeleted(true);
        WorktimeDTO worktimeToSave = createWorktimes(1, deletedSubproject).get(0);
        worktimeToSave.setId(321L);
        Worktime savedWorktime = worktimeMapper.toEntity(worktimeToSave);
        savedWorktime.setEmployee(createEmployee());
        when(worktimeRepository.save(any(Worktime.class))).thenReturn(savedWorktime);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            WorktimeDTO result = underTest.save(worktimeToSave);
        });

        assertNotNull(exception);
        assertEquals("Cant assign worktime to a deleted subproject/project", exception.getMessage());
    }

    @Test
    void getWorktimesAtDayForUser_ShouldReturnWorktimes() {
        List<Worktime> worktimes = worktimeMapper.toEntity(createWorktimes(5, null));
        worktimes.forEach(worktime -> worktime.setEmployee(createEmployee()));
        ArgumentCaptor<LocalDate> startDateArgumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> endDateArgumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        LocalDate day = LocalDate.of(2023, 1, 16);
        when(worktimeRepository.findByDayBetweenAndEmployeeIdAndSubprojectProjectOrganizationId(startDateArgumentCaptor.capture(),
                endDateArgumentCaptor.capture(), any(), any())).thenReturn(worktimes);

        List<WorktimeDTO> result = underTest.getWorktimesAtDayForUser(day, 123L, 321L);

        assertEquals(day, startDateArgumentCaptor.getValue());
        assertEquals(day, endDateArgumentCaptor.getValue());
        assertEquals(5, result.size());
    }

    @Test
    void getWorktimesAtDateRangeForUser_ShouldReturnWorktimes() {
        List<Worktime> worktimes = worktimeMapper.toEntity(createWorktimes(5, null));
        worktimes.forEach(worktime -> worktime.setEmployee(createEmployee()));
        ArgumentCaptor<LocalDate> startDateArgumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> endDateArgumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        LocalDate start = LocalDate.of(2023, 1, 13);
        LocalDate end = LocalDate.of(2023, 1, 16);
        when(worktimeRepository.findByDayBetweenAndEmployeeIdAndSubprojectProjectOrganizationId(startDateArgumentCaptor.capture(),
                endDateArgumentCaptor.capture(), any(), any())).thenReturn(worktimes);

        List<WorktimeDTO> result = underTest.getWorktimesAtDateRangeForUser(start, end, 123L, 321L);

        assertEquals(start, startDateArgumentCaptor.getValue());
        assertEquals(end, endDateArgumentCaptor.getValue());
        assertEquals(5, result.size());
    }

    @Test
    void getWorktimesAtDateRangeForEmployeesInSubprojects_ShouldReturnWorktimes() {
        List<Worktime> worktimes = worktimeMapper.toEntity(createWorktimes(5, null));
        worktimes.forEach(worktime -> worktime.setEmployee(createEmployee()));
        LocalDate start = LocalDate.of(2023, 1, 13);
        LocalDate end = LocalDate.of(2023, 1, 16);
        when(worktimeRepository.findByDayBetweenAndEmployeeIdInAndSubprojectIdIn(any(),
                any(), any(), any())).thenReturn(worktimes);

        List<WorktimeDTO> result = underTest.getWorktimesAtDateRangeForEmployeesInSubprojects(start, end, Collections.singletonList(123L), Collections.singletonList(321L));

        assertEquals(5, result.size());
    }

    @Test
    void deleteWorktime_ShouldCallRepository() {
        underTest.deleteWorktime(123L);

        verify(worktimeRepository).deleteById(123L);
    }

    private List<WorktimeDTO> createWorktimes(int count, SubprojectDTO subproject) {
        List<WorktimeDTO> worktimes = new ArrayList<>(count);
        if (subproject == null) {
            subproject = new SubprojectDTO();
            subproject.setId(123L);
            subproject.setDeleted(false);
        }
        for (int i = 0; i < count; i++) {
            WorktimeDTO worktime = new WorktimeDTO();
            worktime.setId((long) i);
            worktime.setDay(LocalDate.now());
            worktime.setHours((short) 3);
            worktime.setComment("Comment " + i);
            worktime.setSubproject(subproject);
            worktimes.add(worktime);
        }
        return worktimes;
    }



    private Employee createEmployee() {
        Employee employee = new Employee();
        employee.setId(123L);
        employee.setFirstName("Adam");
        employee.setLastName("Test");
        return employee;
    }

}