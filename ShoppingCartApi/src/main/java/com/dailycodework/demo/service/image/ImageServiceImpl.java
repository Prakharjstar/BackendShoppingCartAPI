package com.dailycodework.demo.service.image;

import com.dailycodework.demo.Exceptions.ResourceNotFoundException;
import com.dailycodework.demo.Repositories.ImageRepository;
import com.dailycodework.demo.dto.ImageDto;
import com.dailycodework.demo.model.Image;
import com.dailycodework.demo.model.Product;
import com.dailycodework.demo.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service

public class ImageServiceImpl implements ImageService{
    public ImageServiceImpl(ProductService productService, ImageRepository imageRepository) {
        this.productService = productService;
        this.imageRepository = imageRepository;
    }

    private final ImageRepository imageRepository;
    private final ProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Image is found with this Id " + id));
    }

    @Override
    public void deleteImageById(Long id) {

        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete , () -> { throw new ResourceNotFoundException("No image is found with this Id " + id);});

    }
    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDtos = new ArrayList<>();

        String buildDownloadUrl = "/api/v1/images/image/download/";

        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                // First save to generate the ID
                Image savedImage = imageRepository.save(image);

                // Now create the download URL using the generated ID
                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());

                // Save again with updated download URL
                savedImage = imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());

                savedImageDtos.add(imageDto);

            } catch (IOException | SQLException e) {
                throw new RuntimeException("Failed to save image: " + e.getMessage());
            }
        }

        return savedImageDtos;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);

        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));

            imageRepository.save(image);

        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to update image: " + e.getMessage());
        }
    }
}
