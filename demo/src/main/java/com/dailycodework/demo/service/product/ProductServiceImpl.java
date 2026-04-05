package com.dailycodework.demo.service.product;

import com.dailycodework.demo.Exceptions.ProductNotFoundException;
import com.dailycodework.demo.Repositories.CategoryRepository;
import com.dailycodework.demo.Repositories.ImageRepository;
import com.dailycodework.demo.Repositories.ProductRepository;
import com.dailycodework.demo.dto.ImageDto;
import com.dailycodework.demo.dto.ProductDto;
import com.dailycodework.demo.model.Category;
import com.dailycodework.demo.model.Image;
import com.dailycodework.demo.model.Product;
import com.dailycodework.demo.request.AddProductRequest;
import com.dailycodework.demo.request.ProductUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;


    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository, ModelMapper modelMapper, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.imageRepository = imageRepository;
    }

    @Override
    public Product addProduct(AddProductRequest request) {

        Category category = Optional.ofNullable(
                categoryRepository.findByName(request.getCategory().getName())
        ).orElseGet(() -> {
            Category newCategory = new Category();
            newCategory.setName(request.getCategory().getName());
            return categoryRepository.save(newCategory);
        });

        Product product = createProduct(request, category);
        return productRepository.save(product);
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {

        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    private Product updateExistingProduct(Product existingProduct,
                                          ProductUpdateRequest request) {

        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = Optional.ofNullable(
                categoryRepository.findByName(request.getCategory().getName())
        ).orElseGet(() -> {
            Category newCategory = new Category();
            newCategory.setName(request.getCategory().getName());
            return categoryRepository.save(newCategory);
        });

        existingProduct.setCategory(category);

        return existingProduct;
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndNames(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }


    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product,ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos= images.stream().map(image-> modelMapper.map(image,ImageDto.class)).toList();
        productDto.setImages(imageDtos);
        return productDto;
    }


}