package org.example.expert.domain.todo.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TodoSearchRequest {
    private String title;
    private String nickname;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
