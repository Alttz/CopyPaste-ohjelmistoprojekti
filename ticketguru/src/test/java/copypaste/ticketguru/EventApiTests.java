package copypaste.ticketguru;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class EventApiTests {

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
    

    @Test
    public void testGetEvents() throws Exception {
        String accessToken = obtainAccessToken("admin", "adminpass");
    	
        mockMvc.perform(get("/api/events")
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Lordi"));
    }

    @Test
    public void testCreateEvent() throws Exception {
        String accessToken = obtainAccessToken("admin", "adminpass");
    	
        String eventJson = "{\"date\":\"2025-12-31\",\"place\":\"Convention Center\",\"city\":\"Las Vegas\",\"name\":\"Future Event\",\"ticketCount\":1000}";
        mockMvc.perform(post("/api/events")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Future Event"));
    }

    // Note: this WILL fail on old events in the database using the testing dataset!
    @Test
    public void testNewEventDate() throws Exception {
        String accessToken = obtainAccessToken("admin", "adminpass");

        String eventJson = "{\"date\":\"2025-12-31\",\"place\":\"Convention Center\",\"city\":\"Las Vegas\",\"name\":\"Future Event\",\"ticketCount\":1000}";
        MvcResult createData = mockMvc.perform(post("/api/events")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
                .andReturn();
                
        assertEquals(createData.getResponse().getStatus(), HttpStatus.CREATED.value());
        
        int eventId = JsonPath.parse(createData.getResponse().getContentAsString()).read("$.id");

        mockMvc.perform(get("/api/events/{id}", eventId)
            .header("Authorization", "Bearer " + accessToken)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].date").exists());
    }
}
