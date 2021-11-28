package src.argon.argon.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import src.argon.argon.security.models.AuthenticationRequest;
import src.argon.argon.security.models.AuthenticationResponse;
import src.argon.argon.security.models.RegistrationRequest;
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
    public ResponseEntity<?> createAutehenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
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
        if (StringUtils.isNullOrEmpty(registrationRequest.getUsername()) || StringUtils.isNullOrEmpty(registrationRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Password and/or username weren't provided");
        }
        if (userService.userExists(registrationRequest.getUsername())) {
            return ResponseEntity.badRequest().body("User with that username already exists");
        }
        userService.registerUser(registrationRequest);
        return ResponseEntity.ok("User created successfully");
    }
}
