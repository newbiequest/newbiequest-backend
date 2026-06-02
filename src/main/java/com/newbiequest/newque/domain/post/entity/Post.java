package com.newbiequest.newque.domain.post.entity;

import com.newbiequest.newque.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_post_member_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    Member member;

    @NotNull
    String title;

    String body;

    Long likes;

    LocalDateTime createAt;

    @Builder
    public Post (String title, String body, Member member) {
        this.title = title;
        this.body = body;
        this.member = member;
        this.likes = 0L;
        this.createAt = LocalDateTime.now();
    }
    public void updateTitle(final String title) {
        this.title = title;
    }

    public void updateBody(final String body) {
        this.body = body;
    }

    public void pressLikes() {
        this.likes++;
    }

    public void unpressLikes() {
        this.likes--;
    }
}