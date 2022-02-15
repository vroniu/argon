package src.argon.argon.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "worktimes")
public class Worktime {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "worktimes_id_seq")
    @SequenceGenerator(name = "worktimes_id_seq", sequenceName = "worktimes_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "subproject_id", nullable = false)
    private Subproject subproject;

    @NotNull
    @Column(name = "worktime_day")
    private Date day;

    @NotNull
    private Short hours;

    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Subproject getSubproject() {
        return subproject;
    }

    public void setSubproject(Subproject subproject) {
        this.subproject = subproject;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Short getHours() {
        return hours;
    }

    public void setHours(Short hours) {
        this.hours = hours;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worktime worktime = (Worktime) o;
        return Objects.equals(id, worktime.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
