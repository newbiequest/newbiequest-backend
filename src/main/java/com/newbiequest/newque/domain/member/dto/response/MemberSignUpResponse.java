package com.newbiequest.newque.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSignUpResponse {
    private Long id;

    private String nickname;

    private String password;

    private Long accessToken;
}
