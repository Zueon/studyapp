package com.ze.studyapp.account.domain;

import com.ze.studyapp.account.domain.entity.Account;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.List;

public class UserAccount extends User {
    @Getter
    private final Account account;      // @CurrentUser 에서 account를 반환하도록 하였기 때문에 변수이름을 반드시 account로 설정해줄 것


    // User 객체를 생성하기 위해서는 username, password, authorities가 필요 -> 현재 사용하는 객체인 Account에서 각각 추출해준다.
    public UserAccount(Account account){
        super(account.getNickname(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.account = account;

    }

}
