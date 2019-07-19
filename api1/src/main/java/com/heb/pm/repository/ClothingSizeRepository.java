package com.heb.pm.repository;

import com.heb.pm.entity.ClothingSize;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * repository for clothing size.
 *
 * @author s769046
 * @since 2.8.0
 */

public interface ClothingSizeRepository extends JpaRepository<ClothingSize, String> {
}
