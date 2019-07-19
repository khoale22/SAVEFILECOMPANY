/*
 *  ProductMarketingClaim
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
/**
 * Represents a product marketing claim.
 *
 * @author s573181
 * @since 2.6.0
 */
@Entity
@Table(name = "prod_mkt_clm")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
//dB2Oracle changes vn00907
public class ProductMarketingClaim implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String APPROVED = "A    ";
	public static final String SUBMITTED = "S    ";
	public static final String REJECTED = "R    ";

	/**
	 * Status primo pick constant.
	 */
	public enum PRIMO_PICK_STATUS {
		/**
		 * Active status.
		 *  When primo pick status is approved and Today date is in the range effective date and expiration date.
		 *  example:
		 *  	today date is 10/10/2018
		 *  	effective date is 9/9/2018
		 *  	expiration date is 11/11/2018
		 *	=> primo pick status is 'Active'
		 *
		 */
		ACTIVE("Active"),
		/**
		 * Inactive status.
		 * When primo pick status is approved and Today date is not in the range effective date and expiration date.
		 * example:
		 * 		 *  	today date is 10/10/2018
		 * 		 *  	effective date is 11/11/2018
		 * 		 *  	expiration date is 12/12/2018
		 * 		 *	=> 	primo pick status is 'Inactive'
		 *
		 */
		INACTIVE("Inactive"),
		/**
		 * Submitted status. When primo pick status is Submitted
		 */
		SUBMITTED("Submitted"),
		/**
		 * Rejected status. When primo pick status is Rejected
		 */
		REJECTED("Rejected");

		private String name;

		PRIMO_PICK_STATUS(String name) {
		 	this.name = name;
		}
		/**
		 * Gets name status.
		 *
		 * @return the name
		 */
		public String getName() {
			return this.name;
		}
	}

	public static final int NOOP = 0;
	public static final int INSERT = 1;
	public static final int UPDATE = 2;
	public static final int DELETE = 3;

	@EmbeddedId
	private ProductMarketingClaimKey key;

	@Column(name = "mkt_clm_stat_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String marketingClaimStatusCode;

	@Column(name = "promo_pck_eff_dt")
	private LocalDate promoPickEffectiveDate;

	@Column(name = "promo_pck_exprn_dt")
	private LocalDate promoPickExpirationDate;

	@Column(name = "stat_chg_rsn_txt")
	private String statusChangeReason;

	@Column(name = "lst_updt_uid")
	private String lastUpdateId;

	@OneToOne
	@JoinColumn(name = "mkt_clm_cd", referencedColumnName = "mkt_clm_cd", insertable = false, updatable = false, nullable = false)
	private MarketingClaim marketingClaim;

	@Transient
	private int action;

	/**
	 * Returns the key for this object.
	 *
	 * @return the key for this object.
	 */
	public ProductMarketingClaimKey getKey() {
		return key;
	}

	/**
	 * Set the key for this object.
	 *
	 * @param key the key for this object.
	 */
	public void setKey(ProductMarketingClaimKey key) {
		this.key = key;
	}

	/**
	 * Returns the Marketing Claim Status Code.
	 * "R" rejected
	 * "A" approved
	 * "S" submitted
	 * @return the Marketing Claim Status Code.
	 */
	public String getMarketingClaimStatusCode() {
		return marketingClaimStatusCode;
	}

	/**
	 * Sets the Marketing Claim Status Code.
	 *
	 * @param marketingClaimStatusCode the Marketing Claim Status Code.
	 */
	public void setMarketingClaimStatusCode(String marketingClaimStatusCode) {
		this.marketingClaimStatusCode = marketingClaimStatusCode;
	}

	/**
	 * Returns the promotion pick effective date.
	 *
	 * @return the promotion pick effective date.
	 */
	public LocalDate getPromoPickEffectiveDate() {
		return promoPickEffectiveDate;
	}

	/**
	 * Sets the promotion pick effective date.
	 *
	 * @param promoPickEffectiveDate the promotion pick effective date.
	 */
	public void setPromoPickEffectiveDate(LocalDate promoPickEffectiveDate) {
		this.promoPickEffectiveDate = promoPickEffectiveDate;
	}

	/**
	 * Returns the promotion pick expiration date.
	 * 
	 * @return the promotion pick expiration date.
	 */
	public LocalDate getPromoPickExpirationDate() {
		return promoPickExpirationDate;
	}

	/**
	 * Sets the promotion pick expiration date.
	 *
	 * @param promoPickExpirationDate the promotion pick expiration date.
	 */
	public void setPromoPickExpirationDate(LocalDate promoPickExpirationDate) {
		this.promoPickExpirationDate = promoPickExpirationDate;
	}

	/**
	 * Returns the status change reason.
	 *
	 * @return the status change reason.
	 */
	public String getStatusChangeReason() {
		return statusChangeReason;
	}

	/**
	 * Sets the status change reason.
	 * 
	 * @param statusChangeReason the status change reason.
	 */
	public void setStatusChangeReason(String statusChangeReason) {
		this.statusChangeReason = statusChangeReason;
	}

	/**
	 * Returns the last updated by user id.
	 *
	 * @return the last updated by user id.
	 */
	public String getLastUpdateId() {
		return lastUpdateId;
	}

	/**
	 * Sets the last updated by user id.
	 * 
	 * @param lastUpdateId the last updated by user id.
	 */
	public void setLastUpdateId(String lastUpdateId) {
		this.lastUpdateId = lastUpdateId;
	}

	/**
	 * Returns the marketing claim object.
	 *
	 * @return the marketing claim object.
	 */
	public MarketingClaim getMarketingClaim() {
		return marketingClaim;
	}

	/**
	 * Sets the marketing claim object.
	 *
	 * @param marketingClaim the marketing claim object.
	 */
	public void setMarketingClaim(MarketingClaim marketingClaim) {
		this.marketingClaim = marketingClaim;
	}

	/**
	 * Returns the action to perform with this object (insert, update, delete).
	 *
	 * @return The action to perform with this object.
	 */
	public int getAction() {
		return action;
	}

	/**
	 * Sets the action to perform with this object.
	 *
	 * @param action The action to perform with this object.
	 */
	public void setAction(int action) {
		this.action = action;
	}

	/**
	 * Get primo pick status, base on Marketing Claim Status Code and Marketing Claim Code is primo pick
	 * @return
	 */
	public String getPrimoPickStatus(){
		if(StringUtils.trimToEmpty(getMarketingClaimStatusCode()).equals(APPROVED.trim())){
			if(isValidEffectiveDate() && isValidExpirationDate()){
				return PRIMO_PICK_STATUS.ACTIVE.getName();
			}else {
				return PRIMO_PICK_STATUS.INACTIVE.getName();
			}
		}else if(StringUtils.trimToEmpty(getMarketingClaimStatusCode()).equals(SUBMITTED.trim())){
			return PRIMO_PICK_STATUS.SUBMITTED.getName();
		}else{
			return PRIMO_PICK_STATUS.REJECTED.getName();
		}
	}

	/**
	 * Validate effective date for primo pick. If effective date is equal or before Today date, they will return true and vice versa.
	 * @return
	 */
	private boolean isValidEffectiveDate(){
		if(getPromoPickEffectiveDate()!=null &&
				(getPromoPickEffectiveDate().isBefore(LocalDate.now()) || getPromoPickEffectiveDate().isEqual(LocalDate.now()))) {
			return true;
		}
		return false;
	}

	/**
	 * Validate expiration date for primo pick. If expiration date is equal or after Today date, they will return true and vice versa.
	 * @return
	 */
	private boolean isValidExpirationDate(){
		if(getPromoPickExpirationDate()!=null &&
				(getPromoPickExpirationDate().isAfter(LocalDate.now()) || getPromoPickExpirationDate().isEqual(LocalDate.now()))) {
			return true;
		}
		return false;
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
		if (!(o instanceof ProductMarketingClaim)) return false;

		ProductMarketingClaim that = (ProductMarketingClaim) o;

		return key.equals(that.key);
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return key.hashCode();
	}
	
	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductMarketingClaim{" +
				"key=" + key +
				", marketingClaimStatusCode='" + marketingClaimStatusCode + '\'' +
				", promoPickEffectiveDate=" + promoPickEffectiveDate +
				", promoPickExpirationDate=" + promoPickExpirationDate +
				", statusChangeReason='" + statusChangeReason + '\'' +
				", lastUpdateId='" + lastUpdateId + '\'' +
				'}';
	}
}
