/*
 * CandidateWorkRequest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;


/**
 * Represents a work request. This could be to setup a new product or update
 * an existing product through the batch update framework.
 *
 * @author d116773
 * @since 2.12.0
 */
@Entity
@Table(name="ps_work_rqst")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class CandidateWorkRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final Integer INTNT_ID_DEFAULT =21;
	public static final Integer INTNT_ID_UNASSIGN_PRODUCTS = 35;
	public static final int INTNT_ID_PUBLISH = 41;
	public static final int INTNT_ID_ASSIGNED_BDM = 61;
	public static final int INTNT_ID_ASSIGNED_EBM = 51;
	public static final Integer MOVE_PRODUCT_INTNT_ID = 35;
	public static final int SRC_SYSTEM_ID_DEFAULT = 4;
	public static final boolean RDY_TO_ACTVD_SW_DEFAULT = true;
	public static final String REQUEST_STATUS_WORKING = "107";
	public static final String REQUEST_STATUS_PASS = "108";
	public static final String REQUEST_STATUS_FAIL = "109";
	public static final String REQUEST_STATUS_DELETED = "104";
	public static final String REQUEST_STATUS_REJECTED = "105";
	public static final int INTNT_ID = 21;
	public static final String PD_SETUP_STAT_CD_BATCH_UPLOAD = "111";
	public static final String PD_SETUP_STAT_CD_FAILURE = "109";
	public static final int SRC_SYSTEM_ID = 4;
	public static final long STAT_CHG_RSN_ID_WRKG = 3;
	public static final int SRC_SYSTEM_ID_CPS = 1;
	public static final int INTNT_ID_CPS = 11;
	public static final int INTNT_ID_MODIFY_EXISTING_PRODUCT = 21;
	public static final int INTNT_ID_SERVICE_CASE_DESCRIPTION = 27;

	//Status code for candidate work request
	public enum StatusCode {
		REJECTED("105"),
		SUCCESS("108"),
		FAILURE("109"),
		IN_PROGRESS("111"),//also the default status for any new Batch Uploaded(mass update, excel upload) record.
		DELETED("104");
		private String name;
		StatusCode(String name) {
			this.name = name;
		}

		/**
		 * Gets name.
		 *
		 * @return the name
		 */
		public String getName() {
			return this.name;
		}
	}
	//Status code for candidate work request
	public enum INTENT {
		EXCEL_UPLOAD(28),
		INTRODUCE_A_VARIANT(10),
		APPROVE_PROCESS(27),
		UNAPPROVE_NUTRIENT(26);
		private int name;
		INTENT(int name) {
			this.name = name;
		}

		/**
		 * Gets name.
		 *
		 * @return the name
		 */
		public int getName() {
			return this.name;
		}
	}
	@Id
	@GeneratedValue(generator = "workRequestSequence")
	@SequenceGenerator(name = "workRequestSequence", sequenceName = "ps_work_rqst_seq")
	@Column(name="ps_work_id")
	private Long workRequestId;

	@Column(name="intnt_id")
	private Integer intent;

	@Column(name="pd_setup_stat_cd")
	@Type(type="fixedLengthCharPK")
	private String status;

	@Column(name="cre8_usr_id")
	@Type(type="fixedLengthChar")
	private String userId;

	@Column(name="src_system_id")
	private Integer sourceSystem;

	@Column(name="prod_id")
	private Long productId;

	@Column(name="scn_cd_id")
	private Long upc;

	@Column(name="cre8_ts")
	private LocalDateTime createDate;

	@Column(name="lst_updt_ts")
	private LocalDateTime lastUpdateDate;

	@Column(name="stat_chg_rsn_id")
	private Long statusChangeReason;

	@Column(name="rdy_to_actvd_sw")
	private Boolean readyToActivate;

	@Column(name="cand_updt_uid")
	@Type(type="fixedLengthCharPK")
	private String lastUpdateUserId;

	@Column(name="trx_trkg_id")
	private Long trackingId;

	@Column(name="delgted_by_usr_id")
	private String delegatedByUserId;

	@Column(name="delgted_ts")
	private LocalDateTime delegatedTime;

	@Column(name="itm_key_typ_cd")
	private String itemKeyTypeCode;

	@Column(name="itm_id")
	private Long itemId;

	@Transient
	private boolean uploadServeCaseSign;

	@OneToMany(mappedBy = "workRequest", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<CandidateDistributionFilter> candidateDistributionFilters;

	@JsonIgnoreProperties("candidateWorkRequest")
	@OneToMany(mappedBy = "candidateWorkRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CandidateProductMaster> candidateProductMaster;

	@JsonIgnoreProperties("candidateWorkRequest")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trx_trkg_id", referencedColumnName = "trx_trkg_id", insertable = false, updatable = false)
	private TransactionTracker transactionTracking;

	@OneToMany(mappedBy = "workRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<EffectiveDatedMaintenance> effectiveDatedMaintenances;

	@OneToMany(mappedBy = "candidateWorkRequest", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<CandidateStatus> candidateStatuses;

	@JsonIgnoreProperties("candidateWorkRequest")
	@OneToMany(mappedBy = "candidateWorkRequest", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<CandidateItemMaster> candidateItemMasters;
	
	@JsonIgnoreProperties("candidateWorkRequest")
    @OneToMany(mappedBy = "candidateWorkRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CandidateMasterDataExtensionAttribute> candidateMasterDataExtensionAttributes;

	@JsonIgnoreProperties("candidateWorkRequest")
	@OneToMany(mappedBy = "candidateWorkRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CandidateProductPkVariation> candidateProductPkVariations;

	@JsonIgnoreProperties({"candidateWorkRequest", "nutrientMaster", "servingSizeUOM", "sourceSystemData"})
	@OneToMany(mappedBy = "candidateWorkRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CandidateNutrient> candidateNutrients;

	@ManyToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name="prod_id" , referencedColumnName = "PROD_ID", insertable = false, updatable = false)
	private ProductMaster productMaster;

	@JsonIgnoreProperties("candidateWorkRequest")
	@OneToMany(mappedBy = "candidateWorkRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CandidateComments> candidateComments;

	@JsonIgnoreProperties("candidateWorkRequest")
	@OneToMany(mappedBy = "candidateWorkRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CandidateNutrientPanelHeader> candidateNutrientPanelHeaders;

	@JsonIgnoreProperties("candidateWorkRequest")
	@OneToMany(mappedBy = "candidateWorkRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CandidateGenericEntityRelationship> candidateGenericEntityRelationships;

	@JsonIgnoreProperties("candidateWorkRequest")
	@OneToMany(mappedBy = "candidateWorkRequest", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	private List<CandidateFulfillmentChannel> candidateFulfillmentChannels;

	@JsonIgnoreProperties("candidateWorkRequest")
	@OneToMany(mappedBy ="candidateWorkRequest", cascade = CascadeType.ALL)
	private List<CandidateClassCommodity> candidateClassCommodities;
	
	@JsonIgnoreProperties("candidateWorkRequest")
	@OneToMany(mappedBy = "candidateWorkRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CandidateProductScanCodeNutrient> candidateProductScanCodeNutrient;

	@Transient
	private PDPTemplate pdpTemplate;
	@Transient
	private String brand;
	@Transient
	private String size;
	@Transient
	private String displayName;

	@JsonIgnoreProperties("candidateWorkRequest")
	@OneToMany(mappedBy ="candidateWorkRequest", cascade = CascadeType.ALL)
	private List<CandidateProductOnline> candidateProductOnlines;

	/**
	 * Get the candidateProductScanCodeNutrient.
	 *
	 * @return the candidateProductScanCodeNutrient
	 */
	public List<CandidateProductScanCodeNutrient> getCandidateProductScanCodeNutrient() {
		return candidateProductScanCodeNutrient;
	}

	/**
	 * Set the candidateProductScanCodeNutrient.
	 *
	 * @param candidateProductScanCodeNutrient the candidateProductScanCodeNutrient to set
	 */
	public void setCandidateProductScanCodeNutrient(
			List<CandidateProductScanCodeNutrient> candidateProductScanCodeNutrient) {
		this.candidateProductScanCodeNutrient = candidateProductScanCodeNutrient;
	}

	/**
	 * Get the candidateNutrientPanelHeaders.
	 *
	 * @return the candidateNutrientPanelHeaders
	 */
	public List<CandidateNutrientPanelHeader> getCandidateNutrientPanelHeaders() {
		return candidateNutrientPanelHeaders;
	}

	/**
	 * Set the candidateNutrientPanelHeaders.
	 *
	 * @param candidateNutrientPanelHeaders the candidateNutrientPanelHeaders to set
	 */
	public void setCandidateNutrientPanelHeaders(List<CandidateNutrientPanelHeader> candidateNutrientPanelHeaders) {
		this.candidateNutrientPanelHeaders = candidateNutrientPanelHeaders;
	}

	/**
	 * Get the candidateNutrients.
	 *
	 * @return the candidateNutrients
	 */
	public List<CandidateNutrient> getCandidateNutrients() {
		return candidateNutrients;
	}

	/**
	 * Set the candidateNutrients.
	 *
	 * @param candidateNutrients the candidateNutrients to set
	 */
	public void setCandidateNutrients(List<CandidateNutrient> candidateNutrients) {
		this.candidateNutrients = candidateNutrients;
	}

	/**
	 * Returns the candidateProductMaster for this record.
	 *
	 * @return The candidateProductMaster for this record.
	 */
	public List<CandidateProductMaster> getCandidateProductMaster() {
		if (this.candidateProductMaster == null) {
			this.candidateProductMaster = new LinkedList<>();
		}
		return candidateProductMaster;
	}
	/**
	 * Sets the candidateProductMaster for this record.
	 *
	 * @param candidateProductMaster The candidateProductMaster for this record.
	 */
	public void setCandidateProductMaster(List<CandidateProductMaster> candidateProductMaster) {
		this.candidateProductMaster = candidateProductMaster;
	}

	public TransactionTracker getTransactionTracking() {
		return transactionTracking;
	}

	public void setTransactionTracking(TransactionTracker transactionTracking) {
		this.transactionTracking = transactionTracking;
	}

	/**
	 * Returns the work ID of this candidate request.
	 *
	 * @return The work ID of this candidate request.
	 */
	public Long getWorkRequestId() {
		return workRequestId;
	}

	/**
	 * Sets the work ID of this candidate request.
	 *
	 * @param workRequestId The work ID of this candidate request.
	 */
	public void setWorRequestId(Long workRequestId) {
		this.workRequestId = workRequestId;
	}

	/**
	 * Returns the intent of this candidate request. This may mean a new item, an update, etc.
	 *
	 * @return The intent of this candidate request.
	 */
	public Integer getIntent() {
		return intent;
	}

	/**
	 * Sets the intent of this candidate request.
	 *
	 * @param intent The intent of this candidate request.
	 */
	public void setIntent(Integer intent) {
		this.intent = intent;
	}

	/**
	 * Returns the status of this candidate request.
	 *
	 * @return The status of this candidate request.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status of this candidate request.
	 *
	 * @param status the status of this candidate request.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Returns the ID of the user who created this record.
	 *
	 * @return The ID of the user who created this record.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the ID of the user who created this record.
	 *
	 * @param userId The ID of the user who created this record.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Returns the ID of the system that crated this record.
	 *
	 * @return The ID of the system that crated this record.
	 */
	public Integer getSourceSystem() {
		return sourceSystem;
	}

	/**
	 * Sets the ID of the system that crated this record.
	 *
	 * @param sourceSystem The ID of the system that crated this record.
	 */
	public void setSourceSystem(Integer sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	/**
	 * Returns the product ID this work request is for.
	 *
	 * @return The product ID this work request is for.
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * Sets the product ID this work request is for.
	 *
	 * @param productId The product ID this work request is for.
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * Returns the UPC this work request is for.
	 *
	 * @return The UPC this work request is for.
	 */
	public Long getUpc() {
		return upc;
	}

	/**
	 * Sets rhe UPC this work request is for.
	 *
	 * @param upc The UPC this work request is for.
	 */
	public void setUpc(Long upc) {
		this.upc = upc;
	}

	/**
	 * Returns the date and time this record was created.
	 *
	 * @return The date and time this record was created.
	 */
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the date and time this record was created.
	 *
	 * @param createDate The date and time this record was created.
	 */
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	/**
	 * Returns the date and time this record was last updated.
	 *
	 * @return The date and time this record was last updated.
	 */
	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	/**
	 * Sets the date and time this record was last updated.
	 *
	 * @param lastUpdateDate The date and time this record was last updated.
	 */
	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	/**
	 * Returns the reason the status of this record was last changed.
	 *
	 * @return The reason the status of this record was last changed.
	 */
	public Long getStatusChangeReason() {
		return statusChangeReason;
	}

	/**
	 * Sets the reason the status of this record was last changed.
	 *
	 * @param statusChangeReason The reason the status of this record was last changed.
	 */
	public void setStatusChangeReason(Long statusChangeReason) {
		this.statusChangeReason = statusChangeReason;
	}

	/**
	 * Returns whether or not this candidate is ready for activation.
	 *
	 * @return True if it is ready and false otherwise.
	 */
	public Boolean getReadyToActivate() {
		return readyToActivate;
	}

	/**
	 * Sets whether or not this candidate is ready for activation.
	 *
	 * @param readyToActivate True if it is ready and false otherwise.
	 */
	public void setReadyToActivate(Boolean readyToActivate) {
		this.readyToActivate = readyToActivate;
	}

	/**
	 * Returns the one-pass ID of the last user to update this record.
	 *
	 * @return The one-pass ID of the last user to update this record.
	 */
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	/**
	 * Sets the one-pass ID of the last user to update this record.
	 *
	 * @param lastUpdateUserId The one-pass ID of the last user to update this record.
	 */
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}
	public boolean isUploadServeCaseSign() {
		return uploadServeCaseSign;
	}

	public void setUploadServeCaseSign(boolean uploadServeCaseSign) {
		this.uploadServeCaseSign = uploadServeCaseSign;
	}
	/**
	 * Returns the candidate distribution filters tied to this request.
	 *
	 * @return The candidate distribution filters tied to this request.
	 */
	public List<CandidateDistributionFilter> getCandidateDistributionFilters() {
		return candidateDistributionFilters;
	}

	/**
	 * Sets the candidate distribution filters tied to this request.
	 *
	 * @param candidateDistributionFilters The candidate distribution filters tied to this request.
	 */
	public void setCandidateDistributionFilters(List<CandidateDistributionFilter> candidateDistributionFilters) {
		this.candidateDistributionFilters = candidateDistributionFilters;
	}

	/**
	 * Returns the candidateStatuses for this record.
	 *
	 * @return The candidateStatuses for this record.
	 */
	public List<CandidateStatus> getCandidateStatuses() {
		if (this.candidateStatuses == null) {
			this.candidateStatuses = new LinkedList<>();
		}
		return candidateStatuses;
	}
	/**
	 * Sets the candidateStatuses for this record.
	 *
	 * @param candidateStatuses The candidateStatuses for this record.
	 */
	public void setCandidateStatuses(List<CandidateStatus> candidateStatuses) {
		this.candidateStatuses = candidateStatuses;
	}

	public List<CandidateItemMaster> getCandidateItemMasters() {
		return candidateItemMasters;
	}

	public void setCandidateItemMasters(List<CandidateItemMaster> candidateItemMasters) {
		this.candidateItemMasters = candidateItemMasters;
	}
    public List<CandidateMasterDataExtensionAttribute> getCandidateMasterDataExtensionAttributes() {
		return candidateMasterDataExtensionAttributes;
	}

	public void setCandidateMasterDataExtensionAttributes(List<CandidateMasterDataExtensionAttribute> candidateMasterDataExtensionAttributes) {
		this.candidateMasterDataExtensionAttributes = candidateMasterDataExtensionAttributes;
	}

	public List<CandidateProductPkVariation> getCandidateProductPkVariations() {
		return candidateProductPkVariations;
	}

	public CandidateWorkRequest setCandidateProductPkVariations(List<CandidateProductPkVariation> candidateProductPkVariations) {
		this.candidateProductPkVariations = candidateProductPkVariations;
		return this;
	}
	/**
	 * Returns the transaction tracking ID.
	 *
	 * @return The transaction tracking ID.
	 */
	public Long getTrackingId() {
		return trackingId;
	}

	/**
	 * Sets the transaction tracking ID.
	 *
	 * @param trackingId The transaction tracking ID.
	 */
	public void setTrackingId(Long trackingId) {
		this.trackingId = trackingId;
	}

	/**
	 * Returns delegated by user id.
	 * @return user id.
	 */
	public String getDelegatedByUserId() {
		return delegatedByUserId;
	}

	/**
	 * Sets delegated by user id.
	 * @param delegatedByUserId
	 */
	public void setDelegatedByUserId(String delegatedByUserId) {
		this.delegatedByUserId = delegatedByUserId;
	}

	/**
	 * Returns the delegated time.
	 * @return
	 */
	public LocalDateTime getDelegatedTime() {
		return delegatedTime;
	}

	/**
	 * Sets the delet
	 * @param delegatedTime
	 */
	public void setDelegatedTime(LocalDateTime delegatedTime) {
		this.delegatedTime = delegatedTime;
	}

	/**
	 * Returns the ItemKeyTypeCode. This is used to unassign products. Item key type code is set to 'ENTY'.
	 *
	 * @return ItemKeyTypeCode
	 */
	public String getItemKeyTypeCode() {
		return itemKeyTypeCode;
	}

	/**
	 * Sets the ItemKeyTypeCode
	 *
	 * @param itemKeyTypeCode The ItemKeyTypeCode
	 */
	public void setItemKeyTypeCode(String itemKeyTypeCode) {
		this.itemKeyTypeCode = itemKeyTypeCode;
	}

	/**
	 * Returns the ItemId. This is for un assign products. This is the root id of the hierarchy that is being unassigned.
	 *
	 * @return ItemId
	 */
	public Long getItemId() {
		return itemId;
	}

	/**
	 * Sets the ItemId
	 *
	 * @param itemId The ItemId
	 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	/**
	 * Returns a list of effetive dated maintenances that are tied to this work requst.
	 *
	 * @return A list of effetive dated maintenances that are tied to this work requst.
	 */
	public List<EffectiveDatedMaintenance> getEffectiveDatedMaintenances() {
		return effectiveDatedMaintenances;
	}

	/**
	 * Sets the list of effetive dated maintenances that are tied to this work requst.
	 *
	 * @param effectiveDatedMaintenances The list of effetive dated maintenances that are tied to this work requst.
	 */
	public void setEffectiveDatedMaintenances(List<EffectiveDatedMaintenance> effectiveDatedMaintenances) {
		this.effectiveDatedMaintenances = effectiveDatedMaintenances;
	}

	/**
	 * Retuns Product Master.
	 * @return product master.
	 */
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	/**
	 * Sets Product Master.
	 * @param productMaster
	 */
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	/**
	 * Returns Candidate comments (or notes) or a work request.
	 * @return list of comments
	 */
	public List<CandidateComments> getCandidateComments() {
		return candidateComments;
	}

	/**
	 * Sets candidate comments.
	 * @param candidateComments
	 */
	public void setCandidateComments(List<CandidateComments> candidateComments) {
		this.candidateComments = candidateComments;
	}

	/**
	 * Returns the CandidateGenericEntityRelationships
	 *
	 * @return CandidateGenericEntityRelationships
	 */
	public List<CandidateGenericEntityRelationship> getCandidateGenericEntityRelationships() {
		return candidateGenericEntityRelationships;
	}

	/**
	 * Sets the CandidateGenericEntityRelationships
	 *
	 * @param candidateGenericEntityRelationships The CandidateGenericEntityRelationships
	 */
	public void setCandidateGenericEntityRelationships(List<CandidateGenericEntityRelationship> candidateGenericEntityRelationships) {
		this.candidateGenericEntityRelationships = candidateGenericEntityRelationships;
	}

	/**
	 * Returns the CandidateFulfillmentChannels
	 *
	 * @return CandidateFulfillmentChannels
	 */
	public List<CandidateFulfillmentChannel> getCandidateFulfillmentChannels() {
		return candidateFulfillmentChannels;
	}

	/**
	 * Sets the CandidateFulfillmentChannels
	 *
	 * @param candidateFulfillmentChannels The CandidateFulfillmentChannels
	 */
	public void setCandidateFulfillmentChannels(List<CandidateFulfillmentChannel> candidateFulfillmentChannels) {
		this.candidateFulfillmentChannels = candidateFulfillmentChannels;
	}
	/**
	 * Returns the PDPTemplate.
	 *
	 * @return the PDPTemplate.
	 */
	public PDPTemplate getPdpTemplate() {
		return pdpTemplate;
	}

	/**
	 * Set  the PDPTemplate.
	 * @param pdpTemplate the PDPTemplate.
	 */
	public void setPdpTemplate(PDPTemplate pdpTemplate) {
		this.pdpTemplate = pdpTemplate;
	}

	/**
	 * @return Gets the value of brand and returns brand
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * Sets the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @return Gets the value of size and returns size
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * Sets the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * Returns the list of candidate product onlines.
	 * @return the list of candidate product onlines.
	 */
	public List<CandidateProductOnline> getCandidateProductOnlines() {
		if (this.candidateProductOnlines == null) {
			this.candidateProductOnlines = new LinkedList<>();
		}
		return candidateProductOnlines;
	}

	/**
	 * Sets the list of candidate product onlines.
	 * @param candidateProductOnlines the list of candidate product onlines.
	 */
	public void setCandidateProductOnlines(List<CandidateProductOnline> candidateProductOnlines) {
		this.candidateProductOnlines = candidateProductOnlines;
	}

	/**
	 * @return Gets the value of displayName and returns displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Sets the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Returns the list of candidate class commodity.
	 *
	 * @return the list of candidate class commodity.
	 */
	public List<CandidateClassCommodity> getCandidateClassCommodities() {
		return candidateClassCommodities;
	}

	/**
	 * Sets the list of candidate class commodity.
	 *
	 * @param candidateClassCommodities the list of candidate class commodity.
	 */
	public void setCandidateClassCommodities(List<CandidateClassCommodity> candidateClassCommodities) {
		this.candidateClassCommodities = candidateClassCommodities;
	}

	/**
	 * Compares this object to another for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CandidateWorkRequest)) return false;

		CandidateWorkRequest request = (CandidateWorkRequest) o;

		return !(workRequestId != null ? !workRequestId.equals(request.workRequestId) : request.workRequestId != null);

	}

	/**
	 * Returns a hash code for this object. Equal objects have the same hash code. Unequal objects have
	 * different hash codes.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		return workRequestId != null ? workRequestId.hashCode() : 0;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "CandidateWorkRequest{" +
				"workRequestId=" + workRequestId +
				", intent=" + intent +
				", status='" + status + '\'' +
				", userId='" + userId + '\'' +
				", sourceSystem=" + sourceSystem +
				", productId=" + productId +
				", upc=" + upc +
				", createDate=" + createDate +
				", lastUpdateDate=" + lastUpdateDate +
				", statusChangeReason=" + statusChangeReason +
				", readyToActivate=" + readyToActivate +
				", lastUpdateUserId='" + lastUpdateUserId + '\'' +
				", trackingId=" + trackingId +
				", delegatedByUserId='" + delegatedByUserId + '\'' +
				", delegatedTime=" + delegatedTime +
				", uploadServeCaseSign=" + uploadServeCaseSign +
				'}';
	}

}
