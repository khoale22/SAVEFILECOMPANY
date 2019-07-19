/*
 *  WicLebService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.codeTable.wicLeb;

import com.heb.pm.entity.SellingUnitWic;
import com.heb.pm.entity.WicCategoryRetailSize;
import com.heb.pm.entity.WicSubCategory;
import com.heb.pm.repository.SellingUnitWicRepository;
import com.heb.pm.repository.WicCategoryRetailSizeRepository;
import com.heb.pm.repository.WicSubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Holds all business logic related to Wic Leb information.
 *
 * @author vn70529
 * @since 2.12.0
 */
@Service
public class WicLebService {

	@Autowired
	private WicSubCategoryRepository wicSubCategoryRepository;
	@Autowired
	private SellingUnitWicRepository productScanCodeWicRepository;
	@Autowired
	private WicCategoryRetailSizeRepository wicCategoryRetailSizeRepository;
	/**
	 * Returns all wic sub categories ordered by id.
	 *
	 * @return all wic sub categories ordered by id.
	 */
	public List<WicSubCategory> findAllWicLebs() {
		return this.wicSubCategoryRepository.findAllByOrderByWicCategoryIdAsc();
	}
	/**
	 * Returns all the leb upcs by wic category id and wic sub category id.
	 *
	 * @param wicCategoryId the wic category id.
	 * @param wicSubCategoryId the wic sub category id.
	 * @return the list of ProductScanCodeWics.
	 */
	public List<SellingUnitWic> findLebUpcsByKeyWicCategoryIdAndKeyWicSubCategoryId(Long wicCategoryId,
																					   Long wicSubCategoryId) {
		return this.productScanCodeWicRepository.findByKeyWicCategoryIdAndKeyWicSubCategoryIdOrderByKeyUpc(wicCategoryId,
				wicSubCategoryId);
	}
	/**
	 * Returns the leb sizes by wic category id and wic sub category id.
	 *
	 * @param wicCategoryId the wic category id.
	 * @param wicSubCategoryId the wic sub category id.
	 * @return the list of WicCategoryRetailSizes
	 */
	public List<WicCategoryRetailSize> findAllLebSizesByWicCategoryIdAndWicSubCategoryId(Long wicCategoryId,
																						 Long wicSubCategoryId) {
		return wicCategoryRetailSizeRepository.findByKeyWicCategoryIdAndKeyWicSubCategoryId(wicCategoryId,
				wicSubCategoryId);
	}
}
