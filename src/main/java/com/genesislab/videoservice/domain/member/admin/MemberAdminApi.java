package com.genesislab.videoservice.domain.member.admin;

import com.genesislab.videoservice.domain.member.dto.DateQuery;
import com.genesislab.videoservice.domain.member.dto.Result;
import com.genesislab.videoservice.domain.member.service.MemberStatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/admin/members")
@RestController
public class MemberAdminApi {

    private final MemberStatisticService statisticService;

    @GetMapping
    public Result getMembersByDate(@ModelAttribute @Valid final DateQuery dateQuery, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            fieldErrors.stream().forEach(fieldError -> System.out.println(fieldError.getField()));
        }

        log.debug("dateQuery - startDate: {}, endDate: {}", dateQuery.getStartDate(), dateQuery.getEndDate());
        Result result = statisticService.getMembersByDate(dateQuery);
        return result;
    }
}
