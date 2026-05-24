package vn.ptit.judge.client.grpc;

import GRPC.JudgeRequest;
import GRPC.JudgeResponse;
import GRPC.JudgeServiceGrpc;
import GRPC.SubmitRequest;
import GRPC.SubmitResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Bai4Sum {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2240;
    private static final String STUDENT_CODE = "B22DCCN393";
    private static final String Q_CODE = "kpFm4QvE";

    public static void main(String[] args) {
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

        String[] parts = data.split(",");
        int sum = 0;
        for (String part : parts) {
            sum += Integer.parseInt(part.trim());
        }

        SubmitRequest submit = SubmitRequest.newBuilder()
                .setStudentCode(STUDENT_CODE)
                .setQuestionAlias(Q_CODE)
                .setRequestId(requestId)
                .setAnswer(String.valueOf(sum))
                .build();
        SubmitResponse submitResponse = stub.submit(submit);
        System.out.println("Bai4Sum Result: " + submitResponse.getStatus());

        channel.shutdown();
    }
}
