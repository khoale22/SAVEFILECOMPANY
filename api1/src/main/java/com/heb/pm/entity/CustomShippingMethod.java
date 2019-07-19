package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a custom shipping method.
 *
 * @author m594201
 * @since 2.14.0
 */
@Entity
@Table(name = "cust_shpng_meth")
public class CustomShippingMethod implements Serializable {

	@Id
	@Column(name = "cust_shpng_meth_cd")
	private String customShippingMethod;

	@Column(name = "cust_shpng_meth_ABB")
	private String customShippingAbbriviation;

	@Column(name = "cust_shpng_meth_des")
	private String customShippingMethodDescription;

	@Transient
	private String actionCode;

	/**
	 * Gets action code.
	 *
	 * @return the action code
	 */
	public String getActionCode() {
		return actionCode;
	}

	/**
	 * Sets action code.
	 *
	 * @param actionCode the action code
	 */
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	/**
	 * Gets custom shipping method.
	 *
	 * @return the custom shipping method
	 */
	public String getCustomShippingMethod() {
		return customShippingMethod;
	}

	/**
	 * Sets custom shipping method.
	 *
	 * @param customShippingMethod the custom shipping method
	 */
	public void setCustomShippingMethod(String customShippingMethod) {
		this.customShippingMethod = customShippingMethod;
	}

	/**
	 * Gets custom shipping abbriviation.
	 *
	 * @return the custom shipping abbriviation
	 */
	public String getCustomShippingAbbriviation() {
		return customShippingAbbriviation;
	}

	/**
	 * Sets custom shipping abbriviation.
	 *
	 * @param customShippingAbbriviation the custom shipping abbriviation
	 */
	public void setCustomShippingAbbriviation(String customShippingAbbriviation) {
		this.customShippingAbbriviation = customShippingAbbriviation;
	}

	/**
	 * Gets custom shipping method description.
	 *
	 * @return the custom shipping method description
	 */
	public String getCustomShippingMethodDescription() {
		return customShippingMethodDescription;
	}

	/**
	 * Sets custom shipping method description.
	 *
	 * @param customShippingMethodDescription the custom shipping method description
	 */
	public void setCustomShippingMethodDescription(String customShippingMethodDescription) {
		this.customShippingMethodDescription = customShippingMethodDescription;
	}
}
