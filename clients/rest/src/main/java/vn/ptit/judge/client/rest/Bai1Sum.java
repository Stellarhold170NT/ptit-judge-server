package vn.ptit.judge.client.rest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Bai1Sum {
    private static final String BASE_URL = "http://36.50.135.242:2230";
    private static final String STUDENT_CODE = "B22DCCN393";
    private static final String Q_CODE = "4siaIVgn";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        String getUrl = BASE_URL + "/api/rest/data?studentCode=" + STUDENT_CODE + "&qCode=" + Q_CODE;
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(getUrl))
                .GET()
                .build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        JsonNode root = mapper.readTree(getResponse.body());
        String requestId = root.get("requestId").asText();
        JsonNode dataNode = root.get("data");

        int sum = 0;
        for (JsonNode node : dataNode) {
            sum += node.asInt();
        }

        String submitJson = mapper.writeValueAsString(
                new SubmitRequest(STUDENT_CODE, Q_CODE, requestId, sum));
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/rest/data/submit"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(submitJson))
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        JsonNode submitRoot = mapper.readTree(postResponse.body());
        String status = submitRoot.get("status").asText();
        System.out.println("Bai1Sum Result: " + status);
    }

    public static class SubmitRequest {
        public String studentCode;
        public String qCode;
        public String requestId;
        public Integer answer;

        public SubmitRequest(String studentCode, String qCode, String requestId, Integer answer) {
            this.studentCode = studentCode;
            this.qCode = qCode;
            this.requestId = requestId;
            this.answer = answer;
        }
    }
}
