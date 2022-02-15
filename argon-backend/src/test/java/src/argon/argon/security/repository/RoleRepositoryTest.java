package src.argon.argon.security.repository;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import src.argon.argon.security.models.Role;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    RoleRepository underTest;

    @Test
    @FlywayTest
    @Disabled()
    // TODO - get Flyway migrations to work
    void getUserRole_shouldReturnUserRole() {
        // when
        Role userRole = underTest.getUserRole();
        // then
        assertNotNull(userRole);
    }
}