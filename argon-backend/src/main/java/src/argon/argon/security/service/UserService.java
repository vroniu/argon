package src.argon.argon.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.argon.argon.dto.EmployeeDTO;
import src.argon.argon.mapper.EmployeeMapper;
import src.argon.argon.security.models.RegistrationRequest;
import src.argon.argon.security.models.User;
import src.argon.argon.security.repository.RoleRepository;
import src.argon.argon.security.repository.UserRepository;
import src.argon.argon.service.EmployeeService;

import java.util.Collections;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("User " + username + " not found");
        }
    }

    public UserDetails registerUser(RegistrationRequest registrationRequest) {
        EmployeeDTO employee = registrationRequest.getEmployee();

        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setAuthorities(Collections.singletonList(roleRepository.getUserRole()));
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());

        if (employee != null) {
            employee = employeeService.save(employee);
            user.setEmployee(employeeMapper.toEntity(employee));
        }

        user = userRepository.save(user);
        return user;
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public boolean emailTaken(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
