package com.newbiequest.newque.domain.openai.entity;

import com.newbiequest.newque.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Integer copy;
    private Integer delivery;
    private Integer stamp;
    private Integer meeting;
    private Integer typing;
    private Integer total;

    @Builder
    public MissionCount(Member member) {
        this.member = member;
        this.copy = 0;
        this.delivery = 0;
        this.stamp = 0;
        this.meeting = 0;
        this.typing = 0;
        this.total = 0;
    }

    public void clearMission(String taskType) {
        switch (taskType) {
            case "COPY" -> this.copy++;
            case "DELIVERY" -> this.delivery++;
            case "STAMP" -> this.stamp++;
            case "MEETING" -> this.meeting++;
            case "TYPING" -> this.typing++;
            default -> throw new RuntimeException("Unknown taskType: " + taskType);
        }
        this.total = (this.total == null ? 0: this.total) + 1;
    }

    public Map<String, Integer> toMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("COPY", this.copy);
        map.put("DELIVERY", this.delivery);
        map.put("STAMP", this.stamp);
        map.put("MEETING", this.meeting);
        map.put("TYPING", this.typing);

        return map;
    }
}
