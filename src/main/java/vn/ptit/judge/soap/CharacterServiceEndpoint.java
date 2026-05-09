package vn.ptit.judge.soap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import vn.ptit.judge.dto.ScoringResult;
import vn.ptit.judge.service.ScoringService;

@Endpoint
public class CharacterServiceEndpoint {

    private static final String NAMESPACE = "http://ptit.vn/judge/soap/character";
    private static final String QCODE = "qvCGMEhW";

    private final ScoringService scoringService;
    private final ConcurrentHashMap<String, String> requestIdMap = new ConcurrentHashMap<>();

    @Autowired
    public CharacterServiceEndpoint(ScoringService scoringService) {
        this.scoringService = scoringService;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "requestString")
    @ResponsePayload
    public RequestStringResponse requestString(@RequestPayload RequestString request) {
        String studentCode = request.getStudentCode();
        String qCode = request.getQCode();

        Map<String, Object> result = scoringService.generateExerciseData(qCode);
        String requestId = (String) result.get("requestId");
        String data = (String) result.get("data");

        requestIdMap.put(buildKey(studentCode, qCode), requestId);

        return new RequestStringResponse(data);
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "submitString")
    @ResponsePayload
    public SubmitStringResponse submitString(@RequestPayload SubmitString request) {
        String studentCode = request.getStudentCode();
        String qCode = request.getQCode();
        String answer = request.getAnswer();

        String requestId = requestIdMap.get(buildKey(studentCode, qCode));
        if (requestId == null) {
            return new SubmitStringResponse("RTE: No data request found for this student+qCode");
        }

        ScoringResult result = scoringService.scoreSubmission(qCode, requestId, answer);
        scoringService.recordSubmission(studentCode, qCode, requestId, answer, result.getCorrectAnswer(), result.getStatus());

        return new SubmitStringResponse(result.getStatus());
    }

    private static String buildKey(String studentCode, String qCode) {
        return studentCode + ":" + qCode;
    }
}
