package vn.ptit.judge.client.grpc;

import GRPC.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bai10 {
    public static final String SERVER = "36.50.135.242";
    public static final String MSV = "B22DCCN393";
    public static final String QCODE = "8cCtxs19"; // Mã câu hỏi được giao

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

        // c. Đếm số dòng theo severity đầu dòng
        Map<String, Integer> countsMap = new HashMap<>();
        for (String entry : entries) {
            // Lấy từ đầu tiên trước khoảng trắng hoặc dấu hai chấm
            String severity = entry.trim().split("[\\s]+")[0].toUpperCase();
            countsMap.put(severity, countsMap.getOrDefault(severity, 0) + 1);
        }

        // d. Tìm mã lỗi đầu tiên xuất hiện trong danh sách theo mẫu code=....
        String firstErrorCode = "";
        Pattern pattern = Pattern.compile("code=([^\\s,;\\]}]+)");
        for (String entry : entries) {
            Matcher matcher = pattern.matcher(entry);
            if (matcher.find()) {
                firstErrorCode = matcher.group(1);
                break; // Lấy mã lỗi đầu tiên
            }
        }

        List<String> valuesList = new ArrayList<>();
        if (!firstErrorCode.isEmpty()) {
            valuesList.add(firstErrorCode);
        }

        // Tạo câu trả lời TextBatchAnswer
        TextBatchAnswer answer = TextBatchAnswer.newBuilder()
                .putAllCounts(countsMap)
                .addAllValues(valuesList)
                .build();

        // e. Gọi TypedJudgeService.SubmitTyped để nộp kết quả
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
