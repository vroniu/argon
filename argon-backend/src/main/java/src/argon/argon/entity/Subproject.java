package src.argon.argon.entity;

import com.sun.istack.NotNull;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "subprojects")
@SQLDelete(sql = "UPDATE subprojects SET deleted = true WHERE id = ?")
public class Subproject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subprojects_id_seq")
    @SequenceGenerator(name = "subprojects_id_seq", sequenceName = "subprojects_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "subproject_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "subproject")
    private List<Worktime> worktimes;

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

    public List<Worktime> getWorktimes() {
        return worktimes;
    }

    public void setWorktimes(List<Worktime> worktimes) {
        this.worktimes = worktimes;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subproject that = (Subproject) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
