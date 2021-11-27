package src.argon.argon.security.models;

public class RegistrationRequest {
    private String username;
    private String password;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
