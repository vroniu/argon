package src.argon.argon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.argon.argon.dto.ProjectDTO;
import src.argon.argon.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("projects")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping("/organization/{organizationId}")
    public List<ProjectDTO> getProjectsForOrganization(@PathVariable Long organizationId) {
        return projectService.getProjectsForOrganization(organizationId);
    }

    @PostMapping("")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.status(201).body(projectService.save(projectDTO));
    }

    @PutMapping("")
    public ResponseEntity<ProjectDTO> updateProject(@RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.status(200).body(projectService.save(projectDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.ok().build();
    }
}
