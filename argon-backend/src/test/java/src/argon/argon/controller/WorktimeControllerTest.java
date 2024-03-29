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
import src.argon.argon.dto.OrganizationDTO;
import src.argon.argon.dto.WorktimeDTO;
import src.argon.argon.security.models.JsonResponse;
import src.argon.argon.security.service.JWTUtilService;
import src.argon.argon.security.service.UserService;
import src.argon.argon.service.OrganizationService;
import src.argon.argon.service.WorktimeService;
import src.argon.argon.testutils.AuthBypassUtils;
import src.argon.argon.testutils.LocalDateGsonAdapter;
import src.argon.argon.testutils.TestDataCreator;
import src.argon.argon.testutils.WithMockCustomUser;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class WorktimeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorktimeService worktimeService;

    @MockBean
    JWTUtilService jwtUtilService;

    @MockBean
    UserService userService;

    @MockBean
    OrganizationService organizationService;

    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateGsonAdapter()).create();

    @BeforeEach
    void setUp() {
        AuthBypassUtils.bypassAuthForUser("user", "token", jwtUtilService, userService);
    }

    @Test
    @WithMockCustomUser(id = "123", employeeId = "123")
    void getWorktimesAtDay_ShouldCallServiceWithParameters() throws Exception {
        List<WorktimeDTO> worktimes = TestDataCreator.createWorktimes(5, null);
        ArgumentCaptor<LocalDate> dayArgumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<Long> employeeIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> organizationIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(worktimeService.getWorktimesAtDayForUser(dayArgumentCaptor.capture(), employeeIdArgumentCaptor.capture(),
                organizationIdArgumentCaptor.capture())).thenReturn(worktimes);

        MvcResult result = mockMvc.perform(get("/worktimes/day?day=2023-01-23&organizationId=321")
                .header("Authorization", "Bearer token")
                .header("Content-Type", "application-json"))
            .andReturn();
        List<WorktimeDTO> resultResponse = gson.fromJson(result.getResponse().getContentAsString(), List.class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(5, resultResponse.size());
        assertEquals(123L, employeeIdArgumentCaptor.getValue());
        assertEquals(321L, organizationIdArgumentCaptor.getValue());
        assertEquals(2023, dayArgumentCaptor.getValue().getYear());
        assertEquals(1, dayArgumentCaptor.getValue().getMonth().getValue());
        assertEquals(23, dayArgumentCaptor.getValue().getDayOfMonth());
    }

    @Test
    @WithMockCustomUser(id = "123", employeeId = "123")
    void getWorktimesAtDateRange_ShouldCallServiceWithParameters() throws Exception {
        List<WorktimeDTO> worktimes = TestDataCreator.createWorktimes(5, null);
        ArgumentCaptor<LocalDate> rangeStartArgumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> rangeEndArgumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<Long> employeeIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> organizationIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(worktimeService.getWorktimesAtDateRangeForUser(rangeStartArgumentCaptor.capture(), rangeEndArgumentCaptor.capture(),
                employeeIdArgumentCaptor.capture(), organizationIdArgumentCaptor.capture())).thenReturn(worktimes);

        MvcResult result = mockMvc.
                perform(get("/worktimes/range?rangeStart=2023-01-23&rangeEnd=2024-02-29&organizationId=321")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application-json"))
                .andReturn();
        List<WorktimeDTO> resultResponse = gson.fromJson(result.getResponse().getContentAsString(), List.class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(5, resultResponse.size());
        assertEquals(123L, employeeIdArgumentCaptor.getValue());
        assertEquals(321L, organizationIdArgumentCaptor.getValue());
        assertEquals(2023, rangeStartArgumentCaptor.getValue().getYear());
        assertEquals(1, rangeStartArgumentCaptor.getValue().getMonth().getValue());
        assertEquals(23, rangeStartArgumentCaptor.getValue().getDayOfMonth());
        assertEquals(2024, rangeEndArgumentCaptor.getValue().getYear());
        assertEquals(2, rangeEndArgumentCaptor.getValue().getMonth().getValue());
        assertEquals(29, rangeEndArgumentCaptor.getValue().getDayOfMonth());
    }

    @Test
    @WithMockCustomUser(id = "123", employeeId = "123")
    void getFilteredWorktimesForOrganization_ShouldThrowException_IfUserDoesNotOwnOrganization() throws Exception {
        EmployeeDTO owner = TestDataCreator.createEmployeeDtos(1).get(0);
        OrganizationDTO organization = TestDataCreator.createOrganizationDTOs(1).get(0);
        organization.setOwners(List.of(owner));
        when(organizationService.getOrganizationById(321L)).thenReturn(organization);

        MvcResult result = mockMvc.perform(
                get("/worktimes/filter/321?rangeStart=2023-01-01&rangeEnd=2023-01-01&employeeIds=[123,124]&subprojectIds=[1,2]")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json"))
                .andReturn();
        JsonResponse resultResponse = gson.fromJson(result.getResponse().getContentAsString(), JsonResponse.class);

        assertEquals(401, result.getResponse().getStatus());
        assertEquals("NOT_AN_OWNER", resultResponse.getStatus());
        assertEquals("Only owners can display full list of worktimes", resultResponse.getDescription());
    }

    @Test
    @WithMockCustomUser(id = "123", employeeId = "123")
    void getFilteredWorktimesForOrganization_ShouldCallServiceWithParameters() throws Exception {
        List<WorktimeDTO> worktimes = TestDataCreator.createWorktimes(5, null);
        EmployeeDTO owner = TestDataCreator.createEmployeeDtos(1).get(0);
        owner.setId(123L);
        OrganizationDTO organization = TestDataCreator.createOrganizationDTOs(1).get(0);
        ArgumentCaptor<LocalDate> startDateArgumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> endDateArgumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<List<Long>> employeeIdsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<Long>> subprojectIdsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        organization.setOwners(List.of(owner));
        when(organizationService.getOrganizationById(321L)).thenReturn(organization);
        when(worktimeService.getWorktimesAtDateRangeForEmployeesInSubprojects(startDateArgumentCaptor.capture(),
                endDateArgumentCaptor.capture(), employeeIdsArgumentCaptor.capture(), subprojectIdsArgumentCaptor.capture()))
                .thenReturn(worktimes);

        MvcResult result = mockMvc.perform(
                        get("/worktimes/filter/321?rangeStart=2023-01-02&rangeEnd=2023-02-03&employeeIds=[123,124]&subprojectIds=[1,2]")
                                .header("Authorization", "Bearer token")
                                .header("Content-Type", "application/json"))
                .andReturn();
        List<WorktimeDTO> resultResponse = gson.fromJson(result.getResponse().getContentAsString(), List.class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(5, resultResponse.size());
        assertEquals(2023, startDateArgumentCaptor.getValue().getYear());
        assertEquals(1, startDateArgumentCaptor.getValue().getMonthValue());
        assertEquals(2, startDateArgumentCaptor.getValue().getDayOfMonth());
        assertEquals(2023, endDateArgumentCaptor.getValue().getYear());
        assertEquals(2, endDateArgumentCaptor.getValue().getMonthValue());
        assertEquals(3, endDateArgumentCaptor.getValue().getDayOfMonth());
        assertEquals(2, employeeIdsArgumentCaptor.getValue().size());
        assertTrue(employeeIdsArgumentCaptor.getValue().contains(123L));
        assertTrue(employeeIdsArgumentCaptor.getValue().contains(124L));
        assertEquals(2, subprojectIdsArgumentCaptor.getValue().size());
        assertTrue(subprojectIdsArgumentCaptor.getValue().contains(1L));
        assertTrue(subprojectIdsArgumentCaptor.getValue().contains(2L));
    }

    @Test
    @WithMockCustomUser(id = "123", employeeId = "123")
    void createWorktime_ShouldThrowException_IfServiceThrowsException() throws Exception {
        WorktimeDTO worktime = TestDataCreator.createWorktimes(1, null).get(0);
        worktime.setId(null);
        when(worktimeService.save(any(WorktimeDTO.class)))
                .thenThrow(new IllegalArgumentException("Cant assign worktime to a deleted subproject/project"));

        MvcResult result = mockMvc.perform(post("/worktimes")
                        .header("Authorization", "Bearer token")
                        .header("Content-Type", "application/json")
                        .content(gson.toJson(worktime)))
                .andReturn();
        JsonResponse resultResponse = gson.fromJson(result.getResponse().getContentAsString(), JsonResponse.class);

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("PROJECT_DELETED", resultResponse.getStatus());
        assertEquals("Cant assign worktime to a deleted subproject/project", resultResponse.getDescription());
    }

    @Test
    @WithMockCustomUser(id = "123", employeeId = "123")
    void createWorktime_ShouldReturnCreatedException() throws Exception {
        WorktimeDTO worktime = TestDataCreator.createWorktimes(1, null).get(0);
        worktime.setId(null);
        WorktimeDTO createdWorktime = TestDataCreator.createWorktimes(1, null).get(0);
        ArgumentCaptor<WorktimeDTO> worktimeArgumentCaptor = ArgumentCaptor.forClass(WorktimeDTO.class);
        when(worktimeService.save(worktimeArgumentCaptor.capture())).thenReturn(createdWorktime);

        MvcResult result = mockMvc.perform(post("/worktimes")
                .header("Authorization", "Bearer token")
                .header("Content-Type", "application/json")
                .content(gson.toJson(worktime)))
            .andReturn();
        WorktimeDTO resultResponse = gson.fromJson(result.getResponse().getContentAsString(), WorktimeDTO.class);

        assertEquals(201, result.getResponse().getStatus());
        assertEquals(createdWorktime.getId(), resultResponse.getId());
        assertEquals(createdWorktime.getDay(), resultResponse.getDay());
        assertEquals(createdWorktime.getEmployeeName(), resultResponse.getEmployeeName());
        assertEquals(createdWorktime.getComment(), resultResponse.getComment());
        assertEquals(123L, worktimeArgumentCaptor.getValue().getEmployeeId());
        assertNull(worktimeArgumentCaptor.getValue().getId());
    }

    @Test
    void updateWorktime_ShouldReturnUpdatedWorktime() throws Exception {
        WorktimeDTO worktime = TestDataCreator.createWorktimes(1, null).get(0);
        when(worktimeService.save(any(WorktimeDTO.class))).thenReturn(worktime);

        MvcResult result = mockMvc.perform(put("/worktimes")
                .header("Authorization", "Bearer token")
                .header("Content-Type", "application/json")
                .content(gson.toJson(worktime)))
            .andReturn();
        WorktimeDTO resultResponse = gson.fromJson(result.getResponse().getContentAsString(), WorktimeDTO.class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(worktime.getId(), resultResponse.getId());
        assertEquals(worktime.getDay(), resultResponse.getDay());
        assertEquals(worktime.getEmployeeName(), resultResponse.getEmployeeName());
        assertEquals(worktime.getComment(), resultResponse.getComment());
    }

    @Test
    void deleteWorktime_ShouldCallService() throws Exception {
        ArgumentCaptor<Long> worktimeIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        MvcResult result = mockMvc.perform(delete("/worktimes/123")
                .header("Authorization", "Bearer token"))
            .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        verify(worktimeService, times(1)).deleteWorktime(worktimeIdArgumentCaptor.capture());
        assertEquals(123L, worktimeIdArgumentCaptor.getValue());
    }
}