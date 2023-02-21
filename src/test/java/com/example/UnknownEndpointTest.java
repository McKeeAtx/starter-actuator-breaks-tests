package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UnknownEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetWithValidHeader() throws Exception {
        var validHeader = "Bearer ABCD";
        mockMvc
                .perform(get("/unknown-endpoint").header("Authorization", validHeader))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetWithInvalidHeader() throws Exception {
        var invalidHeader = "Bearer \t";

        mockMvc
                .perform(get("/unknown-endpoint").header("Authorization", invalidHeader))
                .andExpect(status().is4xxClientError());
    }
}
