package vn.ptit.judge.controller.rest;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
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
import vn.ptit.judge.dto.rest.DataSubmitRequest;
import vn.ptit.judge.service.ScoringService;

@WebMvcTest(DataController.class)
public class DataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScoringService scoringService;

    @Test
    void getData_Returns200WithRequestIdAndData() throws Exception {
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("requestId", "test-request-id-123");
        mockResult.put("data", Arrays.asList(10, 20, 30, 40, 50));

        when(scoringService.generateExerciseData("4siaIVgn")).thenReturn(mockResult);

        mockMvc.perform(get("/api/rest/data")
                        .param("studentCode", "B21DCCN001")
                        .param("qCode", "4siaIVgn"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value("test-request-id-123"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void submit_WithCorrectAnswer_ReturnsAC() throws Exception {
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("requestId", "req-123");
        dataResult.put("data", Arrays.asList(1, 2, 3, 4, 5));
        when(scoringService.generateExerciseData("4siaIVgn")).thenReturn(dataResult);

        when(scoringService.scoreSubmission(eq("4siaIVgn"), eq("req-123"), eq("15")))
                .thenReturn(new ScoringResult("AC", "Accepted", "15"));

        DataSubmitRequest request = new DataSubmitRequest();
        request.setStudentCode("B21DCCN001");
        request.setQCode("4siaIVgn");
        request.setRequestId("req-123");
        request.setAnswer(15);

        mockMvc.perform(post("/api/rest/data/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AC"));
    }

    @Test
    void submit_WithWrongAnswer_ReturnsWA() throws Exception {
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("requestId", "req-456");
        dataResult.put("data", Arrays.asList(1, 2, 3, 4, 5));
        when(scoringService.generateExerciseData("4siaIVgn")).thenReturn(dataResult);

        when(scoringService.scoreSubmission(eq("4siaIVgn"), eq("req-456"), eq("999")))
                .thenReturn(new ScoringResult("WA", "Wrong Answer", "15"));

        DataSubmitRequest request = new DataSubmitRequest();
        request.setStudentCode("B21DCCN001");
        request.setQCode("4siaIVgn");
        request.setRequestId("req-456");
        request.setAnswer(999);

        mockMvc.perform(post("/api/rest/data/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("WA"));
    }
}