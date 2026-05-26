package com.newbiequest.newque.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LogInResponse {
    private Long accessToken;

    private String nickname;
}
