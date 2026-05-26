package com.newbiequest.newque.domain.post.dto.request;

import lombok.Getter;

@Getter
public class PostRequest {
    private Long accessToken;

    private String title;

    private String body;
}
