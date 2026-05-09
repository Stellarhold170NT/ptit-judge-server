package vn.ptit.judge.soap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import vn.ptit.judge.dto.ScoringResult;
import vn.ptit.judge.service.ScoringService;

@Endpoint
public class DataServiceEndpoint {

    private static final String NAMESPACE = "http://ptit.vn/judge/soap/data";
    private static final String QCODE = "yyhHZUpt";

    private final ScoringService scoringService;
    private final ConcurrentHashMap<String, String> requestIdMap = new ConcurrentHashMap<>();

    @Autowired
    public DataServiceEndpoint(ScoringService scoringService) {
        this.scoringService = scoringService;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "getDataRequest")
    @ResponsePayload
    public GetDataResponse getData(@RequestPayload GetDataRequest request) {
        String studentCode = request.getStudentCode();
        String qCode = request.getQCode();

        Map<String, Object> result = scoringService.generateExerciseData(qCode);
        String requestId = (String) result.get("requestId");

        requestIdMap.put(buildKey(studentCode, qCode), requestId);

        @SuppressWarnings("unchecked")
        List<Integer> intList = (List<Integer>) result.get("data");

        GetDataResponse response = new GetDataResponse();
        response.setData(intList.stream().map(String::valueOf).collect(Collectors.toList()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "submitDataIntRequest")
    @ResponsePayload
    public SubmitDataIntResponse submitDataInt(@RequestPayload SubmitDataIntRequest request) {
        String studentCode = request.getStudentCode();
        String qCode = request.getQCode();
        String answer = String.valueOf(request.getSum());

        String requestId = requestIdMap.get(buildKey(studentCode, qCode));
        if (requestId == null) {
            return new SubmitDataIntResponse("RTE: No data request found for this student+qCode");
        }

        ScoringResult result = scoringService.scoreSubmission(qCode, requestId, answer);
        scoringService.recordSubmission(studentCode, qCode, requestId, answer, result.getCorrectAnswer(), result.getStatus());

        return new SubmitDataIntResponse(result.getStatus());
    }

    private static String buildKey(String studentCode, String qCode) {
        return studentCode + ":" + qCode;
    }
}
