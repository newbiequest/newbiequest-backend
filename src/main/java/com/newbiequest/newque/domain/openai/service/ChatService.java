package com.newbiequest.newque.domain.openai.service;

import com.newbiequest.newque.global.client.openai.OpenAiClient;
import com.newbiequest.newque.global.client.openai.dto.OpenAiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiClient openAiClient;

    public String getAnswer(String question) {
        OpenAiResponse openAiResponse = openAiClient.getChatCompletion(question);

        return openAiResponse.getChoices().get(0).getMessage().getContent();
    }
}
