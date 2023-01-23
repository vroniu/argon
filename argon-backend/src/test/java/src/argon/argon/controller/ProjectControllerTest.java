package src.argon.argon.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import src.argon.argon.dto.ProjectDTO;
import src.argon.argon.security.service.JWTUtilService;
import src.argon.argon.security.service.UserService;
import src.argon.argon.service.ProjectService;
import src.argon.argon.testutils.AuthBypassUtils;
import src.argon.argon.testutils.TestDataCreator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProjectService projectService;

    @MockBean
    JWTUtilService jwtUtilService;

    @MockBean
    UserService userService;

    Gson gson = new Gson();

    @BeforeEach
    void setUp() {
        AuthBypassUtils.bypassAuthForUser("user", "token", jwtUtilService, userService);
    }

    @Test
    void getProjectsForOrganization_ShouldReturnListOfProjects() throws Exception {
        List<ProjectDTO> projects = TestDataCreator.createProjectDTOs(5);
        ArgumentCaptor<Long> organizationIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(projectService.getProjectsForOrganization(organizationIdArgumentCaptor.capture()))
                .thenReturn(projects);

        MvcResult result = mockMvc.perform(get("/projects/organization/111")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json"))
                .andReturn();
        List<ProjectDTO> resultResponse = gson.fromJson(result.getResponse().getContentAsString(), List.class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(5, resultResponse.size());
        assertEquals(111L, organizationIdArgumentCaptor.getValue());
    }

    @Test
    void createProject_ShouldReturnCreatedProject() throws Exception {
        ProjectDTO project = TestDataCreator.createProjectDTOs(1).get(0);
        project.setId(null);
        ProjectDTO createdProject = TestDataCreator.createProjectDTOs(1).get(0);
        ArgumentCaptor<ProjectDTO> projectArgumentCaptor = ArgumentCaptor.forClass(ProjectDTO.class);
        when(projectService.save(projectArgumentCaptor.capture())).thenReturn(createdProject);

        MvcResult result = mockMvc.perform(post("/projects")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json")
                        .content(gson.toJson(project)))
                .andReturn();
        ProjectDTO resultResponse = gson.fromJson(result.getResponse().getContentAsString(), ProjectDTO.class);

        assertEquals(201, result.getResponse().getStatus());
        assertEquals(project.getId(), projectArgumentCaptor.getValue().getId());
        assertEquals(project.getName(), projectArgumentCaptor.getValue().getName());
        assertEquals(createdProject.getId(), resultResponse.getId());
        assertEquals(createdProject.getName(), resultResponse.getName());
    }

    @Test
    void updateProject_ShouldReturnUpdatedProject() throws Exception {
        ProjectDTO project = TestDataCreator.createProjectDTOs(1).get(0);
        ArgumentCaptor<ProjectDTO> projectArgumentCaptor = ArgumentCaptor.forClass(ProjectDTO.class);
        when(projectService.save(projectArgumentCaptor.capture())).thenReturn(project);

        MvcResult result = mockMvc.perform(put("/projects")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json")
                        .content(gson.toJson(project)))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(project.getId(), projectArgumentCaptor.getValue().getId());
        assertEquals(project.getName(), projectArgumentCaptor.getValue().getName());
    }

    @Test
    void deleteProject_ShouldCallService() throws Exception {
        ArgumentCaptor<Long> projectIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        MvcResult result = mockMvc.perform(delete("/projects/123")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json"))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        verify(projectService, times(1)).delete(projectIdArgumentCaptor.capture());
        assertEquals(123L, projectIdArgumentCaptor.getValue());
    }
}