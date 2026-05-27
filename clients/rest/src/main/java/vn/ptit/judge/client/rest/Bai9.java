package vn.ptit.judge.client.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/*
Một dịch vụ REST PathService được triển khai trên server tại URL http://<Exam_IP>:2230/api/rest/path để kiểm tra cách sử dụng path parameter và query parameter.

Yêu cầu: Viết chương trình tại máy trạm (REST client) để giao tiếp với PathService và thực hiện các công việc sau.

a. Gửi GET /api/rest/path?studentCode=<mã_sinh_viên>&qCode=<qAlias>. qCode là alias runtime được giao.

b. Server trả về requestId và data là danh sách sản phẩm, mỗi sản phẩm có id, name, priceVND.

c. Chọn một id có trong danh sách phase 1.

d. Gửi GET /api/rest/path/{productId}?studentCode=<mã_sinh_viên>&qCode=<qAlias>&requestId=<requestId>&currency=USD.

e. productId phải hợp lệ và query currency phải bằng USD.
*/
public class Bai9 {
    public static final String HTTP_SERVER = "http://36.50.135.242:2230";
    public static final String MSV = "B22DCCN393";
    public static final String QCODE = "YOUR_QCODE_HERE"; // Thay thế bằng mã câu hỏi của bạn

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // a. Gửi GET nhận danh sách sản phẩm
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_SERVER + "/api/rest/path?studentCode=" + MSV + "&qCode=" + QCODE))
                .GET().build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        JsonNode node = mapper.readTree(getResponse.body());
        String requestId = node.get("requestId").asText();
        JsonNode data = node.get("data");

        // c. Chọn một id từ danh sách (ví dụ sản phẩm đầu tiên)
        String productId = data.get(0).get("id").asText();

        // d. Gửi GET /api/rest/path/{productId} kèm các query parameter
        String url = String.format("%s/api/rest/path/%s?studentCode=%s&qCode=%s&requestId=%s&currency=USD",
                HTTP_SERVER, productId, MSV, QCODE, requestId);

        HttpRequest finalGetRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET().build();

        HttpResponse<String> finalResponse = client.send(finalGetRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(finalResponse.body());
    }
}
