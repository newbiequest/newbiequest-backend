package com.newbiequest.newque.domain.chat.entity;

import com.newbiequest.newque.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_chat_member_id"))
    Member member;

    @NotNull
    String message;

    LocalDateTime createAt;

    @Builder
    public Chat(Member member, String message) {
        this.member = member;
        this.message = message;
        this.createAt = LocalDateTime.now();
    }
}
