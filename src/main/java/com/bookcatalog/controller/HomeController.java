package com.bookcatalog.controller;

import com.bookcatalog.dto.BookDTO;
import com.bookcatalog.dto.WeatherDTO;
import com.bookcatalog.service.BookService;
import com.bookcatalog.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final BookService bookService;
    private final WeatherService weatherService;
    
    @GetMapping("/")
    public String home(Model model) {
        Page<BookDTO> books = bookService.convertToDTO(bookService.getAllBooks(PageRequest.of(0, 6)));
        WeatherDTO weather = weatherService.getWeatherByCity("New York");
        
        model.addAttribute("books", books.getContent());
        model.addAttribute("weather", weather);
        return "index";
    }
    
    @GetMapping("/home")
    public String homePage(Model model) {
        return home(model);
    }
    
    @GetMapping("/books")
    public String browseBooks(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "12") int size,
                             @RequestParam(required = false) String category,
                             Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookDTO> books;
        
        if (category != null && !category.isEmpty()) {
            books = bookService.convertToDTO(bookService.getBooksByCategory(category, pageable));
            model.addAttribute("selectedCategory", category);
        } else {
            books = bookService.convertToDTO(bookService.getAllBooks(pageable));
        }
        
        WeatherDTO weather = weatherService.getWeatherByCity("New York");
        
        model.addAttribute("books", books);
        model.addAttribute("weather", weather);
        model.addAttribute("categories", new String[]{"Fiction", "Non-Fiction", "Science", "History", "Biography"});
        
        return "books";
    }
    
    @GetMapping("/book-detail")
    public String bookDetail(@RequestParam Long id, Model model) {
        var book = bookService.getBookById(id);
        if (book.isPresent()) {
            model.addAttribute("book", bookService.convertToDTO(book.get()));
            WeatherDTO weather = weatherService.getWeatherByCity("New York");
            model.addAttribute("weather", weather);
            return "book-detail";
        }
        return "redirect:/books";
    }
    
    @GetMapping("/search")
    public String search(@RequestParam String query,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "12") int size,
                        Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookDTO> books = bookService.convertToDTO(bookService.searchByTitle(query, pageable));
        WeatherDTO weather = weatherService.getWeatherByCity("New York");
        
        model.addAttribute("books", books);
        model.addAttribute("weather", weather);
        model.addAttribute("searchQuery", query);
        
        return "books";
    }
}
