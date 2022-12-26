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
import src.argon.argon.security.service.JWTUtilService;
import src.argon.argon.security.service.UserService;
import src.argon.argon.service.OrganizationService;
import src.argon.argon.testutils.AuthBypassUtils;

import static org.junit.jupiter.api.Assertions.*;
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
}