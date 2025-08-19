package com.sinse.chatroomapp.repository;

import com.sinse.chatroomapp.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//MyBatis의 xml을 직접 제어
@Mapper
public interface MemberMapper {
    public List<Member> selectAll();
    public Member select(int member_id);
    public void insert(Member member);
    public Member selectByIdAndPass(Member member);
}

