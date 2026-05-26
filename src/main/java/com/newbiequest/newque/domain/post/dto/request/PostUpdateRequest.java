package com.newbiequest.newque.domain.post.dto.request;

import lombok.Getter;

@Getter
public class PostUpdateRequest {
    private String title;

    private String body;

    private Long accessToken;
}
