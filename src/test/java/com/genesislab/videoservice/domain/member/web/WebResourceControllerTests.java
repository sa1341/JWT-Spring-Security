package com.genesislab.videoservice.domain.member.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest({TestController.class})
public class WebResourceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void mustache_화면뷰잉_테스트() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andDo(print());
     }
}
