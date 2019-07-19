package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a Product Warning.
 *
 * @author m594201
 * @since 2.14.0
 */
@Entity
@Table(name = "prod_warn")
public class ProductWarning implements Serializable {

	@EmbeddedId
	private ProductWarningKey key;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name="st_prod_warn_cd", referencedColumnName = "st_prod_warn_cd", insertable = false, updatable = false),
			@JoinColumn(name="st_cd", referencedColumnName = "st_cd", insertable = false, updatable = false)
	})
	private ProductStateWarning productStateWarning;

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
	 * Gets key.
	 *
	 * @return the key
	 */
	public ProductWarningKey getKey() {
		return key;
	}

	/**
	 * Sets key.
	 *
	 * @param key the key
	 */
	public void setKey(ProductWarningKey key) {
		this.key = key;
	}

	/**
	 * Gets product state warning.
	 *
	 * @return the product state warning
	 */
	public ProductStateWarning getProductStateWarning() {
		return productStateWarning;
	}

	/**
	 * Sets product state warning.
	 *
	 * @param productStateWarning the product state warning
	 */
	public void setProductStateWarning(ProductStateWarning productStateWarning) {
		this.productStateWarning = productStateWarning;
	}
}
