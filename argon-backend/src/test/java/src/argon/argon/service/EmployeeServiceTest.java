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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        List<Employee> employees = createEmployees(5);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<EmployeeDTO> result = underTest.getEmployees(null, null, Collections.emptyList());

        assertEquals(5, result.size());
    }

    @Test
    void getEmployees_ShouldFilterEmployeesByExcludedOrganizations() {
        List<Employee> employees = createEmployees(5);
        Organization excludedOrganization = new Organization();
        excludedOrganization.setId(321L);
        employees.get(2).setJoinedOrganizations(List.of(excludedOrganization));
        when(employeeRepository.findAll()).thenReturn(employees);

        List<EmployeeDTO> result = underTest.getEmployees(null, null, List.of(excludedOrganization.getId()));

        assertEquals(4, result.size());
        assertFalse(result.stream().anyMatch(employeeDTO -> employeeDTO.getId() == 2L));
    }

    private List<Employee> createEmployees(int count) {
        List<Employee> employeeList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Employee employee = new Employee();
            employee.setJoinedOrganizations(Collections.emptyList());
            employee.setId((long) i);
            employee.setFirstName("Adam");
            employee.setLastName("Test" + i);
            employeeList.add(employee);
        }
        return employeeList;
    }
}