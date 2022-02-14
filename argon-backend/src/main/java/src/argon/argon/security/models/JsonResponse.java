package src.argon.argon.security.models;

public class JsonResponse {
    private String status;
    private String description;

    public JsonResponse(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
