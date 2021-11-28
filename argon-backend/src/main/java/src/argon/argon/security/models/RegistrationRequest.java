package src.argon.argon.security.models;

import src.argon.argon.dto.EmployeeDTO;

public class RegistrationRequest {
    private String username;
    private String password;
    private EmployeeDTO employee;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }
}
