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

    @GetMapping("")
    public List<ProjectDTO> getAllProjects() {
        return projectService.findAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO project = projectService.findOne(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO project) {
        if (project.getId() != null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(projectService.save(project));
        }
    }

    @PutMapping("")
    public ResponseEntity<ProjectDTO> updateProject(@RequestBody ProjectDTO project) {
        return ResponseEntity.ok(projectService.save(project));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.ok().build();
    }
}
