package com.bookcatalog.controller;

import com.bookcatalog.dto.WeatherDTO;
import com.bookcatalog.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.InetAddress;

@Controller
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather")
    public String weatherPage(
            @RequestParam(defaultValue = "New York") String city,
            Model model) {
        WeatherDTO weather = weatherService.getWeatherByCity(city.trim());
        model.addAttribute("weather", weather);
        model.addAttribute("weatherServerIp", weatherService.getWeatherServerIp());
        model.addAttribute("applicationServerIp", getApplicationServerIp());
        return "weather";
    }

    private String getApplicationServerIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "Unavailable";
        }
    }
}