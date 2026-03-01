package org.example.expert.domain.user.service;

import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void userSave_닉네임_정상_저장_테스트() {

        // given
        User user = new User("test@test.com", "1234", UserRole.USER, "닉네임");

        // when
        userRepository.save(user);
        User savedUser = userRepository.findById(user.getId()).orElseThrow();

        // then
        assertEquals("닉네임", savedUser.getNickname());
    }
}
