package src.argon.argon.dto;

import java.io.Serializable;
import java.util.List;

public class EmployeeDTO implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private List<OrganizationDTO> organizations;
    private List<OrganizationDTO> ownedOrganizations;
}
