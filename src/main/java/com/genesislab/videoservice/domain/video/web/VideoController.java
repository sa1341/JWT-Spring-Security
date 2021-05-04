package com.genesislab.videoservice.domain.video.web;

import com.genesislab.videoservice.domain.member.service.MemberSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/videos")
@Controller
public class VideoController {

    // 비디오 업로드 폼 화면 조회
    @GetMapping
    public String uploadVideo(@CookieValue(value = "accessToken",required = false) String token) {
        log.info("cookie 받기");
        log.info("token: {}", token);
        return "videoUpload";
    }

    // 비디오 플레이 목록 화면 조회
    @GetMapping("/video-play")
    public String getVideo() {
        return "video-play";
    }
}
