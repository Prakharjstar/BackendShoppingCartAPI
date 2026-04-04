package com.dailycodework.demo.dto;

import com.dailycodework.demo.model.Category;

import lombok.Data;


import java.math.BigDecimal;
import java.util.List;


@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
    private List<ImageDto> images;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImages(List<ImageDto> images) {
        this.images = images;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


}
