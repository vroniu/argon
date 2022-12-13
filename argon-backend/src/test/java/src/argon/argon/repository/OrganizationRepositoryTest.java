package src.argon.argon.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import src.argon.argon.entity.Employee;
import src.argon.argon.entity.Organization;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrganizationRepositoryTest {

    @Autowired
    OrganizationRepository underTest;

    @Autowired
    EmployeeRepository employeeRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    void findByOwnersId_isOwner_findsOrganization() {
        // given
        Organization organization = new Organization();
        organization.setName("testOrganization");
        Employee owner = new Employee();
        owner.setFirstName("testFirstName");
        owner.setLastName("testLastName");
        owner = employeeRepository.save(owner);
        organization.setOwners(List.of(owner));
        organization = underTest.save(organization);
        // when
        List<Organization> foundOrganizations = underTest.findByOwnersId(owner.getId());
        // then
        assertTrue(foundOrganizations.contains(organization));
    }

    @Test
    void findByOwnersId_isNotOwner_emptyList() {
        // given
        Employee owner = new Employee();
        owner.setFirstName("testFirstName");
        owner.setLastName("testLastName");
        owner = employeeRepository.save(owner);
        // when
        List<Organization> foundOrganizations = underTest.findByOwnersId(owner.getId());
        // then
        assertTrue(foundOrganizations.isEmpty());
    }

    @Test
    void findByEmployeesId_isEmployee_findsOrganization() {
        // given
        Organization organization = new Organization();
        organization.setName("testOrganization");
        Employee employee = new Employee();
        employee.setFirstName("testFirstName");
        employee.setLastName("testLastName");
        employee = employeeRepository.save(employee);
        organization.setEmployees(List.of(employee));
        organization = underTest.save(organization);
        // when
        List<Organization> foundOrganizations = underTest.findByEmployeesId(employee.getId());
        // then
        assertTrue(foundOrganizations.contains(organization));
    }

    @Test
    void findByEmployeesId_isNotEmployee_emptyList() {
        // given
        Employee employee = new Employee();
        employee.setFirstName("testFirstName");
        employee.setLastName("testLastName");
        employee = employeeRepository.save(employee);
        // when
        List<Organization> foundOrganizations = underTest.findByEmployeesId(employee.getId());
        // then
        assertTrue(foundOrganizations.isEmpty());
    }

    @Test
    void updateOrganizationFields_UpdatesOrganization() {
        Organization organization = new Organization();
        organization.setName("Old Name");
        underTest.save(organization);
        Organization savedOrganization = underTest.findAll().get(0);

        underTest.updateOrganizationFields(savedOrganization.getId(), "New Name");
        em.clear();

        assertEquals("New Name", underTest.getById(savedOrganization.getId()).getName());
    }
}