package com.genesislab.videoservice.domain.member.service;

import com.genesislab.videoservice.domain.member.dto.DateQuery;
import com.genesislab.videoservice.domain.member.dto.MemberResponse;
import com.genesislab.videoservice.domain.member.dto.Result;
import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.entity.QMember;
import com.genesislab.videoservice.global.util.LocalDateUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.genesislab.videoservice.domain.member.entity.QMember.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberStatisticService {

    private final JPAQueryFactory queryFactory;

    @Transactional
    public Result getMembersByDate(final DateQuery dateQuery) {
        LocalDateTime startDateTime = LocalDateUtils.parseStartDate(dateQuery.getStartDate());
        LocalDateTime endDateTime = LocalDateUtils.parseEndDate(dateQuery.getEndDate());

        List<Member> members = findMembersByDateQueries(startDateTime, endDateTime);
        List<MemberResponse> memberResponses = members.stream()
                .map(Member::toResponse)
                .collect(Collectors.toList());

        return Result.of(memberResponses);
    }

    private List<Member> findMembersByDateQueries(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return queryFactory.select(member)
                .from(member)
                .where(member.createdAt.between(startDateTime, endDateTime))
                .fetch();
    }
}
