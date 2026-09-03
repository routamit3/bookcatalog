package com.bookcatalog.controller;

import com.bookcatalog.dto.UserRegistrationDTO;
import com.bookcatalog.dto.WeatherDTO;
import com.bookcatalog.service.UserService;
import com.bookcatalog.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    private final WeatherService weatherService;
    
    @GetMapping("/login")
    public String loginPage(Model model) {
        WeatherDTO weather = weatherService.getWeatherByCity("New York");
        model.addAttribute("weather", weather);
        return "login";
    }
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        WeatherDTO weather = weatherService.getWeatherByCity("New York");
        model.addAttribute("userRegistration", new UserRegistrationDTO());
        model.addAttribute("weather", weather);
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserRegistrationDTO registrationDTO,
                              RedirectAttributes redirectAttributes) {
        try {
            // Validate form
            if (registrationDTO.getUsername() == null || registrationDTO.getUsername().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Username is required");
                return "redirect:/register";
            }
            
            if (registrationDTO.getPassword() == null || registrationDTO.getPassword().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Password is required");
                return "redirect:/register";
            }
            
            if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match");
                return "redirect:/register";
            }
            
            if (registrationDTO.getEmail() == null || !registrationDTO.getEmail().contains("@")) {
                redirectAttributes.addFlashAttribute("error", "Valid email is required");
                return "redirect:/register";
            }
            
            userService.registerUser(registrationDTO);
            redirectAttributes.addFlashAttribute("success", 
                "Registration successful! Please login with your credentials.");
            
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/register";
        }
        
        return "redirect:/login";
    }
    
    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        WeatherDTO weather = weatherService.getWeatherByCity("New York");
        model.addAttribute("weather", weather);
        return "access-denied";
    }
}
