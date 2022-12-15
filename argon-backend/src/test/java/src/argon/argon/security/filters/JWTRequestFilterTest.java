package src.argon.argon.security.filters;

import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import src.argon.argon.security.models.JsonResponse;
import src.argon.argon.security.models.User;
import src.argon.argon.security.service.JWTUtilService;
import src.argon.argon.security.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class JWTRequestFilterTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JWTUtilService jwtUtilService;

    @MockBean
    UserService userService;

    @Test
    void doFilterInternal_ShouldPassValidTokens() throws Exception {
        String validToken = "validtoken";
        User user = new User();
        user.setUsername("username");
        when(userService.loadUserByUsername("username")).thenReturn(user);
        when(jwtUtilService.extractUsername(validToken)).thenReturn("username");
        when(jwtUtilService.validateToken(user, validToken)).thenReturn(true);

        MvcResult result = mockMvc.perform(get("/test").header("Authorization", "Bearer " + validToken)).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("Hello world!", result.getResponse().getContentAsString());
    }

    @Test
    void doFilterInternal_ShouldReturnErrorResponse_IfTokenInvalid() throws Exception {
        String invalidToken = "invalidtoken";
        User user = new User();
        user.setUsername("username");
        when(userService.loadUserByUsername("username")).thenReturn(user);
        when(jwtUtilService.extractUsername(invalidToken)).thenReturn("username");
        when(jwtUtilService.validateToken(user, invalidToken)).thenThrow(new ExpiredJwtException(Jwts.header(), Jwts.claims(), "JWT expired"));

        MvcResult result = mockMvc.perform(get("/test").header("Authorization", "Bearer " + invalidToken)).andReturn();
        JsonResponse resultResponse = new Gson().fromJson(result.getResponse().getContentAsString(), JsonResponse.class);

        assertEquals(401, result.getResponse().getStatus());
        assertEquals("JWT_EXPIRED", resultResponse.getStatus());
        assertEquals("JWT token is expired", resultResponse.getDescription());
    }
}