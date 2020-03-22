package com.emeq.counter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Hello {

    @JsonProperty("hello")
    private final String content = "world";

	  public String getContent() {
        return content;
	  }
}
