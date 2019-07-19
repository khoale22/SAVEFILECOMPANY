package com.heb.pm.repository;

import com.heb.pm.entity.ImagePriority;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for the Image Priority code table
 * @author s753601
 * @version 2.13.0
 */
public interface ImagePriorityRepository extends JpaRepository<ImagePriority, String> {
}
