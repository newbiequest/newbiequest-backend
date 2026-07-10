package com.newbiequest.newque.domain.chat.controller;

import com.newbiequest.newque.domain.chat.dto.request.ChatRequest;
import com.newbiequest.newque.domain.chat.dto.response.ChatResponse;
import com.newbiequest.newque.domain.chat.entity.Chat;
import com.newbiequest.newque.domain.chat.service.ChatService;
import com.newbiequest.newque.domain.member.entity.Member;
import com.newbiequest.newque.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    @Qualifier("chatMessageService")
    private final ChatService chatService;
    private final MemberService memberService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> sendChat(@RequestBody ChatRequest chatRequest) {
        Member member = memberService.retrieveToken(chatRequest.getAccessToken());
        Chat chat = chatService.saveChat(chatRequest, member);
        ChatResponse chatResponse = chatService.toChatResponse(chat);

        return ResponseEntity.ok(chatResponse);
    }

    @GetMapping("/chat")
    public ResponseEntity<List<ChatResponse>> getAllChats() {
        List<Chat> chats = chatService.getAllChats();
        List<ChatResponse> chatResponses = chatService.chatsToChatResponses(chats);

        return ResponseEntity.ok(chatResponses);
    }
}
