package com.genesislab.videoservice.domain.member.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class TestController {

    @GetMapping(value = "/test")
    public String test() {
        log.info("index.mustache 실행");
        log.info("하이하이");
        return "index";
    }
}
