package src.argon.argon.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class WorktimeDTO implements Serializable {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private SubprojectDTO subproject;
    private LocalDate day;
    private Short hours;
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public SubprojectDTO getSubproject() {
        return subproject;
    }

    public void setSubproject(SubprojectDTO subproject) {
        this.subproject = subproject;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
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
}
