package vn.ptit.judge.client.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

/*
Một dịch vụ REST HeaderService được triển khai trên server tại URL http://<Exam_IP>:2230/api/rest/header để kiểm tra cách đọc và gửi HTTP header.

Yêu cầu: Viết chương trình tại máy trạm (REST client) để giao tiếp với HeaderService và thực hiện các công việc sau.

a. Gửi GET /api/rest/header?studentCode=<mã_sinh_viên>&qCode=<qAlias>. qCode là alias runtime được giao.

b. Server trả về requestId, data là danh sách số nguyên và response header X-Checksum.

c. Đọc đúng giá trị header X-Checksum từ phase 1.

d. Gửi POST /api/rest/header/submit với body chứa studentCode, qCode, requestId và kèm lại header X-Checksum đã nhận.

e. Không cần tự tạo checksum mới; yêu cầu là truyền lại đúng giá trị server đã cấp.
*/
public class Bai8 {
    public static final String HTTP_SERVER = "http://36.50.135.242:2230";
    public static final String MSV = "B22DCCN393";
    public static final String QCODE = "8GrkJ5Q2";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // a. Gửi GET
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_SERVER + "/api/rest/header?studentCode=" + MSV + "&qCode=" + QCODE))
                .GET().build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        // c. Đọc giá trị header X-Checksum
        String checksum = getResponse.headers().firstValue("X-Checksum").orElse("");
        
        JsonNode node = mapper.readTree(getResponse.body());
        String requestId = node.get("requestId").asText();

        // Chuẩn bị payload nộp bài
        Map<String, Object> payload = Map.of(
                "studentCode", MSV,
                "qCode", QCODE,
                "requestId", requestId
        );

        // d. Gửi POST kèm theo header X-Checksum đã nhận
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_SERVER + "/api/rest/header/submit"))
                .header("Content-Type", "application/json")
                .header("X-Checksum", checksum)
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(postResponse.body());
    }
}
