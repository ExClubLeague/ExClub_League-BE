package com.exclub.exclub_league.User.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class UserViewController {
    @GetMapping("/login")
    public String login() {
        return "oauthLogin";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/OAuth2LoginSuccess")
    public String aa() {
        return "OAuth2LoginSuccess";} // 리액트 경로로 리다이렉트

}
