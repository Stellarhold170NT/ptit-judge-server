package vn.ptit.judge.grpc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import GRPC.JudgeRequest;
import GRPC.JudgeResponse;
import GRPC.JudgeServiceGrpc;
import GRPC.SubmitRequest;
import GRPC.SubmitResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import vn.ptit.judge.repository.SessionRepository;
import vn.ptit.judge.repository.UserRepository;

import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = "grpc.server.port=12240")
class JudgeServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SessionRepository sessionRepository;

    private static ManagedChannel channel;
    private static JudgeServiceGrpc.JudgeServiceBlockingStub stub;

    @BeforeAll
    static void setUp() {
        channel = ManagedChannelBuilder.forAddress("localhost", 12240)
                .usePlaintext()
                .build();
        stub = JudgeServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    static void tearDown() {
        if (channel != null) {
            channel.shutdown();
        }
    }

    @Test
    void request_ReturnsNonEmptyRequestIdAndData() {
        JudgeRequest request = JudgeRequest.newBuilder()
                .setStudentCode("B21DCCN001")
                .setQuestionAlias("kpFm4QvE")
                .build();

        JudgeResponse response = stub.request(request);

        assertNotNull(response);
        assertFalse(response.getRequestId().isEmpty(), "request_id should not be empty");
        assertFalse(response.getData().isEmpty(), "data should not be empty");
    }

    @Test
    void submit_WithCorrectAnswer_ReturnsAC() {
        JudgeRequest req = JudgeRequest.newBuilder()
                .setStudentCode("B21DCCN002")
                .setQuestionAlias("kpFm4QvE")
                .build();
        JudgeResponse judgeResp = stub.request(req);

        String data = judgeResp.getData();
        String[] parts = data.split(",");
        int sum = 0;
        for (String part : parts) {
            sum += Integer.parseInt(part.trim());
        }
        String correctAnswer = String.valueOf(sum);

        SubmitRequest subReq = SubmitRequest.newBuilder()
                .setStudentCode("B21DCCN002")
                .setQuestionAlias("kpFm4QvE")
                .setRequestId(judgeResp.getRequestId())
                .setAnswer(correctAnswer)
                .build();
        SubmitResponse subResp = stub.submit(subReq);

        assertEquals("AC", subResp.getStatus(),
                "Expected AC but got " + subResp.getStatus() + ": " + subResp.getMessage());
    }

    @Test
    void submit_WithWrongAnswer_ReturnsWA() {
        JudgeRequest req = JudgeRequest.newBuilder()
                .setStudentCode("B21DCCN003")
                .setQuestionAlias("kpFm4QvE")
                .build();
        JudgeResponse judgeResp = stub.request(req);

        SubmitRequest subReq = SubmitRequest.newBuilder()
                .setStudentCode("B21DCCN003")
                .setQuestionAlias("kpFm4QvE")
                .setRequestId(judgeResp.getRequestId())
                .setAnswer("-99999")
                .build();
        SubmitResponse subResp = stub.submit(subReq);

        assertEquals("WA", subResp.getStatus());
    }
}
