package com.genesislab.videoservice.domain.video.api;

import com.genesislab.videoservice.domain.video.service.VideoService;
import com.genesislab.videoservice.global.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/videos")
@RestController
public class VideoApi {

    private final VideoService videoService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/{email}/download")
    public StreamingResponseBody getVideo(@PathVariable(name = "email") final String email) throws IOException {
        UserDetails userDetails = jwtTokenProvider.getUserDetails();
        log.debug("userName: {}", userDetails.getUsername());
        return videoService.stream(email);
    }

    @PostMapping
    public ResponseEntity<String> uploadVideo(@RequestParam("files") final MultipartFile[] files) throws Exception {
        log.debug("파일 업로드 프로세스");;
        UserDetails userDetails = jwtTokenProvider.getUserDetails();
        String email = userDetails.getUsername();
        log.debug("userName: {}", email);
        videoService.uploadVideo(files, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
