package com.heb.pm.repository;

import com.heb.pm.entity.ImageCategory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the image category code table
 * @author s753601
 * @version 2.13.0
 */
public interface ImageCategoryRepository  extends JpaRepository<ImageCategory, String> {
}
