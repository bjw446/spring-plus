package org.example.expert.domain.manager;

import org.example.expert.domain.log.LogRepository;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.manager.service.ManagerService;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
public class ManagerLogTest {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private LogRepository logRepository;

    @Test
    void saveManager_등록_실패_log_테스트(){

        //given
        User user = userRepository.save(
                new User("test@test.com", "1234", UserRole.USER, "닉네임")
        );

        User manager = userRepository.save(
                new User("manager@manager.com", "1234", UserRole.USER, "매니저")
        );

        Todo todo = todoRepository.save(
                new Todo("제목", "내용", "맑음", user)
        );

        // 자기 자신 매니저 등록 실패 테스트
        ManagerSaveRequest request = new ManagerSaveRequest(user.getId());

        // when
        assertThatThrownBy(() -> managerService.saveManager(
                        user.getId(), todo.getId(), request)).isInstanceOf(Exception.class);

        // then
        assertThat(managerRepository.count()).isEqualTo(1);

        assertThat(logRepository.count()).isEqualTo(1);

        assertThat(logRepository.findAll().get(0).getStatus()).isEqualTo("FAIL");
    }

    @Test
    void saveManager_등록_성공_log_테스트() {

        //given
        User user2 = userRepository.save(
                new User("test2@test.com", "1234", UserRole.USER, "닉네임2")
        );

        User manager2 = userRepository.save(
                new User("manager2@manager.com", "1234", UserRole.USER, "매니저2")
        );

        Todo todo2 = todoRepository.save(
                new Todo("제목2", "내용2", "맑음", user2)
        );

        ManagerSaveRequest request = new ManagerSaveRequest(manager2.getId());

        // when
        managerService.saveManager(user2.getId(), todo2.getId(), request);

        // then
        assertThat(managerRepository.count()).isEqualTo(2);

        assertThat(logRepository.count()).isEqualTo(1);

        assertThat(logRepository.findAll().get(0).getStatus()).isEqualTo("SUCCESS");
    }
}
