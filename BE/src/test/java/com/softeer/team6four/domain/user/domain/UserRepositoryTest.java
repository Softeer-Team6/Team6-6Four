package com.softeer.team6four.domain.user.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("UserRepository 테스트")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    public void 이메일로_존재여부를_파악할수있음() {
        // given
        User testUser = User.builder()
            .email("test1@test.com")
            .nickname("user1")
            .password("1234")
            .build();
        userRepository.save(testUser);

        // when
        boolean exists = userRepository.existsByEmail("test1@test.com");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void 닉네임으로_존재여부를_파악할수있음() {
        // given
        User testUser = User.builder()
            .email("test1@test.com")
            .nickname("user1")
            .password("1234")
            .build();
        userRepository.save(testUser);

        // when
        boolean exists = userRepository.existsByNickname("user1");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void 이메일에_해당하는_User객체를_가져올수있음() {
        // given
        User testUser = User.builder()
            .email("test1@test.com")
            .nickname("user1")
            .password("1234")
            .build();
        userRepository.save(testUser);

        // when
        Optional<User> foundUser = userRepository.findUserByEmail("test1@test.com");

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test1@test.com");
    }
}
