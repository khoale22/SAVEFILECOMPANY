package com.heb.pm.repository;

import com.heb.pm.entity.ImportItemAudit;
import com.heb.pm.entity.ImportItemAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about vendors.
 *
 * @author s753601
 * @since 2.7.0
 */
public interface ImportItemAuditRepository extends JpaRepository<ImportItemAudit, ImportItemAuditKey> {

	/**
	 * Find by nutrient code in list.
	 *
	 * @param itemCode the item id request
	 * @return the list
	 */
	List<ImportItemAudit> findByKeyItemCodeAndKeyItemTypeAndKeyVendorTypeAndKeyVendorNumberOrderByKeyChangedOn(Long itemCode, String itemType, String vendorType, Integer vendorNumber);
}
