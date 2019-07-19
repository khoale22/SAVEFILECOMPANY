package com.heb.pm.repository;

import com.heb.pm.entity.ProductMat;
import com.heb.pm.entity.ProductMatKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository for the ProductMat entity.
 *
 * @author s573181
 * @since 2.29.0
 */
public interface ProductMatRepository extends JpaRepository<ProductMat, ProductMatKey> {

	List<ProductMat> findAllByProductLineCode(String productLineCode);

	long countByProductLineCode(String productLineCode);
}
