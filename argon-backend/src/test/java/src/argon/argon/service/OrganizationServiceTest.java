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
import src.argon.argon.dao.Organization2EmployeeDao;
import src.argon.argon.dto.EmployeeDTO;
import src.argon.argon.dto.EmployeeWithPositionDTO;
import src.argon.argon.dto.OrganizationDTO;
import src.argon.argon.entity.Employee;
import src.argon.argon.entity.Organization;
import src.argon.argon.mapper.EmployeeMapper;
import src.argon.argon.mapper.OrganizationMapper;
import src.argon.argon.mapper.ProjectMapper;
import src.argon.argon.repository.OrganizationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrganizationServiceTest {

    @InjectMocks
    private OrganizationService underTest;

    @Mock
    OrganizationRepository organizationRepository;

    @Mock
    Organization2EmployeeDao organization2EmployeeDao;

    @Spy
    OrganizationMapper organizationMapper = Mappers.getMapper(OrganizationMapper.class);

    @Spy
    ProjectMapper projectMapper = Mappers.getMapper(ProjectMapper.class);

    @Spy
    EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(organizationMapper, "projectMapper", projectMapper);
        ReflectionTestUtils.setField(organizationMapper, "employeeMapper", employeeMapper);
    }

    @Test
    void update_ShouldReturnUpdatedDto() {
        OrganizationDTO organizationToUpdate = new OrganizationDTO();
        organizationToUpdate.setId(1L);
        organizationToUpdate.setName("Test Organization");
        when(organizationRepository.updateOrganizationFields(1L, "Test Organization")).thenReturn(0);

        OrganizationDTO updatedOrganizationDto = underTest.update(organizationToUpdate);

        assertEquals(1L, updatedOrganizationDto.getId());
        assertEquals("Test Organization", updatedOrganizationDto.getName());
        verify(organizationRepository, times(1)).updateOrganizationFields(1L, "Test Organization");
    }

    @Test
    void updateEmployeePosition_ShouldCallDao() {
        EmployeeWithPositionDTO employeeToUpdate = new EmployeeWithPositionDTO();
        employeeToUpdate.setPosition("New position");
        employeeToUpdate.setId(123L);

        EmployeeWithPositionDTO result = underTest.updateEmployeePosition(employeeToUpdate, 1234L);

        assertEquals("New position", result.getPosition());
        assertEquals(123L, result.getId());
        verify(organization2EmployeeDao, times(1)).updateEmployeeInfo(1234L, 123L, "New position");
    }

    @Test
    void addEmployeeToOrganization_ShouldCallDaoAndReturnAddedEmployee() {
        OrganizationDTO organization = new OrganizationDTO();
        organization.setId(321L);
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(123L);
        EmployeeWithPositionDTO employeeWithPosition = new EmployeeWithPositionDTO();
        employeeWithPosition.setId(123L);
        when(organization2EmployeeDao.getEmployeeInfoForOrganization(321L, 123L)).thenReturn(employeeWithPosition);

        EmployeeWithPositionDTO result = underTest.addEmployeeToOrganization(employee, organization);

        verify(organization2EmployeeDao, times(1)).addEmployeeToOrganization(eq(321L), eq(123L), any(LocalDate.class), eq("Member"));
        assertEquals(employeeWithPosition, result);
    }

    @Test
    void deleteEmployeeFromOrganization_ShouldCallDao() {
        underTest.deleteEmployeeFromOrganization(123L, 321L);
        verify(organization2EmployeeDao, times(1)).deleteEmployeeFromOrganization(123L, 321L);
    }

    @Test
    void getOrganizationsOwnedByUser_ShouldReturnListOfOrganizations() {
        List<Organization> ownedOrganizations = createOrganizations(5);
        when(organizationRepository.findByOwnersId(321L)).thenReturn(ownedOrganizations);

        List<OrganizationDTO> result = underTest.getOrganizationsOwnedByUser(321L);

        assertEquals(5, result.size());
    }

    @Test
    void getOrganizationsJoinedByUser_ShouldReturnListOfOrganizations() {
        List<Organization> ownedOrganizations = createOrganizations(5);
        when(organizationRepository.findByEmployeesId(321L)).thenReturn(ownedOrganizations);

        List<OrganizationDTO> result = underTest.getOrganizationsJoinedByUser(321L);

        assertEquals(5, result.size());
    }

    @Test
    void getOrganizationById_ShouldReturnOrganizationDTO() {
        when(organizationRepository.getById(321L)).thenReturn(createOrganizations(1).get(0));

        OrganizationDTO result = underTest.getOrganizationById(321L);

        assertEquals("Organization 0", result.getName());
        assertEquals(0, result.getId());
    }

    @Test
    void create_ShouldReturnCreatedOrganization() {
        Employee owner = new Employee();
        owner.setId(111L);
        owner.setFirstName("First");
        owner.setLastName("Last");
        Organization savedOrganization = createOrganizations(1).get(0);
        ArgumentCaptor<Organization> organizationArgumentCaptor = ArgumentCaptor.forClass(Organization.class);
        OrganizationDTO organizationToSave = organizationMapper.toDTO(savedOrganization);
        organizationToSave.setId(null);
        when(organizationRepository.saveAndFlush(organizationArgumentCaptor.capture())).thenReturn(savedOrganization);

        OrganizationDTO result = underTest.create(organizationToSave, owner);

        assertEquals("Organization 0", organizationArgumentCaptor.getValue().getName());
        assertEquals(1, organizationArgumentCaptor.getValue().getEmployees().size());
        assertEquals(1, organizationArgumentCaptor.getValue().getOwners().size());
        assertEquals(111L, organizationArgumentCaptor.getValue().getEmployees().get(0).getId());
        assertEquals(111L, organizationArgumentCaptor.getValue().getOwners().get(0).getId());
        verify(organization2EmployeeDao, times(1)).updateEmployeeInfo(eq(0L), eq(111L), eq("Owner"), any(LocalDate.class));
        assertEquals(0L, result.getId());
        assertEquals("Organization 0", result.getName());
    }

    @Test
    void getEmployeeInfo_ShouldReturnListOfEmployees() {
        List<EmployeeWithPositionDTO> employeesWithOrganization = createEmployeesWithOrganization(5);
        when(organization2EmployeeDao.getEmployeesInfoForOrganization(321L)).thenReturn(employeesWithOrganization);

        List<EmployeeWithPositionDTO> result = underTest.getEmployeesWithPositions(321L);

        assertEquals(result, employeesWithOrganization);
    }

    private List<Organization> createOrganizations(int count) {
        List<Organization> organizations = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Organization organization = new Organization();
            organization.setId((long) i);
            organization.setName("Organization " + i);
            organization.setProjects(Collections.emptyList());
            organization.setEmployees(Collections.emptyList());
            organization.setOwners(Collections.emptyList());
            organizations.add(organization);
        }
        return organizations;
    }

    private List<EmployeeWithPositionDTO> createEmployeesWithOrganization(int count) {
        List<EmployeeWithPositionDTO> employees = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            EmployeeWithPositionDTO employee = new EmployeeWithPositionDTO();
            employee.setId((long) i);
            employee.setFirstName("Employee");
            employee.setLastName(String.valueOf(i));
            employee.setPosition("Position");
            employee.setJoinedDate(LocalDate.now());
            employees.add(employee);
        }
        return employees;
    }
}