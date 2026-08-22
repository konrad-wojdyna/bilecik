package io.github.konradwojdyna.bilecik;

import com.jayway.jsonpath.JsonPath;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
class TicketPoolApiTest {

    @Autowired
    private MockMvc mockMvc;

    private Long createEvent() throws Exception {
        String body = """
                {
                  "title": "Test Event",
                  "venue": "Test Venue",
                  "city": "Warszawa",
                  "startsAt": "2026-09-12T18:00:00Z",
                  "timezone": "Europe/Warsaw",
                  "capacity": 100
                }
                """;

        String response = mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer id = JsonPath.read(response, "$.id");
        return id.longValue();
    }

    private Integer createPool(Long eventId, String body) throws Exception {
        String response = mockMvc.perform(
                        post("/api/v1/events/" + eventId + "/ticket-pools")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.read(response, "$.id");
    }

    @Test
    void createsPoolAndReturnsItById() throws Exception {
        Long eventId = createEvent();

        String body = """
                {
                  "name": "Normalny",
                  "note": "Rzędy C-F",
                  "priceGrosze": 14900,
                  "quantity": 50
                }
                """;

        String location = mockMvc.perform(
                        post("/api/v1/events/" + eventId + "/ticket-pools")
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
                .andExpect(jsonPath("$.name").value("Normalny"))
                .andExpect(jsonPath("$.note").value("Rzędy C-F"))
                .andExpect(jsonPath("$.priceGrosze").value(14900))
                .andExpect(jsonPath("$.quantity").value(50))
                .andExpect(jsonPath("$.remaining").value(50));
    }

    @Test
    void rejectsDuplicatePoolName() throws Exception {
        Long eventId = createEvent();

        String body = """
                {"name": "Normalny", "priceGrosze": 14900, "quantity": 50}
                """;

        createPool(eventId, body);

        mockMvc.perform(post("/api/v1/events/" + eventId + "/ticket-pools")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Ticket pool name exists"));
    }

    @Test
    void rejectsPurchaseWhenPoolIsSoldOut() throws Exception {
        Long eventId = createEvent();

        String body = """
                {"name": "Normalny", "priceGrosze": 14900, "quantity": 2}
                """;

        Integer poolId = createPool(eventId, body);
        String purchaseUrl = "/api/v1/events/" + eventId + "/ticket-pools/" + poolId + "/purchase";

        mockMvc.perform(post(purchaseUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remaining").value(1));

        mockMvc.perform(post(purchaseUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remaining").value(0));

        mockMvc.perform(post(purchaseUrl))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Ticket pool is sold out"));
    }

    @Test
    void hidesPoolFromAnotherEvent() throws Exception {
        Long firstEventId = createEvent();
        Long secondEventId = createEvent();

        String body = """
                {"name": "Normalny", "priceGrosze": 14900, "quantity": 2}
                """;

        Integer poolId = createPool(firstEventId, body);

        mockMvc.perform(get("/api/v1/events/" + secondEventId + "/ticket-pools/" + poolId))
                .andExpect(status().isNotFound());
    }
}