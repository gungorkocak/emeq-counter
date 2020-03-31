package com.emeq.counter.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import com.emeq.counter.dto.FoodDTO;

import lombok.Data;

@Data
@Entity(name = "foods")
public class Food {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    public static FoodDTO convert2DTO(final Food food) {
        final FoodDTO foodDTO = new FoodDTO();
        BeanUtils.copyProperties(food, foodDTO);
        return foodDTO;
    }
}
