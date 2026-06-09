package com.newbiequest.newque.global.client.openai;

import com.newbiequest.newque.global.client.openai.dto.OpenAiMessage;
import com.newbiequest.newque.global.client.openai.dto.OpenAiRequest;
import com.newbiequest.newque.global.client.openai.dto.OpenAiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private final RestTemplate restTemplate;

    @Value("${openai.api-url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    public static final List<String> NPC_NAMES = List.of(
            "김부장", "한부장", "신부장",
            "이과장", "윤과장", "홍과장", "류과장", "황과장",
            "박팀장", "장팀장", "노팀장", "전팀장",
            "최대리", "임대리", "송대리", "문대리"
    );

    public OpenAiResponse getChatCompletion(List<String> candidates, String nickname) {
        OpenAiRequest openAiRequest = getOpenAiRequest(candidates, nickname);

        ResponseEntity<OpenAiResponse> chatResponse = restTemplate.postForEntity(
                apiUrl,
                openAiRequest,
                OpenAiResponse.class
        );

        if (!chatResponse.getStatusCode().is2xxSuccessful() || chatResponse.getBody() == null) {
            throw new RuntimeException("OpenAI API error");
        }

        return chatResponse.getBody();
    }

    private OpenAiRequest getOpenAiRequest(List<String> candidates, String nickname) {
        String npcName = NPC_NAMES.get(new Random().nextInt(NPC_NAMES.size()));

        OpenAiMessage systemMessage = new OpenAiMessage(
                "system",
                npcName + "님은 회사 상사가 되어 신입사원에게 업무를 지시합니다. " +
                        "다음 업무 목록 중 하나를 골라 업무를 지시하세요: " + String.join(", ", candidates) + ". " +
                        "응답은 npcName, taskType, condition, message 필드를 가진 JSON 한 줄로만 하세요. " +
                        "필드명은 정확히 소문자로 npcName, taskType, condition, message 를 사용하세요. " +
                        "코드블록 없이 순수 JSON 만 반환하세요. " +
                        "npcName 은 반드시 \"" + npcName + "\" 으로 하세요. " +
                        "taskType 별 condition 형식은 다음과 같습니다. " +
                        "COPY: 숫자 (예: \"3\" -> 3부 복사). " +
                        "DELIVERY: 성씨+직급 (예: \"김부장\" -> 김부장에게 전달). " +
                        "STAMP: 성씨+직급 (예: \"이과장\" -> 이과장에게 도장). " +
                        "MEETING: 시작-종료 인원, 회의는 반드시 2시간 고정 (예: \"13-15 5\" -> 13시~15시 5명). " +
                        "TYPING: 숫자 (예: \"3\" -> 3문제). " +
                        "message 는 신입사원을 \"" + nickname + "\" 님이라고 부르며 상사가 신입사원에게 말하듯 자연스럽게 작성하세요."
        );
        OpenAiMessage userMessage = new OpenAiMessage("user", "업무를 지시해주세요.");

        return new OpenAiRequest(model, List.of(systemMessage, userMessage));
    }
}
