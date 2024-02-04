package com.lilkhalil.user.dao;

import com.lilkhalil.user.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
    
    @Test
    @DisplayName("If user with this username exists then return this user")
    void findByUsername_UserExists_ExpectedUser() {
        User expected = User.builder()
                .username("example")
                .password("1234")
                .biography("Hello, World!")
                .build();

        userRepository.save(expected);

        User user = this.userRepository.findByUsername("example").orElseThrow(AssertionFailedError::new);

        assertNotNull(user.getId());
        assertEquals(expected.getUsername(), user.getUsername());
        assertEquals(expected.getPassword(), user.getPassword());
        assertEquals(expected.getBiography(), user.getBiography());
        assertNull(user.getImageId());
    }

}
