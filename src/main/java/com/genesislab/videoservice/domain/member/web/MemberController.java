package com.genesislab.videoservice.domain.member.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/members")
@Controller
public class MemberController {

    @GetMapping(value = "/sign-up")
    public String signUp() {
        return "sign-up";
    }
}
