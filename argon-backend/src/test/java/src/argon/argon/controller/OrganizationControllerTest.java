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
import src.argon.argon.dto.EmployeeDTO;
import src.argon.argon.dto.EmployeeWithPositionDTO;
import src.argon.argon.dto.OrganizationDTO;
import src.argon.argon.security.models.JsonResponse;
import src.argon.argon.security.service.JWTUtilService;
import src.argon.argon.security.service.UserService;
import src.argon.argon.service.OrganizationService;
import src.argon.argon.testutils.AuthBypassUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrganizationService organizationService;

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
    void addEmployeeToOrganization_ShouldReturnResponseFromService() throws Exception {
        EmployeeDTO requestEmployee = new EmployeeDTO();
        requestEmployee.setId(123L);
        EmployeeWithPositionDTO responseEmployee = new EmployeeWithPositionDTO();
        responseEmployee.setId(123L);
        responseEmployee.setPosition("Member");
        ArgumentCaptor<EmployeeDTO> employeeArgumentCaptor = ArgumentCaptor.forClass(EmployeeDTO.class);
        ArgumentCaptor<OrganizationDTO> organizationArgumentCaptor = ArgumentCaptor.forClass(OrganizationDTO.class);
        when(organizationService.addEmployeeToOrganization(employeeArgumentCaptor.capture(), organizationArgumentCaptor.capture()))
                .thenReturn(responseEmployee);

        MvcResult result = mockMvc.perform(post("/organizations/321/employees")
                .header("Authorization", "Bearer token")
                .header("Content-Type", "application/json")
                .content(gson.toJson(requestEmployee)))
            .andReturn();
        EmployeeWithPositionDTO resultResponse = gson.fromJson(result.getResponse().getContentAsString(), EmployeeWithPositionDTO.class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(321L, organizationArgumentCaptor.getValue().getId());
        assertEquals(requestEmployee.getId(), resultResponse.getId());
        assertEquals("Member", resultResponse.getPosition());
        assertEquals(requestEmployee.getId(), employeeArgumentCaptor.getValue().getId());
    }

    @Test
    void promoteEmployeeToOwner_ShouldReturnBadRequest_IfErrorOccurs() throws Exception {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(123L);
        ArgumentCaptor<Long> organizationIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(organizationService.promoteEmployeeToOwner(organizationIdArgumentCaptor.capture(), any()))
                .thenThrow(new IllegalArgumentException("This employee does not belong to this organization"));

        MvcResult result = mockMvc.perform(post("/organizations/321/employees/promote")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json")
                        .content(gson.toJson(employee)))
                .andReturn();
        JsonResponse resultResponse = gson.fromJson(result.getResponse().getContentAsString(), JsonResponse.class);

        assertEquals(321L, organizationIdArgumentCaptor.getValue());
        assertEquals(400, result.getResponse().getStatus());
        assertEquals("PROMOTE_ERROR", resultResponse.getStatus());
        assertEquals("This employee does not belong to this organization", resultResponse.getDescription());
    }

    @Test
    void promoteEmployeeToOwner_ShouldReturnEmployee() throws Exception {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(123L);
        ArgumentCaptor<Long> organizationIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(organizationService.promoteEmployeeToOwner(organizationIdArgumentCaptor.capture(), any()))
                .thenReturn(employee);

        MvcResult result = mockMvc.perform(post("/organizations/321/employees/promote")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json")
                        .content(gson.toJson(employee)))
                .andReturn();
        EmployeeDTO resultResponse = gson.fromJson(result.getResponse().getContentAsString(), EmployeeDTO.class);

        assertEquals(321L, organizationIdArgumentCaptor.getValue());
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(123L, resultResponse.getId());
    }

    @Test
    void demoteEmployeeFromOwner_ShouldReturnBadRequest_IfErrorOccurs() throws Exception {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(123L);
        ArgumentCaptor<Long> organizationIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(organizationService.demoteEmployeeFromOwner(organizationIdArgumentCaptor.capture(), any()))
                .thenThrow(new IllegalArgumentException("This employee does not belong to this organization"));

        MvcResult result = mockMvc.perform(post("/organizations/321/employees/demote")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json")
                        .content(gson.toJson(employee)))
                .andReturn();
        JsonResponse resultResponse = gson.fromJson(result.getResponse().getContentAsString(), JsonResponse.class);

        assertEquals(321L, organizationIdArgumentCaptor.getValue());
        assertEquals(400, result.getResponse().getStatus());
        assertEquals("DEMOTE_ERROR", resultResponse.getStatus());
        assertEquals("This employee does not belong to this organization", resultResponse.getDescription());
    }

    @Test
    void demoteEmployeeToOwner_ShouldReturnEmployee() throws Exception {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(123L);
        ArgumentCaptor<Long> organizationIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(organizationService.demoteEmployeeFromOwner(organizationIdArgumentCaptor.capture(), any()))
                .thenReturn(employee);

        MvcResult result = mockMvc.perform(post("/organizations/321/employees/demote")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json")
                        .content(gson.toJson(employee)))
                .andReturn();
        EmployeeDTO resultResponse = gson.fromJson(result.getResponse().getContentAsString(), EmployeeDTO.class);

        assertEquals(321L, organizationIdArgumentCaptor.getValue());
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(123L, resultResponse.getId());
    }
}