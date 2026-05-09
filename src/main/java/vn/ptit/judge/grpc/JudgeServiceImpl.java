package vn.ptit.judge.grpc;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import GRPC.JudgeRequest;
import GRPC.JudgeResponse;
import GRPC.JudgeServiceGrpc;
import GRPC.SubmitRequest;
import GRPC.SubmitResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import vn.ptit.judge.dto.ScoringResult;
import vn.ptit.judge.service.ScoringService;

@GrpcService
public class JudgeServiceImpl extends JudgeServiceGrpc.JudgeServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(JudgeServiceImpl.class);

    @Autowired
    private ScoringService scoringService;

    @Override
    public void request(JudgeRequest request, StreamObserver<JudgeResponse> responseObserver) {
        String studentCode = request.getStudentCode();
        String questionAlias = request.getQuestionAlias();

        log.info("gRPC Request: studentCode={}, questionAlias={}", studentCode, questionAlias);

        Map<String, Object> exerciseData = scoringService.generateExerciseData(questionAlias);

        String requestId = (String) exerciseData.get("requestId");
        Object data = exerciseData.get("data");

        JudgeResponse response = JudgeResponse.newBuilder()
                .setRequestId(requestId)
                .setData(data.toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void submit(SubmitRequest request, StreamObserver<SubmitResponse> responseObserver) {
        String studentCode = request.getStudentCode();
        String questionAlias = request.getQuestionAlias();
        String requestId = request.getRequestId();
        String answer = request.getAnswer();

        log.info("gRPC Submit: studentCode={}, questionAlias={}, requestId={}, answer={}",
                studentCode, questionAlias, requestId, answer);

        ScoringResult result = scoringService.scoreSubmission(questionAlias, requestId, answer);

        SubmitResponse response = SubmitResponse.newBuilder()
                .setStatus(result.getStatus())
                .setMessage(result.getMessage())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
