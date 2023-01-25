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
import src.argon.argon.dto.SubprojectDTO;
import src.argon.argon.security.service.JWTUtilService;
import src.argon.argon.security.service.UserService;
import src.argon.argon.service.SubprojectService;
import src.argon.argon.testutils.AuthBypassUtils;
import src.argon.argon.testutils.TestDataCreator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class SubprojectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SubprojectService subprojectService;

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
    void createSubproject_ShouldReturnCreatedSubproject() throws Exception {
        SubprojectDTO subproject = TestDataCreator.createSubprojectDTOs(1).get(0);
        subproject.setId(null);
        SubprojectDTO createdSubproject = TestDataCreator.createSubprojectDTOs(1).get(0);
        ArgumentCaptor<SubprojectDTO> subprojectArgumentCaptor = ArgumentCaptor.forClass(SubprojectDTO.class);
        when(subprojectService.save(subprojectArgumentCaptor.capture())).thenReturn(createdSubproject);

        MvcResult result = mockMvc.perform(post("/subprojects")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json")
                        .content(gson.toJson(subproject)))
                .andReturn();
        SubprojectDTO resultResponse = gson.fromJson(result.getResponse().getContentAsString(), SubprojectDTO.class);

        assertEquals(201, result.getResponse().getStatus());
        assertEquals(subproject.getId(), subprojectArgumentCaptor.getValue().getId());
        assertEquals(subproject.getName(), subprojectArgumentCaptor.getValue().getName());
        assertEquals(createdSubproject.getId(), resultResponse.getId());
        assertEquals(createdSubproject.getName(), resultResponse.getName());
    }

    @Test
    void updateSubproject_ShouldReturnUpdatedSubproject() throws Exception {
        SubprojectDTO subproject = TestDataCreator.createSubprojectDTOs(1).get(0);
        when(subprojectService.save(any())).thenReturn(subproject);

        MvcResult result = mockMvc.perform(put("/subprojects")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json")
                        .content(gson.toJson(subproject)))
                .andReturn();
        SubprojectDTO resultResponse = gson.fromJson(result.getResponse().getContentAsString(), SubprojectDTO.class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(subproject.getId(), resultResponse.getId());
        assertEquals(subproject.getName(), resultResponse.getName());
    }

    @Test
    void deleteSubproject_ShouldCallService() throws Exception {
        ArgumentCaptor<Long> projectIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        MvcResult result = mockMvc.perform(delete("/subprojects/123")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json"))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        verify(subprojectService, times(1)).delete(projectIdArgumentCaptor.capture());
        assertEquals(123L, projectIdArgumentCaptor.getValue());
    }
}