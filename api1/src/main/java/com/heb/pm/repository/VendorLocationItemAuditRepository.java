package com.heb.pm.repository;

import com.heb.pm.entity.VendorLocationItemAudit;
import com.heb.pm.entity.VendorLocationItemAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author m314029
 */
public interface VendorLocationItemAuditRepository extends JpaRepository<VendorLocationItemAudit, VendorLocationItemAuditKey> {
	/**
	 * Find by nutrient code in list.
	 *
	 * @param itemCode the item id request
	 * @return the list
	 */
	List<VendorLocationItemAudit> findByKeyItemCodeAndKeyItemTypeAndKeyVendorTypeAndKeyVendorNumberOrderByKeyChangedOn(Long itemCode, String itemType, String vendorType, Integer vendorNumber);
}
