package com.bookcatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String description;
    private BigDecimal price;
    private String isbn;
    private String category;
    private Integer inventory;
    private boolean outOfStock;
    
    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }
}
