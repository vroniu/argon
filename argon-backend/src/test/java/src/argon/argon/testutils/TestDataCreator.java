package src.argon.argon.testutils;

import src.argon.argon.dto.*;
import src.argon.argon.entity.Employee;
import src.argon.argon.entity.Organization;
import src.argon.argon.entity.Project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestDataCreator {

    public static List<EmployeeWithPositionDTO> createEmployeesWithOrganization(int count) {
        List<EmployeeWithPositionDTO> employees = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            EmployeeWithPositionDTO employee = new EmployeeWithPositionDTO();
            employee.setId((long) i);
            employee.setFirstName("Employee");
            employee.setLastName(String.valueOf(i));
            employee.setPosition("Position");
            employee.setJoinedDate(LocalDate.now());
            employees.add(employee);
        }
        return employees;
    }

    public static List<EmployeeDTO> createEmployeeDtos(int count) {
        List<EmployeeDTO> employeeList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            EmployeeDTO employee = new EmployeeDTO();
            employee.setId((long) i);
            employee.setFirstName("Adam");
            employee.setLastName("Test" + i);
            employeeList.add(employee);
        }
        return employeeList;
    }

    public static List<Employee> createEmployees(int count) {
        List<Employee> employeeList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Employee employee = new Employee();
            employee.setJoinedOrganizations(Collections.emptyList());
            employee.setId((long) i);
            employee.setFirstName("Adam");
            employee.setLastName("Test " + i);
            employeeList.add(employee);
        }
        return employeeList;
    }

    public static List<Organization> createOrganizations(int count) {
        List<Organization> organizations = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Organization organization = new Organization();
            organization.setId((long) i);
            organization.setName("Organization " + i);
            organization.setProjects(Collections.emptyList());
            organization.setEmployees(Collections.emptyList());
            organization.setOwners(Collections.emptyList());
            organizations.add(organization);
        }
        return organizations;
    }

    public static List<OrganizationDTO> createOrganizationDTOs(int count) {
        List<OrganizationDTO> organizations = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            OrganizationDTO organization = new OrganizationDTO();
            organization.setId((long) i);
            organization.setName("Organization " + i);
            organization.setProjects(Collections.emptyList());
            organization.setEmployees(Collections.emptyList());
            organization.setOwners(Collections.emptyList());
            organizations.add(organization);
        }
        return organizations;
    }

    public static List<Project> createProjects(int count) {
        List<Project> projectList = new ArrayList<>(count);
        Organization organization = new Organization();
        organization.setId(111L);
        organization.setName("Test Organization");
        for (int i = 0; i < count; i++) {
            Project project = new Project();
            project.setName("Project " + i);
            project.setSubprojects(Collections.emptyList());
            project.setOrganization(organization);
            projectList.add(project);
        }
        return projectList;
    }

    public static List<ProjectDTO> createProjectDTOs(int count) {
        List<ProjectDTO> projectList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            ProjectDTO project = new ProjectDTO();
            project.setName("Project " + i);
            project.setSubprojects(Collections.emptyList());
            project.setOrganizationId(111L);
            project.setOrganizationName("Test Organization");
            projectList.add(project);
        }
        return projectList;
    }

    public static List<WorktimeDTO> createWorktimes(int count, SubprojectDTO subproject) {
        List<src.argon.argon.dto.WorktimeDTO> worktimes = new ArrayList<>(count);
        if (subproject == null) {
            subproject = new SubprojectDTO();
            subproject.setId(123L);
            subproject.setDeleted(false);
        }
        for (int i = 0; i < count; i++) {
            src.argon.argon.dto.WorktimeDTO worktime = new src.argon.argon.dto.WorktimeDTO();
            worktime.setId((long) i);
            worktime.setDay(LocalDate.now());
            worktime.setHours((short) 3);
            worktime.setComment("Comment " + i);
            worktime.setSubproject(subproject);
            worktimes.add(worktime);
        }
        return worktimes;
    }

}
