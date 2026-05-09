package vn.ptit.judge.client.rest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Bai2Sort {
    private static final String BASE_URL = "http://localhost:2230";
    private static final String STUDENT_CODE = "B21DCCN001";
    private static final String Q_CODE = "oPW4mpqm";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        String getUrl = BASE_URL + "/api/rest/character?studentCode=" + STUDENT_CODE + "&qCode=" + Q_CODE;
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(getUrl))
                .GET()
                .build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        JsonNode root = mapper.readTree(getResponse.body());
        String requestId = root.get("requestId").asText();
        String data = root.get("data").asText();

        String[] words = data.split(" ");
        Arrays.sort(words);
        String answer = String.join(" ", words);

        String submitJson = mapper.writeValueAsString(
                new SubmitRequest(STUDENT_CODE, Q_CODE, requestId, answer));
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/rest/character/submit"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(submitJson))
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        JsonNode submitRoot = mapper.readTree(postResponse.body());
        String status = submitRoot.get("status").asText();
        System.out.println("Bai2Sort Result: " + status);
    }

    public static class SubmitRequest {
        public String studentCode;
        public String qCode;
        public String requestId;
        public String answer;

        public SubmitRequest(String studentCode, String qCode, String requestId, String answer) {
            this.studentCode = studentCode;
            this.qCode = qCode;
            this.requestId = requestId;
            this.answer = answer;
        }
    }
}
