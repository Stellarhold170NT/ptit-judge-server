package vn.ptit.judge.client.grpc;

import java.util.Arrays;
import GRPC.JudgeRequest;
import GRPC.JudgeResponse;
import GRPC.JudgeServiceGrpc;
import GRPC.SubmitRequest;
import GRPC.SubmitResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Bai5Sort {
    private static final String HOST = "localhost";
    private static final int PORT = 2240;
    private static final String STUDENT_CODE = "B21DCCN001";
    private static final String Q_CODE = "0VdN4dph";

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

        String[] words = data.split(",");
        Arrays.sort(words, String.CASE_INSENSITIVE_ORDER);
        String answer = String.join(",", words);

        SubmitRequest submit = SubmitRequest.newBuilder()
                .setStudentCode(STUDENT_CODE)
                .setQuestionAlias(Q_CODE)
                .setRequestId(requestId)
                .setAnswer(answer)
                .build();
        SubmitResponse submitResponse = stub.submit(submit);
        System.out.println("Bai5Sort Result: " + submitResponse.getStatus());

        channel.shutdown();
    }
}
