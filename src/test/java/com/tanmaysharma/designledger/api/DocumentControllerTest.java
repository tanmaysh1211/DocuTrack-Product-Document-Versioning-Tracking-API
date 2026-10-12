package com.tanmaysharma.designledger.api;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DocumentControllerTest {
    @Autowired MockMvc mockMvc;

    @Test
    void rejectsRequestWithoutApiKey() throws Exception {
        mockMvc.perform(get("/api/v1/documents"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Missing or invalid API key")));
    }

    @Test
    void validatesCreateRequest() throws Exception {
        mockMvc.perform(post("/api/v1/documents")
                        .header("X-API-Key", "test-api-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productCode\":\"\",\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.productCode").exists())
                .andExpect(jsonPath("$.validationErrors.name").exists());
    }

    @Test
    void createsAndReadsDocument() throws Exception {
        String response = mockMvc.perform(post("/api/v1/documents")
                        .header("X-API-Key", "test-api-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"productCode":"EDGE-100","name":"Edge Switch","description":"24-port switch"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.productCode", is("EDGE-100")))
                .andReturn().getResponse().getContentAsString();

        String id = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response).get("id").asText();
        mockMvc.perform(get("/api/v1/documents/{id}", id)
                        .header("X-API-Key", "test-api-key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Edge Switch")));
    }
}
