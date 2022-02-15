package src.argon.argon.security.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import src.argon.argon.security.models.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository underTest;

    @Test
    void findByUsername_userExists_findsUser() {
        // given
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("mail@mail.com");
        underTest.save(user);
        // when
        User foundUser = underTest.findByUsername("username");
        // then
        assertNotNull(foundUser);
        assertEquals(foundUser.getUsername(), "username");
    }

    @Test
    void findByUsername_userDoesntExists_returnsNull() {
        // when
        User foundUser = underTest.findByUsername("username");
        // then
        assertNull(foundUser);
    }

    @Test
    void findByEmail_userExists_findsUser() {
        // given
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("mail@mail.com");
        underTest.save(user);
        // when
        User foundUser = underTest.findByEmail("mail@mail.com");
        // then
        assertNotNull(foundUser);
        assertEquals(foundUser.getEmail(), "mail@mail.com");
    }

    @Test
    void findByEmail_userDoesntExist_returnsNull() {
        // when
        User foundUser = underTest.findByEmail("mail@mail.com");
        // then
        assertNull(foundUser);
    }
}