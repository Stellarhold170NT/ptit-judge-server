package vn.ptit.judge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.ptit.judge.dto.ScoringResult;
import vn.ptit.judge.model.ExerciseData;
import vn.ptit.judge.model.ProductY;

@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {

    @Mock
    private DataGenerationService dataGenerationService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private ScoringService scoringService;

    @BeforeEach
    void setUp() {
        scoringService = new ScoringService(dataGenerationService, objectMapper);
    }

    private void seedData(String requestId, String qCode, Object data) {
        ExerciseData ed = new ExerciseData(requestId, qCode, data, LocalDateTime.now());
        ConcurrentHashMap<String, ExerciseData> store = new ConcurrentHashMap<>();
        store.put(requestId, ed);
        ReflectionTestUtils.setField(scoringService, "exerciseDataStore", store);
    }

    @Test
    void computeCorrectAnswer_4siaIVgn_Sum() {
        List<Integer> data = List.of(1, 2, 3, 4, 5);
        String result = scoringService.computeCorrectAnswer("4siaIVgn", data);
        assertEquals("15", result);
    }

    @Test
    void computeCorrectAnswer_oPW4mpqm_SortCaseSensitive() {
        String data = "banana apple Cherry";
        String result = scoringService.computeCorrectAnswer("oPW4mpqm", data);
        assertEquals("Cherry apple banana", result);
    }

    @Test
    void computeCorrectAnswer_2ucfcULf_FinalPrice() {
        ProductY product = new ProductY("Test", 100.0, 10.0, 5.0, 0.0);
        String result = scoringService.computeCorrectAnswer("2ucfcULf", product);
        assertEquals("104.50", result);
    }

    @Test
    void computeCorrectAnswer_kpFm4QvE_CsvSum() {
        String data = "1,2,3,4,5";
        String result = scoringService.computeCorrectAnswer("kpFm4QvE", data);
        assertEquals("15", result);
    }

    @Test
    void computeCorrectAnswer_0VdN4dph_SortCaseInsensitive() {
        String data = "Banana,apple,Cherry";
        String result = scoringService.computeCorrectAnswer("0VdN4dph", data);
        assertEquals("apple,Banana,Cherry", result);
    }

    @Test
    void computeCorrectAnswer_1zog4SNL_JsonFinalPrice() {
        String data = "{\"name\":\"Test\",\"price\":100,\"taxRate\":10,\"discount\":5}";
        String result = scoringService.computeCorrectAnswer("1zog4SNL", data);
        assertEquals("105.00", result);
    }

    @Test
    void computeCorrectAnswer_yyhHZUpt_Sum() {
        List<Integer> data = List.of(10, 20, 30);
        String result = scoringService.computeCorrectAnswer("yyhHZUpt", data);
        assertEquals("60", result);
    }

    @Test
    void computeCorrectAnswer_qvCGMEhW_Reverse() {
        String data = "hello";
        String result = scoringService.computeCorrectAnswer("qvCGMEhW", data);
        assertEquals("olleh", result);
    }

    @Test
    void computeCorrectAnswer_7u07dNqg_FinalPrice() {
        ProductY product = new ProductY("Test", 100.0, 10.0, 5.0, 0.0);
        String result = scoringService.computeCorrectAnswer("7u07dNqg", product);
        assertEquals("104.50", result);
    }

    @Test
    void scoreSubmission_AcceptsCorrectAnswer() {
        String requestId = "test-req-001";
        String qCode = "4siaIVgn";
        List<Integer> data = List.of(1, 2, 3, 4, 5);
        seedData(requestId, qCode, data);

        ScoringResult result = scoringService.scoreSubmission(qCode, requestId, "15");

        assertNotNull(result);
        assertEquals("AC", result.getStatus());
        assertEquals("15", result.getCorrectAnswer());
    }

    @Test
    void scoreSubmission_RejectsWrongAnswer() {
        String requestId = "test-req-002";
        String qCode = "qvCGMEhW";
        String data = "hello";
        seedData(requestId, qCode, data);

        ScoringResult result = scoringService.scoreSubmission(qCode, requestId, "wrong");

        assertNotNull(result);
        assertEquals("WA", result.getStatus());
        assertEquals("olleh", result.getCorrectAnswer());
    }

    @Test
    void scoreSubmission_RejectsInvalidRequestId() {
        ScoringResult result = scoringService.scoreSubmission("4siaIVgn", "nonexistent", "42");

        assertNotNull(result);
        assertEquals("RTE", result.getStatus());
    }

    @Test
    void scoreSubmission_ProductDoubleTolerance() {
        String requestId = "test-req-003";
        String qCode = "2ucfcULf";
        ProductY product = new ProductY("Test", 100.0, 10.0, 5.0, 0.0);
        seedData(requestId, qCode, product);

        ScoringResult result = scoringService.scoreSubmission(qCode, requestId, "104.50");
        assertEquals("AC", result.getStatus());

        ScoringResult closeResult = scoringService.scoreSubmission(qCode, requestId, "104.5");
        assertEquals("AC", closeResult.getStatus());
    }

    @Test
    void generateExerciseData_ReturnsRequestIdAndData() {
        when(dataGenerationService.generateIntegers(5, 1, 100))
                .thenReturn(List.of(3, 7, 2, 9, 1));

        var result = scoringService.generateExerciseData("4siaIVgn");

        assertNotNull(result.get("requestId"));
        assertEquals(List.of(3, 7, 2, 9, 1), result.get("data"));
    }
}
