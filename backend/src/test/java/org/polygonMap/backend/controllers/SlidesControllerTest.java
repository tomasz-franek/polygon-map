package org.polygonMap.backend.controllers;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Log4j2
public class SlidesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String requestTemplate = """
            {
                "id":"%s",
                "slideShowId": "%s",
                "centerPoint": {
                    "longitude":11,
                    "latitude":21
                },
                "mapZoom":3,
                "title":"test",
                "description":"description test",
                "version":"1.0.0",
                "createdDate":"2025-10-10",
                "createdUser": "createdUser",
                "nextVersion":null,
                "prevVersion":null,
                "lastModifiedDate":"2025-10-10",
                "lastModifiedUser": "lastModifiedUser",
                "hash":"xxx",
                "slides":[
                    {
                        "slideId": "1",
                        "polygons":[
                            {
                                "id":"1",
                                "color": 100,
                                "polygonId":null,
                                "points": [
                                    {
                                        "longitude":1,
                                        "latitude":1
                                    }
                                ]
                            },
                            {
                                "id":"2",
                                "color": 200,
                                "polygonId":null,
                                "points": [
                                    {
                                        "longitude":2,
                                        "latitude":2
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
            """;

    @Test
    void saveSlideShow_should_returnCreated_when_correctRequestData() throws Exception {
        mockMvc.perform(post("/slides").content(String.format(requestTemplate, UUID.randomUUID(), null))
                        .accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.slideShowId").isString());
    }

    @Test
    void updateSlideShow_should_updateSlideShowData_when_methodCalledWithCorrectRequest() throws Exception {
        String id = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(
                        post("/slides").content(String.format(requestTemplate, id, "")).accept(APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.slideShowId").isString())
                .andReturn();
        String slideShowId = JsonPath.compile("$.slideShowId").read(mvcResult.getResponse().getContentAsString());
        mockMvc.perform(
                        patch("/slides/{slideShowId}", slideShowId).content(String.format(requestTemplate, id, slideShowId))
                                .accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateSlideShow_should_returnNotFound_when_methodCalledWithIncorrectSlideShowId() throws Exception {
        mockMvc.perform(patch("/slides/{slideShowId}", "wrongId").content(
                        String.format(requestTemplate, UUID.randomUUID(), UUID.randomUUID())).accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void getSlideShow_should_returnSlideShowData_when_methodCalledWithCorrectSlideShowId() throws Exception {
        String id = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(
                        post("/slides").content(String.format(requestTemplate, id, "")).accept(APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.slideShowId").isString())
                .andReturn();
        String slideShowId = JsonPath.compile("$.slideShowId").read(mvcResult.getResponse().getContentAsString());

        mvcResult = mockMvc.perform(get("/slides/{slideShowId}", slideShowId).accept(APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON)).andReturn();
        String actualSlideShow = mvcResult.getResponse().getContentAsString();
        String expectedSlideShow = String.format(requestTemplate, id, slideShowId);
        log.info(expectedSlideShow);
        JSONAssert.assertEquals(expectedSlideShow, actualSlideShow, JSONCompareMode.STRICT);
    }

    @Test
    void getSlideShow_should_returnNotFound_when_methodCalledWithIncorrectSlideShowId() throws Exception {
        mockMvc.perform(get("/slides/{slideShowId}", "wrongId").accept(APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
                .andExpect(content().string("SlideShow with id = 'wrongId' not found."));

    }

    @Test
    void duplicateSlideShowStep_should_returnNotFound_when_methodCalledWithWrongMapsSlideShowId() {
        throw new NotImplementedException();
    }

    @Test
    void duplicateSlideShowStep_should_returnNotFound_when_methodCalledWithWrongSlideId() {
        throw new NotImplementedException();
    }

    @Test
    void duplicateSlideShowStep_should_returnNoContent_when_methodCalledWithCorrectPosition() {
        throw new NotImplementedException();
    }
}
