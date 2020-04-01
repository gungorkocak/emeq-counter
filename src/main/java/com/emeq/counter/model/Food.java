package com.emeq.counter.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import lombok.Data;

import com.emeq.counter.dto.FoodDTO;

@Data
@Entity(name = "foods")
public class Food {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    public FoodDTO toDTO() {
        final FoodDTO foodDTO = new FoodDTO();
        BeanUtils.copyProperties(this, foodDTO);
        return foodDTO;
    }
}
