package com.bookcatalog;

import com.bookcatalog.entity.Book;
import com.bookcatalog.entity.User;
import com.bookcatalog.repository.BookRepository;
import com.bookcatalog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@SpringBootApplication
@RequiredArgsConstructor
public class BookCatalogApplication {
    
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    
    public static void main(String[] args) {
        SpringApplication.run(BookCatalogApplication.class, args);
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public CommandLineRunner init(PasswordEncoder passwordEncoder) {
        return args -> {
            // Initialize sample books if table is empty
            if (bookRepository.count() == 0) {
                // Fiction
                Book book1 = new Book();
                book1.setTitle("The Great Gatsby");
                book1.setAuthor("F. Scott Fitzgerald");
                book1.setDescription("A classic American novel about the Jazz Age");
                book1.setPrice(new BigDecimal("12.99"));
                book1.setIsbn("978-0743273565");
                book1.setCategory("Fiction");
                book1.setInventory(20);
                bookRepository.save(book1);
                
                // Non-Fiction
                Book book2 = new Book();
                book2.setTitle("Sapiens");
                book2.setAuthor("Yuval Noah Harari");
                book2.setDescription("A brief history of humankind");
                book2.setPrice(new BigDecimal("18.99"));
                book2.setIsbn("978-0062316097");
                book2.setCategory("Non-Fiction");
                book2.setInventory(15);
                bookRepository.save(book2);
                
                // Science
                Book book3 = new Book();
                book3.setTitle("A Brief History of Time");
                book3.setAuthor("Stephen Hawking");
                book3.setDescription("From the Big Bang to Black Holes");
                book3.setPrice(new BigDecimal("16.99"));
                book3.setIsbn("978-0553380163");
                book3.setCategory("Science");
                book3.setInventory(10);
                bookRepository.save(book3);
                
                // History
                Book book4 = new Book();
                book4.setTitle("The Fall of the Roman Empire");
                book4.setAuthor("Edward Gibbon");
                book4.setDescription("A comprehensive history of Rome's decline");
                book4.setPrice(new BigDecimal("24.99"));
                book4.setIsbn("978-0143039990");
                book4.setCategory("History");
                book4.setInventory(12);
                bookRepository.save(book4);
                
                // Biography
                Book book5 = new Book();
                book5.setTitle("Steve Jobs");
                book5.setAuthor("Walter Isaacson");
                book5.setDescription("The exclusive biography of the Apple founder");
                book5.setPrice(new BigDecimal("17.99"));
                book5.setIsbn("978-1451648539");
                book5.setCategory("Biography");
                book5.setInventory(18);
                bookRepository.save(book5);
                
                // Additional books
                Book book6 = new Book();
                book6.setTitle("To Kill a Mockingbird");
                book6.setAuthor("Harper Lee");
                book6.setDescription("A Pulitzer Prize winning novel");
                book6.setPrice(new BigDecimal("14.99"));
                book6.setIsbn("978-0061120084");
                book6.setCategory("Fiction");
                book6.setInventory(25);
                bookRepository.save(book6);
            }
            
            // Initialize sample users if table is empty
            if (userRepository.count() == 0) {
                // Regular user
                User user1 = new User();
                user1.setUsername("customer1");
                user1.setPassword(passwordEncoder.encode("password123"));
                user1.setEmail("customer1@example.com");
                user1.setFullName("John Doe");
                user1.setRole(User.UserRole.USER);
                user1.setEnabled(true);
                userRepository.save(user1);
                
                // Employee user
                User employee = new User();
                employee.setUsername("employee1");
                employee.setPassword(passwordEncoder.encode("employee123"));
                employee.setEmail("employee1@example.com");
                employee.setFullName("Jane Smith");
                employee.setRole(User.UserRole.EMPLOYEE);
                employee.setEnabled(true);
                userRepository.save(employee);
                
                // Admin user
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@example.com");
                admin.setFullName("Admin User");
                admin.setRole(User.UserRole.ADMIN);
                admin.setEnabled(true);
                userRepository.save(admin);
            }
        };
    }
}
