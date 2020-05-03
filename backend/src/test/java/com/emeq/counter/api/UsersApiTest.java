package com.emeq.counter.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.emeq.counter.dto.UserDTO;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UsersApiTest {

    @Value("${server.schema}://${server.host}:${local.server.port}")
    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void singUpUser() {
        UserDTO requestDTO = new UserDTO("admin@emeq.com", "very-strong-password");

        ResponseEntity<JsonNode> response = this.restTemplate.postForEntity(endpoint("/sign-up"), requestDTO, JsonNode.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void singUpUserwithBadEmail() {
        UserDTO requestDTO = new UserDTO("its_not_email.com", "very-strong-password");

        ResponseEntity<JsonNode> response = this.restTemplate.postForEntity(endpoint("/sign-up"), requestDTO, JsonNode.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        
        JsonNode resp = response.getBody().get("errors").get(0);

        assertThat(resp.at("/field").asText()).isEqualTo("email");
        assertThat(resp.at("/rejectedValue").asText()).isEqualTo("its_not_email.com");
    }

    private String endpoint(final String suffix) {
        return baseUrl + "/users" + suffix;
    }
}
