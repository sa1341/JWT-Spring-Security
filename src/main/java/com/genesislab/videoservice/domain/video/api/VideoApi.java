package com.genesislab.videoservice.domain.video.api;

import com.genesislab.videoservice.domain.member.entity.CustomUserDetails;
import com.genesislab.videoservice.domain.video.service.VideoService;
import com.genesislab.videoservice.global.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/videos")
@RestController
public class VideoApi {

    private final VideoService videoService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public String getVideo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails details = (CustomUserDetails) authentication.getDetails();
        log.debug("authentication: {}", details.getEmail());
        String videoName = "전래동화";
        return String.format("비디오 이름은 %s 입니다.", videoName);
    }

    @PostMapping
    public ResponseEntity<String> uploadVideo(@RequestParam("files") final MultipartFile[] files) throws Exception {
        log.debug("파일 업로드 프로세스");;
        UserDetails userDetails = jwtTokenProvider.getUserDetails();
        String email = userDetails.getUsername();
        log.debug("userName: {}", email);

        videoService.saveVideo(files, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
