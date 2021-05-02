package com.genesislab.videoservice.domain.token.service;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.entity.QMember;
import com.genesislab.videoservice.domain.member.repository.MemberRepository;
import com.genesislab.videoservice.domain.token.dto.TokenDto;
import com.genesislab.videoservice.domain.token.dto.TokenRequest;
import com.genesislab.videoservice.domain.token.entity.RefreshToken;
import com.genesislab.videoservice.domain.token.repository.RefreshTokenRepository;
import com.genesislab.videoservice.global.auth.JwtTokenProvider;
import com.genesislab.videoservice.global.error.exception.ErrorCode;
import com.genesislab.videoservice.global.error.exception.InvalidException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.genesislab.videoservice.domain.member.entity.QMember.*;
import static com.genesislab.videoservice.domain.token.entity.QRefreshToken.refreshToken;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthTokenService {

    private final JPAQueryFactory queryFactory;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenDto generateToken(final Member member) {
        final TokenDto tokenDto = jwtTokenProvider.generateJwtToken(member);
        final Member findMember = deleteDupRefreshToken(member.getEmail().getValue());
        final RefreshToken refreshToken = RefreshToken.of(tokenDto.getRefreshToken());
        refreshToken.addMember(findMember);
        refreshTokenRepository.save(refreshToken);
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(final TokenRequest tokenRequest) {
        String refreshToken = tokenRequest.getRefreshToken();

        // 리프레시 토큰 만료 기한 체크
        if (jwtTokenProvider.isExpiredToken(refreshToken)) {
            throw new InvalidException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        String email = jwtTokenProvider.getUserEmail(tokenRequest.getAccessToken());
        // 회원과 토큰 엔티티는 1:1 관계라서 기존에 존재하면 삭제 후 재발급하는 로직
        final Member findMember = deleteDupRefreshToken(email);

        final TokenDto tokenDto = jwtTokenProvider.generateJwtToken(findMember);
        final RefreshToken newRefreshToken = RefreshToken.of(tokenDto.getRefreshToken());
        newRefreshToken.addMember(findMember);
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }

    private Member deleteDupRefreshToken(final String email) {
        final Member findMember = getMemberWithRefreshToken(email);

        if (findMember.hasRefreshToken()) {
            refreshTokenRepository.delete(findMember.getRefreshToken());
        }
        return findMember;
    }

    private Member getMemberWithRefreshToken(final String email) {
        final Member findMember = queryFactory.select(member)
                .from(member)
                .leftJoin(member.refreshToken, refreshToken)
                .fetchJoin()
                .where(member.email.value.eq(email))
                .fetchOne();

        return findMember;
    }

    public Member getMember(final String email) {
        final Member member = queryFactory.select(QMember.member)
                .from(QMember.member)
                .where(QMember.member.email.value.eq(email))
                .fetchOne();
        return member;
    }
}
