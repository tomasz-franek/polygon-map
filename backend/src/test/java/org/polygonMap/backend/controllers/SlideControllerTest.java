package org.polygonMap.backend.controllers;

import com.jayway.jsonpath.JsonPath;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class SlideControllerTest {
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
        mockMvc.perform(
                        post("/slide").content(String.format(requestTemplate, UUID.randomUUID(), null)).accept(APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.slideShowId").isString());
    }

    @Test
    void updateSlideShow_should_updateSlideShowData_when_methodCalledWithCorrectRequest() throws Exception {
        String id = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(
                        post("/slide").content(String.format(requestTemplate, id, "")).accept(APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.slideShowId").isString())
                .andReturn();
        String slideShowId = JsonPath.compile("$.slideShowId").read(mvcResult.getResponse().getContentAsString());
        mockMvc.perform(
                        patch("/slide/{slideShowId}", slideShowId).content(String.format(requestTemplate, id, slideShowId))
                                .accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateSlideShow_should_returnNotFound_when_methodCalledWithIncorrectSlideShowId() throws Exception {
        mockMvc.perform(patch("/slide/{slideShowId}", "wrongId").content(
                        String.format(requestTemplate, UUID.randomUUID(), UUID.randomUUID())).accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void getSlideShow_should_returnSlideShowData_when_methodCalledWithCorrectSlideShowId() throws Exception {
        String id = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(
                        post("/slide").content(String.format(requestTemplate, id, "")).accept(APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.slideShowId").isString())
                .andReturn();
        String slideShowId = JsonPath.compile("$.slideShowId").read(mvcResult.getResponse().getContentAsString());

        mvcResult = mockMvc.perform(get("/slide/{slideShowId}", slideShowId).accept(APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON)).andReturn();
        String actualSlideShow = mvcResult.getResponse().getContentAsString();
        String expectedSlideShow = String.format(requestTemplate, id, slideShowId);
        JSONAssert.assertEquals(expectedSlideShow, actualSlideShow, JSONCompareMode.STRICT);
    }

    @Test
    void getSlideShow_should_returnNotFound_when_methodCalledWithIncorrectSlideShowId() throws Exception {
        mockMvc.perform(
                        get("/slide/{slideShowId}", "wrongId").accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("SlideShow with id = 'wrongId' not found."));

    }

    @Test
    void duplicateSlideShowStep_should_returnNotFound_when_methodCalledWithWrongMapsSlideShowId() throws Exception {
        String id = UUID.randomUUID().toString();

        mockMvc.perform(
                        post("/slide/{slideShowId}/slide/{slideId}", "1", "1").content(String.format(requestTemplate, id, ""))
                                .accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void duplicateSlideShowStep_should_returnNotFound_when_methodCalledWithWrongSlideId() throws Exception {
        String id = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(
                        post("/slide").content(String.format(requestTemplate, id, "")).accept(APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.slideShowId").isString())
                .andReturn();
        String slideShowId = JsonPath.compile("$.slideShowId").read(mvcResult.getResponse().getContentAsString());

        mockMvc.perform(post("/slide/{slideShowId}/slide/{slideId}", slideShowId, "wrong_slide_id").content(
                        String.format(requestTemplate, id, "")).accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void duplicateSlideShowStep_should_returnNoContent_when_methodCalledWithCorrectPosition() throws Exception {
        String id = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(
                        post("/slide").content(String.format(requestTemplate, id, "")).accept(APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.slideShowId").isString())
                .andReturn();
        String slideShowId = JsonPath.compile("$.slideShowId").read(mvcResult.getResponse().getContentAsString());
        mockMvc.perform(get("/slide/{slideShowId}", slideShowId).content(String.format(requestTemplate, id, ""))
                        .accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.slides").isArray()).andExpect(jsonPath("$.slides", hasSize(1)));

        mockMvc.perform(post("/slide/{slideShowId}/slide/{slideId}", slideShowId, "1").content(
                        String.format(requestTemplate, id, "")).accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        mockMvc.perform(get("/slide/{slideShowId}", slideShowId).content(String.format(requestTemplate, id, ""))
                        .accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.slides").isArray()).andExpect(jsonPath("$.slides", hasSize(2)));
    }

    @Test
    void updateSlide_should_returnNoContent_when_methodCalledWithNewPolygons() throws Exception {
        String id = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(
                        post("/slide").content(String.format(requestTemplate, id, "")).accept(APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.slideShowId").isString())
                .andReturn();
        String slideShowId = JsonPath.compile("$.slideShowId").read(mvcResult.getResponse().getContentAsString());
        mockMvc.perform(get("/slide/{slideShowId}", slideShowId).content(String.format(requestTemplate, id, ""))
                        .accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.slides").isArray()).andExpect(jsonPath("$.slides", hasSize(1)));

        mockMvc.perform(patch("/slide/{slideShowId}/slide/{slideId}", slideShowId, "1").content("""
                        [
                            {
                                "id":"a",
                                "color": 100,
                                "polygonId":null,
                                "points": [
                                    {
                                        "longitude":3,
                                        "latitude":31
                                    }
                                ]
                            },
                            {
                                "id":"b",
                                "color": 200,
                                "polygonId":null,
                                "points": [
                                    {
                                        "longitude":4,
                                        "latitude":41
                                    }
                                ]
                            }
                        ]
                        """).accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/slide/{slideShowId}", slideShowId).content(String.format(requestTemplate, id, ""))
                        .accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.slides").isArray()).andExpect(jsonPath("$.slides", hasSize(1)))
                .andExpect(jsonPath("$.slides[0].polygons[0].id").value("a"))
                .andExpect(jsonPath("$.slides[0].polygons[0].points[0].longitude").value(3))
                .andExpect(jsonPath("$.slides[0].polygons[0].points[0].latitude").value(31))
                .andExpect(jsonPath("$.slides[0].polygons[1].id").value("b"))
                .andExpect(jsonPath("$.slides[0].polygons[1].points[0].longitude").value(4))
                .andExpect(jsonPath("$.slides[0].polygons[1].points[0].latitude").value(41));
    }

    @Test
    void updateSlide_should_returnNotFound_when_methodCalledWithIncorrectSlideShowId() throws Exception {
        mockMvc.perform(patch("/slide/{slideShowId}/slide/{slideId}", "incorrect_slide_show_id", "1").content("""
                [
                    {
                        "id":"a",
                        "color": 100,
                        "polygonId":null,
                        "points": [
                            {
                                "longitude":3,
                                "latitude":31
                            }
                        ]
                    }
                ]
                """).accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

    }

    @Test
    void updateSlide_should_returnNotFound_when_methodCalledWithIncorrectSlideId() throws Exception {
        String id = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(
                        post("/slide").content(String.format(requestTemplate, id, "")).accept(APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.slideShowId").isString())
                .andReturn();
        String slideShowId = JsonPath.compile("$.slideShowId").read(mvcResult.getResponse().getContentAsString());
        mockMvc.perform(patch("/slide/{slideShowId}/slide/{slideId}", slideShowId, "incorrect_slide_id").content("""
                [
                    {
                        "id":"a",
                        "color": 100,
                        "polygonId":null,
                        "points": [
                            {
                                "longitude":3,
                                "latitude":31
                            }
                        ]
                    }
                ]
                """).accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

    }

    @Test
    void updateSlide_should_returnBadRequest_when_methodCalledWithEmptyPolygonList() throws Exception {
        mockMvc.perform(patch("/slide/{slideShowId}/slide/{slideId}", "2", "1").content("[]").accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

    }

    @Test
    void saveSlideStep_should_returnNotFound_when_methodCalledWithWrongSlideShowId() throws Exception {
        mockMvc.perform(post("/slide/{slideShowId}/slide", "wrong_slide_show_id").content("""
                        {
                            "slideId":"1",
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
                                }
                            ]
                        }""").accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void saveSlideStep_should_returnBadRequest_when_methodCalledWithEmptyRequest() throws Exception {
        String id = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(
                        post("/slide").content(String.format(requestTemplate, id, "")).accept(APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.slideShowId").isString())
                .andReturn();
        String slideShowId = JsonPath.compile("$.slideShowId").read(mvcResult.getResponse().getContentAsString());
        mockMvc.perform(post("/slide/{slideShowId}/slide", slideShowId).content("{}").accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

    }

    @Test
    void saveSlideStep_should_returnCreated_when_methodCalledWithCorrectRequest() throws Exception {
        String id = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(
                        post("/slide").content(String.format(requestTemplate, id, "")).accept(APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.slideShowId").isString())
                .andReturn();
        String slideShowId = JsonPath.compile("$.slideShowId").read(mvcResult.getResponse().getContentAsString());
        mockMvc.perform(post("/slide/{slideShowId}/slide", slideShowId).content("""
                        {
                            "slideId":"1",
                            "polygons":[
                                {
                                    "id":"2",
                                    "color": 543,
                                    "polygonId":null,
                                    "points": [
                                        {
                                            "longitude":1,
                                            "latitude":1
                                        }
                                    ]
                                }
                            ]
                        }
                        """).accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.slideShowId").isNotEmpty());

    }

    @Test
    void deleteSlideStep_should_returnNotFound_when_methodCalledWithWrongSlideShowId() throws Exception {
        mockMvc.perform(
                delete("/slide/{slideShowId}/slide/{slideId}", "wrong_slide_show_id", "1").accept(APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

    }

    @Test
    void deleteSlideStep_should_returnNotFound_when_methodCalledWithWrongSlideId() throws Exception {
        String id = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(
                        post("/slide").content(String.format(requestTemplate, id, "")).accept(APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.slideShowId").isString())
                .andReturn();
        String slideShowId = JsonPath.compile("$.slideShowId").read(mvcResult.getResponse().getContentAsString());
        mockMvc.perform(
                delete("/slide/{slideShowId}/slide/{slideId}", slideShowId, "wrong_slide_id").accept(APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

    }

    @Test
    void deleteSlideStep_should_returnDeleted_when_methodCalledWithWrongSlideId() throws Exception {
        String id = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(
                        post("/slide").content(String.format(requestTemplate, id, "")).accept(APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.slideShowId").isString())
                .andReturn();
        String slideShowId = JsonPath.compile("$.slideShowId").read(mvcResult.getResponse().getContentAsString());
        mockMvc.perform(delete("/slide/{slideShowId}/slide/{slideId}", slideShowId, "1").accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        mockMvc.perform(get("/slide/{slideShowId}", slideShowId).accept(APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.slides").isArray()).andExpect(jsonPath("$.slides", hasSize(0)));

    }
}
