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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminTests {

    @Autowired
    private MockMvc mockMvc;

    private static String adminAccessToken;

    @BeforeAll
    public void loginAdmin() throws Exception {
        // Perform login to get JWT token
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phoneNumber\":\"987654321\", \"password\":\"admin\"}"))
                .andExpect(status().isOk())
                .andReturn();

        // Extract the JWT token from the response
        String response = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseJson = mapper.readTree(response);

        adminAccessToken = responseJson.get("access_token").asText();

    }

    /* ---------------------------------- Court tests ---------------------------------- */

    //GET
    @Test
    void getALlCourts() throws Exception {
        testAdminApi(get("/api/court"), "", status().isOk());
    }

    @Test
    void getCourtById() throws Exception {
        testAdminApi(get("/api/court/1"), "", status().isOk());
    }

    @Test
    void getCourtByIdNotFound() throws Exception {
        testAdminApi(get("/api/court/9999"), "", status().isBadRequest());
    }

    // POST
    @Test
    void saveCourt() throws Exception {
        String jsonBody = "{\"surface\":\"CLAY\",\"price\":225}";
        testAdminApi(post("/api/court"), jsonBody, status().isOk());
    }

    @Test
    void saveCourtWithInvalidSurface() throws Exception {
        String jsonBody = "{\"surface\":\"asdsda\",\"price\":225}";
        testAdminApi(post("/api/court"), jsonBody, status().isBadRequest());
    }

    @Test
    void saveCourtWithInvalidPrice() throws Exception {
        String jsonBody = "{\"surface\":\"CLAY\",\"price\":-110000}";
        testAdminApi(post("/api/court"), jsonBody, status().isBadRequest());
    }


    // PUT
    @Test
    void updateCourt() throws Exception {
        String jsonBody = "{\"courtId\":\"3\",\"surface\":\"CLAY\",\"price\":159.55}";
        testAdminApi(put("/api/court"), jsonBody, status().isOk());
    }

    @Test
    void updateCourtNotFound() throws Exception {
        String jsonBody = "{\"courtId\":\"9999\",\"surface\":\"CLAY\",\"price\":159.55}";
        testAdminApi(put("/api/court"), jsonBody, status().isBadRequest());
    }

    @Test
    void updateCourtWithInvalidSurface() throws Exception {
        String jsonBody = "{\"courtId\":\"2\",\"surface\":\"asdsda\",\"price\":159.55}";
        testAdminApi(put("/api/court"), jsonBody, status().isBadRequest());
    }

    @Test
    void updateCourtWithInvalidPrice() throws Exception {
        String jsonBody = "{\"courtId\":\"2\",\"surface\":\"CLAY\",\"price\":-110000}";
        testAdminApi(put("/api/court"), jsonBody, status().isBadRequest());
    }

    // DELETE
    @Test
    void deleteCourt() throws Exception {
        String jsonBody = "{\"courtId\":\"2\"}";
        testAdminApi(delete("/api/court"), jsonBody, status().isOk());
        testAdminApi(get("/api/court/2"), "", status().isBadRequest());
    }

    /* ---------------------------------- User tests ---------------------------------- */

    // GET
    @Test
    void getUser() throws Exception {
        testAdminApi(get("/api/user/123456789"), "", status().isOk());
    }

    @Test
    void getUserNotFound() throws Exception {
        testAdminApi(get("/api/user/999999999"), "", status().isBadRequest());
    }

    // POST
    @Test
    void saveUser() throws Exception {
        String jsonBody = "{\"phoneNumber\":\"222222222\"," +
                "\"name\":\"user2\"," +
                "\"password\":\"user2\"," +
                "\"role\":\"USER\"}";
        testAdminApi(post("/api/user"), jsonBody, status().isOk());
    }

    @Test
    void saveUserWithInvalidRole() throws Exception {
        String jsonBody = "{\"phoneNumber\":\"333333333\"," +
                "\"name\":\"user3\"," +
                "\"password\":\"user3\"," +
                "\"role\":\"ADMIN\"}";
        testAdminApi(post("/api/user"), jsonBody, status().isBadRequest());
    }

    // PUT
    @Test
    void updateUser() throws Exception {
        String jsonBody = "{\"phoneNumber\":\"88888888\"," +
                "\"name\":\"user4\"," +
                "\"password\":\"user4\"," +
                "\"role\":\"USER\"}";
        testAdminApi(post("/api/user"), jsonBody, status().isOk());

        jsonBody = "{\"phoneNumber\":\"88888888\"," +
                "\"name\":\"user5\"," +
                "\"password\":\"user1\"}";
        testAdminApi(put("/api/user"), jsonBody, status().isOk());
    }

    @Test
    void updateUserNotFound() throws Exception {
        String jsonBody = "{\"phoneNumber\":\"999999999\"," +
                "\"name\":\"user4\"," +
                "\"password\":\"user4\"," +
                "\"role\":\"USER\"}";
        testAdminApi(put("/api/user"), jsonBody, status().isBadRequest());
    }

    // DELETE
    @Test
    void deleteUser() throws Exception {
        String jsonBody = "{\"phoneNumber\":\"777777777\"," +
                "\"name\":\"user4\"," +
                "\"password\":\"user4\"," +
                "\"role\":\"USER\"}";
        testAdminApi(post("/api/user"), jsonBody, status().isOk());

        jsonBody = "{\"phoneNumber\":\"777777777\"}";
        testAdminApi(delete("/api/user"), jsonBody, status().isOk());
        testAdminApi(get("/api/user/777777777"), "", status().isBadRequest());
    }

    @Test
    void deleteUserNotFound() throws Exception {
        String jsonBody = "{\"phoneNumber\":\"999999999\"," +
                "\"name\":\"user4\"," +
                "\"password\":\"user4\"," +
                "\"role\":\"USER\"}";
        testAdminApi(delete("/api/user"), jsonBody, status().isBadRequest());
    }

    /* ---------------------------------- Reservation tests ---------------------------------- */
    // GET
    @Test
    void getReservationByPhone() throws Exception {
        String jsonBody = "{\"phoneNumber\":123456789,\"futureOnly\":false}";
        testAdminApi(get("/api/reservation/phone"), jsonBody, status().isOk());

        jsonBody = "{\"phoneNumber\":123456789,\"futureOnly\":true}";
        testAdminApi(get("/api/reservation/phone"), jsonBody, status().isOk());
    }

    @Test
    void getReservationByPhoneNotFound() throws Exception {
        String jsonBody = "{\"phoneNumber\":999999999,\"futureOnly\":false}";
        testAdminApi(get("/api/reservation/phone"), jsonBody, status().isBadRequest());
    }

    @Test
    void getReservationByCourtId() throws Exception {
        String jsonBody = "{\"courtId\":3,\"futureOnly\":false}";
        testAdminApi(get("/api/reservation/court"), jsonBody, status().isOk());

        jsonBody = "{\"courtId\":3,\"futureOnly\":true}";
        testAdminApi(get("/api/reservation/court"), jsonBody, status().isOk());
    }

    @Test
    void getReservationByCourtIdNotFound() throws Exception {
        String jsonBody = "{\"courtId\":9999,\"futureOnly\":false}";
        testAdminApi(get("/api/reservation/court"), jsonBody, status().isBadRequest());
    }

    // POST
    @Test
    void saveReservationWithKnownUser() throws Exception {
        String jsonBody = "{\"user\":{\"phoneNumber\":\"123456789\"}," +
                "\"courtId\":3," +
                "\"date\":\"2024-12-30\"," +
                "\"startTime\":\"15:00:00\"," +
                "\"endTime\":\"16:00:00\"," +
                "\"isDoubles\":false}";
        testAdminApi(post("/api/reservation"), jsonBody, status().isOk());
    }

    @Test
    void saveReservationWithNewUser() throws Exception {
        String jsonBody = "{\"user\":" +
                "{\"phoneNumber\":\"333333333\"," +
                "\"name\":\"user3\"," +
                "\"password\":\"user3\"}," +
                "\"courtId\":3," +
                "\"date\":\"2024-12-30\"," +
                "\"startTime\":\"12:00:00\"," +
                "\"endTime\":\"15:00:00\"," +
                "\"isDoubles\":false}";
        testAdminApi(post("/api/reservation"), jsonBody, status().isOk());
    }

    @Test
    void saveReservationWhereTimesCollide() throws Exception {
        String jsonBody = "{\"user\":{\"phoneNumber\":\"123456789\"}," +
                "\"courtId\":3," +
                "\"date\":\"2025-08-29\"," +
                "\"startTime\":\"14:00:00\"," +
                "\"endTime\":\"17:00:00\"," +
                "\"isDoubles\":false}";
        testAdminApi(post("/api/reservation"), jsonBody, status().isOk());
        testAdminApi(post("/api/reservation"), jsonBody, status().isBadRequest());
    }

    // PUT
    @Test
    void updateReservationNotFound() throws Exception {
        String jsonBody = "{\"reservationId\":9999," +
                "\"phoneNumber\":\"123456789\"," +
                "\"courtId\":1," +
                "\"date\":\"2025-08-08\"," +
                "\"startTime\":\"10:00:00\"," +
                "\"endTime\":\"12:00:00\"," +
                "\"isDoubles\":true}";
        testAdminApi(put("/api/reservation"), jsonBody, status().isBadRequest());
    }

    // DELETE
    @Test
    void deleteReservation() throws Exception {
        String jsonBody = "{\"reservationId\":1}";
        testAdminApi(delete("/api/reservation"), jsonBody, status().isOk());
        testAdminApi(get("/api/reservation/1"), "", status().isBadRequest());
    }



    private void testAdminApi(MockHttpServletRequestBuilder requestBuilder,
                              String jsonBody,
                              ResultMatcher expectedResult
    ) throws Exception {
        mockMvc.perform(requestBuilder
                        .header("Authorization", "Bearer " + adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(expectedResult);
    }



}
