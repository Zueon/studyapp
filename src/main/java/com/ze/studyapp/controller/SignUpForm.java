package com.ze.studyapp.controller;

import lombok.Data;

@Data
public class SignUpForm {
    private String nickname;
    private String email;
    private String password;
}