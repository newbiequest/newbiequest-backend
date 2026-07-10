package com.newbiequest.newque.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScoreResponse {
    private String nickname;
    private Long score;
}
