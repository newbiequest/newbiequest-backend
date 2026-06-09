package com.newbiequest.newque.domain.openai.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatResponse {

    private String npcName;
    private String taskType;
    private String condition;
    private String message;
}
