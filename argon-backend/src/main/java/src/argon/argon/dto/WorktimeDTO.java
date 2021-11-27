package src.argon.argon.dto;

import java.io.Serializable;
import java.util.Date;

public class WorktimeDTO implements Serializable {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private Long subprojectId;
    private String subprojectName;
    private Date day;
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

    public Long getSubprojectId() {
        return subprojectId;
    }

    public void setSubprojectId(Long subprojectId) {
        this.subprojectId = subprojectId;
    }

    public String getSubprojectName() {
        return subprojectName;
    }

    public void setSubprojectName(String subprojectName) {
        this.subprojectName = subprojectName;
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
}
