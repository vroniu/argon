package src.argon.argon.testutils;

import src.argon.argon.security.models.User;
import src.argon.argon.security.service.JWTUtilService;
import src.argon.argon.security.service.UserService;

import static org.mockito.Mockito.when;

public class AuthBypassUtils {

    public static void bypassAuthForUser(String username, String userToken, JWTUtilService jwtUtilService, UserService userService) {
        User user = new User();
        user.setUsername(username);
        when(jwtUtilService.extractUsername(userToken)).thenReturn(username);
        when(userService.loadUserByUsername(username)).thenReturn(user);
        when(jwtUtilService.validateToken(user, userToken)).thenReturn(true);
    }

}
