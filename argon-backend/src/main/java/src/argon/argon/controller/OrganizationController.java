package src.argon.argon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{id}/employees")
    public List<EmployeeWithPositionDTO> getEmployeesInfoForOrganization(@PathVariable Long id) {
        return organizationService.getEmployeesWithPositions(id);
    }

    @GetMapping("/{id}/employees/me")
    public ResponseEntity<EmployeeWithPositionDTO> getEmployeeInfoForUser(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok().body(organizationService.getEmployeeInfo(id, user.getEmployee().getId()));
    }

}
