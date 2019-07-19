package com.heb.pm.repository;

import com.heb.pm.entity.PackagingType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for packaging type.
 *
 * @author m314029
 * @since 2.21.0
 */
public interface PackagingTypeRepository extends JpaRepository<PackagingType, String> {
}
