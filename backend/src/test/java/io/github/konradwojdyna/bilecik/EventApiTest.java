package io.github.konradwojdyna.bilecik;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
public class EventApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsEventAndReturnsItById() throws  Exception{
        String body = """
                {
                  "title": "Hania Rani",
                  "venue": "Klub Stodoła",
                  "city": "Warszawa",
                  "category": "Koncert",
                  "startsAt": "2026-09-12T18:00:00Z",
                  "timezone": "Europe/Warsaw",
                  "capacity": 420
                }
                """;

        String location = mockMvc.perform(
                post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Hania Rani"))
                .andExpect(jsonPath("$.capacity").value(420));
    }

    @Test
    void rejectsEventWithoutTitle() throws Exception {
        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"venue\":\"X\",\"city\":\"Y\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists());
    }

    @Test
    void returns404ForUnknownEvent() throws Exception {
        mockMvc.perform(get("/api/v1/events/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Event not found"));
    }
}
