package com.genesislab.videoservice.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"value"})
public class PhoneNumber {

    @Column(name = "phoneNumber", length = 13)
    @NotEmpty
    private String value;

    private PhoneNumber(final String value) {
        this.value = value;
    }

    public static PhoneNumber of(final String value) {
        return new PhoneNumber(value);
    }
}
