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
import vn.ptit.judge.dto.rest.CharacterSubmitRequest;
import vn.ptit.judge.service.ScoringService;

@WebMvcTest(CharacterController.class)
public class CharacterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScoringService scoringService;

    @Test
    void getData_Returns200WithRequestIdAndData() throws Exception {
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("requestId", "test-request-id-456");
        mockResult.put("data", "apple banana cherry date elder");

        when(scoringService.generateExerciseData("oPW4mpqm")).thenReturn(mockResult);

        mockMvc.perform(get("/api/rest/character")
                        .param("studentCode", "B21DCCN001")
                        .param("qCode", "oPW4mpqm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value("test-request-id-456"))
                .andExpect(jsonPath("$.data").value("apple banana cherry date elder"));
    }

    @Test
    void submit_WithCorrectAnswer_ReturnsAC() throws Exception {
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("requestId", "req-char-123");
        dataResult.put("data", "apple banana cherry date elder");
        when(scoringService.generateExerciseData("oPW4mpqm")).thenReturn(dataResult);

        when(scoringService.scoreSubmission(eq("oPW4mpqm"), eq("req-char-123"), eq("apple banana cherry date elder")))
                .thenReturn(new ScoringResult("AC", "Accepted", "apple banana cherry date elder"));

        CharacterSubmitRequest request = new CharacterSubmitRequest();
        request.setStudentCode("B21DCCN001");
        request.setQCode("oPW4mpqm");
        request.setRequestId("req-char-123");
        request.setAnswer("apple banana cherry date elder");

        mockMvc.perform(post("/api/rest/character/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AC"));
    }

    @Test
    void submit_WithWrongAnswer_ReturnsWA() throws Exception {
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("requestId", "req-char-456");
        dataResult.put("data", "apple banana cherry date elder");
        when(scoringService.generateExerciseData("oPW4mpqm")).thenReturn(dataResult);

        when(scoringService.scoreSubmission(eq("oPW4mpqm"), eq("req-char-456"), eq("wrong answer")))
                .thenReturn(new ScoringResult("WA", "Wrong Answer", "apple banana cherry date elder"));

        CharacterSubmitRequest request = new CharacterSubmitRequest();
        request.setStudentCode("B21DCCN001");
        request.setQCode("oPW4mpqm");
        request.setRequestId("req-char-456");
        request.setAnswer("wrong answer");

        mockMvc.perform(post("/api/rest/character/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("WA"));
    }
}