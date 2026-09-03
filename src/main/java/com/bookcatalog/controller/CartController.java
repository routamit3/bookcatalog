package com.bookcatalog.controller;

import com.bookcatalog.dto.WeatherDTO;
import com.bookcatalog.entity.CartItem;
import com.bookcatalog.entity.Order;
import com.bookcatalog.entity.User;
import com.bookcatalog.repository.OrderRepository;
import com.bookcatalog.service.CartService;
import com.bookcatalog.service.BookService;
import com.bookcatalog.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;
    private final BookService bookService;
    private final WeatherService weatherService;
    private final OrderRepository orderRepository;
    
    @GetMapping
    public String viewCart(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        User user = (User) authentication.getPrincipal();
        List<CartItem> cartItems = cartService.getCartItems(user);
        BigDecimal totalPrice = cartService.getCartTotal(user);
        WeatherDTO weather = weatherService.getWeatherByCity("New York");
        
        model.addAttribute("cartItems", cartService.convertToDTO(cartItems));
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("weather", weather);
        model.addAttribute("itemCount", cartItems.size());
        
        return "cart";
    }
    
    @PostMapping("/add")
    public String addToCart(@RequestParam Long bookId,
                           @RequestParam(defaultValue = "1") Integer quantity,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        try {
            User user = (User) authentication.getPrincipal();
            var book = bookService.getBookById(bookId);
            
            if (book.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Book not found");
                return "redirect:/books";
            }
            
            if (book.get().isOutOfStock()) {
                redirectAttributes.addFlashAttribute("error", "Book is out of stock");
                return "redirect:/book-detail?id=" + bookId;
            }
            
            if (quantity > book.get().getInventory()) {
                redirectAttributes.addFlashAttribute("error", 
                    "Insufficient stock. Only " + book.get().getInventory() + " available");
                return "redirect:/book-detail?id=" + bookId;
            }
            
            cartService.addToCart(user, bookId, quantity);
            redirectAttributes.addFlashAttribute("success", "Book added to cart successfully!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/cart";
    }
    
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long cartItemId,
                                RedirectAttributes redirectAttributes) {
        try {
            cartService.removeFromCart(cartItemId);
            redirectAttributes.addFlashAttribute("success", "Item removed from cart");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to remove item");
        }
        return "redirect:/cart";
    }
    
    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long cartItemId,
                                @RequestParam Integer quantity,
                                RedirectAttributes redirectAttributes) {
        try {
            if (quantity <= 0) {
                cartService.removeFromCart(cartItemId);
                redirectAttributes.addFlashAttribute("success", "Item removed from cart");
            } else {
                cartService.updateQuantity(cartItemId, quantity);
                redirectAttributes.addFlashAttribute("success", "Quantity updated");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update quantity");
        }
        return "redirect:/cart";
    }
    
    @GetMapping("/clear")
    public String clearCart(Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        User user = (User) authentication.getPrincipal();
        cartService.clearCart(user.getId());
        redirectAttributes.addFlashAttribute("success", "Cart cleared");
        
        return "redirect:/cart";
    }
    
    @GetMapping("/checkout")
    public String checkout(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        User user = (User) authentication.getPrincipal();
        List<CartItem> cartItems = cartService.getCartItems(user);
        
        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }
        
        BigDecimal totalPrice = cartService.getCartTotal(user);
        WeatherDTO weather = weatherService.getWeatherByCity("New York");
        
        model.addAttribute("cartItems", cartService.convertToDTO(cartItems));
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("weather", weather);
        model.addAttribute("user", user);
        
        return "checkout";
    }
    
    @PostMapping("/checkout")
    public String processCheckout(Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        try {
            User user = (User) authentication.getPrincipal();
            List<CartItem> cartItems = cartService.getCartItems(user);
            
            if (cartItems.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Cart is empty");
                return "redirect:/cart";
            }
            
            // Update inventory for each item
            StringBuilder orderItemsBuilder = new StringBuilder();
            for (CartItem item : cartItems) {
                bookService.updateInventory(item.getBook().getId(), item.getQuantity());
                orderItemsBuilder.append(item.getBook().getTitle())
                    .append(" (x").append(item.getQuantity()).append("), ");
            }
            
            // Create order
            Order order = new Order();
            order.setUser(user);
            order.setOrderItems(orderItemsBuilder.toString());
            order.setTotalAmount(cartService.getCartTotal(user));
            order.setStatus(Order.OrderStatus.COMPLETED);
            
            orderRepository.save(order);
            
            // Clear cart
            cartService.clearCart(user.getId());
            
            redirectAttributes.addFlashAttribute("success", 
                "Order placed successfully! Total: $" + order.getTotalAmount());
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
        
        return "redirect:/orders";
    }
    
    @GetMapping("/orders")
    public String viewOrders(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        User user = (User) authentication.getPrincipal();
        List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(user.getId());
        WeatherDTO weather = weatherService.getWeatherByCity("New York");
        
        model.addAttribute("orders", orders);
        model.addAttribute("weather", weather);
        
        return "orders";
    }
}
