package src.argon.argon.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.argon.argon.security.models.RegistrationRequest;
import src.argon.argon.security.models.User;
import src.argon.argon.security.repository.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("User " + username + " not found");
        }
    }

    private boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }
}
