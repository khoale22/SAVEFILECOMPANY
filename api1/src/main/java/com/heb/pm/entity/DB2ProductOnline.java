/*
 *  DB2ProductOnline.java
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package  com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heb.util.jpa.DateToLocalDateConverter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Represents a product online under DB2. This file and it's use must be refactored (removed) once all Alerts table are
 * moved to the Oracle.
 *
 * @author vn40486
 * @since 2.17.0
 */
@Entity
@Table(name = "product_online")
public class DB2ProductOnline implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	@EmbeddedId
	private DB2ProductOnlineKey key;

	@Column(name = "exprn_dt")
	private Date expirationDate;

	@Column(name="prod_id_sw")
	private boolean showOnSite;

	@Transient
	private String action;
	/**
	 * @return Gets the value of key and returns key
	 */
	public void setKey(DB2ProductOnlineKey key) {
		this.key = key;
	}

	/**
	 * Sets the key
	 */
	public DB2ProductOnlineKey getKey() {
		return key;
	}

	/**
	 * @return Gets the value of expirationDate and returns expirationDate
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * Sets the expirationDate
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @return Gets the value of showOnSite and returns showOnSite
	 */
	public void setShowOnSite(boolean showOnSite) {
		this.showOnSite = showOnSite;
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
		if (!(o instanceof DB2ProductOnline)) return false;

		DB2ProductOnline that = (DB2ProductOnline) o;

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
		return "DB2ProductOnline{" +
				"key=" + key +
				", expirationDate=" + expirationDate +
				", showOnSite=" + showOnSite +
				'}';
	}
}