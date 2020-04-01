package com.emeq.counter.service;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.emeq.counter.dto.FoodDTO;
import com.emeq.counter.exception.RecordNotFoundException;
import com.emeq.counter.model.Food;
import com.emeq.counter.repository.FoodRepository;

@Service
public class FoodService {

    @Value("${api.base.url}")
    private String apiBaseUrl;

    @Autowired
    private FoodRepository foodRepository;

    private static final String EXCEPTION_STRING = "Invalid Food Id : ";

    public ResponseEntity<List<FoodDTO>> index() {
        Iterable<Food> foods = foodRepository.findAll();

        List<FoodDTO> result = StreamSupport
                .stream(foods.spliterator(), false)
                .map(Food::convert2DTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<FoodDTO> show(final Long id) {
        Food food = foodRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException(EXCEPTION_STRING + id));

        return ResponseEntity.ok(Food.convert2DTO(food));
    }

    public ResponseEntity<FoodDTO> create(final FoodDTO foodDTO) {
        final Food food = new Food();
        food.setName(foodDTO.getName());
        FoodDTO result = Food.convert2DTO(foodRepository.save(food));

        URI location = URI.create(apiBaseUrl + "/foods/" + result.getId());

        return ResponseEntity.created(location).body(result);
    }

    public ResponseEntity<FoodDTO> update(final Long id, final FoodDTO foodDTO) {
        if (!foodRepository.existsById(id))
            throw new RecordNotFoundException(EXCEPTION_STRING + id);

        final Food food = new Food();
        food.setId(id);
        food.setName(foodDTO.getName());
        FoodDTO result = Food.convert2DTO(foodRepository.save(food));

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<Object> destroy(final Long id) {
        if (!foodRepository.existsById(id))
            throw new RecordNotFoundException(EXCEPTION_STRING + id);

        foodRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}