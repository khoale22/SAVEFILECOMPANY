/*
 *  CodeTableManagementServiceClient
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.ws;

import com.heb.pm.customHierarchy.HierarchyContextController;
import com.heb.pm.entity.*;
import com.heb.pm.repository.GenericEntityRepository;
import com.heb.pm.repository.ProductGroupChoiceOptionRepository;
import com.heb.pm.repository.ProductGroupChoiceTypeRepository;
import com.heb.util.controller.UserInfo;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.CodeTableManagementServiceServiceagent;
import com.heb.xmlns.ei.CodeTableServicePort;
import com.heb.xmlns.ei.Fault;
import com.heb.xmlns.ei.attr.ATTR;
import com.heb.xmlns.ei.choiceopt.ChoiceOpt;
import com.heb.xmlns.ei.choicetyp.ChoiceTyp;
import com.heb.xmlns.ei.cntry_cd.CountryCode;
import com.heb.xmlns.ei.codetablemanagement.update_codetable_reply.UpdateReplyMessage;
import com.heb.xmlns.ei.codetablemanagement.update_codetable_request.UpdateCodeTableRequest;
import com.heb.xmlns.ei.cust_prod_grp.CUSTPRODGRP;
import com.heb.xmlns.ei.dstrb_fltr.DSTRBFLTR;
import com.heb.xmlns.ei.ecom_usr_grp_attr.ECOMUSRGRPATTR;
import com.heb.xmlns.ei.enty.ENTY;
import com.heb.xmlns.ei.enty_attr.ENTYATTR;
import com.heb.xmlns.ei.enty_des.ENTYDES;
import com.heb.xmlns.ei.enty_rlshp.ENTYRLSHP;
import com.heb.xmlns.ei.fctry.FactoryMaster;
import com.heb.xmlns.ei.prod_cat.PRODCAT;
import com.heb.xmlns.ei.prod_grp_chc_opt.PRODGRPCHCOPT;
import com.heb.xmlns.ei.prod_grp_chc_typ.PRODGRPCHCTYP;
import com.heb.xmlns.ei.prod_grp_typ.PRODGRPTYP;
import com.heb.xmlns.ei.scoring_org.SCORINGORG;
import com.heb.xmlns.ei.st_prod_warn.STPRODWARN;
import com.heb.xmlns.ei.tbcoprodtype.TbcoProdType;
import com.heb.xmlns.ei.varietal.VARIETAL;
import com.heb.xmlns.ei.varietal_type.VARIETALTYPE;
import com.heb.xmlns.ei.wine_area.WINEAREA;
import com.heb.xmlns.ei.wine_rgn.WINERGN;
import com.heb.xmlns.ei.winemkr.WINEMKR;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Provides access to service endpoint for CodeTableManagementService.
 *
 * @author l730832
 * @since 2.12.0
 */
@Service
public class CodeTableManagementServiceClient extends BaseWebServiceClient<CodeTableManagementServiceServiceagent, CodeTableServicePort> {

	private static final Logger logger = LoggerFactory.getLogger(CodeTableManagementServiceClient.class);

	private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

	@Value("${codeTableManagementService.uri}")
	private String uri;
	@Autowired
	private UserInfo userInfo;
	@Autowired
	private GenericEntityRepository genericEntityRepository;

	/**
	 * Action code constant.
	 */
	public enum ACTION_CODE { ACTION_CD_DELETE("D"), ACTION_CD_ADD("A"), ACTION_CD_UPDATE(""),ACTION_CD_ALTERNATE("EA"),ACTION_CD_PRIMARY("EP");

		private String value;

		ACTION_CODE(String value) {
			this.value = value;
		}

		/**
		 * Gets value.
		 *
		 * @return the value
		 */
		public String getValue() {
			return this.value;
		}
	}
	/**
	 * Const default code
	 */
	private static final String PSS_DEPT_ZERO = "0";
	private static final String DEFAULT_CHOICE_TYPE_CODE = "0";
	private static final String DEFAULT_PARENT_CHOICE_TYPE_CODE = " ";
	private static final String DEFAULT_PRODUCT_CATEGORY_CODE = "0";
	private static final String SINGLE_SPACE = " ";
	private static final String DEFAULT_CHOICE_OPTION_CODE = "0";
	private static final String WEBSERVICE_RESPONSE_MESSAGE_SUCCESS = "Success";
	private static final String SWITCH_Y = "Y";
	private static final String SWITCH_N = "N";
	private static final String SEQUENCE_SWITCH_N = "N";
	private static final String DEFAULT_TRACKING_NUMBER = "0";

	private static final String DISTRIBUTION_FILTER_UPDATE_BEGIN="User %s is trying to update the distribution filter with keyid %s";
	private static final String DISTRIBUTION_FILTER_UPDATE_COMPLETE="User %s has completed an update the distribution filter with keyid %s";
	private static final String CURRENT_LEVEL_ATTRIBURE_UPDATE_BEGIN=
			"User %s is trying to update the entity relationship data for hierarchy context: %s, between parent id: %d, and child id: %d.";
	private static final String CURRENT_LEVEL_ATTRIBURE_UPDATE_COMPLETE=
			"User %s has completed the entity relationship data for hierarchy context: %s, between parent id: %d, and child id: %d.";
	private static final String MOVE_LEVEL_BEGIN =
			"User %s is trying to move the entity relationship for hierarchy context: %s, between parent id: %d, and child id: %d.";
	private static final String LINK_LEVEL_BEGIN=
			"User %s is trying to link %d entity relationships.";
	private static final String MOVE_LEVEL_COMPLETE=
			"User %s has completed moving the entity relationship for hierarchy context: %s, between parent id: %d, and child id: %d.";
	private static final String LINK_LEVEL_COMPLETE =
			"User %s has completed linking %d entity relationships.";

	/**
	 * PGRP.
	 */
	public static final String PGRP = "PGRP";
	private static String PRIMARY_PATH="Primary";

	private static final String ECOM_ATTRIBUTE_DELETE = "D";
	private static final String ECOM_ATTRIBUTE_ADD = "A";
	private static final String ECOM_ATTRIBUTE_NO_CHANGE = "N";
	private static final Long PRODUCT_CODE = 16L;
	private static final int ABBREVIATION_CHARACTER_LIMIT = 6;
	private static final String DELETE_ALL_CHILDREN_CODE_SINGLE="S";
	private static final String DELETE_ALL_CHILDREN_CODE_YES="Y";
	private static final String DELETE_ALL_CHILDREN_CODE_NO="N";


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
			CodeTableManagementServiceClient.logger.error(e.getMessage());
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
	 * update wine maker. send to webservice object and action code.
	 * then will get message from webservice to check status request
	 * @param lst wine maker
	 * @param action action update, deleted or insert
	 */
	public void updateWineMaker(List<WineMaker> lst, String action){
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(this.getWorkId().toString());
		UpdateCodeTableRequest.WINECODETABLES wine=new UpdateCodeTableRequest.WINECODETABLES();
		for(WineMaker wineMakerInput:lst){
			WINEMKR wineMaker = new WINEMKR();
			wineMaker.setACTIONCD(action);
			wineMaker.setWorkId(this.getWorkId().toString());
			if(wineMakerInput.getWineMakerId()!=null){
				wineMaker.setWINEMKRID(String.valueOf(wineMakerInput.getWineMakerId()));
			}
			wineMaker.setWINEMKRNM(wineMakerInput.getWineMakerName());
			wineMaker.setWINEMKRNARRTXT(wineMakerInput.getWineMakerDescription());
			wine.getWINEMKR().add(wineMaker);
		}
		request.setWINECODETABLES(wine);
		processReplyFromWebservice(request);
	}
	/**
	 * update wine area. send to webservice object and action code.
	 * then will get message from webservice to check status request
	 * @param lst wine area
	 * @param action action update, deleted or insert
	 */
	public void updateWineArea(List<WineArea> lst, String action){
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(this.getWorkId().toString());
		UpdateCodeTableRequest.WINECODETABLES wine=new UpdateCodeTableRequest.WINECODETABLES();
		for(WineArea wineAreaInput:lst){
			WINEAREA wineArea = new WINEAREA();
			wineArea.setACTIONCD(action);
			if(wineAreaInput.getWineAreaId()!=null){
				wineArea.setWINEAREAID(String.valueOf(wineAreaInput.getWineAreaId()));
			}
			wineArea.setWINEAREANM(wineAreaInput.getWineAreaName());
			wineArea.setWINEAREADES(wineAreaInput.getWineAreaDescription());
			wineArea.setWorkId(this.getWorkId().toString());
			wine.getWINEAREA().add(wineArea);
		}
		request.setWINECODETABLES(wine);
		processReplyFromWebservice(request);
	}
	/**
	 * update wine region. send to webservice object and action code.
	 * then will get message from webservice to check status request
	 * @param lst wine region
	 * @param action action update, deleted or insert
	 */
	public void updateWineRegion(List<WineRegion> lst, String action){
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(this.getWorkId().toString());
		UpdateCodeTableRequest.WINECODETABLES wine=new UpdateCodeTableRequest.WINECODETABLES();
		for(WineRegion wineRegionInput:lst){
			WINERGN wineRegion = new WINERGN();
			wineRegion.setACTIONCD(action);
			if(wineRegionInput.getWineRegionId()!=null){
				wineRegion.setWINERGNID(String.valueOf(wineRegionInput.getWineRegionId()));
			}
			wineRegion.setWINEAREAID(String.valueOf(wineRegionInput.getWineArea().getWineAreaId()));
			wineRegion.setWINERGNNM(wineRegionInput.getWineRegionName());
			wineRegion.setWINERGNDES(wineRegionInput.getWineRegionDescription());
			wineRegion.setWorkId(this.getWorkId().toString());
			wine.getWINERGN().add(wineRegion);
		}
		request.setWINECODETABLES(wine);
		processReplyFromWebservice(request);
	}
	/**
	 * update varietal info. send to webservice object and action code. then will get message from webservice to check status request
	 * @param lst list varietal
	 * @param action action update, deleted or insert
	 */
	public void updateVarietal(List<Varietal> lst, String action){
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(this.getWorkId().toString());
		UpdateCodeTableRequest.WINECODETABLES wine=new UpdateCodeTableRequest.WINECODETABLES();
		for(Varietal varietalInput:lst){
			VARIETAL varietal = new VARIETAL();
			varietal.setACTIONCD(action);
			if(varietalInput.getVarietalId()!=null){
				varietal.setVARTLID(String.valueOf(varietalInput.getVarietalId()));
			}
			varietal.setVARTLTYPCD(String.valueOf(varietalInput.getVarietalType().getVarietalTypeCode()));
			varietal.setVARTLNM(varietalInput.getVarietalName());
			varietal.setWorkId(this.getWorkId().toString());
			wine.getVARIETAL().add(varietal);
		}
		request.setWINECODETABLES(wine);
		processReplyFromWebservice(request);
	}
	/**
	 * update varietal info. send to webservice object and action code. then will get message from webservice to check status request
	 * @param lst list varietal
	 * @param action action update, deleted or insert
	 */
	public void updateVarietalType(List<VarietalType> lst, String action){
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		Integer workId = this.getWorkId();
		request.setTrackingNbr(workId.toString());
		UpdateCodeTableRequest.WINECODETABLES wine=new UpdateCodeTableRequest.WINECODETABLES();
		for(VarietalType varietalTypInput:lst){
			VARIETALTYPE varietalType = new VARIETALTYPE();
			varietalType.setACTIONCD(action);
			varietalType.setVARTLTYPDES(varietalTypInput.getVarietalTypeDescription());
			varietalType.setVARTLTYPABB(varietalTypInput.getVarietalTypeAbbreviations());
			if(varietalTypInput.getVarietalTypeCode()!=null){
				varietalType.setVARTLTYPCD(String.valueOf(varietalTypInput.getVarietalTypeCode()));
			}
			varietalType.setWorkId(workId.toString());
			wine.getVARIETALTYPE().add(varietalType);
		}
		request.setWINECODETABLES(wine);
		processReplyFromWebservice(request);

	}
	/**
	 * submit request from webservice.
	 *
	 * @param request request to webservice.
	 */
	private void processReplyFromWebservice(UpdateCodeTableRequest request){
		try {
			UpdateReplyMessage reply=this.getPort().updateCodeTableDetails(request);
			StringBuilder errorMessage = new StringBuilder("");
			if(!reply.getUpdateCodeTableReply().isEmpty()){
				for(UpdateReplyMessage.UpdateCodeTableReply updateCodeTableReply : reply.getUpdateCodeTableReply()){
					logger.info(updateCodeTableReply.getReturnMsg());
					logger.info(updateCodeTableReply.getKeyData());
					logger.info("/n");
					if(!isSuccess(updateCodeTableReply.getReturnMsg())){
						errorMessage.append(updateCodeTableReply.getReturnMsg());
					}
				}
				if(!errorMessage.toString().equals(StringUtils.EMPTY)){
					throw new SoapException(errorMessage.toString());
				}
			}
		} catch (Exception e) {
			throw new SoapException(e.getMessage(), e.getCause());
		}
	}
	/**
	 * Update information a list of factory in code table basing in action (add new, edit or delete).
	 *
	 * @param factories The list of factory requested to delete/insert/update
	 * @param action action update, deleted or insert.
	 */
	public void updateFactories(List<Factory> factories, String action) {
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(this.getWorkId().toString());
		List<FactoryMaster> factoryMasters = new ArrayList<>();
		for(Factory factory : factories){
			FactoryMaster factoryMaster = new FactoryMaster();
			factoryMaster.setACTIONCD(action);
			factoryMaster.setWorkId(this.getWorkId().toString());
			factoryMaster.setFCTRYID(String.valueOf(factory.getFactoryId()));
			factoryMaster.setFCTRYNM(StringUtils.isBlank(factory.getFactoryName())?SINGLE_SPACE:factory.getFactoryName());
			factoryMaster.setCNTRYNM(StringUtils.isBlank(factory.getCountry())?SINGLE_SPACE:factory.getCountry());
			factoryMaster.setCNTYTXT(StringUtils.isBlank(factory.getCounty())?SINGLE_SPACE:factory.getCounty());
			factoryMaster.setCTYTXT(StringUtils.isBlank(factory.getCity())?SINGLE_SPACE:factory.getCity());
			factoryMaster.setFAXPHNNBR(StringUtils.isBlank(factory.getFax())?SINGLE_SPACE:factory.getFax());
			factoryMaster.setFCTRYCONTCNM(StringUtils.isBlank(factory.getContactName())?SINGLE_SPACE:factory.getContactName());
			factoryMaster.setFCTRYSTATCD(StringUtils.isBlank(factory.getStatus())?SINGLE_SPACE:factory.getStatus());
			factoryMaster.setPHNNBR(StringUtils.isBlank(factory.getPhone())?SINGLE_SPACE:factory.getPhone());
			factoryMaster.setSTNM(StringUtils.isBlank(factory.getState())?SINGLE_SPACE:factory.getState());
			factoryMaster.setSTRETONETXT(StringUtils.isBlank(factory.getAddressOne())?SINGLE_SPACE:factory.getAddressOne());
			factoryMaster.setSTRETTWOTXT(StringUtils.isBlank(factory.getAddressTwo())?SINGLE_SPACE:factory.getAddressTwo());
			factoryMaster.setZIP(StringUtils.isBlank(factory.getZip())?SINGLE_SPACE:factory.getZip());
			factoryMaster.setCONTCEMAILADR(StringUtils.isBlank(factory.getContactEmail())?SINGLE_SPACE:factory.getContactEmail());
			factoryMaster.setLSTUID(userInfo.getUserId());
			factoryMasters.add(factoryMaster);
		}
		request.getFCTRY().addAll(factoryMasters);
		// If any requests were processed, send the request to the mainframe.
		UpdateReplyMessage reply;
		try {
			reply = this.getPort().updateCodeTableDetails(request);
			StringBuilder errorMessage = new StringBuilder("");
			if(!reply.getUpdateCodeTableReply().isEmpty()){
				for(UpdateReplyMessage.UpdateCodeTableReply updateCodeTableReply : reply.getUpdateCodeTableReply()){
					if(!this.isSuccess(updateCodeTableReply.getReturnMsg())){
						errorMessage.append(updateCodeTableReply.getReturnMsg());
					}
				}
				if(!errorMessage.toString().equals(StringUtils.EMPTY)){
					throw new SoapException(errorMessage.toString());
				}
			}
		} catch (Exception e) {
			throw new SoapException(e.getMessage(), e.getCause());
		}
	}
	/**
	 * Update the list of choice type basing in action (add new, edit or delete).
	 *
	 * @param choiceTypes the list of choice types requested to delete/insert/update.
	 * @param action action update, deleted or insert.
	 */
	public void updateChoiceType(List<ChoiceType> choiceTypes, String action){
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(this.getWorkId().toString());
		List<ChoiceTyp> choiceTypList = new ArrayList<>();
		for(ChoiceType choiceType : choiceTypes){
			ChoiceTyp choiceTyp = new ChoiceTyp();
			//set action handle (Add new/Edit/Delete)
			choiceTyp.setACTIONCD(action);
			//set choice type, if choice is blank (for case add new) => set default code is 0
			if(StringUtils.isNotBlank(choiceType.getChoiceTypeCode())){
				choiceTyp.setCHCTYPCD(choiceType.getChoiceTypeCode());
			}else{
				choiceTyp.setCHCTYPCD(CodeTableManagementServiceClient.DEFAULT_CHOICE_TYPE_CODE);
			}
			//set choice type abbreviation
			choiceTyp.setCHCTYPABB(choiceType.getAbbreviation());
			//set choice type description
			choiceTyp.setCHCTYPDES(choiceType.getDescription());
			//set parent choice type, if do not exist parent choice type, set parent choice type code with default space
			if(choiceType.getParentChoiceType() != null && StringUtils.isNotBlank(choiceType.getParentChoiceType().getChoiceTypeCode())){
				choiceTyp.setPARNTCHCTYPCD(choiceType.getParentChoiceType().getChoiceTypeCode());
			}else{
				choiceTyp.setPARNTCHCTYPCD(CodeTableManagementServiceClient.DEFAULT_PARENT_CHOICE_TYPE_CODE);
			}
			choiceTypList.add(choiceTyp);
		}
		request.getChoiceTyp().addAll(choiceTypList);
		// If any requests were processed, send the request to the mainframe.
		UpdateReplyMessage reply;
		try {
			reply = this.getPort().updateCodeTableDetails(request);
			StringBuilder errorMessage = new StringBuilder("");
			if(!reply.getUpdateCodeTableReply().isEmpty()){
				for(UpdateReplyMessage.UpdateCodeTableReply updateCodeTableReply : reply.getUpdateCodeTableReply()){
					if(!this.isSuccess(updateCodeTableReply.getReturnMsg())){
						errorMessage.append(updateCodeTableReply.getReturnMsg());
					}
				}
				if(!errorMessage.toString().equals(StringUtils.EMPTY)){
					throw new SoapException(errorMessage.toString());
				}
			}
		} catch (Exception e) {
			throw new SoapException(e.getMessage(), e.getCause());
		}
	}
	/**
	 * Update the list of choice option basing in action (add new, edit or delete).
	 * @param choiceOptions the list of choice options requested to delete/insert/update.
	 * @param action action update, deleted or insert.
	 */
	public void updateChoiceOptions(List<ChoiceOption> choiceOptions, String action){
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(this.getWorkId().toString());
		List<ChoiceOpt> choiceOptList = new ArrayList<>();
		for(ChoiceOption choiceOption : choiceOptions){
			ChoiceOpt choiceOpt = new ChoiceOpt();
			//set action handle (Add new/Edit/Delete)
			choiceOpt.setACTIONCD(action);
			//set choice option key. for case add new, choice option code set default is "0"
			if(choiceOption.getKey() != null && StringUtils.isNotBlank(choiceOption.getKey().getChoiceOptionCode())){
				choiceOpt.setCHCOPTCD(choiceOption.getKey().getChoiceOptionCode());
			} else {
				choiceOpt.setCHCOPTCD(DEFAULT_CHOICE_OPTION_CODE);
			}
			//set choice option type code
			choiceOpt.setCHCTYPCD(StringUtils.trim(choiceOption.getChoiceType().getChoiceTypeCode()));
			//set product choice text
			choiceOpt.setPRODCHCTXT(StringUtils.trim(choiceOption.getProductChoiceText()));
			//add convert to list
			choiceOptList.add(choiceOpt);
		}
		request.getChoiceOpt().addAll(choiceOptList);
		// If any requests were processed, send the request to the mainframe.
		UpdateReplyMessage reply;
		try {
			reply = this.getPort().updateCodeTableDetails(request);
			StringBuilder errorMessage = new StringBuilder("");
			if(!reply.getUpdateCodeTableReply().isEmpty()){
				for(UpdateReplyMessage.UpdateCodeTableReply updateCodeTableReply : reply.getUpdateCodeTableReply()){
					if(!this.isSuccess(updateCodeTableReply.getReturnMsg())){
						errorMessage.append(updateCodeTableReply.getReturnMsg());
					}
				}
				if(!errorMessage.toString().equals(StringUtils.EMPTY)){
					throw new SoapException(errorMessage.toString());
				}
			}
		} catch (Exception e) {
			throw new SoapException(e.getMessage(), e.getCause());
		}
	}
	/**
	 * Looking message return from webservice. If contain Success text => return handle successfully.
	 *
	 * @param returnMessage the return message needs to check.
	 * @return true if it is success or not.
	 */
	private boolean isSuccess(String returnMessage){
		return (returnMessage != null && returnMessage.indexOf(WEBSERVICE_RESPONSE_MESSAGE_SUCCESS) >= 0);
	}
	/**
	 * Update the list of country codes basing in action (add new, edit or delete).
	 *
	 * @param countries the list of countries requested to delete/insert/update.
	 * @param action the action update, deleted or insert.
	 */
	public void updateCountryCode(List<Country> countries, String action){
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(this.getWorkId().toString());
		List<CountryCode> countryCodes = new ArrayList<>();
		for(Country country : countries) {
			CountryCode countryCode = new CountryCode();
			countryCode.setActionCd(action);
			countryCode.setCNTRYID(String.valueOf(country.getCountryId()));
			countryCode.setCNTRYNM(country.getCountryName());
			countryCode.setCNTRYABB(country.getCountryAbbreviation());
			countryCode.setCNTRYISOA3COD(country.getCountIsoA3Cod());
			countryCode.setCNTRYISON3CD(String.valueOf(country.getCountIsoN3Cd()));
			countryCodes.add(countryCode);
		}
		request.getCNTRYCD().addAll(countryCodes);
		// If any requests were processed, send the request to the mainframe.
		UpdateReplyMessage reply;
		try {
			reply = this.getPort().updateCodeTableDetails(request);
			StringBuilder errorMessage = new StringBuilder("");
			if(!reply.getUpdateCodeTableReply().isEmpty()){
				for(UpdateReplyMessage.UpdateCodeTableReply updateCodeTableReply : reply.getUpdateCodeTableReply()){
					if(!isSuccess(updateCodeTableReply.getReturnMsg())){
						errorMessage.append(updateCodeTableReply.getReturnMsg());
					}
				}
				if(!errorMessage.toString().equals(StringUtils.EMPTY)){
					throw new SoapException(errorMessage.toString());
				}
			}
		} catch (Exception e) {
			throw new SoapException(e.getMessage(), e.getCause());
		}
	}
	/**
	 * Update the list of state warnings base on action (add new, edit or delete).
	 *
	 * @param stateWarningList the list of state warnings.
	 * @param action           the action needs to update.
	 */
	public void updateStateWarnings(List<ProductStateWarning> stateWarningList, String action) {
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(this.getWorkId().toString());
		List<STPRODWARN> stateWarnings = new ArrayList<>();
		for (ProductStateWarning stateWarning : stateWarningList) {
			STPRODWARN stateProductWarning = new STPRODWARN();
			stateProductWarning.setACTIONCD(action);
			stateProductWarning.setSTCD(stateWarning.getKey().getStateCode());
			stateProductWarning.setSTPRODWARNCD(stateWarning.getKey().getWarningCode());
			stateProductWarning.setSTPRODWARNDES(stateWarning.getDescription());
			stateProductWarning.setSTPRODWARNABB(StringUtils.trim(stateWarning.getAbbreviation()));
			stateWarnings.add(stateProductWarning);
		}
		request.getSTPRODWARN().addAll(stateWarnings);
		try {
			UpdateReplyMessage reply = this.getPort().updateCodeTableDetails(request);
			StringBuilder errorMessage = new StringBuilder();
			if (!reply.getUpdateCodeTableReply().isEmpty()) {
				for (UpdateReplyMessage.UpdateCodeTableReply codeTableReply : reply.getUpdateCodeTableReply()) {
					if (!this.isSuccess(codeTableReply.getReturnMsg())) {
						errorMessage.append(codeTableReply.getReturnMsg());
					}
				}
				if (!errorMessage.toString().isEmpty()) {
					throw new SoapException(errorMessage.toString());
				}
			}
		} catch (Fault fault) {
			throw new SoapException(fault.getMessage(), fault.getCause());
		}
	}
	/**
	 * Update the list of scoringOrganizations basing in action (add new, edit or delete).
	 *
	 * @param scoringOrganizations the list of scoringOrganizations.
	 * @param action the  action update, deleted or insert.
	 */
	public void updateWineScoringOrganizations(List<ScoringOrganization> scoringOrganizations, String action){
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(this.getWorkId().toString());
		List<SCORINGORG> scoringOrgs = new ArrayList<>();
		for(ScoringOrganization scoringOrganization : scoringOrganizations) {
			SCORINGORG scoringOrg = new SCORINGORG();
			scoringOrg.setACTIONCD(action);
			scoringOrg.setSCORINGORGID(scoringOrganization.getScoringOrganizationId().toString());
			scoringOrg.setSCORINGORGNM(scoringOrganization.getScoringOrganizationName());
			scoringOrg.setSCORINGORGDES(scoringOrganization.getScoringOrganizationDescription());
			scoringOrgs.add(scoringOrg);
		}
		UpdateCodeTableRequest.WINECODETABLES wineCodeTables = new UpdateCodeTableRequest.WINECODETABLES();
		wineCodeTables.getSCORINGORG().addAll(scoringOrgs);
		request.setWINECODETABLES(wineCodeTables);
		// If any requests were processed, send the request to the mainframe.
		UpdateReplyMessage reply;
		try {
			reply = this.getPort().updateCodeTableDetails(request);
			StringBuilder errorMessage = new StringBuilder("");
			if(!reply.getUpdateCodeTableReply().isEmpty()){
				for(UpdateReplyMessage.UpdateCodeTableReply updateCodeTableReply : reply.getUpdateCodeTableReply()){
					if (!this.isSuccess(updateCodeTableReply.getReturnMsg())) {
						errorMessage.append(updateCodeTableReply.getReturnMsg());
					}
				}
				if(!errorMessage.toString().equals(StringUtils.EMPTY)){
					throw new SoapException(errorMessage.toString());
				}
			}
		} catch (Exception e) {
			throw new SoapException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Create new or update or delete list Product Category
	 *
	 * @param listOfProductCategories want to create/update/delete
	 * @return The result of update / delete / create returns from the webservice
	 */
	public void updateProductCategories(List<ProductCategory> listOfProductCategories, String action){
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		// set authentication and tracking number for request.
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(CodeTableManagementServiceClient.DEFAULT_PRODUCT_CATEGORY_CODE);
		//List of PRODCAT that send to WebService
		List<PRODCAT> productCategories = new ArrayList<>();
		for(ProductCategory productCategory:listOfProductCategories){
			PRODCAT proCat = new PRODCAT();
			//set action handle (Add new/Edit/Delete)
			proCat.setACTIONCD(action);
			//set product category id, if id is blank (for case add new) => set default code is 0
			if (null != productCategory.getProductCategoryId() && StringUtils.isNotBlank(productCategory.getProductCategoryId().toString())) {
				proCat.setPRODCATID(productCategory.getProductCategoryId().toString());
			} else {
				proCat.setPRODCATID(CodeTableManagementServiceClient.DEFAULT_PRODUCT_CATEGORY_CODE);
			}
			//set product category abbreviation, if abbreviation is blank => set abbreviation is single space
			if (StringUtils.isBlank(productCategory.getProductCategoryAbb())){
				proCat.setPRODCATABB(CodeTableManagementServiceClient.SINGLE_SPACE);
			} else {
				proCat.setPRODCATABB(productCategory.getProductCategoryAbb());
			}
			//set product category name
			proCat.setPRODCATNM(productCategory.getProductCategoryName());
			//set market consumer code
			proCat.setMKTCONSMEVNTCD(productCategory.getMarketConsumerEventCode().trim());
			//set product category role code
			proCat.setPRODCATROLECD(productCategory.getProductCategoryRoleCode().trim());

			productCategories.add(proCat);
		}
		request.getPRODCAT().addAll(productCategories);
		// If any requests were processed, send the request to the mainframe.
		UpdateReplyMessage reply;
		try{
			reply = this.getPort().updateCodeTableDetails(request);
			StringBuilder errorMessage = new StringBuilder(StringUtils.EMPTY);
			if(!reply.getUpdateCodeTableReply().isEmpty()){
				for(UpdateReplyMessage.UpdateCodeTableReply updateCodeTableReply : reply.getUpdateCodeTableReply()){
					if(!this.isSuccess(updateCodeTableReply.getReturnMsg())){
						errorMessage.append(updateCodeTableReply.getReturnMsg());
					}
				}
				if(!errorMessage.toString().equals(StringUtils.EMPTY)){
					throw new SoapException(errorMessage.toString());
				}
			}
		} catch(Exception e) {
			throw new SoapException(e.getMessage(), e.getCause());
		}
	}
	/**
	 * update Customer Hierarchy Assignment.
	 * @param genericEntityRelationships list GenericEntityRelationship
	 * @param userId
	 *       String
	 * @param productId
	 *    Long
	 * @author vn55306
	 */
	public void updateCustomerHierarchyAssignment(List<GenericEntityRelationship> genericEntityRelationships, Long productId,String userId){
		Long entityIdProduct=0L;
		GenericEntity genaricEntity = this.genericEntityRepository.findTop1ByDisplayNumberAndType(productId,GenericEntity.EntyType.PROD.getName());
		if(genaricEntity == null){
			this.createEntyForProd(productId,userId);
			genaricEntity = this.genericEntityRepository.findTop1ByDisplayNumberAndType(productId,GenericEntity.EntyType.PROD.getName());
			entityIdProduct = genaricEntity.getId();
		} else {
			entityIdProduct = genaricEntity.getId();
		}
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(DEFAULT_TRACKING_NUMBER);
		List<ENTYRLSHP> dataAdd = new ArrayList<ENTYRLSHP>();
		List<ENTYRLSHP> dataDelete = new ArrayList<ENTYRLSHP>();
		ENTYRLSHP primaryEntityChange =null;
		ENTYRLSHP primaryEntityDelete =null;
		for(GenericEntityRelationship genericEntityRelationship:genericEntityRelationships){
			if (genericEntityRelationship.getAction().equalsIgnoreCase(ACTION_CODE.ACTION_CD_ALTERNATE.getValue())) {
				genericEntityRelationship.setAction(ACTION_CODE.ACTION_CD_UPDATE.getValue());
				primaryEntityChange = this.setDataToENTYRLSHP(genericEntityRelationship,entityIdProduct,userId);
			} else if(genericEntityRelationship.getAction().equalsIgnoreCase(ACTION_CODE.ACTION_CD_DELETE.getValue())){
				if(genericEntityRelationship.getDefaultParentValue().equals(PRIMARY_PATH)){
					primaryEntityDelete = this.setDataToENTYRLSHP(genericEntityRelationship, entityIdProduct, userId);
				} else {
					dataDelete.add(this.setDataToENTYRLSHP(genericEntityRelationship, entityIdProduct, userId));
				}
			}else if(genericEntityRelationship.getAction().equalsIgnoreCase(ACTION_CODE.ACTION_CD_ADD.getValue())){
				dataAdd.add(this.setDataToENTYRLSHP(genericEntityRelationship,entityIdProduct,userId));
			} else {
				genericEntityRelationship.setAction(ACTION_CODE.ACTION_CD_UPDATE.getValue());
				dataAdd.add(this.setDataToENTYRLSHP(genericEntityRelationship,entityIdProduct,userId));
			}
		}
		if(primaryEntityChange!=null){
			request.getENTYRLSHP().add(primaryEntityChange);
		}
		if(primaryEntityDelete!=null){
			request.getENTYRLSHP().add(primaryEntityDelete);
		}
		if(!dataDelete.isEmpty()){
			request.getENTYRLSHP().addAll(dataDelete);
		}
		if(!dataAdd.isEmpty()){
			request.getENTYRLSHP().addAll(removeDuplicateRelationship(dataAdd));
		}
		this.processReplyFromWebservice(request);

	}
	/**
	 * remove the duplicate item relationship enty.
	 * @param data
	 *            List<ENTYRLSHP>
	 * @return boolean
	 */
	private List<ENTYRLSHP> removeDuplicateRelationship(List<ENTYRLSHP> data) {
		Map<String,ENTYRLSHP> maps= new HashMap<>();
		List<ENTYRLSHP> dataResult = new ArrayList<>();
		for (ENTYRLSHP entyRLSHP : data) {
			if (!maps.containsKey(entyRLSHP.getCHILDENTYID().concat("_").concat(entyRLSHP.getPARNTENTYID()))) {
				maps.put(entyRLSHP.getCHILDENTYID().concat("_").concat(entyRLSHP.getPARNTENTYID()),entyRLSHP);
			}
		}
		return new ArrayList<ENTYRLSHP>(maps.values());
	}
	/**
	 * update Product Group.
	 * @param customerProductGroup
	 *       CustomerProductGroup
	 * @author vn70633
	 */
	public void updateProductGroup(CustomerProductGroup customerProductGroup, String action){
		CUSTPRODGRP custProductGroup = new CUSTPRODGRP();
		custProductGroup.setACTIONCD(action);
		custProductGroup.setCUSTPRODGRPID(String.valueOf(customerProductGroup.getCustProductGroupId()));
		custProductGroup.setCUSTPRODGRPNM(customerProductGroup.getCustProductGroupName());
		custProductGroup.setCUSTPRODGRPLONG(customerProductGroup.getCustProductGroupDescriptionLong());
		custProductGroup.setCUSTPRODGRPDES(customerProductGroup.getCustProductGroupDescription());
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(DEFAULT_TRACKING_NUMBER);
		request.getCUSTPRODGRP().add(custProductGroup);
		this.processReplyFromWebservice(request);
	}
	/**
	 * create Enty For Prod.
	 * @param prodId
	 *            the prod id
	 * @param userId
	 *            the u id
	 * @return String
	 * @author vn70633
	 */
	public void createEntyForProdGroup(Long prodId, String userId) {
		GenericEntity genericEntity = this.genericEntityRepository.findTop1ByDisplayNumberAndType(prodId,GenericEntity.EntyType.PGRP.getName());
		if (genericEntity ==null) {
			UpdateCodeTableRequest req = new UpdateCodeTableRequest();
			req.setAuthentication(this.getAuthentication());
			req.setTrackingNbr(DEFAULT_TRACKING_NUMBER);
			req.getENTY().add(this.setDataToENTYForSavingProductGroup(prodId,userId));
			this.processReplyFromWebservice(req);
		}
	}
	/**
	 * update Customer Hierarchy Assignment.
	 * @param genericEntityRelationships list GenericEntityRelationship
	 * @param userId
	 *       String
	 * @param productId
	 *    Long
	 * @author vn55306
	 */
	public void updateCustomerHierarchyAssignmentForProductGroup(List<GenericEntityRelationship> genericEntityRelationships, Long productId,String userId){
		Long entityIdProduct=0L;
		GenericEntity genaricEntity = this.genericEntityRepository.findTop1ByDisplayNumberAndType(productId,GenericEntity.EntyType.PGRP.getName());
		if(genaricEntity == null){
			this.createEntyForProdGroup(productId,userId);
			genaricEntity = this.genericEntityRepository.findTop1ByDisplayNumberAndType(productId,GenericEntity.EntyType.PGRP.getName());
			entityIdProduct = genaricEntity.getId();
		} else {
			entityIdProduct = genaricEntity.getId();
		}
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(DEFAULT_TRACKING_NUMBER);
		List<ENTYRLSHP> dataAdd = new ArrayList<ENTYRLSHP>();
		List<ENTYRLSHP> dataDelete = new ArrayList<ENTYRLSHP>();
		ENTYRLSHP primaryEntityChange =null;
		ENTYRLSHP primaryEntityDelete =null;
		for(GenericEntityRelationship genericEntityRelationship:genericEntityRelationships){
			if (genericEntityRelationship.getAction().equalsIgnoreCase(ACTION_CODE.ACTION_CD_ALTERNATE.getValue())) {
				genericEntityRelationship.setAction(ACTION_CODE.ACTION_CD_UPDATE.getValue());
				primaryEntityChange = this.setDataToENTYRLSHP(genericEntityRelationship,entityIdProduct,userId);
			} else if(genericEntityRelationship.getAction().equalsIgnoreCase(ACTION_CODE.ACTION_CD_DELETE.getValue())){
				if(genericEntityRelationship.getDefaultParentValue().equals(PRIMARY_PATH)){
					primaryEntityDelete = this.setDataToENTYRLSHP(genericEntityRelationship, entityIdProduct, userId);
				} else {
					dataDelete.add(this.setDataToENTYRLSHP(genericEntityRelationship, entityIdProduct, userId));
				}
			}else if(genericEntityRelationship.getAction().equalsIgnoreCase(ACTION_CODE.ACTION_CD_ADD.getValue())){
				dataAdd.add(this.setDataToENTYRLSHP(genericEntityRelationship,entityIdProduct,userId));
			} else {
				genericEntityRelationship.setAction(ACTION_CODE.ACTION_CD_UPDATE.getValue());
				dataAdd.add(this.setDataToENTYRLSHP(genericEntityRelationship,entityIdProduct,userId));
			}
		}
		if(primaryEntityChange!=null){
			request.getENTYRLSHP().add(primaryEntityChange);
		}

		if(!dataDelete.isEmpty()){
			request.getENTYRLSHP().addAll(dataDelete);
		}
		if(primaryEntityDelete!=null){
			request.getENTYRLSHP().add(primaryEntityDelete);
		}
		if(!dataAdd.isEmpty()){
			request.getENTYRLSHP().addAll(removeDuplicateRelationship(dataAdd));
		}
		this.processReplyFromWebservice(request);

	}



	/**
	 * set Data To ENTYRLSHP.
	 * @param genericEntityRelationship
	 *            GenericEntityRelationship
	 * @param entityIdProduct
	 *         Long
	 *  @param userId
	 *         String
	 * @return ENTYRLSHP
	 * @author vn565306
	 */
	public static ENTYRLSHP setDataToENTYRLSHP(GenericEntityRelationship genericEntityRelationship,Long entityIdProduct, String userId) {
		ENTYRLSHP entyrlshp = new ENTYRLSHP();
		entyrlshp.setLSTUPDTUID(userId);
		entyrlshp.setPARNTENTYID(String.valueOf(genericEntityRelationship.getKey().getChildEntityId()));
		entyrlshp.setHIERCNTXTCD(genericEntityRelationship.getKey().getHierarchyContext());
		entyrlshp.setCHILDENTYID(String.valueOf(entityIdProduct));
		entyrlshp.setACTIONCD(genericEntityRelationship.getAction());
		if (genericEntityRelationship.getAction().equals(ACTION_CODE.ACTION_CD_ADD.getValue())) {
			entyrlshp.setCRE8UID(userId);
		}
		entyrlshp.setDFLTPARNTSW(genericEntityRelationship.getDefaultParentValue().equals(PRIMARY_PATH)?SWITCH_Y:SWITCH_N);
		return entyrlshp;
	}
	/**
	 * create Enty For Prod.
	 * @param prodId
	 *            the prod id
	 * @param userId
	 *            the u id
	 * @return String
	 * @author loc.ngo
	 */
	public void createEntyForProd(Long prodId, String userId) {
		GenericEntity genericEntity = this.genericEntityRepository.findTop1ByDisplayNumberAndType(prodId,GenericEntity.EntyType.PROD.getName());
		if (genericEntity ==null) {
			UpdateCodeTableRequest req = new UpdateCodeTableRequest();
			req.setAuthentication(this.getAuthentication());
			req.setTrackingNbr(DEFAULT_TRACKING_NUMBER);
			req.getENTY().add(this.setDataToENTY(prodId,userId));
			this.processReplyFromWebservice(req);
		}
	}
	/**
	 * set Data To ENTY.
	 * @param productId
	 *            Integer
	 * @param userId
	 *            String
	 * @return ENTY
	 * @author vn55306
	 */
	public static ENTY setDataToENTY(Long productId, String userId) {
		ENTY objRt = new ENTY();
		objRt.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());
		objRt.setWorkId(DEFAULT_TRACKING_NUMBER);
		objRt.setENTYID(StringUtils.EMPTY);
		objRt.setENTYTYPCD(GenericEntity.EntyType.PROD.getName());
		objRt.setENTYABB(GenericEntity.EntyType.PROD.getName());
		objRt.setENTYDSPLYNBR(productId.toString());
		objRt.setCRE8UID(userId);
		objRt.setLSTUPDTUID(userId);
		return objRt;
	}

	/**
	 * set Data To ENTY.
	 * @param productId
	 *            Integer
	 * @param userId
	 *            String
	 * @return ENTY
	 * @author vn70633
	 */
	public static ENTY setDataToENTYForSavingProductGroup(Long productId, String userId) {
		ENTY objRt = new ENTY();
		objRt.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());
		objRt.setWorkId(DEFAULT_TRACKING_NUMBER);
		objRt.setENTYID(StringUtils.EMPTY);
		objRt.setENTYTYPCD(GenericEntity.EntyType.PGRP.getName());
		objRt.setENTYABB(GenericEntity.EntyType.PGRP.getName());
		objRt.setENTYDSPLYNBR(productId.toString());
		objRt.setCRE8UID(userId);
		objRt.setLSTUPDTUID(userId);
		return objRt;
	}
	/**
	 * Updates a distributesFilter
	 * @param filter the filter to be updated
	 * @param userId the user requesting the change
	 */
	public void updateDistributionFilter(DistributionFilter filter, String userId){
		CodeTableManagementServiceClient.logger.info(String.format(DISTRIBUTION_FILTER_UPDATE_BEGIN, userId, filter.getKey().getKeyId()));
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		String workId = this.getWorkId().toString();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(workId);
		DSTRBFLTR webserviceDistributionFilter = new DSTRBFLTR();
		webserviceDistributionFilter.setWorkId(workId);
		webserviceDistributionFilter.setATTRVALNBR(String.valueOf(filter.getKey().getKeyId()));
		if(filter.getKey().getKeyType().equals("DEL")){
			webserviceDistributionFilter.setACTIONCD(ACTION_CODE.ACTION_CD_DELETE.getValue());
		} else {
			webserviceDistributionFilter.setACTIONCD(ACTION_CODE.ACTION_CD_UPDATE.getValue());
		}
		webserviceDistributionFilter.setDSTRBKEYTYPCD("PROD");
		webserviceDistributionFilter.setTRGSYSTEMID(String.valueOf(filter.getKey().getTargetSystemId()));
		webserviceDistributionFilter.setLSTUPDTUID(userId);
		request.getDSTRBFLTR().add(webserviceDistributionFilter);
		this.processReplyFromWebservice(request);
		CodeTableManagementServiceClient.logger.info(String.format(DISTRIBUTION_FILTER_UPDATE_COMPLETE, userId, filter.getKey().getKeyId()));
	}

	/**
	 * Writes an entity to the entity table.
	 *
	 * @param genericEntity The entity to write.
	 * @return The ID of the entity that was written.
	 */
	public long writeEntity(GenericEntity genericEntity) throws CheckedSoapException{

		ENTY objRt = new ENTY();

		objRt.setWorkId(this.getWorkId().toString());

		long id;

		if (genericEntity.getAction() != GenericEntity.NOOP) {
			// If it doesn't have an ID, then this entity needs to be created.
			if (genericEntity.getId() == null) {
				objRt.setENTYID(StringUtils.EMPTY);
				objRt.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());
				objRt.setCRE8UID(genericEntity.getCreateUserId());

				objRt.setENTYTYPCD(genericEntity.getType());
				objRt.setENTYABB(genericEntity.getAbbreviation());
				objRt.setENTYDSPLYNBR(genericEntity.getDisplayNumber().toString());
				objRt.setENTYDSPLYTXT(genericEntity.getDisplayText());
				objRt.setLSTUPDTUID(genericEntity.getLastUpdateUserId());

				UpdateCodeTableRequest request = new UpdateCodeTableRequest();
				request.setAuthentication(this.getAuthentication());
				request.setTrackingNbr(this.getWorkId().toString());
				request.getENTY().add(objRt);

				id = this.processKeyReturningRequest(request);
			} else {
				id = genericEntity.getId();
			}
		} else {
			id = genericEntity.getId();
		}

		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(this.getWorkId().toString());

		String workId = this.getWorkId().toString();

		boolean requestDoesSomething = false;

		for (GenericEntityDescription genericEntityDescription : genericEntity.getDescriptions()) {
			if (genericEntityDescription.getAction() != GenericEntityDescription.NOOP) {
				requestDoesSomething = true;
				ENTYDES entydes = new ENTYDES();
				entydes.setENTYID(Long.toString(id));
				if (genericEntityDescription.getAction() == GenericEntityDescription.INSERT) {
					entydes.setCRE8TS(LocalDate.now().format(dateTimeFormatter));
					entydes.setCRE8UID(genericEntity.getCreateUserId());
					entydes.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());
				} else {
					entydes.setACTIONCD(ACTION_CODE.ACTION_CD_UPDATE.getValue());
				}
				entydes.setHIERCNTXTCD(genericEntityDescription.getKey().getHierarchyContext());
				entydes.setLSTUPDTTS(LocalDateTime.now().format(dateTimeFormatter));
				entydes.setLSTUPDTUID(genericEntity.getLastUpdateUserId());
				entydes.setWorkId(workId);

				if (genericEntityDescription.getLongDescription() != null) {
					entydes.setENTYLONGDESTEXT(genericEntityDescription.getLongDescription());
					entydes.setENTYSHRTDESTEXT(Integer.toString(genericEntityDescription.getLongDescription().length()));
				}

				if (genericEntityDescription.getShortDescription() != null) {
					entydes.setENTYSHRTDESTEXT(genericEntityDescription.getShortDescription());
					entydes.setENTYSHRTDESLEN(Integer.toString(genericEntityDescription.getShortDescription().length()));
				}
				request.getENTYDES().add(entydes);
			}
		}

		if (requestDoesSomething) {
			this.processRequest(request);
		}
		return id;
	}

	/**
	 * Writes a list of entity relationships to the DB.
	 *
	 * @param genericEntityRelationships The list of entity relationships to write.
	 */
	public void writeEntityRelationships(List<? extends GenericEntityRelationship> genericEntityRelationships) throws CheckedSoapException {

		if (genericEntityRelationships.isEmpty()) {
			return;
		}

		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId);

		for (GenericEntityRelationship er : genericEntityRelationships) {


				ENTYRLSHP entyrlshp = new ENTYRLSHP();
				entyrlshp.setWorkId(workId);
				if (er.getAction().equals(ACTION_CODE.ACTION_CD_ADD.getValue())) {
					entyrlshp.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());
					entyrlshp.setCRE8UID(er.getCreateUserId());
				} else {
					entyrlshp.setACTIONCD(ACTION_CODE.ACTION_CD_UPDATE.getValue());
				}
				entyrlshp.setPARNTENTYID(er.getKey().getParentEntityId().toString());
				entyrlshp.setCHILDENTYID(er.getKey().getChildEntityId().toString());
				entyrlshp.setHIERCNTXTCD(er.getKey().getHierarchyContext());

				entyrlshp.setDFLTPARNTSW(er.getDefaultParent() ? "Y" : "N");
				entyrlshp.setDSPLYSW(er.getDisplay() ? "Y" : "N");
				entyrlshp.setACTVSW(er.getActive() ? "Y" : "N");
				entyrlshp.setSEQNBR(er.getSequence().toString());

				entyrlshp.setLSTUPDTUID(er.getLastUpdateUserId());
				if (er.getEffectiveDate() != null) {
					entyrlshp.setEFFDT(er.getEffectiveDate().format(dateTimeFormatter));
				}
				if (er.getExpirationDate() != null) {
					entyrlshp.setEXPRNDT(er.getExpirationDate().format(dateTimeFormatter));
				}
				request.getENTYRLSHP().add(entyrlshp);

		}

		this.processRequest(request);
	}

	/**
	 * Saves a list of eCommerceUserGroupAttribtues to the DB.
	 *
	 * @param ecommerUserGroupAttributes The list of eCommerceUserGroupAttribtues to add.
	 * @throws CheckedSoapException
	 */
	public void writeEcommerUserGroupAttributes(List<? extends EcommerUserGroupAttribute> ecommerUserGroupAttributes)
			throws CheckedSoapException {

		if (ecommerUserGroupAttributes.isEmpty()) {
			return;
		}

		String workId = Integer.toString(this.getWorkId());

		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(workId);

		for (EcommerUserGroupAttribute ecommerUserGroupAttribute : ecommerUserGroupAttributes) {
			ECOMUSRGRPATTR ecomusrgrpattr = new ECOMUSRGRPATTR();
			ecomusrgrpattr.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());
			ecomusrgrpattr.setATTRID(Long.toString(ecommerUserGroupAttribute.getKey().getAttributeId()));
			ecomusrgrpattr.setSEQNBR(Long.toString(ecommerUserGroupAttribute.getSequence()));
			ecomusrgrpattr.setUSRINRFCGRPCD(ecommerUserGroupAttribute.getKey().getUsrInrfcGrpCd());
			ecomusrgrpattr.setWorkId(workId);
			request.getECOMUSRGRPATTR().add(ecomusrgrpattr);
		}

		this.processRequest(request);
	}

	/**
	 * Update Product Group Type.
	 *
	 * @param request request to webservice.
	 */
	public String updateProductGroupType(UpdateCodeTableRequest request) {
		String productGroupTypeCode = StringUtils.EMPTY;
		UpdateReplyMessage reply;
		try {
			reply = this.getPort().updateCodeTableDetails(request);
			this.handleErrorMessage(reply);
		} catch (Exception e) {
			throw new SoapException(e.getMessage(), e.getCause());
		}
		if(reply.getUpdateCodeTableReply().get(0) != null && reply.getUpdateCodeTableReply().get(0).getKeyData() !=null){
			productGroupTypeCode = reply.getUpdateCodeTableReply().get(0).getKeyData().trim();
		}
		return productGroupTypeCode;
	}

	/**
	 * Handle error when call webservice.
	 *
	 * @param reply the UpdateReplyMessage from webservice.
	 */
	private void handleErrorMessage(UpdateReplyMessage reply) {
		StringBuilder errorMessage = new StringBuilder(StringUtils.EMPTY);
		if (!reply.getUpdateCodeTableReply().isEmpty()) {
			for (UpdateReplyMessage.UpdateCodeTableReply updateCodeTableReply : reply.getUpdateCodeTableReply()) {
				if (!this.isSuccess(updateCodeTableReply.getReturnMsg())) {
					errorMessage.append(updateCodeTableReply.getReturnMsg());
				}
			}
			if (!errorMessage.toString().equals(StringUtils.EMPTY)) {
				throw new SoapException(errorMessage.toString());
			}
		}
	}

	/**
	 * Update Tobacco Product Type.
	 *
	 * @param tobaccoProductTypes list of Tobacco Product Type to update tax rate.
	 */
	public void updateTobaccoProductType(List<TobaccoProductType> tobaccoProductTypes){
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(CodeTableManagementServiceClient.DEFAULT_TRACKING_NUMBER);
		//List of TbcoProdType that send to WebService,
		List<TbcoProdType> tbcoProdTypes = new ArrayList<>();
		for(TobaccoProductType tobaccoProductType:tobaccoProductTypes){
			TbcoProdType tbcoProdType = new TbcoProdType();
			//set tobacco product type code and new tax rate.
			if (null != tobaccoProductType) {
				if (null != tobaccoProductType.getTobaccoProductTypeCode()
						&& StringUtils.isNotBlank(tobaccoProductType.getTobaccoProductTypeCode())) {
					tbcoProdType.setTBCOPRODTYPCD(tobaccoProductType.getTobaccoProductTypeCode());
				}
				tbcoProdType.setTBCOTAXRATEPCT(String.valueOf(tobaccoProductType.getTaxRate()));
			}
			tbcoProdTypes.add(tbcoProdType);
		}
		request.getTbcoProdType().addAll(tbcoProdTypes);
		UpdateReplyMessage reply;
		//Send request and handle response returned from webservice.
		try {
			reply = this.getPort().updateCodeTableDetails(request);
			this.handleErrorMessage(reply);
		} catch (Exception e) {
			throw new SoapException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * This method updates a generic entity relationship's attributes
	 * @param entityRelationship
	 * @param user
	 */
	public void updateCurrentLevelAttributes(GenericEntityRelationship entityRelationship, String user){
		CodeTableManagementServiceClient.logger.info(String.format(CodeTableManagementServiceClient.CURRENT_LEVEL_ATTRIBURE_UPDATE_BEGIN,
				user, entityRelationship.getKey().getHierarchyContext(), entityRelationship.getKey().getParentEntityId(),
				entityRelationship.getKey().getChildEntityId()));
		String workId = this.getWorkId().toString();

		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(workId);
		ENTYRLSHP webserviceEntityRelationship = new ENTYRLSHP();
		if(entityRelationship.getGenericChildEntity() != null) {
			ENTY webserviceEntity = new ENTY();
			webserviceEntity.setWorkId(user);
			webserviceEntity.setENTYID(entityRelationship.getGenericChildEntity().getId().toString());
			if(entityRelationship.getChildDescription().getShortDescription().length() > 6){
				webserviceEntity.setENTYABB(entityRelationship.getGenericChildEntity().getAbbreviation().substring(0,6));
			} else {
				webserviceEntity.setENTYABB(entityRelationship.getGenericChildEntity().getAbbreviation());
			}
			webserviceEntity.setENTYDSPLYTXT(entityRelationship.getGenericChildEntity().getDisplayText());
			webserviceEntity.setLSTUPDTUID(user);
			request.getENTY().add(webserviceEntity);
		}
		if(entityRelationship.getExpirationDate() != null || entityRelationship.getEffectiveDate() != null || entityRelationship.getActive() !=null ) {
			webserviceEntityRelationship.setWorkId(workId);
			webserviceEntityRelationship.setPARNTENTYID(entityRelationship.getKey().getParentEntityId().toString());
			webserviceEntityRelationship.setCHILDENTYID(entityRelationship.getKey().getChildEntityId().toString());
			webserviceEntityRelationship.setHIERCNTXTCD(entityRelationship.getKey().getHierarchyContext());
			webserviceEntityRelationship.setLSTUPDTUID(user);
			if (entityRelationship.getEffectiveDate() != null) {
				webserviceEntityRelationship.setEFFDT(entityRelationship.getEffectiveDate().toString());
			}
			if (entityRelationship.getExpirationDate() != null) {
				webserviceEntityRelationship.setEXPRNDT(entityRelationship.getExpirationDate().toString());
			}
			webserviceEntityRelationship.setACTVSW(convertBooleanToString(entityRelationship.getActive()));
			request.getENTYRLSHP().add(webserviceEntityRelationship);
		}
		if(entityRelationship.getDefaultParent() != null){
			webserviceEntityRelationship.setWorkId(workId);
			webserviceEntityRelationship.setPARNTENTYID(entityRelationship.getKey().getParentEntityId().toString());
			webserviceEntityRelationship.setCHILDENTYID(entityRelationship.getKey().getChildEntityId().toString());
			webserviceEntityRelationship.setHIERCNTXTCD(entityRelationship.getKey().getHierarchyContext());
			webserviceEntityRelationship.setLSTUPDTUID(user);
			webserviceEntityRelationship.setDFLTPARNTSW(convertBooleanToString(entityRelationship.getDefaultParent()));
			request.getENTYRLSHP().add(webserviceEntityRelationship);
		}
		if(entityRelationship.getChildDescription() != null) {
			ENTYDES webserviceEntityDescription = new ENTYDES();
			webserviceEntityDescription.setWorkId(workId);
			webserviceEntityDescription.setENTYID(entityRelationship.getChildDescription().getKey().getEntityId().toString());
			webserviceEntityDescription.setHIERCNTXTCD(entityRelationship.getChildDescription().getKey().getHierarchyContext().toString());
			webserviceEntityDescription.setLSTUPDTUID(user);
			webserviceEntityDescription.setENTYLONGDESTEXT(entityRelationship.getChildDescription().getLongDescription());
			webserviceEntityDescription.setENTYSHRTDESTEXT(entityRelationship.getChildDescription().getShortDescription());
			request.getENTYDES().add(webserviceEntityDescription);
		}
		if(entityRelationship.getGenericChildEntity() != null) {
			ENTY webserviceEntity = new ENTY();
			String entityAbbreviation = entityRelationship.getGenericChildEntity().getAbbreviation();
			webserviceEntity.setWorkId(workId);
			webserviceEntity.setENTYID(entityRelationship.getGenericChildEntity().getId().toString());
			if(entityAbbreviation.length()>6){
				entityAbbreviation = entityAbbreviation.substring(0, ABBREVIATION_CHARACTER_LIMIT);
			}
			webserviceEntity.setENTYABB(entityAbbreviation);
			webserviceEntity.setENTYDSPLYTXT(entityRelationship.getGenericChildEntity().getDisplayText());
			webserviceEntity.setLSTUPDTUID(user);
			request.getENTY().add(webserviceEntity);
		}
		this.processReplyFromWebservice(request);
		CodeTableManagementServiceClient.logger.info(String.format(CodeTableManagementServiceClient.CURRENT_LEVEL_ATTRIBURE_UPDATE_COMPLETE,
				user, entityRelationship.getKey().getHierarchyContext(), entityRelationship.getKey().getParentEntityId(),
				entityRelationship.getKey().getChildEntityId()));
	}

	/**
	 * This method will attempt to remove a level from a custom hierarchy
	 * @param genericEntityRelationship the relationship to be removed
	 * @param removeActionCode the action code
	 * @param userId the user attempting the change
	 *
	 */
	public void saveRemoveLevel(GenericEntityRelationship genericEntityRelationship, String removeActionCode, String userId) {

		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr("0");

		ENTYRLSHP webserviceEntityRelationship = new ENTYRLSHP();
		webserviceEntityRelationship.setACTIONCD(removeActionCode);
		if (genericEntityRelationship.getKey() != null){
			webserviceEntityRelationship.setPARNTENTYID(genericEntityRelationship.getKey().getParentEntityId().toString());
		} else if (genericEntityRelationship.getKey() == null) {
			webserviceEntityRelationship.setPARNTENTYID(genericEntityRelationship.getDefaultParentValue());
		}
		webserviceEntityRelationship.setCHILDENTYID(genericEntityRelationship.getGenericChildEntity().getId().toString());
		webserviceEntityRelationship.setHIERCNTXTCD(genericEntityRelationship.getKey().getHierarchyContext());
		webserviceEntityRelationship.setLSTUPDTUID(userId);
		webserviceEntityRelationship.setDFLTPARNTSW(convertBooleanToString(genericEntityRelationship.getDefaultParent()));

		webserviceEntityRelationship.setDELETEALLCHILDREN(DELETE_ALL_CHILDREN_CODE_SINGLE);

		request.getENTYRLSHP().add(webserviceEntityRelationship);
		this.processReplyFromWebservice(request);

	}

	/**
	 * Attempts to create a new level in a designated custom hierarchy
	 * @param entityRelationship the new level to be created
	 * @param hierarchyValues the attributes for that level
	 * @param addActionCode action code to add the record to the database
	 * @param user the user requesting the change
	 * @return child entity of the added level
	 */
	public Long addCustomHierarchy(GenericEntityRelationship entityRelationship, HierarchyContextController.HierarchyValues hierarchyValues, String addActionCode, String user){

		String workId = this.getWorkId().toString();

		UpdateCodeTableRequest entyRequest = new UpdateCodeTableRequest();
		entyRequest.setAuthentication(this.getAuthentication());
		entyRequest.setTrackingNbr(workId);

		entityRelationship.setEffectiveDate(hierarchyValues.getNewEffectiveDate());
		entityRelationship.setExpirationDate(hierarchyValues.getNewEndDate());
		entityRelationship.setActive(hierarchyValues.getNewActiveSwitch());

		ENTY webserviceEntity = new ENTY();
		webserviceEntity.setENTYDSPLYTXT(hierarchyValues.getNewDescription());
		if(hierarchyValues.getNewDescription().length() > 6) {
			webserviceEntity.setENTYABB(hierarchyValues.getNewDescription().substring(0, 6));
		} else {
			webserviceEntity.setENTYABB(hierarchyValues.getNewDescription());
		}
		webserviceEntity.setLSTUPDTUID(user);
		webserviceEntity.setACTIONCD(addActionCode);
		webserviceEntity.setENTYTYPCD("CUSTH");
		webserviceEntity.setCRE8UID(user);
		entyRequest.getENTY().add(webserviceEntity);

		Long childEntity;
		try {
			childEntity = this.processKeyReturningRequest(entyRequest);
		} catch (CheckedSoapException e) {
			throw new SoapException(e.getMessage(), e.getCause());
		}

		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(workId);

		if(entityRelationship.getExpirationDate() != null || entityRelationship.getEffectiveDate() != null || entityRelationship.getActive() !=null) {
			ENTYRLSHP webserviceEntityRelationship = new ENTYRLSHP();
			webserviceEntityRelationship.setWorkId(workId);
			if (entityRelationship.getKey() != null){
				webserviceEntityRelationship.setPARNTENTYID(entityRelationship.getKey().getChildEntityId().toString());
			} else if (entityRelationship.getKey() == null) {
				webserviceEntityRelationship.setPARNTENTYID(hierarchyValues.getHierarchyContext().getParentEntityId().toString());
			}
			webserviceEntityRelationship.setCHILDENTYID(childEntity.toString());
			webserviceEntityRelationship.setHIERCNTXTCD(hierarchyValues.getHierarchyContext().getId());
			webserviceEntityRelationship.setLSTUPDTUID(user);
			if (entityRelationship.getEffectiveDate() != null) {
				webserviceEntityRelationship.setEFFDT(entityRelationship.getEffectiveDate().toString());
			}
			if (entityRelationship.getExpirationDate() != null) {
				webserviceEntityRelationship.setEXPRNDT(entityRelationship.getExpirationDate().toString());
			}
			webserviceEntityRelationship.setACTVSW(convertBooleanToString(entityRelationship.getActive()));
			webserviceEntityRelationship.setDFLTPARNTSW(SWITCH_Y);
			request.getENTYRLSHP().add(webserviceEntityRelationship);
		}
		if(hierarchyValues.getHierarchyContext() != null) {
			ENTYDES webserviceEntityDescription = new ENTYDES();
			webserviceEntityDescription.setWorkId(workId);
			webserviceEntityDescription.setENTYID(childEntity.toString());
			webserviceEntityDescription.setHIERCNTXTCD(hierarchyValues.getHierarchyContext().getId());
			webserviceEntityDescription.setLSTUPDTUID(user);
			webserviceEntityDescription.setENTYLONGDESTEXT(hierarchyValues.getNewDescription());
			webserviceEntityDescription.setENTYSHRTDESTEXT(hierarchyValues.getNewDescription());
			request.getENTYDES().add(webserviceEntityDescription);
		}
		this.processReplyFromWebservice(request);
		return childEntity;
	}

	/**
	 * Writes a list of Attributes to the DB.
	 *
	 * @param attributes The list of Attributes to write.
	 */
	public void writeAttribute(List<? extends Attribute> attributes) throws CheckedSoapException {

		for (Attribute attribute : attributes) {
			String workId = Integer.toString(this.getWorkId());

			UpdateCodeTableRequest request = new UpdateCodeTableRequest();
			request.setAuthentication(this.getAuthentication());
			request.setTrackingNbr(workId);

			ATTR attr = new ATTR();
			attr.setWorkId(workId);
			if (attribute.getAction() == Attribute.INSERT) {
				attr.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());
			} else {
				attr.setACTIONCD(ACTION_CODE.ACTION_CD_UPDATE.getValue());
				attr.setATTRID(Long.toString(attribute.getAttributeId()));
			}
			attr.setATTRDESTEXT(attribute.getAttributeDescription());
			attr.setATTRDOMAINCD(attribute.getAttributeDomainCode());
			attr.setATTRINSTNDESTEXT(attribute.getAttributeInstantDescription());
			if (attribute.getMaximumLength() != null) {
				attr.setATTRMAXLNNBR(attribute.getMaximumLength().toString());
			}
			attr.setATTRNMTEXT(attribute.getAttributeName());
			if (attribute.getPrecision() != null) {
				attr.setATTRPRCSNNBR(attribute.getPrecision().toString());
			}
			if (attribute.getRequired() == null || attribute.getRequired().equals("X") || attribute.getRequired().equals("0") || attribute.getRequired().equals("0") ||attribute.getRequired().equals("N")) {
				attr.setATTRREQSW("N");
			} else {
				attr.setATTRREQSW("Y");
			}
			attr.setATTRVALLISTSW(attribute.isAttributeValueList() ? "Y" : "N");
			attr.setATTRXTRNLIDTEXT(attribute.getExternalId());
			attr.setDYNATTRSW(attribute.getDynamicAttributeSwitch() == null || !attribute.getDynamicAttributeSwitch() ? "N" : "Y");
			attr.setLOGICPHYTYPCD(attribute.getLogicalOrPhysical());
			attr.setLSTUPDTUID(attribute.getLastUpdateUserId());
			attr.setMULTVALSSW(attribute.getMultipleValueSwitch() == null || !attribute.getMultipleValueSwitch() ? "N" : "Y");
			attr.setOPTLVLCD(attribute.getOptionalOrRequired());
			if (attribute.getSourceSystemId() != null) {
				attr.setSRCSYSTEMID(attribute.getSourceSystemId().toString());
			}
			request.getATTR().add(attr);

			// If it's an insert, it needs to call the function that returns the ID of the created attribute.
			if (attribute.getAction() == Attribute.INSERT) {
				attribute.setAttributeId(this.processKeyReturningRequest(request));
			} else {
				this.processRequest(request);
			}
		}
	}

	/**
	 * Writes a list of EntityAttributes to the DB.
	 *
	 * @param entityAttributes The EntityAttributes to write.
	 */
	public void writeEntityAttributes(List<? extends EntityAttribute> entityAttributes) throws CheckedSoapException{

		String workId = Integer.toString(this.getWorkId());

		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(workId);

		for (EntityAttribute entityAttribute : entityAttributes) {

			ENTYATTR entyattr = new ENTYATTR();
			if (entityAttribute.getAction() == EntityAttribute.INSERT) {
				entyattr.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());
				entyattr.setATTRREQSW(entityAttribute.getRequired() ? "Y" : "N");
				entyattr.setENTYATTRSEQNBR(Long.toString(entityAttribute.getSequenceNumber()));
				entyattr.setENTYATTRFLDNM(entityAttribute.getEntityAttributeFieldName());
			} else if (entityAttribute.getAction() == EntityAttribute.DELETE) {
				entyattr.setACTIONCD(ACTION_CODE.ACTION_CD_DELETE.getValue());
			} else {
				entyattr.setACTIONCD(ACTION_CODE.ACTION_CD_UPDATE.getValue());
				entyattr.setATTRREQSW(entityAttribute.getRequired() ? "Y" : "N");
				entyattr.setENTYATTRSEQNBR(Long.toString(entityAttribute.getSequenceNumber()));
				entyattr.setENTYATTRFLDNM(entityAttribute.getEntityAttributeFieldName());
			}
			entyattr.setATTRID(Long.toString(entityAttribute.getKey().getAttributeId()));
			entyattr.setENTYID(Long.toString(entityAttribute.getKey().getEntityId()));

			request.getENTYATTR().add(entyattr);
		}
		this.processRequest(request);
	}

	/**
	 * Writes a list of AttributeCodes to the DB.
	 *
	 * @param attributeCodes The list of AttributeCodes to add.
	 * @throws CheckedSoapException
	 */
	public void writeAttributeCodes(List<? extends AttributeCode> attributeCodes) throws CheckedSoapException {

		for (AttributeCode attributeCode : attributeCodes) {
			String workId = Integer.toString(this.getWorkId());

			UpdateCodeTableRequest request = new UpdateCodeTableRequest();
			request.setAuthentication(this.getAuthentication());
			request.setTrackingNbr(workId);

			com.heb.xmlns.ei.attributecode.AttributeCode newAttributeCode = new com.heb.xmlns.ei.attributecode.AttributeCode();
			if (attributeCode.getAction() == AttributeCode.INSERT) {
				newAttributeCode.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());
				newAttributeCode.setATTRVALCD(attributeCode.getAttributeValueCode());
				newAttributeCode.setATTRVALTXT(attributeCode.getAttributeValueText());
				newAttributeCode.setATTRVALXTRNLID(attributeCode.getAttributeValueXtrnlId());
				newAttributeCode.setCRE8UID(attributeCode.getCreateUserId());
				newAttributeCode.setLSTUPDTUID(attributeCode.getLastUpdateUserId());
				newAttributeCode.setWorkId(workId);
				newAttributeCode.setXTRNLCDSW(attributeCode.getXtrnlCodeSwitch() ? "Y" : "N");
				request.getAttributeCode().add(newAttributeCode);

				attributeCode.setAttributeCodeId(this.processKeyReturningRequest(request));
			} else if (attributeCode.getAction() == AttributeCode.DELETE) {
				newAttributeCode.setACTIONCD(ACTION_CODE.ACTION_CD_DELETE.getValue());
				newAttributeCode.setATTRCDID(Long.toString(attributeCode.getAttributeCodeId()));
				newAttributeCode.setWorkId(workId);
				request.getAttributeCode().add(newAttributeCode);

				this.processRequest(request);
			}
		}
	}

	/**
	 * Writes a list of EntityAttributeCodes to the DB.
	 *
	 * @param entityAttributeCodes The list of EntityAttributeCodes to write.
	 * @throws CheckedSoapException
	 */
	public void writeEntityAttributeCodes(List<? extends EntityAttributeCode> entityAttributeCodes) throws CheckedSoapException {

		String workId = Integer.toString(this.getWorkId());

		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(workId);

		for (EntityAttributeCode entityAttributeCode : entityAttributeCodes) {

			com.heb.xmlns.ei.entityattributecode.EntityAttributeCode newEntityAttributeCode = new com.heb.xmlns.ei.entityattributecode.EntityAttributeCode();

			if (entityAttributeCode.getAction() == EntityAttributeCode.INSERT) {
				newEntityAttributeCode.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());
				newEntityAttributeCode.setCRE8TS(LocalDateTime.now().toString());
				newEntityAttributeCode.setCRE8UID(userInfo.getUserId());
			} else if (entityAttributeCode.getAction() == EntityAttributeCode.DELETE) {
				newEntityAttributeCode.setACTIONCD(ACTION_CODE.ACTION_CD_DELETE.getValue());
			} else {
				newEntityAttributeCode.setACTIONCD(ACTION_CODE.ACTION_CD_UPDATE.getValue());
			}
			newEntityAttributeCode.setWorkId(workId);
			newEntityAttributeCode.setATTRCDID(Long.toString(entityAttributeCode.getKey().getAttributeCodeId()));
			newEntityAttributeCode.setATTRID(Long.toString(entityAttributeCode.getKey().getAttributeId()));
			newEntityAttributeCode.setENTYID(Long.toString(entityAttributeCode.getKey().getEntityId()));

			newEntityAttributeCode.setLSTUPDTUID(userInfo.getUserId());
			newEntityAttributeCode.setLSTUPDTTS(LocalDate.now().toString());

			request.getEntityAttributeCode().add(newEntityAttributeCode);
		}

		this.processRequest(request);
	}

	/**
	 * Calls the service.
	 *
	 * @param updateCodeTableRequest The request to send to the service.
	 */
	private void processRequest(UpdateCodeTableRequest updateCodeTableRequest) throws CheckedSoapException {
		try {
			UpdateReplyMessage reply = this.getPort().updateCodeTableDetails(updateCodeTableRequest);
			if (!this.isSuccess(reply.getUpdateCodeTableReply().get(0).getReturnMsg())) {
				throw new CheckedSoapException(reply.getUpdateCodeTableReply().get(0).getReturnMsg());
			}
		} catch (Exception e) {
			throw new CheckedSoapException(e.getMessage());
		}
	}

	/**
	 * Calls the service and extracts and returns the key of whatever was created.
	 *
	 * @param updateCodeTableRequest The request to send to the web service.
	 * @return The key of the created thing.
	 */
	private long processKeyReturningRequest(UpdateCodeTableRequest updateCodeTableRequest) throws CheckedSoapException {
		try {
			UpdateReplyMessage reply = this.getPort().updateCodeTableDetails(updateCodeTableRequest);
			if (reply.getUpdateCodeTableReply().get(0).getKeyData().equals("")) {
				throw new CheckedSoapException(reply.getUpdateCodeTableReply().get(0).getReturnMsg());
			}
			return Long.parseLong(reply.getUpdateCodeTableReply().get(0).getKeyData());
		} catch (Exception e) {
			throw new CheckedSoapException(e.getMessage());
		}
	}

	/**
	 * Saves the tag and specification eCommerceUserGroupAttribtues to the DB
	 *
	 * @param attribute
	 *
	 * @throws CheckedSoapException
	 */
	public void writeTagAndSpecEcommerUserGroupAttributes(Attribute attribute)
			throws CheckedSoapException {

		String workId = Integer.toString(this.getWorkId());

		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(workId);

		// Handle tag changes
		if (attribute.getTagOperation().equals(ECOM_ATTRIBUTE_DELETE)) {
			deleteEcomUsrGrpAttribute(attribute, workId, request, EcommerUserGroupAttribute.TAG);
		} if (attribute.getTagOperation().equals(ECOM_ATTRIBUTE_ADD)) {
			addEcomUsrGrpAttribute(attribute, workId, request, EcommerUserGroupAttribute.TAG);
		}

		// Handle specification changes
		if (attribute.getSpecificationOperation().equals(ECOM_ATTRIBUTE_DELETE)) {
			deleteEcomUsrGrpAttribute(attribute, workId, request, EcommerUserGroupAttribute.SPEC);
		} if (attribute.getSpecificationOperation().equals(ECOM_ATTRIBUTE_ADD)) {
			addEcomUsrGrpAttribute(attribute, workId, request, EcommerUserGroupAttribute.SPEC);
		}

		if (!attribute.getTagOperation().equals(ECOM_ATTRIBUTE_NO_CHANGE) || !attribute.getSpecificationOperation().equals(ECOM_ATTRIBUTE_NO_CHANGE)) {
			this.processRequest(request);
		}
	}

	/**
	 * Add an ecommerce user group attribute (tag or spec)
	 * @param attribute
	 * @param workId
	 * @param request
	 */
	private void addEcomUsrGrpAttribute(Attribute attribute, String workId, UpdateCodeTableRequest request, String attrTypeValue) {
		ECOMUSRGRPATTR ecomusrgrpattr = new ECOMUSRGRPATTR();

		ecomusrgrpattr.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());

		ecomusrgrpattr.setATTRID(Long.toString(attribute.getAttributeId()));
		ecomusrgrpattr.setSEQNBR(Long.toString(0));
		ecomusrgrpattr.setUSRINRFCGRPCD(attrTypeValue);
		ecomusrgrpattr.setWorkId(workId);
		request.getECOMUSRGRPATTR().add(ecomusrgrpattr);
	}

	/**
	 * Delete an ecommerce user group attribute (tag or spec)
	 * @param attribute
	 * @param workId
	 * @param request
	 */
	private void deleteEcomUsrGrpAttribute(Attribute attribute, String workId, UpdateCodeTableRequest request, String attrTypeValue) {
		ECOMUSRGRPATTR ecomusrgrpattr = new ECOMUSRGRPATTR();

		ecomusrgrpattr.setACTIONCD(ACTION_CODE.ACTION_CD_DELETE.getValue());

		ecomusrgrpattr.setATTRID(Long.toString(attribute.getAttributeId()));
		ecomusrgrpattr.setSEQNBR(Long.toString(0));
		ecomusrgrpattr.setUSRINRFCGRPCD(attrTypeValue);
		ecomusrgrpattr.setWorkId(workId);
		request.getECOMUSRGRPATTR().add(ecomusrgrpattr);
	}

	/**
	 * Update attribute values
	 * @param numEntityAttributes
	 * @param numEntityAttributeCodes
	 * @param attribute
	 */
	public void updateAttributeValues(int numEntityAttributes, int numEntityAttributeCodes, Attribute attribute) throws CheckedSoapException {

		long attributeId = attribute.getAttributeId();

		List<EntityAttributeCode> addCodes = getAddedEntityAttributeCodes(attribute.getAttributeValues());
		if (addCodes.size() > 0) {

			updateFieldsInEntityAttributeCodes(attributeId, addCodes);

			// add the ENTY_ATTR value
			createEntityAttribute(numEntityAttributes, attributeId, attribute.getAttributeName());

			// add the ENTY_ATTR_CD values
			processAddedEntityAttributeCodes(addCodes);
		}

		List<EntityAttributeCode> deleteCodes = getDeletedEntityAttributeCodes(attribute.getAttributeValues());
		if (deleteCodes.size() > 0) {
			updateFieldsInEntityAttributeCodes(attributeId, deleteCodes);
			processDeletedEntityAttributeCodes(deleteCodes);

			// check if we deleted the last record and if so, then delete the ENTY_ATTR entry
			if (addCodes.size() == 0 && numEntityAttributeCodes == 1) {
				deleteEntityAttribute(attributeId);
			}
		}
	}

	/**
	 * Create the entity attribute
	 * @param numEntityAttributes
	 * @param attributeId
	 * @param attributeName
	 * @throws CheckedSoapException
	 */
	private void createEntityAttribute(int numEntityAttributes, long attributeId, String attributeName) throws CheckedSoapException {
		// need to populate ENTY_ATTR table with key row before adding ENTY_ATTR_CD values
		if (numEntityAttributes == 0) {
			List<EntityAttribute> eaList = new ArrayList<>();
			EntityAttribute ea = new EntityAttribute();
			ea.setKey(new EntityAttributeKey());
			ea.getKey().setEntityId(PRODUCT_CODE);
			ea.getKey().setAttributeId(attributeId);
			ea.setAction(EntityAttribute.INSERT);
			ea.setRequired(false);
			ea.setSequenceNumber(0L);
			ea.setEntityAttributeFieldName(attributeName);
			eaList.add(ea);
			writeEntityAttributes(eaList);
		}
	}

	/**
	 * Delete the entity attribute
	 * @param attributeId to be deleted
	 * @throws CheckedSoapException
	 */
	private void deleteEntityAttribute(long attributeId) throws CheckedSoapException {
		List<EntityAttribute> eaList = new ArrayList<>();
		EntityAttribute ea = new EntityAttribute();
		ea.setKey(new EntityAttributeKey());
		ea.getKey().setEntityId(PRODUCT_CODE);
		ea.getKey().setAttributeId(attributeId);
		ea.setAction(EntityAttribute.DELETE);
		eaList.add(ea);
		writeEntityAttributes(eaList);
	}

	/**
	 * Delete entity attribute codes
	 * @param codes
	 */
	private void processDeletedEntityAttributeCodes(List<EntityAttributeCode> codes) throws CheckedSoapException {
		if (codes.size() > 0) {
			writeEntityAttributeCodes(codes);
			writeAttributeCodesFromEntityAttributeCodes(codes);
		}
	}

	/**
	 * Add entity attribute codes
	 * @param codes
	 */
	private void processAddedEntityAttributeCodes(List<EntityAttributeCode> codes) throws CheckedSoapException {
		if (codes.size() > 0) {
			writeAttributeCodesFromEntityAttributeCodes(codes);

			writeEntityAttributeCodes(codes);
		}
	}

	/**
	 * Update the action in the entity attribute codes
	 * @param codes
	 */
	private void updateFieldsInEntityAttributeCodes(long attributeId, List<EntityAttributeCode> codes) {
		for (EntityAttributeCode code : codes) {
			if (code.getOperation().equals(ECOM_ATTRIBUTE_ADD)) {
				code.setAction(EntityAttributeCode.INSERT);
				code.setKey(new EntityAttributeCodeKey());
				code.getKey().setAttributeId(attributeId);
				code.getKey().setEntityId(PRODUCT_CODE);
			} else if (code.getOperation().equals(ECOM_ATTRIBUTE_DELETE)) {
				code.setAction(EntityAttributeCode.DELETE);
				code.getKey().setAttributeCodeId(code.getAttributeCode().getAttributeCodeId());
			}
		}
	}

	/**
	 * Get list of deleted entity attribute codes
	 * @param attributeValues
	 * @return
	 */
	private List<EntityAttributeCode> getDeletedEntityAttributeCodes(List<EntityAttributeCode> attributeValues) {
		List<EntityAttributeCode> result = new ArrayList<>();

		if (attributeValues != null) {
			for (EntityAttributeCode attributeValue : attributeValues) {
				if (attributeValue.getOperation().equals(ECOM_ATTRIBUTE_DELETE)) {
					result.add(attributeValue);
				}
			}
		}

		return result;
	}

	/**
	 * Get list of added entity attribute codes
	 * @param attributeValues
	 * @return
	 */
	private List<EntityAttributeCode> getAddedEntityAttributeCodes(List<EntityAttributeCode> attributeValues) {
		List<EntityAttributeCode> result = new ArrayList<>();

		if (attributeValues != null) {
			for (EntityAttributeCode attributeValue : attributeValues) {
				if (attributeValue.getOperation().equals(ECOM_ATTRIBUTE_ADD)) {
					result.add(attributeValue);
				}
			}
		}

		return result;
	}

	/**
	 * Write attribute codes
	 * @param entityAttributeCodes
	 */
	private void writeAttributeCodesFromEntityAttributeCodes(List<EntityAttributeCode> entityAttributeCodes) throws CheckedSoapException {
		List<AttributeCode> attributeCodes = new ArrayList<>();

		for (EntityAttributeCode entityAttributeCode : entityAttributeCodes) {
			if (entityAttributeCode.getOperation().equals(ECOM_ATTRIBUTE_ADD)) {
				entityAttributeCode.getAttributeCode().setAction(AttributeCode.INSERT);
				entityAttributeCode.getAttributeCode().setAttributeValueCode(entityAttributeCode.getAttributeCode().getAttributeValueText());
				entityAttributeCode.getAttributeCode().setAttributeValueXtrnlId("0");
				entityAttributeCode.getAttributeCode().setXtrnlCodeSwitch(false);
				entityAttributeCode.getAttributeCode().setCreateUserId(entityAttributeCode.getCreateUserId());
				entityAttributeCode.getAttributeCode().setLastUpdateUserId(entityAttributeCode.getLastUpdateUserId());
			} else if (entityAttributeCode.getOperation().equals(ECOM_ATTRIBUTE_DELETE)) {
				entityAttributeCode.getAttributeCode().setAction(AttributeCode.DELETE);
			}
			attributeCodes.add(entityAttributeCode.getAttributeCode());
		}

		writeAttributeCodes(attributeCodes);

		// update attribute code id in the records after it is created
		for (EntityAttributeCode entityAttributeCode : entityAttributeCodes) {
			entityAttributeCode.getKey().setAttributeCodeId(entityAttributeCode.getAttributeCode().getAttributeCodeId());
		}
	}
	/**
	 * Save data for Product Group info.
	 *
	 * @param custProdGrp the information of Product Group.
	 * @return productGroupCode of Product Group.
	 */
	public String updateProductGroupInfo(CUSTPRODGRP custProdGrp) throws Exception{
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(CodeTableManagementServiceClient.DEFAULT_TRACKING_NUMBER);
		request.getCUSTPRODGRP().add(custProdGrp);
		UpdateReplyMessage reply;
		String productGroupCode = StringUtils.EMPTY;
		//Send request and handle response returned from webservice.
		try {
			reply = this.getPort().updateCodeTableDetails(request);
			this.handleErrorMessage(reply);
			if(reply.getUpdateCodeTableReply().get(0) != null && reply.getUpdateCodeTableReply().get(0).getKeyData() !=null){
				productGroupCode = reply.getUpdateCodeTableReply().get(0).getKeyData().trim();
			}
		} catch (Exception e) {
			throw new SoapException(e.getMessage(), e.getCause());
		}
		return productGroupCode;
	}

	/**
	 * This method will take a list of relationships to remove and request them to be removed from the database
	 * @param relationshipToMove the relationship to be removed and added
	 * @param user the user requesting the change
	 * @return
	 */
	public void customHierarchyGenericEntityRelationshipMoveLevel(HierarchyChanges relationshipToMove, String user){
		CodeTableManagementServiceClient.logger.info(String.format(MOVE_LEVEL_BEGIN, user, relationshipToMove.getRelationshipsRemoved().get(0).getKey().getHierarchyContext(),
				relationshipToMove.getRelationshipsRemoved().get(0).getKey().getParentEntityId(), relationshipToMove.getRelationshipsRemoved().get(0).getKey().getChildEntityId()));
		GenericEntityRelationship relationshipToRemove = relationshipToMove.getRelationshipsRemoved().get(0);
		GenericEntityRelationship relationshipToAdd = relationshipToMove.getRelationshipsAdded().get(0);
		String workId = this.getWorkId().toString();
		UpdateCodeTableRequest requestRemove = new UpdateCodeTableRequest();
		UpdateCodeTableRequest requestAdd = new UpdateCodeTableRequest();
		requestRemove.setAuthentication(this.getAuthentication());
		requestRemove.setTrackingNbr(workId);
		requestAdd.setAuthentication(this.getAuthentication());
		requestAdd.setTrackingNbr(workId);
		ENTYRLSHP webserviceEntityRelationshipToAdd = createOrDestroyEntityRelationship(relationshipToAdd, ACTION_CODE.ACTION_CD_ADD.getValue(),
				workId, user, convertBooleanToString(relationshipToAdd.getDefaultParent()));
		requestAdd.getENTYRLSHP().add(webserviceEntityRelationshipToAdd);
		ENTYRLSHP webserviceEntityRelationshipToRemove = createOrDestroyEntityRelationship(relationshipToRemove, ACTION_CODE.ACTION_CD_DELETE.getValue(),
				workId, user, SWITCH_N);
		requestRemove.getENTYRLSHP().add(webserviceEntityRelationshipToRemove);
		this.processReplyFromWebservice(requestAdd);
		this.processReplyFromWebservice(requestRemove);
		CodeTableManagementServiceClient.logger.info(String.format(MOVE_LEVEL_COMPLETE, user, relationshipToMove.getRelationshipsRemoved().get(0).getKey().getHierarchyContext(),
				relationshipToMove.getRelationshipsRemoved().get(0).getKey().getParentEntityId(), relationshipToMove.getRelationshipsRemoved().get(0).getKey().getChildEntityId()));
	}

	/**
	 * This method will take a list of relationships to add and request them to be added to the database
	 * @param relationshipsToAdd the list of relationships to add
	 * @param user the user requesting the change
	 * @return
	 */
	public void customHierarchyGenericEntityRelationshipAdditions(List<GenericEntityRelationship> relationshipsToAdd, String user, boolean makeParent){
		CodeTableManagementServiceClient.logger.info(String.format(LINK_LEVEL_BEGIN, user, relationshipsToAdd.size()));
		HashSet<String> newHierarchyContexts = new HashSet<>();
		String workId = this.getWorkId().toString();
		UpdateCodeTableRequest request = new UpdateCodeTableRequest();
		request.setAuthentication(this.getAuthentication());
		for (GenericEntityRelationship genericEntityRelationship: relationshipsToAdd){
			if(newHierarchyContexts.add(genericEntityRelationship.getChildDescription().getKey().getHierarchyContext())){
				ENTYDES webserviceEntityDescription = createNewEntityDescription(genericEntityRelationship.getChildDescription(), workId, user);
				request.getENTYDES().add(webserviceEntityDescription);
				request.setTrackingNbr(workId);
			}
			ENTYRLSHP webserviceEntityRelationship = createOrDestroyEntityRelationship(genericEntityRelationship, ACTION_CODE.ACTION_CD_ADD.getValue(), workId, user, convertBooleanToString(makeParent));
			request.getENTYRLSHP().add(webserviceEntityRelationship);
		}
		this.processReplyFromWebservice(request);
		CodeTableManagementServiceClient.logger.info(String.format(LINK_LEVEL_COMPLETE, user, relationshipsToAdd.size()));
	}

	/**
	 * This method will create a webserviceEntity Description for adding to the database
	 * @param genericEntityDescriptionCandidate
	 * @param workId
	 * @param user
	 * @return
	 */
	private ENTYDES createNewEntityDescription(GenericEntityDescription genericEntityDescriptionCandidate, String workId, String user){
		ENTYDES webserviceEntityDescription = new ENTYDES();
		webserviceEntityDescription.setWorkId(workId);
		webserviceEntityDescription.setENTYID(genericEntityDescriptionCandidate.getKey().getEntityId().toString());
		webserviceEntityDescription.setHIERCNTXTCD(genericEntityDescriptionCandidate.getKey().getHierarchyContext());
		webserviceEntityDescription.setLSTUPDTUID(user);
		webserviceEntityDescription.setENTYLONGDESTEXT(genericEntityDescriptionCandidate.getLongDescription());
		webserviceEntityDescription.setENTYSHRTDESTEXT(genericEntityDescriptionCandidate.getShortDescription());
		return webserviceEntityDescription;
	}

	/**
	 * This method will attempt to create or remove an entity relationship
	 * @param genericEntityRelationshipCandidate the relationship in question
	 * @param actionCode the work to be attempted (add/delete)
	 * @param workId the work id for auditing
	 * @param user the user requesting the change
	 * @param defaultParentSwitch if a remove level was a parent then the new relationship is a parent
	 * @return an webserviceEntityRelationship for requesting a change
	 */
	private ENTYRLSHP createOrDestroyEntityRelationship (GenericEntityRelationship genericEntityRelationshipCandidate, String actionCode,
														 String workId, String user, String defaultParentSwitch){
		ENTYRLSHP webserviceEntityRelationship = new ENTYRLSHP();
		webserviceEntityRelationship.setACTIONCD(actionCode);
		webserviceEntityRelationship.setWorkId(workId);
		webserviceEntityRelationship.setPARNTENTYID(genericEntityRelationshipCandidate.getKey().getParentEntityId().toString());
		webserviceEntityRelationship.setCHILDENTYID(genericEntityRelationshipCandidate.getKey().getChildEntityId().toString());
		webserviceEntityRelationship.setHIERCNTXTCD(genericEntityRelationshipCandidate.getKey().getHierarchyContext());
		webserviceEntityRelationship.setDFLTPARNTSW(defaultParentSwitch);
		webserviceEntityRelationship.setDSPLYSW(SWITCH_Y);
		webserviceEntityRelationship.setCRE8UID(user);
		webserviceEntityRelationship.setCRE8TS(LocalDate.now().toString());
		webserviceEntityRelationship.setLSTUPDTUID(user);
		webserviceEntityRelationship.setLSTUPDTTS(LocalDate.now().toString());
		if(genericEntityRelationshipCandidate.getEffectiveDate() != null){
			webserviceEntityRelationship.setEFFDT(genericEntityRelationshipCandidate.getEffectiveDate().toString());
		}
		if(genericEntityRelationshipCandidate.getExpirationDate() != null){
			webserviceEntityRelationship.setEXPRNDT(genericEntityRelationshipCandidate.getExpirationDate().toString());
		}
		webserviceEntityRelationship.setACTVSW(convertBooleanToString(genericEntityRelationshipCandidate.getActive()));
		if(actionCode.equals( ACTION_CODE.ACTION_CD_DELETE.getValue())){
			webserviceEntityRelationship.setDELETEALLCHILDREN(DELETE_ALL_CHILDREN_CODE_SINGLE);
		}
		return webserviceEntityRelationship;
	}
}
