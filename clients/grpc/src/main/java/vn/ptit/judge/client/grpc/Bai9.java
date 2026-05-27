package vn.ptit.judge.client.grpc;

import GRPC.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bai9 {
    public static final String SERVER = "36.50.135.242";
    public static final String MSV = "B22DCCN393";
    public static final String QCODE = "X1J8fIej"; // Thay đổi QCODE tương ứng với đề bài của bạn

    public static void main(String[] args) {
        // a. Tạo gRPC client plaintext tới cổng 2240
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER, 2240)
                .usePlaintext()
                .build();

        TypedJudgeServiceGrpc.TypedJudgeServiceBlockingStub stub = TypedJudgeServiceGrpc.newBlockingStub(channel);

        // Gọi TypedJudgeService.RequestTyped
        TypedJudgeRequest typedJudgeRequest = TypedJudgeRequest.newBuilder()
                .setStudentCode(MSV)
                .setQuestionAlias(QCODE)
                .build();
        
        TypedJudgeResponse typedJudgeResponse = stub.requestTyped(typedJudgeRequest);
        String requestId = typedJudgeResponse.getRequestId();

        // b. Nhận text batch từ server
        TextBatchData textBatch = typedJudgeResponse.getTextBatch();
        List<String> entries = textBatch.getEntriesList();

        int accountCount = 0;
        int paymentCount = 0;
        int refundCount = 0;
        int shippingCount = 0;

        // c. Đếm số entry chứa từng nhãn (so khớp không phân biệt hoa thường)
        for (String entry : entries) {
            String lower = entry.toLowerCase();
            if (lower.contains("account")) {
                accountCount++;
            }
            if (lower.contains("payment")) {
                paymentCount++;
            }
            if (lower.contains("refund")) {
                refundCount++;
            }
            if (lower.contains("shipping")) {
                shippingCount++;
            }
        }

        // d & e. Tạo map counts và danh sách values, chỉ thêm các nhãn có số lần xuất hiện > 0
        Map<String, Integer> countsMap = new HashMap<>();
        List<String> valuesList = new ArrayList<>();

        if (accountCount > 0) {
            countsMap.put("account", accountCount);
            valuesList.add("account");
        }
        if (paymentCount > 0) {
            countsMap.put("payment", paymentCount);
            valuesList.add("payment");
        }
        if (refundCount > 0) {
            countsMap.put("refund", refundCount);
            valuesList.add("refund");
        }
        if (shippingCount > 0) {
            countsMap.put("shipping", shippingCount);
            valuesList.add("shipping");
        }

        // Đảm bảo danh sách values được sắp xếp tăng dần theo tên nhãn
        Collections.sort(valuesList);

        // Tạo câu trả lời TextBatchAnswer
        TextBatchAnswer answer = TextBatchAnswer.newBuilder()
                .putAllCounts(countsMap)
                .addAllValues(valuesList)
                .build();

        // Gọi TypedJudgeService.SubmitTyped để nộp kết quả
        TypedSubmitRequest submitRequest = TypedSubmitRequest.newBuilder()
                .setStudentCode(MSV)
                .setQuestionAlias(QCODE)
                .setRequestId(requestId)
                .setTextBatchAnswer(answer)
                .build();

        TypedSubmitResponse response = stub.submitTyped(submitRequest);
        System.out.println(response);

        channel.shutdown();
    }
}
