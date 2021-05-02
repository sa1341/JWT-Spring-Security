package com.genesislab.videoservice.domain.member.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/admin")
@Controller
public class AdminController {

    @GetMapping
    public String viewMembers() {
        return "admin";
    }
}
