package com.dailycodework.demo.service.product;

import com.dailycodework.demo.model.Product;

import java.util.List;

public interface ProductService {

    Product addProduct(Product product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    void updateProduct(Product product , Long productId);
    List<Product> getAllProduct();
    List<Product> getProductByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category , String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndNames(String brand ,String name);
    Long countProductsByBrandAndName(String brand , String name);





}
