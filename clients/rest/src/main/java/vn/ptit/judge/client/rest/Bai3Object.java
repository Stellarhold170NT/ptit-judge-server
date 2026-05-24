package vn.ptit.judge.client.rest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Bai3Object {
    private static final String BASE_URL = "http://36.50.135.242:2230";
    private static final String STUDENT_CODE = "B22DCCN393";
    private static final String Q_CODE = "2ucfcULf";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        String getUrl = BASE_URL + "/api/rest/object?studentCode=" + STUDENT_CODE + "&qCode=" + Q_CODE;
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(getUrl))
                .GET()
                .build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        JsonNode root = mapper.readTree(getResponse.body());
        String requestId = root.get("requestId").asText();
        JsonNode productNode = root.get("data");

        String name = productNode.get("name").asText();
        double price = productNode.get("price").asDouble();
        double taxRate = productNode.get("taxRate").asDouble();
        double discount = productNode.get("discount").asDouble();

        double finalPrice = price * (1 + taxRate / 100.0) * (1 - discount / 100.0);

        ProductY answer = new ProductY(name, price, taxRate, discount, finalPrice);
        String submitJson = mapper.writeValueAsString(
                new SubmitRequest(STUDENT_CODE, Q_CODE, requestId, answer));
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/rest/object/submit"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(submitJson))
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        JsonNode submitRoot = mapper.readTree(postResponse.body());
        String status = submitRoot.get("status").asText();
        System.out.println("Bai3Object Result: " + status);
    }

    public static class SubmitRequest {
        public String studentCode;
        public String qCode;
        public String requestId;
        public ProductY answer;

        public SubmitRequest(String studentCode, String qCode, String requestId, ProductY answer) {
            this.studentCode = studentCode;
            this.qCode = qCode;
            this.requestId = requestId;
            this.answer = answer;
        }
    }

    public static class ProductY {
        public String name;
        public double price;
        public double taxRate;
        public double discount;
        public double finalPrice;

        public ProductY(String name, double price, double taxRate, double discount, double finalPrice) {
            this.name = name;
            this.price = price;
            this.taxRate = taxRate;
            this.discount = discount;
            this.finalPrice = finalPrice;
        }
    }
}
