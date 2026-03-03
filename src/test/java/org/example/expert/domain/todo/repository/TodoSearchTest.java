package org.example.expert.domain.todo.repository;

import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TodoSearchTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Test
    void 일정_검색_QueryDSL_정상_조회_테스트() {
        // given
        User user1 = userRepository.save(
                new User("test1@test.com", "1234", UserRole.USER, "닉네임1"));

        User user2 = userRepository.save(
                new User("test2@test.com", "1234", UserRole.USER, "닉네임2"));

        Todo todo1 = todoRepository.save(
                new Todo("제목1", "내용1", "맑음", user1));

        Todo todo2 = todoRepository.save(
                new Todo("제목2", "내용2", "맑음", user2));

        managerRepository.save(new Manager(user2, todo1));

        commentRepository.save(new Comment("댓글1", user1, todo1));
        commentRepository.save(new Comment("댓글2", user2, todo1));
        commentRepository.save(new Comment("댓글3", user2, todo2));

        TodoSearchRequest request = TodoSearchRequest.builder()
                .title("제목1")
                .nickname("닉네임")
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<TodoSearchResponse> result = todoRepository.todoSearch(request, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);

        TodoSearchResponse response = result.getContent().get(0);

        assertThat(response.getTitle()).isEqualTo("제목1");
        // 닉네임 부분 일치시에도 검색 테스트
        assertThat(response.getManagerCount()).isEqualTo(2);

        assertThat(response.getCommentCount()).isEqualTo(2);
    }
}
