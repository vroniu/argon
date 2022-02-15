package src.argon.argon.security.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import src.argon.argon.dto.EmployeeDTO;
import src.argon.argon.dto.UserDTO;
import src.argon.argon.mapper.EmployeeMapper;
import src.argon.argon.mapper.UserMapper;
import src.argon.argon.security.models.RegistrationRequest;
import src.argon.argon.security.models.User;
import src.argon.argon.security.repository.RoleRepository;
import src.argon.argon.security.repository.UserRepository;
import src.argon.argon.service.EmployeeService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService underTest;

    @Mock
    UserRepository userRepository;
    @Mock
    EmployeeService employeeService;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    EmployeeMapper employeeMapper;
    @Mock
    UserMapper userMapper;

    @Test
    void loadUserByUsername_userExists_returnUser() {
        // given
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("mail@mail.com");
        when(userRepository.findByUsername("username")).thenReturn(user);

        // when
        UserDetails loadedUser = underTest.loadUserByUsername("username");

        //then
        assertEquals(loadedUser.getUsername(), user.getUsername());
    }

    @Test
    void loadUserByUsername_userDoesntExist_throwsException() {
        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () -> {
            // given
            when(userRepository.findByUsername("username")).thenReturn(null);

            // when
            underTest.loadUserByUsername("username");
        });

        // then
        assertEquals(thrown.getMessage(), "User username not found");
    }

    @Test
    void registerUser_userWithoutEmployee_shouldRegister() {
        // given
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("username");
        registrationRequest.setPassword("password");
        registrationRequest.setEmail("mail@mail.com");
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // when
        underTest.registerUser(registrationRequest);

        // then
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        assertEquals(savedUser.getUsername(), "username");
        assertEquals(savedUser.getEmail(), "mail@mail.com");
        verify(employeeService, never()).save(any());
    }

    @Test
    void registerUser_userWithEmployee_shouldRegister() {
        // given
        RegistrationRequest registrationRequest = new RegistrationRequest();
        EmployeeDTO registeredEmployee = new EmployeeDTO();
        registeredEmployee.setFirstName("John");
        registeredEmployee.setLastName("Doe");
        registrationRequest.setUsername("username");
        registrationRequest.setPassword("password");
        registrationRequest.setEmail("mail@mail.com");
        registrationRequest.setEmployee(registeredEmployee);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<EmployeeDTO> employeeArgumentCaptor = ArgumentCaptor.forClass(EmployeeDTO.class);

        // when
        underTest.registerUser(registrationRequest);

        // then
        verify(userRepository).save(userArgumentCaptor.capture());
        verify(employeeService).save(employeeArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        EmployeeDTO savedEmployee = employeeArgumentCaptor.getValue();
        assertEquals(savedUser.getUsername(), "username");
        assertEquals(savedUser.getEmail(), "mail@mail.com");
        assertEquals(savedEmployee.getFirstName(), "John");
        assertEquals(savedEmployee.getLastName(), "Doe");
    }

    @Test
    void registerUser_userRegistered_userHasUserRole() {
        // given
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("username");
        registrationRequest.setPassword("password");
        registrationRequest.setEmail("mail@mail.com");
        underTest.registerUser(registrationRequest);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // when
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        // verify
        assertEquals(savedUser.getAuthorities().size(), 1);
        assertTrue(savedUser.getAuthorities().contains(roleRepository.getUserRole()));
    }

    @Test
    void userExists_userExists_returnTrue() {
        // given
        when(userRepository.findByUsername("username")).thenReturn(new User());

        // when
        boolean userExists = underTest.userExists("username");

        // then
        assertTrue(userExists);
    }

    @Test
    void userExists_userDoesntExist_returnFalse() {
        // given
        when(userRepository.findByUsername("username")).thenReturn(null);

        // when
        boolean userExists = underTest.userExists("username");

        // then
        assertFalse(userExists);
    }

    @Test
    void emailTaken_emailTaken_returnTrue() {
        // given
        when(userRepository.findByEmail("mail@mail.com")).thenReturn(new User());

        // when
        boolean mailTaken = underTest.emailTaken("mail@mail.com");

        // then
        assertTrue(mailTaken);
    }

    @Test
    void emailTaken_emailNotTaken_returnFalse() {
        // given
        when(userRepository.findByEmail("mail@mail.com")).thenReturn(null);

        // when
        boolean mailTaken = underTest.emailTaken("mail@mail.com");

        // then
        assertFalse(mailTaken);
    }

    @Test
    void getUserById_userExists_returnDTO() {
        // given
        User user = new User();
        user.setId(1L);
        when(userRepository.getById(1L)).thenReturn(user);

        // when
        UserDTO userDTO = underTest.getUserById(1L);

        // then
        assertEquals(userDTO, userMapper.toDTO(user));
    }

    @Test
    void getUserById_userDoesntExist_returnNull() {
        // given
        when(userRepository.getById(1L)).thenReturn(null);

        // when
        UserDTO userDTO = underTest.getUserById(1L);

        // then
        assertNull(userDTO);
    }
}