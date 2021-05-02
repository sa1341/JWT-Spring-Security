package com.genesislab.videoservice.domain.video.web;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.service.MemberSearchService;
import com.genesislab.videoservice.domain.model.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/videos")
@Controller
public class VideoController {

    private final MemberSearchService memberSearchService;

    @GetMapping
    public String uploadVideo(HttpServletResponse response) {
        return "videoUpload";
    }

    @GetMapping("/{email}/video-play")
    public String getVideo(@PathVariable(name = "email") final String email, Model model) {
        Member member = memberSearchService.searchByEmail(Email.of(email));
        model.addAttribute("email", member.getEmail().getValue());
        return "video-play";
    }
}
