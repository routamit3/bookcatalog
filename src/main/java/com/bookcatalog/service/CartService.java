package com.bookcatalog.service;

import com.bookcatalog.dto.CartItemDTO;
import com.bookcatalog.entity.CartItem;
import com.bookcatalog.entity.User;
import com.bookcatalog.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    
    private final CartItemRepository cartItemRepository;
    private final BookService bookService;
    
    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }
    
    public List<CartItem> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }
    
    public CartItem addToCart(User user, Long bookId, Integer quantity) {
        Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndBookId(user.getId(), bookId);
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        }
        
        CartItem newItem = new CartItem();
        newItem.setUser(user);
        newItem.setBook(bookService.getBookById(bookId).orElseThrow(() -> 
            new IllegalArgumentException("Book not found")));
        newItem.setQuantity(quantity);
        
        return cartItemRepository.save(newItem);
    }
    
    public CartItem updateQuantity(Long cartItemId, Integer quantity) {
        Optional<CartItem> optionalItem = cartItemRepository.findById(cartItemId);
        if (optionalItem.isPresent()) {
            CartItem item = optionalItem.get();
            if (quantity <= 0) {
                cartItemRepository.delete(item);
                return null;
            }
            item.setQuantity(quantity);
            return cartItemRepository.save(item);
        }
        return null;
    }
    
    public void removeFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
    
    public void removeFromCart(Long userId, Long bookId) {
        cartItemRepository.deleteByUserIdAndBookId(userId, bookId);
    }
    
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
    
    public BigDecimal getCartTotal(User user) {
        List<CartItem> items = getCartItems(user);
        return items.stream()
            .map(CartItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal getCartTotal(Long userId) {
        List<CartItem> items = getCartItems(userId);
        return items.stream()
            .map(CartItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public CartItemDTO convertToDTO(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(cartItem.getId());
        dto.setBookId(cartItem.getBook().getId());
        dto.setBookTitle(cartItem.getBook().getTitle());
        dto.setBookAuthor(cartItem.getBook().getAuthor());
        dto.setBookPrice(cartItem.getBook().getPrice());
        dto.setQuantity(cartItem.getQuantity());
        dto.setTotalPrice(cartItem.getTotalPrice());
        return dto;
    }
    
    public List<CartItemDTO> convertToDTO(List<CartItem> cartItems) {
        return cartItems.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
