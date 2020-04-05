package com.emeq.counter.repository;

import org.springframework.data.repository.CrudRepository;

import com.emeq.counter.model.Food;

public interface FoodRepository extends CrudRepository<Food, Long> {
}
