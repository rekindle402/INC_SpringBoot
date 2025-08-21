package com.sinse.bootwebsocket.dto;

import com.sinse.bootwebsocket.common.Type;
import lombok.Data;

/**
 * 서버와 클라이언트가 대화를 위한 메시지 전달 객체
 * @Type {
 *     CONNECT : 접속, DISCONNECT : 접속해제, MESSAGE : 채팅,
 *     ROOM_CREATE : 방만들기, ROOM_LIST : 방 목록, ROOM_ENTER : 방 입장, ROOM_LEAVE : 방나오기
 * }
 */
@Data
public class ChatMessage {
    private Type type;
    private String sender;
    private String content;
    private String roomId;

}
