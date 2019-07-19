package com.heb.pm.repository;

import com.heb.pm.entity.ProductShippingException;
import com.heb.pm.entity.ProductShippingExceptionKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Restful client for Product Shipping Exceptions
 * @author m594201
 * @version 2.14.0
 */
public interface ProductShippingExceptionRepository extends JpaRepository<ProductShippingException, ProductShippingExceptionKey> {

	List<ProductShippingException> findByKeyProdId(Long prodId);

}
