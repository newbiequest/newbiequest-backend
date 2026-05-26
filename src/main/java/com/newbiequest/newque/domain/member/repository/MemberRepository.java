package com.newbiequest.newque.domain.member.repository;

import com.newbiequest.newque.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByNickname(String nickname);
    Boolean existsByNickname(String nickname);
}
