package src.argon.argon.mapper;

import org.mapstruct.Mapper;
import src.argon.argon.dto.EmployeeDTO;
import src.argon.argon.entity.Employee;
import src.argon.argon.utils.EntityMapper;

@Mapper(componentModel = "spring", uses = {OrganizationMapper.class})
public interface EmployeeMapper extends EntityMapper<Employee, EmployeeDTO> {

    default Employee fromId(Long id) {
        if (id == null)
            return null;
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
