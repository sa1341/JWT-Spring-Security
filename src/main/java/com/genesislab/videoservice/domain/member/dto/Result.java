package com.genesislab.videoservice.domain.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Result {
    private long count;
    private List<MemberResponse> memberResponses;

    private Result(List<MemberResponse> memberResponses) {
        this.count = memberResponses.size();
        this.memberResponses = memberResponses;
    }

    public static Result of (List<MemberResponse> memberResponses) {
        return new Result(memberResponses);
    }
}
