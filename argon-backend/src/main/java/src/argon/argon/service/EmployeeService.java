package src.argon.argon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.argon.argon.dto.EmployeeDTO;
import src.argon.argon.mapper.EmployeeMapper;
import src.argon.argon.repository.EmployeeRepository;

import javax.transaction.Transactional;

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
}
