package com.genesislab.videoservice.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class DateQuery {

    @NotBlank(message = "조회 시작 날짜를 입력하세요.")
    private String startDate;

    @NotBlank(message = "조회 시작 날짜를 입력하세요.")
    private String endDate;
}
