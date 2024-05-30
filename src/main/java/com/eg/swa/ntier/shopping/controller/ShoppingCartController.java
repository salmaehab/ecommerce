package com.eg.swa.ntier.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eg.swa.ntier.shopping.dto.ShoppingCartDto;
import com.eg.swa.ntier.shopping.model.ShoppingCart;
import com.eg.swa.ntier.shopping.service.ShoppingCartService;

@RestController
@RequestMapping("/api/shopping-cart")
public class ShoppingCartController {
    
    @Autowired
    private ShoppingCartService shoppingCartService;
    
    
    @PostMapping("/getOrCreateCart/{customerId}")
    public ShoppingCart getOrCreateCart(@PathVariable("customerId") Long customerId){
    	return shoppingCartService.getOrCreateCart(customerId);
    }

    @PostMapping("/addToCart/{customerId}")
    public ResponseEntity<String> addToCart(@PathVariable("customerId") Long customerId, @RequestBody ShoppingCartDto shoppingCartDto) throws Exception {

        shoppingCartService.addToCart(customerId, shoppingCartDto);
        return ResponseEntity.ok().body("Item added to cart.");
    }

    @DeleteMapping("/removeFromCart/{customerId}/{cartItemId}")
    public ResponseEntity<String> removeFromCart(@PathVariable("customerId") Long customerId, @PathVariable("cartItemId") Long cartItemId
                                                 ) throws Exception {

        shoppingCartService.removeFromCart(customerId, cartItemId);
        return ResponseEntity.ok().body("Cart item updated.");
    }

    @PutMapping("/updateCartItem/{customerId}/{cartItemId}")
    public ResponseEntity<String> updateCartItem(@PathVariable("customerId") Long customerId, @PathVariable("cartItemId") Long cartItemId, 
                                                  @RequestBody ShoppingCartDto shoppingCartDto) throws Exception {
        shoppingCartDto.setId(cartItemId);
        shoppingCartDto.setCustomerId(customerId);
        shoppingCartService.updateCartItem(shoppingCartDto);
        return ResponseEntity.ok().body("Cart item updated.");
    }
    
}

