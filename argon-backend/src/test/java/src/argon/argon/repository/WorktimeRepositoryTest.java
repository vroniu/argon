package src.argon.argon.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import src.argon.argon.entity.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WorktimeRepositoryTest {

    @Autowired
    WorktimeRepository underTest;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    SubprojectRepository subprojectRepository;

    Employee testEmployee;
    Organization testOrganization;
    Project testProject;
    Subproject testSubproject;

    @BeforeEach
    public void init() {
        testEmployee = new Employee();
        testEmployee.setFirstName("testFirstName");
        testEmployee.setLastName("testLastName");
        testEmployee = employeeRepository.save(testEmployee);

        testOrganization = new Organization();
        testOrganization.setName("testOrganization");
        testOrganization = organizationRepository.save(testOrganization);

        testProject = new Project();
        testProject.setName("testProject");
        testProject.setOrganization(testOrganization);
        testProject = projectRepository.save(testProject);

        testSubproject = new Subproject();
        testSubproject.setName("testSubproject");
        testSubproject.setProject(testProject);
        testSubproject = subprojectRepository.save(testSubproject);
    }

    @Test
    void findByDayBetweenAndEmployeeIdAndSubprojectProjectOrganizationId_singleDayTest_findsWorktime() {
        // given
        Worktime worktimeToFind = new Worktime();
        worktimeToFind.setEmployee(testEmployee);
        worktimeToFind.setSubproject(testSubproject);
        worktimeToFind.setDay(LocalDate.of(2022, 12, 1));
        worktimeToFind = underTest.save(worktimeToFind);
        // when
        List<Worktime> foundWorktimes = underTest.findByDayBetweenAndEmployeeIdAndSubprojectProjectOrganizationId(
                LocalDate.of(2022, 12, 1), LocalDate.of(2022, 12, 1), testEmployee.getId(), testOrganization.getId()
        );
        // then
        assertTrue(foundWorktimes.contains(worktimeToFind));
    }

    @Test
    void findByDayBetweenAndEmployeeIdAndSubprojectProjectOrganizationId_dateRangeTest_findsWorktime() {
        // given
        Worktime worktimeToFind = new Worktime();
        worktimeToFind.setEmployee(testEmployee);
        worktimeToFind.setSubproject(testSubproject);
        worktimeToFind.setDay(LocalDate.of(2022, 12, 2));
        worktimeToFind = underTest.save(worktimeToFind);
        // when
        List<Worktime> foundWorktimes = underTest.findByDayBetweenAndEmployeeIdAndSubprojectProjectOrganizationId(
                LocalDate.of(2022, 12, 1), LocalDate.of(2022, 12, 3), testEmployee.getId(), testOrganization.getId()
        );
        // then
        assertTrue(foundWorktimes.contains(worktimeToFind));
    }

    @Test
    void findByDayBetweenAndEmployeeIdAndSubprojectProjectOrganizationId_outOfDateRange_emptyList() {
        // given
        Worktime worktimeToFind = new Worktime();
        worktimeToFind.setEmployee(testEmployee);
        worktimeToFind.setSubproject(testSubproject);
        worktimeToFind.setDay(LocalDate.of(2022, 12, 4));
        underTest.save(worktimeToFind);
        // when
        List<Worktime> foundWorktimes = underTest.findByDayBetweenAndEmployeeIdAndSubprojectProjectOrganizationId(
                LocalDate.of(2022, 12, 1), LocalDate.of(2022, 12, 3), testEmployee.getId(), testOrganization.getId()
        );
        // then
        assertTrue(foundWorktimes.isEmpty());
    }

    @Test
    void findByDayBetweenAndEmployeeIdAndSubprojectProjectOrganizationId_invalidEmployee_emptyList() {
        // given
        Worktime worktimeToFind = new Worktime();
        Employee unrelatedEmployee = new Employee();
        unrelatedEmployee.setFirstName("testFirstName");
        unrelatedEmployee.setLastName("testLastName");
        unrelatedEmployee = employeeRepository.save(unrelatedEmployee);
        worktimeToFind.setEmployee(unrelatedEmployee);
        worktimeToFind.setSubproject(testSubproject);
        worktimeToFind.setDay(LocalDate.of(2022, 12, 1));
        underTest.save(worktimeToFind);
        // when
        List<Worktime> foundWorktimes = underTest.findByDayBetweenAndEmployeeIdAndSubprojectProjectOrganizationId(
                LocalDate.of(2022, 12, 1), LocalDate.of(2022, 12, 1), testEmployee.getId(), testOrganization.getId()
        );
        // then
        assertTrue(foundWorktimes.isEmpty());
    }

    @Test
    void findByDayBetweenAndEmployeeIdAndSubprojectProjectOrganizationId_invalidSubproject_emptyList() {
        // given
        Worktime worktimeToFind = new Worktime();
        Organization unrelatedOrganization = new Organization();
        unrelatedOrganization.setName("unrelatedOrganization");
        unrelatedOrganization = organizationRepository.save(unrelatedOrganization);
        Project unrelatedProject = new Project();
        unrelatedProject.setName("unrelatedProject");
        unrelatedProject.setOrganization(unrelatedOrganization);
        unrelatedProject = projectRepository.save(unrelatedProject);
        Subproject unrelatedSubproject = new Subproject();
        unrelatedSubproject.setName("unrelatedSubproject");
        unrelatedSubproject.setProject(unrelatedProject);
        unrelatedSubproject = subprojectRepository.save(unrelatedSubproject);
        worktimeToFind.setEmployee(testEmployee);
        worktimeToFind.setSubproject(unrelatedSubproject);
        worktimeToFind.setDay(LocalDate.of(2022, 12, 1));
        underTest.save(worktimeToFind);
        // when
        List<Worktime> foundWorktimes = underTest.findByDayBetweenAndEmployeeIdAndSubprojectProjectOrganizationId(
                LocalDate.of(2022, 12, 1), LocalDate.of(2022, 12, 1), testEmployee.getId(), testOrganization.getId()
        );
        // then
        assertTrue(foundWorktimes.isEmpty());
    }
}