package com.ktb.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MemberResponse {

    @Getter
    @AllArgsConstructor
    public static class SignUpResponse {

        private Long id;
    }

    @Getter
    @AllArgsConstructor
    public static class LoginResponse {

        private Long id;

        private String nickname;

        private String email;
    }

    @Getter
    @AllArgsConstructor
    public static class ProfileResponse {

        private Long id;

        private String nickname;

        private String email;
    }
}
