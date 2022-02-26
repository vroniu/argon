package src.argon.argon.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import src.argon.argon.dto.WorktimeDTO;
import src.argon.argon.entity.Worktime;
import src.argon.argon.utils.EntityMapper;

@Mapper(componentModel = "spring", uses = {SubprojectMapper.class, EmployeeMapper.class})
public interface WorktimeMapper extends EntityMapper<Worktime, WorktimeDTO> {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(target = "employeeName", expression = "java(worktime.getEmployee().getFirstName() + \" \" + worktime.getEmployee().getLastName())")
    WorktimeDTO toDTO(Worktime worktime);

    @Mapping(source = "employeeId", target = "employee")
    Worktime toEntity(WorktimeDTO worktimeDTO);

    default Worktime fromId(Long id) {
        if (id == null)
            return null;
        Worktime worktime = new Worktime();
        worktime.setId(id);
        return worktime;
    }
}
