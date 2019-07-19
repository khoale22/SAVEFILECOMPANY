package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents a Product Shipping Handling Type.
 *
 * @author m594201
 * @since 2.7.0
 */
@Entity
@Table(name = "prod_shp_hndlg_typ")
public class ProductShippingHandlingType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "prod_shp_hndlg_cd")
	private String productShippingHandlingCode;

	@Column(name = "prod_shp_hndlg_abb")
	private String productShippingHandlingAbbreviation;

	@Column(name = "prod_shp_hndlg_des")
	private String productShippingHandlingDescription;

	/**
	 * Gets product shipping handling description, for special handling instructions.
	 *
	 * @return the product shipping handling description, for special handling instructions.
	 */
	public String getProductShippingHandlingDescription() {
		return productShippingHandlingDescription.trim();
	}

	/**
	 * Sets product shipping handling description, for special handling instructions.
	 *
	 * @param productShippingHandlingDescription the product shipping handling description, for special handling instructions
	 */
	public void setProductShippingHandlingDescription(String productShippingHandlingDescription) {
		this.productShippingHandlingDescription = productShippingHandlingDescription;
	}

	/**
	 * Gets product shipping handling code, used to pull shipping handling details.
	 *
	 * @return the product shipping handling code, used to pull shipping handling details.
	 */
	public String getProductShippingHandlingCode() {
		return productShippingHandlingCode;
	}

	/**
	 * Sets product shipping handling code, used to pull shipping handling details.
	 *
	 * @param productShippingHandlingCode the product shipping handling code, used to pull shipping handling details.
	 */
	public void setProductShippingHandlingCode(String productShippingHandlingCode) {
		this.productShippingHandlingCode = productShippingHandlingCode;
	}

	/**
	 * Gets product shipping handling abriviation, the text used to display the handling information.
	 *
	 * @return the product shipping handling abriviation, the text used to display the handling information.
	 */
	public String getProductShippingHandlingAbbreviation() {
		return productShippingHandlingAbbreviation;
	}

	/**
	 * Sets product shipping handling abbreviation, the text used to display the handling information.
	 *
	 * @param productShippingHandlingAbbreviation the product shipping handling abbreviation, the text used to display the handling information.
	 */
	public void setProductShippingHandlingAbbreviation(String productShippingHandlingAbbreviation) {
		this.productShippingHandlingAbbreviation = productShippingHandlingAbbreviation;
	}
}
