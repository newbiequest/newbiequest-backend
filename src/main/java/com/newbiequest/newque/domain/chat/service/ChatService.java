package com.newbiequest.newque.domain.chat.service;

import com.newbiequest.newque.domain.chat.dto.request.ChatRequest;
import com.newbiequest.newque.domain.chat.dto.response.ChatResponse;
import com.newbiequest.newque.domain.chat.entity.Chat;
import com.newbiequest.newque.domain.chat.repository.ChatRepository;
import com.newbiequest.newque.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("chatMessageService")
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    @Transactional
    public Chat saveChat(ChatRequest chatRequest, Member member) {
        Chat chat = Chat.builder()
                .member(member)
                .message(chatRequest.getMessage())
                .build();

        return chatRepository.save(chat);
    }

    public ChatResponse toChatResponse(Chat chat) {
        return ChatResponse.builder()
                .nickname(chat.getMember().getNickname())
                .createAt(chat.getCreateAt())
                .message(chat.getMessage())
                .build();
    }

    @Transactional
    public List<Chat> getAllChats() {
        return chatRepository.findAllByOrderByCreateAtAsc();
    }

    public List<ChatResponse> chatsToChatResponses(List<Chat> chats) {
        return chats.stream()
                .map(this::toChatResponse)
                .collect(Collectors.toList());
    }
}
