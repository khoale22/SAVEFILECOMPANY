package com.heb.pm.entity;

import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a Product Shipping Exception.
 *
 * @author m594201
 * @since 2.14.0
 */
@Entity
@Table(name = "prod_shpng_excp")
public class ProductShippingException implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s[%s]";

	@EmbeddedId
	private ProductShippingExceptionKey key;

	@ManyToOne
	@JoinColumn(name = "cust_shpng_meth_cd", referencedColumnName = "cust_shpng_meth_cd", insertable = false, updatable = false)
	private CustomShippingMethod customShippingMethod;

	/**
	 * Gets key.
	 *
	 * @return the key
	 */
	public ProductShippingExceptionKey getKey() {
		return key;
	}

	/**
	 * Sets key.
	 *
	 * @param key the key
	 */
	public void setKey(ProductShippingExceptionKey key) {
		this.key = key;
	}

	/**
	 * Gets custom shipping method.
	 *
	 * @return the custom shipping method
	 */
	public CustomShippingMethod getCustomShippingMethod() {
		return customShippingMethod;
	}

	/**
	 * Sets custom shipping method.
	 *
	 * @param customShippingMethod the custom shipping method
	 */
	public void setCustomShippingMethod(CustomShippingMethod customShippingMethod) {
		this.customShippingMethod = customShippingMethod;
	}
}
