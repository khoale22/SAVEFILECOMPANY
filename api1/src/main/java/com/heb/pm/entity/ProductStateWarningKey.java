package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * An embeddable key for product state warning.
 *
 * @author m314029
 * @since 2.7.0
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class ProductStateWarningKey implements Serializable {

	// default constructor
	public ProductStateWarningKey(){super();}

	// copy constructor
	public ProductStateWarningKey(ProductStateWarningKey key) {
		super();
		this.setStateCode(key.getStateCode());
		this.setWarningCode(key.getWarningCode());
	}

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name="st_cd")
	@Type(type="fixedLengthCharPK")
	private String stateCode;

	@Column(name="st_prod_warn_cd")
	@Type(type="fixedLengthCharPK")
	private String warningCode;

	/**
	 * Returns the state code for this product state warning.
	 *
	 * @return The StateCode for the state the warning is applied to.
	 **/
	public String getStateCode() {
		return stateCode;
	}

	/**
	 * Sets the StateCode for the state the warning is applied to.
	 *
	 * @param stateCode The StateCode for the state the warning is applied to.
	 **/
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	/**
	 * Returns the warning code for this product state warning.
	 *
	 * @return The WarningCode that is assigned to identify the warning.
	 **/
	public String getWarningCode() {
		return warningCode;
	}

	/**
	 * Sets the WarningCode.
	 *
	 * @param warningCode The WarningCode.
	 **/
	public void setWarningCode(String warningCode) {
		this.warningCode = warningCode;
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

		ProductStateWarningKey that = (ProductStateWarningKey) o;

		if (stateCode != null ? !stateCode.equals(that.stateCode) : that.stateCode != null) return false;
		return warningCode != null ? warningCode.equals(that.warningCode) : that.warningCode == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = stateCode != null ? stateCode.hashCode() : 0;
		result = PRIME_NUMBER * result + (warningCode != null ? warningCode.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductStateWarningKey{" +
				"stateCode='" + stateCode + '\'' +
				", warningCode='" + warningCode + '\'' +
				'}';
	}
}
