package com.heb.pm.repository;

import com.heb.pm.entity.MerchantType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for MerchantType.
 *
 * @author m314029
 * @since 2.5.0
 */
public interface MerchantTypeRepository extends JpaRepository<MerchantType, Character> {
}
