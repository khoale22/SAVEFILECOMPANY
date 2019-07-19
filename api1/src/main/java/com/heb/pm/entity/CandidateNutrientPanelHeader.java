package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The entity for the PS_NTRNT_PAN_HDR database table.
 *
 * @author vn47792
 * @since 2.15.0
 */
@Entity
@Table(name="PS_NTRNT_PAN_HDR")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class CandidateNutrientPanelHeader implements Serializable {
	
	@EmbeddedId
	private CandidateNutrientPanelHeaderKey key;

	@Column(name="SRVNG_SZ_TXT")
	@Type(type="fixedLengthCharPK")
	private String 	servingSizeText;

	@Column(name="spcr_txt")
	@Type(type="fixedLengthCharPK")
	private String servingsPerContainerText;

	@Column(name = "xtrnl_id")
	private String xtrnlId;

	@Column(name = "APPR_SW")
	private Boolean approved;

	@Column(name = "APPR_BY_USER_ID")
	@Type(type="fixedLengthCharPK")
	private String approvedBy;

	@Column(name = "APPR_TS")
	private LocalDateTime approvedTime;

	@Column(name = "SRC_TS")
	private LocalDateTime sourceTime;

	@JsonIgnoreProperties({"candidateMasterDataExtensionAttributes", "candidateProductPkVariations", "candidateNutrients", "candidateNutrientPanelHeaders", "candidateGenericEntityRelationship", "candidateFulfillmentChannels"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="PS_WORK_ID", referencedColumnName = "PS_WORK_ID", insertable = false, updatable = false, nullable = false),
		@JoinColumn(name="SCN_CD_ID", referencedColumnName = "SCN_CD_ID", insertable = false, updatable = false, nullable = false)
	})
	private CandidateWorkRequest candidateWorkRequest;

	/**
	 * Get the key.
	 *
	 * @return the key
	 */
	public CandidateNutrientPanelHeaderKey getKey() {
		return key;
	}

	/**
	 * Set the key.
	 *
	 * @param key the key to set
	 */
	public void setKey(CandidateNutrientPanelHeaderKey key) {
		this.key = key;
	}

	/**
	 * Get the servingSizeText.
	 *
	 * @return the servingSizeText
	 */
	public String getServingSizeText() {
		return servingSizeText;
	}

	/**
	 * Set the servingSizeText.
	 *
	 * @param servingSizeText the servingSizeText to set
	 */
	public void setServingSizeText(String servingSizeText) {
		this.servingSizeText = servingSizeText;
	}

	/**
	 * Get the servingsPerContainerText.
	 *
	 * @return the servingsPerContainerText
	 */
	public String getServingsPerContainerText() {
		return servingsPerContainerText;
	}

	/**
	 * Set the servingsPerContainerText.
	 *
	 * @param servingsPerContainerText the servingsPerContainerText to set
	 */
	public void setServingsPerContainerText(String servingsPerContainerText) {
		this.servingsPerContainerText = servingsPerContainerText;
	}

	/**
	 * Get the xtrnlId.
	 *
	 * @return the xtrnlId
	 */
	public String getXtrnlId() {
		return xtrnlId;
	}

	/**
	 * Set the xtrnlId.
	 *
	 * @param xtrnlId the xtrnlId to set
	 */
	public void setXtrnlId(String xtrnlId) {
		this.xtrnlId = xtrnlId;
	}

	/**
	 * Get the approved.
	 *
	 * @return the approved
	 */
	public Boolean getApproved() {
		return approved;
	}

	/**
	 * Set the approved.
	 *
	 * @param approved the approved to set
	 */
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	/**
	 * Get the approvedBy.
	 *
	 * @return the approvedBy
	 */
	public String getApprovedBy() {
		return approvedBy;
	}

	/**
	 * Set the approvedBy.
	 *
	 * @param approvedBy the approvedBy to set
	 */
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	/**
	 * Get the approvedTime.
	 *
	 * @return the approvedTime
	 */
	public LocalDateTime getApprovedTime() {
		return approvedTime;
	}

	/**
	 * Set the approvedTime.
	 *
	 * @param approvedTime the approvedTime to set
	 */
	public void setApprovedTime(LocalDateTime approvedTime) {
		this.approvedTime = approvedTime;
	}

	/**
	 * Get the sourceTime.
	 *
	 * @return the sourceTime
	 */
	public LocalDateTime getSourceTime() {
		return sourceTime;
	}

	/**
	 * Set the sourceTime.
	 *
	 * @param sourceTime the sourceTime to set
	 */
	public void setSourceTime(LocalDateTime sourceTime) {
		this.sourceTime = sourceTime;
	}

	/**
	 * Get the candidateWorkRequest.
	 *
	 * @return the candidateWorkRequest
	 */
	public CandidateWorkRequest getCandidateWorkRequest() {
		return candidateWorkRequest;
	}

	/**
	 * Set the candidateWorkRequest.
	 *
	 * @param candidateWorkRequest the candidateWorkRequest to set
	 */
	public void setCandidateWorkRequest(CandidateWorkRequest candidateWorkRequest) {
		this.candidateWorkRequest = candidateWorkRequest;
	}
}
