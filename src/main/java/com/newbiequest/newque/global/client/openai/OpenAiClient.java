package com.newbiequest.newque.global.client.openai;

import com.newbiequest.newque.global.client.openai.dto.OpenAiMessage;
import com.newbiequest.newque.global.client.openai.dto.OpenAiRequest;
import com.newbiequest.newque.global.client.openai.dto.OpenAiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private final RestTemplate restTemplate;

    @Value("${openai.api-url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    public OpenAiResponse getChatCompletion(String prompt) {
        OpenAiRequest openAiRequest = getOpenAiRequest(prompt);

        ResponseEntity<OpenAiResponse> chatResponse = restTemplate.postForEntity(
                apiUrl,
                openAiRequest,
                OpenAiResponse.class
        );

        if (!chatResponse.getStatusCode().is2xxSuccessful() || chatResponse.getBody() == null) {
            throw new RuntimeException("OpenAI API error");
        }

        return chatResponse.getBody();
    }

    private OpenAiRequest getOpenAiRequest(String prompt) {
        OpenAiMessage systemMessage = new OpenAiMessage(
                "system",
                "너는 누구야 라는 문장으로 질문 할 경우 짧게 소개해줘." +
                        "그 외의 질문에는 친절한 AI 비서로서 답변해주세요."
        );
        OpenAiMessage userMessage = new OpenAiMessage("user", prompt);

        List<OpenAiMessage> messages = List.of(systemMessage, userMessage);

        return new OpenAiRequest(model, messages);
    }
}
