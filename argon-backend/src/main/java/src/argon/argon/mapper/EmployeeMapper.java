package src.argon.argon.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import src.argon.argon.dto.EmployeeDTO;
import src.argon.argon.entity.Employee;
import src.argon.argon.utils.EntityMapper;

@Mapper(componentModel = "spring", uses = {OrganizationMapper.class})
public interface EmployeeMapper extends EntityMapper<Employee, EmployeeDTO> {

    @Mapping(source = "user.email", target = "email")
    EmployeeDTO toDTO(Employee entity);

    default Employee fromId(Long id) {
        if (id == null)
            return null;
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
