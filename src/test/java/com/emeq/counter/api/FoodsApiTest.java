package com.emeq.counter.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.emeq.counter.model.Food;
import com.emeq.counter.repository.FoodRepository;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FoodsApiTest {

    @Value("${server.schema}://${server.host}:${local.server.port}")
    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ObjectMapper mapper;

    private final String[] fruits = { "Apple", "Avocado", "Kiwi", "Lime", "Orange", "Watrmelon" };

    @BeforeEach
    public void init() {
        for (String fruit : fruits) {
            foodRepository.save(createFood(fruit));
        }
    }

    @Test
    public void allFoods() {
        ArrayNode json = fruitsAsArrayNode();
        final ResponseEntity<ArrayNode> response = this.restTemplate.getForEntity(endpoint("/foods"), ArrayNode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().toString()).isEqualTo(json.toString());
    }

    @Test
    public void getFood() {
        JsonNode json = mapper.createObjectNode();
        ((ObjectNode) json).put("id", 1);
        ((ObjectNode) json).put("name", fruits[0]);

        final ResponseEntity<JsonNode> response = this.restTemplate.getForEntity(endpoint("/foods/1"), JsonNode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().toString()).isEqualTo(json.toString());
    }

    @Test
    public void createFood() {
        JsonNode request = mapper.createObjectNode();
        ((ObjectNode) request).put("name", "Banana");

        JsonNode json = mapper.createObjectNode();
        ((ObjectNode) json).put("id", 7);
        ((ObjectNode) json).put("name", "Banana");

        ResponseEntity<JsonNode> response = this.restTemplate.postForEntity(endpoint("/foods"), json, JsonNode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/foods/7");
        assertThat(response.getBody().toString()).isEqualTo(json.toString());
    }

    @Test
    public void updateSuccess() throws URISyntaxException {
        JsonNode requestJson = mapper.createObjectNode();
        ((ObjectNode) requestJson).put("name", "Watrmelon");

        JsonNode json = mapper.createObjectNode();
        ((ObjectNode) json).put("id", 6);
        ((ObjectNode) json).put("name", "Watrmelon");

        RequestEntity<JsonNode> requestSuccess = RequestEntity
                .put(new URI(endpoint("/foods/6")))
                .headers(valid_content_type())
                .body(requestJson);

        ResponseEntity<JsonNode> responseSuccess = this.restTemplate.exchange(requestSuccess, JsonNode.class);

        assertThat(responseSuccess.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseSuccess.getBody().toString()).isEqualTo(json.toString());
    }

    @Test
    public void updateNotFound() throws URISyntaxException {
        JsonNode requestJson = mapper.createObjectNode();
        ((ObjectNode) requestJson).put("name", "Watrmelon");

        RequestEntity<JsonNode> requestNotFound = RequestEntity.put(new URI(endpoint("/foods/10")))
                .headers(valid_content_type()).body(requestJson);

        ResponseEntity<JsonNode> responseNotFound = this.restTemplate.exchange(requestNotFound, JsonNode.class);

        assertThat(responseNotFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteSuccessFood() throws URISyntaxException {
        RequestEntity<Void> request = RequestEntity
                .delete(new URI(endpoint("/foods/6")))
                .headers(valid_content_type())
                .build();
        ResponseEntity<Object> responseSuccess = this.restTemplate.exchange(request, Object.class);

        assertThat(responseSuccess.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    public void deleteNotFound() throws URISyntaxException {
        RequestEntity<Void> request = RequestEntity
                .delete(new URI(endpoint("/foods/6")))
                .headers(valid_content_type())
                .build();
        ResponseEntity<Object> responseNotFound = this.restTemplate.exchange(request, Object.class);

        assertThat(responseNotFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private String endpoint(final String suffix) {
        return baseUrl + suffix;
    }

    private HttpHeaders valid_content_type() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private Food createFood(String name) {
        Food food = new Food();
        food.setName(name);
        return food;
    }

    private ArrayNode fruitsAsArrayNode() {
        ArrayNode json = mapper.createArrayNode();
        JsonNode child;
        for (int i = 0; i < fruits.length; i++) {
            child = mapper.createObjectNode();
            ((ObjectNode) child).put("id", i + 1);
            ((ObjectNode) child).put("name", fruits[i]);
            json.add(child);
        }
        return json;
    }
}
