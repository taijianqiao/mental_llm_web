package com.spongebob.magic_conch_backend.controller;

import com.spongebob.magic_conch_backend.service.SelfEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/self-evaluation")
public class SelfEvaluationController {

    @Autowired
    private SelfEvaluationService selfEvaluationService;

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeEvaluation(@RequestBody Map<String, List<Map<String, Object>>> request) {
        List<Map<String, Object>> questions = request.get("questions");
        String analysis = selfEvaluationService.analyzeEvaluation(questions);
        return ResponseEntity.ok(analysis);
    }
} 