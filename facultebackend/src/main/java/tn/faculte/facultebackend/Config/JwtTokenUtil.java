package tn.faculte.facultebackend.Config;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tn.faculte.facultebackend.Entity.EnrollmentYear;
import tn.faculte.facultebackend.Entity.Role;
import tn.faculte.facultebackend.Entity.User;
import tn.faculte.facultebackend.Repo.UserRepo;

import java.util.Date;

@Component
public class JwtTokenUtil {
@Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Long expiration;

    @Autowired
    private UserRepo userRepo;

    public String generateJwtToken(Long userId, String email, String rolesAsString, Long specialityId, EnrollmentYear enrollmentYear) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("roles", rolesAsString)
                .claim("specialityId", specialityId)
                .claim("enrollmentYear", enrollmentYear.toString())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getEmailFromToken(String token) {
        try {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
        } catch (Exception e) {
            System.err.println("Error extracting email from token: " + e.getMessage());
            return null;
        }
    }

    public Long getSpecialityIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("specialityId", Long.class);
        } catch (Exception e) {
            System.err.println("Error extracting speciality ID from token: " + e.getMessage());
            return null;
        }
    }


    public EnrollmentYear getEnrollmentYearFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = claims.get("userId", Long.class);
            if (userId != null) {
                // Retrieve the user from the database using the user ID
                User user = userRepo.findById(userId).orElse(null);
                if (user != null && user.getEnrollment() != null) {
                    // Retrieve the enrollment year from the user's enrollment
                    return user.getEnrollment().getEnrollmentYear();
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error extracting enrollment year from token: " + e.getMessage());
            return null;
        }
    }


    public Role getRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            String rolesAsString = claims.get("roles", String.class);
            if (!StringUtils.isEmpty(rolesAsString)) {
                String[] rolesArray = rolesAsString.split(", ");
                // Assuming the first role is the most significant one
                return Role.valueOf(rolesArray[0]);
            }
        } catch (Exception e) {
            System.err.println("Error extracting role from token: " + e.getMessage());
        }
        return null;
    }



    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            // Check token expiration
            Date expirationDate = claims.getBody().getExpiration();
            if (expirationDate != null && expirationDate.before(new Date())) {
                return false;
            }
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (MalformedJwtException | SignatureException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}
