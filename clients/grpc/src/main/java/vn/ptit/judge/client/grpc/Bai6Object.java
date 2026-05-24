package vn.ptit.judge.client.grpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import GRPC.JudgeRequest;
import GRPC.JudgeResponse;
import GRPC.JudgeServiceGrpc;
import GRPC.SubmitRequest;
import GRPC.SubmitResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Bai6Object {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2240;
    private static final String STUDENT_CODE = "B22DCCN393";
    private static final String Q_CODE = "1zog4SNL";

    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext()
                .build();
        JudgeServiceGrpc.JudgeServiceBlockingStub stub = JudgeServiceGrpc.newBlockingStub(channel);

        JudgeRequest request = JudgeRequest.newBuilder()
                .setStudentCode(STUDENT_CODE)
                .setQuestionAlias(Q_CODE)
                .build();
        JudgeResponse response = stub.request(request);
        String requestId = response.getRequestId();
        String data = response.getData();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(data);
        double price = node.get("price").asDouble();
        double taxRate = node.get("taxRate").asDouble();
        double discount = node.get("discount").asDouble();
        double finalPrice = price * (1 + taxRate / 100.0) - discount;
        String answer = String.format(java.util.Locale.US, "%.2f", finalPrice);

        SubmitRequest submit = SubmitRequest.newBuilder()
                .setStudentCode(STUDENT_CODE)
                .setQuestionAlias(Q_CODE)
                .setRequestId(requestId)
                .setAnswer(answer)
                .build();
        SubmitResponse submitResponse = stub.submit(submit);
        System.out.println("Bai6Object Result: " + submitResponse.getStatus());

        channel.shutdown();
    }
}
