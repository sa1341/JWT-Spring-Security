package com.genesislab.videoservice.domain.video.service;

import com.genesislab.videoservice.domain.member.entity.QMember;
import com.genesislab.videoservice.domain.video.entity.QVideo;
import com.genesislab.videoservice.domain.video.entity.Video;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.genesislab.videoservice.domain.member.entity.QMember.*;
import static com.genesislab.videoservice.domain.video.entity.QVideo.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class VideoSearchService {

    private final JPAQueryFactory queryFactory;

    public List<Video> searchVideos() {

        List<Video> videoList = queryFactory.select(video)
                .from(video)
                .join(video.member, member)
                .fetchJoin()
                .fetch();

        return videoList;
    }
}
