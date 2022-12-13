package src.argon.argon.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import src.argon.argon.dao.Organization2EmployeeDao;
import src.argon.argon.dto.EmployeeWithPositionDTO;
import src.argon.argon.dto.OrganizationDTO;
import src.argon.argon.entity.Organization;
import src.argon.argon.mapper.OrganizationMapper;
import src.argon.argon.repository.OrganizationRepository;

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
    void deleteEmployeeFromOrganization_ShouldCallDao() {
        underTest.deleteEmployeeFromOrganization(123L, 321L);
        verify(organization2EmployeeDao, times(1)).deleteEmployeeFromOrganization(123L, 321L);
    }
}