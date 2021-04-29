package com.genesislab.videoservice.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"value"})
public class Name {

    @Column(name = "name", length = 50)
    @NotEmpty
    private String value;

    private Name(final String value) {
        this.value = value;
    }

    public static Name of(final String value) {
        return new Name(value);
    }
}
