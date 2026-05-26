package com.newbiequest.newque.domain.openai.controller;

import com.newbiequest.newque.domain.openai.dto.request.ChatRequest;
import com.newbiequest.newque.domain.openai.dto.response.ChatResponse;
import com.newbiequest.newque.domain.openai.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class OpenAiController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest chatRequest) {
        String answer = chatService.getAnswer(chatRequest.getMessage());

        return ResponseEntity.ok(ChatResponse.of(answer));
    }
}
