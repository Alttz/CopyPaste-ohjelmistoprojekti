package copypaste.ticketguru;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

import copypaste.ticketguru.service.JwtValidatorService;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtValidatorService jwtValidatorService;

    public MvcResult TryGetToken(String username, String password) throws Exception {
        String body = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";

        return mockMvc.perform(post("/api/login")
        .content(body)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andReturn();
    }

    @Test
    public void TestObtainingValidToken() throws Exception {
        MvcResult testTokenResult = TryGetToken("admin", "adminpass");

        assertEquals(testTokenResult.getResponse().getStatus(), HttpStatus.OK.value());
        String token = "Bearer " + JsonPath.parse(testTokenResult.getResponse().getContentAsString()).read("$.token");
        assertTrue(jwtValidatorService.validateToken(token));
    }

    @Test
    public void TestInvalidUserToken() throws Exception {
        MvcResult testTokenResult = TryGetToken("", "");

        assertEquals(testTokenResult.getResponse().getStatus(), HttpStatus.UNAUTHORIZED.value());
        assertEquals(JsonPath.parse(testTokenResult.getResponse().getContentAsString()).read("$.errorDescription"), "User not found");
    }

    @Test
    public void TestInvalidPasswordToken() throws Exception {
        MvcResult testTokenResult = TryGetToken("admin", "h4x0r1337");

        assertEquals(testTokenResult.getResponse().getStatus(), HttpStatus.UNAUTHORIZED.value());
        assertEquals(JsonPath.parse(testTokenResult.getResponse().getContentAsString()).read("$.errorDescription"), "Invalid credentials");
    }
}
