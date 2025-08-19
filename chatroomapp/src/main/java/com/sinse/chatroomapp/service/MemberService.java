package com.sinse.chatroomapp.service;

import com.sinse.chatroomapp.domain.Member;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public interface MemberService {
    public Member login(Member member);
    public void join(Member member);
    public void logout(Member member);
}
