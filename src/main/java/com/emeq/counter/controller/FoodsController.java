package com.emeq.counter.controller;

import java.util.List;

import com.emeq.counter.dto.FoodDTO;
import com.emeq.counter.dto.MessageDTO;
import com.emeq.counter.service.FoodService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/foods")
public class FoodsController {

    @Autowired
    private FoodService foodService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<FoodDTO>> index() {
        return foodService.index();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<FoodDTO> show(@PathVariable final Long id) {
        return foodService.show(id);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<FoodDTO> create(@RequestBody final FoodDTO foodDTO) {
        return foodService.create(foodDTO);
    }

    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<MessageDTO> update(@PathVariable final Long id, @RequestBody final FoodDTO foodDTO) {
        return foodService.update(id, foodDTO);
    }

    @DeleteMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<MessageDTO> destroy(@PathVariable final Long id) {
        return foodService.destroy(id);
    }
}
