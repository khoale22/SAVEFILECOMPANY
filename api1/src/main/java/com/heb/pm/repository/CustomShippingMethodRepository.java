package com.heb.pm.repository;

import com.heb.pm.entity.CustomShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Restful client for Custom Shipping Method.
 * @author m594201
 * @version 2.14.0
 */
public interface CustomShippingMethodRepository extends JpaRepository<CustomShippingMethod, Long> {
}
