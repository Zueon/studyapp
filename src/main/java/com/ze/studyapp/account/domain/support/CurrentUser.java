package com.ze.studyapp.account.domain.support;


import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)     // Runtime 시 유지되도록 한다.
@Target(ElementType.PARAMETER)          // 파라미터에 사용할 수 있어야 한다.
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")      // spEl을 사용하여 인증 정보가 존재하지 않을 시 null, 존재할 경우 account 라는 property 반환
public @interface CurrentUser {
}
