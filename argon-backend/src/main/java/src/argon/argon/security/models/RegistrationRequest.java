package src.argon.argon.security.models;

import src.argon.argon.dto.EmployeeDTO;

public class RegistrationRequest {
    private String username;
    private String password;
    private String email;
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

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }
}
