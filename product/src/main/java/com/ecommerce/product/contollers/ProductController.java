package com.ecommerce.product.contollers;

import com.ecommerce.product.DTO.ProductRequest;
import com.ecommerce.product.DTO.ProductResponse;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    int attempt = 0;

    @GetMapping("/simulate")
    public ResponseEntity<String> simulateFailure(
            @RequestParam(defaultValue = "false") boolean fail)
    {
        if(fail)
        {
            throw new RuntimeException("Simulate Failure for Testing " + ++attempt);
        }
        return ResponseEntity.ok("Product Service is OK") ;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest)
    {
        return new ResponseEntity<>(productService.createProduct(productRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id ,
                                                         @RequestBody ProductRequest productRequest)
    {
        return productService.updateProduct(id,productRequest)
                .map(ResponseEntity :: ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id)
    {
        return productService.getProductByID(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProduct()
    {
        return new ResponseEntity<>(productService.getAllProduct(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id)
    {
        boolean deleted = productService.deleteProduct(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProduct(@RequestParam String keyword)
    {
        return ResponseEntity.ok(productService.searchProducts(keyword));

    }
}
