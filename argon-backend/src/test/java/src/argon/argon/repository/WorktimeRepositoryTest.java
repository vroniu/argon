package src.argon.argon.repository;

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

    @Test
    void findByDayBetweenAndEmployeeInInAndSubprojectIdIn_validDayRange_findsWorktimes() {
        // given
        Worktime worktimeToFind = new Worktime();
        worktimeToFind.setEmployee(testEmployee);
        worktimeToFind.setSubproject(testSubproject);
        worktimeToFind.setDay(LocalDate.of(2022, 12, 2));
        worktimeToFind = underTest.save(worktimeToFind);
        // when
        List<Worktime> foundWorktimes = underTest.findByDayBetweenAndEmployeeIdInAndSubprojectIdIn(
                LocalDate.of(2022, 12, 1), LocalDate.of(2022, 12, 3),
                List.of(testEmployee.getId()), List.of(testSubproject.getId())
        );
        // then
        assertTrue(foundWorktimes.contains(worktimeToFind));
    }

    @Test
    void findByDayBetweenAndEmployeeInInAndSubprojectIdIn_validSingleDay_findsWorktimes() {
        // given
        Worktime worktimeToFind = new Worktime();
        worktimeToFind.setEmployee(testEmployee);
        worktimeToFind.setSubproject(testSubproject);
        worktimeToFind.setDay(LocalDate.of(2022, 12, 2));
        worktimeToFind = underTest.save(worktimeToFind);
        // when
        List<Worktime> foundWorktimes = underTest.findByDayBetweenAndEmployeeIdInAndSubprojectIdIn(
                LocalDate.of(2022, 12, 2), LocalDate.of(2022, 12, 2),
                List.of(testEmployee.getId()), List.of(testSubproject.getId())
        );
        // then
        assertTrue(foundWorktimes.contains(worktimeToFind));
    }

    @Test
    void findByDayBetweenAndEmployeeInInAndSubprojectIdIn_validMultipleEmployees_findsWorktimes() {
        // given
        Employee anotherEmployee = new Employee();
        anotherEmployee.setFirstName("testFirstName");
        anotherEmployee.setLastName("testLastName");
        anotherEmployee = employeeRepository.save(anotherEmployee);
        Worktime worktime1 = new Worktime();
        worktime1.setEmployee(testEmployee);
        worktime1.setSubproject(testSubproject);
        worktime1.setDay(LocalDate.of(2022, 12, 2));
        worktime1 = underTest.save(worktime1);
        Worktime worktime2 = new Worktime();
        worktime2.setEmployee(anotherEmployee);
        worktime2.setSubproject(testSubproject);
        worktime2.setDay(LocalDate.of(2022, 12, 2));
        worktime2 = underTest.save(worktime2);
        // when
        List<Worktime> foundWorktimes = underTest.findByDayBetweenAndEmployeeIdInAndSubprojectIdIn(
                LocalDate.of(2022, 12, 2), LocalDate.of(2022, 12, 2),
                List.of(testEmployee.getId(), anotherEmployee.getId()), List.of(testSubproject.getId())
        );
        // then
        assertEquals(foundWorktimes.size(), 2);
        assertTrue(foundWorktimes.contains(worktime1));
        assertTrue(foundWorktimes.contains(worktime2));
    }

    @Test
    void findByDayBetweenAndEmployeeInInAndSubprojectIdIn_validMultipleSubprojects_findsWorktimes() {
        // given
        Subproject anotherSubproject = new Subproject();
        anotherSubproject.setName("anotherSubproject");
        anotherSubproject.setProject(testProject);
        subprojectRepository.save(anotherSubproject);
        Worktime worktime1 = new Worktime();
        worktime1.setEmployee(testEmployee);
        worktime1.setSubproject(testSubproject);
        worktime1.setDay(LocalDate.of(2022, 12, 2));
        worktime1 = underTest.save(worktime1);
        Worktime worktime2 = new Worktime();
        worktime2.setEmployee(testEmployee);
        worktime2.setSubproject(anotherSubproject);
        worktime2.setDay(LocalDate.of(2022, 12, 2));
        worktime2 = underTest.save(worktime2);
        // when
        List<Worktime> foundWorktimes = underTest.findByDayBetweenAndEmployeeIdInAndSubprojectIdIn(
                LocalDate.of(2022, 12, 2), LocalDate.of(2022, 12, 2),
                List.of(testEmployee.getId()), List.of(testSubproject.getId(), anotherSubproject.getId())
        );
        // then
        assertEquals(foundWorktimes.size(), 2);
        assertTrue(foundWorktimes.contains(worktime1));
        assertTrue(foundWorktimes.contains(worktime2));
    }

    @Test
    void findByDayBetweenAndEmployeeInInAndSubprojectIdIn_invalidDate_emptyList() {
        // given
        Worktime unrelatedWorktime = new Worktime();
        unrelatedWorktime.setEmployee(testEmployee);
        unrelatedWorktime.setSubproject(testSubproject);
        unrelatedWorktime.setDay(LocalDate.of(2022, 12, 3));
        underTest.save(unrelatedWorktime);
        // when
        List<Worktime> foundWorktimes = underTest.findByDayBetweenAndEmployeeIdInAndSubprojectIdIn(
                LocalDate.of(2022, 12, 1), LocalDate.of(2022, 12, 2),
                List.of(testEmployee.getId()), List.of(testSubproject.getId())
        );
        // then
        assert(foundWorktimes.isEmpty());
    }

    @Test
    void findByDayBetweenAndEmployeeInInAndSubprojectIdIn_invalidEmployee_emptyList() {
        // given
        Employee unrelatedEmployee = new Employee();
        unrelatedEmployee.setFirstName("unrelatedFirstName");
        unrelatedEmployee.setLastName("unrelatedLastName");
        unrelatedEmployee = employeeRepository.save(unrelatedEmployee);
        Worktime unrelatedWorktime = new Worktime();
        unrelatedWorktime.setEmployee(unrelatedEmployee);
        unrelatedWorktime.setSubproject(testSubproject);
        unrelatedWorktime.setDay(LocalDate.of(2022, 12, 3));
        underTest.save(unrelatedWorktime);
        // when
        List<Worktime> foundWorktimes = underTest.findByDayBetweenAndEmployeeIdInAndSubprojectIdIn(
                LocalDate.of(2022, 12,3), LocalDate.of(2022, 12,3),
                List.of(testEmployee.getId()), List.of(testSubproject.getId())
        );
        // then
        assertTrue(foundWorktimes.isEmpty());
    }

    @Test
    void findByDayBetweenAndEmployeeInInAndSubprojectIdIn_invalidSubproject_emptyList() {
        // given
        Subproject unrelatedSubproject = new Subproject();
        unrelatedSubproject.setProject(testProject);
        unrelatedSubproject.setName("unrelated");
        subprojectRepository.save(unrelatedSubproject);
        Worktime unreleatedWorktime = new Worktime();
        unreleatedWorktime.setDay(LocalDate.of(2022, 12, 3));
        unreleatedWorktime.setEmployee(testEmployee);
        unreleatedWorktime.setSubproject(unrelatedSubproject);
        underTest.save(unreleatedWorktime);
        // when
        List<Worktime> foundWorktimes = underTest.findByDayBetweenAndEmployeeIdInAndSubprojectIdIn(
                LocalDate.of(2022, 12,3), LocalDate.of(2022, 12,3),
                List.of(testEmployee.getId()), List.of(testProject.getId())
        );
        // then
        assertTrue(foundWorktimes.isEmpty());
    }
}