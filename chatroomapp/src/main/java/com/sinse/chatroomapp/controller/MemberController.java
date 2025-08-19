package com.sinse.chatroomapp.controller;

import com.sinse.chatroomapp.domain.Member;
import com.sinse.chatroomapp.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member/login")
    public String loginForm(){
        return "member/login";
    }

    @GetMapping("/member/regist")
    public String registForm(){
        return "member/regist";
    }

    @GetMapping("/chat/main")
    public String chatMain(HttpSession session){
        if(session.getAttribute("member")==null){
            return "member/login";
        }
        return "chat/main";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute Member member, HttpSession session, RedirectAttributes redirectAttributes){
        Member loginMember = memberService.login(member);

        if (loginMember == null) {
            redirectAttributes.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "redirect:/member/login";
        }

        session.setAttribute("member", loginMember);
        redirectAttributes.addFlashAttribute("message", loginMember.getName() + "님 환영합니다.");
        return "redirect:/chat/main";
    }

    @PostMapping("/member/regist")
    public String regist(@ModelAttribute Member member, RedirectAttributes redirectAttributes){
        memberService.join(member);
        redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
        return "redirect:/member/login";
    }

    @GetMapping("/chat/room")
    public String room(HttpSession session){
        return "chat/room";
    }
}
