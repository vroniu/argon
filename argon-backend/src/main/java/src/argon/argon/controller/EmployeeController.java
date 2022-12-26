package src.argon.argon.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import src.argon.argon.dto.EmployeeDTO;
import src.argon.argon.service.EmployeeService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("employees")
public class EmployeeController {

    final static private Gson gson = new Gson();

    @Autowired
    EmployeeService employeeService;

    @GetMapping("")
    public ResponseEntity<List<EmployeeDTO>> getEmployees(
         @RequestParam(required = false) String name,
         @RequestParam(required = false) String email,
         @RequestParam(required = false) String excludeOrganizations
    ) {
        List<Long> excludeOrganizationsIds = excludeOrganizations == null ? Collections.emptyList()
                : List.of(gson.fromJson(excludeOrganizations, Long[].class));
        return ResponseEntity.ok().body(employeeService.getEmployees(name, email, excludeOrganizationsIds));
    }

}
