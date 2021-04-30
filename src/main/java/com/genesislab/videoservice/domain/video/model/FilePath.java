package com.genesislab.videoservice.domain.video.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"value"})
public class FilePath {

    @Column(name = "filePath", nullable = false)
    private String value;

    private FilePath(final String value) {
        this.value = value;
    }

    public static FilePath of(final String value) {
        return new FilePath(value);
    }
}
