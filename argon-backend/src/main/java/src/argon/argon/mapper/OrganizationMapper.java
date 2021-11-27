package src.argon.argon.mapper;

import org.mapstruct.Mapper;
import src.argon.argon.dto.OrganizationDTO;
import src.argon.argon.entity.Organization;
import src.argon.argon.utils.EntityMapper;

@Mapper(componentModel = "spring", uses = {ProjectMapper.class, EmployeeMapper.class})
public interface OrganizationMapper extends EntityMapper<Organization, OrganizationDTO> {

    default Organization fromId(Long id) {
        if (id == null)
            return null;
        Organization organization = new Organization();
        organization.setId(id);
        return organization;
    }
}
