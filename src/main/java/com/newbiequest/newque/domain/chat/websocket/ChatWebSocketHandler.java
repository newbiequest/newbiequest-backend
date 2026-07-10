package com.newbiequest.newque.domain.chat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.newbiequest.newque.domain.chat.dto.request.ChatRequest;
import com.newbiequest.newque.domain.chat.dto.response.ChatResponse;
import com.newbiequest.newque.domain.chat.entity.Chat;
import com.newbiequest.newque.domain.chat.service.ChatService;
import com.newbiequest.newque.domain.member.entity.Member;
import com.newbiequest.newque.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    private final MemberService memberService;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    public ChatWebSocketHandler(
            @Qualifier("chatMessageService") ChatService chatService,
            MemberService memberService
    ) {
        this.chatService = chatService;
        this.memberService = memberService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("WebSocket connected: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatRequest chatRequest = objectMapper.readValue(message.getPayload(), ChatRequest.class);

        Member member = memberService.retrieveToken(chatRequest.getAccessToken());
        Chat chat = chatService.saveChat(chatRequest, member);
        ChatResponse chatResponse = chatService.toChatResponse(chat);

        String responseJson = objectMapper.writeValueAsString(chatResponse);
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(responseJson));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("WebSocket disconnected: {}", session.getId());
    }
}