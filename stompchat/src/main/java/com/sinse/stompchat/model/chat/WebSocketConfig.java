package com.sinse.stompchat.model.chat;

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
        // 서버가 클라이언트에게 메시지를 브로드캐스팅할때 사용할 접두어(채널 부분)
        registry.enableSimpleBroker("/topic");

        // 클라이언트에서 서버로 요청을 보낼때는 무조건 접두어에 /app
        registry.setApplicationDestinationPrefixes("/app");

        // 브로드캐스팅이 아닌 1:1 메시징 처리에서 사용할 사용자의 prefix
        registry.setUserDestinationPrefix("/user");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }
}
