package src.argon.argon.service;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import src.argon.argon.dto.EmployeeDTO;
import src.argon.argon.entity.Employee;
import src.argon.argon.entity.Organization;
import src.argon.argon.mapper.EmployeeMapper;
import src.argon.argon.repository.EmployeeRepository;
import src.argon.argon.testutils.TestDataCreator;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;

    @Spy
    EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    @InjectMocks
    EmployeeService underTest;

    @Test
    void getEmployees_ShouldReturnAllEmployees_IfParametersAreNull() {
        List<Employee> employees = TestDataCreator.createEmployees(5);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<EmployeeDTO> result = underTest.getEmployees(null, null, Collections.emptyList());

        assertEquals(5, result.size());
    }

    @Test
    void getEmployees_ShouldFilterEmployeesByExcludedOrganizations() {
        List<Employee> employees = TestDataCreator.createEmployees(5);
        Organization excludedOrganization = new Organization();
        excludedOrganization.setId(321L);
        employees.get(2).setJoinedOrganizations(List.of(excludedOrganization));
        when(employeeRepository.findAll()).thenReturn(employees);

        List<EmployeeDTO> result = underTest.getEmployees(null, null, List.of(excludedOrganization.getId()));

        assertEquals(4, result.size());
        assertFalse(result.stream().anyMatch(employeeDTO -> employeeDTO.getId() == 2L));
    }

    @Test
    void save_ShouldReturnSavedEmployee() {
        Employee savedEmployee = TestDataCreator.createEmployees(1).get(0);
        EmployeeDTO employeeToSave = employeeMapper.toDTO(savedEmployee);
        employeeToSave.setId(null);
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        EmployeeDTO result = underTest.save(employeeToSave);

        assertEquals(0L, result.getId());
        assertEquals("Test 0", result.getLastName());
    }

    @Test
    void getEmployee_ShouldReturnEmployee() {
        Employee employee = TestDataCreator.createEmployees(1).get(0);
        when(employeeRepository.getById(0L)).thenReturn(employee);

        EmployeeDTO result = underTest.getEmployee(0L);

        assertEquals(0, result.getId());
        assertEquals("Adam", result.getFirstName());
        assertEquals("Test 0", result.getLastName());
    }
}