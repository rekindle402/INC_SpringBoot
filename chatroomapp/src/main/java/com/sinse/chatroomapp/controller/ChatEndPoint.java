package com.sinse.chatroomapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.chatroomapp.config.HttpSessionConfigurator;
import com.sinse.chatroomapp.domain.Member;
import com.sinse.chatroomapp.dto.*;
import com.sinse.chatroomapp.exception.MemberException;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
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
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        if (httpSession != null) {
            Member member = (Member) httpSession.getAttribute("member");

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

            CreateRoomResponse createRoomResponse = new CreateRoomResponse();
            createRoomResponse.setResponseType("createRoom");

            //회원정보 채우기
            Member obj = new Member();
            obj.setId(member.getId());
            obj.setName(member.getName());
            obj.setEmail(member.getEmail());
            memberList.add(obj); // 접속 명단에 채우기

            createRoomResponse.setMemberList(memberList);
            createRoomResponse.setRoomList(roomList);

            String json = objectMapper.writeValueAsString(createRoomResponse);

            session.getAsyncRemote().sendText(json);
        }
    }


    @OnMessage
    public void onMessage(String message, Session session) throws JsonProcessingException {
        //파싱
        JsonNode jsonNode = objectMapper.readTree(message);
        String requestType = jsonNode.get("requestType").asText();

        switch (requestType) {
            case "chat": {

                log.debug("chat");
                String sender = jsonNode.get("sender").asText();
                String data = jsonNode.get("data").asText();
                String uuid = jsonNode.get("uuid").asText();

                Room room = null;

                for (Room r : roomList) {
                    if (uuid.equals(r.getUUID())) {
                        room = r;
                        break;
                    }
                }

                //전송 메시지 구성
                ChatResponse chatResponse = new ChatResponse();
                chatResponse.setResponseType("chat");
                chatResponse.setSender(sender);
                chatResponse.setData(data);
                chatResponse.setUuid(uuid);
                String json = objectMapper.writeValueAsString(chatResponse);

                for (Session ss : userList) {
                    for (Member member : room.getUserList()) {
                        Member obj = (Member) ss.getUserProperties().get("member");
                        if (obj.getId().equals(member.getId())) {
                            ss.getAsyncRemote().sendText(json);
                        }
                    }
                }
                break;
            }
            case "joinRoom": {
                String uuid = jsonNode.get("uuid").asText();
                Room room = null;
                for (Room r : roomList) {
                    if (uuid.equals(r.getUUID())) {
                        room = r;
                        break;
                    }
                }
                boolean exist = false;

                Member member = (Member) session.getUserProperties().get("member");

                for (Member obj : room.getUserList()) {
                    if (member.getId().equals(obj.getId())) {
                        exist = true; // 중복 발견
                        break;
                    }
                }

                Member obj = null;
                if (exist == false) {
                    obj = new Member();
                    obj.setId(member.getId());
                    obj.setName(member.getName());
                    obj.setEmail(member.getEmail());
                    room.getUserList().add(obj);
                }
                JoinRoomResponse joinRoomResponse = new JoinRoomResponse();
                joinRoomResponse.setResponseType("joinRoom");
                joinRoomResponse.setRoom(room);

                session.getAsyncRemote().sendText(objectMapper.writeValueAsString(joinRoomResponse));

                /**
                 * 선언적 프로그래밍
                 roomList.stream()
                 .filter(r -> uuid.equals(r.getUUID()))
                 .findFirst()
                 .orElse(null);
                 */
                break;
            }
            case "exitRoom":
                log.debug("exitRoom");
                break;
            case "createRoom": {
                String userId = jsonNode.get("userId").asText();
                String roomName = jsonNode.get("roomName").asText();

                // 검증
                Member member = (Member) session.getUserProperties().get("member");
                if (!member.getId().equals(userId)) {
                    //클라이언트에게 에러를 전송!!
                    break;
                }
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
                room.setUserList(users);

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

                CreateRoomResponse createRoomResponse = new CreateRoomResponse();
                createRoomResponse.setResponseType("createRoom");
                createRoomResponse.setMemberList(memberList);
                createRoomResponse.setRoomList(roomList);

                //방이 생성된 사실을 서버에 접속한 모든 클라이언트가 알아야 하므로, 브로드 캐스팅의 대상이 된다.
                for (Session ss : userList) {
                    ss.getAsyncRemote().sendText(objectMapper.writeValueAsString(createRoomResponse));
                }
            }
        }

    }

    @OnClose
    public void onClose(Session session) throws JsonProcessingException {
//        Member member = (Member) session.getUserProperties().get("member");
//
//        memberList.remove(member);
//        userList.remove(session);
//
//        //현재 접속자가 참여하고 있는 채티방에서 뺌
//        Room room = null;
//        Member mr = null;
//
//        for(Room r : roomList) {
//            for(Member obj : r.getUserList()){
//                if(obj.getId().equals(member.getId())){
//                    room=r;
//                    mr=obj;
//                    break;
//                }
//            }
//        }
//        room.getUserList().remove(mr);
//
//        // 응답정보 구성후 보내기
//        CloseResponse closeResponse = new CloseResponse();
//        closeResponse.setResponseType("close");
//        closeResponse.setMemberList(memberList);
//        closeResponse.setRoomList(roomList);
//
//       String json = objectMapper.writeValueAsString(closeResponse);
//       session.getAsyncRemote().sendText(json);
    }
}