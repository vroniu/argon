package src.argon.argon.service;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.argon.argon.dao.Organization2EmployeeDao;
import src.argon.argon.dto.EmployeeWithPositionDTO;
import src.argon.argon.dto.OrganizationDTO;
import src.argon.argon.entity.Employee;
import src.argon.argon.entity.Organization;
import src.argon.argon.mapper.OrganizationMapper;
import src.argon.argon.repository.OrganizationRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class OrganizationService {
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    OrganizationMapper organizationMapper;
    @Autowired
    Organization2EmployeeDao organization2EmployeeDao;

    public List<OrganizationDTO> getOrganizationsOwnedByUser(Long userId) {
        return organizationMapper.toDTO(organizationRepository.findByOwnersId(userId));
    }

    public List<OrganizationDTO> getOrganizationsJoinedByUser(Long userId) {
        return organizationMapper.toDTO(organizationRepository.findByEmployeesId(userId));
    }

    public OrganizationDTO getOrganizationById(Long id) {
        Organization organization = organizationRepository.getById(id);
        return organizationMapper.toDTO(organization);
    }

    public OrganizationDTO create(OrganizationDTO organizationDTO, Employee owner) {
        Organization organization = organizationMapper.toEntity(organizationDTO);
        organization.setOwners(List.of(owner));
        organization.setEmployees(List.of(owner));
        organization = organizationRepository.saveAndFlush(organization);
        organization2EmployeeDao.updateEmployeeInfo(organization.getId(), owner.getId(), "Owner", LocalDate.now());
        return organizationMapper.toDTO(organization);
    }

    public OrganizationDTO update(OrganizationDTO organizationDTO) {
        organizationRepository.updateOrganizationFields(organizationDTO.getId(), organizationDTO.getName());
        return organizationDTO;
    }

    public EmployeeWithPositionDTO updateEmployeePosition(EmployeeWithPositionDTO employee, Long organizationId) {
        organization2EmployeeDao.updateEmployeeInfo(organizationId, employee.getId(), employee.getPosition());
        return employee;
    }

    public void deleteEmployeeFromOrganization(Long organizationId, Long employeeId) {
        organization2EmployeeDao.deleteEmployeeFromOrganization(organizationId, employeeId);
    }

    public Boolean employeeOwnsOrganization(Long emploeeId, Long organizationId) {
        return organizationRepository.getById(organizationId).getOwners().stream()
                .filter(owner -> owner.getId() == emploeeId)
                .findFirst().orElse(null) != null;
    }

    public Boolean employeeJoinedOrganization(Long emploeeId, Long organizationId) {
        return organizationRepository.getById(organizationId).getEmployees().stream()
                .filter(owner -> owner.getId() == emploeeId)
                .findFirst().orElse(null) != null;
    }

    public List<EmployeeWithPositionDTO> getEmployeesWithPositions(Long organizationId) {
        return organization2EmployeeDao.getEmployeesInfoForOrganization(organizationId);
    }

    public EmployeeWithPositionDTO getEmployeeInfo(Long organizationId, Long emploeeId) {
        return organization2EmployeeDao.getEmployeeInfoForOrganization(organizationId, emploeeId);
    }
}
