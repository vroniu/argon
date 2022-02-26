package src.argon.argon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.argon.argon.dto.SubprojectDTO;
import src.argon.argon.service.SubprojectService;

@RestController
@RequestMapping("subprojects")
public class SubprojectController {

    @Autowired
    SubprojectService subprojectService;

    @PostMapping("")
    public ResponseEntity<SubprojectDTO> createSubproject(@RequestBody SubprojectDTO subprojectDTO) {
        return ResponseEntity.status(201).body(subprojectService.save(subprojectDTO));
    }

    @PutMapping("")
    public ResponseEntity<SubprojectDTO> updateSubproject(@RequestBody SubprojectDTO subprojectDTO) {
        return ResponseEntity.status(200).body(subprojectService.save(subprojectDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubproject(@PathVariable Long id) {
        subprojectService.delete(id);
        return ResponseEntity.ok().build();
    }
}
