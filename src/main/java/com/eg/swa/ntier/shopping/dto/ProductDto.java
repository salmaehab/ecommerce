package com.eg.swa.ntier.shopping.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class ProductDto {
    private Long id;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}
