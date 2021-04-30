package com.genesislab.videoservice.domain.member.model;

import com.genesislab.videoservice.domain.model.Email;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmailValidTests {

    private static final String otherEmail = "adjls12314";

    @Test
    public void 이메일_입력값을_검증한다() throws Exception {
        //given
        Email email = Email.of(otherEmail);

        //then
        Assertions.assertThat(email.getValue()).isEqualTo("adjls12314");
     }

}
