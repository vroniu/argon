package src.argon.argon.project;

import org.mapstruct.Mapper;
import src.argon.argon.utils.EntityMapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper extends EntityMapper<Project, ProjectDTO> {

}
