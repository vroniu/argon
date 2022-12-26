package src.argon.argon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.argon.argon.dto.EmployeeDTO;
import src.argon.argon.entity.Employee;
import src.argon.argon.mapper.EmployeeMapper;
import src.argon.argon.repository.EmployeeRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        return employeeMapper.toDTO(employeeRepository.save(employeeMapper.toEntity(employeeDTO)));
    }

    public EmployeeDTO getEmployee(Long employeeId) {
        return employeeMapper.toDTO(employeeRepository.getById(employeeId));
    }

    public List<EmployeeDTO> getEmployees(String name, String email, List<Long> excludeOrganizations) {
        List<Employee> allEmployees = employeeRepository.findAll();
        if (name != null) {

        }
        if (email != null) {

        }
        if (!excludeOrganizations.isEmpty()) {
            allEmployees = allEmployees.stream().filter(employee ->
                    employee.getJoinedOrganizations().stream().noneMatch(organization -> excludeOrganizations.contains(organization.getId()))
            ).collect(Collectors.toList());
        }
        return employeeMapper.toDTO(allEmployees);
    }
}
