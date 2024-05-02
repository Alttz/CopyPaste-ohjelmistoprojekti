package copypaste.ticketguru;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import com.jayway.jsonpath.JsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EndToEndTest {

    @Autowired
    private MockMvc mockMvc;
        
    @Test
    public String obtainAccessToken(String username, String password) throws Exception {
        String body = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";

        MvcResult result = mockMvc.perform(post("/api/login")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        return JsonPath.parse(responseContent).read("$.token");
    }
    
    public Long createEventAndReturnId(String accessToken) throws Exception {
        String jsonEvent = "{\"date\":\"2024-12-31\",\"place\":\"Convention Center\",\"city\":\"Las Vegas\",\"name\":\"New Year Eve\",\"ticketCount\":500}";
        MvcResult result = mockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken)
            .content(jsonEvent))
            .andExpect(status().isCreated())
            .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        return JsonPath.parse(responseContent).read("$.id", Long.class);
    }


    @Test
    public void testEventLifecycleIncludingTickets() throws Exception {
        String accessToken = obtainAccessToken("admin", "adminpass");

        // Step 1: Create an event and get its ID
        Long eventId = createEventAndReturnId(accessToken);

        // Step 2: Add ticket types to the event
        String jsonTicketTypes = "[{\"name\": \"Adult\", \"price\": 25.00}, {\"name\": \"VIP\", \"price\": 50.00}, {\"name\": \"Child\", \"price\": 10.00}]";
        mockMvc.perform(post("/api/events/" + eventId + "/tickettypes")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonTicketTypes))
                .andExpect(status().isCreated());

        // Step 3: Purchase tickets
        String purchasePayload = String.format("""
                {
                  "userId": 1,
                  "purchaseRequestRows": [
                    {
                      "eventId": %d,
                      "userId": 1,
                      "ticketTypeNames": ["Adult", "Adult", "VIP", "VIP", "Child"]
                    }
                  ]
                }""", eventId);
        mockMvc.perform(post("/api/purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(purchasePayload))
                .andExpect(status().isOk());

        // Step 4: Fetch tickets for the event to get UUIDs
        MvcResult result = mockMvc.perform(get("/api/tickets")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        
        String ticketsJson = result.getResponse().getContentAsString();
        String uuid = JsonPath.parse(ticketsJson).read("$[0].uuid");  // Fetch the UUID of the first ticket

        // Step 5: Mark the ticket as used using its UUID
        mockMvc.perform(patch("/api/tickets/markAsUsed")
                .param("uuid", uuid)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent());

        // Step 6: Re-check the ticket's state
        mockMvc.perform(get("/api/tickets/byUuid")
                .param("uuid", uuid)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].used").value(true));                
    }


}
