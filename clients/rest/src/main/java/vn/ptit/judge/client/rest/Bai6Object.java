package vn.ptit.judge.client.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpPrincipal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class Bai6Object {
    public static final String HTTP_SERVER = "http://36.50.135.242:2230";
    public static final String MSV = "B22DCCN393";
    public static final String QCODE = "WGUcboYT";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create(HTTP_SERVER + "/api/rest/object?studentCode="
                + MSV + "&qCode=" + QCODE)).GET().build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        JsonNode node = mapper.readTree(getResponse.body());

        String requestId = node.get("requestId").asText();
        JsonNode dataNode = node.get("data");

        double weightKq = dataNode.get("weightKg").asDouble();
        int maxEtaDays = dataNode.get("maxEtaDays").asInt();
        JsonNode quotes = dataNode.get("quotes");

        JsonNode bestQuote = null;
        BigDecimal minTotalFee = null;

        for (JsonNode quote : quotes) {
            int etaDays = quote.get("etaDays").asInt();
            if (etaDays <= maxEtaDays) {
                double baseFree = quote.get("baseFee").asDouble();
                double perKgFree = quote.get("perKgFee").asDouble();
                double reliability = quote.get("reliability").asDouble();

                BigDecimal totalFee = BigDecimal.valueOf(baseFree).
                        add(BigDecimal.valueOf(weightKq).
                                multiply(BigDecimal.valueOf(perKgFree)))
                        .setScale(2, RoundingMode.HALF_UP);

                if (bestQuote == null) {
                    bestQuote = quote;
                    minTotalFee = totalFee;
                } else {
                    int feeCompare = totalFee.compareTo(minTotalFee);
                    if (feeCompare < 0) {
                        bestQuote = quote;
                        minTotalFee = totalFee;
                    } else if (feeCompare == 0) {
                        double bestReliability = bestQuote.get("reliability").asDouble();
                        if (reliability > bestReliability) {
                            bestQuote = quote;
                            minTotalFee = totalFee;
                        }
                    }
                }
            }
        }

        System.out.println(getResponse.body());

        Map<String, Object> answer = Map.of(
                "carrier", bestQuote.get("carrier").asText(),
                "totalFee", minTotalFee,
                "etaDays", bestQuote.get("etaDays").asInt()
        );

        Map<String, Object> submitPayload = Map.of(
                "studentCode", MSV,
                "qCode", QCODE,
                "requestId", requestId,
                "answer", answer
        );

        HttpRequest postRequest = HttpRequest.newBuilder().uri(URI.create(HTTP_SERVER + "/api/rest/object/submit"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        mapper.writeValueAsString(submitPayload)
                )).build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(postResponse.body());

       }
}
