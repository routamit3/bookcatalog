package com.bookcatalog.controller;

import com.bookcatalog.dto.WeatherDTO;
import com.bookcatalog.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

import java.net.InetAddress;

@Controller
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather")
    public String weatherPage(
            @RequestParam(required = false) String city,
            HttpServletRequest request,
            Model model) {
        String clientIp = request.getRemoteAddr();
        WeatherDTO weather = city == null || city.isBlank()
            ? weatherService.getWeatherByIp(clientIp)
            : weatherService.getWeatherByCity(city.trim());
        model.addAttribute("weather", weather);
        model.addAttribute("clientIp", clientIp);
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