package src.argon.argon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import src.argon.argon.dto.ProjectDTO;
import src.argon.argon.entity.Organization;
import src.argon.argon.entity.Project;
import src.argon.argon.entity.Subproject;
import src.argon.argon.mapper.OrganizationMapper;
import src.argon.argon.mapper.ProjectMapper;
import src.argon.argon.mapper.SubprojectMapper;
import src.argon.argon.repository.ProjectRepository;
import src.argon.argon.repository.SubprojectRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProjectServiceTest {

    @InjectMocks
    ProjectService underTest;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    SubprojectRepository subprojectRepository;

    @Spy
    ProjectMapper projectMapper = Mappers.getMapper(ProjectMapper.class);

    @Spy
    SubprojectMapper subprojectMapper = Mappers.getMapper(SubprojectMapper.class);

    @Spy
    OrganizationMapper organizationMapper = Mappers.getMapper(OrganizationMapper.class);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(projectMapper, "subprojectMapper", subprojectMapper);
        ReflectionTestUtils.setField(projectMapper, "organizationMapper", organizationMapper);
    }

    @Test
    void getProjectsForOrganization_ShouldReturnListOfProjects() {
        List<Project> projects = createProjects(5);
        when(projectRepository.findAllByOrganizationId(111L)).thenReturn(projects);

        List<ProjectDTO> result = underTest.getProjectsForOrganization(111L);

        assertEquals(5, result.size());
        assertEquals("Project 4", result.get(4).getName());
        assertEquals("Test Organization", result.get(0).getOrganizationName());
    }

    @Test
    void save_ShouldReturnSavedProject() {
        Project savedProject = createProjects(1).get(0);
        savedProject.setId(123L);
        ProjectDTO projectToSave = projectMapper.toDTO(savedProject);
        projectToSave.setId(null);
        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);

        ProjectDTO result = underTest.save(projectToSave);

        assertEquals(123, result.getId());
        assertEquals("Project 0", result.getName());
        assertEquals("Test Organization", result.getOrganizationName());
    }

    @Test
    void delete_ShouldDeleteProjectAndSubprojects() {
        Project project = createProjects(1).get(0);
        Subproject subproject = new Subproject();
        subproject.setId(333L);
        subproject.setName("Project 0 Subproject");
        project.setSubprojects(List.of(subproject));
        when(projectRepository.getById(0L)).thenReturn(project);
        ArgumentCaptor<List<Subproject>> subprojectListArgumentCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Long> projectIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        underTest.delete(0L);

        verify(subprojectRepository).deleteAll(subprojectListArgumentCaptor.capture());
        verify(projectRepository).deleteById(projectIdArgumentCaptor.capture());
        assertEquals(subproject, subprojectListArgumentCaptor.getValue().get(0));
        assertEquals(0L, projectIdArgumentCaptor.getValue());
    }

    private List<Project> createProjects(int count) {
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
}