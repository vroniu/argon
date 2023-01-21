package src.argon.argon.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import src.argon.argon.entity.Employee;
import src.argon.argon.security.models.JsonResponse;
import src.argon.argon.security.service.JWTUtilService;
import src.argon.argon.security.service.UserService;
import src.argon.argon.service.OrganizationService;
import src.argon.argon.testutils.AuthBypassUtils;
import src.argon.argon.testutils.LocalDateGsonAdapter;
import src.argon.argon.testutils.TestDataCreator;
import src.argon.argon.testutils.WithMockCustomUser;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateGsonAdapter()).create();

    @BeforeEach
    void setUp() {
        AuthBypassUtils.bypassAuthForUser("user", "token", jwtUtilService, userService);
    }

    @Test
    @WithMockCustomUser(id = "123")
    void getOrganizationsOwnedByUser_ShouldReturnListOfOrganizations() throws Exception {
        List<OrganizationDTO> organizations = TestDataCreator.createOrganizationDTOs(5);
        ArgumentCaptor<Long> userIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(organizationService.getOrganizationsOwnedByUser(userIdArgumentCaptor.capture())).thenReturn(organizations);

        MvcResult result = mockMvc.perform(get("/organizations/owned")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json"))
                .andReturn();
        List<OrganizationDTO> resultResponse = gson.fromJson(result.getResponse().getContentAsString(), List.class);

        assertEquals(5, resultResponse.size());
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(123L, userIdArgumentCaptor.getValue());
    }

    @Test
    @WithMockCustomUser(id = "123")
    void getOrganizationsJoinedByUser_ShouldReturnListOfOrganizations() throws Exception {
        List<OrganizationDTO> organizations = TestDataCreator.createOrganizationDTOs(5);
        ArgumentCaptor<Long> userIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(organizationService.getOrganizationsJoinedByUser(userIdArgumentCaptor.capture())).thenReturn(organizations);

        MvcResult result = mockMvc.perform(get("/organizations/joined")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json"))
                .andReturn();
        List<OrganizationDTO> resultResponse = gson.fromJson(result.getResponse().getContentAsString(), List.class);

        assertEquals(5, resultResponse.size());
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(123L, userIdArgumentCaptor.getValue());
    }

    @Test
    void getOrganizationById_ShouldReturnOrganization() throws Exception {
        OrganizationDTO organization = TestDataCreator.createOrganizationDTOs(1).get(0);
        ArgumentCaptor<Long> organizationIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(organizationService.getOrganizationById(organizationIdArgumentCaptor.capture())).thenReturn(organization);

        MvcResult result = mockMvc.perform(get("/organizations/123")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json"))
                .andReturn();
        OrganizationDTO resultResponse = gson.fromJson(result.getResponse().getContentAsString(), OrganizationDTO.class);

        assertEquals(organization.getId(), resultResponse.getId());
        assertEquals(organization.getName(), resultResponse.getName());
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(123L, organizationIdArgumentCaptor.getValue());
    }

    @Test
    void getOrganizationById_ShouldReturn404_IfOrganizationNotFound() throws Exception {
        when(organizationService.getOrganizationById(123L)).thenReturn(null);

        MvcResult result = mockMvc.perform(get("/organizations/123")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json"))
                .andReturn();

        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    @WithMockCustomUser(id = "123", employeeId = "321")
    void createOrganization_ShouldReturnCreatedOrganization() throws Exception {
        OrganizationDTO organization = TestDataCreator.createOrganizationDTOs(1).get(0);
        organization.setId(null);
        OrganizationDTO createdOrganization = TestDataCreator.createOrganizationDTOs(1).get(0);
        ArgumentCaptor<OrganizationDTO> organizationArgumentCaptor = ArgumentCaptor.forClass(OrganizationDTO.class);
        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        when(organizationService.create(organizationArgumentCaptor.capture(), employeeArgumentCaptor.capture()))
                .thenReturn(createdOrganization);

        MvcResult result = mockMvc.perform(post("/organizations")
                .header("Authorization", "Bearer token")
                .header("Content-Type", "application/json")
                .content(gson.toJson(organization)))
            .andReturn();
        OrganizationDTO resultResponse = gson.fromJson(result.getResponse().getContentAsString(), OrganizationDTO.class);

        assertEquals(createdOrganization.getId(), resultResponse.getId());
        assertEquals(createdOrganization.getName(), resultResponse.getName());
        assertEquals(201, result.getResponse().getStatus());
        assertEquals(organization.getId(), organizationArgumentCaptor.getValue().getId());
        assertEquals(organization.getName(), organizationArgumentCaptor.getValue().getName());
        assertEquals(321L, employeeArgumentCaptor.getValue().getId());
    }

    @Test
    void updateOrganization_ShouldReturnUpdatedOrganization() throws Exception {
        OrganizationDTO organization = TestDataCreator.createOrganizationDTOs(1).get(0);
        ArgumentCaptor<OrganizationDTO> organizationArgumentCaptor = ArgumentCaptor.forClass(OrganizationDTO.class);
        when(organizationService.update(organizationArgumentCaptor.capture())).thenReturn(organization);

        MvcResult result = mockMvc.perform(put("/organizations")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json")
                        .content(gson.toJson(organization)))
                .andReturn();
        OrganizationDTO resultResponse = gson.fromJson(result.getResponse().getContentAsString(), OrganizationDTO.class);

        assertEquals(organization.getId(), resultResponse.getId());
        assertEquals(organization.getName(), resultResponse.getName());
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(organization.getId(), organizationArgumentCaptor.getValue().getId());
        assertEquals(organization.getName(), organizationArgumentCaptor.getValue().getName());
    }

    @Test
    void getEmployeesInfoForOrganization_ShouldReturnListOfEmployees() throws Exception {
        ArgumentCaptor<Long> organizationIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        List<EmployeeWithPositionDTO> employees = TestDataCreator.createEmployeesWithOrganization(5);
        when(organizationService.getEmployeesWithPositions(organizationIdArgumentCaptor.capture()))
                .thenReturn(employees);

        MvcResult result = mockMvc.perform(get("/organizations/123/employees")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json"))
                .andReturn();
        List<EmployeeWithPositionDTO> resultResponse = gson.fromJson(result.getResponse().getContentAsString(), List.class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(5, resultResponse.size());
    }

    @Test
    @WithMockCustomUser(id = "123", employeeId = "321")
    void getEmployeeInfoForUser_ShouldReturnEmployeeInfo() throws Exception {
        EmployeeWithPositionDTO employeeInfo = TestDataCreator.createEmployeesWithOrganization(1).get(0);
        ArgumentCaptor<Long> organizationIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> employeeIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(organizationService.getEmployeeInfo(organizationIdArgumentCaptor.capture(), employeeIdArgumentCaptor.capture()))
                .thenReturn(employeeInfo);

        MvcResult result = mockMvc.perform(get("/organizations/123/employees/me")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json"))
                .andReturn();
        EmployeeWithPositionDTO resultResponse = gson.fromJson(result.getResponse().getContentAsString(), EmployeeWithPositionDTO.class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(123L, organizationIdArgumentCaptor.getValue());
        assertEquals(321L, employeeIdArgumentCaptor.getValue());
        assertEquals(employeeInfo.getId(), resultResponse.getId());
        assertEquals(employeeInfo.getFirstName(), resultResponse.getFirstName());
        assertEquals(employeeInfo.getLastName(), resultResponse.getLastName());
    }

    @Test
    void updateEmployeeInfoForOrganization_ShouldReturnEmployeeInfo() throws Exception {
        EmployeeWithPositionDTO employeeInfo = TestDataCreator.createEmployeesWithOrganization(1).get(0);
        ArgumentCaptor<Long> organizationIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<EmployeeWithPositionDTO> employeeArgumentCaptor = ArgumentCaptor.forClass(EmployeeWithPositionDTO.class);
        when(organizationService.updateEmployeePosition(employeeArgumentCaptor.capture(), organizationIdArgumentCaptor.capture()))
                .thenReturn(employeeInfo);

        MvcResult result = mockMvc.perform(put("/organizations/123/employees")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json")
                        .content(gson.toJson(employeeInfo)))
                .andReturn();
        EmployeeWithPositionDTO resultReponse = gson.fromJson(result.getResponse().getContentAsString(), EmployeeWithPositionDTO.class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(123L, organizationIdArgumentCaptor.getValue());
        assertEquals(employeeInfo.getId(), resultReponse.getId());
        assertEquals(employeeInfo.getFirstName(), resultReponse.getFirstName());
        assertEquals(employeeInfo.getLastName(), resultReponse.getLastName());
        assertEquals(employeeInfo.getId(), employeeArgumentCaptor.getValue().getId());
        assertEquals(employeeInfo.getFirstName(), employeeArgumentCaptor.getValue().getFirstName());
        assertEquals(employeeInfo.getLastName(), employeeArgumentCaptor.getValue().getLastName());
    }

    @Test
    void deleteEmployeeFromOrganization_ShouldCallService() throws Exception {
        ArgumentCaptor<Long> organizationIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> employeeIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        MvcResult result = mockMvc.perform(delete("/organizations/123/employees/321")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json"))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        verify(organizationService, times(1)).deleteEmployeeFromOrganization(organizationIdArgumentCaptor.capture(), employeeIdArgumentCaptor.capture());
        assertEquals(123L, organizationIdArgumentCaptor.getValue());
        assertEquals(321L, employeeIdArgumentCaptor.getValue());
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