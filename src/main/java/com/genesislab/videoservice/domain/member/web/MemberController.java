package com.genesislab.videoservice.domain.member.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/members")
@Controller
public class MemberController {

    @GetMapping(value = "/signUp")
    public String signUp() {
        return "sign-up";
    }

    @GetMapping(value = "/signIn")
    public String signIn() {
        return "sign-in";
    }
}
