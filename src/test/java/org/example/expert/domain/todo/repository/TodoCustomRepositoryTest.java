package org.example.expert.domain.todo.repository;

import org.example.expert.config.QuerydslConfig;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class TodoCustomRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void findByIdWithUser_QueryDSL_정상_조회_테스트() {
        // given
        User user = new User("test@test.com", "password", UserRole.USER, "닉네임");
        em.persist(user);

        Todo todo = new Todo("제목", "내용", "맑음", user);
        em.persist(todo);

        em.flush();
        em.clear();

        // when
        Optional<Todo> result = todoRepository.findByIdWithUser(todo.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("제목");
        assertThat(result.get().getUser().getEmail()).isEqualTo("test@test.com");
    }
}
