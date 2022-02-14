package src.argon.argon.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import src.argon.argon.dto.UserDTO;
import src.argon.argon.mapper.UserMapper;
import src.argon.argon.security.models.AuthenticationRequest;
import src.argon.argon.security.models.AuthenticationResponse;
import src.argon.argon.security.models.RegistrationRequest;
import src.argon.argon.security.models.User;
import src.argon.argon.security.service.JWTUtilService;
import src.argon.argon.security.service.UserService;
import src.argon.argon.utils.StringUtils;

@RestController
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JWTUtilService jwtUtilService;

    @Autowired
    UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtilService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<String> createAccount(@RequestBody RegistrationRequest registrationRequest) {
        if (StringUtils.isNullOrEmpty(registrationRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username not provided");
        }
        if (userService.userExists(registrationRequest.getUsername())) {
            return ResponseEntity.badRequest().body("User with that username already exists");
        }

        if (StringUtils.isNullOrEmpty(registrationRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email not provided");
        }
        if (!StringUtils.validEmail(registrationRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email");

        }
        if (userService.emailTaken(registrationRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        if (StringUtils.isNullOrEmpty(registrationRequest.getPassword())) {
            return ResponseEntity.badRequest().body("Password not provided");
        }
        if (!StringUtils.validPassword(registrationRequest.getPassword())) {
            return ResponseEntity.badRequest().body("Password must be at least 8 characters long and contain at least one digit");
        }

        userService.registerUser(registrationRequest);
        return ResponseEntity.status(201).body("User created successfully");
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUserInfo(Authentication authentication) {
       User user = (User)authentication.getPrincipal();
       return ResponseEntity.ok(userMapper.toDTO(user));
    }

}
