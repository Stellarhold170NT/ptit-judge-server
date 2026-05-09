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

import vn.ptit.judge.dto.ScoringResult;
import vn.ptit.judge.dto.rest.CharacterResponse;
import vn.ptit.judge.dto.rest.CharacterSubmitRequest;
import vn.ptit.judge.dto.rest.StatusResponse;
import vn.ptit.judge.service.ScoringService;

@RestController
@RequestMapping("/api/rest/character")
public class CharacterController {

    private static final String Q_CODE = "oPW4mpqm";

    @Autowired
    private ScoringService scoringService;

    @GetMapping
    public ResponseEntity<CharacterResponse> getData(@RequestParam String studentCode, @RequestParam String qCode) {
        Map<String, Object> result = scoringService.generateExerciseData(Q_CODE);

        CharacterResponse response = new CharacterResponse();
        response.setRequestId((String) result.get("requestId"));
        response.setData((String) result.get("data"));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/submit")
    public ResponseEntity<StatusResponse> submit(@RequestBody CharacterSubmitRequest request) {
        ScoringResult scoringResult = scoringService.scoreSubmission(Q_CODE, request.getRequestId(), request.getAnswer());

        StatusResponse response = new StatusResponse();
        response.setStatus(scoringResult.getStatus());

        return ResponseEntity.ok(response);
    }
}