package com.newbiequest.newque.domain.openai.Repository;

import com.newbiequest.newque.domain.openai.entity.MissionCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionCountRepository extends JpaRepository<MissionCount, Long> {
    Optional<MissionCount> findByMemberId(Long memberId);
}
