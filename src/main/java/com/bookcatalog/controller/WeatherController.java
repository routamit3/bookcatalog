package com.bookcatalog.controller;

import com.bookcatalog.dto.WeatherDTO;
import com.bookcatalog.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather")
    public String weatherPage(
            @RequestParam(required = false) String weatherIp,
            Model model) {
        WeatherDTO weather = weatherIp == null || weatherIp.isBlank()
            ? weatherService.getWeatherByCity("New York")
            : weatherService.getWeatherFromVm(weatherIp.trim());
        model.addAttribute("weather", weather);
        model.addAttribute("weatherIp", weatherIp == null ? "" : weatherIp);
        return "weather";
    }
}