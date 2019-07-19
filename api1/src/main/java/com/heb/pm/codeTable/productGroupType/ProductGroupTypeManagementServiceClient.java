/*
 *  ProductGroupTypeManagementServiceClient
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.productGroupType;

import com.heb.pm.entity.*;
import com.heb.pm.repository.ProductGroupChoiceOptionRepository;
import com.heb.pm.repository.ProductGroupChoiceTypeRepository;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import com.heb.util.controller.UserInfo;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.xmlns.ei.CodeTableManagementServiceServiceagent;
import com.heb.xmlns.ei.CodeTableServicePort;
import com.heb.xmlns.ei.codetablemanagement.update_codetable_request.UpdateCodeTableRequest;
import com.heb.xmlns.ei.prod_grp_chc_opt.PRODGRPCHCOPT;
import com.heb.xmlns.ei.prod_grp_chc_typ.PRODGRPCHCTYP;
import com.heb.xmlns.ei.prod_grp_typ.PRODGRPTYP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Handle ProductGroupType before call CodeTableManagementService.
 *
 * @author vn70529
 * @since 2.15.0
 */
@Service
public class ProductGroupTypeManagementServiceClient extends BaseWebServiceClient<CodeTableManagementServiceServiceagent, CodeTableServicePort> {
	private static final Logger logger = LoggerFactory.getLogger(ProductGroupTypeManagementServiceClient.class);
	@Value("${codeTableManagementService.uri}")
	private String uri;
	@Autowired
	private UserInfo userInfo;
	@Autowired
	private CodeTableManagementServiceClient codeTableManagementServiceClient;
	@Autowired
	private ProductGroupChoiceOptionRepository productGroupChoiceOptionRepository;
	@Autowired
	private ProductGroupChoiceTypeRepository productGroupChoiceTypeRepository;

	private static final String SWITCH_Y = "Y";
	private static final String SWITCH_N = "N";

	/**
	 * Return the service agent for this client.
	 *
	 * @return CodeTableManagementServiceServiceagent associated with this client.
	 */
	@Override
	protected CodeTableManagementServiceServiceagent getServiceAgent() {
		try {
			URL url = new URL(this.getWebServiceUri());
			return new CodeTableManagementServiceServiceagent(url);
		} catch (MalformedURLException e) {
			ProductGroupTypeManagementServiceClient.logger.error(e.getMessage());
		}
		return new CodeTableManagementServiceServiceagent();
	}
	/**
	 * Return the port type for this client.
	 *
	 * @param agent The agent to use to create the port.
	 * @return CodeTableServicePort associated with this client.
	 */
	@Override
	protected CodeTableServicePort getServicePort(CodeTableManagementServiceServiceagent agent) {
		return agent.getCodeTableManagementService();
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
	 * Add Product Group Type.
	 *
	 * @param productGroupType the information of Product Group Type Add.
	 */
	public void addProductGroupType(ProductGroupType productGroupType) {
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(this.getWorkId().toString());
		List<PRODGRPTYP> listOfProductGroupTypes = this.convertProductGroupType(productGroupType, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
		if (listOfProductGroupTypes != null && listOfProductGroupTypes.size() > 0) {
			request.getPRODGRPTYP().addAll(listOfProductGroupTypes);
			String productGroupTypeCode = this.codeTableManagementServiceClient.updateProductGroupType(request);
			this.updateChoiceTypeAndChoiceOption(productGroupTypeCode, productGroupType.getProductGroupChoiceTypes(),getProductGroupChoiceOptions(productGroupType));
		}
	}
	/**
	 * Update the list of choice type basing in action (add new, edit or delete).
	 *
	 * @param productGroupType the list of choice types requested to delete/insert/update.
	 */
	public void updateProductGroupType(ProductGroupType productGroupType){
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(this.getWorkId().toString());
		List<ProductGroupChoiceOption> productGroupChoiceOptions = getProductGroupChoiceOptions(productGroupType);
		List<ProductGroupChoiceType> productGroupChoiceTypes = new ArrayList<ProductGroupChoiceType>();
		for (ProductGroupChoiceType productGroupChoiceType:productGroupType.getProductGroupChoiceTypes()) {
			if(productGroupChoiceType.getAction() != null) {
				productGroupChoiceTypes.add(productGroupChoiceType);
			}
		}
		this.deleteChcOptForChcTyp(productGroupChoiceOptions, productGroupType.getProductGroupTypeCode());
		this.updateChoiceTypeAndChoiceOption(productGroupType.getProductGroupTypeCode(), productGroupChoiceTypes, productGroupChoiceOptions);
		List<PRODGRPTYP> listOfProductGroupTypes = this.convertProductGroupType(productGroupType, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
		if (listOfProductGroupTypes != null && listOfProductGroupTypes.size() > 0) {
			request.getPRODGRPTYP().addAll(listOfProductGroupTypes);
			this.codeTableManagementServiceClient.updateProductGroupType(request);
		}
	}
	/**
	 * Delete product group choice option.
	 *
	 * @param productGroupTypeCode      the productGroupTypeCode.
	 * @param productGroupChoiceOptions the information of Product Group Choice Option update.
	 */
	private void deleteChcOptForChcTyp(List<ProductGroupChoiceOption> productGroupChoiceOptions, String productGroupTypeCode) {
		List<PRODGRPCHCOPT> listOfProductGroupChoiceOptionDeletes = this.convertProductGroupChoiceOption(productGroupChoiceOptions, productGroupTypeCode, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
		if (listOfProductGroupChoiceOptionDeletes !=null && listOfProductGroupChoiceOptionDeletes.size() > 0) {
			UpdateCodeTableRequest request = new UpdateCodeTableRequest();
			request.setAuthentication(this.getAuthentication());
			request.setTrackingNbr(this.getWorkId().toString());
			request.getPRODGRPCHCOPT().addAll(listOfProductGroupChoiceOptionDeletes);
			this.codeTableManagementServiceClient.updateProductGroupType(request);
		}
	}
	/**
	 * Get list ProductGroupChoiceOptions change.
	 *
	 * @param productGroupType      the productGroupType.
	 * @return List<ProductGroupChoiceOption> the list ProductGroupChoiceOption change.
	 */
	private List<ProductGroupChoiceOption> getProductGroupChoiceOptions(ProductGroupType productGroupType){
		List<ProductGroupChoiceOption> productGroupChoiceOptions = new ArrayList<ProductGroupChoiceOption>();
		List<ProductGroupChoiceType> productGroupChoiceTypes = productGroupType.getProductGroupChoiceTypes();
		for (ProductGroupChoiceType productGroupChoiceType:productGroupChoiceTypes) {
			if(productGroupChoiceType.getProductGroupChoiceOptions() !=null && productGroupChoiceType.getProductGroupChoiceOptions().size() > 0) {
				productGroupChoiceOptions.addAll(productGroupChoiceType.getProductGroupChoiceOptions());
			}
		}
		return productGroupChoiceOptions;
	}
	/**
	 * Update Data for Product Group Type.
	 *
	 * @param productGroupTypeCode the productGroupTypeCode.
	 * @param productGroupChoiceTypes the list of ProductGroupChoiceType change.
	 * @param productGroupChoiceOptions the list of ProductGroupChoiceOption change.
	 */
	private void updateChoiceTypeAndChoiceOption(String productGroupTypeCode, List<ProductGroupChoiceType> productGroupChoiceTypes, List<ProductGroupChoiceOption> productGroupChoiceOptions) {
		List<PRODGRPCHCTYP> listOfProductGroupChoiceTypes = this.convertProductGroupChoiceType(productGroupChoiceTypes, productGroupTypeCode);
		List<PRODGRPCHCOPT> listOfProductGroupChoiceOptions = this.convertProductGroupChoiceOption(productGroupChoiceOptions, productGroupTypeCode, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
		if ((listOfProductGroupChoiceTypes != null && listOfProductGroupChoiceTypes.size() > 0) || (listOfProductGroupChoiceOptions!= null && listOfProductGroupChoiceOptions.size() > 0)) {
			UpdateCodeTableRequest request = new UpdateCodeTableRequest();
			request.setAuthentication(this.getAuthentication());
			request.setTrackingNbr(this.getWorkId().toString());
			if (listOfProductGroupChoiceTypes != null && listOfProductGroupChoiceTypes.size() > 0) {
				request.getPRODGRPCHCTYP().addAll(listOfProductGroupChoiceTypes);
			}
			if (listOfProductGroupChoiceOptions != null && listOfProductGroupChoiceOptions.size() > 0) {
				request.getPRODGRPCHCOPT().addAll(listOfProductGroupChoiceOptions);
			}
			this.codeTableManagementServiceClient.updateProductGroupType(request);
		}
	}
	/**
	 * Delete Product Group Type.
	 *
	 * @param productGroupType the information of Product Group Type delete.
	 * @return the information of Product Group Type after delete.
	 */
	public void deleteProductGroupType(ProductGroupType productGroupType) {
		List<ProductGroupChoiceOption> productGroupChoiceOptions =  this.productGroupChoiceOptionRepository.findByKeyProductGroupTypeCode(productGroupType.getProductGroupTypeCode());
		List<ProductGroupChoiceType> productGroupChoiceTypes = this.productGroupChoiceTypeRepository.findByKeyProductGroupTypeCode(productGroupType.getProductGroupTypeCode());
		this.deleteChcOptForChcTyp(productGroupChoiceOptions, productGroupType.getProductGroupTypeCode());
		this.updateChoiceTypeAndChoiceOption(productGroupType.getProductGroupTypeCode(), productGroupChoiceTypes, productGroupChoiceOptions);
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(this.getWorkId().toString());
		PRODGRPTYP productGroupTypeForWs = new PRODGRPTYP();
		productGroupTypeForWs.setACTIONCD(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
		productGroupTypeForWs.setPRODGRPTYPCD(productGroupType.getProductGroupTypeCode());
		productGroupTypeForWs.setWorkId(productGroupType.getProductGroupTypeCode());
		request.getPRODGRPTYP().add(productGroupTypeForWs);
		this.codeTableManagementServiceClient.updateProductGroupType(request);
	}
	/**
	 * Convert ProductGroupType to PRODGRPTYP.
	 *
	 * @param productGroupType the ProductGroupType.
	 * @param action             the action add/update.
	 * @return the list PRODGRPTYP.
	 */
	private List<PRODGRPTYP> convertProductGroupType(ProductGroupType productGroupType, String action) {
		List<PRODGRPTYP> listOfProductGroupTypes = new ArrayList<>();
		PRODGRPTYP productGroupTypeForWs = new PRODGRPTYP();
		if (productGroupType != null) {
			if (action.equals(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue())) {
				productGroupTypeForWs.setACTIONCD(action);
				productGroupTypeForWs.setPRODGRPTYPCD(" ");
			} else {
				productGroupTypeForWs.setACTIONCD(action);
				productGroupTypeForWs.setPRODGRPTYPCD(productGroupType.getProductGroupTypeCode());
				productGroupTypeForWs.setWorkId(productGroupType.getProductGroupTypeCode());
			}
			productGroupTypeForWs.setCUSTPRODTYPNM(productGroupType.getProductGroupType());
			productGroupTypeForWs.setSTRDEPTNBR(productGroupType.getDepartmentNumberString());
			productGroupTypeForWs.setSTRSUBDEPTID(productGroupType.getSubDepartmentId());
			productGroupTypeForWs.setPSSDEPT1("0");
			listOfProductGroupTypes.add(productGroupTypeForWs);
		}
		return listOfProductGroupTypes;
	}
	/**
	 * Convert ProductGroupChoiceType to PRODGRPCHCTYP.
	 *
	 * @param productGroupTypeCode    the productGroupTypeCode.
	 * @param productGroupChoiceTypes the product group choice type convert.
	 * @return the list PRODGRPCHCTYP.
	 */
	private List<PRODGRPCHCTYP> convertProductGroupChoiceType(List<ProductGroupChoiceType> productGroupChoiceTypes, String productGroupTypeCode) {
		List<PRODGRPCHCTYP> listOfProductGroupChoiceTypes = new ArrayList<PRODGRPCHCTYP>();
		if (!productGroupChoiceTypes.isEmpty()) {
			for (ProductGroupChoiceType productGroupChoiceType : productGroupChoiceTypes) {
				PRODGRPCHCTYP productGroupChoiceTypeForWs = new PRODGRPCHCTYP();
				if (productGroupChoiceType.getAction() == null) {
					productGroupChoiceTypeForWs.setACTIONCD(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
				} else {
					productGroupChoiceTypeForWs.setACTIONCD(productGroupChoiceType.getAction());
				}
				if (productGroupChoiceType.getPickerSwitch()) {
					productGroupChoiceTypeForWs.setPCKERSW(SWITCH_Y);
				} else {
					productGroupChoiceTypeForWs.setPCKERSW(SWITCH_N);
				}
				productGroupChoiceTypeForWs.setWorkId(productGroupTypeCode);
				if (productGroupChoiceType.getAction() != null && productGroupChoiceType.getAction().equals(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue())) {
					productGroupChoiceTypeForWs.setREQSW(SWITCH_N);
				}
				productGroupChoiceTypeForWs.setPRODGRPTYPCD(productGroupTypeCode);
				productGroupChoiceTypeForWs.setCHCTYPCD(productGroupChoiceType.getChoiceType().getChoiceTypeCode());
				productGroupChoiceTypeForWs.setLSTUPDTUID(this.userInfo.getUserId());
				listOfProductGroupChoiceTypes.add(productGroupChoiceTypeForWs);
			}
		}
		return listOfProductGroupChoiceTypes;
	}
	/**
	 * Convert ProductGroupChoiceOption to PRODGRPCHCOPT.
	 *
	 * @param productGroupTypeCode      the productGroupTypeCode.
	 * @param productGroupChoiceOptions the product group choice option convert.
	 * @param action                    the action add new or delete.
	 * @return the list PRODGRPCHCOPT.
	 */
	private List<PRODGRPCHCOPT> convertProductGroupChoiceOption(List<ProductGroupChoiceOption> productGroupChoiceOptions, String productGroupTypeCode, String action) {
		List<PRODGRPCHCOPT> listOfProductGroupChoiceOptionWS = new ArrayList<>();
		for (ProductGroupChoiceOption productGroupChoiceOption : productGroupChoiceOptions) {
			if (productGroupChoiceOption.getAction() == null || action.equals(productGroupChoiceOption.getAction())) {
				PRODGRPCHCOPT productGroupChoiceOptionForWs = new PRODGRPCHCOPT();
				if (productGroupChoiceOption.getAction() == null) {
					productGroupChoiceOptionForWs.setACTIONCD(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
				} else {
					productGroupChoiceOptionForWs.setACTIONCD(action);
				}
				productGroupChoiceOptionForWs.setWorkId(this.getWorkId().toString());
				productGroupChoiceOptionForWs.setPRODGRPTYPCD(productGroupTypeCode);
				productGroupChoiceOptionForWs.setCHCTYPCD(productGroupChoiceOption.getKey().getChoiceTypeCode());
				productGroupChoiceOptionForWs.setCHCOPTCD(productGroupChoiceOption.getKey().getChoiceOptionCode());
				productGroupChoiceOptionForWs.setLSTUPDTUID(this.userInfo.getUserId());
				listOfProductGroupChoiceOptionWS.add(productGroupChoiceOptionForWs);
			}
		}
		return listOfProductGroupChoiceOptionWS;
	}
}
