package tn.faculte.facultebackend.Config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtAuthenticationToken  extends UsernamePasswordAuthenticationToken {

    private String token;

    public JwtAuthenticationToken(String principal, String token) {
        super(principal, token,null);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
