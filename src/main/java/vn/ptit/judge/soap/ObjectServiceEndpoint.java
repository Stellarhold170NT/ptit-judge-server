package vn.ptit.judge.soap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import vn.ptit.judge.dto.ScoringResult;
import vn.ptit.judge.model.ProductY;
import vn.ptit.judge.service.ScoringService;

@Endpoint
public class ObjectServiceEndpoint {

    private static final String NAMESPACE = "http://ptit.vn/judge/soap/object";
    private static final String QCODE = "7u07dNqg";

    private final ScoringService scoringService;
    private final ConcurrentHashMap<String, String> requestIdMap = new ConcurrentHashMap<>();

    @Autowired
    public ObjectServiceEndpoint(ScoringService scoringService) {
        this.scoringService = scoringService;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "requestProductY")
    @ResponsePayload
    public RequestProductYResponse requestProductY(@RequestPayload RequestProductY request) {
        String studentCode = request.getStudentCode();
        String qCode = request.getQCode();

        Map<String, Object> result = scoringService.generateExerciseData(qCode);
        String requestId = (String) result.get("requestId");
        ProductY product = (ProductY) result.get("data");

        requestIdMap.put(buildKey(studentCode, qCode), requestId);

        return new RequestProductYResponse(SoapProductY.from(product));
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "submitProductY")
    @ResponsePayload
    public SubmitProductYResponse submitProductY(@RequestPayload SubmitProductY request) {
        String studentCode = request.getStudentCode();
        String qCode = request.getQCode();
        SoapProductY soapProduct = request.getProductY();
        String answer = String.format("%.2f", soapProduct.getFinalPrice());

        String requestId = requestIdMap.get(buildKey(studentCode, qCode));
        if (requestId == null) {
            return new SubmitProductYResponse("RTE: No data request found for this student+qCode");
        }

        ScoringResult result = scoringService.scoreSubmission(qCode, requestId, answer);
        scoringService.recordSubmission(studentCode, qCode, requestId, answer, result.getCorrectAnswer(), result.getStatus());

        return new SubmitProductYResponse(result.getStatus());
    }

    private static String buildKey(String studentCode, String qCode) {
        return studentCode + ":" + qCode;
    }
}
