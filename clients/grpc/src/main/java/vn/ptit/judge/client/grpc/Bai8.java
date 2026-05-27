
import GRPC.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Bai8 {
    public static final String SERVER = "36.50.135.242";
    public static final String MSV = "B22DCCN393";
    public static final String QCODE = "qM9kCK2a";

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER, 2240)
                .usePlaintext().build();

        TypedJudgeServiceGrpc.TypedJudgeServiceBlockingStub stub = TypedJudgeServiceGrpc.newBlockingStub(channel);

        TypedJudgeRequest request = TypedJudgeRequest.newBuilder()
                .setStudentCode(MSV)
                .setQuestionAlias(QCODE)
                .build();

        TypedJudgeResponse typedJudgeResponse = stub.requestTyped(request);
        String requestId = typedJudgeResponse.getRequestId();

        SensorTelemetryData telemetryData = typedJudgeResponse.getSensorTelemetry();
        List<SensorReading> readings = telemetryData.getReadingsList();

        double threshold = telemetryData.getThreshold();

        int n = readings.size();
        if (n == 0) {
            return;
        }

        double sum = 0;
        List<Double> values = new ArrayList<>();
        int anomalyCount = 0;

        for (SensorReading reading : readings) {
            double val = reading.getValue();
            sum += val;
            values.add(val);
            if (val > threshold) {
                anomalyCount++;
            }
        }

        double average = sum / n;

        Collections.sort(values);
        int p95Index = (int) Math.ceil(n * 0.95) - 1;
        double p95 = values.get(p95Index);

        BigDecimal avgBd = BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP);
        BigDecimal p95Bd = BigDecimal.valueOf(p95).setScale(2, RoundingMode.HALF_UP);

        SensorTelemetryAnswer answer = SensorTelemetryAnswer.newBuilder()
                .setAverage(avgBd.doubleValue())
                .setP95(p95Bd.doubleValue())
                .setAnomalyCount(anomalyCount)
                .build();

        TypedSubmitRequest submitRequest = TypedSubmitRequest.newBuilder()
                .setStudentCode(MSV)
                .setQuestionAlias(QCODE)
                .setRequestId(requestId)
                .setSensorTelemetryAnswer(answer)
                .build();

        TypedSubmitResponse response = stub.submitTyped(submitRequest);
        System.out.println(response);




    }
}
