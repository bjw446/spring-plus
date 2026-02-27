package org.example.expert.domain.todo.service;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TodoServiceTest {
    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void todoSave_정상_저장_테스트() {

        User user = new User("test@test.com", "1234", UserRole.USER, "닉네임");
        userRepository.save(user);

        AuthUser authUser =
                new AuthUser(user.getId(), user.getEmail(), user.getUserRole(), user.getNickname());

        TodoSaveRequest request =
                new TodoSaveRequest("제목", "내용");

        TodoSaveResponse response =
                todoService.saveTodo(authUser, request);

        assertThat(response).isNotNull();
        assertThat(todoRepository.findById(response.getId()))
                .isPresent();
    }
}
