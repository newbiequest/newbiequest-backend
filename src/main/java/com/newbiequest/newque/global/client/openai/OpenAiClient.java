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
                        "응답은 아래 필드를 가진 JSON 한 줄로만 하세요. 코드블록 없이 순수 JSON만 반환하세요. " +
                        "npcName 은 반드시 \"" + npcName + "\" 으로 하세요. " +
                        "isNpcMission 은 false, npcMissionType 은 null 로 하세요. " +

                        "taskType 별 수행 장소는 반드시 message 안에 자연스럽게 포함하세요. " +
                        "PRINT 장소는 복사실, COFFEE 장소는 탕비실, COMPUTER 장소는 사무실 빈자리, " +
                        "PARCEL 장소는 택배실, DOC_STORAGE 장소는 문서보관실, " +
                        "SMALL_MTG 장소는 소회의실, BIG_MTG 장소는 대회의실입니다. " +

                        "taskType 별로 포함할 필드와 값 범위는 다음과 같습니다. " +
                        "PRINT: copyCount(1~10), 나머지 미션 필드 null. " +
                        "COFFEE: coffeeCount(1~5), sugarCount(0~3), 나머지 미션 필드 null. " +
                        "COMPUTER: pageCount(1~2), 나머지 미션 필드 null. " +
                        "PARCEL: ownerName(NPC 이름 중 하나), 나머지 미션 필드 null. " +
                        "DOC_STORAGE: sortingType(DATE/NAME/NUMBER 중 하나), 나머지 미션 필드 null. " +
                        "DOC_STORAGE message에서는 sortingType이 DATE면 날짜순, NAME이면 이름순, NUMBER면 번호순이라고 표현하세요. " +
                        "SMALL_MTG: 소회의실 예약하기 미션입니다. meetingHeadcount(2~7), meetingStartHour(9~18), meetingStartMinute(0~59), meetingPurpose(회의 목적), 나머지 미션 필드 null. " +
                        "BIG_MTG: 대회의실 예약하기 미션입니다. meetingHeadcount(8~9), meetingStartHour(9~18), meetingStartMinute(0~59), meetingPurpose(회의 목적), 나머지 미션 필드 null. " +
                        "회의 미션이 아닌 taskType 에서는 meetingStartHour, meetingStartMinute, meetingPurpose 를 null 로 하세요. " +

                        "message 는 신입사원을 \"" + nickname + "\" 님이라고 부르며 작성하세요. " +
                        "message의 말투는 매번 다양하게 작성하세요. " +
                        "존댓말만 사용하지 말고 실제 직장 상사처럼 상황에 따라 반말, 존댓말, 혼합형 말투를 모두 사용할 수 있습니다. " +
                        "예를 들어 '" + nickname + "님, 이거 부탁드립니다.', '" + nickname + "님, 이건 바로 처리해 주세요.', '" + nickname + "님, 이거 오늘 안으로 해줘요.', '" + nickname + "님, 복사실 가서 5부만 복사해 와요.' 와 같이 작성할 수 있습니다. " +
                        "같은 말투나 문장 패턴을 반복하지 마세요. " +
                        "지시형, 부탁형, 확인형, 보고요청형, 이유설명형을 무작위로 사용하세요. " +
                        "다만 무례하거나 공격적이거나 인신공격성 표현은 사용하지 마세요. " +

                        "불필요한 인사말, 잡담, 장황한 설명은 빼고 한 문장 또는 두 문장으로 짧게 작성하세요. " +
                        "message 안에는 반드시 수행 장소와 미션 성공에 필요한 조건을 모두 포함하세요. " +

                        "PRINT message에는 복사실과 copyCount를 포함하세요. " +
                        "COFFEE message에는 탕비실, coffeeCount, sugarCount를 포함하세요. " +
                        "COMPUTER message에는 사무실 빈자리, pageCount, 작업 내용 또는 문서라는 표현을 포함하세요. 안내 문구라는 표현은 사용하지 마세요. " +
                        "PARCEL message에는 택배실과 ownerName을 포함하세요. " +
                        "DOC_STORAGE message에는 문서보관실과 정리 기준을 포함하세요. " +
                        "SMALL_MTG message에는 소회의실 예약, meetingHeadcount, meetingStartHour, meetingStartMinute, meetingPurpose를 포함하세요. " +
                        "BIG_MTG message에는 대회의실 예약, meetingHeadcount, meetingStartHour, meetingStartMinute, meetingPurpose를 포함하세요."
        );
        OpenAiMessage userMessage = new OpenAiMessage("user", "업무를 지시해주세요.");

        return new OpenAiRequest(model, List.of(systemMessage, userMessage));
    }
}