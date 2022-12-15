package src.argon.argon.security.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import src.argon.argon.dto.UserDTO;
import src.argon.argon.security.models.*;
import src.argon.argon.security.service.JWTUtilService;
import src.argon.argon.security.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    UserService userService;

    @MockBean
    JWTUtilService jwtUtilService;

    @Autowired
    MockMvc mockMvc;

    Gson gson = new Gson();

    @Test
    void createAuthenticationToken_ShouldReturnError_IfIncorrectCredentials() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("username", "password");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        MvcResult result = mockMvc.perform(post("/login").content(gson.toJson(authenticationRequest)).contentType(MediaType.APPLICATION_JSON)).andReturn();
        JsonResponse resultResponse = gson.fromJson(result.getResponse().getContentAsString(), JsonResponse.class);

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("BAD_CREDENTIALS", resultResponse.getStatus());
        assertEquals("Incorrect username or password", resultResponse.getDescription());
    }

    @Test
    void createAuthenticationToken_ShouldReturnToken() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("username", "password");
        User user = new User();
        user.setUsername("username");
        when(userService.loadUserByUsername("username")).thenReturn(user);
        when(jwtUtilService.generateToken(user)).thenReturn("jwt_token");

        MvcResult result = mockMvc.perform(post("/login").content(gson.toJson(authenticationRequest)).contentType(MediaType.APPLICATION_JSON)).andReturn();
        AuthenticationResponse resultResponse = gson.fromJson(result.getResponse().getContentAsString(), AuthenticationResponse.class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("jwt_token", resultResponse.getJwt());
    }

    @Test
    void createAccount_ShouldCreateAccount() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("username");
        request.setEmail("mail@mail.com");
        request.setPassword("password123");
        when(userService.userExists("username")).thenReturn(false);
        when(userService.emailTaken("mail@mail.com")).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/register").content(gson.toJson(request)).contentType(MediaType.APPLICATION_JSON)).andReturn();
        JsonResponse resultResponse = gson.fromJson(result.getResponse().getContentAsString(), JsonResponse.class);

        assertEquals(201, result.getResponse().getStatus());
        assertEquals("SUCCESS", resultResponse.getStatus());
        assertEquals("User created successfully", resultResponse.getDescription());
        verify(userService, times(1)).registerUser(argThat(req -> req.getUsername().equals("username")));
    }

    @Test
    void createAccount_ShouldReturnError_IfNoUsername() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("");
        request.setEmail("mail@mail.com");
        request.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/register").content(gson.toJson(request)).contentType(MediaType.APPLICATION_JSON)).andReturn();
        JsonResponse resultResponse = gson.fromJson(result.getResponse().getContentAsString(), JsonResponse.class);

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("NO_USERNAME", resultResponse.getStatus());
        assertEquals("Username not provided", resultResponse.getDescription());
    }

    @Test
    void createAccount_ShouldReturnError_IfUsernameTaken() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("taken");
        request.setEmail("mail@mail.com");
        request.setPassword("password123");
        when(userService.userExists("taken")).thenReturn(true);

        MvcResult result = mockMvc.perform(post("/register").content(gson.toJson(request)).contentType(MediaType.APPLICATION_JSON)).andReturn();
        JsonResponse resultResponse = gson.fromJson(result.getResponse().getContentAsString(), JsonResponse.class);

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("USERNAME_TAKEN", resultResponse.getStatus());
        assertEquals("User with that username already exists", resultResponse.getDescription());
    }

    @Test
    void createAccount_ShouldReturnError_IfNoEmail() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("username");
        request.setEmail("");
        request.setPassword("password123");
        when(userService.userExists("username")).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/register").content(gson.toJson(request)).contentType(MediaType.APPLICATION_JSON)).andReturn();
        JsonResponse resultResponse = gson.fromJson(result.getResponse().getContentAsString(), JsonResponse.class);

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("NO_EMAIL", resultResponse.getStatus());
        assertEquals("Email not provided", resultResponse.getDescription());
    }

    @Test
    void createAccount_ShouldReturnError_IfInvalidEmail() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("username");
        request.setEmail("notavalidemail");
        request.setPassword("password123");
        when(userService.userExists("username")).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/register").content(gson.toJson(request)).contentType(MediaType.APPLICATION_JSON)).andReturn();
        JsonResponse resultResponse = gson.fromJson(result.getResponse().getContentAsString(), JsonResponse.class);

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("INVALID_EMAIL", resultResponse.getStatus());
        assertEquals("Invalid email", resultResponse.getDescription());
    }

    @Test
    void createAccount_ShouldReturnError_IfTakenEmail() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("username");
        request.setEmail("taken@mail.com");
        request.setPassword("password123");
        when(userService.userExists("username")).thenReturn(false);
        when(userService.emailTaken("taken@mail.com")).thenReturn(true);

        MvcResult result = mockMvc.perform(post("/register").content(gson.toJson(request)).contentType(MediaType.APPLICATION_JSON)).andReturn();
        JsonResponse resultResponse = gson.fromJson(result.getResponse().getContentAsString(), JsonResponse.class);

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("EMAIL_TAKEN", resultResponse.getStatus());
        assertEquals("Email already in use", resultResponse.getDescription());
    }

    @Test
    void createAccount_ShouldReturnError_IfNoPassword() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("username");
        request.setEmail("taken@mail.com");
        request.setPassword("");
        when(userService.userExists("username")).thenReturn(false);
        when(userService.emailTaken("taken@mail.com")).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/register").content(gson.toJson(request)).contentType(MediaType.APPLICATION_JSON)).andReturn();
        JsonResponse resultResponse = gson.fromJson(result.getResponse().getContentAsString(), JsonResponse.class);

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("NO_PASSWORD", resultResponse.getStatus());
        assertEquals("Password not provided", resultResponse.getDescription());
    }

    @Test
    void createAccount_ShouldReturnError_IfInvalidPassword() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("username");
        request.setEmail("taken@mail.com");
        request.setPassword("invld");
        when(userService.userExists("username")).thenReturn(false);
        when(userService.emailTaken("taken@mail.com")).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/register").content(gson.toJson(request)).contentType(MediaType.APPLICATION_JSON)).andReturn();
        JsonResponse resultResponse = gson.fromJson(result.getResponse().getContentAsString(), JsonResponse.class);

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("INVALID_PASSWORD", resultResponse.getStatus());
        assertEquals("Invalid password", resultResponse.getDescription());
    }

    @Test
    void getUserInfo_ShouldReturnUserInfo() throws Exception {
        User requestUser = new User();
        requestUser.setId(123L);
        requestUser.setUsername("username");
        requestUser.setEmail("mail@mail.com");
        UserDTO responseUser = new UserDTO();
        responseUser.setId(123L);
        responseUser.setUsername("username");
        responseUser.setEmail("mail@mail.com");
        when(userService.getUserById(123L)).thenReturn(responseUser);

        MvcResult result = mockMvc.perform(get("/user").with(user(requestUser))).andReturn();
        UserDTO resultResponse = gson.fromJson(result.getResponse().getContentAsString(), UserDTO.class);

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("username", resultResponse.getUsername());
        assertEquals("mail@mail.com", resultResponse.getEmail());
    }

}