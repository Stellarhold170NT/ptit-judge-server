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

import vn.ptit.judge.dto.rest.ObjectResponse;
import vn.ptit.judge.dto.rest.ObjectSubmitRequest;
import vn.ptit.judge.dto.rest.StatusResponse;
import vn.ptit.judge.model.ProductY;
import vn.ptit.judge.service.ScoringService;

@RestController
@RequestMapping("/api/rest/object")
public class ObjectController {

    private static final String Q_CODE = "2ucfcULf";

    @Autowired
    private ScoringService scoringService;

    @GetMapping
    public ResponseEntity<ObjectResponse> getData(@RequestParam String studentCode, @RequestParam String qCode) {
        Map<String, Object> result = scoringService.generateExerciseData(Q_CODE);

        ObjectResponse response = new ObjectResponse();
        response.setRequestId((String) result.get("requestId"));
        response.setData((ProductY) result.get("data"));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/submit")
    public ResponseEntity<StatusResponse> submit(@RequestBody ObjectSubmitRequest request) {
        ProductY product = request.getAnswer();
        String submittedAnswer = String.format("%.2f", product.getFinalPrice());

        vn.ptit.judge.dto.ScoringResult scoringResult = scoringService.scoreSubmission(Q_CODE, request.getRequestId(), submittedAnswer);

        StatusResponse response = new StatusResponse();
        response.setStatus(scoringResult.getStatus());

        return ResponseEntity.ok(response);
    }
}