package src.argon.argon.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organizations_id_seq")
    @SequenceGenerator(name = "organizations_id_seq", sequenceName = "organizations_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "organization")
    private List<Project> projects;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="organization2employee",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> employees;

    @ManyToMany
    @JoinTable(
            name = "organization_owners",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id")
    )
    private List<Employee> owners;

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

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Employee> getOwners() {
        return owners;
    }

    public void setOwners(List<Employee> owners) {
        this.owners = owners;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
