package com.genesislab.videoservice.domain.video.dto;

import com.genesislab.videoservice.domain.model.Name;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(of = {"name"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VideoResponse {
    private Name name;
}
