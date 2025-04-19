package com.spongebob.magic_conch_backend.service.impl.chat;

import com.spongebob.magic_conch_backend.config.AiServiceConfig;
import com.spongebob.magic_conch_backend.service.ChatService;
import com.spongebob.magic_conch_backend.service.impl.chat.model.GenerateResponse;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatServiceImpl implements ChatService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private AiServiceConfig aiServiceConfig;

    @Override
    public String callAiForOneReply(String prompt) {
        // 获取基础URL http://localhost:8000
        String baseUrl = aiServiceConfig.getBaseUrl();
        // 构建完整的请求URL http://localhost:8000/generate?prompt=XXX
        String url = String.format("%s/generate?prompt=%s", baseUrl, prompt);
        // 发送GET请求并获取响应
        GenerateResponse response = restTemplate.getForObject(url, GenerateResponse.class);
        if (response != null) {
            // 响应对象不为 null，请求成功收到响应
            String generatedText = response.getGenerated_text();
            if (generatedText != null && !generatedText.isEmpty()) {
                // 响应内容有效
                System.out.println("获取响应成功，内容为: " + generatedText);
            } else {
                // 响应内容为空
                System.out.println("获取响应成功，但响应内容为空");
            }
        } else {
            // 响应对象为 null，请求可能失败
            System.out.println("请求失败，未收到有效的响应");
        }
        // 从响应中取出 generated_text 字段值返回
        return response != null ? response.getGenerated_text() : "";
    }
}
