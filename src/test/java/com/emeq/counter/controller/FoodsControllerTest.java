package com.emeq.counter.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.emeq.counter.dto.FoodDTO;
import com.emeq.counter.dto.MessageDTO;
import com.emeq.counter.exception.RecordNotFoundException;
import com.emeq.counter.model.Food;
import com.emeq.counter.repository.FoodRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class FoodsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FoodsController controller;

    @MockBean
    private FoodRepository foodRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void contexLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void allFoods() {
        Food foods[] = { createFood(1, "banana"), createFood(2, "apple"), createFood(3, "orange") };
        Iterable<Food> iterable = Arrays.asList(foods);

        when(foodRepository.findAll()).thenReturn(iterable);

        ResponseEntity<FoodDTO[]> response = this.restTemplate.getForEntity(endpoint("/foods"), FoodDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(mapper.convertValue(response.getBody(), JsonNode.class))
                .isEqualTo(mapper.convertValue(convertDtos(iterable), JsonNode.class));
    }

    @Test
    public void getFood() {
        Food food = createFood(1, "banana");
        when(foodRepository.findById((long) 1)).thenReturn(Optional.of(food));

        ResponseEntity<FoodDTO> response = this.restTemplate.getForEntity(endpoint("/foods/1"), FoodDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(mapper.convertValue(response.getBody(), JsonNode.class))
                .isEqualTo(mapper.convertValue(Food.convert2DTO(food), JsonNode.class));
    }

    @Test
    public void createFood() {
        Food savedFood = createFood(10, "banana");
        when(foodRepository.save(createFood("banana"))).thenReturn(savedFood);
        FoodDTO foodDTO = Food.convert2DTO(savedFood);
        ResponseEntity<FoodDTO> response = this.restTemplate.postForEntity(endpoint("/foods"), foodDTO, FoodDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/foods/" + savedFood.getId());

        assertThat(mapper.convertValue(response.getBody(), JsonNode.class))
                .isEqualTo(mapper.convertValue(foodDTO, JsonNode.class));
    }

    @Test
    public void updateFood() throws URISyntaxException {
        Food updatedFood = createFood(1, "apple");
        FoodDTO foodDTO = Food.convert2DTO(updatedFood);

        when(foodRepository.existsById(any(Long.class))).thenThrow(RecordNotFoundException.class).thenReturn(true);
        doReturn(updatedFood).when(foodRepository).save(updatedFood);

        RequestEntity<FoodDTO> request = RequestEntity.put(new URI(endpoint("/foods/" + updatedFood.getId())))
                .headers(valid_content_type()).body(foodDTO);

        ResponseEntity<MessageDTO> responseNotFound = this.restTemplate.exchange(request, MessageDTO.class);
        ResponseEntity<MessageDTO> responseSuccess = this.restTemplate.exchange(request, MessageDTO.class);

        assertThat(responseNotFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseSuccess.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteFood() throws URISyntaxException {
        Food food = createFood(1, "banana");

        when(foodRepository.existsById(any(Long.class))).thenReturn(true).thenThrow(RecordNotFoundException.class);
        doNothing().when(foodRepository).deleteById(food.getId());

        RequestEntity<Void> request = RequestEntity.delete(new URI(endpoint("/foods/" + food.getId())))
                .headers(valid_content_type()).build();

        ResponseEntity<MessageDTO> responseSuccess = this.restTemplate.exchange(request, MessageDTO.class);
        ResponseEntity<MessageDTO> responseNotFound = this.restTemplate.exchange(request, MessageDTO.class);

        assertThat(responseSuccess.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseNotFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private String endpoint(String suffix) {
        return "http://localhost:" + port + suffix;
    }

    private List<FoodDTO> convertDtos(Iterable<Food> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).map(Food::convert2DTO).collect(Collectors.toList());
    }

    private HttpHeaders valid_content_type() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private Food createFood(int id, String name) {
        Food food = new Food();
        food.setId((long) id);
        food.setName(name);
        return food;
    }

    private Food createFood(String name) {
        Food food = new Food();
        food.setName(name);
        return food;
    }
}
