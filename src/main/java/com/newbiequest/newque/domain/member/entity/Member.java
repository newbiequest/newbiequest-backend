package com.newbiequest.newque.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    @NotBlank
    String nickname;

    @NotBlank
    String password;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    Long score = 0L;

    @Builder
    public Member(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
        this.score = 0L;
    }

    public void updateScore(Long newScore) {
        if (newScore > this.score) {
            this.score = newScore;
        }
    }
}
