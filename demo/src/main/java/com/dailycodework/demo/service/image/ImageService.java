package com.dailycodework.demo.service.image;

import com.dailycodework.demo.dto.ImageDto;
import com.dailycodework.demo.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> files , Long productId);
    void updateImage(MultipartFile file , Long imageId);
}
