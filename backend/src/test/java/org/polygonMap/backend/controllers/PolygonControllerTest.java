package org.polygonMap.backend.controllers;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
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
class PolygonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void savePolygon_should_returnCreated_when_correctRequestData() throws Exception {
        mockMvc.perform(post("/polygon").content("""
                        {
                            "coordinates" : [[1,1],[2,2]]
                        }
                        """).accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.polygonId").isString());
    }

    @Test
    void getPolygon_should_returnPolygonData_when_callWithExistingPolygonId() throws Exception {
        MvcResult result = mockMvc.perform(post("/polygon").content("""
                        {
                            "coordinates" : [[1,1],[2,2]]
                        }
                        """).accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.polygonId").isString())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String id = JsonPath.parse(response).read("$.polygonId");

        mockMvc.perform(
                        get("/polygon/{polygonId}", id).accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.polygonId", equalTo(id))).andExpect(jsonPath("$.coordinates").isArray())
                .andExpect(jsonPath("$.coordinates", hasSize(2))).andReturn();
    }

    @Test
    void updatePolygon_should_returnNoContent_when_callPolygonUpdated() throws Exception {
        MvcResult result = mockMvc.perform(post("/polygon").content("""
                        {
                            "coordinates" : [[1,1],[2,2]],
                            "color": 0
                        }
                        """).accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON)).andExpect(jsonPath("$.polygonId").isString())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String polygonId = JsonPath.parse(response).read("$.polygonId");

        mockMvc.perform(patch("/polygon/{polygonId}", polygonId).content("""
                        {
                            "coordinates" : [[3,4],[5,6]],
                            "color": 1
                        }
                        """).accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(
                        get("/polygon/{polygonId}", polygonId).accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.polygonId", equalTo(polygonId))).andExpect(jsonPath("$.coordinates").isArray())
                .andExpect(jsonPath("$.coordinates", hasSize(2))).andExpect(jsonPath("$.coordinates[0][0]").value(3))
                .andExpect(jsonPath("$.coordinates[0][1]").value(4)).andExpect(jsonPath("$.coordinates[1][0]").value(5))
                .andExpect(jsonPath("$.coordinates[1][1]").value(6));
    }

    @Test
    void updatePolygon_should_returnNoNotFound_when_callWithNonExistingPolygonId() throws Exception {
        String polygonId = UUID.randomUUID().toString();
        mockMvc.perform(patch("/polygon/{polygonId}", polygonId).content("""
                        {
                            "points" : [
                                {
                                    "longitude":3,
                                    "latitude":4
                                },
                                {
                                    "longitude":5,
                                    "latitude":6
                                }
                            ],
                            "color": 1
                        }
                        """).accept(APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
                .andExpect(content().string(String.format("Polygon with id = '%s' not found.", polygonId)));

    }
}