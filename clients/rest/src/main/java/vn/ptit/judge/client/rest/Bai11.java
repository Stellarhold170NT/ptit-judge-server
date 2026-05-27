package vn.ptit.judge.client.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
Một dịch vụ REST MethodService được triển khai trên server tại URL http://<Exam_IP>:2230/api/rest/method để kiểm tra cách sử dụng HTTP method trong quy trình submit.

Yêu cầu: Viết chương trình tại máy trạm (REST client) để giao tiếp với MethodService và thực hiện các công việc sau.

a. Gửi GET /api/rest/method?studentCode=<mã_sinh_viên>&qCode=<qAlias> để nhận ticket cần xử lý. qCode là alias runtime được giao.

b. Server trả về requestId và data gồm ticketId, status, targetStatus, version, etag.

c. Gửi phase 2 bằng phương thức PATCH tới /api/rest/method/{requestId} và kèm header If-Match đúng bằng etag nhận được.

d. Body JSON chứa studentCode, qCode và answer; trong đó answer.status bằng RESOLVED.

e. Thiếu header If-Match, sai etag, hoặc dùng sai phương thức sẽ không đạt.
*/
public class Bai11 {
    public static final String HTTP_SERVER = "http://36.50.135.242:2230";
    public static final String MSV = "B22DCCN393";
    public static final String QCODE = "OP6MadHF";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // a. Gửi GET
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_SERVER + "/api/rest/method?studentCode=" + MSV + "&qCode=" + QCODE))
                .GET().build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        JsonNode node = mapper.readTree(getResponse.body());
        String requestId = node.get("requestId").asText();
        JsonNode data = node.get("data");

        // Đọc etag từ data
        String etag = data.get("etag").asText();

        // Sao chép toàn bộ data sang answer
        Map<String, Object> answer = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = data.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            JsonNode val = field.getValue();
            if (val.isNumber()) {
                answer.put(field.getKey(), val.numberValue());
            } else if (val.isBoolean()) {
                answer.put(field.getKey(), val.asBoolean());
            } else {
                answer.put(field.getKey(), val.asText());
            }
        }

        // d. Thay đổi status thành RESOLVED
        answer.put("status", "RESOLVED");

        Map<String, Object> payload = Map.of(
                "studentCode", MSV,
                "qCode", QCODE,
                "answer", answer
        );

        // c. Gửi PATCH tới /api/rest/method/{requestId} kèm header If-Match
        HttpRequest patchRequest = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_SERVER + "/api/rest/method/" + requestId))
                .header("Content-Type", "application/json")
                .header("If-Match", etag)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();

        HttpResponse<String> patchResponse = client.send(patchRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(patchResponse.body());
    }
}
