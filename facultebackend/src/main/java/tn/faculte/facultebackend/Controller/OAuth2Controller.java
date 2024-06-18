package tn.faculte.facultebackend.Controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

   /* @GetMapping("/authorize")
    public void authorize(HttpServletResponse response) throws IOException {
        String clientId = "834394576559-of3m7r10o45a2d9s0ijucf99tm69qsk3.apps.googleusercontent.com";
        String redirectUri = "http://localhost:8080/google/callback";
        String[] scopes = {
                "https://www.googleapis.com/auth/calendar", // Required for basic calendar access
                "https://www.googleapis.com/auth/calendar.events", // Required for managing events
                // Add more scopes as needed for additional functionality
        };
        String responseType = "code";

        String scope = String.join(" ", scopes);

        String authorizationUrl = String.format("https://accounts.google.com/o/oauth2/auth?client_id=%s&redirect_uri=%s&scope=%s&response_type=%s",
                clientId, redirectUri, scope, responseType);

        response.sendRedirect(authorizationUrl);
    }


    @PostMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("code") String code) {
        // Exchange authorization code for access token
        String accessToken = exchangeCodeForToken(code);
        return ResponseEntity.ok(accessToken);
    }

    private String exchangeCodeForToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        String tokenUrl = "https://oauth2.googleapis.com/token";
        String clientId = "834394576559-of3m7r10o45a2d9s0ijucf99tm69qsk3.apps.googleusercontent.com";
        String clientSecret = "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";
        String redirectUri = "http://localhost:8080/google/callback";
        String grantType = "authorization_code";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("grant_type", grantType);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(tokenUrl, requestEntity, Map.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = responseEntity.getBody();
            return (String) responseBody.get("access_token");
        } else {
            // Handle error
            return null;
        }
    }*/
}

