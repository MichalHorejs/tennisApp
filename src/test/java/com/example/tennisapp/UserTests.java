package com.example.tennisapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTests {

    @Autowired
    private MockMvc mockMvc;

    private static String userAccessToken;

    @BeforeAll
    public void loginUser() throws Exception {
        // Perform login to get JWT token
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phoneNumber\":\"123456789\", \"password\":\"user\"}"))
                .andExpect(status().isOk())
                .andReturn();

        // Extract the JWT token from the response
        String response = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseJson = mapper.readTree(response);

        userAccessToken = responseJson.get("access_token").asText();

    }

    /* ---------------------------------- GET method tests ---------------------------------- */
    @Test
    void getUser() throws Exception {
        testUserApi(get("/api/user/123456789"), "", status().isOk());
    }

    @Test
    void getUserNotFound() throws Exception {
        testUserApi(get("/api/user/123456780"), "", status().isBadRequest());
    }

    @Test
    void getAllCourts() throws Exception {
        testUserApi(get("/api/court"), "", status().isOk());
    }

    @Test
    void getCourtById() throws Exception {
        testUserApi(get("/api/court/1"), "", status().isOk());
    }

    @Test
    void getCourtByIdNotFound() throws Exception {
        testUserApi(get("/api/court/999"), "", status().isBadRequest());
    }

    @Test
    void getReservationByPhone() throws Exception {
        String jsonBody = "{\"phoneNumber\":123456789,\"futureOnly\":false}";
        testUserApi(get("/api/reservation/phone"), jsonBody, status().isOk());

        jsonBody = "{\"phoneNumber\":123456789,\"futureOnly\":true}";
        testUserApi(get("/api/reservation/phone"), jsonBody, status().isOk());
    }

    @Test
    void getReservationByPhoneNotFound() throws Exception {
        String jsonBody = "{\"phoneNumber\":999999999,\"futureOnly\":false}";
        testUserApi(get("/api/reservation/phone"), jsonBody, status().isBadRequest());
    }

    @Test
    void getReservationByCourtId() throws Exception {
        String jsonBody = "{\"courtId\":3,\"futureOnly\":false}";
        testUserApi(get("/api/reservation/court"), jsonBody, status().isOk());

        jsonBody = "{\"courtId\":3,\"futureOnly\":true}";
        testUserApi(get("/api/reservation/court"), jsonBody, status().isOk());
    }

    @Test
    void getReservationByCourtIdNotFound() throws Exception {
        String jsonBody = "{\"courtId\":9999,\"futureOnly\":false}";
        testUserApi(get("/api/reservation/court"), jsonBody, status().isBadRequest());
    }


    /* ---------------------------------- POST method tests ---------------------------------- */
    @Test
    void saveReservationWithKnownUser() throws Exception {
        String jsonBody = "{\"user\":{\"phoneNumber\":\"123456789\"}," +
                "\"courtId\":3," +
                "\"date\":\"2024-11-30\"," +
                "\"startTime\":\"15:00:00\"," +
                "\"endTime\":\"16:00:00\"," +
                "\"isDoubles\":false}";
        testUserApi(post("/api/reservation"), jsonBody, status().isOk());
    }

    @Test
    void saveReservationWithNewUser() throws Exception {
        String jsonBody = "{\"user\":" +
                "{\"phoneNumber\":\"333333333\"," +
                "\"name\":\"user3\"," +
                "\"password\":\"user3\"}," +
                "\"courtId\":3," +
                "\"date\":\"2024-10-30\"," +
                "\"startTime\":\"12:00:00\"," +
                "\"endTime\":\"15:00:00\"," +
                "\"isDoubles\":false}";
        testUserApi(post("/api/reservation"), jsonBody, status().isOk());
    }

    @Test
    void saveReservationWhereTimesCollide() throws Exception {
        String jsonBody = "{\"user\":{\"phoneNumber\":\"123456789\"}," +
                "\"courtId\":3," +
                "\"date\":\"2025-08-17\"," +
                "\"startTime\":\"14:00:00\"," +
                "\"endTime\":\"17:00:00\"," +
                "\"isDoubles\":false}";
        testUserApi(post("/api/reservation"), jsonBody, status().isOk());
        testUserApi(post("/api/reservation"), jsonBody, status().isBadRequest());
    }



    private void testUserApi(MockHttpServletRequestBuilder requestBuilder,
                              String jsonBody,
                              ResultMatcher expectedResult
    ) throws Exception {
        mockMvc.perform(requestBuilder
                        .header("Authorization", "Bearer " + userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(expectedResult);
    }
}
