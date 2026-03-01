package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class TodoSearchRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @TestConfiguration
    @EnableJpaAuditing
    static class Config {
    }

    @Test
    void weather및_기간_조건_검색_테스트() {

        // given
        User user = new User("test@test.com", "1234", UserRole.USER, "닉네임");
        testEntityManager.persist(user);

        Todo todo1 = new Todo("제목1", "내용1", "맑음", user);
        Todo todo2 = new Todo("제목2", "내용2", "소나기", user);

        testEntityManager.persist(todo1);
        testEntityManager.persist(todo2);
        testEntityManager.flush();
        testEntityManager.clear();

        Pageable pageable = PageRequest.of(0, 10);

        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        // when
        Page<Todo> result =
                todoRepository.searchTodos(
                        "맑음",
                        start,
                        end,
                        pageable
                );

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getWeather())
                .isEqualTo("맑음");
    }
}
