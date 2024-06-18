package tn.faculte.facultebackend.LoginResponse;

public class LoginMesage {
    String message;
    Boolean status;
    private String role;
    private String token;



    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public LoginMesage(String message, Boolean status, String role, String token) {
        this.message = message;
        this.status = status;
        this.role = role;
        this.token = token;

    }
}
