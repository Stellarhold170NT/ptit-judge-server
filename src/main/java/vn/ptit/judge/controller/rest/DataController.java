package vn.ptit.judge.controller.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.ptit.judge.dto.rest.DataResponse;
import vn.ptit.judge.dto.rest.DataSubmitRequest;
import vn.ptit.judge.dto.rest.StatusResponse;
import vn.ptit.judge.service.ScoringService;

@RestController
@RequestMapping("/api/rest/data")
public class DataController {

    private static final String Q_CODE = "4siaIVgn";

    @Autowired
    private ScoringService scoringService;

    @GetMapping
    public ResponseEntity<DataResponse> getData(@RequestParam String studentCode, @RequestParam String qCode) {
        Map<String, Object> result = scoringService.generateExerciseData(Q_CODE);

        DataResponse response = new DataResponse();
        response.setRequestId((String) result.get("requestId"));
        response.setData((java.util.List<Integer>) result.get("data"));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/submit")
    public ResponseEntity<StatusResponse> submit(@RequestBody DataSubmitRequest request) {
        String submittedAnswer = String.valueOf(request.getAnswer());
        ScoringService scoringService = this.scoringService;
        vn.ptit.judge.dto.ScoringResult scoringResult = scoringService.scoreSubmission(Q_CODE, request.getRequestId(), submittedAnswer);

        StatusResponse response = new StatusResponse();
        response.setStatus(scoringResult.getStatus());

        return ResponseEntity.ok(response);
    }
}