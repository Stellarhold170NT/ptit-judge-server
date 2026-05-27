package vn.ptit.judge.client.grpc;

import GRPC.*;
import io.grpc.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Bai7TypedJudgeService {
    public static final String SERVER = "36.50.135.242";
    public static final String MSV = "B22DCCN393";
    public static final String QCODE = "XHdi6kaH";

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER,2240)
                .usePlaintext().build();

        TypedJudgeServiceGrpc.TypedJudgeServiceBlockingStub stub = TypedJudgeServiceGrpc.newBlockingStub(channel);

        TypedJudgeRequest typedJudgeRequest = TypedJudgeRequest.newBuilder()
                .setStudentCode(MSV)
                .setQuestionAlias(QCODE)
                .build();
        
        TypedJudgeResponse typedJudgeResponse = stub.requestTyped(typedJudgeRequest);
        String requestId = typedJudgeResponse.getRequestId();

        List<TransactionRecord> transactions = typedJudgeResponse.getTransactionRiskBatch().getTransactionsList();

        List<String> highRiskIds = new ArrayList<>();
        BigDecimal totalHightRiskAmount = BigDecimal.ZERO;

        for (TransactionRecord tx : transactions) {
            boolean needReview = false;
            if (tx.getAmount() >= 5000.0) {
                needReview = true;
            }
            else if (tx.getChargebackCount() >= 2) {
                needReview = true;
            }
            else if (tx.getNewDevice() && !tx.getCountry().equalsIgnoreCase("VN")) {
                needReview = true;
            }

            if (needReview) {
                highRiskIds.add(tx.getTransactionId());
                totalHightRiskAmount = totalHightRiskAmount.add(BigDecimal.valueOf(tx.getAmount()));
            }


        }

        totalHightRiskAmount = totalHightRiskAmount.setScale(2, RoundingMode.HALF_UP);

        TransactionRiskAnswer answer = TransactionRiskAnswer.newBuilder().addAllHighRiskTransactionIds(highRiskIds)
                .setReviewCount(highRiskIds.size())
                .setTotalHighRiskAmount(totalHightRiskAmount.doubleValue())
                .build();

        TypedSubmitRequest submitRequest = TypedSubmitRequest.newBuilder()
                .setStudentCode(MSV)
                .setQuestionAlias(QCODE)
                .setRequestId(requestId)
                .setTransactionRiskAnswer(answer)
                .build();

        TypedSubmitResponse response = stub.submitTyped(submitRequest);
        System.out.println(response);

    }

}
