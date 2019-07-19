/*
 * VendorItemStoreRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;


import com.heb.pm.entity.VendorItemStore;
import com.heb.pm.entity.VendorItemStoreKey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository to retrieve information about VendorItemStore.
 *
 * @author vn70529
 * @since 2.23.0
 */
public interface VendorItemStoreRepository extends JpaRepository<VendorItemStore, VendorItemStoreKey> {
	/**
	 * Query to find Item based on item, Store and departmentnbr.
	 */
    //Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	String FIND_VEND_ITEM_STORE_QUERY = "select vis from VendorItemStore  vis where vis.id.itmId =" +
			" :itmId and  vis.id.itmKeyTypCd = :itmKeyTypCd and vis.id.locNbr= :locNbr and vis.id.locTypCd=:locTypCd"
			+ " and vis.authnSw='Y' and vis.strDeptNbr= :strDeptNbr" ; 
	
	@Query(value = FIND_VEND_ITEM_STORE_QUERY)
	List<VendorItemStore> findVendItemStoreByStrDeptNbr(@Param("itmId")Long itmId,  @Param("itmKeyTypCd")String itmKeyTypCd,
			@Param("locNbr")long locNbr,@Param("locTypCd")String locTypCd,@Param("strDeptNbr")String strDeptNbr); 
    //End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

}
