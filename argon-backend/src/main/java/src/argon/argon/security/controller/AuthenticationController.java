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
import src.argon.argon.security.models.*;
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

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new JsonResponse("BAD_CREDENTIALS", "Incorrect username or password"));
        }
        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtilService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<JsonResponse> createAccount(@RequestBody RegistrationRequest registrationRequest) {
        if (StringUtils.isNullOrEmpty(registrationRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new JsonResponse("NO_USERNAME", "Username not provided"));
        }
        if (userService.userExists(registrationRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new JsonResponse("USERNAME_TAKEN","User with that username already exists"));
        }

        if (StringUtils.isNullOrEmpty(registrationRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new JsonResponse("NO_EMAIL","Email not provided"));
        }
        if (!StringUtils.validEmail(registrationRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new JsonResponse("INVALID_EMAIL", "Invalid email"));
        }
        if (userService.emailTaken(registrationRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new JsonResponse("EMAIL_TAKEN","Email already in use"));
        }

        if (StringUtils.isNullOrEmpty(registrationRequest.getPassword())) {
            return ResponseEntity.badRequest().body(new JsonResponse("NO_PASSWORD","Password not provided"));
        }
        if (!StringUtils.validPassword(registrationRequest.getPassword())) {
            return ResponseEntity.badRequest().body(new JsonResponse("INVALID_PASSWORD","Invalid password"));
        }

        userService.registerUser(registrationRequest);
        return ResponseEntity.status(201).body(new JsonResponse("SUCCESS", "User created successfully"));
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUserInfo(Authentication authentication) {
       User user = (User) authentication.getPrincipal();
       return ResponseEntity.ok(userService.getUserById(user.getId()));
    }

}
