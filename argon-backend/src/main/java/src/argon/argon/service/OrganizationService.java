package src.argon.argon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.argon.argon.dao.Organization2EmployeeDao;
import src.argon.argon.dto.EmployeeDTO;
import src.argon.argon.dto.EmployeeWithPositionDTO;
import src.argon.argon.dto.OrganizationDTO;
import src.argon.argon.entity.Employee;
import src.argon.argon.entity.Organization;
import src.argon.argon.mapper.EmployeeMapper;
import src.argon.argon.mapper.OrganizationMapper;
import src.argon.argon.repository.OrganizationRepository;

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
    EmployeeMapper employeeMapper;
    @Autowired
    Organization2EmployeeDao organization2EmployeeDao;

    public List<OrganizationDTO> getOrganizationsOwnedByUser(Long userId) {
        return organizationMapper.toDTO(organizationRepository.findByOwnersId(userId));
    }

    public List<OrganizationDTO> getOrganizationsJoinedByUser(Long userId) {
        return organizationMapper.toDTO(organizationRepository.findByEmployeesId(userId));
    }

    public OrganizationDTO getOrganizationById(Long id) {
        return organizationMapper.toDTO(organizationRepository.getById(id));
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

    public EmployeeWithPositionDTO addEmployeeToOrganization(EmployeeDTO employee, OrganizationDTO organization) {
        organization2EmployeeDao.addEmployeeToOrganization(organization.getId(), employee.getId(), LocalDate.now(), "Member");
        return getEmployeeInfo(organization.getId(), employee.getId());
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

    public EmployeeWithPositionDTO getEmployeeInfo(Long organizationId, Long employeeId) {
        return organization2EmployeeDao.getEmployeeInfoForOrganization(organizationId, employeeId);
    }

    public EmployeeDTO promoteEmployeeToOwner(Long organizationId, EmployeeDTO employee) throws IllegalArgumentException {
        if (!employeeJoinedOrganization(employee.getId(), organizationId)) {
            throw new IllegalArgumentException("This employee does not belong to this organization");
        }
        if (employeeOwnsOrganization(employee.getId(), organizationId)) {
            throw new IllegalArgumentException("This employee is already marked as owner of this organization");
        }
        Organization organization = organizationRepository.getById(organizationId);
        organization.getOwners().add(employeeMapper.toEntity(employee));
        organization = organizationRepository.save(organization);
        return employeeMapper.toDTO(
                organization.getOwners().stream()
                        .filter(organizationOwner -> organizationOwner.getId() == employee.getId())
                        .findFirst().get());
    }

    public EmployeeDTO demoteEmployeeFromOwner(Long organizationId, EmployeeDTO employee) throws IllegalArgumentException {
        if (!employeeJoinedOrganization(employee.getId(), organizationId)) {
            throw new IllegalArgumentException("This employee does not belong to this organization");
        }
        if (!employeeOwnsOrganization(employee.getId(), organizationId)) {
            throw new IllegalArgumentException("This employee does not own this organization");
        }
        Organization organization = organizationRepository.getById(organizationId);
        if (organization.getOwners().size() == 1) {
            throw new IllegalArgumentException("Cannot demote the only owner of organization");
        }
        organization.getOwners().remove(employeeMapper.toEntity(employee));
        organization = organizationRepository.save(organization);
        return employeeMapper.toDTO(
                organization.getEmployees().stream()
                        .filter(organizationEmployee -> organizationEmployee.getId() == employee.getId())
                        .findFirst().get());
    }
}
