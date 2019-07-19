/*
 * ProductManagementServiceClient
 *  *
 *  *
 *  *  Copyright (c) 2016 HEB
 *  *  All rights reserved.
 *  *
 *  *  This software is the confidential and proprietary information
 *  *  of HEB.
 *  *
 */

package com.heb.pm.ws;

import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.*;
import com.heb.pm.productDetails.casePack.PssDepartment;
import com.heb.pm.productDetails.product.OnlineAttributes.RelatedProductsService;
import com.heb.pm.productDetails.product.eCommerceView.ProductECommerceViewService;
import com.heb.pm.productDetails.product.eCommerceView.ProductECommerceViewUtil;
import com.heb.pm.productDiscontinue.RemoveFromStores;
import com.heb.pm.repository.ProductFullfilmentChanelRepository;
import com.heb.pm.repository.ProductInfoRepository;
import com.heb.pm.upcMaintenance.UpcSwap;
import com.heb.util.controller.UserInfo;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.ProductManagementServicePort;
import com.heb.xmlns.ei.ProductManagementServiceServiceagent;
import com.heb.xmlns.ei.ProviderFaultSchema;
import com.heb.xmlns.ei.goodsprod.GoodsProd;
import com.heb.xmlns.ei.itemmaster.ItemMaster;
import com.heb.xmlns.ei.pdshipper.PdShipper;
import com.heb.xmlns.ei.prodchnl.ProdChnlUpdate;
import com.heb.xmlns.ei.proddesctxt.ProdDescTxt;
import com.heb.xmlns.ei.proditem.ProdItem;
import com.heb.xmlns.ei.prodrlshp.ProdRlshp;
import com.heb.xmlns.ei.prodscncdextent.ProdScnCdExtent;
import com.heb.xmlns.ei.prodscncodes.ProdScnCodes;
import com.heb.xmlns.ei.prodshpngexcp.ProdShpngExcp;
import com.heb.xmlns.ei.productmanagement.update_product_reply.UpdateProductReply;
import com.heb.xmlns.ei.productmanagement.update_product_request.UpdateProductRequest;
import com.heb.xmlns.ei.productmaster.ProductMaster;
import com.heb.xmlns.ei.productupdateresponse.Detail;
import com.heb.xmlns.ei.productupdateresponse.ProductUpdateResponse;
import com.heb.xmlns.ei.rxprod.RxProd;
import com.heb.xmlns.ei.swap_scancodes_request.SwapScanCodesRequest;
import com.heb.xmlns.ei.tbcoprod.TbcoProd;
import com.heb.xmlns.ei.venditmfctryxref.VendItmFctryXref;
import com.heb.xmlns.ei.vendlocimprtitm.VendLocImprtItm;
import com.heb.xmlns.ei.vendlocitm.VendLocItm;
import com.heb.xmlns.ei.whselocitm.WhseLocItm;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Provides access to service endpoint for product management service.
 *
 * @author l730832
 * @since 2.1.0
 */
@Service
public class ProductManagementServiceClient extends BaseWebServiceClient
		<ProductManagementServiceServiceagent, ProductManagementServicePort> {

	private static final Logger logger = LoggerFactory.getLogger(ProductManagementServiceClient.class);

	@Value("${productManagementService.uri}")
	private String uri;

	@Autowired
	private UserInfo userInfo;

	// error messages
	private static final String STRING_SUCCESS = "Success";
	private static final String PRODUCT_UPDATE_SUCCESSFUL = "Y";

	// Both to DSD webservice  operation codes
	private static final String DEL_BOTH_TO_DSD = "DEL ";

	// DSD to both webservice operation code
	private static final String COPY_DSD_TO_BOTH = "COPY";
	private static final String SWAP_WHS_TO_WHS = "SWAP";

	// Warehouse to warehouse webservice operation codes
	private static final String MOVE_PRIMARY_TO_PRIMARY = "MP2P";
	private static final String MOVE_PRIMARY_TO_ASSOCIATE = "MP2A";
	private static final String MOVE_ASSOCIATE_TO_PRIMARY = "MA2P";
	private static final String MOVE_ASSOCIATE_TO_ASSOCIATE = "MA2A";
	private static final String SET_PRIMARY_UPC = "SET ";

	// Add associate operation code
	private static final String ADD_ASSOCIATE = "ADD";

	// Message Text
	private static final String WHS_TO_WHS_TYPE = "WHS2WHS";
	private static final String DSD_TO_BOTH_TYPE = "DSD2BOTH";
	private static final String BOTH_TO_DSD_TYPE = "BOTH2DSD";
	private static final String WHS_TO_WHS_SWAP_TYPE = "SWAP";
	private static final String ADD_ASSOCIATE_TYPE = "ADDASSOC";

	// error messages
	private static final String ERROR_WEB_SERVICE_UPC_SWAP_RESPONSE = "Error with this swap: %s.";
	private static final String ERROR_WEB_SERVICE_RESPONSE = "Error with this remove: %s.";
	private static final String ERROR_WEB_SERVICE_UPDATE_RESPONSE = "Error with this update: %s.";
	private static final String INVALID_SERVICE_RESPONSE = "Unknown response from save. Please check the product.";

	//WEBSERVICE ACTION_CODE_KEY CODES
	private static final String ADD_ACTION = "A";
	private static final String UPDATE_ACTION = "";
	private static final String DELETE_ACTION = "D";

	// REPRESENTS AN EMPTY VALUE
	private static final String EMPTY = "0";
	private static final String SINGLE_SPACE = " ";

	// The table handle for UPC Swap
	private static final String UPC_SWAP_TABLE_HANDLE = "1027";

	// Log Messages
	private static final String SUBMIT_SWAP_LOG_MESSAGE = "Submiting UPC Swap";
	private static final String ONLINE_ATTRIBUTE_UPDATE_BEGIN="User %s is trying to update the online attributes for product with id %s";
	private static final String ONLINE_ATTRIBUTE_UPDATE_COMPLETE="User %s has completed an update the online attributes for product with id %s";

	// Customer frienldy description and language types.
	private static final String ENGLISH_DESCRIPTION_TAG = "ENG";
	private static final String SPANISH_DESCRIPTION_TAG = "SPN";
	private static final String CUSTOMER_FRIENDLY_TAG_1 = "TAG1";
	private static final String CUSTOMER_FRIENDLY_TAG_2 = "TAG2";
	private static final String PRIMO_PICK_LONG_DESCRIPTION = "PRIML";

	//Product Maintenance source system number.
	private static final String PRODUCT_MAINTENANCE_SOURCE_SYSTEM = "4";

	/**
	 * PROD_EXT_DTA_CD_FOR_ECOM_DES1.
	 */
	public static final String PROD_EXT_DTA_CD_FOR_ECOM_DES1 = "ESHRT";
	/**
	 * PROD_EXT_DTA_CD_FOR_ECOM_DES2.
	 */
	public static final String PROD_EXT_DTA_CD_FOR_ECOM_DES2 = "ELONG";
	/**
	 * SALSCHNLCD_03.
	 */
	private static final String SALSCHNLCD_03 = "03";
	/**
	 * SALSCHNLCD_04.
	 */
	private static final String SALSCHNLCD_04 = "04";
	/**
	 * SALSCHNLCD_05.
	 */
	private static final String SALSCHNLCD_05 = "05";
	private static final String DEFAULT_TRUE_STRING = "Y";
	private static final String DEFAULT_FALSE_STRING = "N";

	private static final String DATE_FORMAT_YYYYMMDD="yyy-MM-dd";

	private static final String COMMA = ", ";

	@Autowired
	private ProductFullfilmentChanelRepository productFullfilmentChanelRepository;
	@Autowired
	private ProductInfoRepository productInfoRepository;

	/**
	 * Update UPC Info Dimensions.
	 *
	 * @param goodsProduct
	 * @throws CheckedSoapException
	 */
	public void updateDimensions(GoodsProduct goodsProduct) throws CheckedSoapException {
		UpdateProductRequest request= new UpdateProductRequest();
		Integer workId = this.getWorkId();
		request.setTrackingNbr(workId.toString());
		request.setAuthentication(this.getAuthentication());
		ProductMaster productMaster = new ProductMaster();
		productMaster.setPRODID(goodsProduct.getProdId().toString());
		productMaster.setWorkId(workId.toString());
		GoodsProd goodsProd = new GoodsProd();
		goodsProd.setPRODID(goodsProduct.getProdId().toString());
		goodsProd.setVCLSTUPDTUID(this.userInfo.getUserId());
		if(goodsProduct.getRetailHeight() != null) {
			goodsProd.setRETLUNTHT(goodsProduct.getRetailHeight().toString());
		}
		if(goodsProduct.getRetailLength() != null) {
			goodsProd.setRETLUNTLN(goodsProduct.getRetailLength().toString());
		}
		if(goodsProduct.getRetailWidth() != null) {
			goodsProd.setRETLUNTWD(goodsProduct.getRetailWidth().toString());
		}
		if(goodsProduct.getRetailWeight() != null) {
			goodsProd.setRETLUNTWT(goodsProduct.getRetailWeight().toString());
		}
		productMaster.setGoodsProd(goodsProd);
		request.getProductMaster().add(productMaster);
		this.updateProduct(request);
	}

	/**
	 * Populates Service object with goodS Product data.
	 *
	 * @param goodsProduct
	 * @param upc
	 * @return
	 */
	private ProdScnCodes getProdScanCodesFromGoodsProduct(GoodsProduct goodsProduct, Long upc) {
		ProdScnCodes prodScnCodes = new ProdScnCodes();
		prodScnCodes.setSCNCDID(String.valueOf(upc));

		if(goodsProduct.getRetailWeight() != null) {
			prodScnCodes.setRETLUNTWT(goodsProduct.getRetailWeight().toString());
		}
		if(goodsProduct.getRetailHeight() != null) {
			prodScnCodes.setRETLUNTHT(goodsProduct.getRetailHeight().toString());
		}
		if(goodsProduct.getRetailWidth() != null) {
			prodScnCodes.setRETLUNTWD(goodsProduct.getRetailWidth().toString());
		}
		if(goodsProduct.getRetailLength() != null) {
			prodScnCodes.setRETLUNTLN(goodsProduct.getRetailLength().toString());
		}

		prodScnCodes.setVCLSTUPDTUSRID(this.userInfo.getUserId());
		prodScnCodes.setPRODID(String.valueOf(goodsProduct.getProdId()));

		return prodScnCodes;
	}

	/**
	 * Update upc info.
	 *
	 * @param sellingUnit the selling unit
	 * @throws CheckedSoapException the checked soap exception
	 */
	public void updateUpcInfo(SellingUnit sellingUnit) throws CheckedSoapException {

		UpdateProductRequest request= new UpdateProductRequest();
		Integer workId = this.getWorkId();
		request.setTrackingNbr(workId.toString());
		request.setAuthentication(this.getAuthentication());
		ProductMaster productMaster = new ProductMaster();
		productMaster.setPRODID(String.valueOf(sellingUnit.getProdId()));
		productMaster.setWorkId(workId.toString());

		productMaster.getProdScnCodes().add(this.getProdScanCodesFromSellingUnit(sellingUnit));

		if(sellingUnit.getRetailWeight() != null){
			GoodsProd goodsProd = new GoodsProd();
			goodsProd.setPRODID(String.valueOf(sellingUnit.getProdId()));
			goodsProd.setVCLSTUPDTUID(this.userInfo.getUserId());
			if(sellingUnit.getRetailWeight() != null) {
				goodsProd.setRETLUNTWT(sellingUnit.getRetailWeight().toString());
			}
			productMaster.setGoodsProd(goodsProd);
		}

		request.getProductMaster().add(productMaster);

		this.updateProduct(request);

	}

	/**
	 * Gets prod scan codes from selling unit.
	 *
	 * @param sellingUnit the selling unit
	 * @return the prod scan codes from selling unit
	 */
	public ProdScnCodes getProdScanCodesFromSellingUnit(SellingUnit sellingUnit) {

		ProdScnCodes prodScnCodes = new ProdScnCodes();
		prodScnCodes.setSCNCDID(String.valueOf(sellingUnit.getUpc()));
		if(sellingUnit.getTagSize() != null) {
			prodScnCodes.setTAGSZDES(sellingUnit.getTagSize());
		}
		if(sellingUnit.getQuantity() != null) {
			prodScnCodes.setRETLUNTSELLSZ1(String.valueOf(sellingUnit.getQuantity()));
		}
		if(sellingUnit.getRetailUnitOfMeasureCode() != null) {
			prodScnCodes.setRETLSELLSZCD1(sellingUnit.getRetailUnitOfMeasureCode());
		}
		if(sellingUnit.isDsdDeleteSwitch() != null) {
			prodScnCodes.setDSDDELDSW(this.convertBooleanToString(sellingUnit.isDsdDeleteSwitch()));
		}
		if(sellingUnit.isDsdDeptOverideSwitch() != null) {
			prodScnCodes.setDSDDEPTOVRDSW(this.convertBooleanToString(sellingUnit.isDsdDeptOverideSwitch()));
		}
		if(sellingUnit.isBonusSwitch() != null) {
			prodScnCodes.setBNSSCNCDSW(this.convertBooleanToString(sellingUnit.isBonusSwitch()));
		}
		if (sellingUnit.getRetailWeight() != null) {
			prodScnCodes.setRETLUNTWT(sellingUnit.getRetailWeight().toString());
		}
		prodScnCodes.setVCLSTUPDTUSRID(this.userInfo.getUserId());
		prodScnCodes.setPRODID(String.valueOf(sellingUnit.getProdId()));

		return prodScnCodes;

	}

	/**
	 * Update upc info request string.
	 *
	 * @param request the request
	 * @return the string
	 * @throws CheckedSoapException the checked soap exception
	 */
	public String updateUpcInfoRequest(UpdateProductRequest request) throws CheckedSoapException{

		try {
			UpdateProductReply reply = this.getPort().updateProductDetails(request);
			if(!reply.getProductUpdateResponse().isEmpty()){
				if(reply.getProductUpdateResponse().get(0).getDetail().get(0).getSucessFlag().equals("Y")){
					return null;
				} else {
					throw new CheckedSoapException(reply.getProductUpdateResponse().get(0).getDetail().get(0).getReturnMsg());
				}
			}
		} catch (ProviderFaultSchema fault) {
			fault.printStackTrace();
			throw new CheckedSoapException(String.format(ERROR_WEB_SERVICE_RESPONSE, fault.getFaultInfo().getProviderSOAPErrorMsg().get(0).getErrorCode()));
		}
		return StringUtils.EMPTY;

	}

	/**
	 * Calls the ProductManagement service to send the updated remove from stores switch.
	 *
	 * @param returnList the return list
	 * @return the string
	 * @throws CheckedSoapException the checked soap exception
	 */
	public String submitRemoveFromStoresSwitch(RemoveFromStores returnList) throws CheckedSoapException {
		UpdateProductRequest request = new UpdateProductRequest();
		ProductMaster productMaster = new ProductMaster();
		ProdScnCodes prodScnCodes;

		request.setAuthentication(this.getAuthentication());

		prodScnCodes = new ProdScnCodes();
		prodScnCodes.setACTIONCD("U");
		prodScnCodes.setPROCSCNMAINTSW(returnList.isRemovedInStores() ? "N" : "Y");//Converting to N for false and Y for true
		prodScnCodes.setSCNCDID(String.valueOf(returnList.getUpc()));
		productMaster.getProdScnCodes().add(prodScnCodes);
		request.getProductMaster().add(productMaster);
		request.setTrackingNbr("0");

		return this.updateRemoveFromStores(request);

	}

	/**
	 * Update shipping method exceptions.
	 *
	 * @param updateList the update list
	 * @param prodId     the prod id
	 * @param userId     the user id
	 */
	public void updateShippingMethodExceptions(List<CustomShippingMethod> updateList,long prodId, String userId) {

		List<ProductMaster> prodShpngExcpList = new ArrayList<>();
		ProductMaster productMaster = new ProductMaster();
		productMaster.setPRODID(String.valueOf(prodId));
		String workId = this.getWorkId().toString();

		for(CustomShippingMethod customShippingMethod : updateList) {
			ProdShpngExcp prodShpngExcp = new ProdShpngExcp();
			prodShpngExcp.setACTIONCD(customShippingMethod.getActionCode());
			prodShpngExcp.setPRODID(String.valueOf(prodId));
			prodShpngExcp.setCUSTSHPNGMETHCD(String.valueOf(customShippingMethod.getCustomShippingMethod()));
			prodShpngExcp.setVCLSTUPDTUID(userId);

			productMaster.getProdShpngExcp().add(prodShpngExcp);
			prodShpngExcpList.add(productMaster);
		}

		UpdateProductRequest request = new UpdateProductRequest();
		request.getProductMaster().add(productMaster);
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(workId.trim());

		this.updateProduct(request);
	}

	/**
	 * Submits an updatedProductReply to update the procMaintenanceSwitch
	 *
	 * @param request
	 * @return an empty string
	 * @throws CheckedSoapException
	 */
	private String updateRemoveFromStores(UpdateProductRequest request) throws CheckedSoapException {
		try {
			UpdateProductReply reply = this.getPort().updateProductDetails(request);
			if (!reply.getProductUpdateResponse().isEmpty()) {
				if (reply.getProductUpdateResponse().get(0).getDetail().get(0).getSucessFlag().equals("Y")) {
					return null;
				} else {
					throw new CheckedSoapException(reply.getProductUpdateResponse().get(0).getDetail().get(0).getReturnMsg());
				}
			}
		} catch (ProviderFaultSchema fault) {
			fault.printStackTrace();
			throw new CheckedSoapException(String.format(ERROR_WEB_SERVICE_RESPONSE, fault.getFaultInfo().getProviderSOAPErrorMsg().get(0).getErrorCode()));
		}
		return StringUtils.EMPTY;

	}

	/**
	 * Processes a request to add associate UPCs through the UPC Swap function.
	 *
	 * @param addAssociateList This list of requests to add associates.
	 * @throws CheckedSoapException
	 */
	public void submitAddAssociate(List<UpcSwap> addAssociateList) throws CheckedSoapException {

		SwapScanCodesRequest request = new SwapScanCodesRequest();
		request.setAuthentication(this.getAuthentication());

		// Go through the list of requests, and convert them to the SOAP request.
		for (UpcSwap addAssociate : addAssociateList) {

			// Don't try to do an add if the request has an error.
			if (addAssociate.isErrorFound()) {
				continue;
			}

			// The top level of the request.
			com.heb.xmlns.ei.upcswap.UpcSwap swap = new com.heb.xmlns.ei.upcswap.UpcSwap();
			swap.setOPERATION(ProductManagementServiceClient.ADD_ASSOCIATE);
			swap.setUSERID(this.userInfo.getUserId());

			// Set up the source side.
			com.heb.xmlns.ei.upcswap.UpcSwap.SOURCE source = new com.heb.xmlns.ei.upcswap.UpcSwap.SOURCE();
			swap.setSOURCE(source);

			source.setPRODID(BigInteger.valueOf(addAssociate.getSource().getProductId()));
			source.setITEMTYPE(addAssociate.getSource().getItemType());
			source.setITEMID(addAssociate.getSource().getItemCode().toString());
			source.setPRIMARYUPC(addAssociate.getSource().getPrimaryUpc().toString());
			source.setASSOCIATEDUPC(addAssociate.getDestination().getUpc().toString());

			// Set up the destination side. This is basically empty, but it doesn't work with a bunch of nulls.
			com.heb.xmlns.ei.upcswap.UpcSwap.DESTINATION destination =
					new com.heb.xmlns.ei.upcswap.UpcSwap.DESTINATION();
			swap.setDESTINATION(destination);
			destination.setPRODID(BigInteger.ZERO);
			destination.setITEMTYPE(ItemMasterKey.WAREHOUSE);
			destination.setITEMID(Integer.toString(0));
			destination.setPRIMARYUPC(Integer.toString(0));
			destination.setASSOCIATEDUPC(Integer.toString(0));

			// Add the audit information.
			swap.setUpcSwapAud(
					this.setUpcSwapAuditInfo(addAssociate, ProductManagementServiceClient.ADD_ASSOCIATE_TYPE));

			request.getUpcSwap().add(swap);
		}

		this.swapScanCodes(request);
	}

	/**
	 * Calls the product management service to send upc swap.
	 *
	 * @param upcSwap Upc swap to submit to webservice.
	 * @throws CheckedSoapException the checked soap exception
	 */
	public void submitUpcSwap(UpcSwap upcSwap) throws CheckedSoapException {
		SwapScanCodesRequest request = new SwapScanCodesRequest();
		com.heb.xmlns.ei.upcswap.UpcSwap swap = new com.heb.xmlns.ei.upcswap.UpcSwap();
		com.heb.xmlns.ei.upcswap.UpcSwap.SOURCE source = new com.heb.xmlns.ei.upcswap.UpcSwap.SOURCE();
		com.heb.xmlns.ei.upcswap.UpcSwap.DESTINATION destination = new com.heb.xmlns.ei.upcswap.UpcSwap.DESTINATION();

		String operationCode;

		// source and destination item codes are the same
		if (upcSwap.getSource().getItemCode().longValue() == upcSwap.getDestination().getItemCode().longValue()) {
			operationCode = SET_PRIMARY_UPC;
		}

		// UPC is existing primary and will be primary of new item code.
		else if (upcSwap.isSourcePrimaryUpc() && upcSwap.isMakeDestinationPrimaryUpc()) {
			operationCode = MOVE_PRIMARY_TO_PRIMARY;
		}
		// UPC is existing primary and will not be primary of new item code.
		else if (upcSwap.isSourcePrimaryUpc()) {
			operationCode = MOVE_PRIMARY_TO_ASSOCIATE;
		}
		// UPC is not existing primary and will be primary of new item code.
		else if (upcSwap.isMakeDestinationPrimaryUpc()) {
			operationCode = MOVE_ASSOCIATE_TO_PRIMARY;
		}
		// UPC is not existing primary and will not be primary of new item code.
		else {
			operationCode = MOVE_ASSOCIATE_TO_ASSOCIATE;
		}

		source.setPRODID(BigInteger.valueOf(upcSwap.getSource().getProductId()));
		source.setITEMTYPE(ItemMasterKey.WAREHOUSE);
		source.setITEMID(String.valueOf(upcSwap.getSource().getItemCode()));

		switch (operationCode) {
			case SET_PRIMARY_UPC: {
				destination.setPRODID(BigInteger.ZERO);
				destination.setITEMTYPE(String.valueOf(BigInteger.ZERO));
				destination.setITEMID(String.valueOf(BigInteger.ZERO));
				destination.setPRIMARYUPC(String.valueOf(BigInteger.ZERO));
				destination.setASSOCIATEDUPC(String.valueOf(BigInteger.ZERO));
				break;
			}
			default: {
				destination.setPRODID(BigInteger.valueOf(upcSwap.getDestination().getProductId()));
				destination.setITEMTYPE(ItemMasterKey.WAREHOUSE);
				destination.setITEMID(String.valueOf(upcSwap.getDestination().getItemCode()));
				destination.setPRIMARYUPC(String.valueOf(upcSwap.getDestinationPrimaryUpc()));
				destination.setASSOCIATEDUPC(String.valueOf(BigInteger.ZERO));
				break;
			}
		}

		if (upcSwap.isSourcePrimaryUpc()) {
			source.setPRIMARYUPC(String.valueOf(upcSwap.getSourceUpc()));
			if (upcSwap.getSelectSourcePrimaryUpc() != null) {
				source.setASSOCIATEDUPC(String.valueOf(upcSwap.getSelectSourcePrimaryUpc()));

			} else {
				source.setASSOCIATEDUPC(String.valueOf(BigInteger.ZERO));
			}
		} else {
			source.setPRIMARYUPC(String.valueOf(upcSwap.getSelectSourcePrimaryUpc()));
			source.setASSOCIATEDUPC(String.valueOf(upcSwap.getSourceUpc()));
		}

		swap.setSOURCE(source);
		swap.setDESTINATION(destination);

		swap.setUpcSwapAud(setUpcSwapAuditInfo(upcSwap, ProductManagementServiceClient.WHS_TO_WHS_TYPE));
		swap.setUSERID(this.userInfo.getUserId());
		swap.setOPERATION(operationCode);
		request.setAuthentication(this.getAuthentication());
		request.getUpcSwap().add(swap);

		this.swapScanCodes(request);
	}

	/**
	 * Submit whs to whs swap .
	 *
	 * @param upcSwap the upc swap
	 * @throws CheckedSoapException the checked soap exception
	 */
	public void submitWhsToWhsSwap(UpcSwap upcSwap) throws CheckedSoapException {

		SwapScanCodesRequest request = new SwapScanCodesRequest();
		com.heb.xmlns.ei.upcswap.UpcSwap swap = new com.heb.xmlns.ei.upcswap.UpcSwap();
		com.heb.xmlns.ei.upcswap.UpcSwap.SOURCE source = new com.heb.xmlns.ei.upcswap.UpcSwap.SOURCE();
		com.heb.xmlns.ei.upcswap.UpcSwap.DESTINATION destination = new com.heb.xmlns.ei.upcswap.UpcSwap.DESTINATION();

		source.setPRODID(BigInteger.valueOf(upcSwap.getSource().getProductId()));
		source.setITEMTYPE(String.valueOf(upcSwap.getSource().getItemType()));
		source.setITEMID(String.valueOf(upcSwap.getSource().getItemCode()));
		source.setPRIMARYUPC(String.valueOf(upcSwap.getSourceUpc()));
		source.setASSOCIATEDUPC(String.valueOf(BigInteger.ZERO));
		destination.setPRODID(BigInteger.valueOf(upcSwap.getDestination().getProductId()));
		destination.setITEMTYPE(String.valueOf(upcSwap.getDestination().getItemType()));
		destination.setITEMID(String.valueOf(upcSwap.getDestination().getItemCode()));
		destination.setASSOCIATEDUPC(String.valueOf(BigInteger.ZERO));
		destination.setPRIMARYUPC(String.valueOf(upcSwap.getDestinationPrimaryUpc()));

		swap.setSOURCE(source);
		swap.setDESTINATION(destination);
		swap.setUSERID(this.userInfo.getUserId());

		swap.setUpcSwapAud(setUpcSwapAuditInfo(upcSwap, ProductManagementServiceClient.WHS_TO_WHS_SWAP_TYPE));
		swap.setOPERATION(SWAP_WHS_TO_WHS);
		request.setAuthentication(this.getAuthentication());
		request.getUpcSwap().add(swap);

		this.swapScanCodes(request);
	}

	/**
	 * Calls the product management service to send DSD to Both request.
	 *
	 * @param upcSwap DSD to both UPC swap to submit to webservice.
	 * @throws CheckedSoapException the checked soap exception
	 */
	public void submitDsdToBoth(UpcSwap upcSwap) throws CheckedSoapException {
		SwapScanCodesRequest request = new SwapScanCodesRequest();
		com.heb.xmlns.ei.upcswap.UpcSwap swap = new com.heb.xmlns.ei.upcswap.UpcSwap();
		com.heb.xmlns.ei.upcswap.UpcSwap.SOURCE source = new com.heb.xmlns.ei.upcswap.UpcSwap.SOURCE();
		com.heb.xmlns.ei.upcswap.UpcSwap.DESTINATION destination = new com.heb.xmlns.ei.upcswap.UpcSwap.DESTINATION();

		source.setPRODID(BigInteger.valueOf(upcSwap.getSource().getProductId()));
		source.setITEMTYPE(ItemMasterKey.DSD);
		source.setITEMID(String.valueOf(upcSwap.getSource().getItemCode()));
		source.setPRIMARYUPC(String.valueOf(upcSwap.getSourceUpc()));
		source.setASSOCIATEDUPC(String.valueOf(BigInteger.ZERO));
		destination.setPRODID(BigInteger.valueOf(upcSwap.getDestination().getProductId()));
		destination.setITEMTYPE(String.valueOf(ItemMasterKey.WAREHOUSE));
		destination.setITEMID(String.valueOf(upcSwap.getDestination().getItemCode()));
		destination.setASSOCIATEDUPC(String.valueOf(BigInteger.ZERO));
		if (upcSwap.isMakeDestinationPrimaryUpc()) {
			destination.setPRIMARYUPC(String.valueOf(BigInteger.ZERO));
		} else {
			destination.setPRIMARYUPC(String.valueOf(upcSwap.getDestinationPrimaryUpc()));
		}

		swap.setSOURCE(source);
		swap.setDESTINATION(destination);
		swap.setUSERID(this.userInfo.getUserId());

		swap.setUpcSwapAud(setUpcSwapAuditInfo(upcSwap, ProductManagementServiceClient.DSD_TO_BOTH_TYPE));
		swap.setOPERATION(COPY_DSD_TO_BOTH);
		request.setAuthentication(this.getAuthentication());
		request.getUpcSwap().add(swap);

		this.swapScanCodes(request);
	}

	/**
	 * Calls the product management service to send both to DSD request.
	 *
	 * @param upcSwap bothToDSD UPC swap to submit to webservice.
	 * @throws CheckedSoapException the checked soap exception
	 */
	public void submitBothToDsd(UpcSwap upcSwap) throws CheckedSoapException {
		SwapScanCodesRequest request = new SwapScanCodesRequest();
		com.heb.xmlns.ei.upcswap.UpcSwap swap = new com.heb.xmlns.ei.upcswap.UpcSwap();
		com.heb.xmlns.ei.upcswap.UpcSwap.SOURCE source = new com.heb.xmlns.ei.upcswap.UpcSwap.SOURCE();
		com.heb.xmlns.ei.upcswap.UpcSwap.DESTINATION destination = new com.heb.xmlns.ei.upcswap.UpcSwap.DESTINATION();

		source.setPRODID(BigInteger.valueOf(upcSwap.getSource().getProductId()));
		source.setITEMTYPE(ItemMasterKey.WAREHOUSE);
		source.setITEMID(String.valueOf(upcSwap.getSource().getItemCode()));

		if (!upcSwap.isSourcePrimaryUpc()) {

			source.setPRIMARYUPC(String.valueOf(upcSwap.getSelectSourcePrimaryUpc()));
			source.setASSOCIATEDUPC(String.valueOf(upcSwap.getSourceUpc()));
		} else {
			source.setPRIMARYUPC(String.valueOf(upcSwap.getSourceUpc()));
			source.setASSOCIATEDUPC(String.valueOf(BigInteger.ZERO));
		}
		swap.setSOURCE(source);

		//if source upc is primary upc, and the item code had associate upcs, also send new primary
		if (upcSwap.isSourcePrimaryUpc() && upcSwap.getSource().getAssociatedUpcList().size() > 0) {
			destination.setPRODID(BigInteger.valueOf(upcSwap.getSource().getProductId()));
			destination.setITEMTYPE(ItemMasterKey.WAREHOUSE);
			destination.setITEMID(String.valueOf(upcSwap.getSource().getItemCode()));
			destination.setPRIMARYUPC(String.valueOf(upcSwap.getSelectSourcePrimaryUpc()));
			destination.setASSOCIATEDUPC(String.valueOf(BigInteger.ZERO));
		}
		swap.setDESTINATION(destination);
		swap.setUSERID(this.userInfo.getUserId());
		swap.setOPERATION(DEL_BOTH_TO_DSD);

		swap.setUpcSwapAud(setUpcSwapAuditInfo(upcSwap, ProductManagementServiceClient.BOTH_TO_DSD_TYPE));
		request.setAuthentication(this.getAuthentication());
		request.getUpcSwap().add(swap);

		this.swapScanCodes(request);
	}

	/**
	 * Submits a upc swap to product management service.
	 *
	 * @param request The request to send to product management service.
	 * @return String error or success.
	 */
	private void swapScanCodes(SwapScanCodesRequest request) throws CheckedSoapException {
		if (!request.getUpcSwap().isEmpty()) {
			UpdateProductReply reply;
			try {
				ProductManagementServiceClient.logger.info(ProductManagementServiceClient.SUBMIT_SWAP_LOG_MESSAGE);
				reply = this.getPort().swapScanCodes(request);
			} catch (Exception e) {
				throw new CheckedSoapException(e.getMessage(), e.getCause());
			}

			// Even though this submits multiple requests, it only get's back one response. That response
			// is buried inside several levels of object, loop through and see if it's successful.
			for (ProductUpdateResponse response : reply.getProductUpdateResponse()) {
				for (Detail responseDetail : response.getDetail()) {
					if (ProductManagementServiceClient.UPC_SWAP_TABLE_HANDLE.equals(responseDetail.getTableHandle())) {
						if (ProductManagementServiceClient.PRODUCT_UPDATE_SUCCESSFUL.equals(
								responseDetail.getSucessFlag())) {
							// If we get here, then the save was successful
							return;
						} else {
							// If we get here, then the save failed, so throw an error with the message
							throw new CheckedSoapException(responseDetail.getReturnMsg());
						}
					}
				}
			}

			// If we get here, then we didn't get a response with the information we need, so throw an error.
			throw new CheckedSoapException(ProductManagementServiceClient.INVALID_SERVICE_RESPONSE);
		}
	}

	/**
	 * Return the service agent for this client.
	 *
	 * @return ProductManagementServiceServiceagent associated with this client.
	 */
	@Override
	protected ProductManagementServiceServiceagent getServiceAgent() {
		try {
			URL url = new URL(this.getWebServiceUri());
			return new ProductManagementServiceServiceagent(url);
		} catch (MalformedURLException e) {
			ProductManagementServiceClient.logger.error(e.getMessage());
		}
		return new ProductManagementServiceServiceagent();
	}

	/**
	 * Return the port type for this client.
	 *
	 * @param agent The agent to use to create the port.
	 * @return ProductManagementServicePort associated with this client.
	 */
	@Override
	protected ProductManagementServicePort getServicePort(ProductManagementServiceServiceagent agent) {
		return agent.getProductManagementService();
	}

	/**
	 * Return the url for this client.
	 *
	 * @return String url for this client.
	 */
	@Override
	protected String getWebServiceUri() {
		return this.uri;
	}

	/**
	 * Sets the URI to access vendor service. This is primarily used for testing.
	 *
	 * @param uri The URI to access vendor service.
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * Sets the UserInfo for this object to use. This is for testing.
	 *
	 * @param userInfo The UserInfo for this object to use.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Sets the upc swap audit
	 *
	 * @param upcSwap The upcSwap to audit
	 * @return the upc swap audit info
	 */
	public com.heb.xmlns.ei.upcswapaud.UpcSwapAud setUpcSwapAuditInfo(UpcSwap upcSwap, String typeOfSwap) {
		com.heb.xmlns.ei.upcswapaud.UpcSwapAud upcSwapAud = new com.heb.xmlns.ei.upcswapaud.UpcSwapAud();
		upcSwapAud.setPRIMSCNCDSW(upcSwap.isSourcePrimaryUpc() ? "Y" : "N");
		if (upcSwap.getSource().isOnLiveOrPendingPog() != null) {
			upcSwapAud.setSRCONACTVPOGSW(upcSwap.getSource().isOnLiveOrPendingPog() ? "Y" : "N");
		}
		upcSwapAud.setSRCONHANDQTY(String.valueOf(upcSwap.getSource().getBalanceOnHand()));
		upcSwapAud.setSRCONORDQTY(String.valueOf(upcSwap.getSource().getBalanceOnOrder()));
		upcSwapAud.setSRCHASOPENPOSW(upcSwap.getSource().getPurchaseOrderDisplayText().equals("No PO") ? "Y" : "N");
		upcSwapAud.setSRCSCNCDID(String.valueOf(upcSwap.getSourceUpc()));

		if (upcSwap.getDestination() != null) {
			upcSwapAud.setDESTHASOPENPOSW(upcSwap.getDestination().getPurchaseOrderDisplayText().equals("No PO") ? "Y" : "N");
			if (upcSwap.getDestination().isOnLiveOrPendingPog() != null) {
				upcSwapAud.setDESTONACTVPOGSW(upcSwap.getDestination().isOnLiveOrPendingPog() ? "Y" : "N");
			}
			upcSwapAud.setDESTONHANDQTY(String.valueOf(upcSwap.getDestination().getBalanceOnHand()));
			upcSwapAud.setDESTONORDQTY(String.valueOf(upcSwap.getDestination().getBalanceOnOrder()));
			upcSwapAud.setFUTRPRIMSCNCDID(String.valueOf(upcSwap.getDestinationPrimaryUpc()));
			upcSwapAud.setMAKEPRIMSCNCDSW(upcSwap.isMakeDestinationPrimaryUpc() ? "Y" : "N");
		}

		upcSwapAud.setMSGTXT(typeOfSwap);
		upcSwapAud.setSUCCESSSW("Y");
		return upcSwapAud;
	}

	/**
	 * Calls ProductManagementService to update Import Item  information (Vend_Loc_Imprt_Itm).
	 *
	 * @param importItem The importItem.
	 */
	public void updateVendLocImprtItm(ImportItem importItem) {
		VendLocImprtItm vendLocImprtItm = new VendLocImprtItm();

		vendLocImprtItm.setACTIONCD(UPDATE_ACTION);

		vendLocImprtItm.setITMID(importItem.getKey().getItemCode().toString());
		vendLocImprtItm.setITMKEYTYPCD(importItem.getKey().getItemType());
		vendLocImprtItm.setVENDLOCTYPCD(importItem.getKey().getVendorType());
		vendLocImprtItm.setVENDLOCNBR(importItem.getKey().getVendorNumber().toString());

		vendLocImprtItm.setHTSNBR(importItem.getHts1().toString());
		vendLocImprtItm.setHTS2NBR(importItem.getHts2().toString());
		vendLocImprtItm.setHTS3NBR(importItem.getHts3().toString());
		vendLocImprtItm.setPRODNMINORDDES(getValueWithDefaultAsSpace(importItem.getMinOrderDescription()));
		vendLocImprtItm.setPRODNMINORDQTY(importItem.getMinOrderQuantity().toString());
		vendLocImprtItm.setPCKUPPNTTXT(getValueWithDefaultAsSpace(importItem.getPickupPoint()));
		vendLocImprtItm.setCNTRYOFORIGNM(importItem.getCountryOfOrigin());
		vendLocImprtItm.setCNTANSZCD(importItem.getContainerSizeCode());
		vendLocImprtItm.setINCOTRMCD(importItem.getIncoTermCode());
		if (importItem.getProrationDate() != null) {
			vendLocImprtItm.setPRORDT(importItem.getProrationDate().toString());
		}
		if (importItem.getInStoreDate() != null) {
			vendLocImprtItm.setINSTRDT(importItem.getInStoreDate().toString());
		}
		if (importItem.getWarehouseFlushDate() != null) {
			vendLocImprtItm.setWHSEFLSHDT(importItem.getWarehouseFlushDate().toString());
		}
		vendLocImprtItm.setSEASNTXT(getValueWithDefaultAsSpace(importItem.getSeason()));
		vendLocImprtItm.setSELLYY(importItem.getSellByYear().toString());
		vendLocImprtItm.setCOLORDES(getValueWithDefaultAsSpace(importItem.getColor()));
		vendLocImprtItm.setCRTNMRKNGTXT(getValueWithDefaultAsSpace(importItem.getCartonMarking()));
		vendLocImprtItm.setAGENTCOMSNPCT(importItem.getAgentCommissionPercent().toString());
		vendLocImprtItm.setDUTYRTPCT(importItem.getDutyPercent().toString());
		vendLocImprtItm.setDUTYINFOTXT(getValueWithDefaultAsSpace(importItem.getDutyInformation()));
		if (importItem.getDutyConfirmationDate() != null) {
			vendLocImprtItm.setDUTYCNFRMTXT(importItem.getDutyConfirmationDate());
		}
		if (importItem.getFreightConfirmationDate() != null) {
			vendLocImprtItm.setFRTCNFRMTXT(importItem.getFreightConfirmationDate());
		}

		VendLocItm vendLocItm = new VendLocItm();
		vendLocItm.getVendLocImprtItm().add(vendLocImprtItm);

		ItemMaster itemMaster = new ItemMaster();
		itemMaster.getVendLocItm().add(vendLocItm);

		String workId = this.getWorkId().toString();

		ProductMaster productMaster = new ProductMaster();
		productMaster.getItemMaster().add(itemMaster);
		productMaster.setWorkId(workId);

		UpdateProductRequest request = new UpdateProductRequest();
		request.getProductMaster().add(productMaster);
		request.setTrackingNbr(workId);
		request.setAuthentication(this.getAuthentication());
		this.updateProduct(request);
	}

	/**
	 * Calls ProductManagementService to update VendorLocationItem information (vend_loc_itm).
	 *
	 * @param vendorLocationItems The vendorLocationItem.
	 */
	public void updateVendorLocationItem(List<VendorLocationItem> vendorLocationItems) {
		ItemMaster itemMaster = new ItemMaster();
		vendorLocationItems.forEach(vendorLocationItem -> {
			VendLocItm vendLocItm = new VendLocItm();

			vendLocItm.setITMID(vendorLocationItem.getKey().getItemCode().toString().trim());
			vendLocItm.setITMKEYTYPCD(vendorLocationItem.getKey().getItemType().trim());
			vendLocItm.setVENDLOCNBR(vendorLocationItem.getKey().getVendorNumber().toString().trim());
			vendLocItm.setVENDLOCTYPCD(vendorLocationItem.getKey().getVendorType());
			if (vendorLocationItem.getVendItemId() != null){
				vendLocItm.setVENDITMID(StringUtils.trimToEmpty(vendorLocationItem.getVendItemId()));
			}
			if (vendorLocationItem.getPalletQuantity() != null) {
				vendLocItm.setVENDPALQTY(vendorLocationItem.getPalletQuantity().toString());
			}
			if (vendorLocationItem.getCostOwnerId() != null){
				vendLocItm.setCSTOWNID(vendorLocationItem.getCostOwnerId().toString());
			}
			if(vendorLocationItem.getCountryOfOriginId() != null){
				vendLocItm.setCNTRYOFORIGID(vendorLocationItem.getCountryOfOriginId().toString());
			}
			if(vendorLocationItem.getOrderQuantityRestrictionCode() != null) {
				vendLocItm.setORDQTYRSTRCD(vendorLocationItem.getOrderQuantityRestrictionCode().trim());
			}
			if(vendorLocationItem.getCostLinkId() != null) {
				vendLocItm.setCSTLNKID(String.valueOf(vendorLocationItem.getCostLinkId()));
			}
			if (!vendorLocationItem.getItemMaster().getKey().isDsd()) {
				if(vendorLocationItem.getLength() != null) {
					vendLocItm.setVENDLN(vendorLocationItem.getLength().toString().trim());
				}
				if(vendorLocationItem.getWidth() != null) {
					vendLocItm.setVENDWD(vendorLocationItem.getWidth().toString().trim());
				}
				if(vendorLocationItem.getWeight() != null) {
					vendLocItm.setVENDWT(vendorLocationItem.getWeight().toString().trim());
				}
				if(vendorLocationItem.getHeight() != null) {
					vendLocItm.setVENDHT(vendorLocationItem.getHeight().toString().trim());
				}
				if (vendorLocationItem.getNestCube() != null) {
					vendLocItm.setVENDNESTCU(vendorLocationItem.getNestCube().toString());
				}
				if (vendorLocationItem.getNestMax() != null) {
					vendLocItm.setVENDNESTMAX(vendorLocationItem.getNestMax().toString().trim());
				}
				if(vendorLocationItem.getTie() != null){
					vendLocItm.setVENDPALTIE(String.valueOf(vendorLocationItem.getTie()));
				}
				if(vendorLocationItem.getTier() != null){
					vendLocItm.setVENDPALTIER(String.valueOf(vendorLocationItem.getTier()));
				}
			}
			vendLocItm.setLSTUPDTUID(userInfo.getUserId());
			itemMaster.getVendLocItm().add(vendLocItm);
		});

		String workId = this.getWorkId().toString();

		ProductMaster productMaster = new ProductMaster();
		productMaster.getItemMaster().add(itemMaster);
		productMaster.setWorkId(workId);

		UpdateProductRequest request = new UpdateProductRequest();
		request.getProductMaster().add(productMaster);
		request.setTrackingNbr(workId);
		request.setAuthentication(this.getAuthentication());
		this.updateProduct(request);
	}


	/**
	 * Submits an updatedProductRequest to update a product.
	 *
	 * @param request UpdateProductRequest
	 * @throws SoapException A checked soap exception
	 */
	private void updateProduct(UpdateProductRequest request) throws SoapException {
		try {
			UpdateProductReply reply = this.getPort().updateProductDetails(request);
			List<String> errorMessageList = new ArrayList<>();
			if (!reply.getProductUpdateResponse().isEmpty()) {
				for (ProductUpdateResponse response : reply.getProductUpdateResponse()) {
					for (Detail detail : response.getDetail()) {
						if (!detail.getSucessFlag().equals(PRODUCT_UPDATE_SUCCESSFUL)) {
							if(!errorMessageList.contains(detail.getReturnMsg())){
								errorMessageList.add(detail.getReturnMsg());
							}
						}
					}
				}
				if (!errorMessageList.isEmpty()) {
					String errorMessageString = String.join(COMMA, errorMessageList);
					logger.error(errorMessageString);
					throw new SoapException(errorMessageString);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new SoapException(String.format(ERROR_WEB_SERVICE_UPDATE_RESPONSE, e.getMessage()));
		}
	}

	/**
	 * Update mrt information.
	 *
	 *  @param itemMaster shipper
	 *                   Calls webservice response to update the quantity value of the shipper object
	 */
	public void updateMrtInfo(com.heb.pm.entity.ItemMaster itemMaster) {
		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());
		List<Shipper> shipperList = itemMaster.getPrimaryUpc().getShipper();
		ProductMaster productMaster = new ProductMaster();
		for (Shipper shipper : shipperList) {
			PdShipper pdShipper = new PdShipper();
			pdShipper.setACTIONCD(shipper.getActionCode());
			pdShipper.setWorkId(workId.trim());
			pdShipper.setPDSHPRQTY(shipper.getShipperQuantity().toString().trim());
			pdShipper.setPDSHPRUPCNO(String.valueOf(shipper.getKey().getShipperUpc()));
			pdShipper.setPDSHPRTYPCD(String.valueOf(shipper.getShipperTypeCode()));
			pdShipper.setPDUPCNO(String.valueOf(shipper.getKey().getUpc()));
			pdShipper.setLSTUPDTUID(userInfo.getUserId());
			request.getPdShipper().add(pdShipper);
		}
		for(com.heb.pm.entity.ProdItem prodItemEntity: itemMaster.getProdItems()){
			ProdItem prodItem = new ProdItem();
			prodItem.setACTIONCD(prodItemEntity.getActionCode());
			prodItem.setITMKEYTYPCD(itemMaster.getKey().getItemType());
			prodItem.setITMCD(Long.toString(itemMaster.getKey().getItemCode()));
			prodItem.setPRODID(Long.toString(prodItemEntity.getKey().getProductId()));
			prodItem.setRETLPACKQTY(Long.toString(prodItemEntity.getProductCount()));
			productMaster.getProdItem().add(prodItem);
		}
		// Update item size (ITEM_SIZE_TXT) to item master.
		if(StringUtils.isNotBlank(itemMaster.getItemSize())) {
			ItemMaster webserviceItemMaster = new ItemMaster();
			webserviceItemMaster.setACTIONCD("");
			webserviceItemMaster.setITMID(Long.toString(itemMaster.getKey().getItemCode()));
			webserviceItemMaster.setITMKEYTYPCD(itemMaster.getKey().getItemType().trim());
			webserviceItemMaster.setITEMSIZETXT(itemMaster.getItemSize().trim());
			productMaster.getItemMaster().add(webserviceItemMaster);
		}
		if((productMaster.getProdItem() != null && !productMaster.getProdItem().isEmpty()) ||
				!productMaster.getItemMaster().isEmpty()) {
			request.getProductMaster().add(productMaster);
		}
		this.updateProduct(request);
	}

	/**
	 * Update dru quantity.
	 *
	 * @param itemMaster shipper
	 *                   Calls webservice response to update the quantity value of the shipper object
	 */
	public void updateDruQuantity(com.heb.pm.entity.ItemMaster itemMaster) {


		ItemMaster webserviceItemMaster = new ItemMaster();
		webserviceItemMaster.setACTIONCD("");
		webserviceItemMaster.setITMID(Long.toString(itemMaster.getKey().getItemCode()));
		webserviceItemMaster.setITMKEYTYPCD(itemMaster.getKey().getItemType());
		webserviceItemMaster.setDSPLYRDYPALSW(convertBooleanToString(itemMaster.getDisplayReadyUnit()));
		webserviceItemMaster.setSRSAFFTYPCD(itemMaster.getDisplayReadyUnitType().getCode());
		webserviceItemMaster.setSTDSUBSTLOGICSW(convertBooleanToString(itemMaster.getAlwaysSubWhenOut()));
		webserviceItemMaster.setPRODFCNGNBR(Long.toString(itemMaster.getRowsFacing()));
		webserviceItemMaster.setPRODROWDEEPNBR(Long.toString(itemMaster.getRowsDeep()));
		webserviceItemMaster.setPRODROWHINBR(Long.toString(itemMaster.getRowsHigh()));
		webserviceItemMaster.setNBROFORINTNBR(Long.toString(itemMaster.getOrientation()));
		webserviceItemMaster.setLSTSYSUPDTID(PRODUCT_MAINTENANCE_SOURCE_SYSTEM);
		webserviceItemMaster.setVCLSTUPDTUSRID(this.userInfo.getUserId());

		String workId = this.getWorkId().toString();
		ProductMaster productMaster = new ProductMaster();
		productMaster.getItemMaster().add(webserviceItemMaster);
		productMaster.setWorkId(workId);


		UpdateProductRequest request = new UpdateProductRequest();
		request.getProductMaster().add(productMaster);
		request.setTrackingNbr(workId.trim());
		request.setAuthentication(this.getAuthentication());
		this.updateProduct(request);
	}

	/**
	 * Updating case pack info.
	 *
	 * @param itemMaster the item master that contains the case pack information to update.
	 */
	public void updateCasePackInfo(com.heb.pm.entity.ItemMaster itemMaster) {
		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());

		ItemMaster webserviceItemMaster = new ItemMaster();
		webserviceItemMaster.setITMID(itemMaster.getKey().getItemCode().toString());
		webserviceItemMaster.setITMKEYTYPCD(itemMaster.getKey().getItemType());
		webserviceItemMaster.setITMTYPCD(itemMaster.getItemTypeCode());
		webserviceItemMaster.setONETOUCHTYPCD(itemMaster.getOneTouchTypeCode());
		webserviceItemMaster.setCASEUPC(itemMaster.getCaseUpc().toString());
		webserviceItemMaster.setITEMDES(itemMaster.getDescription().toUpperCase());
		if(itemMaster.getReActive() != null && itemMaster.getReActive()){
			webserviceItemMaster.setDSCONDT(SINGLE_SPACE);
			webserviceItemMaster.setDSCONRSNCD(SINGLE_SPACE);
			webserviceItemMaster.setDSCONUSRID(SINGLE_SPACE);
		}else {
			if (null != itemMaster.getDiscontinueDate()) {
				webserviceItemMaster.setDSCONDT(itemMaster.getDiscontinueDate().toString());
			}
			if (null != itemMaster.getDiscontinueReason()) {
				webserviceItemMaster.setDSCONRSNCD(itemMaster.getDiscontinueReason().getId());
			}
			if(null != itemMaster.getDiscontinueAction() && itemMaster.getDiscontinueAction()){
				webserviceItemMaster.setDSCONUSRID(userInfo.getUserId());
			}
		}
		webserviceItemMaster.setACTIONCD(UPDATE_ACTION);
		webserviceItemMaster.setLSTUPDTUSRID(userInfo.getUserId());
		webserviceItemMaster.setVCLSTUPDTUSRID(this.userInfo.getUserId());
		webserviceItemMaster.setLSTSYSUPDTID(PRODUCT_MAINTENANCE_SOURCE_SYSTEM);

		ProductMaster productMaster = new ProductMaster();
		productMaster.getItemMaster().add(webserviceItemMaster);
		productMaster.setWorkId(workId);
		productMaster.setACTIONCD(UPDATE_ACTION);

		request.getProductMaster().add(productMaster);
		request.setTrackingNbr(workId);
		request.setAuthentication(this.getAuthentication());
		this.updateProduct(request);
	}

	/**
	 * This receives a product master with only a list of updated descriptions. If the description is empty then that
	 * means the description was removed from the product which means to represent a delete for the webservice. Everything
	 * else means an update.
	 *
	 * @param updatedProductMaster A product master that contains updated descriptions.
	 */
	public void updateShelfAttributes(com.heb.pm.entity.ProductMaster updatedProductMaster) {

		boolean somethingUpdated = false;

		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());

		ProductMaster webserviceProductMaster = new ProductMaster();
		webserviceProductMaster.setWorkId(workId);
		webserviceProductMaster.setACTIONCD(UPDATE_ACTION);

		webserviceProductMaster.setPRODID(Long.toString(updatedProductMaster.getProdId()));

		if (updatedProductMaster.getGoodsProduct() != null && updatedProductMaster.getGoodsProduct().getTagType() != null) {
			GoodsProd goodsProd = new GoodsProd();
			goodsProd.setTAGTYPCD(updatedProductMaster.getGoodsProduct().getTagType());
			goodsProd.setVCLSTUPDTUID(userInfo.getUserId());
			goodsProd.setPRODID(Long.toString(updatedProductMaster.getProdId()));
			goodsProd.setACTIONCD(UPDATE_ACTION);
			webserviceProductMaster.setGoodsProd(goodsProd);
			somethingUpdated = true;
		}

		// English description.
		if (updatedProductMaster.getDescription() != null) {
			somethingUpdated = true;
			webserviceProductMaster.setPRODENGDES(updatedProductMaster.getDescription().toUpperCase());
		}

		// Spanish description.
		if (updatedProductMaster.getSpanishDescription() != null) {
			somethingUpdated = true;
			webserviceProductMaster.setPRODSPNSHDES(updatedProductMaster.getSpanishDescription().toUpperCase());
		}

		// This is for customer friendly descriptions.
		if (updatedProductMaster.getProductDescriptions() != null) {
			for (ProductDescription productDescription : updatedProductMaster.getProductDescriptions()) {
				// if the description key is null that means it wasn't updated so we want to continue onto the next one.
				if (productDescription.getDescription() == null) {
					continue;
				}
				somethingUpdated = true;
				ProdDescTxt prodDescTxt = new ProdDescTxt();
				prodDescTxt.setPRODID(Long.toString(productDescription.getKey().getProductId()));
				prodDescTxt.setVCLSTUPDTUID(userInfo.getUserId());
				prodDescTxt.setPRODDESTEXT(productDescription.getDescription().trim());
				if (productDescription.getKey().getLanguageType().trim().equals(ENGLISH_DESCRIPTION_TAG)) {
					prodDescTxt.setLANGTYPCD(ENGLISH_DESCRIPTION_TAG);
				}
				if (productDescription.getKey().getLanguageType().trim().equals(SPANISH_DESCRIPTION_TAG)) {
					prodDescTxt.setLANGTYPCD(SPANISH_DESCRIPTION_TAG);
				}
				if (productDescription.getDescription().trim().isEmpty()) {
					prodDescTxt.setACTIONCD(DELETE_ACTION);
				} else {
					prodDescTxt.setACTIONCD(UPDATE_ACTION);
				}
				prodDescTxt.setDESTYPCD(productDescription.getKey().getDescriptionType().trim());

				prodDescTxt.setLSTUPDTUID(userInfo.getUserId());
				prodDescTxt.setVCLSTUPDTUID(this.userInfo.getUserId());
				webserviceProductMaster.getProdDescTxt().add(prodDescTxt);
			}
		}

		webserviceProductMaster.setLSTUPDTUID(userInfo.getUserId());
		webserviceProductMaster.setVCLSTUPDTUSRID(this.userInfo.getUserId());
		request.getProductMaster().add(webserviceProductMaster);
		if (somethingUpdated) {
			this.updateProduct(request);
		}
	}

	/**
	 * handle update VendorItemFactory
	 *
	 * @param removedFactories List of removed factory.
	 * @param addedFactories   List of added  factory.
	 */
	public void updateVendorItemFactory(List<VendorItemFactory> removedFactories, List<VendorItemFactory>
			addedFactories) {
		//create request
		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());
		//create productMaster request
		ProductMaster productMaster = new ProductMaster();
		productMaster.setWorkId(workId);
		//create item
		ItemMaster itemMaster = new ItemMaster();
		//create vendorItmLoc
		VendLocItm vendLocItm = new VendLocItm();
		VendLocImprtItm vendLocImprtItm = new VendLocImprtItm();
		//create factory

		List<VendItmFctryXref> lstVendItmFctryXref = new ArrayList<>();
		VendItmFctryXref vendItmFctryXref = null;
		for (VendorItemFactory vendorItemFactory : removedFactories) {
			vendItmFctryXref = new VendItmFctryXref();
			vendItmFctryXref.setACTIONCD(DELETE_ACTION);
			vendItmFctryXref.setITMID(String.valueOf(vendorItemFactory.getKey().getItemCode()));
			vendItmFctryXref.setITMKEYTYPCD(StringUtils.trim(vendorItemFactory.getKey().getItemType()));
			vendItmFctryXref.setVENDLOCNBR(String.valueOf(vendorItemFactory.getKey().getVendorNumber()));
			vendItmFctryXref.setVENDLOCTYPCD(StringUtils.trim(vendorItemFactory.getKey().getVendorType()));
			vendItmFctryXref.setFCTRYID(String.valueOf(vendorItemFactory.getKey().getFactoryId()));
			vendItmFctryXref.setLSTUID(userInfo.getUserId());
			lstVendItmFctryXref.add(vendItmFctryXref);
		}

		for (VendorItemFactory vendorItemFactory : addedFactories) {
			vendItmFctryXref = new VendItmFctryXref();
			vendItmFctryXref.setITMID(String.valueOf(vendorItemFactory.getKey().getItemCode()));
			vendItmFctryXref.setITMKEYTYPCD(StringUtils.trim(vendorItemFactory.getKey().getItemType()));
			vendItmFctryXref.setVENDLOCNBR(String.valueOf(vendorItemFactory.getKey().getVendorNumber()));
			vendItmFctryXref.setVENDLOCTYPCD(StringUtils.trim(vendorItemFactory.getKey().getVendorType()));
			vendItmFctryXref.setFCTRYID(String.valueOf(vendorItemFactory.getKey().getFactoryId()));
			vendItmFctryXref.setLSTUID(userInfo.getUserId());
			lstVendItmFctryXref.add(vendItmFctryXref);
		}
		vendLocImprtItm.getVendItmFctryXref().addAll(lstVendItmFctryXref);
		vendLocItm.getVendLocImprtItm().add(vendLocImprtItm);
		itemMaster.getVendLocItm().add(vendLocItm);
		productMaster.getItemMaster().add(itemMaster);
		request.getProductMaster().add(productMaster);
		//send to ws
		this.updateProduct(request);
	}

	/**
	 * Calls ProductManagementService to update Warehouse Location Item  information (WhseLocItm).
	 *
	 * @param warehouseLocationItems The list of warehouse loction item.
	 */
	public void updateWarehouseLocationItem(List<WarehouseLocationItem> warehouseLocationItems) {
		List<WhseLocItm> whseLocItms = new ArrayList<>();
		String userId = userInfo.getUserId();
		for (WarehouseLocationItem warehouseLocationItem : warehouseLocationItems) {
			WhseLocItm whseLocItm = new WhseLocItm();
			whseLocItm.setITMID(String.valueOf(warehouseLocationItem.getKey().getItemCode()));
			whseLocItm.setWHSELOCNBR(String.valueOf(warehouseLocationItem.getKey().getWarehouseNumber()));
			whseLocItm.setWHSELOCTYPCD(StringUtils.trim(warehouseLocationItem.getKey().getWarehouseType()));
			whseLocItm.setITMKEYTYPCD(StringUtils.trim(warehouseLocationItem.getKey().getItemType()));
			whseLocItm.setVCUSRID(userId);
			//PM-489 Case Pack > Warehouse > Edit - Weight
			whseLocItm.setVARWTSW(warehouseLocationItem.isVariableWeight() ? "Y" : "N");
			//PM-450 Case Pack > Warehouse > Edit - Ordering Info
			whseLocItm.setORDQTYTYPCD(StringUtils.trim(warehouseLocationItem.getOrderQuantityTypeCode()));
			//PM-1181 Case Pack > Warehouse > Edit - Catch Weight
			whseLocItm.setCTCHWTSW(warehouseLocationItem.isCatchWeight() ? "Y" : "N");

			if (warehouseLocationItem.getWhseMaxShipQuantityNumber() == null || warehouseLocationItem.getWhseMaxShipQuantityNumber() == 0) {
				whseLocItm.setMAXSHPWHSEQTY(SINGLE_SPACE);
			} else {
				whseLocItm.setMAXSHPWHSEQTY(String.valueOf(warehouseLocationItem.getWhseMaxShipQuantityNumber()));
			}
			if (warehouseLocationItem.getUnitFactor1() == null || BigDecimal.valueOf(warehouseLocationItem.getUnitFactor1()).equals(BigDecimal.ZERO)) {
				whseLocItm.setCSUNTFACTR1(SINGLE_SPACE);
			} else {
				whseLocItm.setCSUNTFACTR1(String.valueOf(BigDecimal.valueOf(warehouseLocationItem.getUnitFactor1())));
			}
			if (warehouseLocationItem.getUnitFactor2() == null || BigDecimal.valueOf(warehouseLocationItem.getUnitFactor2()).equals(BigDecimal.ZERO)) {
				whseLocItm.setCSUNTFACTR2(SINGLE_SPACE);
			} else {
				whseLocItm.setCSUNTFACTR2(String.valueOf(BigDecimal.valueOf(warehouseLocationItem.getUnitFactor2())));
			}
			//PM-490 Case Pack > Warehouse > Edit - Tie, Tier, Flow Type
			if (warehouseLocationItem.getWhseTie() == null || warehouseLocationItem.getWhseTie() == 0) {
				whseLocItm.setPALTI(SINGLE_SPACE);
			} else {
				whseLocItm.setPALTI(String.valueOf(warehouseLocationItem.getWhseTie()));
			}
			if (warehouseLocationItem.getWhseTier() == null || warehouseLocationItem.getWhseTier() == 0) {
				whseLocItm.setPALTIER(SINGLE_SPACE);
			} else {
				whseLocItm.setPALTIER(String.valueOf(warehouseLocationItem.getWhseTier()));
			}
			if (warehouseLocationItem.getFlowTypeCode() == null) {
				whseLocItm.setFLWTYPCD(SINGLE_SPACE);
			} else {
				whseLocItm.setFLWTYPCD(warehouseLocationItem.getFlowTypeCode());
			}

			whseLocItms.add(whseLocItm);
		}

		ItemMaster itemMaster = new ItemMaster();
		itemMaster.getWhseLocItm().addAll(whseLocItms);
		String workId = this.getWorkId().toString();
		ProductMaster productMaster = new ProductMaster();
		productMaster.getItemMaster().add(itemMaster);
		productMaster.setWorkId(workId);
		UpdateProductRequest request = new UpdateProductRequest();
		request.getProductMaster().add(productMaster);
		request.setTrackingNbr(workId);
		request.setAuthentication(this.getAuthentication());
		this.updateProduct(request);
	}

	/**
	 * Updating for Remark and Comment of Warehouse.
	 *
	 * @param warehouseLocationItem Warehouse location item.
	 */
	public void saveRemarkAndCommentForWarehouse(WarehouseLocationItem warehouseLocationItem) {
		if (warehouseLocationItem != null) {
			UpdateProductRequest request = new UpdateProductRequest();
			String workId = this.getWorkId().toString();
			request.setTrackingNbr(workId);
			request.setAuthentication(this.getAuthentication());
			ProductMaster productMaster = new ProductMaster();
			productMaster.setWorkId(workId);
			ItemMaster itemMaster = new ItemMaster();
			WhseLocItm whseLocItm = null;
			if (warehouseLocationItem.getComment() != null) {
				whseLocItm = ProductManagementServiceHelper.createWhseLocItmRequestForRemark(
						warehouseLocationItem.getKey().getItemCode(),
						warehouseLocationItem.getKey().getItemType(),
						warehouseLocationItem.getKey().getWarehouseNumber(),
						warehouseLocationItem.getKey().getWarehouseType(),
						warehouseLocationItem.getComment(), userInfo.getUserId());
			}
			if (warehouseLocationItem.getItemWarehouseCommentsList() != null &&
					warehouseLocationItem.getItemWarehouseCommentsList().size() > 0) {
				if (whseLocItm == null)
					whseLocItm = new WhseLocItm();
				for (ItemWarehouseComments itemWarehouseComments : warehouseLocationItem.getItemWarehouseCommentsList()) {
					whseLocItm.getItemWarehouseComments().add(
							ProductManagementServiceHelper.createItemWarehouseCommentsForRemark(
									itemWarehouseComments.getKey().getItemId(),
									itemWarehouseComments.getKey().getItemType(),
									itemWarehouseComments.getKey().getWarehouseNumber(),
									itemWarehouseComments.getKey().getWarehouseType(),
									itemWarehouseComments));
				}
			}
			if (whseLocItm != null)
				itemMaster.getWhseLocItm().add(whseLocItm);
			productMaster.getItemMaster().add(itemMaster);
			request.getProductMaster().add(productMaster);
			this.updateProduct(request);
		}
	}

	/**
	 * Handle update Department
	 *
	 * @param pssDepartments List of department.
	 */
	public void updatePssDepartment(List<PssDepartment> pssDepartments) {
		//create request
		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());
		//create productMaster request
		ProductMaster productMaster = new ProductMaster();
		productMaster.setWorkId(workId);
		//create item
		ItemMaster itemMaster = new ItemMaster();
		for (PssDepartment pssDepartment : pssDepartments) {
			itemMaster.setITMID(StringUtils.trim(pssDepartment.getItmId()));
			itemMaster.setITMKEYTYPCD(StringUtils.trim(pssDepartment.getItmTypCd()));
			switch (pssDepartment.getIndex()) {
				case 0:
					itemMaster.setDEPTID1(pssDepartment.getSubDepartment().getKey().getDepartment());
					itemMaster.setSUBDEPTID1(pssDepartment.getSubDepartment().getKey().getSubDepartment());
					itemMaster.setDEPTMDSETYP1(pssDepartment.getMerchandiseType().getId());
					itemMaster.setPSSDEPT1(pssDepartment.getPssDepartmentCode().getKey().getId().toString());
					break;
				case 1:
					itemMaster.setDEPTID2(pssDepartment.getSubDepartment().getKey().getDepartment());
					itemMaster.setSUBDEPTID2(pssDepartment.getSubDepartment().getKey().getSubDepartment());
					itemMaster.setDEPTMDSETYP2(pssDepartment.getMerchandiseType().getId());
					itemMaster.setPSSDEPT2(pssDepartment.getPssDepartmentCode().getKey().getId().toString());
					break;
				case 2:
					itemMaster.setDEPTID3(pssDepartment.getSubDepartment().getKey().getDepartment());
					itemMaster.setSUBDEPTID3(pssDepartment.getSubDepartment().getKey().getSubDepartment());
					itemMaster.setDEPTMDSETYP3(pssDepartment.getMerchandiseType().getId());
					itemMaster.setPSSDEPT3(pssDepartment.getPssDepartmentCode().getKey().getId().toString());
					break;
				case 3:
					itemMaster.setDEPTID4(pssDepartment.getSubDepartment().getKey().getDepartment());
					itemMaster.setSUBDEPTID4(pssDepartment.getSubDepartment().getKey().getSubDepartment());
					itemMaster.setDEPTMDSETYP4(pssDepartment.getMerchandiseType().getId());
					itemMaster.setPSSDEPT4(pssDepartment.getPssDepartmentCode().getKey().getId().toString());
					break;
			}
		}
		itemMaster.setLSTSYSUPDTID(PRODUCT_MAINTENANCE_SOURCE_SYSTEM);
		itemMaster.setVCLSTUPDTUSRID(userInfo.getUserId());
		//set item master to product master
		productMaster.getItemMaster().add(itemMaster);
		request.getProductMaster().add(productMaster);
		//send to ws
		this.updateProduct(request);
	}

	/**
	 * Calls ProductManagementService to update RXProduct  information (RX_PROD).
	 * If the value is null coming in then that means it wasn't updated on the front end.
	 *
	 * @param productMaster
	 */
	public void updatePharmacy(com.heb.pm.entity.ProductMaster productMaster) {
		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());

		// Web service productMaster.
		ProductMaster webserviceProductMaster = new ProductMaster();
		webserviceProductMaster.setWorkId(workId);
		webserviceProductMaster.setACTIONCD(UPDATE_ACTION);
		webserviceProductMaster.setPRODID(Long.toString(productMaster.getProdId()));
		webserviceProductMaster.setLSTUPDTUID(this.userInfo.getUserId());
		webserviceProductMaster.setVCLSTUPDTUSRID(this.userInfo.getUserId());

		// Updates the webservice goods product based on what was updated.
		if (productMaster.getGoodsProduct() != null) {

			// Webservice goods prod
			GoodsProd webserviceGoodsProd = new GoodsProd();
			webserviceProductMaster.setGoodsProd(webserviceGoodsProd);
			webserviceProductMaster.getGoodsProd().setACTIONCD(UPDATE_ACTION);
			webserviceProductMaster.getGoodsProd().setPRODID(Long.toString(productMaster.getProdId()));
			if (productMaster.getGoodsProduct().getMedicaidCode() != null) {
				webserviceProductMaster.getGoodsProd().setMEDICAIDCD(productMaster.getGoodsProduct().getMedicaidCode());
			}
			if (productMaster.getGoodsProduct().getRxProductFlag() != null) {
				if(productMaster.getGoodsProduct().getRxProductFlag()) {
					webserviceProductMaster.getGoodsProd().setRXPRODSW("Y");
				} else if(!productMaster.getGoodsProduct().getRxProductFlag()) {
					webserviceProductMaster.getGoodsProd().setRXPRODSW("N");
				}
			}
			if (productMaster.getGoodsProduct().getPseTypeCode() != null) {
				webserviceProductMaster.getGoodsProd().setPSETYPECD(productMaster.getGoodsProduct().getPseTypeCode());
			}
			if (productMaster.getGoodsProduct().getRxProduct() != null) {
				RxProd webserviceRxProd = new RxProd();
				webserviceRxProd.setPRODID(Long.toString(productMaster.getProdId()));
				webserviceProductMaster.getGoodsProd().setRxProd(webserviceRxProd);
				if (productMaster.getGoodsProduct().getRxProduct().getNdc() != null) {
					if(StringUtils.isEmpty(productMaster.getGoodsProduct().getRxProduct().getNdc())) {
						webserviceProductMaster.getGoodsProd().getRxProd().setNDCID(SINGLE_SPACE);
					}else{
						webserviceProductMaster.getGoodsProd().getRxProd().setNDCID(productMaster.getGoodsProduct().getRxProduct().getNdc());
					}
				}
				if (productMaster.getGoodsProduct().getRxProduct().getDrugScheduleTypeCode() != null) {
					webserviceProductMaster.getGoodsProd().getRxProd().setDRUGSCHTYPCD(
							productMaster.getGoodsProduct().getRxProduct().getDrugScheduleTypeCode());
				}

				webserviceProductMaster.getGoodsProd().getRxProd().setACTIONCD(UPDATE_ACTION);
				webserviceProductMaster.getGoodsProd().getRxProd().setPRODID(Long.toString(productMaster.getProdId()));
				webserviceProductMaster.getGoodsProd().getRxProd().setLSTUPDTUID(this.userInfo.getUserId());
			}
			if(productMaster.getGoodsProduct().getRxProductFlag() != null && productMaster.getGoodsProduct()
					.getRxProductFlag() == false) {
				if(webserviceProductMaster.getGoodsProd().getRxProd() == null){
					RxProd webserviceRxProd = new RxProd();
					webserviceRxProd.setPRODID(Long.toString(productMaster.getProdId()));
					webserviceProductMaster.getGoodsProd().setRxProd(webserviceRxProd);
				}
				webserviceProductMaster.getGoodsProd().getRxProd().setACTIONCD(DELETE_ACTION);
			}

			webserviceProductMaster.getGoodsProd().setLSTUPDTUID(this.userInfo.getUserId());
			webserviceProductMaster.getGoodsProd().setVCLSTUPDTUID(this.userInfo.getUserId());
		}
		if (productMaster.getSellingUnits() != null) {
			for (SellingUnit sellingUnit : productMaster.getSellingUnits()) {
				if (sellingUnit != null) {
					ProdScnCodes prodScnCodes = new ProdScnCodes();
					prodScnCodes.setACTIONCD(UPDATE_ACTION);
					prodScnCodes.setPRODID(Long.toString(productMaster.getProdId()));
					prodScnCodes.setSCNCDID(Long.toString(sellingUnit.getUpc()));
					prodScnCodes.setLSTUPDTUID(this.userInfo.getUserId());
					prodScnCodes.setVCLSTUPDTUSRID(this.userInfo.getUserId());
					prodScnCodes.setPSEGRAMSWT(sellingUnit.getPseGramWeight().toString());
					webserviceProductMaster.getProdScnCodes().add(prodScnCodes);
				}
			}
		}
		request.getProductMaster().add(webserviceProductMaster);
		this.updateProduct(request);
	}

	/**
	 * Updates the code date.
	 *
	 * @param productMaster
	 */
	public void updateCodeDate(com.heb.pm.entity.ProductMaster productMaster) {
		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());

		// Web service productMaster.
		ProductMaster webserviceProductMaster = new ProductMaster();

		// Updates the webservice goods product based on what was updated.
		if (productMaster.getGoodsProduct() != null) {

			// Webservice goods prod
			GoodsProd webserviceGoodsProd = new GoodsProd();
			webserviceGoodsProd.setACTIONCD(UPDATE_ACTION);
			webserviceGoodsProd.setPRODID(Long.toString(productMaster.getProdId()));
			if (productMaster.getGoodsProduct().getCodeDate() != null) {
				if (productMaster.getGoodsProduct().getCodeDate()) {
					webserviceGoodsProd.setCDDATEDITMCD("Y");
				} else if (!productMaster.getGoodsProduct().getCodeDate()) {
					webserviceGoodsProd.setCDDATEDITMCD("N");
				}
			}
			if (productMaster.getGoodsProduct().getMaxShelfLifeDays() != null) {
				webserviceGoodsProd.setMAXSHLFLIFEDD(productMaster.getGoodsProduct().getMaxShelfLifeDays().toString());
			}
			if (productMaster.getGoodsProduct().getInboundSpecificationDays() != null) {
				webserviceGoodsProd.setINBNDSPCFNDD(productMaster.getGoodsProduct().getInboundSpecificationDays().toString());
			}
			if (productMaster.getGoodsProduct().getWarehouseReactionDays() != null) {
				webserviceGoodsProd.setREACTDD(productMaster.getGoodsProduct().getWarehouseReactionDays().toString());
			}
			if (productMaster.getGoodsProduct().getGuaranteeToStoreDays() != null) {
				webserviceGoodsProd.setGUARNTOSTRDD(productMaster.getGoodsProduct().getGuaranteeToStoreDays().toString());
			}

			if (productMaster.getGoodsProduct().getSendCodeDate() != null) {
				if (productMaster.getGoodsProduct().getSendCodeDate()) {
					webserviceGoodsProd.setSNDDDTOWMSSW("Y");
				} else if (!productMaster.getGoodsProduct().getSendCodeDate()) {
					webserviceGoodsProd.setSNDDDTOWMSSW("N");
				}
			}
			webserviceGoodsProd.setLSTUPDTUID(this.userInfo.getUserId());
			webserviceGoodsProd.setVCLSTUPDTUID(this.userInfo.getUserId());
			webserviceProductMaster.setGoodsProd(webserviceGoodsProd);
		}
		request.getProductMaster().add(webserviceProductMaster);
		this.updateProduct(request);
	}

	/**
	 * Calls ProductManagementService to change item primary upc.
	 * source : contain information of current item primary upc
	 * destination: contain information of new item primary upc.
	 *
	 * @param upcSwap The upc swap that contain all information current item primary upc and new item primary upc.
	 */
	public void changeItemPrimaryUPC(UpcSwap upcSwap) throws CheckedSoapException {
		SwapScanCodesRequest request = new SwapScanCodesRequest();
		com.heb.xmlns.ei.upcswap.UpcSwap swap = new com.heb.xmlns.ei.upcswap.UpcSwap();
		com.heb.xmlns.ei.upcswap.UpcSwap.SOURCE source = new com.heb.xmlns.ei.upcswap.UpcSwap.SOURCE();
		com.heb.xmlns.ei.upcswap.UpcSwap.DESTINATION destination = new com.heb.xmlns.ei.upcswap.UpcSwap.DESTINATION();

		source.setPRODID(BigInteger.valueOf(upcSwap.getSource().getProductId()));
		source.setITEMTYPE(ItemMasterKey.WAREHOUSE);
		source.setITEMID(String.valueOf(upcSwap.getSource().getItemCode()));
		source.setPRIMARYUPC(String.valueOf(upcSwap.getSource().getPrimaryUpc()));
		source.setASSOCIATEDUPC(String.valueOf(upcSwap.getDestination().getPrimaryUpc()));

		destination.setPRODID(BigInteger.valueOf(upcSwap.getDestination().getProductId()));
		destination.setITEMTYPE(ItemMasterKey.WAREHOUSE);
		destination.setITEMID(String.valueOf(upcSwap.getDestination().getItemCode()));
		destination.setPRIMARYUPC(String.valueOf(upcSwap.getDestination().getPrimaryUpc()));
		destination.setASSOCIATEDUPC(String.valueOf(upcSwap.getSource().getPrimaryUpc()));

		swap.setSOURCE(source);
		swap.setDESTINATION(destination);

		com.heb.xmlns.ei.upcswapaud.UpcSwapAud upcSwapAud = new com.heb.xmlns.ei.upcswapaud.UpcSwapAud();
		upcSwapAud.setSRCSCNCDID(String.valueOf(upcSwap.getSource().getPrimaryUpc()));
		upcSwapAud.setFUTRPRIMSCNCDID(String.valueOf(upcSwap.getDestination().getPrimaryUpc()));
		upcSwapAud.setMAKEPRIMSCNCDSW("Y");

		swap.setUpcSwapAud(upcSwapAud);
		swap.setUSERID(this.userInfo.getUserId());
		swap.setOPERATION(SET_PRIMARY_UPC);
		request.setAuthentication(this.getAuthentication());
		request.getUpcSwap().add(swap);

		this.swapScanCodes(request);
	}

	/**
	 * Update subscription date.
	 *
	 * @param subDate    the sub date
	 * @param productId  the product id
	 */
	public void updateSubscriptionDate(ECommerceViewAttributePriorities subDate, long productId) {
		if(subDate != null) {
			com.heb.pm.entity.ProductMaster productMaster = productInfoRepository.findOne(productId);
			if(productMaster != null && this.compareSubscriptionDate(subDate, productMaster) != 0) {
				UpdateProductRequest updateProductRequest = new UpdateProductRequest();
				updateProductRequest.setTrackingNbr(String.valueOf(0));
				ProductMaster proMaster = new ProductMaster();
				proMaster.setPRODID(String.valueOf(productId));
				proMaster.setACTIONCD(UPDATE_ACTION);
				proMaster.setSUBSCRPRODSW(convertTrueFalseToYNString(subDate.isSubscriptionProductSw()));
				proMaster.setSUBSCRSTRTDT(subDate.getStartDate());
				proMaster.setSUBSCRENDDT(subDate.getEndDate());
				updateProductRequest.getProductMaster().add(proMaster);
				updateProductRequest.setAuthentication(this.getAuthentication());
				this.updateProduct(updateProductRequest);
			}
		}
	}
	/**
	 * Compare sub date.
	 *
	 * @param newSubscriptionDate  the ECommerceViewAttributePriorities object holds new Subscription date.
	 * @param oldSubscriptionDate the ProductMaster holds the old Subscription date.
	 * @return the int
	 */
	private int compareSubscriptionDate(ECommerceViewAttributePriorities newSubscriptionDate, com.heb.pm.entity.ProductMaster  oldSubscriptionDate) {
		int ret = -1;
		if (newSubscriptionDate.getStartDate() != null && newSubscriptionDate.getEndDate() != null && oldSubscriptionDate.getSubscriptionStartDate() != null && oldSubscriptionDate.getSubscriptionEndDate() != null) {
			ret = new CompareToBuilder().append(newSubscriptionDate.isSubscriptionProductSw(), oldSubscriptionDate.isSubscription()).
					append(newSubscriptionDate.getStartDate(),ProductECommerceViewUtil.convertDateToStringDateYYYYMMDD(oldSubscriptionDate.getSubscriptionStartDate()))
					.append(newSubscriptionDate.getEndDate(), ProductECommerceViewUtil.convertDateToStringDateYYYYMMDD(oldSubscriptionDate.getSubscriptionEndDate())).toComparison();
		}
		return ret;
	}

	/**
	 * Update fulfillment program.
	 *
	 * @param productFullfilmentChanels the list of ecommerce fulfillments
	 * @param productId                 the prod id
	 * @param userId                    the user id
	 */
	public void updateFulfillmentProgram(List<ProductFullfilmentChanel> productFullfilmentChanels, long productId, String userId) {
		if (productFullfilmentChanels != null && !productFullfilmentChanels.isEmpty()) {
			ProdChnlUpdate prodChnl;
			List<ProdChnlUpdate> prodChnls = new ArrayList<>();
			boolean isExpiredDay = false;
			List<ProductFullfilmentChanel> listProductFullfilmentChanel = productFullfilmentChanelRepository.findByKeyProductId(productId);
			for (ProductFullfilmentChanel productFullfilmentChanel : productFullfilmentChanels) {
				isExpiredDay = this.checkFulfillmentProgramExpiredDay(listProductFullfilmentChanel,
						productFullfilmentChanel.getKey().getSalesChanelCode(),
						productFullfilmentChanel.getKey().getFullfillmentChanelCode());
				prodChnl = new ProdChnlUpdate();
				prodChnl.setPRODID(String.valueOf(productId));
				prodChnl.setSALSCHNLCD(productFullfilmentChanel.getKey().getSalesChanelCode());
				prodChnl.setFLFLCHNLCD(productFullfilmentChanel.getKey().getFullfillmentChanelCode());
				prodChnl.setEFFDT(ProductECommerceViewUtil.convertDateToStringDateYYYYMMDD(productFullfilmentChanel.getEffectDate()));
				prodChnl.setEXPRNDT(ProductECommerceViewUtil.convertDateToStringDateYYYYMMDD(productFullfilmentChanel.getExpirationDate()));
				if(productFullfilmentChanel.getActionCode().equalsIgnoreCase(ADD_ACTION)){
					if(isExpiredDay){
						// Update case.
						prodChnl.setACTIONCD(UPDATE_ACTION);
					}else{
						// Add case.
						prodChnl.setACTIONCD(ADD_ACTION);
					}
				}else if(productFullfilmentChanel.getActionCode().equalsIgnoreCase(DELETE_ACTION)){
					// Delete case.
					prodChnl.setACTIONCD(DELETE_ACTION);
				}else{
					// Update case.
					prodChnl.setACTIONCD(UPDATE_ACTION);
				}
				prodChnl.setLSTUPDTUID(userId);
				prodChnls.add(prodChnl);
			}
			GoodsProd goodsProd = new GoodsProd();
			goodsProd.getProdChnl().addAll(prodChnls);
			com.heb.xmlns.ei.productmaster.ProductMaster productMaster = new com.heb.xmlns.ei.productmaster.ProductMaster();
			productMaster.setGoodsProd(goodsProd);
			UpdateProductRequest updateProductRequest = new UpdateProductRequest();
			updateProductRequest.setTrackingNbr(String.valueOf(0));
			updateProductRequest.getProductMaster().add(productMaster);
			updateProductRequest.setAuthentication(this.getAuthentication());
			this.updateProduct(updateProductRequest);
		}
	}

	/**
	 * Check fulfillment program is expired day.
	 *
	 * @param productFullfilmentChanels The list of ProductFullfilmentChanels.
	 * @param salesChanelCode The sales chanel code.
	 * @param fullfillmentChanelCode The fullfillment chanel code.
	 * @return true, if it is not expired day, or expired day.
	 */
	private boolean checkFulfillmentProgramExpiredDay(List<ProductFullfilmentChanel> productFullfilmentChanels,
													  String salesChanelCode, String fullfillmentChanelCode) {
		boolean isExpiredDay = false;
		if (productFullfilmentChanels != null) {
			for (ProductFullfilmentChanel productFullfilmentChanel : productFullfilmentChanels) {
				if (StringUtils.trimToEmpty(salesChanelCode).equals(StringUtils.trimToEmpty(productFullfilmentChanel.getKey().getSalesChanelCode())) &&
						StringUtils.trimToEmpty(fullfillmentChanelCode).equals(StringUtils.trimToEmpty(productFullfilmentChanel.getKey().getFullfillmentChanelCode()))) {
					Date currentDateString = ProductECommerceViewUtil.getCurrentDate();
					Date endDateString = ProductECommerceViewUtil.convertLocalDateToDate(productFullfilmentChanel.getExpirationDate());
					if (endDateString != null && endDateString.compareTo(currentDateString) < 0) {
						isExpiredDay = true;
						break;
					}
				}
			}
		}
		return isExpiredDay;
	}

	public void updateShippingHandlingChanges(com.heb.pm.entity.ProductMaster productMaster, String userId) {
		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());

		// Web service productMaster.
		ProductMaster webserviceProductMaster = new ProductMaster();
		webserviceProductMaster.setWorkId(workId);
		webserviceProductMaster.setACTIONCD(UPDATE_ACTION);
		webserviceProductMaster.setPRODID(Long.toString(productMaster.getProdId()));
		webserviceProductMaster.setLSTUPDTUID(userId);
		webserviceProductMaster.setVCLSTUPDTUSRID(userId);

		// Updates the webservice goods product based on what was updated.
		if(productMaster.getGoodsProduct() != null) {

			// Webservice goods prod
			GoodsProd webserviceGoodsProd = new GoodsProd();
			webserviceProductMaster.setGoodsProd(webserviceGoodsProd);
			webserviceProductMaster.getGoodsProd().setACTIONCD(UPDATE_ACTION);
			webserviceProductMaster.getGoodsProd().setPRODID(Long.toString(productMaster.getProdId()));
			if(productMaster.getGoodsProduct().getFragile() != null) {
				webserviceProductMaster.getGoodsProd().setFRAGILESW(convertTrueFalseToYNString(productMaster.getGoodsProduct().getFragile()));
			}
			if(productMaster.getGoodsProduct().getOrmd() != null) {
				webserviceProductMaster.getGoodsProd().setORMDSW(convertTrueFalseToYNString(productMaster.getGoodsProduct().getOrmd()));
			}
			if(productMaster.getGoodsProduct().getShipByItself() != null) {
				webserviceProductMaster.getGoodsProd().setSHPALONESW(convertTrueFalseToYNString(productMaster.getGoodsProduct().getShipByItself()));
			}
			webserviceProductMaster.getGoodsProd().setLSTUPDTUID(userId);
			webserviceProductMaster.getGoodsProd().setVCLSTUPDTUID(userId);
		}
		request.getProductMaster().add(webserviceProductMaster);
		this.updateProduct(request);
	}

	/**
	 * Calls ProductManagementService to update Tobacco Product  information (TBCO_PROD).
	 * If the value is null coming in then that means it wasn't updated on the front end.
	 *
	 * @param productMaster
	 */
	public void updateTobacco(com.heb.pm.entity.ProductMaster productMaster) {
		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());

		// Web service productMaster.
		ProductMaster webserviceProductMaster = new ProductMaster();
		webserviceProductMaster.setWorkId(workId);
		webserviceProductMaster.setACTIONCD(UPDATE_ACTION);
		webserviceProductMaster.setPRODID(Long.toString(productMaster.getProdId()));
		webserviceProductMaster.setLSTUPDTUID(this.userInfo.getUserId());
		webserviceProductMaster.setVCLSTUPDTUSRID(this.userInfo.getUserId());

		// Updates the webservice goods product based on what was updated.
		if(productMaster.getGoodsProduct() != null) {

			// Webservice goods prod
			GoodsProd webserviceGoodsProd = new GoodsProd();
			webserviceProductMaster.setGoodsProd(webserviceGoodsProd);
			webserviceProductMaster.getGoodsProd().setACTIONCD(UPDATE_ACTION);
			webserviceProductMaster.getGoodsProd().setPRODID(Long.toString(productMaster.getProdId()));
			webserviceProductMaster.getGoodsProd().setTBCOPRODSW(convertTrueFalseToYNString(productMaster.getGoodsProduct().getTobaccoProductSwitch()));
			if(productMaster.getGoodsProduct().getTobaccoProduct() != null) {
				TbcoProd webserviceTobaccoProduct = new TbcoProd();
				webserviceTobaccoProduct.setPRODID(Long.toString(productMaster.getProdId()));
				webserviceProductMaster.getGoodsProd().setTbcoProd(webserviceTobaccoProduct);
				if (productMaster.getGoodsProduct().getTobaccoProduct().getTobaccoProductTypeCode() != null) {
					webserviceProductMaster.getGoodsProd().getTbcoProd().setTBCOPRODTYPCD(
							productMaster.getGoodsProduct().getTobaccoProduct().getTobaccoProductTypeCode());
				}

				webserviceProductMaster.getGoodsProd().getTbcoProd().setACTIONCD(UPDATE_ACTION);
				webserviceProductMaster.getGoodsProd().getTbcoProd().setPRODID(Long.toString(productMaster.getProdId()));
				webserviceProductMaster.getGoodsProd().getTbcoProd().setLSTUPDTUID(this.userInfo.getUserId());

			}
			webserviceProductMaster.getGoodsProd().setVCLSTUPDTUID(this.userInfo.getUserId());
		}
		request.getProductMaster().add(webserviceProductMaster);
		this.updateProduct(request);
	}
	/**
	 * Helper method to convert the boolean to a string
	 *
	 * @param value the current Boolean for the object
	 * @return what the table expects
	 */
	private String convertTrueFalseToYNString(Boolean value) {
		if (value != null) {
			if (value) {
				return (DEFAULT_TRUE_STRING);
			} else {
				return (DEFAULT_FALSE_STRING);
			}
		} else {
			return (null);
		}
	}


	/**
	 * Calls ProductManagementService to update product information.
	 *
	 * @param updatableFields The fields that changed.
	 * @param productId The product id of the product that changed.
	 */
	public void updateProductInformation(List<UpdatableField> updatableFields, Long productId) {
		// if there is nothing to update, just return
		if(updatableFields == null || updatableFields.isEmpty()){
			return;
		}
		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());
		request.getProductMaster().add(this.convertUpdatableFieldsToProductMaster(updatableFields, String.valueOf(productId)));
		this.updateProduct(request);
	}

	/**
	 * Converts a list of updatable fields to a webservice product master, then returns that webservice product master.
	 *
	 * @param updatableFields List of fields the user has access to modify, and the value changed.
	 * @param productIdString String version of the product id
	 * @return Webservice product master created from the list of updatable fields.
	 */
	private ProductMaster convertUpdatableFieldsToProductMaster(List<UpdatableField> updatableFields, String productIdString) {

		// Web service productMaster.
		ProductMaster webserviceProductMaster = new ProductMaster();
		webserviceProductMaster.setPRODID(productIdString);
		webserviceProductMaster.setVCLSTUPDTUSRID(this.userInfo.getUserId());
		webserviceProductMaster.setACTIONCD(UPDATE_ACTION);

		for(UpdatableField updatableField : updatableFields){
			switch (updatableField.getField()){
				case ResourceConstants.PRODUCT_DESCRIPTION: {
					webserviceProductMaster.setPRODENGDES(
							updatableField.getValue().toString().toUpperCase());
					webserviceProductMaster.getProdDescTxt().add(
							convertProductDescriptionAndProductIdToProdDescTxt(
									updatableField.getValue().toString(), productIdString, DescriptionType.Codes.STANDARD));
					break;
				}
				case ResourceConstants.PRODUCT_QUANTITY_REQUIRED: {
					webserviceProductMaster.setRCIMQTYREQFLAG(updatableField.getValue().toString());
					break;
				}
				case ResourceConstants.PRODUCT_STYLE_DESCRIPTION: {
					webserviceProductMaster.getProdDescTxt().add(
							convertProductDescriptionAndProductIdToProdDescTxt(
									updatableField.getValue().toString(), productIdString, DescriptionType.Codes.STYLE));
					break;
				}
				case ResourceConstants.PRODUCT_SHOW_CALORIES: {
					webserviceProductMaster.setSHOWCLRSSW(
							convertBooleanToString(Boolean.valueOf(updatableField.getValue().toString())));
					break;
				}
				case ResourceConstants.PRODUCT_SUB_COMMODITY: {
					webserviceProductMaster.setPDOMISUBCOMCD(updatableField.getValue().toString());
					break;
				}
				case ResourceConstants.PRODUCT_PRICE_REQUIRED: {
					webserviceProductMaster.setRCIMPRCREQFLAG(
							convertBooleanToString(Boolean.valueOf(updatableField.getValue().toString())));
					break;
				}
				case ResourceConstants.PRODUCT_QUALIFYING_CONDITION: {
					webserviceProductMaster.setTAXQUALCD(
							updatableField.getValue() != null ? updatableField.getValue().toString() : SINGLE_SPACE);
					break;
				}
			}
		}

		// set goods prod portion of product master
		webserviceProductMaster.setGoodsProd(
				this.getGoodsProductPartOfProductMaster(updatableFields, productIdString));

		// set deposit product relation portion of product master
		webserviceProductMaster.getProdRlshp().add(
				this.getDepositRelatedProductPartOfProductMaster(updatableFields, productIdString));
		return webserviceProductMaster;
	}

	private ProdRlshp getDepositRelatedProductPartOfProductMaster(List<UpdatableField> updatableFields, String productIdString) {
		for(UpdatableField updatableField : updatableFields){
			switch (updatableField.getField()){
				case ResourceConstants.PRODUCT_DEPOSIT_UPC: {
					ProdRlshp webserviceProductRelationship = new ProdRlshp();
					com.heb.pm.entity.ProductMaster depositProduct =
							(com.heb.pm.entity.ProductMaster)updatableField.getValue();
					webserviceProductRelationship.setPRODUCTID(productIdString);
					webserviceProductRelationship.setRELATEDPRODUCTID(String.valueOf(depositProduct.getProdId()));
					webserviceProductRelationship.setPRODUCTRLSHPCD(
							ProductRelationship.ProductRelationshipCode.DEPOSIT_PRODUCT.getValue());
					webserviceProductRelationship.setVCDEPOSITUPCH(String.valueOf(depositProduct.getProductPrimaryScanCodeId()));
					webserviceProductRelationship.setLSTUPDTUID(this.userInfo.getUserId());
					if(depositProduct.getActionCode() != null){
						webserviceProductRelationship.setACTIONCD(depositProduct.getActionCode());
					}
					return webserviceProductRelationship;
				}
			}
		}
		return null;
	}

	private ProdDescTxt convertProductDescriptionAndProductIdToProdDescTxt(String description, String productId, DescriptionType.Codes descriptionType) {
		ProdDescTxt toReturn = new ProdDescTxt();
		toReturn.setDESTYPCD(descriptionType.getId().trim());
		toReturn.setPRODID(productId);
		toReturn.setLANGTYPCD(LanguageType.Codes.ENGLISH.getId().trim());
		toReturn.setLSTUPDTUID(this.userInfo.getUserId());
		if(StringUtils.isNotEmpty(description)){
			toReturn.setPRODDESTEXT(description);
		} else {
			toReturn.setACTIONCD(DELETE_ACTION);
		}
		return toReturn;
	}

	private GoodsProd getGoodsProductPartOfProductMaster(List<UpdatableField> updatableFields, String productIdString) {

		// Webservice goods prod
		GoodsProd webserviceGoodsProd = new GoodsProd();
		boolean updatedGoodsProd = false;
		for(UpdatableField updatableField : updatableFields){
			switch (updatableField.getField()){
				case ResourceConstants.PRODUCT_GENERIC_PRODUCT: {
					updatedGoodsProd = true;
					webserviceGoodsProd.setGNRCPRODSW(
							convertBooleanToString(Boolean.valueOf(updatableField.getValue().toString())));
					break;
				}
				case ResourceConstants.PRODUCT_SELF_MANUFACTURED: {
					updatedGoodsProd = true;
					webserviceGoodsProd.setSELFMFGSW(
							convertBooleanToString(Boolean.valueOf(updatableField.getValue().toString())));
					break;
				}
				case ResourceConstants.PRODUCT_COLOR: {
					updatedGoodsProd = true;
					webserviceGoodsProd.setCOLORCD(
                            !StringUtils.isEmpty(updatableField.getValue().toString()) ? updatableField.getValue().toString() : SINGLE_SPACE);
					break;
				}
				case ResourceConstants.PRODUCT_MODEL: {
					updatedGoodsProd = true;
					webserviceGoodsProd.setPRODMODLTXT(
                            !StringUtils.isEmpty(updatableField.getValue().toString()) ? updatableField.getValue().toString() : SINGLE_SPACE);
					break;
				}
				case ResourceConstants.PRODUCT_CRITICAL_PRODUCT: {
					updatedGoodsProd = true;
					webserviceGoodsProd.setCRITPRODSW(
							updatableField.getValue() != null ? updatableField.getValue().toString() : SINGLE_SPACE);
					break;
				}
				case ResourceConstants.PRODUCT_PRE_PRICE: {
					updatedGoodsProd = true;
					webserviceGoodsProd.setPREMRKPRCAMT(updatableField.getValue().toString());
					break;
				}
				case ResourceConstants.PRODUCT_DRUG_FACT_PANEL: {
					updatedGoodsProd = true;
					webserviceGoodsProd.setDRGFACTPANSW(
							convertBooleanToString(Boolean.valueOf(updatableField.getValue().toString())));
					break;
				}
				case ResourceConstants.PRODUCT_FSA: {
					updatedGoodsProd = true;
					webserviceGoodsProd.setFSACD(
							updatableField.getValue() != null ? updatableField.getValue().toString() :  SINGLE_SPACE);
					break;
				}
				case ResourceConstants.PRODUCT_FAMILY_CODE_1: {
					updatedGoodsProd = true;
					webserviceGoodsProd.setFAM1CD(
							updatableField.getValue() != null ? updatableField.getValue().toString() : SINGLE_SPACE);
					break;
				}
				case ResourceConstants.PRODUCT_FAMILY_CODE_2: {
					updatedGoodsProd = true;
					webserviceGoodsProd.setFAM2CD(
							updatableField.getValue() != null ? updatableField.getValue().toString() : SINGLE_SPACE);
					break;
				}
				case ResourceConstants.PRODUCT_PACKAGING: {
					updatedGoodsProd = true;
					webserviceGoodsProd.setPKGTXT(
							updatableField.getValue() != null ? updatableField.getValue().toString() : SINGLE_SPACE);
					break;
				}
				case ResourceConstants.PRODUCT_SCALE_LABEL: {
					updatedGoodsProd = true;
					webserviceGoodsProd.setSCALESW(
							convertBooleanToString(Boolean.valueOf(updatableField.getValue().toString())));
					break;
				}
				case ResourceConstants.PRODUCT_FOOD_STAMP: {
					updatedGoodsProd = true;
					webserviceGoodsProd.setFDSTMPSW(
							convertBooleanToString(Boolean.valueOf(updatableField.getValue().toString())));
					break;
				}
			}
		}

		if(updatedGoodsProd){
			webserviceGoodsProd.setACTIONCD(UPDATE_ACTION);
			webserviceGoodsProd.setPRODID(productIdString);
			webserviceGoodsProd.setLSTUPDTUID(this.userInfo.getUserId());
			webserviceGoodsProd.setVCLSTUPDTUID(this.userInfo.getUserId());
			return webserviceGoodsProd;
		} else {
			return null;
		}
	}

	/**
	 * Update the related product
	 *
	 * @param relatedProducts to be updated
	 *
	 */
	public void updateRelatedProducts(List<ProductRelationship> relatedProducts) {
		if (relatedProducts.isEmpty()) {
			return;
		}

		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString().trim();
		request.setTrackingNbr(workId.trim());

		relatedProducts.stream().forEach((relatedProduct) -> {
			createRelatedProductsProdRelshp(workId, request, relatedProduct);
			createRelatedProductsGoodsProd(request, workId, relatedProduct);
		});

		this.updateProduct(request);
	}

	/**
	 * Create related products prod relshp product master record for the web service call
	 * @param workId - unique ID for this update
	 * @param request - update product request
	 * @param relatedProduct - related product to update
	 */
	private void createRelatedProductsProdRelshp(String workId, UpdateProductRequest request, ProductRelationship relatedProduct) {
		ProductMaster productMaster = new ProductMaster();
		productMaster.setWorkId(workId);

		ProdRlshp prodRelshp = new ProdRlshp();

		if (relatedProduct.getActionCode().equals(RelatedProductsService.ACTION_CODE_ADD)) {
			prodRelshp.setACTIONCD(UPDATE_ACTION);
		} else if (relatedProduct.getActionCode().equals(RelatedProductsService.ACTION_CODE_DELETE)) {
			prodRelshp.setACTIONCD(DELETE_ACTION);
		}
		prodRelshp.setLSTUPDTUID(userInfo.getUserId());
		prodRelshp.setPRODUCTID(relatedProduct.getKey().getProductId().toString());
		prodRelshp.setRELATEDPRODUCTID(relatedProduct.getKey().getRelatedProductId().toString());
		prodRelshp.setPRODUCTRLSHPCD(relatedProduct.getKey().getProductRelationshipCode());

		productMaster.getProdRlshp().add(prodRelshp);

		request.getProductMaster().add(productMaster);
	}

	/**
	 * Create related products goods prod product master record for the web service call
	 * @param workId - unique ID for this update
	 * @param request - update product request
	 * @param relatedProduct - related product to update
	 */
	private void createRelatedProductsGoodsProd(UpdateProductRequest request, String workId, ProductRelationship relatedProduct) {
		if (relatedProduct.getActionCode().equals(RelatedProductsService.ACTION_CODE_ADD)) {
			String prodId = relatedProduct.getKey().getRelatedProductId().toString();

			ProductMaster productMaster = new ProductMaster();
			productMaster.setWorkId(workId);
			productMaster.setACTIONCD(UPDATE_ACTION);
			productMaster.setLSTUPDTUID(this.userInfo.getUserId());
			productMaster.setVCLSTUPDTUSRID(this.userInfo.getUserId());

			GoodsProd goodProd = new GoodsProd();
			productMaster.setGoodsProd(goodProd);
			productMaster.getGoodsProd().setACTIONCD(UPDATE_ACTION);
			productMaster.getGoodsProd().setPRODID(prodId);
			productMaster.getGoodsProd().setSOLDSEPLYSW(relatedProduct.getRelatedProduct().getGoodsProduct().isSellableProduct() ? "Y" : "N");
			productMaster.getGoodsProd().setLSTUPDTUID(this.userInfo.getUserId());
			productMaster.getGoodsProd().setVCLSTUPDTUID(this.userInfo.getUserId());

			request.getProductMaster().add(productMaster);
		}
	}

	/**
	 * This method will take a product master with changes to it's online attributes fields
	 * @param productMaster the product master to be updated
	 * @param userId the user requesting the change
	 */
	public void updateOnlineAttributes(com.heb.pm.entity.ProductMaster productMaster, String userId){
		ProductManagementServiceClient.logger.info(String.format(ONLINE_ATTRIBUTE_UPDATE_BEGIN, userId, productMaster.getProdId()));
		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());

		// Web service productMaster.
		ProductMaster webserviceProductMaster = new ProductMaster();
		webserviceProductMaster.setWorkId(workId);
		webserviceProductMaster.setACTIONCD(UPDATE_ACTION);
		webserviceProductMaster.setPRODID(Long.toString(productMaster.getProdId()));
		webserviceProductMaster.setVCLSTUPDTUSRID(this.userInfo.getUserId());
		webserviceProductMaster.setGIFTMSGREQSW(convertTrueFalseToYNString(productMaster.getGiftMessageSwitch()));

		if(productMaster.getProductDescriptions() != null && productMaster.getProductDescriptions().size() > 0){
			ProdDescTxt webserviceProductDescription = new ProdDescTxt();
			webserviceProductDescription.setDESTYPCD("SSIZE");
			webserviceProductDescription.setLANGTYPCD("ENG");
			webserviceProductDescription.setACTIONCD(UPDATE_ACTION);
			webserviceProductDescription.setPRODDESTEXT(productMaster.getProductDescriptions().get(0).getDescription());
			webserviceProductDescription.setPRODID(productMaster.getProdId().toString());
			webserviceProductDescription.setVCLSTUPDTUID(userId);
			webserviceProductDescription.setLSTUPDTUID(userId);
			webserviceProductDescription.setVCLSTUPDTUID(userId);
			webserviceProductMaster.getProdDescTxt().add(webserviceProductDescription);
		}

		// Updates the webservice goods product based on what was updated.
		if(productMaster.getGoodsProduct() != null) {

			// Webservice goods prod
			GoodsProd webserviceGoodsProd = new GoodsProd();
			webserviceProductMaster.setGoodsProd(webserviceGoodsProd);
			webserviceProductMaster.getGoodsProd().setACTIONCD(UPDATE_ACTION);
			webserviceProductMaster.getGoodsProd().setPRODID(Long.toString(productMaster.getProdId()));
			webserviceProductMaster.getGoodsProd().setVCLSTUPDTUID(this.userInfo.getUserId());
			webserviceProductMaster.getGoodsProd().setWINPRODSW(convertTrueFalseToYNString(productMaster.getGoodsProduct().getWineProductSwitch()));
			webserviceProductMaster.getGoodsProd().setINSTRPRODNSW(convertTrueFalseToYNString(productMaster.getGoodsProduct().getInStoreProductionSwitch()));
			if(productMaster.getGoodsProduct().getHebGuaranteeTypeCode() != null) {
				webserviceProductMaster.getGoodsProd().setHEBGUARNTYPCD(productMaster.getGoodsProduct()
						.getHebGuaranteeTypeCode().getHebGuaranteeTypeCode());
			}
			if (productMaster.getGoodsProduct().getSoldBy() != null) {
				webserviceProductMaster.getGoodsProd().setCONSMPRCHCHCCD(productMaster.getGoodsProduct().getSoldBy());
			}
			if (productMaster.getGoodsProduct().getMinCustomerOrderQuantity() != null) {
				webserviceProductMaster.getGoodsProd().setMINCUSTORDQTY(String.valueOf(productMaster.getGoodsProduct().getMinCustomerOrderQuantity()));
			}
			if (productMaster.getGoodsProduct().getMaxCustomerOrderQuantity() != null) {
				webserviceProductMaster.getGoodsProd().setMAXCUSTORDQTY(String.valueOf(productMaster.getGoodsProduct().getMaxCustomerOrderQuantity()));
			}
			if (productMaster.getGoodsProduct().getCustomerOrderIncrementQuantity() != null) {
				webserviceProductMaster.getGoodsProd().setCUSTORDINCRMQTY(String.valueOf(productMaster.getGoodsProduct().getCustomerOrderIncrementQuantity()));
			}
			if (productMaster.getGoodsProduct().getFlexWeightSwitch() != null) {
				webserviceProductMaster.getGoodsProd().setWTSW(convertTrueFalseToYNString(productMaster.getGoodsProduct().getFlexWeightSwitch()));
			}
			if (productMaster.getGoodsProduct().getMinWeight() != null) {
				webserviceProductMaster.getGoodsProd().setMINUNTSZWT(String.valueOf(productMaster.getGoodsProduct().getMinWeight()));
			}
			if (productMaster.getGoodsProduct().getMaxWeight() != null) {
				webserviceProductMaster.getGoodsProd().setMAXUNTSZWT(String.valueOf(productMaster.getGoodsProduct().getMaxWeight()));
			}
		}

		request.getProductMaster().add(webserviceProductMaster);
		this.updateProduct(request);
		ProductManagementServiceClient.logger.info(String.format(ONLINE_ATTRIBUTE_UPDATE_COMPLETE, userId, productMaster.getProdId()));
	}

	/**
	 * Update eCommerce View information with product management web service. The list of attribute of eCommerce View
	 * information has been updated with product eCommerce View information web service : subscription date,
	 * fulfillment program, short description, romance copy.
	 *
	 * @param eCommerceViewDetails the ECommerceViewDetails object.
	 * @param userId  the user id.
	 */
	public void updateECommerceViewInformation(ECommerceViewDetails eCommerceViewDetails, String userId) {
		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());
		boolean changed = false;
		//check subscription date
		ProductMaster productMaster = new ProductMaster();
		GoodsProd goodsProd = new GoodsProd();
		productMaster.setWorkId(workId);
		if(eCommerceViewDetails.isSubscriptionChanged()) {
			productMaster.setLSTUPDTUID(this.userInfo.getUserId());
			productMaster.setVCLSTUPDTUSRID(this.userInfo.getUserId());
			productMaster.setACTIONCD(StringUtils.EMPTY);
			productMaster.setPRODID(Long.toString(eCommerceViewDetails.getProductId()));
			productMaster.setSUBSCRPRODSW(eCommerceViewDetails.isSubscription() ? "Y" : "N");
			if (eCommerceViewDetails.getSubscriptionStartDate() != null && eCommerceViewDetails.getSubscriptionEndDate() != null) {
				productMaster.setSUBSCRSTRTDT(ProductECommerceViewUtil.convertDateToStringDateYYYYMMDD(eCommerceViewDetails
						.getSubscriptionStartDate()));
				productMaster.setSUBSCRENDDT(ProductECommerceViewUtil.convertDateToStringDateYYYYMMDD(eCommerceViewDetails
						.getSubscriptionEndDate()));
			}
			changed = true;
		}

		//check fulfillment program
		List<ProdChnlUpdate> prodChnls = this.getFulfillmentProgramRequest(eCommerceViewDetails.getProductFullfilmentChanels(), eCommerceViewDetails.getProductId(), userId);
		goodsProd.getProdChnl().addAll(prodChnls);
		if(goodsProd.getProdChnl() != null && !goodsProd.getProdChnl().isEmpty()){
			changed = true;
			productMaster.setGoodsProd(goodsProd);
		}

		//add to request
		request.getProductMaster().add(productMaster);
		//call method update from service
		if(changed) {
			this.updateProduct(request);
		}
	}

	/**
	 * Get update fulfillment program request.
	 *
	 * @param productFullfilmentChanels the list of ecommerce fulfillments
	 * @param productId                 the prod id
	 * @param userId                    the user id
	 */
	public List<ProdChnlUpdate> getFulfillmentProgramRequest(List<ProductFullfilmentChanel> productFullfilmentChanels, long productId, String userId) {
		List<ProdChnlUpdate> prodChnls = new ArrayList<>();
		if (productFullfilmentChanels != null && !productFullfilmentChanels.isEmpty()) {
			ProdChnlUpdate prodChnl;
			boolean isExpiredDay = false;
			List<ProductFullfilmentChanel> listProductFullfilmentChanel = productFullfilmentChanelRepository.findByKeyProductId(productId);
			for (ProductFullfilmentChanel productFullfilmentChanel : productFullfilmentChanels) {
				isExpiredDay = this.checkFulfillmentProgramExpiredDay(listProductFullfilmentChanel,
						productFullfilmentChanel.getKey().getSalesChanelCode(),
						productFullfilmentChanel.getKey().getFullfillmentChanelCode());
				prodChnl = new ProdChnlUpdate();
				prodChnl.setPRODID(String.valueOf(productId));
				prodChnl.setSALSCHNLCD(productFullfilmentChanel.getKey().getSalesChanelCode());
				prodChnl.setFLFLCHNLCD(productFullfilmentChanel.getKey().getFullfillmentChanelCode());
				prodChnl.setEFFDT(ProductECommerceViewUtil.convertDateToStringDateYYYYMMDD(productFullfilmentChanel.getEffectDate()));
				prodChnl.setEXPRNDT(ProductECommerceViewUtil.convertDateToStringDateYYYYMMDD(productFullfilmentChanel.getExpirationDate()));
				if(productFullfilmentChanel.getActionCode().equalsIgnoreCase(ADD_ACTION)){
					if(isExpiredDay){
						// Update case.
						prodChnl.setACTIONCD(UPDATE_ACTION);
					}else{
						// Add case.
						prodChnl.setACTIONCD(ADD_ACTION);
					}
				}else if(productFullfilmentChanel.getActionCode().equalsIgnoreCase(DELETE_ACTION)){
					// Delete case.
					prodChnl.setACTIONCD(DELETE_ACTION);
				}else{
					// Update case.
					prodChnl.setACTIONCD(UPDATE_ACTION);
				}
				prodChnl.setLSTUPDTUID(userId);
				prodChnls.add(prodChnl);
			}
		}
		return prodChnls;
	}

	private ProdScnCdExtent generateProductScanCodeExtension(long scanCodeId, String content, String prodExtDtaCd,
															 String userId) {
		ProdScnCdExtent prodScnCdExtent = new ProdScnCdExtent();
		prodScnCdExtent.setSCNCDID(String.valueOf(scanCodeId));
		prodScnCdExtent.setPRODEXTDTACD(prodExtDtaCd);
		if (StringUtils.isNotBlank(content)) {
			prodScnCdExtent.setPRODDESTXTTEXT(content);
			prodScnCdExtent.setACTIONCD(UPDATE_ACTION);
		} else {
			prodScnCdExtent.setACTIONCD(DELETE_ACTION);
		}
		prodScnCdExtent.setSRCSYSTEMID(String.valueOf(ProductECommerceViewService.SourceSystemNumber
				.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue()));
		prodScnCdExtent.setCRE8ID(userId);
		prodScnCdExtent.setLSTUPDTUID(userId);
		prodScnCdExtent.setLSTUPDTTS(DateTime.now().toString(DATE_FORMAT_YYYYMMDD));
		return prodScnCdExtent;
	}

	public void updateDataSourceForProdScnCdExtent(Long productId, Long scanCodeId, String content, String
			prodExtDtaCd, String userId){
		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());

		ProductMaster productMaster = new ProductMaster();
		productMaster.setWorkId(workId);

		//add product scan code extension
		ProdScnCdExtent prodScnCdExtent = this.generateProductScanCodeExtension(scanCodeId, content,
				prodExtDtaCd, userId);
		productMaster.getProdScnCdExtent().add(prodScnCdExtent);

		//add to request
		request.getProductMaster().add(productMaster);
		//call method update from service
		this.updateProduct(request);
	}
	/**
	 * save itemMaster variant
	 * @param itemMaster
	 */
	public void saveItemMasterVariant(com.heb.pm.entity.ItemMaster itemMaster){
		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());

		ItemMaster item = new ItemMaster();
		item.setVARIANTCD(com.heb.pm.entity.ItemMaster.VARIANT_CODE_KNOWN);
		item.setITMID(String.valueOf(itemMaster.getKey().getItemCode()));
		item.setITMKEYTYPCD(itemMaster.getKey().getItemType());
		item.setACTIONCD(UPDATE_ACTION);
		ProductMaster product = new ProductMaster();
		product.getItemMaster().add(item);
		product.setWorkId(workId);
		request.getProductMaster().add(product);
		//call method update from service
		this.updateProduct(request);
	}
	public void updateProductManagement(ProductMaster productMaster) {
		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());
		productMaster.setWorkId(workId);
		//add to request
		request.getProductMaster().add(productMaster);
		//call method update from service
		this.updateProduct(request);
	}


	/**
	 * Update the list of product relationship
	 *
	 * @param relatedProducts to be updated
	 *
	 */
	public void updateProductRelationships(List<ProductRelationship> relatedProducts) {
		if (relatedProducts.isEmpty()) {
			return;
		}

		UpdateProductRequest request = new UpdateProductRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString().trim();
		request.setTrackingNbr(workId.trim());

		ProductMaster productMaster = new ProductMaster();
		productMaster.setWorkId(workId);

		relatedProducts.forEach(relatedProduct -> {
			ProdRlshp prodRelshp = new ProdRlshp();

			if (relatedProduct.getActionCode().equals(RelatedProductsService.ACTION_CODE_ADD)) {
				prodRelshp.setACTIONCD(UPDATE_ACTION);
			} else if (relatedProduct.getActionCode().equals(RelatedProductsService.ACTION_CODE_DELETE)) {
				prodRelshp.setACTIONCD(DELETE_ACTION);
				prodRelshp.setVCDEPOSITUPCH("0");
			}
			prodRelshp.setLSTUPDTUID(userInfo.getUserId());
			prodRelshp.setPRODUCTID(relatedProduct.getKey().getProductId().toString());
			prodRelshp.setRELATEDPRODUCTID(relatedProduct.getKey().getRelatedProductId().toString());
			prodRelshp.setPRODUCTRLSHPCD(relatedProduct.getKey().getProductRelationshipCode());
			if(relatedProduct.getProductQuantity() != null) {
				prodRelshp.setPRODUCTQTY(relatedProduct.getProductQuantity().toString());
			}else{
				prodRelshp.setPRODUCTQTY("0");
			}
			productMaster.getProdRlshp().add(prodRelshp);
		});

		request.getProductMaster().add(productMaster);

		this.updateProduct(request);
	}

	/**
	 * Get value with default as space. If value is blank return single space else return the value.
	 *
	 * @param value the string value.
	 * @return an space string if value is blank, or the value if value is not blank.
	 */
	private String getValueWithDefaultAsSpace(String value) {
		return StringUtils.isBlank(value) ? SINGLE_SPACE : value;
	}
}
