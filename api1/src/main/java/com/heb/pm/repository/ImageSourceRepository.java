package com.heb.pm.repository;

import com.heb.pm.entity.ImageSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for the image source code table
 * @author s753601
 * @version 2.13.0
 */

public interface ImageSourceRepository extends JpaRepository<ImageSource, String> {

	List<ImageSource> findByActiveImage(boolean isActive);
}
