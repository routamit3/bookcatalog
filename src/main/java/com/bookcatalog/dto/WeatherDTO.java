package com.bookcatalog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDTO {
    private String city;
    private double temperature;
    private String description;
    private int humidity;
    private double windSpeed;
    private String icon;
}
