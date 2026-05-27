package vn.ptit.judge.client.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

/*
Một dịch vụ REST MethodService được triển khai trên server tại URL http://<Exam_IP>:2230/api/rest/method để kiểm tra cách sử dụng HTTP method trong quy trình submit.

Yêu cầu: Viết chương trình tại máy trạm (REST client) để giao tiếp với MethodService và thực hiện các công việc sau.

a. Gửi GET /api/rest/method?studentCode=<mã_sinh_viên>&qCode=<qAlias> để nhận task cần cập nhật. qCode là alias runtime được giao.

b. Server trả về requestId và data gồm id, title, status.

c. Gửi phase 2 bằng đúng phương thức PUT tới /api/rest/method/{requestId}.

d. Body JSON chứa studentCode, qCode và answer có status bằng done.

e. Dùng phương thức khác như PATCH không được chấp nhận cho bài này.
*/
public class Bai7 {
    public static final String HTTP_SERVER = "http://36.50.135.242:2230";
    public static final String MSV = "B22DCCN393";
    public static final String QCODE = "gvaE5vXi";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // a. Gửi GET nhận dữ liệu
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_SERVER + "/api/rest/method?studentCode=" + MSV + "&qCode=" + QCODE))
                .GET().build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        
        JsonNode node = mapper.readTree(getResponse.body());
        String requestId = node.get("requestId").asText();
        JsonNode data = node.get("data");

        // Chuẩn bị dữ liệu submit
        Map<String, Object> answer = Map.of(
                "id", data.get("id").asInt(),
                "title", data.get("title").asText(),
                "status", "done"
        );
        Map<String, Object> payload = Map.of(
                "studentCode", MSV,
                "qCode", QCODE,
                "answer", answer
        );

        // c. Gửi phase 2 bằng PUT tới /api/rest/method/{requestId}
        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_SERVER + "/api/rest/method/" + requestId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                .build();
        
        HttpResponse<String> putResponse = client.send(putRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(putResponse.body());
    }
}
