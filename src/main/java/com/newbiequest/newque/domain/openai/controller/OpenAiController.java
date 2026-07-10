package com.newbiequest.newque.domain.openai.controller;

import com.newbiequest.newque.domain.member.entity.Member;
import com.newbiequest.newque.domain.member.service.MemberService;
import com.newbiequest.newque.domain.openai.dto.request.MissionCompleteRequest;
import com.newbiequest.newque.domain.openai.dto.response.OpenAiResponse;
import com.newbiequest.newque.domain.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OpenAiController {

    private final OpenAiService chatService;
    private final MemberService memberService;

    @PostMapping("/mission/{accessToken}")
    public ResponseEntity<OpenAiResponse> getMission(
            @PathVariable Long accessToken
    ) {
        Member member = memberService.retrieveToken(accessToken);
        OpenAiResponse response = chatService.getAnswer(member.getId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/mission/complete/{accessToken}/{taskType}")
    public ResponseEntity<Void> completeMission(
            @PathVariable Long accessToken,
            @PathVariable String taskType,
            @RequestBody MissionCompleteRequest request
    ) {
        Member member = memberService.retrieveToken(accessToken);

        if (request.isCompleted()) {
            chatService.clearMission(member.getId(), taskType);
        }

        return ResponseEntity.ok().build();
    }
}
