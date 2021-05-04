package com.genesislab.videoservice.domain.member.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping(value = "/members")
@Controller
public class MemberController {

    @GetMapping(value = "/signUp")
    public String signUp(@CookieValue(name = "accessToken", required = false) String token) {
        log.info("token: ", token);
        return "sign-up";
    }

    @GetMapping(value = "/signIn")
    public String signIn() {
        return "sign-in";
    }

    @GetMapping(value = "/home")
    public String test() {
        return "index";
    }
}
