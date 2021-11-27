package src.argon.argon.dto;

import java.io.Serializable;
import java.util.List;

public class OrganizationDTO implements Serializable {
    private Long id;
    private String name;
    private List<ProjectDTO> projects;
    private List<EmployeeDTO> employees;
    private List<EmployeeDTO> owners;

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

    public List<ProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDTO> projects) {
        this.projects = projects;
    }

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
    }

    public List<EmployeeDTO> getOwners() {
        return owners;
    }

    public void setOwners(List<EmployeeDTO> owners) {
        this.owners = owners;
    }
}
