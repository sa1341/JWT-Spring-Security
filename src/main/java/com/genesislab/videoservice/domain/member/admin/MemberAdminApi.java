package com.genesislab.videoservice.domain.member.admin;

import com.genesislab.videoservice.domain.member.dto.Result;
import com.genesislab.videoservice.domain.member.service.MemberStatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/admin/members")
@RestController
public class MemberAdminApi {

    private final MemberStatisticService statisticService;

    @GetMapping
    public Result getMembersByDate(@RequestParam @NotBlank final String startDate, @RequestParam @NotBlank final String endDate) {
        log.debug("startDate: {}", startDate);
        log.debug("endDate: {}", endDate);
        Result result = statisticService.getMembersByDate(startDate, endDate);
        return result;
    }
}
