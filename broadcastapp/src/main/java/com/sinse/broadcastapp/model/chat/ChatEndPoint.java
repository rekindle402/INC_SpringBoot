package com.sinse.broadcastapp.model.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.broadcastapp.dto.ResponseChat;
import com.sinse.broadcastapp.dto.ResponseConnect;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/*
 * ▶ 웹 소켓을 구현하는 방법
 * 1. 순수 JAVA EE API 이용
 * 2. Spring 지원 API 이용
 *
 * ▶ 메시지 교환 방법
 * 1. 개발자가 직접 프로토콜을 설계하는 방법
 * 2. STOMP 프로토콜을 이용하는 방법
 *  - WebSocket 위에서 메시지를 주고받기 위한 메시지 프로토콜(개발자가 직접 설계할 필요 없다, 즉 편하다)
 */

@Slf4j
@ServerEndpoint("/ws/multi")
@Component
public class ChatEndPoint {

    // 유저에게 보낼 데이터가 아닌, 서버측에서 사용할 접속자 정보
    private static Set<Session> userList = new HashSet<>();
    
    // 접속자 명단 : Set으로 접속자들을 모아보자
    // 유저에게 접속자 정보를 보내기 위한 Set
    private static Set<String> userIdList=new HashSet();

    // JAVA <--> Json 변환 담당 객체
    private static ObjectMapper objectMapper = new ObjectMapper();

    // 접속 감지
    @OnOpen
    public void onOpen(Session session) throws JsonProcessingException {
        log.debug("onOpen 호출, 생성 ID = "+session.getId());

        //서버에서 사용할 set에 채우기
        userList.add(session);

        //접속과 동시에, 클라이언트에게 서버의 접속자 명단을 전송하자
//        User user = new User();
//        user.setId(session.getId());
//        user.setName("JH"+session.getId());

        // 접속과 동시에 클라이언트에게 접속자 정보를 구성해서 보내자 (프로토콜 형식에 맞게)
        /*
            {
                "responseType" : "connect",
                "data" : [
                    "1", "2", "3"
                ]
            }
         */
        ResponseConnect responseConnect = new ResponseConnect();
        responseConnect.setResponseType("connect");
        userIdList.add(session.getId());
        responseConnect.setData(userIdList);

        String json = objectMapper.writeValueAsString(responseConnect);
        session.getAsyncRemote().sendText(json);
    }

    // 메시지 감시
    @OnMessage
    public void onMessage(Session session, String message) throws JsonProcessingException {
        log.debug("Client Msg : " + message);
        JsonNode jsonNode = objectMapper.readTree(message);
        String requestType = jsonNode.get("requestType").asText();

        if (requestType.equals("chat")) {
            log.debug("클라이언트가 채팅을 원합니다.");

            ResponseChat responseChat = new ResponseChat();
            responseChat.setResponseType("chat");
            responseChat.setSender(session.getId());
            String data = jsonNode.get("data").asText();
            responseChat.setData(data);

            String json = objectMapper.writeValueAsString(responseChat);

            for (Session ss : userList) {
                if (ss.isOpen()) {
                    ss.getAsyncRemote().sendText(json);
                }
            }
        }
    }
    @OnClose
    public void onClose(Session session) throws Exception {
        log.debug("세션해제");
        //Session이 끊기면 Set 에서 제거
        userList.remove(session);
        userIdList.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) throws JsonProcessingException {
        userList.remove(session);
        userIdList.remove(session.getId());
    }
}
