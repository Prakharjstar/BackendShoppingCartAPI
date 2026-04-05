package com.dailycodework.demo.service.product;

import com.dailycodework.demo.dto.ProductDto;
import com.dailycodework.demo.model.Product;
import com.dailycodework.demo.request.AddProductRequest;
import com.dailycodework.demo.request.ProductUpdateRequest;

import java.util.List;

public interface ProductService {

    Product addProduct( AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product , Long productId);
    List<Product> getAllProduct();
    List<Product> getProductByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category , String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndNames(String brand ,String name);
    Long countProductsByBrandAndName(String brand , String name);


    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
