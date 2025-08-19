package com.sinse.chatroomapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.chatroomapp.config.HttpSessionConfigurator;
import com.sinse.chatroomapp.domain.Member;
import com.sinse.chatroomapp.dto.Room;
import com.sinse.chatroomapp.dto.RoomResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@ServerEndpoint(value = "/chat/multi", configurator = HttpSessionConfigurator.class)
public class ChatEndPoint {

    // 접속자 명단 
    private static Set<Session> userList = new HashSet<>(); // 서버측에서 필요한 접속자 정보

    private static Set<Member> memberList = new HashSet<>(); // 클라이언트에게 전달할 접속자 정보
    private static Set<Room> roomList = new HashSet<>(); // 클라이언트에게 전달할 룸 정보

    private static ObjectMapper objectMapper = new ObjectMapper(); // 클라이언트에게 전달함 접속자 정보`

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws JsonProcessingException {
        HttpSession httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());

        if(httpSession != null) {
            Member member = (Member)httpSession.getAttribute("member");

            // 클라이언트는 접속자 명단만 필요하므로, 아이디만 추출하여 보내주자 (보안)

            session.getUserProperties().put("member", member);
            userList.add(session);            // 접속자 명단에 현재 웹소켓 세션 추가

            /**
             * 접속한 클라이언트가 알아야할 정보 전송(누가 접속했는지, 방들의 정보)
             * @Response
             * {reponseType:"createRoom",
             *      memberList : [
             *          {id:"아이디", nmae:"이름",email"이메일}
             *      ],
             *      roomList : []
             * }
             */

            RoomResponse roomResponse = new RoomResponse();
            roomResponse.setResponseType("createRoom");

            //회원정보 채우기
            Member obj = new Member();
            obj.setId(member.getId());
            obj.setName(member.getName());
            obj.setEmail(member.getEmail());
            memberList.add(obj); // 접속 명단에 채우기

            roomResponse.setMemberList(memberList);

            String json = objectMapper.writeValueAsString(roomResponse);

            session.getAsyncRemote().sendText(json);
        }
    }


    @OnMessage
    public void onMessage(String message, Session session) throws JsonProcessingException {
        //파싱
        JsonNode jsonNode = objectMapper.readTree(message);
        String requestType = jsonNode.get("requestType").asText();

        
        //유저 정보 검증

        switch (requestType) {
            case "chat":
                log.debug("chat");
                break;
                // 방 입장 ================================================
//            case "joinRoom":
//                log.debug("joinRoom 응답 : " + jsonNode.get("uuid").asText());
//                // 참여자 목록
//                Set users = new HashSet<>();
//                Member obj = new Member();
//                obj.setId(member.getId());
//                obj.setName(member.getName());
//                obj.setEmail(member.getEmail());
//
//                users.add(obj); //방을 개설한 주인을 참여자로 등록
//                room.setUsers(users);
//                break;
//                // 방 입장 ================================================

            case "exitRoom":
                log.debug("exitRoom");
                break;
            case "createRoom":
                log.debug("createRoom");
                String userId=jsonNode.get("userId").asText();
                String roomName=jsonNode.get("roomName").asText();
                // 로그인시 사용된 HttpSession에 들어있는 회원정보와, 웹소켓을 통해 전달된 회원정보를 비교하여 검증
                Member member = (Member) session.getUserProperties().get("member");
                if(!member.getId().equals(userId)){
                    //클라이언트에게 에러를 전송
                } else {
                    //방 고유 식별자
                    UUID uuid = UUID.randomUUID();
                    Room room = new Room();
                    room.setUUID(uuid.toString());
                    room.setMaster(userId);
                    room.setRoomName(roomName);

                    // 참여자 목록
                    Set users = new HashSet<>();

                    Member obj = new Member();
                    obj.setId(member.getId());
                    obj.setName(member.getName());
                    obj.setEmail(member.getEmail());
                    
                    users.add(obj); //방을 개설한 주인을 참여자로 등록
                    room.setUsers(users);

                    roomList.add(room);

                    /**
                     * 클라이언트에게 전송할 응답 프로토콜
                     * @Response
                     * {
                     *      responseType : "createRoom",
                     *      memberList : [
                     *      {   }
                     *      ]
                     *      roomList : [
                     *      {UUID:"", master:""}
                     *      ]
                     */

                    RoomResponse roomResponse = new RoomResponse();
                    roomResponse.setResponseType("createRoom");
                    roomResponse.setMemberList(memberList);
                    roomResponse.setRoomList(roomList);

                    session.getAsyncRemote().sendText(objectMapper.writeValueAsString(roomResponse));
                }
                break;
        }
    }

//    public void createRoom(Session session) throws JsonProcessingException {
//
//    }
}