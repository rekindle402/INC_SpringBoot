package com.sinse.chatroomapp.dto;

import lombok.Data;

@Data
public class JoinRoomResponse {
    private String responseType;
    private Room room;
}
