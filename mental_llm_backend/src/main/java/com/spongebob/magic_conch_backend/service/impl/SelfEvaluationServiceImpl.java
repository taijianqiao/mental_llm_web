package com.spongebob.magic_conch_backend.service.impl;

import com.spongebob.magic_conch_backend.service.SelfEvaluationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SelfEvaluationServiceImpl implements SelfEvaluationService {

    @Value("${openrouter.api.key}")
    private String openRouterApiKey;

    private final RestTemplate restTemplate;

    public SelfEvaluationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String analyzeEvaluation(List<Map<String, Object>> questions) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openRouterApiKey);
        headers.set("HTTP-Referer", "http://localhost:5173");
        headers.set("X-Title", "Magic Conch");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek/deepseek-chat-v3-0324:free");
        
        List<Map<String, String>> messages = List.of(
            Map.of(
                "role", "system",
                "content", "你是一位专业的心理健康顾问。请基于用户提供的 GHQ - 12 问卷结果进行分析。问卷共12题，本量表的计分是记录1，2，3，4分。在正常人群中平均得分为23.62±7.92，总分范围12 - 48分，超过27分可能表示心理状况欠佳。请根据用户提供的具体答案（包含问题类型、选项和分数）给出综合评价，指出可能的积极方面和需要关注的方面，并提供一些具体的、可操作的改善建议。请注意保持专业、empathic和鼓励的语气。"
            ),
            Map.of(
                "role", "user",
                "content", "以下是我的心理健康自评问卷 (GHQ - 12) 结果，请帮我分析：\n\n" + questions + "\n\n请基于这些结果，给我一份详细的分析报告，包括总体评价、积极点、关注点和改善建议。"
            )
        );
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        
        Map<String, Object> response = restTemplate.postForObject(
            "https://openrouter.ai/api/v1/chat/completions",
            request,
            Map.class
        );

        if (response != null && response.containsKey("choices")) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (!choices.isEmpty()) {
                Map<String, Object> choice = choices.get(0);
                Map<String, Object> message = (Map<String, Object>) choice.get("message");
                return (String) message.get("content");
            }
        }
        
        return "抱歉，分析过程中出现错误，请稍后重试。";
    }
} 