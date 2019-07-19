package com.heb.pm.entity;

import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a product warning for a specific state (i.e. This product contains chemicals known to California to
 * cause cancer).
 *
 * @author m314029
 * @since 2.7.0
 */
@Entity
@Table(name = "st_prod_warn")
public class ProductStateWarning implements Serializable {

	// default constructor
	public ProductStateWarning(){super();}

	// copy constructor
	public ProductStateWarning(ProductStateWarning productStateWarning) {
		super();
		this.setKey(new ProductStateWarningKey(productStateWarning.getKey()));
		this.setDescription(productStateWarning.getDescription());
		this.setAbbreviation(productStateWarning.getAbbreviation());
	}

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_STATE_WARNINGS_SORT_FIELD = "key.warningCode";

	@EmbeddedId
	private ProductStateWarningKey key;

	@Column(name="st_prod_warn_abb")
	private String abbreviation;

	@Column(name="st_prod_warn_des")
	private String description;

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
	 * Returns Composite key to pull primary key(s).
	 *
	 * @return The Composite key to pull primary key(s).
	 **/
	public ProductStateWarningKey getKey() {
		return key;
	}

	/**
	 * Sets the Key.
	 *
	 * @param key Composite key to pull primary key(s).
	 **/
	public void setKey(ProductStateWarningKey key) {
		this.key = key;
	}

	/**
	 * Returns the abbreviation which is used for the state warning.
	 *
	 * @return the abbreviation which is used for the state warning.
	 **/
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Sets the abbreviation which is used for the state warning.
	 *
	 * @param abbreviation the abbreviation which is used for the state warning.
	 **/
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * Returns Description which is used for detail the state warning.
	 *
	 * @return The Description.
	 **/
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Description which is used for detail the state warning.
	 *
	 * @param description The Description which is used for detail the state warning.
	 **/
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the default sort order by warning code for the state warnings table.
	 *
	 * @return The default sort order by warning code for the state warnings table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(new Sort.Order(Sort.Direction.DESC, ProductStateWarning.DEFAULT_STATE_WARNINGS_SORT_FIELD));
	}

	/**
	 * Compares another object to this one. The key is the only thing used to determine equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductStateWarning that = (ProductStateWarning) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductStateWarning{" +
				"key=" + key +
				", abbreviation='" + abbreviation + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
