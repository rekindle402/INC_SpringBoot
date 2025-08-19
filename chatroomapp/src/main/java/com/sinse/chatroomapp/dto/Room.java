package com.sinse.chatroomapp.dto;

import com.sinse.chatroomapp.domain.Member;
import lombok.Data;

import java.util.Set;

@Data
public class Room {
    private String UUID; // 방의 고유 식별자
    private String master;
    private String roomName;
    private Set<Member> users;
}
