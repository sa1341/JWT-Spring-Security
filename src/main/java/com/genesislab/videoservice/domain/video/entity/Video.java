package com.genesislab.videoservice.domain.video.entity;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.model.Name;
import com.genesislab.videoservice.domain.video.model.FilePath;
import com.genesislab.videoservice.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@ToString(of = {"name"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Video extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private FilePath filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    private Video(Name name, FilePath filePath, Member member) {
        this.name = name;
        this.filePath = filePath;
        this.member = member;
    }

    public static Video of(final Name name, final FilePath filePath, final Member member) {
        return new Video(name, filePath, member);
    }
}
