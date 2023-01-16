package src.argon.argon.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import src.argon.argon.dto.EmployeeDTO;
import src.argon.argon.dto.OrganizationDTO;
import src.argon.argon.dto.WorktimeDTO;
import src.argon.argon.mapper.EmployeeMapper;
import src.argon.argon.security.models.JsonResponse;
import src.argon.argon.security.models.User;
import src.argon.argon.service.OrganizationService;
import src.argon.argon.service.WorktimeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("worktimes")
public class WorktimeController {

    final static private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    final static private Gson gson = new Gson();

    @Autowired
    WorktimeService worktimeService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    EmployeeMapper employeeMapper;

    @GetMapping("/day")
    public List<WorktimeDTO> getWorktimesAtDay(@RequestParam String day, @RequestParam Long organizationId, Authentication authentication) {
        LocalDate dateDay = LocalDate.parse(day, dtf);
        User user = (User) authentication.getPrincipal();
        return worktimeService.getWorktimesAtDayForUser(dateDay, user.getEmployee().getId(), organizationId);
    }

    @GetMapping("/range")
    public List<WorktimeDTO> getWorktimesAtDateRange(@RequestParam String rangeStart, @RequestParam String rangeEnd, @RequestParam Long organizationId, Authentication authentication) {
        LocalDate dateStart = LocalDate.parse(rangeStart, dtf);
        LocalDate dateEnd = LocalDate.parse(rangeEnd, dtf);
        User user = (User) authentication.getPrincipal();
        return worktimeService.getWorktimesAtDateRangeForUser(dateStart, dateEnd, user.getEmployee().getId(), organizationId);
    }

    @GetMapping("/filter/{organizationId}")
    public List<WorktimeDTO> getFilteredWorktimesForOrganization(@RequestParam String rangeStart, @RequestParam String rangeEnd,
                                                                 @RequestParam String employeeIds, @RequestParam String subprojectIds,
                                                                 @PathVariable Long organizationId, Authentication authentication) {
        OrganizationDTO organization = organizationService.getOrganizationById(organizationId);
        User user = (User) authentication.getPrincipal();
        EmployeeDTO employeeDTO = employeeMapper.toDTO(user.getEmployee());
        if (!organization.getOwners().contains(employeeDTO)) {
            // TODO check organization ownership
        }
        LocalDate dateFrom = LocalDate.parse(rangeStart, dtf);
        LocalDate dateTo = LocalDate.parse(rangeEnd, dtf);
        List<Long> employeeIdsList = List.of(gson.fromJson(employeeIds, Long[].class));
        List<Long> subprojectIdsList = List.of(gson.fromJson(subprojectIds, Long[].class));

        return worktimeService.getWorktimesAtDateRangeForEmployeesInSubprojects(dateFrom, dateTo, employeeIdsList, subprojectIdsList);
    }

    @PostMapping("")
    public ResponseEntity<?> createWorktime(@RequestBody WorktimeDTO worktimeDTO, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        worktimeDTO.setEmployeeId(user.getEmployee().getId());
        try {
            WorktimeDTO result = worktimeService.save(worktimeDTO);
            return ResponseEntity.status(201).body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new JsonResponse("PROJECT_DELETED", e.getMessage()));
        }
    }

    @PutMapping("")
    public ResponseEntity<WorktimeDTO> updateWorktime(@RequestBody WorktimeDTO worktimeDTO) {
        WorktimeDTO result = worktimeService.save(worktimeDTO);
        return ResponseEntity.status(201).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorktime(@PathVariable Long id) {
        worktimeService.deleteWorktime(id);
        return ResponseEntity.ok().build();
    }

}
