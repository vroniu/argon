package src.argon.argon.security.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import src.argon.argon.security.models.User;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JWTUtilServiceTest {

    @Value("${argon.secret}")
    private String secret;

    @Value("${argon.jwt.ttl}")
    private Long tokenTTL;

    @Autowired
    JWTUtilService underTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void generateToken_ShouldReturnToken() {
        User user = new User();
        user.setUsername("testuser");

        String result = underTest.generateToken(user);

        String userFromJwt = Jwts.parser().setSigningKey(secret).parseClaimsJws(result).getBody().get("sub", String.class);
        long jwtIssuedAt = Jwts.parser().setSigningKey(secret).parseClaimsJws(result).getBody().get("iat", Date.class).getTime();
        long jwtExpiresAt = Jwts.parser().setSigningKey(secret).parseClaimsJws(result).getBody().get("exp", Date.class).getTime();
        long currentDate = new Date().getTime();
        assertEquals("testuser", userFromJwt);
        // give the expiry/issued times a 10ms margin
        assertEquals(currentDate, jwtIssuedAt, 10);
        assertEquals(currentDate + tokenTTL * 1000, jwtExpiresAt, 10);
    }

    @Test
    void validateToken_ShouldReturnTrue_IfTokenValid() {
        String validToken = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * tokenTTL))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        User user = new User();
        user.setUsername("testuser");

        Boolean result = underTest.validateToken(user, validToken);

        assertTrue(result);
    }

    @Test
    void validateToken_ShouldThrowException_IfTokenExpired() throws InterruptedException {
        String invalidToken = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        User user = new User();
        user.setUsername("testuser");
        TimeUnit.SECONDS.sleep(1);

        ExpiredJwtException exception = assertThrows(ExpiredJwtException.class, () -> {
            underTest.validateToken(user, invalidToken);
        });

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("JWT expired"));
    }

    @Test
    void validateToken_ShouldReturnFalse_IfTokenUserDoesntMatch() {
        String invalidToken = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject("anotheruser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * tokenTTL))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        User user = new User();
        user.setUsername("testuser");

        Boolean result = underTest.validateToken(user, invalidToken);

        assertFalse(result);
    }


    @Test
    void extractUsername_ShouldReturnUsername() {
        String token = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * tokenTTL))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        String result = underTest.extractUsername(token);

        assertEquals("testuser", result);
    }

    @Test
    void extractExpiration_ShouldReturnExpirationDate() {
        String token = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * tokenTTL))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        Date result = underTest.extractExpiration(token);

        long predictedExpirationDate = new Date().getTime() + 1000 * tokenTTL;
        assertEquals(predictedExpirationDate, result.getTime(), 10);
    }

}