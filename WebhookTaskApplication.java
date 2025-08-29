package com.example.webhooktask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class WebhookTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebhookTaskApplication.class, args);
    }

    @Bean
    public ApplicationRunner runWebhookFlow() {
        return args -> {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper objectMapper = new ObjectMapper();

            // 1. Generate webhook
            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
            Map<String, String> reqBody = new HashMap<>();
            reqBody.put("name", "John Doe");
            reqBody.put("regNo", "REG12347");
            reqBody.put("email", "john@example.com");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(reqBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            JsonNode respJson = objectMapper.readTree(response.getBody());
            String webhookUrl = respJson.get("webhook").asText();
            String accessToken = respJson.get("accessToken").asText();

            // 2. Solve SQL problem (replace with your actual final SQL)
            String sqlQuery = "YOUR_SQL_QUERY_HERE"; // Replace with the actual query

            // 3. Submit solution using JWT header
            Map<String, String> sqlBody = new HashMap<>();
            sqlBody.put("finalQuery", sqlQuery);

            HttpHeaders webhookHeaders = new HttpHeaders();
            webhookHeaders.setContentType(MediaType.APPLICATION_JSON);
            webhookHeaders.setBearerAuth(accessToken);

            HttpEntity<Map<String, String>> sqlEntity = new HttpEntity<>(sqlBody, webhookHeaders);
            restTemplate.postForEntity(webhookUrl, sqlEntity, String.class);

            System.out.println("Submission done!");
        };
    }
}
