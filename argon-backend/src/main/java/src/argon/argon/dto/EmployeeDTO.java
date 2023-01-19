package src.argon.argon.dto;

import java.io.Serializable;
import java.util.List;

public class EmployeeDTO implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrganizationDTO> organizations;
    private List<OrganizationDTO> ownedOrganizations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<OrganizationDTO> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<OrganizationDTO> organizations) {
        this.organizations = organizations;
    }

    public List<OrganizationDTO> getOwnedOrganizations() {
        return ownedOrganizations;
    }

    public void setOwnedOrganizations(List<OrganizationDTO> ownedOrganizations) {
        this.ownedOrganizations = ownedOrganizations;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
