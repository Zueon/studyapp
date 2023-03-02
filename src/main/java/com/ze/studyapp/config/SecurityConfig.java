package com.ze.studyapp.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/", "/login", "/sign-up", "/check-email-token",
                        "/email-login", "/check-email-login", "/login-link").permitAll()
                .mvcMatchers(HttpMethod.GET, "/profile/*").permitAll()
                .anyRequest().authenticated();

        // form 기반 인증 지원
        http.formLogin()
                .loginPage("/login")    // loginPage를 따로 설정해주지 않을 경우 스프링이 기본으로 로그인페이지를 생성해준다.
                                        // 로그인 시 login 페이지로 이동하게 했으니 컨트롤러에도 추가해주도록 한다.
                .permitAll();           // 로그인 페이지에는 인증하지 않아도 접근할 수 있도록 설정


        // 로그아웃 시 설정 지원
        http.logout()
                .logoutSuccessUrl("/"); // 로그아웃 성공 시 이동할 경로
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .mvcMatchers("/node_modules/**", "/images/**")
                .antMatchers("/h2-console/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();  // 기본 인코더를 빈으로 등록한다. 등록 후 인코딩 적용을 위해 Accountservice 수정

    }

}
