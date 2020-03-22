package com.emeq.counter.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;

    public MessageDTO() {

    }

    public MessageDTO(String message) {
        super();
        setMessage(message);
    }
}
