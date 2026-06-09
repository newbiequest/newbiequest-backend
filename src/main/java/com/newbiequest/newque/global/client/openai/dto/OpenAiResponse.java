package com.newbiequest.newque.global.client.openai.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OpenAiResponse {

    private List<Choice> choices;

    @Getter
    public static class Choice {
        private OpenAiMessage message;
    }
}
