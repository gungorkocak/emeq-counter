package com.emeq.counter.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HomeControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private HomeController controller;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void contexLoads() throws Exception {
    assertThat(controller).isNotNull();
  }

  @Test
  public void indexShouldReturnHelloWorld() throws Exception {
    String endpoint = "http://localhost:" + port + "/hello";
    ObjectMapper mapper = new ObjectMapper();
    JsonNode json = mapper.createObjectNode();
    ((ObjectNode)json).put("hello", "world");

    assertThat(this.restTemplate.getForObject(endpoint, JsonNode.class))
        .isEqualTo(json);
  }
}
