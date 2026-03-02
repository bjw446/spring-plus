package org.example.expert.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SpringSecurityTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String jwtToken;

    @BeforeEach
    void 로그인_테스트() throws Exception {
        // given
        User user = new User("test@test.com", passwordEncoder.encode("1234"), UserRole.USER, "닉네임");
        userRepository.save(user);

        // when
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/auth/signin",
                new SigninRequest("test@test.com", "1234"),
                String.class
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode node = objectMapper.readTree(response.getBody());
        jwtToken = node.get("bearerToken").asText();
    }

    @Test
    void JWT_통과_후_SecurityContext_정상_동작_테스트() throws Exception {

        // given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        UserChangePasswordRequest request =
                new UserChangePasswordRequest("1234", "ABCD12345");

        HttpEntity<String> entity =
                new HttpEntity<>(objectMapper.writeValueAsString(request), headers);

        // when
        ResponseEntity<Void> response =
                restTemplate.exchange(
                        "/users",
                        HttpMethod.PUT,
                        entity,
                        Void.class
                );

        // then
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }
}
