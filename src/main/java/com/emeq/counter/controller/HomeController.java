package com.emeq.counter.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  @GetMapping("/hello")
  public JsonNode hello() {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode json = mapper.createObjectNode();
    ((ObjectNode)json).put("hello", "world");

    return json;
  }
}
