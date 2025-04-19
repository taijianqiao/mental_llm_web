package com.spongebob.magic_conch_backend.service;

import java.util.List;
import java.util.Map;

public interface SelfEvaluationService {
    String analyzeEvaluation(List<Map<String, Object>> questions);
} 