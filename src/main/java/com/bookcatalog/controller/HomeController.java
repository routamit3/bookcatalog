package com.bookcatalog.controller;

import com.bookcatalog.dto.WeatherDTO;
import com.bookcatalog.service.BookService;
import com.bookcatalog.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private static final String[] CATEGORIES = {
        "Fiction", "Non-Fiction", "Science", "History", "Biography"
    };

    private final BookService bookService;
    private final WeatherService weatherService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("books", bookService.convertToDTO(bookService.getAllBooks(PageRequest.of(0, 3))).getContent());
        model.addAttribute("weather", weatherService.getWeatherByCity("New York"));
        return "index";
    }

    @GetMapping("/books")
    public String books(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        var books = category == null || category.isBlank()
            ? bookService.getAllBooks(PageRequest.of(Math.max(page, 0), 6))
            : bookService.getBooksByCategory(category, PageRequest.of(Math.max(page, 0), 6));

        model.addAttribute("books", bookService.convertToDTO(books));
        model.addAttribute("categories", CATEGORIES);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("weather", weatherService.getWeatherByCity("New York"));
        return "books";
    }

    @GetMapping("/search")
    public String search(@RequestParam String query, Model model) {
        var books = bookService.convertToDTO(
            bookService.searchByTitleOrAuthor(query, PageRequest.of(0, 6)));
        model.addAttribute("books", books);
        model.addAttribute("categories", CATEGORIES);
        model.addAttribute("selectedCategory", null);
        model.addAttribute("weather", weatherService.getWeatherByCity("New York"));
        return "books";
    }

    @GetMapping("/book-detail")
    public String bookDetail(@RequestParam Long id, Model model) {
        bookService.getBookById(id).ifPresent(book ->
            model.addAttribute("book", bookService.convertToDTO(book)));
        model.addAttribute("weather", weatherService.getWeatherByCity("New York"));
        return "book-detail";
    }
}
