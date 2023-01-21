package src.argon.argon.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import src.argon.argon.security.service.JWTUtilService;
import src.argon.argon.security.service.UserService;
import src.argon.argon.service.EmployeeService;
import src.argon.argon.testutils.AuthBypassUtils;
import src.argon.argon.testutils.TestDataCreator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    @MockBean
    JWTUtilService jwtUtilService;

    @MockBean
    UserService userService;

    @BeforeEach
    void setUp() {
        AuthBypassUtils.bypassAuthForUser("user", "token", jwtUtilService, userService);
    }

    Gson gson = new Gson();

    Type employeeDtoListType = new TypeToken<ArrayList<EmployeeDTO>>() {}.getType();

    @Test
    void getAllEmployees_ShouldCallServiceWithNullParameters_IfNoRequestParamsProvided() throws Exception {
        ArgumentCaptor<String> nameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List<Long>> excludedOrganizationsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        List<EmployeeDTO> employees = TestDataCreator.createEmployeeDtos(5);
        when(employeeService.getEmployees(nameArgumentCaptor.capture(), emailArgumentCaptor.capture(),
                excludedOrganizationsArgumentCaptor.capture())).thenReturn(employees);

        MvcResult result = mockMvc.perform(get("/employees").header("Authorization", "Bearer token")).andReturn();
        List<EmployeeDTO> resultResponse = gson.fromJson(result.getResponse().getContentAsString(), employeeDtoListType);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(5, resultResponse.size());
        assertNull(nameArgumentCaptor.getValue());
        assertNull(emailArgumentCaptor.getValue());
        assertTrue(excludedOrganizationsArgumentCaptor.getValue().isEmpty());
    }

    @Test
    void getAllEmployees_ShouldCallServiceWithListOfExcludedOrganizations_IfParamProvided() throws Exception {
        ArgumentCaptor<List<Long>> excludedOrganizationsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        List<EmployeeDTO> employees = TestDataCreator.createEmployeeDtos(5);
        when(employeeService.getEmployees(isNull(), isNull(),
                excludedOrganizationsArgumentCaptor.capture())).thenReturn(employees);

        MvcResult result = mockMvc.perform(get("/employees").header("Authorization", "Bearer token").param("excludeOrganizations", "[1,2,3]")).andReturn();
        List<EmployeeDTO> resultResponse = gson.fromJson(result.getResponse().getContentAsString(), employeeDtoListType);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(5, resultResponse.size());
        assertEquals(3, excludedOrganizationsArgumentCaptor.getValue().size());
    }


}