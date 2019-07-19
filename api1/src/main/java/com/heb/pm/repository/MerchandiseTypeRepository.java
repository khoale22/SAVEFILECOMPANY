package com.heb.pm.repository;

import com.heb.pm.entity.MerchandiseType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for MerchantType.
 *
 * @author vn70516
 * @since 2.7.0
 */
public interface MerchandiseTypeRepository extends JpaRepository<MerchandiseType, String> {
}
