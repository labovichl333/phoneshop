package com.es.phoneshop.web.controller.pages.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExeptionDto {
    @JsonProperty("error")
    private String exeptionMessage;
}
