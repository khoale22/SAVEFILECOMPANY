/*
 *  MarketConsumerEventType
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.StringUtils;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a Market Consumer Event Type.
 * @author vn70529
 * @since 2.12.0
 */
@Entity
@Table(name = "mkt_consm_evnt_typ")
public class MarketConsumerEventType implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String MARKET_CONSUMER_EVENT_TYPE_SUMMARY_FORMAT="%s [%s]";

	@Id
	@Column(name = "mkt_consm_evnt_cd")
	private String marketConsumerEventCode;

	@Column(name = "mkt_consm_evnt_nm")
	private String marketConsumerEventName;

	@JsonIgnoreProperties("marketConsumerEventType")
	@OneToMany(mappedBy = "marketConsumerEventType", fetch = FetchType.LAZY)
	private List<ProductCategory> productCategory;

	/**
	 * get Product Category Role Code
	 *
	 * @return Product Category Role Code
	 */
	public String getMarketConsumerEventCode() {
		return marketConsumerEventCode;
	}

	/**
	 * Set the market Consumer Event Code
	 *
	 * @param marketConsumerEventCode The market Consumer Event Name
	 **/
	public void setMarketConsumerEventCode(String marketConsumerEventCode) {
		this.marketConsumerEventCode = marketConsumerEventCode;
	}

	/**
	 * get Market Consumer Event Name
	 *
	 * @return Market Consumer Event Name
	 */
	public String getMarketConsumerEventName() {
		return marketConsumerEventName;
	}

	/**
	 * Set the market Consumer Event Name
	 *
	 * @param marketConsumerEventName The market Consumer Event Name
	 **/
	public void setMarketConsumerEventName(String marketConsumerEventName) {
		this.marketConsumerEventName = marketConsumerEventName;
	}

	/**
	 * get summary field to show ui
	 * @return summary detail
	 */
	public String getMarketConsumerEventTypeSummary(){
		return String.format(MARKET_CONSUMER_EVENT_TYPE_SUMMARY_FORMAT, StringUtils.trimToEmpty(this.getMarketConsumerEventName()), StringUtils.trimToEmpty(this.getMarketConsumerEventCode()));
	}

	/**
	 * Compares another object to this one. If that object is a MarketConsumerEventType, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MarketConsumerEventType)) return false;

		MarketConsumerEventType marketConsumerEventType = (MarketConsumerEventType) o;

		return this.getMarketConsumerEventCode().equals(marketConsumerEventType.getMarketConsumerEventCode());
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return this.getMarketConsumerEventCode().hashCode();
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "MarketConsumerEventType{" +
				"marketConsumerEventCode=" + this.getMarketConsumerEventCode() +
				", marketConsumerEventCode='" + this.getMarketConsumerEventName() + '\'' +
				'}';
	}
}
