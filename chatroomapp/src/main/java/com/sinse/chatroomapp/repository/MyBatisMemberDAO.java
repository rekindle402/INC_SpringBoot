package com.sinse.chatroomapp.repository;

import com.sinse.chatroomapp.domain.Member;
import com.sinse.chatroomapp.exception.MemberException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("mybatisMemberDAO")
public class MyBatisMemberDAO implements MemberDAO {
    private MemberMapper memberMapper;

    public MyBatisMemberDAO(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    public List<Member> selectAll() {
        return memberMapper.selectAll();
    }

    @Override
    public Member select(int member_id) {
        return memberMapper.select(member_id);
    }

    @Override
    public void insert(Member member) {
        try{
            memberMapper.insert(member);
        } catch (DataAccessException e){
            throw new MemberException(e);
        }
    }

    @Override
    public Member selectByIdAndPass(Member member) {
        return memberMapper.selectByIdAndPass(member);
    }
}
