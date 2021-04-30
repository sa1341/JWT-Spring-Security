package com.genesislab.videoservice.domain.video.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/videos")
@Controller
public class VideoController {

    @GetMapping
    public String uploadVideo() {
        return "videoUpload";
    }
}
