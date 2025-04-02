package org.polygonMap.backend.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getSearchResultData_should_returnOk_when_objectFound() throws Exception {
        mockMvc.perform(get("/search").param("slideName", "eS").accept(APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[*].title", everyItem(equalTo("test"))));
    }
}