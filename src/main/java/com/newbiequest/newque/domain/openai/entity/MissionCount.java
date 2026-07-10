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

    private Integer print;
    private Integer coffee;
    private Integer computer;
    private Integer parcel;
    private Integer docStorage;
    private Integer smallMtg;
    private Integer bigMtg;
    private Integer total;

    @Builder
    public MissionCount(Member member) {
        this.member = member;
        this.print = 0;
        this.coffee = 0;
        this.computer = 0;
        this.parcel = 0;
        this.docStorage = 0;
        this.smallMtg = 0;
        this.bigMtg = 0;
        this.total = 0;
    }

    public void clearMission(String taskType) {
        switch (taskType) {
            case "PRINT"       -> this.print++;
            case "COFFEE"      -> this.coffee++;
            case "COMPUTER"    -> this.computer++;
            case "PARCEL"      -> this.parcel++;
            case "DOC_STORAGE" -> this.docStorage++;
            case "SMALL_MTG"   -> this.smallMtg++;
            case "BIG_MTG"     -> this.bigMtg++;
            default -> throw new RuntimeException("Unknown taskType: " + taskType);
        }
        this.total = (this.total == null ? 0 : this.total) + 1;
    }

    public Map<String, Integer> toMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("PRINT",       this.print);
        map.put("COFFEE",      this.coffee);
        map.put("COMPUTER",    this.computer);
        map.put("PARCEL",      this.parcel);
        map.put("DOC_STORAGE", this.docStorage);
        map.put("SMALL_MTG",   this.smallMtg);
        map.put("BIG_MTG",     this.bigMtg);
        return map;
    }
}