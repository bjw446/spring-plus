package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);


    // Todo에 weather 및 기간의 시작과 끝 조건이 Null 또는 값을 가지고 있는지 검색
    // modifiedAt 기준 기간의 시작과 끝 조건 설정
    // 기존 OrderBy 동일하게 추가
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user " +
            "WHERE (:weather IS NULL OR t.weather = :weather) " +
            "AND (:startDate IS NULL OR t.modifiedAt >= :startDate) " +
            "AND (:endDate IS NULL OR t.modifiedAt <= :endDate) " +
            "ORDER BY t.modifiedAt DESC ")
    Page<Todo> searchTodos(@Param("weather") String weather,
                           @Param("startDate") LocalDateTime startDate,
                           @Param("endDate") LocalDateTime endDate,
                           Pageable pageable);
}
