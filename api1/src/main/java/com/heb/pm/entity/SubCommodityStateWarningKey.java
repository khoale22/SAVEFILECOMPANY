package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * An embeddable key for sub commodity state warning
 *
 * @author m314029
 * @since 2.7.0
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class SubCommodityStateWarningKey implements Serializable {

	// default constructor
	public SubCommodityStateWarningKey(){super();}

	// copy constructor
	public SubCommodityStateWarningKey(SubCommodityStateWarningKey key) {
		super();
		this.setStateCode(key.getStateCode());
		this.setStateProductWarningCode(key.getStateProductWarningCode());
		this.setSubCommodityCode(key.getSubCommodityCode());
	}

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name = "pd_omi_sub_com_cd")
	private Integer subCommodityCode;

	@Column(name = "st_cd")
	@Type(type="fixedLengthCharPK")
	private String stateCode;

	@Column(name = "st_prod_warn_cd")
	@Type(type="fixedLengthCharPK")
	private String stateProductWarningCode;

	/**
	 * Returns code of the sub commodity this state warning refers to.
	 *
	 * @return The SubCommodityCode  that identifies a subcommodity  is a lower identification of the commodity and brand.
	 **/
	public Integer getSubCommodityCode() {
		return subCommodityCode;
	}

	/**
	 * Sets the SubCommodityCode  that identifies a subcommodity  is a lower identification of the commodity and brand.
	 *
	 * @param subCommodityCode The SubCommodityCode that identifies a subcommodity is a lower identification of the commodity and brand.
	 **/
	public void setSubCommodityCode(Integer subCommodityCode) {
		this.subCommodityCode = subCommodityCode;
	}

	/**
	 * Returns code of the state this state warning refers to.
	 *
	 * @return The StateCode that identifies the state the warning applies to.
	 **/
	public String getStateCode() {
		return stateCode;
	}

	/**
	 * Sets the StateCode that identifies the state the warning applies to.
	 *
	 * @param stateCode The StateCode that identifies the state the warning applies to.
	 **/
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	/**
	 * Returns code of the product state warning this state warning refers to.
	 *
	 * @return The StateProductWarningCode that identifies the warning that is being set.
	 **/
	public String getStateProductWarningCode() {
		return stateProductWarningCode;
	}

	/**
	 * Sets the StateProductWarningCode that identifies the warning that is being set.
	 *
	 * @param stateProductWarningCode The StateProductWarningCode that identifies the warning that is being set.
	 **/
	public void setStateProductWarningCode(String stateProductWarningCode) {
		this.stateProductWarningCode = stateProductWarningCode;
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

		SubCommodityStateWarningKey that = (SubCommodityStateWarningKey) o;

		if (subCommodityCode != null ? !subCommodityCode.equals(that.subCommodityCode) : that.subCommodityCode != null)
			return false;
		if (stateCode != null ? !stateCode.equals(that.stateCode) : that.stateCode != null) return false;
		return stateProductWarningCode != null ? stateProductWarningCode.equals(that.stateProductWarningCode) : that.stateProductWarningCode == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = subCommodityCode != null ? subCommodityCode.hashCode() : 0;
		result = PRIME_NUMBER * result + (stateCode != null ? stateCode.hashCode() : 0);
		result = PRIME_NUMBER * result + (stateProductWarningCode != null ? stateProductWarningCode.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "SubCommodityStateWarningKey{" +
				"subCommodityCode=" + subCommodityCode +
				", stateCode='" + stateCode + '\'' +
				", stateProductWarningCode='" + stateProductWarningCode + '\'' +
				'}';
	}
}
