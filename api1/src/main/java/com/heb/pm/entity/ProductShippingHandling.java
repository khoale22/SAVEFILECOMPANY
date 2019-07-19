package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents Product Shipping Handling.
 *
 * @author m594201
 * @since 2.7.0
 */
@Entity
@Table(name = "prod_shpng_hndlg")
public class ProductShippingHandling implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s[%s]";

	@EmbeddedId
	private ProductShippingHandlingKey key;

	@Column(name="prod_id", insertable = false, updatable = false)
	private long prodId;

	@Column(name = "prod_shp_hndlg_cd", insertable = false, updatable = false)
	private String productShippingHandlingCode;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "prod_shp_hndlg_cd", referencedColumnName = "prod_shp_hndlg_cd", insertable = false, updatable = false)
	private ProductShippingHandlingType productShippingHandlingType;

	/**
	 * Gets id that is tied to the current product.
	 *
	 * @return the prod id that is tied to the current product.
	 */
	public long getProdId() {
		return prodId;
	}

	/**
	 * Sets id that is tied to the current product.
	 *
	 * @param prodId the prod id that is tied to the current product.
	 */
	public void setProdId(long prodId) {
		this.prodId = prodId;
	}

	/**
	 * Gets product shipping handling type that explains the size of the shipping container tied to other attributes.
	 *
	 * @return the product shipping handling type that explains the size of the shipping container tied to other attributes.
	 */
	public ProductShippingHandlingType getProductShippingHandlingType() {
		return productShippingHandlingType;
	}

	/**
	 * Sets product shipping handling type. The size of the shipping container tied to other attributes.
	 *
	 * @param productShippingHandlingType the product shipping handling type which is the size of the shipping container tied to other attributes.
	 */
	public void setProductShippingHandlingType(ProductShippingHandlingType productShippingHandlingType) {
		this.productShippingHandlingType = productShippingHandlingType;
	}

	/**
	 * Gets product shipping handling code, that is used to look up the special handling information.
	 *
	 * @return the product shipping handling code, that is used to look up the special handling information.
	 */
	public String getProductShippingHandlingCode() {
		return productShippingHandlingCode;
	}

	/**
	 * Sets product shipping handling code, that is used to look up the special handling information.
	 *
	 * @param productShippingHandlingCode the product shipping handling code, that is used to look up the special handling information.
	 */
	public void setProductShippingHandlingCode(String productShippingHandlingCode) {
		this.productShippingHandlingCode = productShippingHandlingCode;
	}

	/**
	 * Gets the composite key that ties this to the primary key of the table.
	 *
	 * @return the composite key that ties this to the primary key of the table.
	 */
	public ProductShippingHandlingKey getKey() {
		return key;
	}

	/**
	 * Sets composite key that ties this to the primary key of the table. .
	 *
	 * @param key the composite key that ties this to the primary key of the table.
	 */
	public void setKey(ProductShippingHandlingKey key) {
		this.key = key;
	}
}
