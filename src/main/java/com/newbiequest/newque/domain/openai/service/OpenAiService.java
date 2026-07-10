package com.newbiequest.newque.domain.openai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbiequest.newque.domain.member.entity.Member;
import com.newbiequest.newque.domain.member.repository.MemberRepository;
import com.newbiequest.newque.domain.openai.Repository.MissionCountRepository;
import com.newbiequest.newque.domain.openai.dto.response.OpenAiResponse;
import com.newbiequest.newque.domain.openai.entity.MissionCount;
import com.newbiequest.newque.global.client.openai.OpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final OpenAiClient openAiClient;
    private final MissionCountRepository missionCountRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public OpenAiResponse getAnswer(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();

        MissionCount missionCount = missionCountRepository.findByMemberId(memberId)
                .orElseGet(() -> missionCountRepository.save(
                        MissionCount.builder().member(member).build()));

        List<String> candidates = getLeastClearedMissions(missionCount);

        com.newbiequest.newque.global.client.openai.dto.OpenAiResponse openAiResponse = openAiClient.getChatCompletion(candidates, member.getNickname());
        String contnet = openAiResponse.getChoices().get(0).getMessage().getContent();

        try {
            return objectMapper.readValue(contnet, OpenAiResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response: " + contnet, e);
        }
    }

    @Transactional
    public void clearMission(Long memberId, String taskType) {
        MissionCount missionCount = missionCountRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("MissionCount not found"));
        missionCount.clearMission(taskType);
    }

    private List<String> getLeastClearedMissions(MissionCount missionCount) {
        Map<String, Integer> countMap = missionCount.toMap();
        Integer minCount = Collections.min(countMap.values());

        return countMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(minCount))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
