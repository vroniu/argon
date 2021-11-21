package src.argon.argon.dto;

import java.io.Serializable;
import java.util.List;

public class OrganizationDTO implements Serializable {
    private Long id;
    private String name;
    private List<ProjectDTO> projects;
    private List<EmployeeDTO> employees;
    private List<EmployeeDTO> owners;
}
