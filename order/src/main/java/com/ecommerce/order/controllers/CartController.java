package com.ecommerce.order.controllers;



import com.ecommerce.order.DTO.cartItemRequest;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader ("X-User-ID") String userId,
            @RequestBody cartItemRequest request) {

        if(!cartService.addCart(request,userId))
        {
            return ResponseEntity.badRequest().body("Not able to Proceed Request");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> deleteCart(
            @RequestHeader ("X-User-ID") String userId ,
            @PathVariable String productId)
    {
        boolean deleted = cartService.deleteItemFromCart(userId,productId);

        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@RequestHeader ("X-User-ID") String userId)
    {
        return ResponseEntity.ok(cartService.getCartItem(userId));
    }

}
