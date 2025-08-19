package com.sinse.chatroomapp.service;

import com.sinse.chatroomapp.domain.Member;
import com.sinse.chatroomapp.repository.MemberDAO;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    private MemberDAO memberDAO;

    public MemberServiceImpl(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    @Override
    public Member login(Member member) {
        return memberDAO.selectByIdAndPass(member);
    }

    @Override
    public void join(Member member){
            memberDAO.insert(member);
    }

    @Override
    public void logout(Member member) {

    }
}
