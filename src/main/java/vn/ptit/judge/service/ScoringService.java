package vn.ptit.judge.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.ptit.judge.dto.ScoringResult;
import vn.ptit.judge.model.ExerciseData;
import vn.ptit.judge.model.ProductY;
import vn.ptit.judge.model.Submission;
import vn.ptit.judge.repository.SubmissionRepository;
import vn.ptit.judge.util.AnswerComparator;

@Service
public class ScoringService {

    private final DataGenerationService dataGenerationService;
    private final ObjectMapper objectMapper;
    private SubmissionRepository submissionRepository;
    private final ConcurrentHashMap<String, ExerciseData> exerciseDataStore = new ConcurrentHashMap<>();

    public ScoringService(DataGenerationService dataGenerationService, ObjectMapper objectMapper) {
        this.dataGenerationService = dataGenerationService;
        this.objectMapper = objectMapper;
    }

    public void setSubmissionRepository(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public Map<String, Object> generateExerciseData(String qCode) {
        String requestId = UUID.randomUUID().toString();
        Object data;

        switch (qCode) {
            case "4siaIVgn":
                data = dataGenerationService.generateIntegers(5, 1, 100);
                break;
            case "oPW4mpqm":
                data = dataGenerationService.generateWords(5);
                break;
            case "2ucfcULf":
                data = dataGenerationService.generateProduct();
                break;
            case "kpFm4QvE":
                List<Integer> ints = dataGenerationService.generateIntegers(5, 1, 100);
                data = ints.stream().map(String::valueOf).collect(Collectors.joining(","));
                break;
            case "0VdN4dph":
                String spaceWords = dataGenerationService.generateWords(5);
                data = spaceWords.replace(" ", ",");
                break;
            case "1zog4SNL":
                data = dataGenerationService.generateProductGrpc();
                break;
            case "yyhHZUpt":
                data = dataGenerationService.generateIntegers(8, 1, 100);
                break;
            case "qvCGMEhW":
                data = dataGenerationService.generateString();
                break;
            case "7u07dNqg":
                data = dataGenerationService.generateProduct();
                break;
            default:
                throw new IllegalArgumentException("Unknown qCode: " + qCode);
        }

        ExerciseData exerciseData = new ExerciseData(requestId, qCode, data, LocalDateTime.now());
        exerciseDataStore.put(requestId, exerciseData);

        Map<String, Object> result = new HashMap<>();
        result.put("requestId", requestId);
        result.put("data", data);
        return result;
    }

    public ScoringResult scoreSubmission(String qCode, String requestId, String submittedAnswer) {
        ExerciseData exerciseData = exerciseDataStore.get(requestId);
        if (exerciseData == null) {
            return new ScoringResult("RTE", "Invalid requestId: exercise data not found", null);
        }

        String correctAnswer;
        try {
            correctAnswer = computeCorrectAnswer(qCode, exerciseData.getData());
        } catch (Exception e) {
            return new ScoringResult("RTE", "Error computing answer: " + e.getMessage(), null);
        }

        boolean isCorrect;
        switch (qCode) {
            case "4siaIVgn":
            case "yyhHZUpt":
            case "kpFm4QvE":
                try {
                    isCorrect = AnswerComparator.compareIntegers(
                            Integer.parseInt(correctAnswer.trim()),
                            Integer.parseInt(submittedAnswer.trim()));
                } catch (NumberFormatException e) {
                    isCorrect = false;
                }
                break;
            case "2ucfcULf":
            case "1zog4SNL":
            case "7u07dNqg":
                try {
                    isCorrect = AnswerComparator.compareDouble(
                            Double.parseDouble(correctAnswer.trim()),
                            Double.parseDouble(submittedAnswer.trim()));
                } catch (NumberFormatException e) {
                    isCorrect = false;
                }
                break;
            default:
                isCorrect = AnswerComparator.compareStrings(correctAnswer, submittedAnswer.trim());
                break;
        }

        String status = isCorrect ? "AC" : "WA";
        String message = isCorrect ? "Accepted" : "Wrong Answer";
        return new ScoringResult(status, message, correctAnswer);
    }

    public String computeCorrectAnswer(String qCode, Object data) {
        switch (qCode) {
            case "4siaIVgn":
            case "yyhHZUpt": {
                @SuppressWarnings("unchecked")
                List<Integer> intList = (List<Integer>) data;
                int sum = intList.stream().mapToInt(Integer::intValue).sum();
                return String.valueOf(sum);
            }
            case "oPW4mpqm": {
                String words = (String) data;
                String[] arr = words.split(" ");
                Arrays.sort(arr);
                return String.join(" ", arr);
            }
            case "2ucfcULf":
            case "7u07dNqg": {
                ProductY product = (ProductY) data;
                double fp = product.getPrice() * (1 + product.getTaxRate() / 100) * (1 - product.getDiscount() / 100);
                return String.format("%.2f", fp);
            }
            case "kpFm4QvE": {
                String csv = (String) data;
                String[] parts = csv.split(",");
                int total = 0;
                for (String part : parts) {
                    total += Integer.parseInt(part.trim());
                }
                return String.valueOf(total);
            }
            case "0VdN4dph": {
                String csv = (String) data;
                String[] arr = csv.split(",");
                Arrays.sort(arr, String.CASE_INSENSITIVE_ORDER);
                return String.join(",", arr);
            }
            case "1zog4SNL": {
                String json = (String) data;
                try {
                    JsonNode node = objectMapper.readTree(json);
                    double price = node.get("price").asDouble();
                    double taxRate = node.get("taxRate").asDouble();
                    double discount = node.get("discount").asDouble();
                    double fp = price * (1 + taxRate / 100) - discount;
                    return String.format("%.2f", fp);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to parse product JSON: " + e.getMessage(), e);
                }
            }
            case "qvCGMEhW": {
                String str = (String) data;
                return new StringBuilder(str).reverse().toString();
            }
            default:
                throw new IllegalArgumentException("Unknown qCode: " + qCode);
        }
    }

    public Submission recordSubmission(String studentCode, String qCode, String requestId,
                                        String submittedAnswer, String correctAnswer, String status) {
        Submission submission = new Submission();
        submission.setId(UUID.randomUUID().toString());
        submission.setStudentCode(studentCode);
        submission.setQCode(qCode);
        submission.setRequestId(requestId);
        submission.setAnswer(submittedAnswer);
        submission.setCorrectAnswer(correctAnswer);
        submission.setStatus(status);
        submission.setTimestamp(LocalDateTime.now());

        if (submissionRepository != null) {
            submissionRepository.save(submission);
        }
        return submission;
    }
}
