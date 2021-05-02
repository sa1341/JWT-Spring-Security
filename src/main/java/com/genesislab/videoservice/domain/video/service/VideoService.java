package com.genesislab.videoservice.domain.video.service;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.service.MemberSearchService;
import com.genesislab.videoservice.domain.model.Email;
import com.genesislab.videoservice.domain.model.Name;
import com.genesislab.videoservice.domain.video.entity.Video;
import com.genesislab.videoservice.domain.video.model.FilePath;
import com.genesislab.videoservice.domain.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
@Service
public class VideoService {

    @Value("${file.storage.path}")
    private String storageDir;

    private final VideoRepository videoRepository;
    private final MemberSearchService memberSearchService;

    @Transactional
    public StreamingResponseBody stream(final String email) throws IOException {
        final Member member = memberSearchService.searchByEmail(Email.of(email));
        final Video video = member.getVideos().get(0);
        final String filePath = video.getFilePath().getValue();
        final File videoFile = new File(filePath);
        final InputStream is = new FileInputStream(videoFile);

        return os -> {
            readAndWrite(is, os);
        };
    }

    private void readAndWrite(InputStream is, OutputStream os) throws IOException {
        byte[] data = new byte[1024];
        int read = 0;
        while ((read = is.read(data)) != 0) {
            os.write(data, 0, read);
        }
        os.flush();
    }

    @Transactional
    public void uploadVideo(final MultipartFile[] files, final String email) throws IOException {
        final Member member = memberSearchService.searchByEmail(Email.of(email));
        saveVideoFiles(files, member);
    }

    private void saveVideoFiles(final MultipartFile[] files, final Member member) throws IOException {
        for (MultipartFile file: files) {
            if (file.isEmpty()) {
                continue;
            }

            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            log.debug("OriginalFilename: {}", fileName);
            final Path path = Paths.get(storageDir, fileName);
            Files.write(path, bytes);

            final Video video = Video.of(Name.of(fileName), FilePath.of(path.toAbsolutePath().toString()));
            video.addMember(member);
            videoRepository.save(video);
        }
    }
}
