package com.heb.pm.repository;

import com.heb.pm.entity.ItemMasterAudit;
import com.heb.pm.entity.ItemMasterAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This repository is intended to get all change records on the Item Master table based on the search criteria
 * @author  s753601
 * @version 2.8.0
 */
public interface ItemMasterAuditRepository extends JpaRepository<ItemMasterAudit, ItemMasterAuditKey> {

	/**
	 * Retrieves all records of changes on an item master's attributes
	 * @param itemCode numeric component to identify the item master
	 * @param itemType classification component to identify the item master
	 * @return all records of changes on an item master's attributes
	 */
	List<ItemMasterAudit> findByKeyItemCodeAndKeyItemTypeOrderByKeyChangedOn(Long itemCode, String itemType);
}
