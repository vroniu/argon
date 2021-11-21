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
}
