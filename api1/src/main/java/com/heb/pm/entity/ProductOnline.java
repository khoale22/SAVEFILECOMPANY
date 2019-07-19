/*
 *  ProductOnline
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a product online.
 *
 * @author vn70516
 * @since 2.14.0
 */
@Entity
@Table(name = "product_online")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductOnline implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String SHOW_ON_SITE_YES = "Y";
	public static final String SHOW_ON_SITE_NO = "N";


	@EmbeddedId
	private ProductOnlineKey key;

	@Column(name = "exprn_dt")
	@JsonFormat(
			pattern=DATE_FORMAT,
			shape=JsonFormat.Shape.STRING)
	private LocalDate expirationDate;

	@Column(name="prod_id_sw")
	private boolean showOnSite;

	@Transient
	private String action;
	/**
	 * @return Gets the value of key and returns key
	 */
	public void setKey(ProductOnlineKey key) {
		this.key = key;
	}

	/**
	 * Sets the key
	 */
	public ProductOnlineKey getKey() {
		return key;
	}

	/**
	 * @return Gets the value of expirationDate and returns expirationDate
	 */
	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * Sets the expirationDate
	 */
	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @return Gets the value of showOnSite and returns showOnSite
	 */
	public void setShowOnSite(boolean showOnSite) {
		this.showOnSite = showOnSite;
	}

	/**
	 * check show on site by expiration date more than current date
	 * @return
	 */
	public Boolean getShowOnSiteByExpirationDate(){
		if(getExpirationDate()!=null)
			return getExpirationDate().compareTo(LocalDate.now().plusDays(1L)) >= 0;
		else
			return false;
	}

	/**
	 * Sets the showOnSite
	 */
	public boolean isShowOnSite() {
		return showOnSite;
	}

	/**
	 * Get the action of ProductGroupChoiceOption.
	 *
	 * @return return the action add/update/delete ProductGroupChoiceOption.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets the action for ProductGroupChoiceOption.
	 *
	 * @param action the action of ProductGroupChoiceOption.
	 */
	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductOnline)) return false;

		ProductOnline that = (ProductOnline) o;

		if (isShowOnSite() != that.isShowOnSite()) return false;
		if (getKey() != null ? !getKey().equals(that.getKey()) : that.getKey() != null) return false;
		return getExpirationDate() != null ? getExpirationDate().equals(that.getExpirationDate()) : that.getExpirationDate() == null;
	}

	@Override
	public int hashCode() {
		int result = getKey() != null ? getKey().hashCode() : 0;
		result = 31 * result + (getExpirationDate() != null ? getExpirationDate().hashCode() : 0);
		result = 31 * result + (isShowOnSite() ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ProductOnline{" +
				"key=" + key +
				", expirationDate=" + expirationDate +
				", showOnSite=" + showOnSite +
				'}';
	}
}