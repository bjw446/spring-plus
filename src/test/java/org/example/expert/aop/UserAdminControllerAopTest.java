package org.example.expert.aop;

import org.example.expert.domain.user.service.UserAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserAdminControllerAopTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAdminService userAdminService;

    @SpyBean
    private AdminAccessLoggingAspect adminAccessLoggingAspect;

    @Test
    void changeUserRole_호출_시_AOP_실행_test() throws Exception {

        // given
        String requestBody = """
                {
                    "role": "ADMIN"
                }
                """;

        // when
        mockMvc.perform(
                        patch("/admin/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk());

        // then
        verify(adminAccessLoggingAspect)
                .logAfterChangeUserRole(any());
    }
}
