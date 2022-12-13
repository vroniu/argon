package src.argon.argon.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import src.argon.argon.dto.EmployeeWithPositionDTO;

import java.time.LocalDate;
import java.util.List;

@Component
public class Organization2EmployeeDao {
    private static final String QUERY_ALL_ORGANIZATION_EMPLOYEES = "SELECT * FROM organization2employee o2e JOIN employees e ON o2e.employee_id = e.id WHERE o2e.organization_id = ?;";
    private static final String QUERY_ORGANIZATION_EMPLOYEE = "SELECT * FROM organization2employee o2e JOIN employees e ON o2e.employee_id = e.id WHERE o2e.organization_id = ? AND o2e.employee_id = ?;";
    private static final String QUERY_UPDATE_EMPLOYEE_INFO = "UPDATE organization2employee o2e SET employee_postion = ?, joined_date = ? WHERE organization_id = ? AND employee_id = ?;";
    private static final String QUERY_UPDATE_EMPLOYEE_POSITION = "UPDATE organization2employee o2e SET employee_postion = ? WHERE organization_id = ? AND employee_id = ?;";

    private static final String QUERY_DELETE_EMPLOYEE_FROM_ORGANIZATION = "DELETE FROM organization2employee o2e WHERE organization_id = ? AND employee_id = ?;";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<EmployeeWithPositionDTO> getEmployeesInfoForOrganization(Long organizationId) {
        return jdbcTemplate.query(QUERY_ALL_ORGANIZATION_EMPLOYEES, new Object[]{organizationId}, rowMapper);
    }

    public EmployeeWithPositionDTO getEmployeeInfoForOrganization(Long organizationId, Long employeeId) {
        return jdbcTemplate.queryForObject(QUERY_ORGANIZATION_EMPLOYEE, new Object[]{organizationId, employeeId}, rowMapper);
    }

    public void updateEmployeeInfo(Long organizationId, Long employeeId, String position) {
        jdbcTemplate.update(QUERY_UPDATE_EMPLOYEE_POSITION, new Object[]{position, organizationId, employeeId});
    }

    public void updateEmployeeInfo(Long organizationId, Long employeeId, String position, LocalDate joinedDate) {
        jdbcTemplate.update(QUERY_UPDATE_EMPLOYEE_INFO, new Object[]{position, joinedDate, organizationId, employeeId});
    }

    public void deleteEmployeeFromOrganization(Long organizationId, Long employeeId) {
        jdbcTemplate.update(QUERY_DELETE_EMPLOYEE_FROM_ORGANIZATION, new Object[]{organizationId, employeeId});
    }

    private RowMapper<EmployeeWithPositionDTO> rowMapper = (rs, rowNum) -> {
        EmployeeWithPositionDTO model = new EmployeeWithPositionDTO();
        model.setId(rs.getLong("employee_id"));
        model.setFirstName(rs.getString("first_name"));
        model.setLastName(rs.getString("last_name"));
        model.setJoinedDate(rs.getDate("joined_date").toLocalDate());
        model.setPosition(rs.getString("employee_postion"));
        return model;
    };

}
