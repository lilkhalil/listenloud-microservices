package com.lilkhalil.user;

import com.lilkhalil.user.dao.UserRepository;
import com.lilkhalil.user.domain.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Test
    void whenUserWithGivenUsernameFoundThenUserPropertiesEquals() {
        this.em.persist(
                User.builder()
                        .username("example")
                        .password("1234")
                        .biography("Hello, World!")
                        .imageId(null)
                        .build()
        );
        this.userRepository.findByUsername("example").ifPresentOrElse(
                user -> {
                    assertThat(user.getId()).isEqualTo(1L);
                    assertThat(user.getUsername()).isEqualTo("example");
                    assertThat(user.getPassword()).isEqualTo("1234");
                    assertThat(user.getBiography()).isEqualTo("Hello, World!");
                    assertThat(user.getImageId()).isNull();
                },
                Assertions::fail
        );
    }

}
