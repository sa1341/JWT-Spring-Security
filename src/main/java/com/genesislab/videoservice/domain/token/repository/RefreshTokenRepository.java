package com.genesislab.videoservice.domain.token.repository;

import com.genesislab.videoservice.domain.token.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
