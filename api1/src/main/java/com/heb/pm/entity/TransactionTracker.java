/*
 * TransactionTracker
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author d116773
 * @update vn87351
 *
 */
@Entity
@Table(name="trx_tracking")
public class TransactionTracker implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String SUMMARY_UNKNOWN = "NA";
	public static final String STAT_CODE_COMPLETE = "102";
	public static final String STAT_CODE_NOT_COMPLETE = "101";
	public static final String ROLE_CODE_USER = "USER";

	/**
	 * In many cases, field FILE_NM in TRX_TRACKING table is re-purposed to classify the type of the batch
	 * request. It specifies what attribute(or type of attributes) is affected in a particular
	 * batch upload request(tracking id). The batch request can be an excel upload or a mass update.
	 */
	public static final String FILE_NAME_ATTRIBUTE_TASKS = "TASKS";

	public static final String USER_ROLE_CODE = "USER";
	/**
	 * file name code enum
	 */
	public enum FileNameCode {
		PRODUCT_NEW("NWPRD"),
		PRODUCT_UPDATE("PRUPD"),
		PRODUCT_WRITE("PRRVW"),
		NEW_IMAGE("NWIMG"),
		TASKS("TASKS"),
		IMAGE_CONTENT_TYPE("image"),
		IMAGE_ATTRIBUTES("Image Attributes"),
		PRODUCT_ATTRIBUTES("Product Attributes"),
		PUBLISH_PRODUCT("Publish");

		private String name;
		FileNameCode(String name) {
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
	@Id
	@GeneratedValue(generator = "trackingIdSequence")
	@SequenceGenerator(name = "trackingIdSequence", sequenceName = "trx_tracking_seq")
	@Column(name="trx_trkg_id")
	private Long trackingId;

	@Column(name="trx_src_cd")
	private String source;

	@Column(name="cre8_ts",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
	private LocalDateTime createDate;

	@Column(name="cre8_usr_id")
	private String userId;

	@Column(name="usr_role_cd")
	private String userRole;

	@Column(name = "IC_CNTL_NBR")
	private Long icCntlNbr;

	@Column(name = "GRP_CNTL_NBR")
	private Long grpCntlNbr;

	@Column(name = "TRX_CNTL_NBR")
	private Long trxCntlNbr;

	@Column(name = "FILE_NM")
	private String fileNm;

	@Column(name = "FILE_DES")
	private String fileDes;

	@Column(name = "TRX_STAT_CD")
	private String trxStatCd;

@JsonIgnoreProperties("transactionTracking")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transactionTracking")
	private List<CandidateWorkRequest> candidateWorkRequest;

	public Long getIcCntlNbr() {
		return icCntlNbr;
	}

	public void setIcCntlNbr(Long icCntlNbr) {
		this.icCntlNbr = icCntlNbr;
	}

	public Long getGrpCntlNbr() {
		return grpCntlNbr;
	}

	public void setGrpCntlNbr(Long grpCntlNbr) {
		this.grpCntlNbr = grpCntlNbr;
	}

	public Long getTrxCntlNbr() {
		return trxCntlNbr;
	}

	public void setTrxCntlNbr(Long trxCntlNbr) {
		this.trxCntlNbr = trxCntlNbr;
	}

	public String getFileNm() {
		return fileNm;
	}

	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}

	public String getFileDes() {
		return fileDes;
	}

	public void setFileDes(String fileDes) {
		this.fileDes = fileDes;
	}

	public String getTrxStatCd() {
		return trxStatCd;
	}

	public void setTrxStatCd(String trxStatCd) {
		this.trxStatCd = trxStatCd;
	}

	public void setCandidateWorkRequest(List<CandidateWorkRequest> candidateWorkRequest) {
		this.candidateWorkRequest = candidateWorkRequest;
	}

	public List<CandidateWorkRequest> getCandidateWorkRequest() {
		return candidateWorkRequest;
	}
	public Long getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(Long trackingId) {
		this.trackingId = trackingId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TransactionTracker)) return false;

		TransactionTracker tracking = (TransactionTracker) o;

		return !(trackingId != null ? !trackingId.equals(tracking.trackingId) : tracking.trackingId != null);

	}

	@Override
	public int hashCode() {
		return trackingId != null ? trackingId.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "Tracking{" +
				"trackingId=" + trackingId +
				", source='" + source + '\'' +
				", createDate=" + createDate +
				", userId='" + userId + '\'' +
				", userRole='" + userRole + '\'' +
				'}';
	}
	/**
	 * Returns the default sort order for the Tracking table.
	 *
	 * @return The default sort order for the Tracking table.
	 */
	public static Sort getDefaultSort() {
		return  new Sort(
				new Sort.Order(Sort.Direction.DESC, "createDate")
		);
	}
}
