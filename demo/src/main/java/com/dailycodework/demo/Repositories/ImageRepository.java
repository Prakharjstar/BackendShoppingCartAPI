package com.dailycodework.demo.Repositories;

import com.dailycodework.demo.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image , Long> {
}
