package vn.ptit.judge.client.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class Bai4ArrayObject {
    private static final String HTTP_SERVER = "http://36.50.135.242:2230";
    private static final String MSV = "B22DCCN393";
    private static final String QCODE = "ggGFmI1T";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(HTTP_SERVER + "/api/rest/data?studentCode="
            + MSV + "&qCode=" + QCODE)).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode node = mapper.readTree(response.body());
        String requestId = node.get("requestId").asText();

        JsonNode data = node.get("data");

        BigDecimal capturedTotal = BigDecimal.ZERO;
        BigDecimal refundedTotal = BigDecimal.ZERO;
        BigDecimal netTotal = BigDecimal.ZERO;

        Integer failedCount = 0;
        for (JsonNode n : data) {
            BigDecimal amount = BigDecimal.valueOf(n.get("amount").asDouble());
            String status = n.get("status").asText();
            if ("CAPTURED".equalsIgnoreCase(status)) {
                capturedTotal = capturedTotal.add(amount);
            }
            else if ("REFUNDED".equalsIgnoreCase(status)) {
                refundedTotal = refundedTotal.add(amount);
            }
            else if ("FAILED".equalsIgnoreCase(status)) {
                failedCount++;
            }
        }

        netTotal = capturedTotal.subtract(refundedTotal);
        capturedTotal = capturedTotal.setScale(2, RoundingMode.HALF_UP);
        refundedTotal = refundedTotal.setScale(2, RoundingMode.HALF_UP);
        netTotal = netTotal.setScale(2, RoundingMode.HALF_UP);

        Map<String, Object> answer = Map.of(
                "capturedTotal", capturedTotal,
                "refundedTotal", refundedTotal,
                "netTotal", netTotal,
                "failedCount", failedCount
        );

        Map<String, Object> submitPayload = Map.of(
                "studentCode", MSV,
                "qCode", QCODE,
                "requestId", requestId,
                "answer", answer
        );

        String submitJson = mapper.writeValueAsString(submitPayload);
        HttpRequest postRequest = HttpRequest.newBuilder().uri(URI.create(HTTP_SERVER + "/api/rest/data/submit"))
                .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(submitJson)).build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(postResponse.body());
    }
}
