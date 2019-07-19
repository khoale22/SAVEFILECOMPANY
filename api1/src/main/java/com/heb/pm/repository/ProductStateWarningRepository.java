package com.heb.pm.repository;

import com.heb.pm.entity.ProductStateWarning;
import com.heb.pm.entity.ProductStateWarningKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for ProductStateWarning.
 *
 * @author m314029
 * @since 2.12.0
 */
public interface ProductStateWarningRepository extends JpaRepository<ProductStateWarning, ProductStateWarningKey> {
}
