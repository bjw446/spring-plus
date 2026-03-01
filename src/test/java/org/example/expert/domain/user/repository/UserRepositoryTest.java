package org.example.expert.domain.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void User_테이블_닉네임_컬럼_확인() {
        // when
        String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_NAME = 'USERS' AND COLUMN_NAME = 'NICKNAME'";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);

        // then
        assertTrue(!result.isEmpty(), "nickname 컬럼이 존재하지 않습니다.");

    }
}

