package com.newbiequest.newque.domain.member.dto.request;

import lombok.Getter;

@Getter
public class LogInRequest {
    private String nickname;

    private String password;
}
