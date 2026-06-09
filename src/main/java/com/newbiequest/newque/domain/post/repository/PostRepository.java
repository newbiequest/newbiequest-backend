package com.newbiequest.newque.domain.post.repository;

import com.newbiequest.newque.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByMemberId(Long memberId);
    List<Post> findAllByOrderByCreateAtDesc();
}