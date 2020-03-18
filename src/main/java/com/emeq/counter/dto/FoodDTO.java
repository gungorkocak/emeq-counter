package com.emeq.counter.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonInclude(Include.NON_NULL)
  private Long id;

  private String name;
}
