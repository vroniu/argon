package src.argon.argon.dto;

import java.io.Serializable;
import java.util.List;

public class ProjectDTO implements Serializable {
    private Long id;
    private String name;
    private Long organizationId;
    private String organizationName;
    private List<SubprojectDTO> subprojects;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public List<SubprojectDTO> getSubprojects() {
        return subprojects;
    }

    public void setSubprojects(List<SubprojectDTO> subprojects) {
        this.subprojects = subprojects;
    }
}
