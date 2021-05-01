package com.genesislab.videoservice.domain.video.repository;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByMember(Member member);
}
