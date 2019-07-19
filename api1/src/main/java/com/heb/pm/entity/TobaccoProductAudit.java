package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This entity holds information for tobacco audit products
 */
@Entity
@Table(name = "TBCO_PROD_AUD")
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class TobaccoProductAudit implements Audit, Serializable {

	@EmbeddedId
	private TobaccoProductAuditKey key;

	@Column(name = "act_cd")
	private String action;

	@Column(name = "lst_updt_uid")
	private String changedBy;

	@AuditableField(displayName = "Tobacco Product Type",filter = FilterConstants.TOBACCO_AUDIT)
	@Column(name = "TBCO_PROD_TYP_CD")
	@Type(type = "fixedLengthCharPK")
	private String tobaccoProductTypeCode;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="TBCO_PROD_TYP_CD", referencedColumnName = "TBCO_PROD_TYP_CD", insertable = false, updatable = false, nullable = false)
	private TobaccoProductType tobaccoProductType;

	@Column(name="CIG_TAX_AMT")
	private BigDecimal cigTaxAmt;

	@Column(name="UNSTAMPED_SW")
	private Boolean unstampedSw;

	//bi-directional one-to-one association to GoodsProd
	@OneToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties("tobaccoProduct")
	@JoinColumn(name="PROD_ID", insertable = false, updatable = false)
	private GoodsProduct goodsProduct;

	/**
	 * Unique Identifier for the tobacco product
	 * @return prodId
	 */
	public Long getProdId() {
		return key.getProdId();
	}

	public TobaccoProductAuditKey getKey() {
		return key;
	}

	public void setKey(TobaccoProductAuditKey key) {
		this.key = key;
	}

	public Boolean getUnstampedSw() {
		return unstampedSw;
	}

	/**
	 * Updates the prodId
	 * @param prodId the new prodId
	 */
	public void setProdId(Long prodId) {
		this.key.setProdId(prodId);
	}

	/**
	 * The type of tobacco product identification code
	 * @return tobaccoProductTypeCode
	 */
	public String getTobaccoProductTypeCode() {
		return tobaccoProductTypeCode;
	}

	/**
	 * Updates the tobaccoProductTypeCode
	 * @param tobaccoProductTypeCode
	 */
	public void setTobaccoProductTypeCode(String tobaccoProductTypeCode) {
		this.tobaccoProductTypeCode = tobaccoProductTypeCode;
	}

	/**
	 * The tobacco type holds the class of the tobacco product and some tax information
	 * @return type
	 */
	public TobaccoProductType getTobaccoProductType() {
		return tobaccoProductType;
	}

	/**
	 * updates the tobaccoProdcutType
	 * @param tobaccoProductType
	 */
	public void setTobaccoProductType(TobaccoProductType tobaccoProductType) {
		this.tobaccoProductType = tobaccoProductType;
	}
	public Boolean isUnstampedSw() {
		return this.unstampedSw;
	}

	public void setUnstampedSw(Boolean unstampedSw) {
		this.unstampedSw = unstampedSw;
	}

	public GoodsProduct getGoodsProduct() {
		return this.goodsProduct;
	}

	public void setGoodsProduct(GoodsProduct goodsProduct) {
		this.goodsProduct = goodsProduct;
	}
	public BigDecimal getCigTaxAmt() {
		return this.cigTaxAmt;
	}

	public void setCigTaxAmt(BigDecimal cigTaxAmt) {
		this.cigTaxAmt = cigTaxAmt;
	}

	@Override
	public String toString() {
		return "TobaccoProductAudit{" +
				"key=" + key +
				", action='" + action + '\'' +
				", changedBy='" + changedBy + '\'' +
				", tobaccoProductTypeCode='" + tobaccoProductTypeCode + '\'' +
				", tobaccoProductType=" + tobaccoProductType +
				", cigTaxAmt=" + cigTaxAmt +
				", unstampedSw=" + unstampedSw +
				", goodsProduct=" + goodsProduct +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TobaccoProductAudit that = (TobaccoProductAudit) o;
		return Objects.equals(key, that.key) &&
				Objects.equals(action, that.action) &&
				Objects.equals(changedBy, that.changedBy) &&
				Objects.equals(tobaccoProductTypeCode, that.tobaccoProductTypeCode) &&
				Objects.equals(tobaccoProductType, that.tobaccoProductType) &&
				Objects.equals(cigTaxAmt, that.cigTaxAmt) &&
				Objects.equals(unstampedSw, that.unstampedSw) &&
				Objects.equals(goodsProduct, that.goodsProduct);
	}

	@Override
	public int hashCode() {

		return Objects.hash(key, action, changedBy, tobaccoProductTypeCode, tobaccoProductType, cigTaxAmt, unstampedSw, goodsProduct);
	}

	/**
	 * Getter for changedBy
	 * @return changedBy
	 */
	public String getChangedBy() {
		return changedBy;
	}

	/**
	 * Setter for changedBy
	 * @param changedBy
	 */
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	/**
	 * Getter for action
	 * @return action
	 */
	@Override
	public String getAction() {
		return action;
	}

	/**
	 * Setter for action
	 * @param action the action to set
	 */
	@Override
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Getter for changedOn attribute
	 * @return changedOn attribute
	 */
	@Override
	public LocalDateTime getChangedOn() {
		return key.getChangedOn();
	}

	/**
	 * Setter for changedOn
	 * @param changedOn The time the modification was done.
	 */
	public void setChangedOn(LocalDateTime changedOn) {
		this.key.setChangedOn(changedOn);
	}

}
