/*
 * SellingUnitRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.SellingUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for product scan codes (UPC).
 *
 * @author m314029
 * @since 2.0.3
 */
public interface SellingUnitRepository extends JpaRepository<SellingUnit, Long>{
    /**
     * Query to find SellingUnit object by UPC and location's active status.
     */
    String FIND_SELLING_UNIT_BY_UPC_QUERY = "SELECT su FROM SellingUnit su " +
            " INNER JOIN su.goodsProduct gp " +
            " INNER JOIN su.productMaster pm " +
            " INNER JOIN pm.classCommodity cc " +
            " WHERE su.upc =:upc";
    /**
     * Query to find UPC code by alert type and search criteria session. It use to count upc in Hits at nutrient task screen.
     */
    static final String QUERY_FIND_UPC_IN_SELLING_UNIT = "select CAST(sellingUnit.upc AS long) from SellingUnit sellingUnit " +
            "where exists (select searchCriteria.upc from SearchCriteria searchCriteria where " +
            "sellingUnit.upc = searchCriteria.upc and searchCriteria.sessionId = :sessionId) and sellingUnit.prodId in " +
            "(select CAST(alertStaging.alertKey AS long) from AlertStaging  alertStaging " +
            "where alertStaging.alertTypeCD = :alertTypeCD and alertStaging.alertStatusCD = :alertStatusCD)";

    /**
     * Find the list of selling unit by product id.
     * @param prodId - The prduct id
     * @return List<SellingUnit>
     */
    List<SellingUnit> findByProdId(long prodId);

    /**
     * Find the list of selling unit by product id.
     * @param prodId - The prduct id
     * @return List<SellingUnit>
     */
    List<SellingUnit> findByProdIdOrderByUpcAsc(long prodId);

    /**
     * Returns the list of UPCs that exist that are part of a user's search criteria in their search session.
     *
     * @param sessionId The ID of the user's session.
     * @return A list of UPCs that match the list the user has provided.
     */
    @Query(value = "select sellingUnit.upc from SellingUnit sellingUnit where exists (select searchCriteria.upc from SearchCriteria searchCriteria where sellingUnit.upc = searchCriteria.upc and searchCriteria.sessionId = :sessionId)")
    List<Long> findAllForSearch(@Param("sessionId") String sessionId);

    @Query(value = "select sellingUnit.upc from SellingUnit sellingUnit where sellingUnit.upc in :upcsGenLst")
    List<Long> findCaseUpcsByUpcGenList(@Param("upcsGenLst") List<Long> upcsGenLst);
    /**
     * Find selling unit with inner join to good product, product master and class commodity by upc.
     * @param upc the upc id.
     * @return the selling unit.
     */
    @Query(FIND_SELLING_UNIT_BY_UPC_QUERY)
    SellingUnit findOneByUpc(@Param("upc") long upc);

    /**
     * Find alert by UPCs. Return the list of UPCs that exist that are part of a user's search criteria in their search session.
     *
     * @param alertTypeCD   the alert type.
     * @param alertStatusCD the alert status.
     * @param sessionId     the session Id of search nutrient updates.
     * @return A list of UPCs that match the list the user has provided.
     */
    @Query(value = QUERY_FIND_UPC_IN_SELLING_UNIT)
    List<Long> findAllUpcsForSearchNutritionUpdate(@Param("alertTypeCD") String alertTypeCD,
                                                   @Param("alertStatusCD") String alertStatusCD,
                                                   @Param("sessionId") String sessionId);

    /**
     * Used to count SellingUnit by the ProductBrandId.
     * @param productBrandId - productBrandId.
     * @return the number of SellingUnit
     */
    Long countSellingUnitByProductBrandProductBrandId(Long productBrandId);

    /**
     * Count selling unit by cost owner id.
     *
     * @param costOwnerId The id of cost owner.
     * @return number of selling units.
     */
    Long countSellingUnitByCostOwnerCostOwnerId(Integer costOwnerId);
}
