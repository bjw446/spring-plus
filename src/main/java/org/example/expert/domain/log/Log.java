package org.example.expert.domain.log;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "log")
public class Log extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private Long todoId;
    private String status;

    public Log(String nickname, Long todoId, String status) {
        this.nickname = nickname;
        this.todoId = todoId;
        this.status = status;
    }
}
