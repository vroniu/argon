package src.argon.argon.testutils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.core.Authentication;
import src.argon.argon.entity.Employee;
import src.argon.argon.security.models.User;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User principal = new User();
        if (customUser.employeeId().length() != 0) {
            Employee employee = new Employee();
            employee.setId(Long.valueOf(customUser.employeeId()));
            principal.setEmployee(employee);
        }
        principal.setUsername(customUser.username());
        principal.setId(Long.valueOf(customUser.id()));
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}