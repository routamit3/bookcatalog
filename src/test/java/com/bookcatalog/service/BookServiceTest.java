package com.bookcatalog.service;

import com.bookcatalog.entity.Book;
import com.bookcatalog.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(bookRepository);
    }

    @Test
    void searchByTitleOrAuthor_shouldReturnMatchingBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        Book book = new Book();
        book.setTitle("Hamlet");
        book.setAuthor("William Shakespeare");
        Page<Book> expected = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase("shakespeare", "shakespeare", pageable))
                .thenReturn(expected);

        Page<Book> actual = bookService.searchByTitleOrAuthor("shakespeare", pageable);

        assertEquals(expected, actual);
    }
}
