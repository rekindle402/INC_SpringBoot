package com.sinse.xmlapp.controller;

import com.sinse.xmlapp.model.member.Member;
import com.sinse.xmlapp.model.member.MemberService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestMemberController {

    private MemberService memberService;

    public RestMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/test")
    public String test(){
        return  "test";
    }

    @GetMapping("/members")
    public List<Member> memberList() throws  Exception{
        return memberService.parse();
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e){
        return e.getMessage();
    }
}
