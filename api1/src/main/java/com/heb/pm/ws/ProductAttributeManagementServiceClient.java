package com.heb.pm.ws;

import com.heb.pm.entity.*;
import com.heb.pm.productDetails.product.eCommerceView.ProductECommerceViewService;
import com.heb.pm.productDetails.product.eCommerceView.ProductECommerceViewUtil;
import com.heb.pm.productDetails.product.taxonomy.ProductAttribute;
import com.heb.pm.productDetails.product.taxonomy.ProductAttributeValue;
import com.heb.pm.repository.*;
import com.heb.util.controller.UserInfo;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.ProductAttributeManagementServicePort;
import com.heb.xmlns.ei.ProductAttributeManagementServiceServiceagent;
import com.heb.xmlns.ei.copyecommerceattributes.CopyeCommerceattributes;
import com.heb.xmlns.ei.masterdataextnattribute.MasterDataExtnAttribute;
import com.heb.xmlns.ei.prodattrovrd.ProdAttrOvrd;
import com.heb.xmlns.ei.proddiscthrh.ProdDiscThrh;
import com.heb.xmlns.ei.proditmvar.ProdItmVar;
import com.heb.xmlns.ei.prodpkvariation.ProdPackVariation;
import com.heb.xmlns.ei.prodsalsrstr.ProdSalsRstr;
import com.heb.xmlns.ei.prodtrashcan.ProdTrashCan;
import com.heb.xmlns.ei.productattributeresponse.Detail;
import com.heb.xmlns.ei.productattributeresponse.ProductAttributeResponse;
import com.heb.xmlns.ei.productmanagement.update_productattribute_reply.UpdateProductAttributeReply;
import com.heb.xmlns.ei.productmanagement.update_productattribute_request.UpdateProductAttributeRequest;
import com.heb.xmlns.ei.productmarketclaim.ProductMarketClaim;
import com.heb.xmlns.ei.productscanimageurl.ProductScanImageUrl;
import com.heb.xmlns.ei.productscanntrntl.ProductScanNtrntl;
import com.heb.xmlns.ei.prodwarn.ProdWarn;
import com.tibco.schemas.productattributemanagementservice.shared.resources.schema.definitions.custom.schema.ProductScanMapPrice;
import com.tibco.schemas.productattributemanagementservice.shared.resources.schema.definitions.custom.schema.UpdateExtAttributeBatchUploadRequest;
import com.tibco.schemas.productattributemanagementservice.shared.resources.schema.definitions.custom.schema1.UpdateExtAttributeBatchUploadReply;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;

/**
 * Provides access to service endpoint for product attribute management service.
 */
@Service
public class ProductAttributeManagementServiceClient extends BaseWebServiceClient
		<ProductAttributeManagementServiceServiceagent, ProductAttributeManagementServicePort> {

	private static final Logger logger = LoggerFactory.getLogger(ProductAttributeManagementServiceClient.class);

	private static final String DEFAULT_TRUE_STRING = "Y";
	private static final String DEFAULT_FALSE_STRING = "N";
	private static final String STATUS_APPROVED = "A";
	private static final String STATUS_REJECTED = "R";
	private static final String STATUS_FOR_REVIEW = "S";
	private static final String UPC_ITEM_KEY_CODE = "UPC  ";
	private static final String PRODUCT_ITEM_KEY_CODE = "PROD ";
	private static final String EMPTY_STRING = "";
	private static final String DEFAULT_WORK_ID = "0";
	private static final int MAX_IMG_STATUS_REASON = 80;
	private static final String MAINFRAME_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSS";
	private static final String SINGLE_SPACE = " ";

	private static final String BEGIN_PUBLISH_ECOMMERCE_DATA_LOG = "User %s is publishing eCommerce data to Heb Com.";
	private static final String FINISHED_PUBLISH_ECOMMERCE_DATA_LOG = "User %s publish eCommerce data to Heb Com have been completed.";
	private static final String ERROR_INVALID_PRODUCT_ID_FOR_UPDATE_WORK_STATUS = "Invalid product id for updating work status.";
	private static final String BEGIN_UPDATE_ECOMMERCE_VIEW_FOR_SOURCE27_LOG = "User %s is begining to update eCommerce view for source 27";
	private static final String FINISHED_UPDATE_ECOMMERCE_VIEW_FOR_SOURCE27_LOG = "User %s  update eCommerce view requests for source27 have been completed.";

	private static final String TIME_COMPONENT = " 00:00:00.000000";
	private static final String DATE_FORMAT_YYYYMMDD="yyy-MM-dd";
	private static final String DYNAMIC_ATTRIBUTE_DECIMAL_FORMAT = "%.4f";

	private static final String BEGIN_UPDATE_LOG = "User %s is begining to update images";
	private static final String UPDATE_LOG = "User %s is updating image info with upc %s, and sequence number %s";
	private static final String ERROR_WEB_SERVICE_UPDATE_RESPONSE = "Error with this update: %s.";
	private static final String FINISHED_UPDATE_LOG = "User %s update images requests have been completed";
	private static final String BEGIN_UPDATE_BANNER_LOG = "User %s is beginning to update image banners";
	private static final String FINISHED_UPDATE_BANNER_LOG = "User %s  update image banners requests have been completed";
	private static final String BEGIN_UPDATE_TIER_PRICING_LOG = "User %s is beginning to update the Tier Pricing for product: %s";
	private static final String FINISHED_UPDATE_TIER_PRICING_LOG = "User %s  update to product's %s tier pricing list requests have been completed";
	private static final String ERROR_WEB_SERVICE_UPLOAD_RESPONSE = "Error with this upload: %s.";
	private static final String BEGIN_PRODUCT_RESTRICTION_LOG = "User %s is beginning to update the Restrictions for product: %s";
	private static final String FINISHED_UPDATE_PRODUCT_RESTRICTION_LOG = "User %s  update to product's %s restrictions list requests have been completed";
	private static final String BEGIN_MASTER_DATA_EXTENSION_AND_PRODUCT_OVERRIDE_LOG = "User %s is beginning to update Master Data Extension Attribute & Product Attribute Override for product %s upc %s";
	private static final String FINISH_MASTER_DATA_EXTENSION_AND_PRODUCT_OVERRIDE_LOG = "User %s finish updating Master Data Extension Attribute & Product Attribute Override for product %s upc %s";
	private static final String BEGIN_UPDATE_TAGS_AND_SPECS_LOG = "User %s is beginning to update the Tags and Specs for product: %s";
	private static final String COMPLETE_UPDATE_TAGS_AND_SPECS_LOG = "User %s has completed an update the Tags and Specs for product: %s";
	private static final String PRODUCT_MARKETING_CLAIM_UPDATE_BEGIN="User %s is trying to update the marketing claim for product with id %s";
	private static final String PRODUCT_MARKETING_CLAIM_UPDATE_COMPLETE="User %s has completed an update the marketing claim for product with id %s";
	private static final String PRODUCT_TRASH_CAN_UPDATE_BEGIN="User %s is trying to update the trash can for product with id %s";
	private static final String PRODUCT_TRASH_CAN_UPDATE_COMPLETE="User %s has completed an update the trash can for product with id %s";

	private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * Action code constant.
	 */
	public enum ACTION_CODE {
		/**
		 * Action cd delete action code.
		 */
		ACTION_CD_DELETE("D"), /**
		 * Action cd add action code.
		 */
		ACTION_CD_ADD("A"), /**
		 * Action cd update action code.
		 */
		ACTION_CD_UPDATE(""), /**
		 * Action cd alternate action code.
		 */
		ACTION_CD_ALTERNATE("P"), /**
		 * Action cd primary action code.
		 */
		ACTION_CD_PRIMARY("");

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

	@Value("${productAttributeManagementService.uri}")
	private String uri;

	@Value("${app.sourceSystemId}")
	private int defaultSourceSystemCode;

	@Autowired
	private ProductTrashCanRepository productTrashCanRepository;
	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;
	@Autowired
	private ProductOnlineRepository productOnlineRepository;
	@Autowired
	private ProductPkVariationRepository productPkVariationRepository;
	@Autowired
	private MasterDataExtensionAttributeRepository masterDataExtensionAttributeRepository;
	@Autowired
	private ProductAttributeOverwriteRepository productAttributeOverwriteRepository;
	@Autowired
	private TargetSystemAttributePriorityRepository targetSystemAttributePriorityRepository;
	@Autowired
	private UserInfo userInfo;

	private void updateProductAttribute(UpdateProductAttributeRequest request) {
		try {
			UpdateProductAttributeReply reply = this.getPort().updateProductAttribute(request);
			StringBuilder errorMessage = new StringBuilder("");
			if (!reply.getProductAttributeResponse().isEmpty()) {
				for (ProductAttributeResponse response : reply.getProductAttributeResponse()) {
					for (Detail detail : response.getDetail()) {
						if (!detail.getSucessFlag().equals(DEFAULT_TRUE_STRING)) {
							//If the message has a table name that part of the string will be removed
							int tableNameIndex = detail.getReturnMsg().indexOf(':');
							if(tableNameIndex > 0) {
								errorMessage.append(detail.getReturnMsg().substring(tableNameIndex+2));
							} else {
								errorMessage.append(detail.getReturnMsg());
							}
						}
					}
				}
				if (!errorMessage.toString().equals(StringUtils.EMPTY)) {
					throw new SoapException(errorMessage.toString());
				}
			}
		} catch (Exception e) {
			throw new SoapException(String.format(ERROR_WEB_SERVICE_UPDATE_RESPONSE, e.getMessage()));
		}
	}

	/**
	 * Writes a list of MasterDataExtensionAttributes to the DB.
	 *
	 * @param masterDataExtensionAttributes The list of MasterDataExtensionAttribute to write.
	 */
	public void updateMasterDataExtendedAttribute(List<? extends MasterDataExtensionAttribute> masterDataExtensionAttributes) throws CheckedSoapException{

		if (masterDataExtensionAttributes.isEmpty()) {
			return;
		}

		String workId = this.getWorkId().toString();

		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(workId);

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDD + TIME_COMPONENT);

		for (MasterDataExtensionAttribute masterDataExtensionAttribute : masterDataExtensionAttributes) {
			MasterDataExtnAttribute mdea = new MasterDataExtnAttribute();
			mdea.setWORKID(workId);
			switch (masterDataExtensionAttribute.getAction()) {
				case MasterDataExtensionAttribute.INSERT:
					mdea.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());
					mdea.setCRE8UID(masterDataExtensionAttribute.getCreateUserId());
//					if (masterDataExtensionAttribute.getCreateTime() != null) {
//						mdea.setCRE8TS(masterDataExtensionAttribute.getCreateTime().format(dateTimeFormatter));
//					} else {
//						mdea.setCRE8TS(LocalDateTime.now().format(dateTimeFormatter));
//					}
					break;
				case MasterDataExtensionAttribute.UPDATE:
					mdea.setACTIONCD(ACTION_CODE.ACTION_CD_UPDATE.getValue());
			}
			mdea.setATTRID(masterDataExtensionAttribute.getKey().getAttributeId().toString());
			if(StringUtils.isNotBlank(masterDataExtensionAttribute.getAttributeValueText())) {
				mdea.setATTRVALTXTTEXT(masterDataExtensionAttribute.getAttributeValueText());
			}
			if(masterDataExtensionAttribute.getAttributeValueNumber() != null) {
				mdea.setATTRVALNBR(String.format(DYNAMIC_ATTRIBUTE_DECIMAL_FORMAT, masterDataExtensionAttribute.getAttributeValueNumber()));
			}
			if(masterDataExtensionAttribute.getAttributeValueDate() != null) {
				mdea.setATTRVALDT(masterDataExtensionAttribute.getAttributeValueDate().toString());
			}
			if(masterDataExtensionAttribute.getAttributeValueTime() != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				mdea.setATTRVALTS(masterDataExtensionAttribute.getAttributeValueTime().format(formatter));
			}
			mdea.setKEYID(Long.toString(masterDataExtensionAttribute.getKey().getId()));
			mdea.setITMPRODKEYCD(masterDataExtensionAttribute.getKey().getItemProdIdCode());
			mdea.setSEQNBR(Long.toString(masterDataExtensionAttribute.getKey().getSequenceNumber()));
			mdea.setDTASRCSYS(masterDataExtensionAttribute.getKey().getDataSourceSystem().toString());
			mdea.setLSTUPDTUID(masterDataExtensionAttribute.getLastUdateUserId());
//			if (masterDataExtensionAttribute.getLstUpdtTs() != null) {
//				mdea.setLSTUPDTTS(masterDataExtensionAttribute.getLstUpdtTs().format(dateTimeFormatter));
//			} else {
//				mdea.setLSTUPDTTS(LocalDateTime.now().format(dateTimeFormatter));
//			}
			// These all depend on what is set.
			if (masterDataExtensionAttribute.getAttributeCodeId() != null) {
				mdea.setATTRCDID(masterDataExtensionAttribute.getAttributeCodeId().toString());
			}
			request.getMasterDataExtnAttribute().add(mdea);
		}
		try {
			this.updateProductAttribute(request);
		} catch (SoapException e) {
			throw new CheckedSoapException(e.getLocalizedMessage());
		}
	}

	/**
	 * Update shipping restrictions.
	 *
	 * @param updateList the update list
	 * @param userId     the user id
	 */
	public void updateShippingRestrictions(List<ProductRestrictions> updateList, String userId) {

		List<ProdSalsRstr> prodSalsRstrList = new ArrayList<>();
		String workId = this.getWorkId().toString();

		for(ProductRestrictions productRestriction : updateList) {
			ProdSalsRstr prodSalsRstr = new ProdSalsRstr();
			prodSalsRstr.setACTIONCD(productRestriction.getActionCode());
			prodSalsRstr.setPRODID(String.valueOf(productRestriction.getKey().getProdId()));
			prodSalsRstr.setSALSRSTRCD(String.valueOf(productRestriction.getKey().getRestrictionCode().trim()));
			prodSalsRstr.setWORKID(workId);
			prodSalsRstr.setVCLSTUPDTUID(userId);

			prodSalsRstrList.add(prodSalsRstr);
		}

		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(workId.trim());
		request.getProdSalsRstr().addAll(prodSalsRstrList);

		this.updateProductAttribute(request);

	}

	/**
	 * Update product state warnings.
	 *
	 * @param updateList the update list
	 * @param prodId     the prod id
	 * @param userId     the user id
	 */
	public void updateProductStateWarnings(List<ProductStateWarning> updateList, Long prodId, String userId) {
		List<ProdWarn> prodWarnList = new ArrayList<>();
		String workId = this.getWorkId().toString();

		for(ProductStateWarning productStateWarning : updateList) {
			ProdWarn prodWarn = new ProdWarn();
			prodWarn.setACTIONCD(productStateWarning.getActionCode());
			prodWarn.setPRODID(String.valueOf(String.valueOf(prodId)));
			prodWarn.setSTCD(String.valueOf(productStateWarning.getKey().getStateCode()));
			prodWarn.setSTPRODWARNCD(String.valueOf(productStateWarning.getKey().getWarningCode()));
			prodWarn.setWORKID(workId);
			prodWarn.setLSTUPDTUID(userId);

			prodWarnList.add(prodWarn);
		}

		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(workId.trim());
		request.getProdWarn().addAll(prodWarnList);

		this.updateProductAttribute(request);
	}

	/**
	 * Update image info.
	 *
	 * @param imageInfoList the image info list
	 * @param userId        the user id
	 */
	public void updateImageInfo(List<ProductScanImageURI> imageInfoList, String userId) {
		ProductScanImageUrl webserviceImageInfo = new ProductScanImageUrl();
		ProductAttributeManagementServiceClient.logger.info(String.format(BEGIN_UPDATE_LOG, userId));
		for (ProductScanImageURI imageInfo : imageInfoList) {
			ProductAttributeManagementServiceClient.logger.info(String.format(UPDATE_LOG, userId, imageInfo.getKey().getId(), imageInfo.getKey().getSequenceNumber()));
			//Sets up image meta data update
			webserviceImageInfo.setACTIONCD("");
			webserviceImageInfo.setSCNCDID(Long.toString(imageInfo.getKey().getId()));
			webserviceImageInfo.setSEQNBR(Long.toString(imageInfo.getKey().getSequenceNumber()));
			webserviceImageInfo.setIMGALTTXT((imageInfo.getAltTag()!=null && StringUtils.isBlank(imageInfo.getAltTag()))?SINGLE_SPACE:imageInfo.getAltTag());
			webserviceImageInfo.setIMGCATCD(imageInfo.getImageCategoryCode());
			webserviceImageInfo.setIMGPRTYCD(imageInfo.getImagePriorityCode());
			if (imageInfo.isActiveOnline() != null) {
				webserviceImageInfo.setACTVONLINSW(getActiveOnlineString(imageInfo.isActiveOnline()));
			} else {
				webserviceImageInfo.setACTVONLINSW(null);
			}
			webserviceImageInfo.setIMGSTATCD(imageInfo.getImageStatusCode());
			webserviceImageInfo.setLSTUPDTUID(userId);
			webserviceImageInfo.setACTVONLINSW(getActiveOnlineString(imageInfo.isActiveOnline()));
			webserviceImageInfo = updateImageStatus(imageInfo.getImageStatusCode(), webserviceImageInfo);
			String workId = this.getWorkId().toString();
			webserviceImageInfo.setWORKID(workId);
			webserviceImageInfo.setACTIONCD("");
			UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
			request.getProductScanImageUrl().add(webserviceImageInfo);
			request.setTrackingNbr(workId.trim());
			request.setAuthentication(this.getAuthentication());
			this.updateProductAttribute(request);
		}
		ProductAttributeManagementServiceClient.logger.info(String.format(FINISHED_UPDATE_LOG, userId));
	}


	/**
	 * Update destinations.
	 *
	 * @param upc            the upc
	 * @param sequenceNumber the sequence number
	 * @param actionCode     the action code
	 * @param channel        the channel
	 * @param activeSwitch   the active switch
	 * @param userId         the user id
	 */
	public void updateDestinations(String upc, String sequenceNumber, String actionCode, String channel, Boolean activeSwitch, String userId) {
		com.heb.xmlns.ei.productscanimagebanner.ProductScanImageBanner webserviceImageBanner =
				new com.heb.xmlns.ei.productscanimagebanner.ProductScanImageBanner();
		ProductAttributeManagementServiceClient.logger.info(String.format(BEGIN_UPDATE_BANNER_LOG, userId));
		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		String workId = this.getWorkId().toString();
		request.setTrackingNbr(workId.trim());
		request.setAuthentication(this.getAuthentication());
		webserviceImageBanner.setACTIONCD(actionCode);
		webserviceImageBanner.setLSTUPDTUID(userId);
		webserviceImageBanner.setSEQNBR(sequenceNumber);
		webserviceImageBanner.setSCNCDID(upc);
		webserviceImageBanner.getSALSCHNLCD().add(channel);
		webserviceImageBanner.setWORKID(workId);
		request.getProductScanImageBanner().add(webserviceImageBanner);
		if (activeSwitch != null) {
			webserviceImageBanner.setACTVSW(getActiveOnlineString(activeSwitch));
		} else {
			webserviceImageBanner.setACTVSW(null);
		}
		this.updateProductAttribute(request);
		ProductAttributeManagementServiceClient.logger.info(String.format(FINISHED_UPDATE_BANNER_LOG, userId));
	}

    /**
     * Publish eCommerce data to HEB Com.
     *
     * @param product the ECommerceViewDetails object.
     * @param userId  the user id.
     */
    public void publishECommerceViewDataToHebCom(ECommerceViewDetails product, String userId) {
        ProductAttributeManagementServiceClient.logger.info(String.format(BEGIN_PUBLISH_ECOMMERCE_DATA_LOG, userId));
        UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
        request.setAuthentication(this.getAuthentication());
        request.setTrackingNbr(EMPTY_STRING);
        // Common publish.
        request.getCopyeCommerceattributes().add(this.createCopyeCommerceAttributes(product.getScanCodeId(), 0,
                product.getSalsChnlCd().trim(), userId, product.getAlertId()));
        this.updateProductAttribute(request);
        if (product.getProductId() > 0) {
            // Update work status.
            this.updateProductTrashCan(product.getProductId());
        } else {
            throw new SoapException(ERROR_INVALID_PRODUCT_ID_FOR_UPDATE_WORK_STATUS);
        }
        ProductAttributeManagementServiceClient.logger.info(String.format(FINISHED_PUBLISH_ECOMMERCE_DATA_LOG, userId));
    }

	/**
	 * Create createCopyeCommerceAttributes for publish.
	 *
	 * @param upc the scan code.
	 * @param logicAttributeId the logic attribute id.
	 * @param salsChnlCd the sales channel code.
	 * @param userId the user id.
	 * @return the CopyeCommerceattributes object.
	 */
	private CopyeCommerceattributes createCopyeCommerceAttributes(long upc, long logicAttributeId, String salsChnlCd, String userId, Long alertId){
		String workId = this.getWorkId().toString();
		CopyeCommerceattributes copyeCommerceattributes = new CopyeCommerceattributes();
		copyeCommerceattributes.setWORKID(workId);
		copyeCommerceattributes.setACTIONCD(EMPTY_STRING);
		copyeCommerceattributes.setKEYID(BigDecimal.valueOf(upc));
		copyeCommerceattributes.setTYPECD(StringUtils.trim(UPC_ITEM_KEY_CODE));
		copyeCommerceattributes.setTRGTSRCSYS(BigDecimal.valueOf(ProductECommerceViewService.SourceSystemNumber.ECOMMERCE_SOURCE_SYSTEM_NUMBER.getValue()));
		copyeCommerceattributes.setCOPYLOGICATTRID(BigDecimal.valueOf(logicAttributeId));
		copyeCommerceattributes.setUSERID(userId);
		copyeCommerceattributes.setSALSCHNLCD(salsChnlCd.trim());
		if(alertId!=null && alertId>0)
			copyeCommerceattributes.setTASKALERTID(alertId.toString());
		return copyeCommerceattributes;
	}
	/**
	 * Update work status. This method will be called after publish success.
	 *
	 * @param productId the product id.
	 */
	private void updateProductTrashCan(Long productId) {
		// Update work status
		String actionCode = ACTION_CODE.ACTION_CD_UPDATE.getValue();
		if(productTrashCanRepository.findOne(productId)== null) {
			actionCode = ACTION_CODE.ACTION_CD_ADD.getValue();
		}
		ProdTrashCan productTrashCan = new ProdTrashCan();
		productTrashCan.setACTIONCD(actionCode);
		productTrashCan.setPRODID(String.valueOf(productId));
		productTrashCan.setPBLSHTOECOMMCD(EMPTY_STRING);
		productTrashCan.setONLINSALEONLYSW(DEFAULT_TRUE_STRING);
		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(String.valueOf(0));
		request.getProdTrashCan().add(productTrashCan);
		this.updateProductAttribute(request);
	}

	/**
	 * Update nutrition fact information.
	 *
	 * @param eCommerceViewAttributePriorities - The list of ECommerceViewAttributePriorities
	 * @param productId                        - The product id
	 * @param userId                           - The user id
	 */
	public void updateNutritionFactInformation(List<ECommerceViewAttributePriorities> eCommerceViewAttributePriorities, Long productId, String userId) {
		ProductAttributeManagementServiceClient.logger.info(String.format(BEGIN_UPDATE_ECOMMERCE_VIEW_FOR_SOURCE27_LOG, userId));
		List<ProdAttrOvrd> prodAttrOvrds = new ArrayList<ProdAttrOvrd>();
		for (ECommerceViewAttributePriorities eCommerceViewAttributePriority : eCommerceViewAttributePriorities) {
			ProdAttrOvrd prodAttrOvrd = new ProdAttrOvrd();
			if (eCommerceViewAttributePriority.getActionCode() != null) {
				prodAttrOvrd.setACTIONCD(eCommerceViewAttributePriority.getActionCode());
			}
			prodAttrOvrd.setTRGTSYSID(ProductECommerceViewService.SourceSystemNumber.ECOMMERCE_SOURCE_SYSTEM_NUMBER.getValue().toString());
			prodAttrOvrd.setITMPRODKEYCD(PRODUCT_ITEM_KEY_CODE.trim());
			prodAttrOvrd.setKEYID(BigDecimal.valueOf(productId));
			prodAttrOvrd.setLOGICATTRID(ProductECommerceViewService.LogicAttributeCode.NUTRITION_FACTS_LOGIC_ATTRIBUTE_ID.getValue().toString());
			prodAttrOvrd.setDTASRCSYSID(eCommerceViewAttributePriority.getSourceSystemId().toString());
			prodAttrOvrd.setPHYATTRID(eCommerceViewAttributePriority.getPhysicalAttributeId().toString());
			prodAttrOvrd.setRLSHPGRPTYPCD(eCommerceViewAttributePriority.getRelationshipGroupTypeCode());
			prodAttrOvrds.add(prodAttrOvrd);
		}
		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(String.valueOf(0));
		request.getProdAttrOvrd().addAll(prodAttrOvrds);
		this.updateProductAttribute(request);
		ProductAttributeManagementServiceClient.logger.info(String.format(FINISHED_UPDATE_ECOMMERCE_VIEW_FOR_SOURCE27_LOG, userId));
	}

	/**
	 * Returns the action code for MasterDataExtnAttribute;
	 *
	 * @param attributeId    attribute id.
	 * @param upc            the scan code.
	 * @param sourceSystemId the source system id.
	 * @return ACTION_CD_UPDATE if it existed in database, or ACTION_CD_ADD.
	 */
	private String getActionCodeForMasterDataExtnAttribute(Long attributeId, Long upc, long sourceSystemId) {
		MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
				.findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(attributeId, upc,
						UPC_ITEM_KEY_CODE, sourceSystemId);
		if (masterDataExtensionAttribute != null) {
			return ACTION_CODE.ACTION_CD_UPDATE.getValue();
		}
		return ACTION_CODE.ACTION_CD_ADD.getValue();
	}


	/**
	 * Helper method to convert the boolean to a string
	 *
	 * @param value the current Boolean for the object
	 * @return what the table expects
	 */
	private String getActiveOnlineString(Boolean value) {
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

	private ProductScanImageUrl updateImageStatus(String status, ProductScanImageUrl productScanImageUrl) {
		if (status != null) {
			if (status.equals(STATUS_APPROVED)) {
				productScanImageUrl.setIMGACPTABLESW(DEFAULT_TRUE_STRING);
				productScanImageUrl.setIMGSTATCD(STATUS_APPROVED);
			} else if (status.equals(STATUS_FOR_REVIEW)) {
				productScanImageUrl.setIMGACPTABLESW(DEFAULT_FALSE_STRING);
				productScanImageUrl.setIMGSTATCD(STATUS_REJECTED);
			} else if (status.equals(STATUS_REJECTED)) {
				productScanImageUrl.setIMGACPTABLESW(STATUS_REJECTED);
				productScanImageUrl.setIMGSTATCD(STATUS_REJECTED);
			}
		}
		return productScanImageUrl;
	}

	/**
	 * This method will create an update for the products tier pricing list
	 *
	 * @param tierPricing the tier pricing information to be updated or deleted
	 * @param userId      the user making the request
	 * @param actionCode  that action to be preformed either "" for insert or "D" for delete
	 */
	public void updateTierPricingUnit(TierPricing tierPricing, String userId, String actionCode) {
		ProductAttributeManagementServiceClient.logger.info(String.format(BEGIN_UPDATE_TIER_PRICING_LOG, userId, tierPricing.getKey().getProdId()));
		ProdDiscThrh webserviceTierPricing = new ProdDiscThrh();
		webserviceTierPricing.setWORKID(this.getWorkId().toString());
		webserviceTierPricing.setACTIONCD(actionCode);
		webserviceTierPricing.setPRODID(tierPricing.getKey().getProdId().toString());
		webserviceTierPricing.setEFFTS(tierPricing.getKey().getEffectiveTimeStamp().toString() + TIME_COMPONENT);
		webserviceTierPricing.setMINDISCTHRHQTY(tierPricing.getKey().getDiscountQuantity().toString());
		webserviceTierPricing.setTHRHDISCAMT(tierPricing.getDiscountValue().toString());
		webserviceTierPricing.setTHRHDISCTYPCD(tierPricing.getDiscountTypeCode());
		webserviceTierPricing.setCRE8TS(LocalDateTime.now().toString());
		webserviceTierPricing.setCRE8UID(userId);
		webserviceTierPricing.setLSTUPDTTS(userId);
		webserviceTierPricing.setLSTUPDTTS(LocalDateTime.now().toString());

		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		String workId = getWorkId().toString().trim();

		request.setAuthentication(this.getAuthentication());
		request.setTrackingNbr(workId);
		request.getProdDiscThrh().add(webserviceTierPricing);
		this.updateProductAttribute(request);

		ProductAttributeManagementServiceClient.logger.info(String.format(FINISHED_UPDATE_TIER_PRICING_LOG, userId, tierPricing.getKey().getProdId()));
	}

	/**
	 * This method will create a single restriction update
	 *
	 * @param update     the restriction to be updated
	 * @param actionCode the kind of update to be done
	 * @param userId     the user requesting the update
	 */
	public void updateProductRestriction(ProductRestrictions update, String actionCode, String userId) {
		ProductAttributeManagementServiceClient.logger.info(String.format(BEGIN_PRODUCT_RESTRICTION_LOG, userId, update.getKey().getProdId()));
		String workId = this.getWorkId().toString();
		ProdSalsRstr webserviceProductRestriction = new ProdSalsRstr();
		webserviceProductRestriction.setWORKID(workId);
		webserviceProductRestriction.setACTIONCD(actionCode);
		webserviceProductRestriction.setPRODID(Long.toString(update.getKey().getProdId()));
		webserviceProductRestriction.setSALSRSTRCD(update.getRestriction().getRestrictionCode().trim());
		webserviceProductRestriction.setVCLSTUPDTUID(userId);

		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		request.setTrackingNbr(workId);
		request.setAuthentication(this.getAuthentication());
		request.getProdSalsRstr().add(webserviceProductRestriction);

		this.updateProductAttribute(request);
		ProductAttributeManagementServiceClient.logger.info(String.format(FINISHED_UPDATE_PRODUCT_RESTRICTION_LOG, userId, update.getKey().getProdId()));
	}

	/**
	 * Upload template for brick name.
	 *
	 * @param brinkName   the name of brink.
	 * @param description the description for upload file.
	 * @param data        uploaded file.
	 * @param userId      the user id
	 * @return upload results.
	 * @author vn55306
	 */
	public Long updateExtAttributeBatchUpload(String brinkName, String description, byte[] data, String userId) {
		Long trackingid = null;
		try {
			UpdateExtAttributeBatchUploadRequest updateExtAttributeBatchUploadRequest = new UpdateExtAttributeBatchUploadRequest();
			updateExtAttributeBatchUploadRequest.setUserId(userId);
			updateExtAttributeBatchUploadRequest.setExcelTemplate(brinkName);
			updateExtAttributeBatchUploadRequest.setExcelBinaryFormat(data);
			updateExtAttributeBatchUploadRequest.setFilename(description);
			updateExtAttributeBatchUploadRequest.setDtaSourceSystemId(BigInteger.valueOf(defaultSourceSystemCode));
			UpdateExtAttributeBatchUploadReply updateExtAttributeBatchUploadReply = this.getPort().updateExtAttributeBatchUpload(updateExtAttributeBatchUploadRequest);
			if (updateExtAttributeBatchUploadReply != null && updateExtAttributeBatchUploadReply.getTrackingNbr().length() > 0) {
				trackingid = Long.valueOf(updateExtAttributeBatchUploadReply.getTrackingNbr());
			}
		} catch (Exception e) {
			throw new SoapException(String.format(
					ProductAttributeManagementServiceClient.ERROR_WEB_SERVICE_UPLOAD_RESPONSE, e.getMessage()));
		}
		return trackingid;
	}

	/**
	 * Get product attribute override entity by ECommerceViewAttributePriorities entity and productId.
	 *
	 * @param attributePriority the ECommerceViewAttributePriorities entity to be updated.
	 * @param productId         the product id to be updated.
	 * @return the product attribute override entity.
	 */
	public ProdAttrOvrd getProductAttributeOverride(ECommerceViewAttributePriorities attributePriority, long productId) {

		ProdAttrOvrd productAttributeOverride = new ProdAttrOvrd();
		productAttributeOverride.setWORKID(this.getWorkId().toString());
		productAttributeOverride.setACTIONCD(attributePriority.getActionCode());
		productAttributeOverride.setTRGTSYSID(
				ProductECommerceViewService.SourceSystemNumber.ECOMMERCE_SOURCE_SYSTEM_NUMBER.getValue().toString());
		productAttributeOverride.setITMPRODKEYCD(ProductAttributeManagementServiceClient.PRODUCT_ITEM_KEY_CODE.trim());
		productAttributeOverride.setKEYID(BigDecimal.valueOf(productId));
		productAttributeOverride.setLOGICATTRID(attributePriority.getLogicAttributeId().toString());
		productAttributeOverride.setDTASRCSYSID(attributePriority.getSourceSystemId().toString());
		productAttributeOverride.setPHYATTRID(attributePriority.getPhysicalAttributeId().toString());
		TargetSystemAttributePriority targetSystemAttributePriority = this.targetSystemAttributePriorityRepository.
				findOneByKeyLogicalAttributeIdAndKeyDataSourceSystemIdAndKeyPhysicalAttributeIdOrderByAttributePriorityNumberAsc(attributePriority.getLogicAttributeId().intValue(), attributePriority.getSourceSystemId(), attributePriority.getPhysicalAttributeId());
		if(targetSystemAttributePriority != null){
			productAttributeOverride.setRLSHPGRPTYPCD(StringUtils.trim(targetSystemAttributePriority.getKey()
					.getRelationshipGroupTypeCode()));
		}else{
			productAttributeOverride.setRLSHPGRPTYPCD(ProductECommerceViewService.ATTRIBUTE_RELATIONSHIP_GROUP_TYPE_CODE);
		}
		return productAttributeOverride;
	}

	/**
	 * Get master data extension entity by ECommerceViewAttributePriorities entity and upc.
	 *
	 * @param attributePriority the ECommerceViewAttributePriorities entity to be updated.
	 * @param scanCodeId        the upc to be updated.
	 * @param userId            the user id that request updating.
	 * @return the master data extension attribute entity.
	 */
	public MasterDataExtnAttribute getMasterDataExtensionAttribute(ECommerceViewAttributePriorities attributePriority, long scanCodeId,
																   String userId) {

		MasterDataExtnAttribute masterDataExtnAttribute = new MasterDataExtnAttribute();
		masterDataExtnAttribute.setWORKID(this.getWorkId().toString());
		masterDataExtnAttribute.setACTIONCD(this.getActionCodeForMasterDataExtnAttribute(
				attributePriority.getPhysicalAttributeId(), scanCodeId, attributePriority.getSourceSystemId()));
		masterDataExtnAttribute.setATTRID(attributePriority.getPhysicalAttributeId().toString());
		masterDataExtnAttribute.setKEYID(String.valueOf(scanCodeId));
		masterDataExtnAttribute.setITMPRODKEYCD(ProductAttributeManagementServiceClient.UPC_ITEM_KEY_CODE.trim());
		masterDataExtnAttribute.setSEQNBR(String.valueOf(0));
		masterDataExtnAttribute.setDTASRCSYS(attributePriority.getSourceSystemId().toString());
		masterDataExtnAttribute.setATTRVALTXTTEXT(attributePriority.getContent().trim());
		if (attributePriority.getPostedDate() != null) {
			masterDataExtnAttribute.setATTRVALTS(attributePriority.getPostedDate().toString());
		}
		if (ACTION_CODE.ACTION_CD_ADD.getValue().equals(attributePriority.getActionCode())) {
			masterDataExtnAttribute.setCRE8UID(userId);
			masterDataExtnAttribute.setCRE8TS(attributePriority.getCreatedDate().toString());
		}
		masterDataExtnAttribute.setLSTUPDTUID(userId);
		masterDataExtnAttribute.setLSTUPDTTS(attributePriority.getCreatedDate().toString());

		return masterDataExtnAttribute;
	}

	/**
	 * Call webservice to update Master data extension attribute and Product attribute override.
	 *
	 * @param eCommerceViewDetails the ECommerceViewDetails entity to update.
	 * @param userId               the user id to request update.
	 */
	public void updateMasterDataExtensionAttributeAndProductAttributeOverride(ECommerceViewDetails eCommerceViewDetails,
																			  String userId) {
		long productId = eCommerceViewDetails.getProductId();
		long scanCodeId = eCommerceViewDetails.getScanCodeId();
		ProductAttributeManagementServiceClient.logger.info(String.format(BEGIN_MASTER_DATA_EXTENSION_AND_PRODUCT_OVERRIDE_LOG,
				userId, productId, scanCodeId));

		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		request.setTrackingNbr(this.getWorkId().toString());
		request.setAuthentication(this.getAuthentication());

		if (eCommerceViewDetails.getIngredientsToDelete() != null) {
			ProdAttrOvrd ingredientToDelete = this.getProductAttributeOverride(
					eCommerceViewDetails.getIngredientsToDelete(), eCommerceViewDetails.getProductId());
			request.getProdAttrOvrd().add(ingredientToDelete);
		}
		if (eCommerceViewDetails.getIngredients() != null) {
			ProdAttrOvrd ingredient = this.getProductAttributeOverride(
					eCommerceViewDetails.getIngredients(), eCommerceViewDetails.getProductId());
			request.getProdAttrOvrd().add(ingredient);
		}
		if (eCommerceViewDetails.getIngredientsAttribute() != null) {
			MasterDataExtnAttribute ingredientAttribute = this.getMasterDataExtensionAttribute(
					eCommerceViewDetails.getIngredientsAttribute(), eCommerceViewDetails.getScanCodeId(), userId);
			request.getMasterDataExtnAttribute().add(ingredientAttribute);
		}

		this.updateProductAttribute(request);
		ProductAttributeManagementServiceClient.logger.info(String.format(FINISH_MASTER_DATA_EXTENSION_AND_PRODUCT_OVERRIDE_LOG,
				userId, productId, scanCodeId));
	}

	/**
	 * This method is for updating Tags and Specs attributes
	 *
	 * @param updates the changes to be made
	 * @param userId  the user requesting the change
	 */
	public void updateTagsAndSpecsAttribute(List<TagsAndSpecsAttribute> updates, String userId){
		ProductAttributeManagementServiceClient.logger.info(String.format(BEGIN_UPDATE_TAGS_AND_SPECS_LOG, userId, updates.get(0).getProdId()));
		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		request.setTrackingNbr(this.getWorkId().toString());
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		for(TagsAndSpecsAttribute update: updates) {
			if(update.getMultiValueFlag()){
				clearAllPrevious(update, update.getAttributeId(), userId);
			}
			if (update.getValues().size() > 0) {
				for (TagsAndSpecsValue value : update.getValues()) {
					MasterDataExtnAttribute webserviceMasterDataExtnAttribute = createTagsAndSpecsAttributeUpdate(update, value, userId, workId, value.getName(), value.getSequenceNumber());
					request.getMasterDataExtnAttribute().add(webserviceMasterDataExtnAttribute);
				}
			} else {
				MasterDataExtnAttribute webserviceMasterDataExtnAttribute = createTagsAndSpecsAttributeUpdate(update, null, userId, workId, update.getAlternativeAttributeCodeText(), 0);
				request.getMasterDataExtnAttribute().add(webserviceMasterDataExtnAttribute);
			}
		}
		this.updateProductAttribute(request);
		ProductAttributeManagementServiceClient.logger.info(String.format(COMPLETE_UPDATE_TAGS_AND_SPECS_LOG, userId, updates.get(0).getProdId()));

	}

	/**
	 * This method will take an tags and specs attribute with multiple values and remove all of the old ones so new ones
	 * can be added
	 * @param update the attribute to be cleared
	 * @param attributeId the id of the attribute to be cleared
	 */
	private void clearAllPrevious(TagsAndSpecsAttribute update, long attributeId, String userId){
		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		request.setTrackingNbr(this.getWorkId().toString());
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		List<MasterDataExtensionAttribute> oldAttributes =
				this.masterDataExtensionAttributeRepository.findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystemOrderByAttributeCode(
						attributeId, update.getProdId(), PRODUCT_ITEM_KEY_CODE.trim(), defaultSourceSystemCode);
		for(int index = 0; index < oldAttributes.size(); index++){
			MasterDataExtnAttribute webserviceMasterDataExtnAttribute = new MasterDataExtnAttribute();
			webserviceMasterDataExtnAttribute.setWORKID(workId);
			webserviceMasterDataExtnAttribute.setLSTUPDTUID(userId);
			webserviceMasterDataExtnAttribute.setACTIONCD(ACTION_CODE.ACTION_CD_DELETE.getValue());
			webserviceMasterDataExtnAttribute.setSEQNBR(String.valueOf(oldAttributes.get(index).getKey().getSequenceNumber()));
			webserviceMasterDataExtnAttribute.setATTRID(String.valueOf(oldAttributes.get(index).getKey().getAttributeId()));
			webserviceMasterDataExtnAttribute.setKEYID(String.valueOf(oldAttributes.get(index).getKey().getId()));
			webserviceMasterDataExtnAttribute.setITMPRODKEYCD(PRODUCT_ITEM_KEY_CODE.trim());
			webserviceMasterDataExtnAttribute.setATTRCDID(oldAttributes.get(index).getAttributeCodeId().toString());
			request.getMasterDataExtnAttribute().add(webserviceMasterDataExtnAttribute);
		}
		this.updateProductAttribute(request);

	}

	/**
	 * This method creates or deletes rows from the Master Data Extension Attribute table based on what the tags and specs
	 * update specifies
	 * @param attribute attribute to be edited
	 * @param value attribute code to be added or deleted
	 * @param userId the used doing the update
	 * @param workId the id for the update
	 * @param textValue if it is a custom attribute its value
	 * @param sequenceNumber the sequence number for multiple valued attributes
	 * @return
	 */
	private MasterDataExtnAttribute createTagsAndSpecsAttributeUpdate(TagsAndSpecsAttribute attribute, TagsAndSpecsValue value,
																	  String userId, String workId, String textValue, Integer sequenceNumber){
		MasterDataExtnAttribute webserviceMasterDataExtnAttribute = new MasterDataExtnAttribute();
		webserviceMasterDataExtnAttribute.setWORKID(workId);
		if(value != null){
			if (value.getSelected()) {
				webserviceMasterDataExtnAttribute.setACTIONCD(" ");
			} else {
				webserviceMasterDataExtnAttribute.setACTIONCD(ACTION_CODE.ACTION_CD_DELETE.getValue());
			}
			webserviceMasterDataExtnAttribute.setATTRCDID(String.valueOf(value.getAttributeCodeId()));
		} else {
			webserviceMasterDataExtnAttribute.setACTIONCD(ACTION_CODE.ACTION_CD_UPDATE.getValue());
			webserviceMasterDataExtnAttribute.setATTRCDID(null);
		}
		if(sequenceNumber != null){
			webserviceMasterDataExtnAttribute.setSEQNBR(sequenceNumber.toString());
		} else {
			webserviceMasterDataExtnAttribute.setSEQNBR(String.valueOf(0));
		}
		webserviceMasterDataExtnAttribute.setATTRID(String.valueOf(attribute.getAttributeId()));
		webserviceMasterDataExtnAttribute.setKEYID(attribute.getProdId().toString());
		webserviceMasterDataExtnAttribute.setITMPRODKEYCD(PRODUCT_ITEM_KEY_CODE.trim());
		webserviceMasterDataExtnAttribute.setATTRVALTXTTEXT(textValue);
		webserviceMasterDataExtnAttribute.setLSTUPDTUID(userId);
		webserviceMasterDataExtnAttribute.setLSTUPDTTS(LocalDateTime.now().toString());
		webserviceMasterDataExtnAttribute.setDTASRCSYS(String.valueOf(defaultSourceSystemCode));
		return webserviceMasterDataExtnAttribute;
	}

	/**
	 * This method attempts to update a product's trash can
	 * @param productTrashCan the changes to be made
	 * @param userId the user requesting the change
	 */
	public void updateOnlineOnly(ProductTrashCan productTrashCan, String userId){
		ProductAttributeManagementServiceClient.logger.info(String.format(PRODUCT_TRASH_CAN_UPDATE_BEGIN, userId, productTrashCan.getProductId()));
		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		request.setTrackingNbr(this.getWorkId().toString());
		request.setAuthentication(this.getAuthentication());
		String workId = this.getWorkId().toString();
		ProdTrashCan webserviceTrashCan = new ProdTrashCan();
		webserviceTrashCan.setWORKID(workId);
		webserviceTrashCan.setACTIONCD(" ");
		webserviceTrashCan.setPRODID(String.valueOf(productTrashCan.getProductId()));
		webserviceTrashCan.setONLINSALEONLYSW(convertBooleanToString(productTrashCan.getOnlineSaleOnlySw()));
		request.getProdTrashCan().add(webserviceTrashCan);
		this.updateProductAttribute(request);
		ProductAttributeManagementServiceClient.logger.info(String.format(PRODUCT_TRASH_CAN_UPDATE_COMPLETE, userId, productTrashCan.getProductId()));

	}

	/**
	 * This method attempts to updates a product's marketing claim
	 * @param claims the marketing claims to be updated
	 * @param userId the user making the update
	 */
	public void updateProductMarketingClaim(List<ProductMarketingClaim> claims, String userId){

		if (claims.isEmpty()) {
			return;
		}

		String workId = this.getWorkId().toString();

		ProductAttributeManagementServiceClient.logger.info(String.format(PRODUCT_MARKETING_CLAIM_UPDATE_BEGIN, userId, claims.get(0).getKey().getProdId()));
		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		request.setTrackingNbr(workId);
		request.setAuthentication(this.getAuthentication());

		for (ProductMarketingClaim claim : claims) {
			ProductMarketClaim webserviceMarketingClaim = new ProductMarketClaim();
			if (claim.getAction() == ProductMarketingClaim.DELETE) {
				webserviceMarketingClaim.setACTIONCD("D");
			} else {
				webserviceMarketingClaim.setACTIONCD(" ");
			}
			webserviceMarketingClaim.setLSTUPDTUID(userId);
			webserviceMarketingClaim.setMKTCLMCD(claim.getKey().getMarketingClaimCode());
			webserviceMarketingClaim.setMKTCLMSTATCD(claim.getMarketingClaimStatusCode());
			webserviceMarketingClaim.setPRODID(claim.getKey().getProdId().toString());
			webserviceMarketingClaim.setWORKID(workId);
			if (claim.getPromoPickEffectiveDate() != null) {
				webserviceMarketingClaim.setPROMOPCKEFFDT(claim.getPromoPickEffectiveDate().format(dateTimeFormatter));
			}
			if (claim.getPromoPickExpirationDate() != null) {
				webserviceMarketingClaim.setPROMOPCKEXPRNDT(claim.getPromoPickExpirationDate().format(dateTimeFormatter));
			}

			request.getProductMarketClaim().add(webserviceMarketingClaim);
		}

		this.updateProductAttribute(request);
		ProductAttributeManagementServiceClient.logger.info(String.format(PRODUCT_MARKETING_CLAIM_UPDATE_COMPLETE, userId, claims.get(0).getKey().getProdId()));
	}

	/**
	 * This method attempts to updates shelf attribute informatuon include product's marketing claim and nutrition
	 * claims.
	 * @param claims the marketing claims to be updated.
	 * @param nutritionalClaims the nutrition claims to be updated.
	 * @param userId  the user making the update.
	 */
	public  void updateShelfAttributes(List<ProductMarketingClaim> claims, List<NutritionalClaims> nutritionalClaims,
									   String userId){
		if (claims.isEmpty() && (nutritionalClaims == null || nutritionalClaims.isEmpty())) {
			return;
		}

		String workId = this.getWorkId().toString();

		ProductAttributeManagementServiceClient.logger.info(String.format(PRODUCT_MARKETING_CLAIM_UPDATE_BEGIN,
				userId, !claims.isEmpty()?claims.get(0).getKey().getProdId():nutritionalClaims.get(0).getKey().getUpc()));
		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		request.setTrackingNbr(workId);
		request.setAuthentication(this.getAuthentication());

		//update Product Marketing Claim
		if (!claims.isEmpty()) {
			for (ProductMarketingClaim claim : claims) {
				ProductMarketClaim webserviceMarketingClaim = new ProductMarketClaim();
				if (claim.getAction() == ProductMarketingClaim.DELETE) {
					webserviceMarketingClaim.setACTIONCD("D");
				} else {
					webserviceMarketingClaim.setACTIONCD(" ");
				}
				webserviceMarketingClaim.setLSTUPDTUID(userId);
					webserviceMarketingClaim.setSTATCHGRSNTXT(claim.getStatusChangeReason());
				webserviceMarketingClaim.setMKTCLMCD(claim.getKey().getMarketingClaimCode());
				webserviceMarketingClaim.setMKTCLMSTATCD(claim.getMarketingClaimStatusCode());
				webserviceMarketingClaim.setPRODID(claim.getKey().getProdId().toString());
				webserviceMarketingClaim.setWORKID(workId);
				if (claim.getPromoPickEffectiveDate() != null) {
					webserviceMarketingClaim.setPROMOPCKEFFDT(claim.getPromoPickEffectiveDate().format(dateTimeFormatter));
				}
				if (claim.getPromoPickExpirationDate() != null) {
					webserviceMarketingClaim.setPROMOPCKEXPRNDT(claim.getPromoPickExpirationDate().format(dateTimeFormatter));
				}
				request.getProductMarketClaim().add(webserviceMarketingClaim);
			}
		}

		//Update Nutritional Claims
		if(nutritionalClaims != null) {
			for (NutritionalClaims nutritionalCl : nutritionalClaims) {
				ProductScanNtrntl productScanNtrntl = new ProductScanNtrntl();
				productScanNtrntl.setWORKID(workId);
				productScanNtrntl.setCRE8UID(userId);
				productScanNtrntl.setLSTUPDTUID(userId);
				productScanNtrntl.setSCNCDID(nutritionalCl.getKey().getUpc().toString());
				productScanNtrntl.setPRODNTRNTLCD(nutritionalCl.getKey().getNutritionalClaimsCode());
				productScanNtrntl.setSRCSYSTEMID(nutritionalCl.getSourceSystemId().toString());
				if (nutritionalCl.isSelected()) {
					productScanNtrntl.setACTIONCD("A");
				} else {
					productScanNtrntl.setACTIONCD("D");
				}
				request.getProductScanNtrntl().add(productScanNtrntl);
			}
		}

		this.updateProductAttribute(request);
		ProductAttributeManagementServiceClient.logger.info(String.format(PRODUCT_MARKETING_CLAIM_UPDATE_COMPLETE,
				userId, !claims.isEmpty()?claims.get(0).getKey().getProdId():nutritionalClaims.get(0).getKey().getUpc()));
	}

	/**
	 * Return the service agent for this client.
	 *
	 * @return ProductAttributeManagementServiceServiceagent associated with this client.
	 */
	@Override
	protected ProductAttributeManagementServiceServiceagent getServiceAgent() {
		try {
			URL url = new URL(this.getWebServiceUri());
			return new ProductAttributeManagementServiceServiceagent(url);
		} catch (MalformedURLException e) {
			ProductAttributeManagementServiceClient.logger.error(e.getMessage());
		}
		return new ProductAttributeManagementServiceServiceagent();
	}

	/**
	 * Return the port type for this client.
	 *
	 * @param agent The agent to use to create the port.
	 * @return ProductAttributeManagementServicePort associated with this client.
	 */
	@Override
	protected ProductAttributeManagementServicePort getServicePort(ProductAttributeManagementServiceServiceagent agent) {
		return agent.getProductAttributeManagementService();
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
	 * Update eCommerce View data source. Will by update content and set product attribute overwrite.
	 *
	 * @param eCommerceViewAttributePriority the eCommerceViewAttributePriorities object. Contain information change
	 * @return {{message}}
	 * @throws Exception
	 */
	public void updateECommerceViewDataSource(ECommerceViewAttributePriority eCommerceViewAttributePriority, String userId) throws Exception {
		if(CollectionUtils.isNotEmpty(eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails())
				&& eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().get(0).getTargetSystemAttributePriority().getKey().getLogicalAttributeId() == ProductECommerceViewService
				.LogicAttributeCode.DIMENSIONS_LOGIC_ATTRIBUTE_ID.getValue().intValue()){
			this.updateECommerceViewDataSourceByLogicalAttribute(eCommerceViewAttributePriority, userId);
		}else{
			this.updateAllECommerceViewDataSource(eCommerceViewAttributePriority, userId);
		}
	}

	/**
	 * Update eCommerce View data source. Will by update content and set product attribute overwrite.
	 *
	 * @param eCommerceViewAttributePriority the eCommerceViewAttributePriorities object. Contain information change
	 * @return {{message}}
	 * @throws Exception
	 */
	public void updateAllECommerceViewDataSource(ECommerceViewAttributePriority eCommerceViewAttributePriority, String userId) throws Exception {
		//Init request to update from Product Attribute Management Service
		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		String psWorkId = this.getWorkId().toString();
		request.setTrackingNbr(psWorkId);
		request.setAuthentication(this.getAuthentication());

		//Handle data to add request
		//Remove old product attribute overwrite
		if(eCommerceViewAttributePriority.getProductAttributeOverwrite() != null){
			ProdAttrOvrd prodAttrOvrd = this.createProductAttributeOverride(eCommerceViewAttributePriority
							.getProductAttributeOverwrite(), eCommerceViewAttributePriority.getProductId(), psWorkId,
					ACTION_CODE.ACTION_CD_DELETE.getValue());
			request.getProdAttrOvrd().add(prodAttrOvrd);
		}
		//Add new product attribute overwrite
		for(ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails : eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails()){
			if(eCommerceViewAttributePriorityDetails.isSelected()){
				boolean changeAttributeOverwrite = true;
				if(eCommerceViewAttributePriority.getProductAttributeOverwrite() != null &&
						(eCommerceViewAttributePriority.getProductAttributeOverwrite().getKey().getLogicAttributeId()
								.intValue() == eCommerceViewAttributePriorityDetails.getTargetSystemAttributePriority().getKey()
								.getLogicalAttributeId()) && eCommerceViewAttributePriority.getProductAttributeOverwrite()
						.getKey().getPhysicalAttributeId().equals(eCommerceViewAttributePriorityDetails
								.getTargetSystemAttributePriority().getKey().getPhysicalAttributeId()) &&
						eCommerceViewAttributePriority.getProductAttributeOverwrite().getKey().getSourceSystemId().equals
								(eCommerceViewAttributePriorityDetails.getTargetSystemAttributePriority().getKey()
										.getDataSourceSystemId())){
					changeAttributeOverwrite = false;
					request.getProdAttrOvrd().clear();
				}
				if((changeAttributeOverwrite || eCommerceViewAttributePriority.getProductAttributeOverwrite() == null)
						&& !eCommerceViewAttributePriorityDetails.isSourceDefault()){
					ProdAttrOvrd prodAttrOvrd = this.createProductAttributeOverride(eCommerceViewAttributePriorityDetails.getTargetSystemAttributePriority(),
							eCommerceViewAttributePriority.getProductId(), psWorkId, ACTION_CODE.ACTION_CD_ADD.getValue());
					request.getProdAttrOvrd().add(prodAttrOvrd);
				}

				//edit information content has been changed. Rule allow edit, when edit content, source system must be 4 and
				// has physical attribute allow edit basing on attribute id
				if(eCommerceViewAttributePriorityDetails.isSourceEditable()){
					if(!(eCommerceViewAttributePriority.getAttributeId().equals(ProductECommerceViewService
							.LogicAttributeCode.DISPLAY_NAME_LOGIC_ATTRIBUTE_ID.getValue())) 
							&& !(eCommerceViewAttributePriority.getAttributeId().equals(ProductECommerceViewService
							.LogicAttributeCode.ROMANCE_COPY_LOGIC_ATTRIBUTE_ID.getValue()))
							&& !(eCommerceViewAttributePriority.getAttributeId().equals(ProductECommerceViewService
							.LogicAttributeCode.FAVOR_ITEM_DESCRIPTION_LOGIC_ATTRIBUTE_ID.getValue()))){
						MasterDataExtnAttribute masterDataExtnAttribute = this.createMasterDataExtensionAttribute
								(eCommerceViewAttributePriorityDetails, eCommerceViewAttributePriority.getPrimaryScanCode(), userId);
						request.getMasterDataExtnAttribute().add(masterDataExtnAttribute);
					}
				}
				break;
			}
		}
        if(CollectionUtils.isNotEmpty(request.getProdAttrOvrd()) 
				|| CollectionUtils.isNotEmpty(request.getMasterDataExtnAttribute())){
		    //Call service to update data
		    this.updateProductAttribute(request);
		}
	}

	/**
	 * Update eCommerce View data source. Will by update content and set product attribute overwrite.
	 *
	 * @param eCommerceViewAttributePriority the eCommerceViewAttributePriorities object. Contain information change
	 * @return {{message}}
	 * @throws Exception
	 */
	public void updateECommerceViewDataSourceByLogicalAttribute(ECommerceViewAttributePriority eCommerceViewAttributePriority, String userId) throws Exception {
		//Init request to update from Product Attribute Management Service
		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		String psWorkId = this.getWorkId().toString();
		request.setTrackingNbr(psWorkId);
		request.setAuthentication(this.getAuthentication());

		//Handle data to add request
		//Remove all old product attribute overwrite
		ProdAttrOvrd addNewProdAttrOvrd = null;
		if(eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails() != null){
			for(ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails : eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails()){
				if(eCommerceViewAttributePriorityDetails.getTargetSystemAttributePriority() != null){
					if(!eCommerceViewAttributePriorityDetails.isSelected()){
						ProdAttrOvrd prodAttrOvrd = this.createProductAttributeOverride(eCommerceViewAttributePriorityDetails.getTargetSystemAttributePriority(),
								eCommerceViewAttributePriority.getProductId(), psWorkId, ACTION_CODE.ACTION_CD_DELETE.getValue());
						request.getProdAttrOvrd().add(prodAttrOvrd);
					}else{
						if(!eCommerceViewAttributePriorityDetails.isSourceDefault()){
							addNewProdAttrOvrd = this.createProductAttributeOverride(eCommerceViewAttributePriorityDetails.getTargetSystemAttributePriority(),
									eCommerceViewAttributePriority.getProductId(), psWorkId, ACTION_CODE.ACTION_CD_ADD.getValue());
						}
					}
				}
			}
		}
		//Add new product attribute overwrite
		if(addNewProdAttrOvrd != null){
			request.getProdAttrOvrd().add(addNewProdAttrOvrd);
		}
		//Call service to update data
		this.updateProductAttribute(request);
	}

	/**
	 * create master data Extension Attribute
	 * @param eCommerceViewAttributePriorityDetails
	 * @param scanCodeId
	 * @param userId
	 * @return
	 */
	private MasterDataExtnAttribute createMasterDataExtensionAttribute(ECommerceViewAttributePriorityDetails
																			   eCommerceViewAttributePriorityDetails,
																	   long scanCodeId, String userId) {
		TargetSystemAttributePriority targetSystemAttributePriority = eCommerceViewAttributePriorityDetails
				.getTargetSystemAttributePriority();
		String actionCode = this.getActionCodeForMasterDataExtnAttribute(
				targetSystemAttributePriority.getKey().getPhysicalAttributeId(), scanCodeId,
				targetSystemAttributePriority.getKey().getDataSourceSystemId());
		MasterDataExtnAttribute masterDataExtnAttribute = new MasterDataExtnAttribute();
		masterDataExtnAttribute.setWORKID(this.getWorkId().toString());
		masterDataExtnAttribute.setACTIONCD(actionCode);
		masterDataExtnAttribute.setATTRID(String.valueOf(targetSystemAttributePriority.getKey()
				.getPhysicalAttributeId()));
		masterDataExtnAttribute.setKEYID(String.valueOf(scanCodeId));
		masterDataExtnAttribute.setITMPRODKEYCD(ProductAttributeManagementServiceClient.UPC_ITEM_KEY_CODE.trim());
		masterDataExtnAttribute.setSEQNBR(String.valueOf(0));
		masterDataExtnAttribute.setDTASRCSYS(String.valueOf(targetSystemAttributePriority.getKey()
				.getDataSourceSystemId()));
		if(eCommerceViewAttributePriorityDetails.<String>getContent() != null) {
			masterDataExtnAttribute.setATTRVALTXTTEXT(eCommerceViewAttributePriorityDetails.<String>getContent().getContent());
		}
		if (eCommerceViewAttributePriorityDetails.getPostedDate() != null) {
			masterDataExtnAttribute.setATTRVALTS(eCommerceViewAttributePriorityDetails.getPostedDate().toString());
		}
		if (ACTION_CODE.ACTION_CD_ADD.getValue().equals(actionCode)) {
			masterDataExtnAttribute.setCRE8UID(userId);
			masterDataExtnAttribute.setCRE8TS(DateTime.now().toString(DATE_FORMAT_YYYYMMDD));
		}
		masterDataExtnAttribute.setLSTUPDTUID(userId);
		masterDataExtnAttribute.setLSTUPDTTS(DateTime.now().toString(DATE_FORMAT_YYYYMMDD));
		return masterDataExtnAttribute;
	}

	/**
	 * create product attribute override entity by ProductAttributeOverwrite entity and productId and action code.
	 * @param productAttributeOverwrite the ProductAttributeOverwrite entity to be updated.
	 * @param productId the product id to be updated.
	 * @param psWorkId the ps work id to be updated.
	 * @param actionCode the action to handle
	 * @return {{ProdAttrOvrd object}}
	 */
	private ProdAttrOvrd createProductAttributeOverride(ProductAttributeOverwrite productAttributeOverwrite, long
			productId, String psWorkId, String actionCode) {
		ProdAttrOvrd productAttributeOverride = new ProdAttrOvrd();
		productAttributeOverride.setWORKID(psWorkId);
		productAttributeOverride.setACTIONCD(actionCode);
		productAttributeOverride.setTRGTSYSID(String.valueOf(productAttributeOverwrite.getKey().getSystemId()));
		productAttributeOverride.setITMPRODKEYCD(ProductAttributeManagementServiceClient.PRODUCT_ITEM_KEY_CODE.trim());
		productAttributeOverride.setKEYID(BigDecimal.valueOf(productId));
		productAttributeOverride.setLOGICATTRID(String.valueOf(productAttributeOverwrite.getKey().getLogicAttributeId()));
		productAttributeOverride.setDTASRCSYSID(String.valueOf(productAttributeOverwrite.getKey().getSourceSystemId()));
		productAttributeOverride.setPHYATTRID(String.valueOf(productAttributeOverwrite.getKey().getPhysicalAttributeId()));

		TargetSystemAttributePriority targetSystemAttributePriority = this.targetSystemAttributePriorityRepository.
				findOneByKeyLogicalAttributeIdAndKeyDataSourceSystemIdAndKeyPhysicalAttributeIdOrderByAttributePriorityNumberAsc(
						productAttributeOverwrite.getKey().getLogicAttributeId().intValue(),
						productAttributeOverwrite.getKey().getSourceSystemId(),
						productAttributeOverwrite.getKey().getPhysicalAttributeId());
		if(targetSystemAttributePriority != null){
			productAttributeOverride.setRLSHPGRPTYPCD(StringUtils.trim(targetSystemAttributePriority.getKey()
					.getRelationshipGroupTypeCode()));
		}else{
			productAttributeOverride.setRLSHPGRPTYPCD(ProductECommerceViewService.ATTRIBUTE_RELATIONSHIP_GROUP_TYPE_CODE);
		}
		return productAttributeOverride;
	}

	/**
	 * create product attribute override entity by ProductAttributeOverwrite entity and productId and action code.
	 * @param targetSystemAttributePriority the TargetSystemAttributePriority entity to be updated.
	 * @param productId the product id to be updated.
	 * @param psWorkId the ps work id to be updated.
	 * @param actionCode the action to handle
	 * @return {{ProdAttrOvrd object}}
	 */
	private ProdAttrOvrd createProductAttributeOverride(TargetSystemAttributePriority targetSystemAttributePriority, long
			productId, String psWorkId, String actionCode) {
		ProdAttrOvrd productAttributeOverride = new ProdAttrOvrd();
		productAttributeOverride.setWORKID(psWorkId);
		productAttributeOverride.setACTIONCD(actionCode);
		productAttributeOverride.setTRGTSYSID(String.valueOf(targetSystemAttributePriority.getKey().getTargetSystemId()));
		productAttributeOverride.setITMPRODKEYCD(ProductAttributeManagementServiceClient.PRODUCT_ITEM_KEY_CODE.trim());
		productAttributeOverride.setKEYID(BigDecimal.valueOf(productId));
		productAttributeOverride.setLOGICATTRID(String.valueOf(targetSystemAttributePriority.getKey().getLogicalAttributeId()));
		productAttributeOverride.setDTASRCSYSID(String.valueOf(targetSystemAttributePriority.getKey().getDataSourceSystemId()));
		productAttributeOverride.setPHYATTRID(String.valueOf(targetSystemAttributePriority.getKey().getPhysicalAttributeId()));
		productAttributeOverride.setRLSHPGRPTYPCD(targetSystemAttributePriority.getKey().getRelationshipGroupTypeCode());
		return productAttributeOverride;
	}

	/**
	 * create product attribute override entity by ProductAttributeOverwrite entity and productId and action code.
	 * @param logicAttributeId the attribute id
	 * @param physicalId the physical id
	 * @param productId the product id to be updated.
	 * @param psWorkId the ps work id to be updated.
	 * @param actionCode the action to handle
	 * @return {{ProdAttrOvrd object}}
	 */
	private void createProductAttributeOverride(Long logicAttributeId, Long physicalId, long productId,
												String psWorkId, String actionCode,Long sourceSystem,
												UpdateProductAttributeRequest request) {
		ProdAttrOvrd productAttributeOverride = new ProdAttrOvrd();
		productAttributeOverride.setWORKID(psWorkId);
		productAttributeOverride.setACTIONCD(actionCode);
		productAttributeOverride.setTRGTSYSID(
				ProductECommerceViewService.SourceSystemNumber.ECOMMERCE_SOURCE_SYSTEM_NUMBER.getValue().toString());
		productAttributeOverride.setITMPRODKEYCD(ProductAttributeManagementServiceClient.PRODUCT_ITEM_KEY_CODE.trim());
		productAttributeOverride.setKEYID(BigDecimal.valueOf(productId));
		productAttributeOverride.setLOGICATTRID(String.valueOf(logicAttributeId));
		productAttributeOverride.setDTASRCSYSID(String.valueOf(sourceSystem));
		productAttributeOverride.setPHYATTRID(String.valueOf(physicalId));
		TargetSystemAttributePriority targetSystemAttributePriority = this.targetSystemAttributePriorityRepository.
				findOneByKeyLogicalAttributeIdAndKeyDataSourceSystemIdAndKeyPhysicalAttributeIdOrderByAttributePriorityNumberAsc(logicAttributeId.intValue(), sourceSystem, physicalId);
		if(targetSystemAttributePriority != null){
			productAttributeOverride.setRLSHPGRPTYPCD(StringUtils.trim(targetSystemAttributePriority.getKey()
					.getRelationshipGroupTypeCode()));
		}else{
			productAttributeOverride.setRLSHPGRPTYPCD(ProductECommerceViewService.ATTRIBUTE_RELATIONSHIP_GROUP_TYPE_CODE);
		}
		request.getProdAttrOvrd().add(productAttributeOverride);
	}

	/**
	 *
	 * @param physicalId
	 * @param content
	 * @param keyId
	 * @param itmProdKeyCd
	 * @param userId
	 * @param request
	 */
	private void createMasterDataExtensionAttribute(Long physicalId, String content, long keyId,
													String itmProdKeyCd, String userId,
													UpdateProductAttributeRequest request) {
		String actionCode = ACTION_CODE.ACTION_CD_ADD.getValue();
		MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
				.findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(physicalId, keyId,
						itmProdKeyCd, ProductECommerceViewService.SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue());
		if (masterDataExtensionAttribute != null) {
			actionCode =  ACTION_CODE.ACTION_CD_UPDATE.getValue();
		}
		MasterDataExtnAttribute masterDataExtnAttribute = new MasterDataExtnAttribute();
		masterDataExtnAttribute.setWORKID(this.getWorkId().toString());
		masterDataExtnAttribute.setACTIONCD(actionCode);
		masterDataExtnAttribute.setATTRID(String.valueOf(physicalId));
		masterDataExtnAttribute.setKEYID(String.valueOf(keyId));
		masterDataExtnAttribute.setITMPRODKEYCD(itmProdKeyCd);
		masterDataExtnAttribute.setSEQNBR(String.valueOf(0));
		masterDataExtnAttribute.setDTASRCSYS(String.valueOf(ProductECommerceViewService.SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue()));
		masterDataExtnAttribute.setATTRVALTXTTEXT(content);
		if (ACTION_CODE.ACTION_CD_ADD.getValue().equals(actionCode)) {
			masterDataExtnAttribute.setCRE8UID(userId);
			masterDataExtnAttribute.setCRE8TS(DateTime.now().toString(DATE_FORMAT_YYYYMMDD));
		}
		masterDataExtnAttribute.setLSTUPDTUID(userId);
		masterDataExtnAttribute.setLSTUPDTTS(DateTime.now().toString(DATE_FORMAT_YYYYMMDD));
		request.getMasterDataExtnAttribute().add(masterDataExtnAttribute);
	}

	private boolean checkEditDataSourceByAttribute(ECommerceViewAttributePriorities attributePriorities, Long productId,
												   Long scanCodeId, String psWorkId, String userId,
												   Map<Long, ProductAttributeOverwrite> productAttributeOverwriteMap, Long physicalAttributeEdit,
												   boolean addMasterDataExtension, UpdateProductAttributeRequest
														   request){
		boolean changed = false;
		if(attributePriorities != null){
			boolean addNew = false;
			ProductAttributeOverwrite prodAttribute = productAttributeOverwriteMap.get(attributePriorities
					.getLogicAttributeId());
			if(prodAttribute != null && (!prodAttribute.getKey().getPhysicalAttributeId().equals(physicalAttributeEdit)
					|| !prodAttribute.getKey().getSourceSystemId().equals(ProductECommerceViewService.SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue()))){
				this.createProductAttributeOverride(prodAttribute.getKey().getLogicAttributeId(),prodAttribute
						.getKey().getPhysicalAttributeId(),productId, psWorkId, ACTION_CODE.ACTION_CD_DELETE
						.getValue(), prodAttribute.getKey().getSourceSystemId(),request);
				addNew = true;
				changed = true;
			}
			if(prodAttribute == null || addNew) {
				this.createProductAttributeOverride(attributePriorities.getLogicAttributeId(),
						physicalAttributeEdit, productId, psWorkId, ACTION_CODE.ACTION_CD_ADD.getValue(),
						ProductECommerceViewService.SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue(),
						request);
				changed = true;
			}
			if(addMasterDataExtension) {
				this.createMasterDataExtensionAttribute(physicalAttributeEdit, attributePriorities.getContent(),
						scanCodeId, UPC_ITEM_KEY_CODE, userId, request);
				changed = true;
			}
		}
		return changed;
	}

	/**
	 * Update eCommerce View information.
	 *
	 * @param eCommerceViewDetails the ECommerceViewDetails object.
	 * @param userId  the user id.
	 */
	public void updateECommerceViewInformation(ECommerceViewDetails eCommerceViewDetails, String userId) {
		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		String psWorkId = this.getWorkId().toString();
		request.setTrackingNbr(psWorkId);
		request.setAuthentication(this.getAuthentication());
		boolean changed = false;
		List<Boolean> listChange = new ArrayList<Boolean>();
		//check product attribute overwrite basing on attribute id
		List<ProductAttributeOverwrite> productAttributeOverwrites = this.productAttributeOverwriteRepository
				.findByKeyItemProductKeyCodeAndKeyKeyIdAndKeyLogicAttributeIdIn(ProductECommerceViewService
						.PRODUCT_ITEM_KEY_CODE, eCommerceViewDetails.getProductId(), eCommerceViewDetails
						.getAttributeListChange());
		Map<Long, ProductAttributeOverwrite> productAttributeOverwriteMap = productAttributeOverwrites.stream()
				.collect(Collectors.toMap(p -> p.getKey().getLogicAttributeId(),p -> p));
		//Check brand
		listChange.add(checkEditDataSourceByAttribute(eCommerceViewDetails.getBrand(), eCommerceViewDetails.getProductId(),
				eCommerceViewDetails.getScanCodeId(), psWorkId, userId, productAttributeOverwriteMap, ProductECommerceViewService.PhysicalAttributeCodeEditable
						.BRAND_PHYSICAL_ATTRIBUTE_EDITABLE.getValue(), true, request));
		//Check display name
		listChange.add(checkEditDataSourceByAttribute(eCommerceViewDetails.getDisplayName(), eCommerceViewDetails.getProductId(),
				eCommerceViewDetails.getScanCodeId(), psWorkId, userId, productAttributeOverwriteMap, ProductECommerceViewService.PhysicalAttributeCodeEditable
						.DISPLAY_NAME_ATTRIBUTE_EDITABLE.getValue(), false, request));
		//Check size
		listChange.add(checkEditDataSourceByAttribute(eCommerceViewDetails.getSize(), eCommerceViewDetails.getProductId(),
				eCommerceViewDetails.getScanCodeId(), psWorkId, userId, productAttributeOverwriteMap, ProductECommerceViewService.PhysicalAttributeCodeEditable
						.SIZE_PHYSICAL_ATTRIBUTE_EDITABLE.getValue(), true, request));
		//Check Romance Copy
		listChange.add(checkEditDataSourceByAttribute(eCommerceViewDetails.getRomanceCopy(), eCommerceViewDetails.getProductId(),
				eCommerceViewDetails.getScanCodeId(), psWorkId, userId, productAttributeOverwriteMap, ProductECommerceViewService.PhysicalAttributeCodeEditable
						.ROMANCE_COPY_ATTRIBUTE_EDITABLE.getValue(), false, request));
		//Check favor item description
		listChange.add(checkEditDataSourceByAttribute(eCommerceViewDetails.getFavorItemDescription(), eCommerceViewDetails.getProductId(),
				eCommerceViewDetails.getScanCodeId(), psWorkId, userId, productAttributeOverwriteMap, ProductECommerceViewService.PhysicalAttributeCodeEditable
						.FAVOR_ITEM_DESCRIPTION_ATTRIBUTE_EDITABLE.getValue(), false, request));
		//Check Ingredients
		listChange.add(checkEditDataSourceByAttribute(eCommerceViewDetails.getIngredients(), eCommerceViewDetails.getProductId(),
				eCommerceViewDetails.getScanCodeId(), psWorkId, userId, productAttributeOverwriteMap, ProductECommerceViewService.PhysicalAttributeCodeEditable
						.INGREDIENT_STATEMENT_PHYSICAL_ATTRIBUTE_EDITABLE.getValue(), true, request));
		//check Directions
		listChange.add(checkEditDataSourceByAttribute(eCommerceViewDetails.getDirections(), eCommerceViewDetails.getProductId(),
				eCommerceViewDetails.getScanCodeId(), psWorkId, userId, productAttributeOverwriteMap, ProductECommerceViewService.PhysicalAttributeCodeEditable
						.DIRECTIONS_PHYSICAL_ATTRIBUTE_EDITABLE.getValue(), true, request));
		//check warning
		listChange.add(checkEditDataSourceByAttribute(eCommerceViewDetails.getWarnings(), eCommerceViewDetails.getProductId(),
				eCommerceViewDetails.getScanCodeId(), psWorkId, userId, productAttributeOverwriteMap, ProductECommerceViewService.PhysicalAttributeCodeEditable
						.WARNING_PHYSICAL_ATTRIBUTE_EDITABLE.getValue(), true, request));
		//PDP Template
		if(eCommerceViewDetails.getPdpTemplate() != null){
			this.createMasterDataExtensionAttribute(ProductECommerceViewService.LogicAttributeCode
							.PDP_TEMPLATE_LOGIC_ATTRIBUTE_ID.getValue(), eCommerceViewDetails.getPdpTemplate().getContent(),
					eCommerceViewDetails.getProductId(), PRODUCT_ITEM_KEY_CODE, userId, request);
			listChange.add(true);
		}

		//Show on site
		ProductOnline productOnlineOrg = this.productOnlineRepository
				.findTop1ByKeyProductIdAndKeySaleChannelCodeAndShowOnSiteOrderByKeyEffectiveDateDesc(eCommerceViewDetails.getProductId(),
						eCommerceViewDetails.getSalsChnlCd(), ProductECommerceViewService.PRODUCT_SHOW_ON_SITE);
		// ShowOnSite is true and StartDate is changed
		if (productOnlineOrg != null) {
			// Delete
			com.heb.xmlns.ei.productonline.ProductOnline productOnline = new com.heb.xmlns.ei.productonline.ProductOnline();
			productOnline.setWORKID(String.valueOf(0));
			productOnline.setPRODID(String.valueOf(eCommerceViewDetails.getProductId()));
			productOnline.setPRODIDSW(this.getActiveOnlineString(productOnlineOrg.isShowOnSite()));
			productOnline.setACTIONCD(ACTION_CODE.ACTION_CD_DELETE.getValue());
			productOnline.setEFFDT(ProductECommerceViewUtil.convertDateToStringDateYYYYMMDD(productOnlineOrg.getKey().getEffectiveDate()));
			productOnline.setEXPRNDT(ProductECommerceViewUtil.convertDateToStringDateYYYYMMDD(productOnlineOrg.getExpirationDate()));
			productOnline.setSALSCHNLCD(eCommerceViewDetails.getSalsChnlCd());
			request.getProductOnline().add(productOnline);
			listChange.add(true);
		}
		//add new show on site
		if(eCommerceViewDetails.isShowOnSite()) {
			com.heb.xmlns.ei.productonline.ProductOnline productOnline = new com.heb.xmlns.ei.productonline.ProductOnline();
			productOnline.setWORKID(String.valueOf(0));
			productOnline.setPRODID(String.valueOf(eCommerceViewDetails.getProductId()));
			productOnline.setPRODIDSW(DEFAULT_TRUE_STRING);
			productOnline.setSALSCHNLCD(eCommerceViewDetails.getSalsChnlCd());
			productOnline.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());
			productOnline.setEFFDT(ProductECommerceViewUtil.convertDateToStringDateYYYYMMDD(eCommerceViewDetails.getShowOnSiteStartDate()));
			productOnline.setEXPRNDT(ProductECommerceViewUtil.convertDateToStringDateYYYYMMDD(eCommerceViewDetails.getShowOnSiteEndDate()));
			request.getProductOnline().add(productOnline);
			listChange.add(true);
		}else{
			if(eCommerceViewDetails.getShowOnSiteStartDate()!= null && eCommerceViewDetails.getShowOnSiteStartDate().compareTo(LocalDate.now()) <= 0){
				com.heb.xmlns.ei.productonline.ProductOnline productOnline = new com.heb.xmlns.ei.productonline.ProductOnline();
				productOnline.setWORKID(String.valueOf(0));
				productOnline.setPRODID(String.valueOf(eCommerceViewDetails.getProductId()));
				productOnline.setPRODIDSW(DEFAULT_TRUE_STRING);
				productOnline.setSALSCHNLCD(eCommerceViewDetails.getSalsChnlCd());
				productOnline.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());
				productOnline.setEFFDT(ProductECommerceViewUtil.convertDateToStringDateYYYYMMDD(eCommerceViewDetails.getShowOnSiteStartDate()));
				productOnline.setEXPRNDT(ProductECommerceViewUtil.convertDateToStringDateYYYYMMDD(eCommerceViewDetails.getShowOnSiteEndDate()));
				request.getProductOnline().add(productOnline);
				listChange.add(true);
			}
		}

		//Update Panel Type
		List<ProdPackVariation> prodPkVariations = this.createProdPkVariation(eCommerceViewDetails.getScanCodeId(), eCommerceViewDetails.getNutriPanelTypCd(), userId);
		if (!prodPkVariations.isEmpty()) {
			request.getProdPkVariation().addAll(prodPkVariations);
			listChange.add(true);
		}

		changed = listChange.contains(true);

		//Call service to update data
		if(changed) {
			this.updateProductAttribute(request);
		}
	}

	/**
	 * Create the ProdPkVariation by upc, nutrient panel type.
	 *
	 * @param upc             the scan code.
	 * @param nutriPanelTypCd the nutriPanelTypCd.
	 * @return the list of ProdPackVariation.
	 */
	private List<ProdPackVariation> createProdPkVariation(long upc, String nutriPanelTypCd, String userId) {
		List<ProdPackVariation> prodPkVariations = new ArrayList<>();
		List<ProductPkVariation> productPkVariations = this.productPkVariationRepository.findByKeyUpc(upc);
		if (StringUtils.isNotEmpty(nutriPanelTypCd) && productPkVariations != null && !productPkVariations.isEmpty()) {
			for (ProductPkVariation productPkVariation : productPkVariations) {
				ProdPackVariation prodPkVar = new ProdPackVariation();
				prodPkVar.setSCNCDID(String.valueOf(upc));
				prodPkVar.setSRCSYSTEMID(String.valueOf(productPkVariation.getKey().getSourceSystem()));
				if (StringUtils.isBlank(nutriPanelTypCd)) {
					prodPkVar.setPRODPANELTYP(" ");
				} else {
					prodPkVar.setPRODPANELTYP(nutriPanelTypCd);
				}
				prodPkVar.setLSTUPDTUID(userId);
				prodPkVariations.add(prodPkVar);
			}
		}
		return prodPkVariations;
	}

	/**
	 * save product item variant
	 * @param lst
	 */
	public void saveProductItemVariant(List<ProductItemVariant> lst){
		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		String psWorkId = this.getWorkId().toString();
		request.setTrackingNbr(psWorkId);
		request.setAuthentication(this.getAuthentication());
		List<ProdItmVar> lstItem = new ArrayList<>();
		for(ProductItemVariant item : lst){
			ProdItmVar prodItmVar = new ProdItmVar();
			prodItmVar.setITMID(String.valueOf(item.getKey().getItemId()));
			prodItmVar.setPRODID(String.valueOf(item.getKey().getProductId()));
			prodItmVar.setRETLPACKQTY(String.valueOf(item.getRetailPackQuantity()));
			prodItmVar.setITMKEYTYPCD(item.getKey().getItemKeyTypeCode());
			prodItmVar.setACTIONCD(!item.getDelete()?ACTION_CODE.ACTION_CD_UPDATE.getValue():ACTION_CODE.ACTION_CD_DELETE.getValue());
			lstItem.add(prodItmVar);
		}
		request.getProdItmVar().addAll(lstItem);
		this.updateProductAttribute(request);
	}
	/**
	 * Update show on site for product group.
	 *
	 * @param productOnlines the list product online.
	 */
	public void updateShowOnSiteForProductGroup(List<com.heb.xmlns.ei.productonline.ProductOnline> productOnlines) {
		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		String psWorkId = this.getWorkId().toString();
		request.setTrackingNbr(psWorkId);
		request.setAuthentication(this.getAuthentication());
		request.getProductOnline().addAll(productOnlines);
		this.updateProductAttribute(request);
	}

	/**
	 * Assign Image(s) to Variant Product.
	 *
	 * @param images list of images.
	 * @param scnCode upc of product.
	 * @param userId the userid.
	 */
	public void assignImages(List<ProductScanImageURI> images, Long scnCode, String userId){
		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		request.setTrackingNbr(this.getWorkId().toString());
		request.setAuthentication(this.getAuthentication());
		List<ProductScanImageUrl> assignImages = new ArrayList<>();
		for(ProductScanImageURI image : images){
			ProductScanImageUrl productScanImageUrl = new ProductScanImageUrl();
			productScanImageUrl.setSCNCDID(scnCode.toString());
			productScanImageUrl.setIMGTYPCD(image.getImageTypeCode());
			productScanImageUrl.setURITXT(image.getImageURI());
			productScanImageUrl.setIMGLNDSCPSW(image.getImglndscpsw());
			productScanImageUrl.setACTVSW(this.getActiveOnlineString(image.getActiveSwitch()));
			productScanImageUrl.setIMGSRCNM(image.getApplicationSource());
			productScanImageUrl.setIMGACPTABLESW(image.getImageAccepted());
			productScanImageUrl.setREVDBYUID(image.getRevisedByID());
			productScanImageUrl.setSRCSYSTEMID(image.getSourceSystemId().toString());
			productScanImageUrl.setIMGCATCD(image.getImageCategoryCode());
			productScanImageUrl.setIMGPRTYCD(image.getImagePriorityCode());
			productScanImageUrl.setIMGFRMATCD(image.getImageFormat());
			productScanImageUrl.setACTVONLINSW(this.getActiveOnlineString(image.isActiveOnline()));
			productScanImageUrl.setRESLXNBR(image.getxAxisResolution().toString());
			productScanImageUrl.setRESLYNBR(image.getyAxisResolution().toString());
			productScanImageUrl.setIMGALTTXT(image.getAltTag());
			productScanImageUrl.setIMGSTATCD(image.getImageStatusCode());
			if (image.getImageStatusReason() != null){
				if (image.getImageStatusReason().length() > MAX_IMG_STATUS_REASON){
					productScanImageUrl.setIMGSTATRSNTXT(image.getImageStatusReason().substring(0, MAX_IMG_STATUS_REASON - 1));
				} else {
					productScanImageUrl.setIMGSTATRSNTXT(image.getImageStatusReason());
				}
			}
			productScanImageUrl.setCRE8ID(userId);
			productScanImageUrl.setLSTUPDTUID(userId);
			productScanImageUrl.setWORKID(DEFAULT_WORK_ID);
			productScanImageUrl.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());
			String currentTime = LocalDateTime.now().toString();
			productScanImageUrl.setCRE8TS(currentTime);
			productScanImageUrl.setLSTUPDTTS(currentTime);
			productScanImageUrl.setIMGCRETNTS(currentTime);
			productScanImageUrl.setREVDTS(currentTime);
			assignImages.add(productScanImageUrl);
		}
		request.getProductScanImageUrl().addAll(assignImages);
		this.updateProductAttribute(request);
	}

	/**
	 * Given a list of map prices (java entities), convert that to a list of webservice product scan map prices, and
	 * send that list to the webservice to be updated.
	 *
	 * @param mapPrices List of map prices to update.
	 * @param userId ID of the user making the change.
	 */
	public void updateFromProductInfo(Long productId, Boolean selectIngredients, List<MapPrice> mapPrices, String userId) {
		boolean setSomething = false;

		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		String psWorkId = this.getWorkId().toString();
		request.setTrackingNbr(psWorkId);
		request.setAuthentication(this.getAuthentication());

		// if there is nothing to update, just return
		if(mapPrices != null && !mapPrices.isEmpty()){
			request.getProductScanMapPrice().addAll(this.convertMapPricesToProductScanMapPriceList(mapPrices, psWorkId, userId));
			setSomething = true;
		}

		if (selectIngredients != null) {
			ProductMarketClaim productMarketClaim = new ProductMarketClaim();
			if (selectIngredients) {
				productMarketClaim.setACTIONCD(ACTION_CODE.ACTION_CD_ADD.getValue());
			} else {
				productMarketClaim.setACTIONCD(ACTION_CODE.ACTION_CD_DELETE.getValue());
			}
			productMarketClaim.setLSTUPDTUID(userId);
			productMarketClaim.setMKTCLMCD(MarketingClaim.Codes.SELECT_INGREDIENTS.getCode());
			productMarketClaim.setPRODID(productId.toString());
			productMarketClaim.setWORKID(psWorkId);
			request.getProductMarketClaim().add(productMarketClaim);
			setSomething = true;
		}

		if (setSomething) {
			this.updateProductAttribute(request);
		}
	}

	/**
	 * Convert a list of map prices to a list of webservice product scan map prices, and return the created list.
	 *
	 * @param mapPrices List of map prices to convert.
	 * @param workId Work id used for this webservice transaction.
	 * @param userId ID of the user making the change.
	 * @return List of product scan map prices to update.
	 */
	private List<ProductScanMapPrice> convertMapPricesToProductScanMapPriceList(List<MapPrice> mapPrices, String workId, String userId) {
		List<ProductScanMapPrice> toReturn = new ArrayList<>();
		DateTimeFormatter format = DateTimeFormatter.ofPattern(MAINFRAME_DATE_FORMAT);
		ProductScanMapPrice productScanMapPrice;
		for(MapPrice mapPrice : mapPrices) {
			productScanMapPrice = new ProductScanMapPrice();
			productScanMapPrice.setWORKID(workId);
			productScanMapPrice.setMAPAMT(BigDecimal.valueOf(mapPrice.getMapAmount()));
			productScanMapPrice.
					setSCNCDID(BigDecimal.valueOf(mapPrice.getKey().getUpc()));
			productScanMapPrice.setACTIONCD(mapPrice.getActionCode());
			productScanMapPrice.setEFFTS(mapPrice.getKey().getEffectiveTime().format(format));
			productScanMapPrice.setEXPRNTS(mapPrice.getExpirationTime().format(format));
			productScanMapPrice.setLSTUPDTUID(userId);
			if (ACTION_CODE.ACTION_CD_ADD.getValue().equals(mapPrice.getActionCode())) {
				productScanMapPrice.setCRE8UID(userId);
			}
			toReturn.add(productScanMapPrice);
		}
		return toReturn;
	}

	/**
	 * Sends request of added and removed attribute values.
	 *
	 * @param productAttributes The productAttributes that have attributes values that were added or removed.
	 */
	public void updateDynamicAttributes(List<ProductAttribute> productAttributes){
		if(CollectionUtils.isEmpty(productAttributes)){
			return;
		}
		UpdateProductAttributeRequest request = this.createUpdateProductAttributeRequest();
		request.getMasterDataExtnAttribute().addAll(
				this.convertProductAttributesToDynamicAttributes(productAttributes, request.getTrackingNbr()));
		this.updateProductAttribute(request);
	}

	/**
	 * Returns the a list of MasterDataExtnAttribute with the added and removed attribute values.
	 *
	 * @param productAttributes The productAttributes with added and removed attribute values.
	 * @param trackingId The trackingId.
	 * @return The MasterDataExtnAttributes
	 */
	private List<MasterDataExtnAttribute> convertProductAttributesToDynamicAttributes(List<ProductAttribute> productAttributes, String trackingId) {
		List<MasterDataExtnAttribute> toReturn = new ArrayList<>();
		MasterDataExtnAttribute attribute;
		for (ProductAttribute productAttribute : productAttributes){
			for(ProductAttributeValue productAttributeValue : productAttribute.getValues()){
				attribute = new MasterDataExtnAttribute();
				attribute.setATTRID(String.valueOf(productAttribute.getAttributeId()));
				attribute.setDTASRCSYS(String.valueOf(productAttributeValue.getSourceSystem()));
				attribute.setKEYID(String.valueOf(productAttribute.getKeyId()));
				attribute.setITMPRODKEYCD(productAttribute.getKeyType().trim());
				attribute.setSEQNBR(String.valueOf(productAttributeValue.getSequenceNumber()));
				attribute.setACTIONCD(productAttributeValue.getAction());
				if (ACTION_CODE.ACTION_CD_ADD.getValue().equals(productAttributeValue.getAction())) {
					attribute.setCRE8UID(this.userInfo.getUserId());
					attribute.setCRE8TS(LocalDateTime.now().toString());
				}
				attribute.setLSTUPDTUID(this.userInfo.getUserId());
				attribute.setLSTUPDTTS(LocalDateTime.now().toString());
				attribute.setATTRVALTXTTEXT(
						productAttributeValue.getText() != null ?
						productAttributeValue.getText() : StringUtils.EMPTY);
				attribute.setATTRVALTS(
						productAttributeValue.getTimestamp() != null ?
						productAttributeValue.getTimestamp()
								.format(ProductAttributeManagementServiceClient.formatter) : StringUtils.EMPTY);
				attribute.setATTRVALDT(
						productAttributeValue.getDate() != null ?
						productAttributeValue.getDate().toString() : StringUtils.EMPTY);
				attribute.setATTRVALNBR(
						productAttributeValue.getNumber() != null ?
						productAttributeValue.getNumber().toString() : StringUtils.EMPTY);
				attribute.setATTRCDID(
						productAttributeValue.getCode() != null ?
						productAttributeValue.getCode().toString() : StringUtils.EMPTY);
				attribute.setWORKID(trackingId);
				toReturn.add(attribute);
			}
		}
		return toReturn;
	}

	/**
	 * Returns a Update Product attribute request
	 *
	 * @return The UpdateProductAttributeRequest.
	 */
	private UpdateProductAttributeRequest createUpdateProductAttributeRequest() {
		UpdateProductAttributeRequest request = new UpdateProductAttributeRequest();
		String psWorkId = this.getWorkId().toString();
		request.setTrackingNbr(psWorkId);
		request.setAuthentication(this.getAuthentication());
		return request;
	}
}
