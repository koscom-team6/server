package koscom.team6.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAIConfig {
    @Value("${openai.secret-key}")
    private String key;

    private static final String BASE_URL = "https://api.openai.com/v1";

    @Bean
    public WebClient openAiWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(BASE_URL)
                .defaultHeader("Authorization", "Bearer " + key)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}