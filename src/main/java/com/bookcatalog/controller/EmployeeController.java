package com.bookcatalog.controller;

import com.bookcatalog.dto.BookDTO;
import com.bookcatalog.dto.WeatherDTO;
import com.bookcatalog.entity.Book;
import com.bookcatalog.entity.User;
import com.bookcatalog.service.BookService;
import com.bookcatalog.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
public class EmployeeController {
    
    private final BookService bookService;
    private final WeatherService weatherService;
    
    @GetMapping
    public String employeeDashboard(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        var books = bookService.convertToDTO(bookService.getAllBooks(PageRequest.of(0, 20)));
        WeatherDTO weather = weatherService.getWeatherByCity("New York");
        
        model.addAttribute("user", user);
        model.addAttribute("books", books.getContent());
        model.addAttribute("weather", weather);
        model.addAttribute("totalBooks", bookService.getAllBooks().size());
        
        return "employee/dashboard";
    }
    
    @GetMapping("/inventory")
    public String manageInventory(Model model) {
        var books = bookService.convertToDTO(bookService.getAllBooks(PageRequest.of(0, 50)));
        WeatherDTO weather = weatherService.getWeatherByCity("New York");
        
        model.addAttribute("books", books.getContent());
        model.addAttribute("weather", weather);
        
        return "employee/inventory";
    }
    
    @GetMapping("/add-book")
    public String addBookPage(Model model) {
        WeatherDTO weather = weatherService.getWeatherByCity("New York");
        model.addAttribute("book", new BookDTO());
        model.addAttribute("weather", weather);
        model.addAttribute("categories", new String[]{"Fiction", "Non-Fiction", "Science", "History", "Biography"});
        
        return "employee/add-book";
    }
    
    @PostMapping("/add-book")
    public String addBook(@ModelAttribute BookDTO bookDTO,
                         RedirectAttributes redirectAttributes) {
        try {
            Book book = new Book();
            book.setTitle(bookDTO.getTitle());
            book.setAuthor(bookDTO.getAuthor());
            book.setDescription(bookDTO.getDescription());
            book.setPrice(bookDTO.getPrice());
            book.setIsbn(bookDTO.getIsbn());
            book.setCategory(bookDTO.getCategory());
            book.setInventory(bookDTO.getInventory());
            
            bookService.createBook(book);
            redirectAttributes.addFlashAttribute("success", "Book added successfully!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add book: " + e.getMessage());
        }
        
        return "redirect:/employee/inventory";
    }
    
    @GetMapping("/edit-book/{id}")
    public String editBookPage(@PathVariable Long id, Model model) {
        var book = bookService.getBookById(id);
        if (book.isPresent()) {
            model.addAttribute("book", bookService.convertToDTO(book.get()));
            WeatherDTO weather = weatherService.getWeatherByCity("New York");
            model.addAttribute("weather", weather);
            model.addAttribute("categories", new String[]{"Fiction", "Non-Fiction", "Science", "History", "Biography"});
            return "employee/edit-book";
        }
        return "redirect:/employee/inventory";
    }
    
    @PostMapping("/edit-book/{id}")
    public String editBook(@PathVariable Long id,
                          @ModelAttribute BookDTO bookDTO,
                          RedirectAttributes redirectAttributes) {
        try {
            var bookOptional = bookService.getBookById(id);
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                book.setTitle(bookDTO.getTitle());
                book.setAuthor(bookDTO.getAuthor());
                book.setDescription(bookDTO.getDescription());
                book.setPrice(bookDTO.getPrice());
                book.setIsbn(bookDTO.getIsbn());
                book.setCategory(bookDTO.getCategory());
                book.setInventory(bookDTO.getInventory());
                
                bookService.updateBook(id, book);
                redirectAttributes.addFlashAttribute("success", "Book updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update book: " + e.getMessage());
        }
        
        return "redirect:/employee/inventory";
    }
    
    @PostMapping("/delete-book/{id}")
    public String deleteBook(@PathVariable Long id,
                            RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("success", "Book deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete book");
        }
        
        return "redirect:/employee/inventory";
    }
    
    @PostMapping("/update-inventory/{id}")
    public String updateInventory(@PathVariable Long id,
                                 @RequestParam Integer quantity,
                                 RedirectAttributes redirectAttributes) {
        try {
            var bookOptional = bookService.getBookById(id);
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                book.setInventory(quantity);
                bookService.updateBook(id, book);
                redirectAttributes.addFlashAttribute("success", "Inventory updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update inventory");
        }
        
        return "redirect:/employee/inventory";
    }
}
