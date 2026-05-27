package vn.ptit.judge.client.grpc;

import GRPC.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Bai11 {
    public static final String SERVER = "36.50.135.242";
    public static final String MSV = "B22DCCN393";
    public static final String QCODE = "YgNJXhFD"; // Mã câu hỏi được giao

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

        // b. Nhận dữ liệu báo giá vận chuyển
        ShippingQuoteData shippingQuote = typedJudgeResponse.getShippingQuote();
        double weightKg = shippingQuote.getWeightKg();
        int maxEtaDays = shippingQuote.getMaxEtaDays();
        List<CarrierQuote> quotes = shippingQuote.getQuotesList();

        CarrierQuote bestQuote = null;
        double bestTotalFee = Double.MAX_VALUE;
        double bestReliability = -1.0;

        for (CarrierQuote quote : quotes) {
            // c. Chỉ xét quote có eta_days <= max_eta_days
            if (quote.getEtaDays() <= maxEtaDays) {
                // Tính total_fee = base_fee + weight_kg * per_kg_fee
                double rawFee = quote.getBaseFee() + weightKg * quote.getPerKgFee();
                // Làm tròn 2 chữ số thập phân
                double roundedFee = BigDecimal.valueOf(rawFee).setScale(2, RoundingMode.HALF_UP).doubleValue();

                // d. Chọn quote có total_fee nhỏ nhất; nếu bằng nhau, chọn reliability cao hơn
                if (bestQuote == null || roundedFee < bestTotalFee) {
                    bestQuote = quote;
                    bestTotalFee = roundedFee;
                    bestReliability = quote.getReliability();
                } else if (roundedFee == bestTotalFee) {
                    if (quote.getReliability() > bestReliability) {
                        bestQuote = quote;
                        bestTotalFee = roundedFee;
                        bestReliability = quote.getReliability();
                    }
                }
            }
        }

        // Tạo câu trả lời ShippingQuoteAnswer
        ShippingQuoteAnswer answer = ShippingQuoteAnswer.newBuilder()
                .setCarrier(bestQuote.getCarrier())
                .setTotalFee(bestTotalFee)
                .setEtaDays(bestQuote.getEtaDays())
                .build();

        // e. Gọi TypedJudgeService.SubmitTyped để nộp kết quả
        TypedSubmitRequest submitRequest = TypedSubmitRequest.newBuilder()
                .setStudentCode(MSV)
                .setQuestionAlias(QCODE)
                .setRequestId(requestId)
                .setShippingQuoteAnswer(answer)
                .build();

        TypedSubmitResponse response = stub.submitTyped(submitRequest);
        System.out.println(response);

        channel.shutdown();
    }
}
