package com.sinse.chatapp.model.chat;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@ServerEndpoint("/ws/echo")
@Component
public class ChatEndPoint {
    // 접속 감지 메서드(연결 감지)
    // 웹 소켓에서는 더이상 java se에서 Socket 객체가 통신을 담당하지 않고 대신 Session 객체가 통신을 담당
    // 또한, 접속자마다 1:1 대응하는 Thread를 제어할 필요가 없다
    @OnOpen
    public void onOpen(Session session) {
        log.info("onOpen : "+ session.getId());
    }
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.debug("onMessage : " + session.getId()+ " : " + message);
        // 클라이언트에게 메시지 전송
        session.getBasicRemote().sendText("server : " + message);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException {
        log.debug("Close : "+session.getId()+","+reason);
    }

    @OnError
    public void onError(Session session, Throwable error) throws IOException {
        error.printStackTrace();
    }
}
