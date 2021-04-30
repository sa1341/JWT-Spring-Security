package com.genesislab.videoservice.domain.video.repository;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.entity.QMember;
import com.genesislab.videoservice.domain.member.repository.MemberRepository;
import com.genesislab.videoservice.domain.model.*;
import com.genesislab.videoservice.domain.video.entity.QVideo;
import com.genesislab.videoservice.domain.video.entity.Video;
import com.genesislab.videoservice.domain.video.model.FilePath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.genesislab.videoservice.domain.member.entity.QMember.*;
import static com.genesislab.videoservice.domain.video.entity.QVideo.*;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class VideoRepositoryTests {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JPAQueryFactory queryFactory;

    @BeforeEach
    public void setUp() {
        Member member = Member.builder()
                .name(Name.of("임준영"))
                .password(Password.of("wnsdud2"))
                .phoneNumber(PhoneNumber.of("010-7900-7714"))
                .email(Email.of("a79007713@gmail.com"))
                .role(Role.USER)
                .build();
        memberRepository.save(member);

        Path path1 = Paths.get("/Users/limjun-young/workspace/privacy/dev/test/video.mp4");
        String fileName1 = path1.getFileName().toString();
        String filePath1 = path1.toAbsolutePath().toString();

        Path path2 = Paths.get("/Users/limjun-young/workspace/privacy/dev/test/test.txt");
        String fileName2 = path2.getFileName().toString();
        String filePath2 = path2.toAbsolutePath().toString();

        Video video1 = Video.of(Name.of(fileName1), FilePath.of(filePath1), member);
        Video video2 = Video.of(Name.of(fileName2), FilePath.of(filePath2), member);

        videoRepository.save(video1);
        videoRepository.save(video2);
    }

    @Test
    public void 비디오_파일을_저장한다() throws Exception {
        //given
        Member member = memberRepository.findByEmail(Email.of("a79007713@gmail.com")).get();

        //when
        List<Video> videos = videoRepository.findByMember(member);

        videos.forEach(video -> {
            System.out.println(video.getMember().getEmail().getValue());
            System.out.println(video.getFilePath().getValue());
            System.out.println(video.getName().getValue());
        });
    }

    @Test
    public void 비디오_목록을_조회한다() throws Exception {
        //when
        List<Video> result = queryFactory.select(video)
                .from(video)
                .join(video.member, member)
                .fetchJoin()
                .where(member.email.value.eq("a79007713@gmail.com"))
                .fetch();
        //then
        result.forEach(video -> {
            System.out.println(video.getName().getValue());
            System.out.println(video.getFilePath().getValue());
            System.out.println(video.getMember().getName().getValue());
        });
     }
}
