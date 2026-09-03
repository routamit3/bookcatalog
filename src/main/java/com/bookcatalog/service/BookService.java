package com.bookcatalog.service;

import com.bookcatalog.dto.BookDTO;
import com.bookcatalog.entity.Book;
import com.bookcatalog.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
    
    private final BookRepository bookRepository;
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }
    
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
    
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
    
    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategory(category);
    }
    
    public Page<Book> getBooksByCategory(String category, Pageable pageable) {
        return bookRepository.findByCategory(category, pageable);
    }
    
    public Page<Book> searchByTitle(String title, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
    }
    
    public Page<Book> searchByAuthor(String author, Pageable pageable) {
        return bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
    }
    
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }
    
    public Book updateBook(Long id, Book bookDetails) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setDescription(bookDetails.getDescription());
            book.setPrice(bookDetails.getPrice());
            book.setIsbn(bookDetails.getIsbn());
            book.setCategory(bookDetails.getCategory());
            book.setInventory(bookDetails.getInventory());
            return bookRepository.save(book);
        }
        return null;
    }
    
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
    
    public void updateInventory(Long bookId, Integer quantityToDeduct) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            int newInventory = book.getInventory() - quantityToDeduct;
            if (newInventory < 0) {
                throw new IllegalArgumentException("Insufficient inventory for book: " + book.getTitle());
            }
            book.setInventory(newInventory);
            bookRepository.save(book);
        }
    }
    
    public void restoreInventory(Long bookId, Integer quantityToRestore) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setInventory(book.getInventory() + quantityToRestore);
            bookRepository.save(book);
        }
    }
    
    public BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setDescription(book.getDescription());
        dto.setPrice(book.getPrice());
        dto.setIsbn(book.getIsbn());
        dto.setCategory(book.getCategory());
        dto.setInventory(book.getInventory());
        dto.setOutOfStock(book.isOutOfStock());
        return dto;
    }
    
    public List<BookDTO> convertToDTO(List<Book> books) {
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    public Page<BookDTO> convertToDTO(Page<Book> page) {
        return new PageImpl<>(convertToDTO(page.getContent()), page.getPageable(), page.getTotalElements());
    }
}
