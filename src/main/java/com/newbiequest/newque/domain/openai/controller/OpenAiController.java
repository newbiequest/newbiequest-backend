package com.newbiequest.newque.domain.openai.controller;

import com.newbiequest.newque.domain.openai.dto.request.MissionCompleteRequest;
import com.newbiequest.newque.domain.openai.dto.response.ChatResponse;
import com.newbiequest.newque.domain.openai.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OpenAiController {

    private final ChatService chatService;

    @PostMapping("/mission/{memberId}")
    public ResponseEntity<ChatResponse> getMission(
            @PathVariable Long memberId
    ) {
        ChatResponse response = chatService.getAnswer(memberId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/mission/complete/{memberId}/{taskType}")
    public ResponseEntity<Void> completeMission(
            @PathVariable Long memberId,
            @PathVariable String taskType,
            @RequestBody MissionCompleteRequest request
    ) {
        if (request.isCompleted()) {
            chatService.clearMission(memberId, taskType);
        }

        return ResponseEntity.ok().build();
    }
}
