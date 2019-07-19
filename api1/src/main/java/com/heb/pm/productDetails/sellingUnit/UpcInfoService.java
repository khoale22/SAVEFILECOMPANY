/*
 *  UpcInfoService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.sellingUnit;

import com.heb.pm.ResourceConstants;
import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.*;
import com.heb.pm.repository.RetailUnitOfMeasureRepository;
import com.heb.pm.repository.SellingUnitWicRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.audit.AuditRecord;
import com.heb.pm.repository.WicCategoryRetailSizeRepository;
import com.heb.util.controller.UserInfo;
import com.heb.util.ws.SoapException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This holds all of the logic for the upc info screen.
 *
 * @author l730832
 * @since 2.12.0
 */
@Service
public class UpcInfoService {

	@Autowired
	private SellingUnitWicRepository repository;

	@Autowired
	private WicCategoryRetailSizeRepository wicCategoryRetailSizeRepository;

	@Autowired
	private RetailUnitOfMeasureRepository retailUnitOfMeasureRepository;

	@Autowired
	private AuditService auditService;

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Retrieve the list of cross linked upc's sorted.
	 *
	 * @param wicAplId the wic apl id
	 * @return the list of cross linked upcs
	 */
	public List<SellingUnitWic> getCrossLinkedListOfUPCs(Long wicAplId) {
		return this.repository.findByKeyWicApprovedProductListIdOrderByKeyUpc(wicAplId);
	}

	/**
	 * This gets all of the wics attached to the selling unit.
	 *
	 * @param upc the selling unit it is searching for wics by.
	 * @return list of selling unit wics.
	 */
	public List<SellingUnitWic> findByUpc(Long upc) {
		return this.repository.findByKeyUpc(upc);
	}

	/**
	 * This gets all of the LEB upcs that match the current wic category and sub category.
	 *
	 * @param wicCategoryId    the wic category id.
	 * @param wicSubCategoryId the wic sub category id.
	 * @return a selling unit wic that matches with category id and sub category id.
	 */
	public List<SellingUnitWic> getLebUpcs(Long wicCategoryId, Long wicSubCategoryId) {
		return this.repository.findByKeyWicCategoryIdAndKeyWicSubCategoryIdOrderByKeyUpc(wicCategoryId, wicSubCategoryId);
	}

	/**
	 * Returns  a list of LEB Sizes to the front end.
	 *
	 * @param wicCategoryId    the wic category id
	 * @param wicSubCategoryId the wic sub category id
	 * @return a list of LEB retail sizes.
	 */
	public List<WicCategoryRetailSize> getLebSizes(Long wicCategoryId, Long wicSubCategoryId) {
		return this.wicCategoryRetailSizeRepository.findByKeyWicCategoryIdAndKeyWicSubCategoryId(
				wicCategoryId, wicSubCategoryId);
	}

	/**
	 * Retrieves upc audit information for a selling unit.
	 *
	 * @param upc    the selling units upc to search on.
	 * @param filter the filter
	 * @return upc audit information
	 */
	public List<AuditRecord> getUpcAuditInformation(Long upc, String filter) {
		return this.auditService.getSellingUnitAuditInformation(upc, filter);
	}

	/**
	 * Gets uom list.
	 *
	 * @return the uom list
	 */
	public List<RetailUnitOfMeasure> getUomList() {

		return this.retailUnitOfMeasureRepository.findAll();
	}

	/**
	 * Update upc info.
	 *
	 * @param sellingUnit the selling unit
	 */
	public void updateUpcInfo(SellingUnit sellingUnit) {

		SellingUnit updatedSellingUnit = this.saveUpcInfo(sellingUnit);
		try{
		  this.productManagementServiceClient.updateUpcInfo(updatedSellingUnit);
		} catch (Exception e) {
			throw new SoapException(e.getMessage());
		}
	}

	/**
	 * Save upc info selling unit.
	 *
	 * @param sellingUnit the selling unit
	 * @return the selling unit
	 */
	public SellingUnit saveUpcInfo(SellingUnit sellingUnit) {
		SellingUnit userEditableSellingUnit = new SellingUnit();

		userEditableSellingUnit.setUpc(sellingUnit.getUpc());
		userEditableSellingUnit.setProdId(sellingUnit.getProdId());

		if(sellingUnit.getTagSize() != null && this.userInfo.canUserEditResource(ResourceConstants.UPC_INFO_PRODUCT_SIZE)){
			userEditableSellingUnit.setTagSize(sellingUnit.getTagSize());
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.UPC_INFO_QUANTITY)){
			userEditableSellingUnit.setQuantity(sellingUnit.getQuantity());
		}
		if(sellingUnit.getRetailUnitOfMeasureCode() != null && this.userInfo.canUserEditResource(ResourceConstants.UPC_INFO_UOM)){
			userEditableSellingUnit.setRetailUnitOfMeasureCode(sellingUnit.getRetailUnitOfMeasureCode());
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.UPC_INFO_DSD_DELETE)) {
			userEditableSellingUnit.setDsdDeleteSwitch(sellingUnit.isDsdDeleteSwitch());
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.UPC_INFO_DSD_OVERRIDE)) {
			userEditableSellingUnit.setDsdDeptOverideSwitch(sellingUnit.isDsdDeptOverideSwitch());
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.UPC_INFO_BONUS)) {
			userEditableSellingUnit.setDsdDeptOverideSwitch(sellingUnit.isDsdDeptOverideSwitch());
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.UPC_INFO_BONUS)) {
			userEditableSellingUnit.setBonusSwitch(sellingUnit.getBonusSwitch());
		}
		if (this.userInfo.canUserEditResource(ResourceConstants.UPC_INFO_RETAIL_WEIGHT)) {
			userEditableSellingUnit.setRetailWeight(sellingUnit.getRetailWeight());
		}
		return userEditableSellingUnit;
	}

	/**
	 * Update dimensions.
	 *
	 * @param dimensionsParameters the dimensions parameters
	 */
	public void updateDimensions(DimensionsParameters dimensionsParameters) {

		GoodsProduct userEditableGoodsProduct = this.saveDimensions(dimensionsParameters);
		try{
			this.productManagementServiceClient.updateDimensions(userEditableGoodsProduct);
		} catch (Exception e) {
			throw new SoapException(e.getMessage());
		}

	}

	/**
	 * Save dimensions goods product.
	 *
	 * @param dimensionsParameters the dimensions parameters
	 * @return the goods product
	 */
	public GoodsProduct saveDimensions(DimensionsParameters dimensionsParameters) {

		GoodsProduct updatedGoodsProduct = dimensionsParameters.getProductMaster().getGoodsProduct();
		GoodsProduct userEditableGoodsProduct = new GoodsProduct();

		if(dimensionsParameters.getProductMaster().getGoodsProduct() != null) {

			if(dimensionsParameters.getProductMaster().getProdId() != null){
				userEditableGoodsProduct.setProdId(dimensionsParameters.getProductMaster().getProdId());
			}
			if(updatedGoodsProduct.getRetailLength() != null) {
				userEditableGoodsProduct.setRetailLength(updatedGoodsProduct.getRetailLength());
			}
			if(updatedGoodsProduct.getRetailWidth() != null) {
				userEditableGoodsProduct.setRetailWidth(updatedGoodsProduct.getRetailWidth());
			}
			if(updatedGoodsProduct.getRetailHeight() != null) {
				userEditableGoodsProduct.setRetailHeight(updatedGoodsProduct.getRetailHeight());
			}
			if(updatedGoodsProduct.getRetailWeight() != null) {
				userEditableGoodsProduct.setRetailWeight(updatedGoodsProduct.getRetailWeight());
			}

		}

		return userEditableGoodsProduct;
	}

	/**
	 * Retrieves dimensions audit information for a product.
	 *
	 * @param prodId    the product ID to search on.
	 * @return dimensions audit information
	 */
	public List<AuditRecord> getDimensionsAuditInformation(Long prodId) {
		return this.auditService.getDimensionsAuditInformation(prodId);
	}
}
