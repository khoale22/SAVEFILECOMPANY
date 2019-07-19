package com.heb.pm.repository;

import com.heb.pm.entity.ImageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for the image status code table
 * @author s753601
 * @version 2.13.0
 */
public interface ImageStatusRepository  extends JpaRepository<ImageStatus, String> {
}
