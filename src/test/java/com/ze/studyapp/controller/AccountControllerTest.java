package com.ze.studyapp.controller;

import com.ze.studyapp.account.infra.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
달
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @MockBean
    JavaMailSender mailSender;


    @Test
    @DisplayName("회원 가입 화면 진입 확인")
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }
/**
 * *
 * (1) 포맷에 맞지 않는 이메일
 * (2) 8자리가 되지 않는 비밀번호
 * (3) 상태는 처리 여부와 관계 없이 200 ok 반환 -> AccountController에 페이지를 이동시키도록 구현되어 있기 때문에
 * (4) 입력값이 잘못되었기 때문에 /sign-up 페이지로 되돌아가 에러 노출 *
 * * * */
    @Test
    @DisplayName("회원 가입 처리 : 입력값 오류")
    void signUpSubmitWithError() throws Exception {
        mockMvc.perform(post("/sign-up")
                    .param("nickname", "nickname")
                    .param("email", "email@gmail")  // (1)
                    .param("password", "1234!")     // (2)
                    .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())                       // (3)
                .andExpect(view().name("account/sign-up"));     //(4)
    }


    /**
     * 회원가입 후 이메일 검증을 위해 AccountRepository 주입 *
     * (2) 모든 필드의 값 정상적으로 입력 후 crsf 설정 -> security, thymeleaf 같이 사용하면 thymeleaf에서 csrf 토큰을 임의로 생성하여 넣어주기 때문에
     * crsf() 없이 수행할 걍우 403 Error 발생 *
     * (3) 이메일이 정상적으로 저장되었는지 확인 *
     * (4) 메일 전송 확인. 실제 전송 여부 확인이 어려움 -> JavaMailSender를 @MockBean 을 이용하여 주입하고, mailSender가 send 메서드 호출하고 그 때 전달된 타입이 SimpleMailMessage인지 확인*
     * * */
    @Test
    @DisplayName("회원 가입 처리: 입력값 정상")
    void signUpSubmit() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "nickname")
                        .param("email", "email@gmail.com")
                        .param("password", "1234!@#$")
                        .with(csrf()))          // (2)
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        assertTrue(accountRepository.existsByEmail("email@gmail.com")); // (3)

        then(mailSender)
                .should()
                .send(any(SimpleMailMessage.class));                    // (4)
    }
}