package com.ze.studyapp.account.service;

import com.ze.studyapp.account.domain.entity.Account;
import com.ze.studyapp.account.endpoint.controller.SignUpForm;
import com.ze.studyapp.account.infra.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateToken();
        sendVerificationEmail(newAccount);

    }

    private Account saveNewAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .nickname(signUpForm.getNickname())
                .notificationSetting(Account.NotificationSetting.builder()
                        .studyCreatedByWeb(true)
                        .studyUpdatedByWeb(true)
                        .studyRegistrationResultByEmailByWeb(true)
                        .build())
                .build();

        Account newAccount = accountRepository.save(account);
        return newAccount;
    }


    private void sendVerificationEmail(Account newAccount) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("StudyApp 회원 가입 인증");
        mailMessage.setText(String.format("/check-email-token?token=%s&email=%s", newAccount.getEmailToken(), newAccount.getEmail()));
        javaMailSender.send(mailMessage);
    }


    public Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
}
