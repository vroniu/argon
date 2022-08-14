package src.argon.argon.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import src.argon.argon.entity.Organization;
import src.argon.argon.entity.Project;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    ProjectRepository underTest;

    @Autowired
    OrganizationRepository organizationRepository;

    @Test
    void findAllByOrganizationId_belongsToOrganization_findsProject() {
        // given
        Organization testOrganization = new Organization();
        testOrganization.setName("testOrganization");
        testOrganization = organizationRepository.save(testOrganization);
        Organization anotherOrganization = new Organization();
        anotherOrganization.setName("anotherOrganization");
        anotherOrganization = organizationRepository.save(anotherOrganization);
        Project projectToFind = new Project();
        projectToFind.setName("projectToFind");
        projectToFind.setOrganization(testOrganization);
        Project anotherProject = new Project();
        anotherProject.setName("anotherProject");
        anotherProject.setOrganization(anotherOrganization);
        underTest.save(projectToFind);
        underTest.save(anotherProject);
        // when
        List<Project> foundProjects = underTest.findAllByOrganizationId(testOrganization.getId());
        // then
        assertTrue(foundProjects.contains(projectToFind));
        assertFalse(foundProjects.contains(anotherProject));
    }

    @Test
    void findAllByOrganizationId_doesntBelongToOrganization_emptyList() {
        // given
        Organization testOrganization = new Organization();
        testOrganization.setName("testOrganization");
        testOrganization = organizationRepository.save(testOrganization);
        Organization unrelatedOrganization = new Organization();
        unrelatedOrganization.setName("anotherOrganization");
        unrelatedOrganization = organizationRepository.save(unrelatedOrganization);
        Project unrelatedProject = new Project();
        unrelatedProject.setName("unrelatedProject");
        unrelatedProject.setOrganization(unrelatedOrganization);
        underTest.save(unrelatedProject);
        // when
        List<Project> foundProjects = underTest.findAllByOrganizationId(testOrganization.getId());
        // then
        assertTrue(foundProjects.isEmpty());
    }
}