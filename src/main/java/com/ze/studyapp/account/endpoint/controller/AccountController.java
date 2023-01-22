package com.ze.studyapp.account.endpoint.controller;

import com.ze.studyapp.account.domain.entity.Account;
import com.ze.studyapp.account.endpoint.controller.validator.SignUpFormValidator;
import com.ze.studyapp.account.infra.AccountRepository;
import com.ze.studyapp.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;


    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);

    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid @ModelAttribute SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) return "account/sign-up";

        accountService.signUp(signUpForm);
        return "redirect:/";
    }



//    @PostMapping("/sign-up")
//    // @Valid : 타입에 대한 검증 실시
//    // @ModelAttributes : 전달 받은 데이터를 해당 타입에 매핑해준다.
//    public String signUpSubmit(@Valid @ModelAttribute SignUpForm signUpForm, Errors errors) {
//        // 에러가 존재할 경우 다시 회원가입 페이지를 띄운다. Errors 객체로 에러가 전달되기 때문에
//        // Thymeleaf로 렌더링된 HTML에 해당 에러를 전달해 업데이트한다.
//        if (errors.hasErrors()) {
//            return "accont/sign-up";
//        }
//        signUpFormValidator.validate(signUpForm, errors);
//        return "redirect:/";
//    }
}
