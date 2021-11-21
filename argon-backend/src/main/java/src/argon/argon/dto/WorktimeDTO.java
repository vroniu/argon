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
}
