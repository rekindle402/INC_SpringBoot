package com.sinse.formloginnodb.member;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Security;

@Controller
public class MemberController {
    /**
     * 스프링 시큐리티의 기본 로그임 폼에서는 로그인 성공시 무조건 root로 redirect 되므로,
     * 로그인 성공 후 보여질 페이지를 컨트롤러에서 매핑해야 한다.
     */

    // 스프링 시큐리티에 의해 인증이 성공되면 사용자 정보는 UserDetails 객체에 들어있다.
    // 이때 UserDetails 라는 객체를 꺼내는 방법은 총 4가지 방법이 있다.
    /*--------------------------------
     방법 1. 세션에서 직접 꺼내기
     ---------------------------------*/
    @GetMapping("/")
    public String index1(HttpSession session, Model model) {
        SecurityContext context = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");

        Authentication auth = context.getAuthentication();
        UserDetails userDetails = (UserDetails)auth.getPrincipal();
        String id = userDetails.getUsername();

        model.addAttribute("id", "seesion에서 꺼냄 " + id);

        return "member/index";
    }

     /*--------------------------------
     방법 2. Authentication에서 직접 꺼내기
     ---------------------------------*/
    @GetMapping("/main")
    public String main(Authentication auth, Model model) {
        UserDetails userDetails = (UserDetails)auth.getPrincipal();
        model.addAttribute("id", "Authentication에서 꺼냄 : "+userDetails.getUsername());

        return "member/index";
    }
     /*--------------------------------
     방법 3. SecurityContextHolder에서 직접 꺼내기
     ---------------------------------*/

    @GetMapping("/home")
    public String home(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        String id = userDetails.getUsername();
        model.addAttribute("id","ContextHolder에서 꺼냄 : "+ id);
        return "member/index";
    }

     /*--------------------------------
     방법 4. @AuthenticationPrincipal
     ---------------------------------*/
    @GetMapping("/default")
    public String default1(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String id = userDetails.getUsername();
        model.addAttribute("id", "@AuthenticationPrincipal 에서 꺼냄 " + userDetails.getUsername());
        return "member/index";
    }
}
