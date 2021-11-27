package src.argon.argon.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import src.argon.argon.dto.SubprojectDTO;
import src.argon.argon.entity.Subproject;
import src.argon.argon.utils.EntityMapper;

@Mapper(componentModel = "spring", uses = {ProjectMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubprojectMapper extends EntityMapper<Subproject, SubprojectDTO> {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    SubprojectDTO toDTO(Subproject entity);

    @Mapping(source = "projectId", target = "project")
    Subproject toEntity(SubprojectDTO subprojectDTO);

    default Subproject fromId(Long id) {
        if (id == null)
            return null;
        Subproject subproject = new Subproject();
        subproject.setId(id);
        return subproject;
    }
}
