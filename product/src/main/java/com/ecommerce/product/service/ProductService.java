package com.ecommerce.product.service;


import com.ecommerce.product.DTO.ProductRequest;
import com.ecommerce.product.DTO.ProductResponse;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product,productRequest);
        productRepository.save(product);
        return mapToProductResponse(product);
    }

    private ProductResponse mapToProductResponse(Product product) {

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setCategory(product.getCategory());
        response.setActive(product.getActive());
        response.setPrice(product.getPrice());
        response.setDescription(product.getDescription());
        response.setImageUrl(product.getImageUrl());
        response.setStockQuantity(product.getStockQuantity());

        return response;
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setPrice(productRequest.getPrice());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStockQuantity(productRequest.getStockQuantity());

    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {

        return productRepository.findById(id)
                .map(existingProduct -> {
                    updateProductFromRequest(existingProduct,productRequest);
                    Product savedProduct = productRepository.save(existingProduct);
                    return mapToProductResponse(savedProduct);
                });
    }

    public List<ProductResponse> getAllProduct() {

        return productRepository.findByActiveTrue().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id) {

        return productRepository.findById(id).map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);
    }

    public List<ProductResponse> searchProducts(String keyword) {

        return productRepository.searchProducts(keyword).stream()
                .map(this::mapToProductResponse)
                .toList();
    }


    public Optional<ProductResponse> getProductByID(String id) {

        return productRepository.findByIdAndActiveTrue(Long.valueOf(id))
                .map(this::mapToProductResponse);
    }
}
