package src.argon.argon.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import src.argon.argon.entity.Organization;
import src.argon.argon.entity.Project;
import src.argon.argon.entity.Subproject;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SubprojectRepositoryTest {

    @Autowired
    SubprojectRepository underTest;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Test
    void findAllByProjectOrganizationId_subprojectBelongsToOrganization_findsSubproject() {
        // given
        Organization testOrganization = new Organization();
        testOrganization.setName("testOrganization");
        testOrganization = organizationRepository.save(testOrganization);
        Project organizationProject = new Project();
        organizationProject.setName("organizationProject");
        organizationProject.setOrganization(testOrganization);
        organizationProject = projectRepository.save(organizationProject);
        Subproject subprojectToFind = new Subproject();
        subprojectToFind.setName("subprojectToFind");
        subprojectToFind.setProject(organizationProject);
        underTest.save(subprojectToFind);

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
        underTest.save(unrelatedSubproject);
        // when
        List<Subproject> foundSubprojects = underTest.findAllByProjectOrganizationId(testOrganization.getId());
        // then
        assertTrue(foundSubprojects.contains(subprojectToFind));
        assertFalse(foundSubprojects.contains(unrelatedSubproject));
    }

    @Test
    void findAllByProjectOrganizationId_subprojectDoesntBelongsToOrganization_emptyList() {
        // given
        Organization testOrganization = new Organization();
        testOrganization.setName("testOrganization");
        testOrganization = organizationRepository.save(testOrganization);
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
        underTest.save(unrelatedSubproject);
        // when
        List<Subproject> foundSubprojects = underTest.findAllByProjectOrganizationId(testOrganization.getId());
        // then
        assertTrue(foundSubprojects.isEmpty());
    }
}