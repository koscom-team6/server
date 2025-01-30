package koscom.team6.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 메세지 구독 경로 (서버 -> 클라이언트)
        registry.enableSimpleBroker("/sub");

        // 메세지 발행 경로 (클라이언트 -> 서버)
        registry.setApplicationDestinationPrefixes("/pub");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // client에서 websocket 연결할 때 사용 API - 대결 답안용
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*");

        // client에서 websocket 연결할 때 사용 API - 대결 매칭용
        registry.addEndpoint("/ws-match")
                .setAllowedOriginPatterns("*");

    }
}
