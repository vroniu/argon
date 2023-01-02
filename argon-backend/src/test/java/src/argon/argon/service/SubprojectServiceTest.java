package src.argon.argon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import src.argon.argon.dto.SubprojectDTO;
import src.argon.argon.entity.Project;
import src.argon.argon.entity.Subproject;
import src.argon.argon.mapper.ProjectMapper;
import src.argon.argon.mapper.SubprojectMapper;
import src.argon.argon.repository.ProjectRepository;
import src.argon.argon.repository.SubprojectRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SubprojectServiceTest {

    @InjectMocks
    SubprojectService underTest;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    SubprojectRepository subprojectRepository;

    @Spy
    SubprojectMapper subprojectMapper = Mappers.getMapper(SubprojectMapper.class);

    @Spy
    ProjectMapper projectMapper = Mappers.getMapper(ProjectMapper.class);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(subprojectMapper, "projectMapper", projectMapper);
    }

    @Test
    void save_ShouldReturnSavedSubproject() {
        Project parentProject = new Project();
        parentProject.setId(123L);
        parentProject.setName("Parent Project");
        SubprojectDTO subprojectToSave = new SubprojectDTO();
        subprojectToSave.setName("Subproject");
        subprojectToSave.setProjectId(123L);
        subprojectToSave.setProjectName("Parent Project");
        subprojectToSave.setOrganizationId(1L);
        Subproject savedSubproject = subprojectMapper.toEntity(subprojectToSave);
        savedSubproject.setId(111L);
        when(projectRepository.getById(123L)).thenReturn(parentProject);
        when(subprojectRepository.save(any(Subproject.class))).thenReturn(savedSubproject);

        SubprojectDTO result = underTest.save(subprojectToSave);

        assertEquals(111L, result.getId());
    }

    @Test
    void delete() {
        underTest.delete(123L);

        verify(subprojectRepository, times(1)).deleteById(123L);
    }
}