package com.genesislab.videoservice.domain.video.service;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.service.MemberSearchService;
import com.genesislab.videoservice.domain.model.Email;
import com.genesislab.videoservice.domain.model.Name;
import com.genesislab.videoservice.domain.video.entity.Video;
import com.genesislab.videoservice.domain.video.model.FilePath;
import com.genesislab.videoservice.domain.video.repository.VideoRepository;
import com.genesislab.videoservice.global.error.exception.EntityNotFoundException;
import com.genesislab.videoservice.global.error.exception.ErrorCode;
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
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        final Optional<Video> video = member.getVideos().stream().filter(VideoService::checkExistFile).findFirst();
        video.orElseThrow(() -> new EntityNotFoundException(ErrorCode.VIDEO_NOT_FOUND.getMessage()));
        final File videoFile = new File(video.get().getFilePath().getValue());
        final InputStream is = new FileInputStream(videoFile);

        return os -> {
            readAndWrite(is, os);
        };
    }

    private static boolean checkExistFile(final Video video) {
        Path videoFile = Paths.get(video.getFilePath().getValue());
        return Files.exists(videoFile) && !Files.isDirectory(videoFile);
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
            final Path dir = Paths.get(storageDir, UUID.randomUUID().toString());

            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            final Path videoPath = Paths.get(dir.toAbsolutePath().toString(), fileName);

            Files.write(videoPath, bytes);

            final Video video = Video.of(Name.of(fileName), FilePath.of(videoPath.toAbsolutePath().toString()));
            video.addMember(member);
            videoRepository.save(video);
        }
    }
}
