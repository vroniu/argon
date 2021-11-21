package src.argon.argon.mapper;

import org.mapstruct.Mapper;
import src.argon.argon.dto.ProjectDTO;
import src.argon.argon.entity.Project;
import src.argon.argon.utils.EntityMapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper extends EntityMapper<Project, ProjectDTO> {

}
