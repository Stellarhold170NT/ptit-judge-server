package vn.ptit.judge.client.grpc;

import GRPC.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Bai12 {
    public static final String SERVER = "36.50.135.242";
    public static final String MSV = "B22DCCN393";
    public static final String QCODE = "p3WiRr4z"; // Mã câu hỏi được giao

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

        // b. Nhận dữ liệu xét tuyển
        EnrollmentData enrollment = typedJudgeResponse.getEnrollment();
        List<String> completedCourses = enrollment.getCompletedCoursesList();
        List<String> requiredCourses = enrollment.getRequiredCoursesList();
        double gpa = enrollment.getGpa();
        double minGpa = enrollment.getMinGpa();

        // c. Tính danh sách missing_courses là các môn bắt buộc chưa hoàn thành
        Set<String> completedSet = new HashSet<>(completedCourses);
        List<String> missingCourses = new ArrayList<>();
        for (String course : requiredCourses) {
            if (!completedSet.contains(course)) {
                missingCourses.add(course);
            }
        }
        // Sắp xếp tăng dần theo mã môn
        Collections.sort(missingCourses);

        // d. Tính gpa_gap = max(0, min_gpa - gpa), làm tròn 2 chữ số thập phân
        double rawGap = Math.max(0.0, minGpa - gpa);
        double gpaGap = BigDecimal.valueOf(rawGap).setScale(2, RoundingMode.HALF_UP).doubleValue();

        // eligible đúng khi không thiếu môn và gpa_gap bằng 0
        boolean eligible = missingCourses.isEmpty() && (gpaGap == 0.0);

        // Tạo câu trả lời EnrollmentAnswer
        EnrollmentAnswer answer = EnrollmentAnswer.newBuilder()
                .setEligible(eligible)
                .addAllMissingCourses(missingCourses)
                .setGpaGap(gpaGap)
                .build();

        // e. Gọi TypedJudgeService.SubmitTyped để nộp kết quả
        TypedSubmitRequest submitRequest = TypedSubmitRequest.newBuilder()
                .setStudentCode(MSV)
                .setQuestionAlias(QCODE)
                .setRequestId(requestId)
                .setEnrollmentAnswer(answer)
                .build();

        TypedSubmitResponse response = stub.submitTyped(submitRequest);
        System.out.println(response);

        channel.shutdown();
    }
}
