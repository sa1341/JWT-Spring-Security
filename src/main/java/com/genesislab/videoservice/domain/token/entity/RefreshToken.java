package com.genesislab.videoservice.domain.token.entity;

import com.genesislab.videoservice.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "expired_at", nullable = false, updatable = false)
    private LocalDateTime expiredDateTime;

    private RefreshToken(LocalDateTime expiredDateTime) {
        this.expiredDateTime = expiredDateTime;
    }

    public static RefreshToken of(LocalDateTime expiredDateTime) {
        return new RefreshToken(expiredDateTime);
    }
}
