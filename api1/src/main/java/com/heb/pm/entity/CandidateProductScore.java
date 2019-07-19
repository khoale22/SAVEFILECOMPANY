/*
 *  PsProdScore
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Represents a ps prod score.
 *
 * @author vn70529
 * @since 2.12
 */
@Entity
@Table(name = "ps_prod_score")
public class CandidateProductScore {

	/**
	 * The ps prod score.
	 */
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "psProdId", column = @Column(name = "PS_PROD_ID", nullable = false)),
			@AttributeOverride(name = "scoringOrgId", column = @Column(name = "SCORING_ORG_ID", nullable = false))
	})
	private CandidateProductScoreKey key;

	/**
	 * The ps product master.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PS_PROD_ID", nullable = false, insertable = false, updatable = false)
	private CandidateProductMaster candidateProductMaster;

	/**
	 * the prod score dt.
	 */
	@Column(name = "PROD_SCORE_DT", nullable = false)
	private LocalDate productScoreDate;

	/**
	 * The pro score nbr.
	 */
	@Column(name = "PROD_SCORE_NBR", nullable = true, precision = 7)
	private BigDecimal productScoreNumber;
	/**
	 * pro score max nbr.
	 */
	@Column(name = "PROD_SCORE_MAX_NBR", nullable = true, precision = 7)
	private BigDecimal productScoreMaxNumber;

	/**
	 * The prod score txt.
	 */
	@Column(name = "PROD_SCORE_TXT", nullable = true, length = 160)
	private String productScoreText;

	/**
	 * The prod score tst txt.
	 */
	@Column(name = "PROD_SCORE_TST_TXT", nullable = true, length = 8000)
	private String prodScoreTstText;

	/**
	 * The new data sw.
	 */
	@Column(name = "NEW_DATA_SW")
	private boolean newDataSw;


	/**
	 * The wine vintage txt.
	 */
	@Column(name = "WINE_VINTAGE_TXT", nullable = false)
	private String wineVintageText;

	/**
	 * Get ps prod score key.
	 *
	 * @return CandidateProductScoreKey
	 */
	public CandidateProductScoreKey getKey() {
		return key;
	}

	/**
	 * Set CandidateProductScoreKey
	 *
	 * @param key
	 */
	public void setKey(CandidateProductScoreKey key) {
		this.key = key;
	}

	/**
	 * Get psProductMaster object.
	 *
	 * @return psProductMaster object.
	 */
	public CandidateProductMaster getCandidateProductMaster() {
		return candidateProductMaster;
	}

	/**
	 * Set psProductMaster object.
	 *
	 * @param candidateProductMaster instance of psProductMaster.
	 */
	public void setCandidateProductMaster(CandidateProductMaster candidateProductMaster) {
		this.candidateProductMaster = candidateProductMaster;
	}

	/**
	 * Get prod score date.
	 *
	 * @return prod score date.
	 */
	public LocalDate getroductScoreDate() {
		return productScoreDate;
	}

	/**
	 * Set prod score date.
	 *
	 * @param productScoreDate prod score date.
	 */
	public void setProductScoreDate(LocalDate productScoreDate) {
		this.productScoreDate = productScoreDate;
	}

	/**
	 * Get prod score number.
	 *
	 * @return prod score number.
	 */
	public BigDecimal getProductScoreNumber() {
		return productScoreNumber;
	}

	/**
	 * Set prod score number.
	 *
	 * @param productScoreNumber pro score number.
	 */
	public void setProductScoreNumber(BigDecimal productScoreNumber) {
		this.productScoreNumber = productScoreNumber;
	}

	/**
	 * Get prod score max number.
	 *
	 * @return pro score max number.
	 */
	public BigDecimal getProductScoreMaxNumber() {
		return productScoreMaxNumber;
	}

	/**
	 * Set prod score max number.
	 *
	 * @param productScoreMaxNumber prod score max number.
	 */
	public void setProductScoreMaxNumber(BigDecimal productScoreMaxNumber) {
		this.productScoreMaxNumber = productScoreMaxNumber;
	}

	/**
	 * Get prod score text.
	 *
	 * @return prod score text.
	 */
	public String getProductScoreText() {
		return productScoreText;
	}

	/**
	 * Set prod score text.
	 *
	 * @param productScoreText prod score text.
	 */
	public void setProductScoreText(String productScoreText) {
		this.productScoreText = productScoreText;
	}

	/**
	 * Get prod score tst text.
	 *
	 * @return prod score tst text.
	 */
	public String getProdScoreTstText() {
		return prodScoreTstText;
	}

	/**
	 * Set prod score tst text.
	 *
	 * @param prodScoreTstText prod score tst text.
	 */
	public void setProdScoreTstText(String prodScoreTstText) {
		this.prodScoreTstText = prodScoreTstText;
	}

	/**
	 * Get new data sw.
	 *
	 * @return new data sw.
	 */
	public boolean getNewDataSw() {
		return newDataSw;
	}

	/**
	 * Set new data sw.
	 *
	 * @param newDataSw new data sw.
	 */
	public void setNewDataSw(boolean newDataSw) {
		this.newDataSw = newDataSw;
	}

	/**
	 * Get wine vintage txt.
	 *
	 * @return wine vintage txt.
	 */
	public String getWineVintageText() {
		return wineVintageText;
	}

	/**
	 * Set wine vintage txt.
	 *
	 * @param wineVintageText wine vintage txt.
	 */
	public void setWineVintageText(String wineVintageText) {
		this.wineVintageText = wineVintageText;
	}
	/**
	 * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
	 * it is inserted into the work request table.
	 */
	@PrePersist
	public void setCandidateProductId() {
		if (this.getKey().getCandidateProductId() == null) {
			this.getKey().setCandidateProductId(this.candidateProductMaster.getCandidateProductId());
		}
	}
}
