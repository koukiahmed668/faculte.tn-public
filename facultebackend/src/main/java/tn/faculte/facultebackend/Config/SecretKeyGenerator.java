package tn.faculte.facultebackend.Config;
import io.jsonwebtoken.security.Keys;


import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;


public class SecretKeyGenerator {
    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());


        System.out.println("Generated Secret Key: " + base64Key);
    }
}
