package com.genesislab.videoservice.domain.token.entity;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    private RefreshToken(final String value) {
        this.value = value;
    }

    public static RefreshToken of(final String value) {
        return new RefreshToken(value);
    }

    public void addMember(final Member member) {
        this.member = member;
        member.setRefreshToken(this);
    }

    public void deleteMember(final Member member) {
        this.member = null;
        member.setRefreshToken(null);
    }
}
