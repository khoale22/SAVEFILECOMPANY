/*
 * ItemMasterRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ItemMasterKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository to retrieve information about an item.
 *
 * @author m314029
 * @since 2.0.4
 */
public interface ItemMasterRepository extends JpaRepository<ItemMaster, ItemMasterKey>{

	/**
	 * Query to find Item Master object by UPC code and item type code.
	 */
	String FIND_ITEM_MASTER_BY_UPC_CODE_QUERY = "SELECT im FROM ItemMaster im WHERE im.key.itemCode =" +
			" (SELECT DISTINCT pu.itemCode FROM PrimaryUpc pu INNER JOIN pu.associateUpcs au " +
			"WHERE au.upc = :upcCode)";
	/**
	 * Query to find item code by alert type and search criteria session. It use to count item codes in Hits at nutrient task screen.
	 */
	static final String QUERY_FIND_ITEM_CODE_IN_PROD_ITEM = "select CAST(prodItem.key.itemCode AS long) from ProdItem prodItem " +
			"where prodItem.key.itemType = 'ITMCD' and prodItem.productId in " +
			"(select CAST(alertStaging.alertKey AS long) from AlertStaging  alertStaging " +
			"where alertStaging.alertTypeCD = :alertTypeCD and alertStaging.alertStatusCD = :alertStatusCD " +
			") and exists (select searchCriteria.itemCode " +
			"from SearchCriteria searchCriteria " +
			"where CAST(prodItem.key.itemCode AS string) = CAST(searchCriteria.itemCode AS string) and searchCriteria.sessionId = :sessionId)";

	/**
	 * Returns a list of ItemMaster object searching by ordering UPC. This can return multiples for items that are
	 * both warehouse and DSD.
	 *
	 * @param upc The ordering UPC to look for.
	 * @return A list of item masters that have the looked for ordering UPC.
	 */
	List<ItemMaster> findItemMasterByOrderingUpc(long upc);

	/**
	 * Returns a list of ItemMaster object searching by Primary Shipper UPC Or Primary Associate UPC.
	 *
	 * @param shipperUpc The ordering UPC to look for.
	 * @param associateUpc The ordering UPC to look for.
	 * @return A list of item masters.
	 */
	List<ItemMaster> findByPrimaryUpcShipperKeyShipperUpcOrPrimaryUpcAssociateUpcsUpc(long shipperUpc, long associateUpc);

	/**
	 * Returns the list of item codes that exist that are part of a user's search criteria in their search session.
	 *
	 * @param sessionId The ID of the user's session.
	 * @return A list of item codes that match the list the user has provided.
	 */
	@Query(value = "select itemMaster.key.itemCode from ItemMaster itemMaster " +
			"where exists (select searchCriteria.itemCode " +
			"from SearchCriteria searchCriteria " +
			"where itemMaster.key.itemCode = searchCriteria.itemCode and searchCriteria.sessionId = :sessionId)")
	List<Long> findAllForSearch(@Param("sessionId") String sessionId);

	/**
	 * Returns the list of case UPCs that exist that are part of a user's search criteria in their search session.
	 *
	 * @param sessionId The ID of the user's session.
	 * @return A list of case UPCs that match the list the user has provided.
	 */
	@Query(value = "select itemMaster.caseUpc from ItemMaster itemMaster " +
			"where exists (select searchCriteria.upc " +
			"from SearchCriteria searchCriteria " +
			"where itemMaster.caseUpc = searchCriteria.upc and searchCriteria.sessionId = :sessionId)")
	List<Long> findAllCaseUpcsForSearch(@Param("sessionId") String sessionId);

	@Query(value = "select itemMaster.caseUpc from ItemMaster itemMaster where itemMaster.caseUpc in :upcsGenLst")
	List<Long> findCaseUpcsByUpcGenList(@Param("upcsGenLst") List<Long> upcsGenLst);

	/**
	 * Find Item Master by Item Code.
	 *
	 * @param upcCode the upc code
	 * @return Item master.
	 */
	@Query(value = FIND_ITEM_MASTER_BY_UPC_CODE_QUERY)
	ItemMaster findByItemCode(@Param("upcCode") Long upcCode);

	/**
	 * Find Item Master by Ordering Upc.
	 *
	 * @param OrderingUpc the odering Upc.
	 * @return Item Master.
	 */
	ItemMaster findByOrderingUpc(Long OrderingUpc);

	/**
	 * Returns ItemMasters by ItemType and OrderingUpc.
	 *
	 * @param itemType the itemType.
	 * @param orderingUpc the orderingUpc.
	 * @return ItemMasters by ItemType and OrderingUpc.
	 */
	List<ItemMaster> findByKeyItemTypeAndOrderingUpc(String itemType, Long orderingUpc);

	/**
	 * Returns the list of item codes that exist that are part of a user's search criteria in their search session.
	 * This can return multiples for items that are both warehouse and active.
	 *
	 * @param alertTypeCD   the alert type.
	 * @param alertStatusCD the alert status.
	 * @param sessionId     the session Id of search nutrient updates.
	 * @return A list of item codes that match the list the user has provided.
	 */
	@Query(value = QUERY_FIND_ITEM_CODE_IN_PROD_ITEM)
	List<Long> findAllItemCodesForSearchNutritionUpdate(@Param("alertTypeCD") String alertTypeCD,
														@Param("alertStatusCD") String alertStatusCD,
														@Param("sessionId") String sessionId);
}
