package com.newbiequest.newque.domain.openai.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OpenAiResponse {

    private String npcName;
    private String taskType;
    private String message;
    private Boolean isNpcMission;
    private String npcMissionType;
    private Integer copyCount;
    private Integer coffeeCount;
    private Integer sugarCount;
    private Integer pageCount;
    private String ownerName;
    private String sortingType;
    private Integer meetingHeadcount;
    private Integer meetingStartHour;
    private Integer meetingStartMinute;
    private String meetingPurpose;
}