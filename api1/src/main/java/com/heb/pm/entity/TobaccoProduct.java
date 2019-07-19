package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * This entity holds information for tobacco products
 * @author s753601
 * @version 2.13.0
 */
@Entity
@Table(name = "TBCO_PROD")
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class TobaccoProduct {

	@Id
	@Column(name = "PROD_ID")
	private Long prodId;

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
		return prodId;
	}

	/**
	 * Updates the prodId
	 * @param prodId the new prodId
	 */
	public void setProdId(Long prodId) {
		this.prodId = prodId;
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

	/**
	 * Compares another object to this one. If that object is a ImportItem, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GoodsProduct)) return false;

		TobaccoProduct that = (TobaccoProduct) o;

		if (prodId != null ? !prodId.equals(that.prodId) : that.prodId != null) return false;
		return true;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		if(prodId == null){
			return 0;
		}
		return prodId.hashCode();
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "TobaccoProduct{" +
				"prodId=" + prodId +
				", tobaccoProductTypeCode='" + tobaccoProductTypeCode + '\'' +
				", tobaccoProductType=" + tobaccoProductType +
				'}';
	}
}
