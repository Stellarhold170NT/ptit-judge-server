package vn.ptit.judge.controller.rest;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.ptit.judge.dto.ScoringResult;
import vn.ptit.judge.dto.rest.ObjectSubmitRequest;
import vn.ptit.judge.model.ProductY;
import vn.ptit.judge.service.ScoringService;

@WebMvcTest(ObjectController.class)
public class ObjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScoringService scoringService;

    @Test
    void getData_Returns200WithRequestIdAndData() throws Exception {
        ProductY product = new ProductY("Laptop", 1000.0, 10.0, 5.0, 1045.0);
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("requestId", "test-request-id-789");
        mockResult.put("data", product);

        when(scoringService.generateExerciseData("2ucfcULf")).thenReturn(mockResult);

        mockMvc.perform(get("/api/rest/object")
                        .param("studentCode", "B21DCCN001")
                        .param("qCode", "2ucfcULf"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value("test-request-id-789"))
                .andExpect(jsonPath("$.data.name").value("Laptop"))
                .andExpect(jsonPath("$.data.finalPrice").value(1045.0));
    }

    @Test
    void submit_WithCorrectAnswer_ReturnsAC() throws Exception {
        ProductY product = new ProductY("Laptop", 1000.0, 10.0, 5.0, 1045.0);
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("requestId", "req-obj-123");
        dataResult.put("data", product);
        when(scoringService.generateExerciseData("2ucfcULf")).thenReturn(dataResult);

        when(scoringService.scoreSubmission(eq("2ucfcULf"), eq("req-obj-123"), eq("1045.00")))
                .thenReturn(new ScoringResult("AC", "Accepted", "1045.00"));

        ObjectSubmitRequest request = new ObjectSubmitRequest();
        request.setStudentCode("B21DCCN001");
        request.setQCode("2ucfcULf");
        request.setRequestId("req-obj-123");

        ProductY answer = new ProductY();
        answer.setName("Laptop");
        answer.setPrice(1000.0);
        answer.setTaxRate(10.0);
        answer.setDiscount(5.0);
        answer.setFinalPrice(1045.0);
        request.setAnswer(answer);

        mockMvc.perform(post("/api/rest/object/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AC"));
    }

    @Test
    void submit_WithWrongAnswer_ReturnsWA() throws Exception {
        ProductY product = new ProductY("Laptop", 1000.0, 10.0, 5.0, 1045.0);
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("requestId", "req-obj-456");
        dataResult.put("data", product);
        when(scoringService.generateExerciseData("2ucfcULf")).thenReturn(dataResult);

        when(scoringService.scoreSubmission(eq("2ucfcULf"), eq("req-obj-456"), eq("999.00")))
                .thenReturn(new ScoringResult("WA", "Wrong Answer", "1045.00"));

        ObjectSubmitRequest request = new ObjectSubmitRequest();
        request.setStudentCode("B21DCCN001");
        request.setQCode("2ucfcULf");
        request.setRequestId("req-obj-456");

        ProductY answer = new ProductY();
        answer.setName("Laptop");
        answer.setPrice(1000.0);
        answer.setTaxRate(10.0);
        answer.setDiscount(5.0);
        answer.setFinalPrice(999.0);
        request.setAnswer(answer);

        mockMvc.perform(post("/api/rest/object/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("WA"));
    }
}