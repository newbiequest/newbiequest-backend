package com.newbiequest.newque.domain.post.dto.response;

import com.newbiequest.newque.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class PostResponse {
    private Long id;

    private Member member;

    private String title;

    private String body;

    private Long likes;

    private LocalDateTime createAt;
}
