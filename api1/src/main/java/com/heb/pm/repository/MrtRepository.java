package com.heb.pm.repository;

import com.heb.pm.entity.PrimaryUpc;
import com.heb.pm.entity.Shipper;
import com.heb.pm.entity.ShipperKey;
import com.heb.pm.entity.ShippingRestrictionHierarchyLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about an mrt.
 *
 * @author m594201
 * @since 2.6.0
 */
public interface MrtRepository extends JpaRepository<Shipper, ShipperKey> {

	List<Shipper> findByKeyUpc(long upc);

}
