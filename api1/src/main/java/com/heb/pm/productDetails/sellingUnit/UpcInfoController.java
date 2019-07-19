/*
 *  UpcInfoController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.sellingUnit;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.*;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.product.ProductInfoService;
import com.heb.pm.product.UpcService;
import com.heb.pm.productDetails.product.SellingUnitWicResolver;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * This represents the controller for the upc info screen.
 *
 * @author l730832
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + UpcInfoController.UPC_INFO_URL)
@AuthorizedResource(ResourceConstants.UPC_INFO)
public class UpcInfoController {

	private static final Logger logger = LoggerFactory.getLogger(UpcInfoController.class);

	/**
	 * The constant UPC_INFO_URL.
	 */
	protected static final String UPC_INFO_URL = "/upcInfo";

	private static final String DEFAULT_RETRIEVED_CROSS_LINKED_LIST_OF_UPCS = "User %s from IP %s has requested the list" +
			"of cross linked list of upcs for wic apl id: %d.";
	private static final String RETRIEVED_CROSS_LINKED_LIST_OF_UPCS = "UpcInfoController.getCrossLinkedListOfUPCs";

	private static final String GET_LIST_OF_CROSS_LINKED_UPCS = "List of Cross linked upcs for wic apl id: %d.";

	private static final String FIND_WICS_BY_UPC = "Wics tied to UPC: %d have been requested.";

	private static final String GET_LEB_UPCS = "LEB UPCs with category id: %d and sub category id: %d have been requested.";

	private static final String GET_LEB_SIZES = "LEB Sizes with category id: %d and sub category id: %d have been requested.";

	private static final String LOG_UPC_INFO_AUDIT_BY_UPC = "User %s from IP %s has requested UPC Info Audit" +
			"information with upc %s.";

	private static final String LOG_UPC_INFO_AUDIT_BY_UPC_COMPLETE ="UPC Info Audit information has been retrieved" +
			"for Selling Unit %s.";

	private static final String LOG_DIMENSIONS_AUDIT_BY_PRODUCT_ID = "User %s from IP %s has requested UPC Info Dimensions audit information for prod ID: %s";

	private static final String GET_DIMENSIONS_AUDITS = "/getDimensionsAudits";

	private static final String SUBMIT_UPC_INFO_UPDATE = "User %s from IP %s has submitted an UPC Info details update : %s.";

	@Autowired
	private UpcInfoService upcInfoService;

	@Autowired
	private UpcService upcService;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ProductInfoService productInfoService;

	private SellingUnitResolver resolver = new SellingUnitResolver();

	private ProductInfoResolver productInfoResolver = new ProductInfoResolver();

	private LazyObjectResolver<SellingUnitWic> sellingUnitWicResolver = new SellingUnitWicResolver();

	private class SellingUnitResolver implements LazyObjectResolver<SellingUnit> {

		@Override
		public void fetch(SellingUnit d) {
			if (d.isProductPrimary()) {
				d.getNutritionalClaims().size();
			}
		}
	}

	/**
	 * Retrieves a list of cross linked upcs according to the wic APL id and sorts the list.
	 *
	 * @param wicAplId the wic apl id.
	 * @param request  the http servlet request
	 * @return list of cross linked list of upcs.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "getCrossLinkedListOfUPCs")
	public ModifiedEntity<List<SellingUnitWic>> getListOfCrossLinkedUPCs(
			@RequestParam(value = "wicAplId") Long wicAplId,
			HttpServletRequest request) {
		List<SellingUnitWic> wicProductDetails = this.upcInfoService.getCrossLinkedListOfUPCs(wicAplId);
		wicProductDetails.forEach(sellingUnitWic -> this.sellingUnitWicResolver.fetch(sellingUnitWic));
		this.logGetListOfCrossLinkedUPCs(wicAplId);
		String updateMessage = this.messageSource.getMessage(
				UpcInfoController.RETRIEVED_CROSS_LINKED_LIST_OF_UPCS,
				new Object[]{wicAplId},
				UpcInfoController.DEFAULT_RETRIEVED_CROSS_LINKED_LIST_OF_UPCS, request.getLocale());
		return new ModifiedEntity<>(wicProductDetails, updateMessage);
	}

	/**
	 * Finds the list of wics tied to the upc.
	 *
	 * @param upc     The upc that the wics are being retrieved for.
	 * @param request the http request.
	 * @return A list of wics tied to the upc.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "getByUPC")
	public List<SellingUnitWic> findByUpc(@RequestParam(value = "upc") Long upc,
										  HttpServletRequest request) {
		this.logFindByUPC(upc);
		return this.upcInfoService.findByUpc(upc);
	}

	/**
	 * Finds the list of LEB upcs that match the current wic's category id and sub category id.
	 *
	 * @param wicCategoryId    the wic category id
	 * @param wicSubCategoryId the wic sub category id
	 * @param request          the http request.
	 * @return a list of selling unit wics.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "getLebUpcs")
	public List<SellingUnitWic> getLebUpcs(@RequestParam(value = "wicCategoryId") Long wicCategoryId,
										   @RequestParam(value = "wicSubCategoryId") Long wicSubCategoryId,
										   HttpServletRequest request) {
		this.logGetLebUpcs(wicCategoryId, wicSubCategoryId);
		List<SellingUnitWic> sellingUnitWics = this.upcInfoService.getLebUpcs(wicCategoryId, wicSubCategoryId);
		sellingUnitWics.forEach(sellingUnitWic -> this.sellingUnitWicResolver.fetch(sellingUnitWic));
		return sellingUnitWics;
	}

	/**
	 * This gets a list of Leb Sizes. An LEB size is a wic category retail size.
	 *
	 * @param wicCategoryId    the wic category id
	 * @param wicSubCategoryId the wic sub category id
	 * @param request          the servlet request
	 * @return a list of wic category retail size keys
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "getLebSizes")
	public List<WicCategoryRetailSize> getLebSizes(@RequestParam(value = "wicCategoryId") Long wicCategoryId,
												   @RequestParam(value = "wicSubCategoryId") Long wicSubCategoryId,
												   HttpServletRequest request) {
		this.logGetLebSizes(wicCategoryId, wicSubCategoryId);
		return this.upcInfoService.getLebSizes(wicCategoryId, wicSubCategoryId);
	}

	/**
	 * Retrieves UPC info audit information.
	 *
	 * @param sellingUnit The sellingUnit that the audit is being searched on.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of upc info audits attached to given item code.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = "/upcInfoAudit")
	public List<AuditRecord> getUpcInfoAuditInfo(@RequestBody SellingUnit sellingUnit, HttpServletRequest request) {
		this.logGetUpcInfoAuditInformation(request.getRemoteAddr(), sellingUnit.getUpc());
		List<AuditRecord> casePackAudit = this.upcInfoService.getUpcAuditInformation(sellingUnit.getUpc(), FilterConstants.UPC_INFO_AUDIT);
		this.logGetUpcInfoAuditInformationComplete(sellingUnit.getUpc());
		return casePackAudit;
	}

	/**
	 * Gets uom list.
	 *
	 * @return the uom list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "/getUomList")
	public List<RetailUnitOfMeasure> getUomList() {

		List<RetailUnitOfMeasure> retailUnitOfMeasures = this.upcInfoService.getUomList();

		return retailUnitOfMeasures;
	}

	/**
	 * Save upc info selling unit.
	 *
	 * @param sellingUnit the selling unit
	 * @param request     the request
	 * @return the selling unit
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/saveUpcInfo")
	public ProductMaster saveUpcInfo(@RequestBody SellingUnit sellingUnit, HttpServletRequest request) {

		this.logSubmitUpcInfoUpdate(request.getRemoteAddr(), sellingUnit);

		 this.upcInfoService.updateUpcInfo(sellingUnit);

		ProductMaster productMaster = this.productInfoService.findProductInfoByProdId(sellingUnit.getProdId());

		this.productInfoResolver.fetch(productMaster);

		return productMaster;
	}

	/**
	 * Save dimensions product master.
	 *
	 * @param dimensionsParameters the dimensions parameters
	 * @param request              the request
	 * @return the product master
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/saveDimensions")
	public ProductMaster saveDimensions(@RequestBody DimensionsParameters dimensionsParameters, HttpServletRequest request) {

		this.upcInfoService.updateDimensions(dimensionsParameters);

		ProductMaster productMaster = this.productInfoService.findProductInfoByProdId(dimensionsParameters.getProductMaster().getProdId());

		this.productInfoResolver.fetch(productMaster);

		return productMaster;
	}

	/**
	 * Logs the completion of upc info audit information.
	 */
	private void logGetUpcInfoAuditInformationComplete(Long itemCode) {
		UpcInfoController.logger.info(
				String.format(UpcInfoController.LOG_UPC_INFO_AUDIT_BY_UPC_COMPLETE, itemCode));
	}

	/**
	 * Logs the retrieval of upc audit information.
	 * @param remoteAddr
	 * @param itemCode
	 */
	private void logGetUpcInfoAuditInformation(String remoteAddr, Long itemCode) {
		UpcInfoController.logger.info(
				String.format(
						UpcInfoController.LOG_UPC_INFO_AUDIT_BY_UPC,
						this.userInfo.getUserId(), remoteAddr, itemCode)
		);
	}

	/**
	 * Log find by UPC.
	 *
	 * @param upc The upc that the wics are being retrieved for.
	 */
	private void logFindByUPC(Long upc) {
		UpcInfoController.logger.info(
				String.format(UpcInfoController.FIND_WICS_BY_UPC, upc)
		);
	}

	/**
	 * Logs get list of cross linked upcs.
	 *
	 * @param wicAplId The wicAplId that is used to retrieve the list of cross linked upcs.
	 */
	private void logGetListOfCrossLinkedUPCs(Long wicAplId) {
		UpcInfoController.logger.info(
				String.format(UpcInfoController.GET_LIST_OF_CROSS_LINKED_UPCS, wicAplId)
		);
	}

	/**
	 * Logs get Leb Upcs.
	 *
	 * @param wicCategoryId The category id for the current wic.
	 * @param wicSubCategoryId The sub category id for the current wic.
	 */
	private void logGetLebUpcs(Long wicCategoryId, Long wicSubCategoryId) {
		UpcInfoController.logger.info(
				String.format(UpcInfoController.GET_LEB_UPCS, wicCategoryId, wicSubCategoryId)
		);
	}

	/**
	 * Logs get Leb Sizes.
	 *
	 * @param wicCategoryId the wic category id.
	 * @param wicSubCategoryId the wic sub category id.
	 */
	private void logGetLebSizes(Long wicCategoryId, Long wicSubCategoryId) {
		UpcInfoController.logger.info(
				String.format(UpcInfoController.GET_LEB_SIZES, wicCategoryId, wicSubCategoryId)
		);
	}

	/**
	 * Retrieves Dimensions audit information.
	 * @param prodId The Product ID that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of Dimensions audits attached to given product ID.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = UpcInfoController.GET_DIMENSIONS_AUDITS)
	public List<AuditRecord> getDimensionsAuditInfo(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {

		this.logGetDimensionsAuditInformation(request.getRemoteAddr(), prodId);

		List<AuditRecord> dimensionsAuditRecords = this.upcInfoService.getDimensionsAuditInformation(prodId);

		return dimensionsAuditRecords;
	}

	/**
	 * Logs get dimensions audit information by prodId.
	 *
	 * @param ip The user's ip.
	 * @param prodId The prodId being searched on.
	 */
	private void logGetDimensionsAuditInformation(String ip, Long prodId) {
		UpcInfoController.logger.info(
				String.format(UpcInfoController.LOG_DIMENSIONS_AUDIT_BY_PRODUCT_ID, this.userInfo.getUserId(), ip, prodId)
		);
	}

	/**
	 * Log changes made to the UPC Info attributes
	 *
	 * @param ip the ip where the request came from
	 * @param sellingUnit the new UPC information
	 */
	private void logSubmitUpcInfoUpdate(String ip, SellingUnit sellingUnit) {

		UpcInfoController.logger.info(
				String.format(UpcInfoController.SUBMIT_UPC_INFO_UPDATE, this.userInfo.getUserId(),
						ip, sellingUnit));
	}
}