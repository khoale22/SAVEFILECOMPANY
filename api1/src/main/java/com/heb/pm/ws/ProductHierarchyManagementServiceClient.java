package com.heb.pm.ws;

import com.heb.pm.customHierarchy.HierarchyContextController;
import com.heb.pm.entity.*;
import com.heb.util.controller.UserInfo;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.PortType;
import com.heb.xmlns.ei.ProductHierarchyManagementServiceServiceagent;
import com.heb.xmlns.ei.hierarchycontext.HierarchyContext;
import com.heb.xmlns.ei.itmcls.ItmCls;
import com.heb.xmlns.ei.pdclasscommodity.PdClassCommodity;
import com.heb.xmlns.ei.pdclscomsubcom.PdClsComSubCom;
import com.heb.xmlns.ei.producthierarchymanagement.update_product_hierarchy_request.UpdateProductHierarchyRequest;
import com.heb.xmlns.ei.productmanagement.update_producthierarchy_reply.UpdateProductHierarchyReplyMessage;
import com.heb.xmlns.ei.salsrstrcodedflt.SalsRstrCodeDflt;
import com.heb.xmlns.ei.salsrstrgrpdflt.SalsRstrGrpDflt;
import com.heb.xmlns.ei.stwarndfltscom.StWarnDfltScom;
import com.heb.xmlns.ei.subcommoditydefaultuom.SubCommodityDefaultUOMUpdate;
import org.apache.commons.lang.StringUtils;
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
 * Provides access to service endpoint for product hierarchy management service.
 *
 * @author m314029
 * @since 2.8.0
 */
@Service
public class ProductHierarchyManagementServiceClient extends BaseWebServiceClient
		<ProductHierarchyManagementServiceServiceagent, PortType>{

	private static final Logger logger = LoggerFactory.getLogger(ProductHierarchyManagementServiceClient.class);

	// constants for webservice
	private static final String STRING_YES = "Y";
	private static final String STRING_NO = "N";

	// webservice success messages
	private static final String UPDATE_PRODUCT_HIERARCHY_REPLY_SUCCESS = "Z06S0500 - Successfully Updated";

	// errors
	private static final String ERROR_WEB_SERVICE_UPDATE_RESPONSE = "Error with this update: %s.";

	@Value("${productHierarchyManagementService.uri}")
	private String uri;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Submit the call to product hierarchy management service to update selling restrictions.
	 *
	 * @param sellingRestrictions The selling restrictions to update.
	 */
	public void updateSellingRestrictions(List<SellingRestrictionHierarchyLevel> sellingRestrictions){
		UpdateProductHierarchyRequest updateProductHierarchyRequest = new UpdateProductHierarchyRequest();
		Integer workId = this.getWorkId();
		updateProductHierarchyRequest.getSalsRstrGrpDflt().addAll(
				this.convertSalsRstrGrpDfltsFromSellingRestrictionHierarchyLevels(sellingRestrictions, workId));
		updateProductHierarchyRequest.setTrackingNbr(workId.toString());
		this.updateProductHierarchyDetails(updateProductHierarchyRequest);
	}

	/**
	 * Submit the call to product hierarchy management service to update shipping restrictions.
	 *
	 * @param shippingRestrictions The shipping restrictions to update.
	 */
	public void updateShippingRestrictions(List<ShippingRestrictionHierarchyLevel> shippingRestrictions){
		UpdateProductHierarchyRequest updateProductHierarchyRequest = new UpdateProductHierarchyRequest();
		Integer workId = this.getWorkId();
		updateProductHierarchyRequest.getSalsRstrCodeDflt().addAll(
				this.convertSalsRstrCodeDfltsFromShippingRestrictionHierarchyLevels(shippingRestrictions, workId));
		updateProductHierarchyRequest.setTrackingNbr(workId.toString());
		this.updateProductHierarchyDetails(updateProductHierarchyRequest);
	}

	/**
	 * Converts a list of SellingRestrictionHierarchyLevel into a list of SalsRstrGrpDflt.
	 *
	 * @param sellingRestrictions List of SellingRestrictionHierarchyLevel to convert.
	 * @param workId The work id to use for this request and all elements within.
	 * @return List of SalsRstrGrpDflt.
	 */
	private List<SalsRstrGrpDflt> convertSalsRstrGrpDfltsFromSellingRestrictionHierarchyLevels(List<SellingRestrictionHierarchyLevel> sellingRestrictions, Integer workId) {
		List<SalsRstrGrpDflt> salsRstrGrpDflts = new ArrayList<>();
		for(SellingRestrictionHierarchyLevel restriction : sellingRestrictions){
			salsRstrGrpDflts.add(this.createSalsRstrGrpDfltFromSellingRestrictionHierarchyLevel(restriction, workId));
		}
		return salsRstrGrpDflts;
	}

	/**
	 * Converts a list of ShippingRestrictionHierarchyLevel into a list of SalsRstrCodeDflt.
	 *
	 * @param shippingRestrictions List of ShippingRestrictionHierarchyLevel to convert.
	 * @param workId The work id to use for this request and all elements within.
	 * @return List of SalsRstrCodeDflt.
	 */
	private List<SalsRstrCodeDflt> convertSalsRstrCodeDfltsFromShippingRestrictionHierarchyLevels(
			List<ShippingRestrictionHierarchyLevel> shippingRestrictions, Integer workId) {
		List<SalsRstrCodeDflt> salsRstrCodeDflts = new ArrayList<>();
		for(ShippingRestrictionHierarchyLevel restriction : shippingRestrictions){
			salsRstrCodeDflts.add(this.createSalsRstrCodeDfltFromShippingRestrictionHierarchyLevel(restriction, workId));
		}
		return salsRstrCodeDflts;
	}

	/**
	 * Update custom hierarchy.  Updates HIER_CNTXT by adding new Hierarchy.
	 *
	 * @param customHierarchy the custom hierarchy
	 * @param userInfo        the user info
	 */
	public void updateCustomHierarchy(HierarchyContextController.CustomHierarchyValues customHierarchy, UserInfo userInfo) {
		HierarchyContext hierarchyContext = new HierarchyContext();
		Integer workId = this.getWorkId();

		hierarchyContext.setACTIONCD("");
		hierarchyContext.setWorkId(workId.toString());
		hierarchyContext.setHIERCNTXTCD(customHierarchy.getNewHierarchyContext().toUpperCase());
		hierarchyContext.setHIERCNTXTDES(customHierarchy.getNewHierarchyName());
		hierarchyContext.setLSTUPDTUID(userInfo.getUserId());

		UpdateProductHierarchyRequest request = new UpdateProductHierarchyRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(workId.toString());
		request.getHierarchyContext().add(hierarchyContext);

		this.updateProductHierarchyDetails(request);
	}

	/**
	 * Submits an updateProductHierarchyDetailsRequest to update a product hierarchy. The request parameter should
	 * already be filled in with variable components.
	 *
	 * @param request The request to send with variable components filled in.
	 */
	private void updateProductHierarchyDetails(UpdateProductHierarchyRequest request){
		request.setAuthentication(this.getAuthentication());
		try{
			UpdateProductHierarchyReplyMessage replyMessage = this.getPort().updateProductHierarchyDetails(request);
			StringBuilder errorMessage = new StringBuilder(StringUtils.EMPTY);
			if(!replyMessage.getUpdateProductHierarchyReply().isEmpty()){
				String returnMessage;
				for(UpdateProductHierarchyReplyMessage.UpdateProductHierarchyReply reply :
						replyMessage.getUpdateProductHierarchyReply()){
					returnMessage = reply.getReturnMsg();

					// if a return message is not success or empty, it is an error: add the message to errorMessage
					if(!returnMessage.equals(ProductHierarchyManagementServiceClient.UPDATE_PRODUCT_HIERARCHY_REPLY_SUCCESS)
							&& !returnMessage.equals(StringUtils.EMPTY)){
						errorMessage.append(reply.getReturnMsg());
					}
				}

				// if there were any error messages, throw exception
				if(!errorMessage.toString().equals(StringUtils.EMPTY)){
					throw new SoapException(errorMessage.toString());
				}
			}
		} catch (Exception e) {
			throw new SoapException(e.getMessage());
		}
	}

	/**
	 * Create a product hierarchy management service SalsRstrGrpDflt object from a SellingRestrictionHierarchyLevel
	 * entity, then return the SalsRstrGrpDflt created.
	 *
	 * @param sellingRestriction The selling restriction to convert.
	 * @param workId The work id to use for this request and all elements within.
	 * @return The SalsRstrGrpDflt created.
	 */
	private SalsRstrGrpDflt createSalsRstrGrpDfltFromSellingRestrictionHierarchyLevel(SellingRestrictionHierarchyLevel sellingRestriction, Integer workId){
		SalsRstrGrpDflt toReturn = new SalsRstrGrpDflt();
		toReturn.setWorkId(workId.toString());
		toReturn.setACTIONCD(sellingRestriction.getActionCode());
		toReturn.setSALSRSTRGRPCD(sellingRestriction.getKey().getRestrictionGroupCode());
		toReturn.setLSTUPDTUID(this.userInfo.getUserId());
		toReturn.setSTRDEPTNBR(sellingRestriction.getKey().getDepartment());
		toReturn.setSTRSUBDEPTID(sellingRestriction.getKey().getSubDepartment().trim().equals(StringUtils.EMPTY) ?
				"0" : sellingRestriction.getKey().getSubDepartment());
		toReturn.setITEMCLSCODE(sellingRestriction.getKey().getItemClass().toString());
		toReturn.setPDOMICOMCD(sellingRestriction.getKey().getCommodity().toString());
		toReturn.setPDOMISUBCOMCD(sellingRestriction.getKey().getSubCommodity().toString());
		toReturn.setOverrideLowerlevels(sellingRestriction.getOverrideLowerLevels() ?
				ProductHierarchyManagementServiceClient.STRING_YES : ProductHierarchyManagementServiceClient.STRING_NO);
		return toReturn;
	}

	/**
	 * Create a product hierarchy management service SalsRstrCodeDflt object from a ShippingRestrictionHierarchyLevel
	 * entity, then return the SalsRstrCodeDflt created.
	 *
	 * @param shippingRestriction The shipping restriction to convert.
	 * @param workId The work id to use for this request and all elements within.
	 * @return The SalsRstrCodeDflt created.
	 */
	private SalsRstrCodeDflt createSalsRstrCodeDfltFromShippingRestrictionHierarchyLevel(ShippingRestrictionHierarchyLevel shippingRestriction, Integer workId){
		SalsRstrCodeDflt toReturn = new SalsRstrCodeDflt();
		toReturn.setWorkId(workId.toString());
		toReturn.setACTIONCD(shippingRestriction.getActionCode());
		toReturn.setSALSRSTRCD(shippingRestriction.getKey().getRestrictionCode());
		toReturn.setLSTUPDTUID(this.userInfo.getUserId());
		toReturn.setSTRDEPTNBR(shippingRestriction.getKey().getDepartment());
		toReturn.setSTRSUBDEPTID(shippingRestriction.getKey().getSubDepartment());
		toReturn.setITEMCLSCODE(shippingRestriction.getKey().getItemClass().toString());
		toReturn.setPDOMICOMCD(shippingRestriction.getKey().getCommodity().toString());
		toReturn.setPDOMISUBCOMCD(shippingRestriction.getKey().getSubCommodity().toString());
		toReturn.setOverrideLowerlevels(shippingRestriction.getOverrideLowerLevels() ?
				ProductHierarchyManagementServiceClient.STRING_YES : ProductHierarchyManagementServiceClient.STRING_NO);
		toReturn.setEXCSNSW(shippingRestriction.getApplicableAtThisLevel() ?
				ProductHierarchyManagementServiceClient.STRING_YES : ProductHierarchyManagementServiceClient.STRING_NO);
		return toReturn;
	}

	/**
	 * Submit the call to product hierarchy management service to update an item-class.
	 *
	 * @param itemClass The item class to update.
	 */
	public void updateItemClass(ItemClass itemClass) {
		UpdateProductHierarchyRequest updateProductHierarchyRequest = new UpdateProductHierarchyRequest();
		Integer workId = this.getWorkId();
		updateProductHierarchyRequest.getItmCls().add(this.createItmClsFromItemClass(itemClass, workId));
		updateProductHierarchyRequest.setTrackingNbr(workId.toString());
		this.updateProductHierarchyDetails(updateProductHierarchyRequest);
	}

	/**
	 * Create a product hierarchy management service ItmCls object from a ItemClass entity, then return the
	 * ItmCls created.
	 *
	 * @param itemClass The item-class to convert.
	 * @param workId The work id to use for this request and all elements within.
	 * @return The ItmCls created.
	 */
	private ItmCls createItmClsFromItemClass(ItemClass itemClass, Integer workId) {
		ItmCls toReturn = new ItmCls();
		toReturn.setWorkId(workId.toString());
		toReturn.setITEMCLSCODE(itemClass.getItemClassCode().toString());
		if(itemClass.getBillCostEligible() != null)
			toReturn.setBILCSTELIGSW(itemClass.getBillCostEligible() ?
					ProductHierarchyManagementServiceClient.STRING_YES : ProductHierarchyManagementServiceClient.STRING_NO);
		if(itemClass.getGenericClass() != null)
			toReturn.setGNRCCLS(itemClass.getGenericClass().toString());
		if(!StringUtils.EMPTY.equals(itemClass.getItemClassDescription().trim())) {
			toReturn.setITMCLSDES(itemClass.getItemClassDescription().toUpperCase());
		}
		if(itemClass.getMerchantTypeCode() != null) {
			toReturn.setMERCHTYPCD(itemClass.getMerchantTypeCode());
		}
		if(itemClass.getScanDepartment() != null) {
			toReturn.setSCNDEPT(itemClass.getScanDepartment().toString());
		}
		return toReturn;
	}

	/**
	 * Submit the call to product hierarchy management service to update a commodity.
	 *
	 * @param commodity The commodity to update.
	 */
	public void updateCommodity(ClassCommodity commodity) {
		UpdateProductHierarchyRequest updateProductHierarchyRequest = new UpdateProductHierarchyRequest();
		Integer workId = this.getWorkId();
		updateProductHierarchyRequest.getPdClassCommodity().add(
				this.createPdClassCommodityFromClassCommodity(commodity, workId));
		updateProductHierarchyRequest.setTrackingNbr(workId.toString());
		this.updateProductHierarchyDetails(updateProductHierarchyRequest);
	}

	/**
	 * Create a product hierarchy management service PdClassCommodity object from a ClassCommodity entity, then return
	 * the PdClassCommodity created.
	 *
	 * @param commodity The commodity to convert.
	 * @param workId The work id to use for this request and all elements within.
	 * @return The PdClassCommodity created.
	 */
	private PdClassCommodity createPdClassCommodityFromClassCommodity(ClassCommodity commodity, Integer workId) {
		PdClassCommodity toReturn = new PdClassCommodity();
		toReturn.setWorkId(workId.toString());
		toReturn.setPDOMICOMCLSCD(commodity.getKey().getClassCode().toString());
		toReturn.setPDOMICOMCD(commodity.getKey().getCommodityCode().toString());
		if(commodity.getName() != null && !StringUtils.EMPTY.equals(commodity.getName().trim())) {
			toReturn.setPDOMICOMDES(commodity.getName());
		}
		if(commodity.getPssDepartment() != null) {
			toReturn.setPDPSSDEPTNO(commodity.getPssDepartment().toString());
		}
		if(commodity.getBdmCode() != null) {
			toReturn.setBDMCD(commodity.getBdmCode());
		}
		if(commodity.getbDAid() != null) {
			if (StringUtils.EMPTY.equals(commodity.getbDAid())) {
				toReturn.setBDAUID(" ");
			} else {
				toReturn.setBDAUID(commodity.getbDAid());
			}
		}
		if(commodity.geteBMid() != null) {
			if (StringUtils.EMPTY.equals(commodity.geteBMid())) {
				toReturn.setECOMMBUSMGRID(" ");
			} else {
				toReturn.setECOMMBUSMGRID(commodity.geteBMid());
			}
		}
		if(commodity.getClassCommodityActive() != null) {
			toReturn.setPCCLSCOMACTVCD(commodity.getClassCommodityActive().toString());
		}
		toReturn.setLSTUPDTUID(this.userInfo.getUserId());
		return toReturn;
	}

	/**
	 * Submit the call to product hierarchy management service to update a sub-commodity .
	 *
	 * @param subCommodity The sub-commodity to update.
	 */
	public void updateSubCommodity(SubCommodity subCommodity) {
		UpdateProductHierarchyRequest updateProductHierarchyRequest = new UpdateProductHierarchyRequest();
		String workId = this.getWorkId().toString();
		updateProductHierarchyRequest.setTrackingNbr(workId);
		updateProductHierarchyRequest.getPdClsComSubCom().add(
				this.createPdClsComSubComFromSubCommodity(subCommodity, workId));
		this.updateProductHierarchyDetails(updateProductHierarchyRequest);
	}

	/**
	 * Submit the call to product hierarchy management service to update a list of sub-commodity state warnings.
	 *
	 * @param stateWarnings The sub-commodity state warnings to update.
	 */
	public void updateSubCommodityStateWarnings(List<SubCommodityStateWarning> stateWarnings) {
		UpdateProductHierarchyRequest updateProductHierarchyRequest = new UpdateProductHierarchyRequest();
		String workId = this.getWorkId().toString();
		updateProductHierarchyRequest.setTrackingNbr(workId);
		updateProductHierarchyRequest.getStWarnDfltScom().addAll(
				this.createStWarnDfltScomsFromStateWarnings(stateWarnings, workId));
		this.updateProductHierarchyDetails(updateProductHierarchyRequest);
	}

	/**
	 * Submit the call to product hierarchy management service to update a list of product preferred unit of measures.
	 *
	 * @param productPreferredUnitOfMeasures The product preferred unit of measures to update.
	 */
	public void updateProductPreferredUnitOfMeasures(List<ProductPreferredUnitOfMeasure> productPreferredUnitOfMeasures) {
		UpdateProductHierarchyRequest updateProductHierarchyRequest = new UpdateProductHierarchyRequest();
		String workId = this.getWorkId().toString();
		updateProductHierarchyRequest.setTrackingNbr(workId);
		updateProductHierarchyRequest.getSubCommodityDefaultUOM().addAll(
				this.createSubCommodityDefaultUOMUpdatesFromProductPreferredUnitOfMeasures(
						productPreferredUnitOfMeasures, workId));
		this.updateProductHierarchyDetails(updateProductHierarchyRequest);
	}

	/**
	 * Create a list of product hierarchy management service StWarnDfltScom objects from a list of
	 * SubCommodityStateWarning entities, then return the list created.
	 *
	 * @param stateWarnings The SubCommodityStateWarnings to convert.
	 * @param workId The work id to use for this request and all elements within.
	 * @return The StWarnDfltScoms created.
	 */
	private List<StWarnDfltScom> createStWarnDfltScomsFromStateWarnings(
			List<SubCommodityStateWarning> stateWarnings, String workId) {
		List<StWarnDfltScom> returnList = new ArrayList<>();
		StWarnDfltScom stateWarning;
		for(SubCommodityStateWarning subCommodityStateWarning : stateWarnings){
			stateWarning = new StWarnDfltScom();
			stateWarning.setWorkId(workId);
			stateWarning.setACTIONCD(subCommodityStateWarning.getAction());
			stateWarning.setPDOMISUBCOMCD(subCommodityStateWarning.getKey().getSubCommodityCode().toString());
			stateWarning.setSTCD(subCommodityStateWarning.getKey().getStateCode());
			stateWarning.setSTPRODWARNCD(subCommodityStateWarning.getKey().getStateProductWarningCode());
			stateWarning.setLSTUPDTUID(this.userInfo.getUserId());
			returnList.add(stateWarning);
		}
		return returnList;
	}

	/**
	 * Create a list of product hierarchy management service SubCommodityDefaultUOMUpdate objects from a list of
	 * ProductPreferredUnitOfMeasure entities, then return the list created.
	 *
	 * @param productPreferredUnitOfMeasures The ProductPreferredUnitOfMeasures to convert.
	 * @param workId The work id to use for this request and all elements within.
	 * @return The SubCommodityDefaultUOMUpdates created.
	 */
	private List<SubCommodityDefaultUOMUpdate> createSubCommodityDefaultUOMUpdatesFromProductPreferredUnitOfMeasures(
			List<ProductPreferredUnitOfMeasure> productPreferredUnitOfMeasures, String workId) {
		List<SubCommodityDefaultUOMUpdate> returnList = new ArrayList<>();
		SubCommodityDefaultUOMUpdate defaultUOM;
		for(ProductPreferredUnitOfMeasure preferredUnitOfMeasure : productPreferredUnitOfMeasures){
			defaultUOM = new SubCommodityDefaultUOMUpdate();
			defaultUOM.setWorkId(workId);
			defaultUOM.setACTIONCD(preferredUnitOfMeasure.getAction());
			defaultUOM.setPDOMISUBCOMCD(preferredUnitOfMeasure.getKey().getSubCommodityCode().toString());
			defaultUOM.setRETLSELLSZCD(preferredUnitOfMeasure.getKey().getRetailUnitOfMeasureCode());
			defaultUOM.setPREFUOMSEQNBR(preferredUnitOfMeasure.getSequenceNumber().toString());
			defaultUOM.setLSTUPDTUID(this.userInfo.getUserId());
			returnList.add(defaultUOM);
		}
		return returnList;
	}

	/**
	 * Create a product hierarchy management service PdClsComSubCom object from a SubCommodity entity, then return
	 * the PdClsComSubCom created.
	 *
	 * @param subCommodity The sub-commodity to convert.
	 * @param workId The work id to use for this request and all elements within.
	 * @return The PdClsComSubCom created.
	 */
	private PdClsComSubCom createPdClsComSubComFromSubCommodity(SubCommodity subCommodity, String workId) {
		PdClsComSubCom toReturn = new PdClsComSubCom();
		toReturn.setWorkId(workId);
		toReturn.setPDOMICOMCLSCD(subCommodity.getKey().getClassCode().toString());
		toReturn.setPDOMICOMCD(subCommodity.getKey().getCommodityCode().toString());
		toReturn.setPDOMISUBCOMCD(subCommodity.getKey().getSubCommodityCode().toString());
		if(subCommodity.getName() != null && !StringUtils.EMPTY.equals(subCommodity.getName().trim())) {
			toReturn.setPDOMICOMDES(subCommodity.getName());
		}
		if(subCommodity.getProductCategoryId() != null) {
			toReturn.setPRODCATID(subCommodity.getProductCategoryId().toString());
		}
		if(subCommodity.getSubCommodityActive() != null) {
			toReturn.setPCSUBCOMACTVCD(subCommodity.getSubCommodityActive().toString());
		}
		if(subCommodity.getImsCommodityCode() != null) {
			toReturn.setPDCOMCD(subCommodity.getImsCommodityCode().toString());
		}
		if(subCommodity.getImsSubCommodityCode() != null) {
			toReturn.setPDSUBCOMCD(subCommodity.getImsSubCommodityCode().toString());
		}
		if(subCommodity.getFoodStampEligible() != null) {
			toReturn.setPDFDSTAMPCD(subCommodity.getFoodStampEligible() ?
					ProductHierarchyManagementServiceClient.STRING_YES : ProductHierarchyManagementServiceClient.STRING_NO);
		}
		if(subCommodity.getTaxEligible() != null) {
			toReturn.setPDCRGTAXCD(subCommodity.getTaxEligible() ?
					ProductHierarchyManagementServiceClient.STRING_YES : ProductHierarchyManagementServiceClient.STRING_NO);
		}
		if(subCommodity.getTaxCategoryCode() != null) {
			toReturn.setVERTEXTAXCD(subCommodity.getTaxCategoryCode());
		}
		if(subCommodity.getNonTaxCategoryCode() != null) {
			toReturn.setVERTEXNONTAXCD(subCommodity.getNonTaxCategoryCode());
		}
		toReturn.setLSTUPDTUID(this.userInfo.getUserId());
		return toReturn;
	}

	/**
	 * Return the service agent for this client.
	 *
	 * @return ProductHierarchyManagementServiceServiceagent associated with this client.
	 */
	@Override
	protected ProductHierarchyManagementServiceServiceagent getServiceAgent() {
		try {
			URL url = new URL(this.getWebServiceUri());
			return new ProductHierarchyManagementServiceServiceagent(url);
		} catch (MalformedURLException e) {
			ProductHierarchyManagementServiceClient.logger.error(e.getMessage());
		}
		return new ProductHierarchyManagementServiceServiceagent();
	}

	/**
	 * Return the port type for this client.
	 *
	 * @param agent The agent to use to create the port.
	 * @return PortType associated with this client.
	 */
	@Override
	protected PortType getServicePort(ProductHierarchyManagementServiceServiceagent agent) {
		return agent.getProductHierarchyManagementService();
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
	 * Sets the URI to access product hierarchy management service. This is primarily used for testing.
	 *
	 * @param uri The URI to access product hierarchy management service.
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
}
