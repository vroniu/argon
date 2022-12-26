package src.argon.argon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import src.argon.argon.dto.EmployeeDTO;
import src.argon.argon.dto.EmployeeWithPositionDTO;
import src.argon.argon.dto.OrganizationDTO;
import src.argon.argon.security.models.User;
import src.argon.argon.service.OrganizationService;

import java.util.List;

@RestController
@RequestMapping("organizations")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    @GetMapping("/owned")
    public List<OrganizationDTO> getOrganizationsOwnedByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return organizationService.getOrganizationsOwnedByUser(user.getId());
    }

    @GetMapping("/joined")
    public List<OrganizationDTO> getOrganizationsJoinedByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return organizationService.getOrganizationsJoinedByUser(user.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDTO> getOrganizationById(@PathVariable Long id) {
        OrganizationDTO result = organizationService.getOrganizationById(id);
        if (result != null) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<OrganizationDTO> createOrganization(@RequestBody OrganizationDTO organizationDTO, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(201).body(organizationService.create(organizationDTO, user.getEmployee()));
    }

    @PutMapping("")
    public ResponseEntity<OrganizationDTO> updateOrganization(@RequestBody OrganizationDTO organizationDTO) {
        return ResponseEntity.status(200).body(organizationService.update(organizationDTO));
    }

    @PostMapping("/{id}/employees")
    public ResponseEntity<EmployeeWithPositionDTO> addEmployeeToOrganization(@PathVariable Long id, @RequestBody EmployeeDTO employee) {
        OrganizationDTO organization = new OrganizationDTO();
        organization.setId(id);
        return ResponseEntity.ok().body(organizationService.addEmployeeToOrganization(employee, organization));
    }

    @GetMapping("/{id}/employees")
    public List<EmployeeWithPositionDTO> getEmployeesInfoForOrganization(@PathVariable Long id) {
        return organizationService.getEmployeesWithPositions(id);
    }

    @GetMapping("/{id}/employees/me")
    public ResponseEntity<EmployeeWithPositionDTO> getEmployeeInfoForUser(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok().body(organizationService.getEmployeeInfo(id, user.getEmployee().getId()));
    }

    @PutMapping("/{id}/employees")
    public ResponseEntity<EmployeeWithPositionDTO> updateEmployeeInfoForOrganization(@PathVariable Long id, @RequestBody EmployeeWithPositionDTO employee) {
        return ResponseEntity.ok().body(organizationService.updateEmployeePosition(employee, id));
    }

    @DeleteMapping("/{id}/employees/{employeeId}")
    public void deleteEmployeeFromOrganization(@PathVariable Long id, @PathVariable Long employeeId) {
        organizationService.deleteEmployeeFromOrganization(id, employeeId);
    }
}
