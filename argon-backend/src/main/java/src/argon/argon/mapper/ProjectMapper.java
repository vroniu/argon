package src.argon.argon.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import src.argon.argon.dto.ProjectDTO;
import src.argon.argon.entity.Project;
import src.argon.argon.utils.EntityMapper;

@Mapper(componentModel = "spring", uses = {OrganizationMapper.class, SubprojectMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper extends EntityMapper<Project, ProjectDTO> {

    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    ProjectDTO toDTO(Project project);

    @Mapping(source = "organizationId", target = "organization")
    Project toEntity(ProjectDTO projectDTO);

    default Project fromId(Long id) {
        if (id == null)
            return null;
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
