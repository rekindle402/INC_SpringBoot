package com.sinse.bootwebsocket.model.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Spring에서 지원하는 WebSocket은 Config 클래스에서 EndPoint를 지정할 수 있고,
 * 클라이언트의 요청을 처리하는 객체를 여기서 등록해야 한다.
 * @RequiredArgsConstructor --> 매개변수가 있는 생성자 자동 생성 (final이 붙어있는 변수만)0
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatWebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws")
                .setAllowedOrigins("*");
    }
}

