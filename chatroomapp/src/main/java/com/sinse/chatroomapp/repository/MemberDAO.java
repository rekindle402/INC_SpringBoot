package com.sinse.chatroomapp.repository;

import com.sinse.chatroomapp.domain.Member;

import java.util.List;

public interface MemberDAO {
    public List<Member> selectAll();
    public Member select(int member_id);
    public void insert(Member member);
    public Member selectByIdAndPass(Member member);
}
