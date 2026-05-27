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

b. Server trả về requestId và data là danh sách khách hàng, mỗi phần tử có customerId, status, overdueAmount, page.

c. Chỉ xét khách hàng có status bằng OVERDUE, chọn khách hàng có overdueAmount lớn nhất.

d. Gửi GET /api/rest/path/{customerId}?studentCode=<mã_sinh_viên>&qCode=<qAlias>&requestId=<requestId>&status=OVERDUE&page=<page>.

e. customerId, status và page phải khớp khách hàng đã chọn.
*/
public class Bai13 {
    public static final String HTTP_SERVER = "http://36.50.135.242:2230";
    public static final String MSV = "B22DCCN393";
    public static final String QCODE = "TaQrfpNa";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // a. Gửi GET nhận danh sách khách hàng
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(HTTP_SERVER + "/api/rest/path?studentCode=" + MSV + "&qCode=" + QCODE))
                .GET().build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        JsonNode node = mapper.readTree(getResponse.body());
        String requestId = node.get("requestId").asText();
        JsonNode data = node.get("data");

        // c. Lọc các khách hàng có status là "OVERDUE", chọn người có overdueAmount lớn nhất
        JsonNode bestCustomer = null;
        double maxOverdue = -1.0;

        for (JsonNode customer : data) {
            String status = customer.get("status").asText();
            if ("OVERDUE".equals(status)) {
                double overdueAmount = customer.get("overdueAmount").asDouble();
                if (overdueAmount > maxOverdue) {
                    maxOverdue = overdueAmount;
                    bestCustomer = customer;
                }
            }
        }

        if (bestCustomer != null) {
            String customerId = bestCustomer.get("customerId").asText();
            int page = bestCustomer.get("page").asInt();

            // d. Gửi GET /api/rest/path/{customerId}
            String url = String.format("%s/api/rest/path/%s?studentCode=%s&qCode=%s&requestId=%s&status=OVERDUE&page=%d",
                    HTTP_SERVER, customerId, MSV, QCODE, requestId, page);

            HttpRequest finalGetRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET().build();

            HttpResponse<String> finalResponse = client.send(finalGetRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(finalResponse.body());
        } else {
            System.out.println("Không tìm thấy khách hàng OVERDUE nào.");
        }
    }
}
