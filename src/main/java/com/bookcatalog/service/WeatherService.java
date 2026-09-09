package com.bookcatalog.service;

import com.bookcatalog.dto.WeatherDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.URI;

@Service
@RequiredArgsConstructor
public class WeatherService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Value("${weather.api.key:demo}")
    private String apiKey;
    
    @Value("${weather.api.url:https://api.openweathermap.org/data/2.5/weather}")
    private String apiUrl;

    @Value("${weather.geolocation.url:https://ipapi.co}")
    private String geolocationUrl;

    public String getWeatherServerIp() {
        try {
            return InetAddress.getByName(URI.create(apiUrl).getHost()).getHostAddress();
        } catch (Exception e) {
            return "Unavailable";
        }
    }
    
    public WeatherDTO getWeatherByCity(String city) {
        try {
            // Using Open Weather Map API (free tier)
            String url = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, city, apiKey);
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            WeatherDTO weather = new WeatherDTO();
            weather.setCity(city);
            weather.setTemperature(jsonNode.get("main").get("temp").asDouble());
            weather.setDescription(jsonNode.get("weather").get(0).get("main").asText());
            weather.setHumidity(jsonNode.get("main").get("humidity").asInt());
            weather.setWindSpeed(jsonNode.get("wind").get("speed").asDouble());
            weather.setIcon(jsonNode.get("weather").get(0).get("icon").asText());
            addOutfitAdvice(weather);
            
            return weather;
        } catch (Exception e) {
            // Return default weather data if API fails
            WeatherDTO defaultWeather = new WeatherDTO();
            defaultWeather.setCity(city);
            defaultWeather.setTemperature(20.0);
            defaultWeather.setDescription("Weather data unavailable");
            defaultWeather.setHumidity(0);
            defaultWeather.setWindSpeed(0.0);
            defaultWeather.setIcon("02d");
            addOutfitAdvice(defaultWeather);
            return defaultWeather;
        }
    }

    public WeatherDTO getWeatherByIp(String ipAddress) {
        try {
            String url = String.format("%s/%s/json/", geolocationUrl, ipAddress);
            String response = restTemplate.getForObject(url, String.class);
            JsonNode location = objectMapper.readTree(response);
            double latitude = location.get("latitude").asDouble();
            double longitude = location.get("longitude").asDouble();
            return getWeatherByCoordinates(latitude, longitude);
        } catch (Exception e) {
            return getWeatherByCity("New York");
        }
    }
    
    public WeatherDTO getWeatherByCoordinates(double latitude, double longitude) {
        try {
            String url = String.format("%s?lat=%f&lon=%f&appid=%s&units=metric", 
                apiUrl, latitude, longitude, apiKey);
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            WeatherDTO weather = new WeatherDTO();
            weather.setCity(jsonNode.get("name").asText());
            weather.setTemperature(jsonNode.get("main").get("temp").asDouble());
            weather.setDescription(jsonNode.get("weather").get(0).get("main").asText());
            weather.setHumidity(jsonNode.get("main").get("humidity").asInt());
            weather.setWindSpeed(jsonNode.get("wind").get("speed").asDouble());
            weather.setIcon(jsonNode.get("weather").get(0).get("icon").asText());
            addOutfitAdvice(weather);
            
            return weather;
        } catch (Exception e) {
            WeatherDTO defaultWeather = new WeatherDTO();
            defaultWeather.setCity("Unknown");
            defaultWeather.setTemperature(20.0);
            defaultWeather.setDescription("Weather data unavailable");
            defaultWeather.setHumidity(0);
            defaultWeather.setWindSpeed(0.0);
            defaultWeather.setIcon("02d");
            addOutfitAdvice(defaultWeather);
            return defaultWeather;
        }
    }

    private void addOutfitAdvice(WeatherDTO weather) {
        weather.setCoatAdvice(weather.getTemperature() < 15
            ? "Bring a coat or warm layer."
            : "A coat is not needed for most visitors.");
        String description = weather.getDescription() == null
            ? ""
            : weather.getDescription().toLowerCase();
        weather.setUmbrellaAdvice(description.contains("rain") || description.contains("drizzle")
            || description.contains("thunderstorm")
            ? "Bring an umbrella today."
            : "An umbrella is probably not needed today.");
    }
}
