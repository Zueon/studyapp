package com.ze.studyapp.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


/*
* @Entity : 직렬화를 위해 반드시 기본 생성자가 필요하다. 외부에서 new 불가능하도록 protected 레벨로 생성자 만들기*
* @Builder : 빌더 사용을 위해서 생성자가 필요하다 -> @AllArgsConstructor
*  */
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@ToString
public class Account extends AuditingEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "account_id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean isValid;

    private String emailToken;

    @Embedded               // 해당 클래스의 필드들이 DB에서는 개별 컬럼에 매핑된다.
    private Profile profile;

    @Embeddable             // @Embedded 와 매핑되는 애너테이션. 해당 클래스가 Entity가 아닌 다른 Entity에 귀속될 수 있음을 의미한다.
    @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder @Getter @ToString
    public static class Profile {
        private String bio;
        @Convert(converter = ListStringConverter.class)     // 리스트를 DB 컬럼 하나에 매핑하기 위해서 Converter를 사용한다.
        private List<String> url;
        private String job;
        private String location;
        private String company;
        @Lob @Basic(fetch = FetchType.EAGER)
        private String image;
    }

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder @Getter @ToString
    public static class NotificationSetting {
        private boolean studyCreatedByEmail;
        private boolean studyCreatedByWeb;
        private boolean studyRegistrationResultByEmailByEmail;
        private boolean studyRegistrationResultByEmailByWeb;
        private boolean studyUpdatedByEmail;
        private boolean studyUpdatedByWeb;


    }
}