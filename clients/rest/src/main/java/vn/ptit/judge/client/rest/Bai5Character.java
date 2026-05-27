package vn.ptit.judge.client.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.*;
import java.util.Map;


public class Bai5Character {
    public static final String HTTP_SERVER = "http://36.50.135.242:2230";
    public static final String MSV = "B22DCCN393";
    public static final String QCODE = "YCFwf95e";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(HTTP_SERVER + "/api/rest/character?studentCode="
                + MSV + "&qCode=" + QCODE)).GET().build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        JsonNode node = mapper.readTree(getResponse.body());

        System.out.println(getResponse.body());

        String requestId = node.get("requestId").asText();
        String data = node.get("data").asText();

        String userRegex = "user=\\S+";
        String phoneRegex = "phone=\\S+";
        String tokenRegex = "token=\\S+";

        String[] lines = data.split("\\|\\|");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            line = line.replaceAll(userRegex, "user=[EMAIL]");
            line = line.replaceAll(phoneRegex, "phone=[PHONE]");
            line = line.replaceAll(tokenRegex, "token=[TOKEN]");
            lines[i] = line;
        }

        String answer = String.join("||", lines);
        Map<String, Object> submitPayload = Map.of(
                "studentCode", MSV,
                "qCode", QCODE,
                "requestId", requestId,
                "answer", answer
        );

        String submitJson = mapper.writeValueAsString(submitPayload);
        HttpRequest postRequest = HttpRequest.newBuilder().uri(URI.create(HTTP_SERVER + "/api/rest/character/submit"))
                .POST(HttpRequest.BodyPublishers.ofString(submitJson)).build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(postResponse.body());
    }
}
