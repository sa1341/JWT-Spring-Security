package com.genesislab.videoservice.domain.video.service;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.model.Name;
import com.genesislab.videoservice.domain.video.entity.Video;
import com.genesislab.videoservice.domain.video.model.FilePath;
import com.genesislab.videoservice.domain.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class VideoService {

    @Value("${file.storage.path}")
    private String storageDir;

    private final VideoRepository videoRepository;

    public void saveVideo(final MultipartFile[] files, final Member member) throws IOException {
        uploadVideoFiles(files, member);
    }

    private void uploadVideoFiles(final MultipartFile[] files, final Member member) throws IOException {
        for (MultipartFile file: files) {
            if (file.isEmpty()) {
                continue;
            }

            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            log.info("OriginalFilename: {}", fileName);
            Path path = Paths.get(storageDir, fileName);
            Files.write(path, bytes);

            Video video = Video.of(Name.of(fileName), FilePath.of(path.toAbsolutePath().toString()), member);
            videoRepository.save(video);
        }
    }
}
