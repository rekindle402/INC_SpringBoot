package com.sinse.bootwebsocket.model.chat;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.bootwebsocket.common.Type;
import com.sinse.bootwebsocket.dto.ChatMessage;
import com.sinse.bootwebsocket.dto.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JAVAEE 내장 API로 구현했던 ServerEndPonit 클래스와 같은 역할.
 * Spring 지원 API로 구현해보기.
 */
@Slf4j
@Component
public class ChatWebSocketHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 접속자 목록(세션)
    // 아래 객체가 ObjectMapper에게 변환을 맡기면 보안상 중요한 세션 정보가 Json으로 전달된다.
    // 따라서 클라이언트는 접속자 아이디만 알면 되므로, connectedUsers라는 Set 별도로 정의하여 관리해야 한다.
    private final Set<WebSocketSession> sessions = new ConcurrentHashMap<>().newKeySet();


    // 접속자 목록(전달용)
    // 다중 쓰레드 환경에서 동시성 문제를 이미 해결해놓은 인터페이스
    private final Set<String> connectedUsers = new ConcurrentHashMap<>().newKeySet();

    //방 목록 저장소
    private final Map<String, ChatRoom> roomStorage = new ConcurrentHashMap<>();

    @Override // OnOpen
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session); // 실질적인 접속자 추가
        log.debug("새 클라이언트 접속됨 " + session.getId() );
    }

    /**
     *  거의 모든 클라이언트의 요청마다, 서버는 접속한 클라이언트들을 대상으로 메시지를 전송해야 한다.
     *  따라서, 반복문을 수시로 Session 수 만큼 돌려야 한다.
     *  이 메서드는 handleMessage() 멤버 메서드에서만 접근할 것이므로 private으로 함.
     */
    private void broadCast(String destination,Object data) throws IOException {
        String json = objectMapper.writeValueAsString(Map.of("destination", destination,"body",data));

        // 서버에 현재 젒속해있는 모든 클라이언트의 세션만큼 반복
        for(WebSocketSession session : sessions){
            if(session.isOpen()) session.sendMessage(new TextMessage(json));
        }
    }

    @Override // OnMessage
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 클라이언트가 보낸 메시지는 String이므로, Jackson 라이브러리로 자바에 객체화 시켜야 함.7
        log.debug("handleMessage" + message.getPayload().toString());
        ChatMessage chatMessage = objectMapper.readValue(message.getPayload().toString(), ChatMessage.class);

        switch (chatMessage.getType()){
            case CONNECT -> {
                connectedUsers.add(chatMessage.getSender());
                // 모든 접속된 유저들에게 접속자 목록에 대한 브로드 캐스팅
                // 브로드캐스팅시, 클라이언트가 서버가 보낸 메시지를 구분 할 수 있는 구분 채널, 또는
                // 구분 값을 포함해서 보내주자
                broadCast("/users", connectedUsers);
            }

            case DISCONNECT -> {
                connectedUsers.remove(chatMessage.getSender());
                broadCast("/users", connectedUsers);
            }

            case MESSAGE -> {
                broadCast("/messages",  chatMessage);
            }
            case ROOM_CREATE -> {
                //방을 생성
                String uuid = UUID.randomUUID().toString();
                ChatRoom room = new ChatRoom();
                room.setRoomId(uuid);
                room.setRoomName(chatMessage.getContent());

                roomStorage.put(uuid, room);
                broadCast("/rooms", roomStorage.values());
            }

            case ROOM_ENTER -> {
                //Map에 모여있는 룸들 중 클라이언트가 참여하기를 원하는 룸을 검색하자
                ChatRoom room = roomStorage.get(chatMessage.getRoomId());
                if(room != null){
                    room.getUsers().add(chatMessage.getSender());
                }
                broadCast("/rooms", roomStorage.values());
            }

            case ROOM_LEAVE -> {
                //Map에 모여있는 룸들 중 클라이언트가 참여하기를 원하는 룸을 검색하자
                ChatRoom room = roomStorage.get(chatMessage.getRoomId());
                if(room != null){
                    room.getUsers().remove(chatMessage.getSender()); // 룸에서 제거
                }
                broadCast("/rooms", roomStorage.values());
            }
            default -> {
                throw new Exception("잘못된 type 요청");
            }
        }
    }


    @Override // OnError
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.debug("handleTransportError");
    }

    @Override // OnClose
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.debug("afterConnectionClosed");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
