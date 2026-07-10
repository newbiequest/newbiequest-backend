package com.newbiequest.newque.domain.chat.dto.request;

import lombok.Getter;

@Getter
public class ChatRequest {
    private Long accessToken;
    private String message;
}
