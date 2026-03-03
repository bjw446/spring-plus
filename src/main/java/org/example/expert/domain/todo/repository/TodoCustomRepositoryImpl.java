package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        Todo result = jpaQueryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user)
                .fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<TodoSearchResponse> todoSearch(TodoSearchRequest request, Pageable pageable) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;
        QComment comment = QComment.comment;
        QManager manager = QManager.manager;

        List<TodoSearchResponse> content = jpaQueryFactory
                .select(Projections.constructor(TodoSearchResponse.class,
                        todo.title,
                        manager.user.countDistinct(),
                        comment.id.countDistinct()))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .leftJoin(todo.comments, comment)
                .where(
                        titleContains(request.getTitle()),
                        nicknameContains(request.getNickname()),
                        createdBetween(request.getStartDate(), request.getEndDate())
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(todo.count())
                .from(todo)
                .where(
                        titleContains(request.getTitle()),
                        nicknameContains(request.getNickname()),
                        createdBetween(request.getStartDate(), request.getEndDate())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression titleContains(String keyword) {
        return hasText(keyword) ? QTodo.todo.title.contains(keyword) : null;
    }

    private BooleanExpression nicknameContains(String nickname) {
        return hasText(nickname) ? QUser.user.nickname.contains(nickname) : null;
    }

    private BooleanExpression createdBetween(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            return QTodo.todo.createdAt.between(start, end);
        }
        return null;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
