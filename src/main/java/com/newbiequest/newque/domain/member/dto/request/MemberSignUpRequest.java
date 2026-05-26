package com.newbiequest.newque.domain.member.dto.request;

import lombok.Getter;

@Getter
public class MemberSignUpRequest {
    private String nickname;

    private String password;

    private Boolean consentToTerms;
}
