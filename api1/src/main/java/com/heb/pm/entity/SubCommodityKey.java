package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * An embeddable key for sub-commodities.
 *
 * @author d116773
 * @since 2.0.2
 */
@Embeddable
public class SubCommodityKey implements Serializable {

	// default constructor
	public SubCommodityKey(){super();}

	// copy constructor
	public SubCommodityKey(SubCommodityKey key){
		super();
		this.setClassCode(key.getClassCode());
		this.setCommodityCode(key.getCommodityCode());
		this.setSubCommodityCode(key.getSubCommodityCode());
	}

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name="pd_omi_com_cls_cd")
	private Integer classCode;

	@Column(name="pd_omi_com_cd")
	private Integer commodityCode;

	@Column(name = "pd_omi_sub_com_cd")
	private Integer subCommodityCode;

	/**
	 * Returns the class ID.
	 *
	 * @return The class ID.
	 */
	public Integer getClassCode() {
		return classCode;
	}

	/**
	 * Sets the class ID.
	 *
	 * @param classCode The class ID.
	 */
	public void setClassCode(Integer classCode) {
		this.classCode = classCode;
	}

	/**
	 * Returns the commodity ID.
	 *
	 * @return The commodity ID.
	 */
	public Integer getCommodityCode() {
		return commodityCode;
	}

	/**
	 * Sets the commodity ID.
	 *
	 * @param commodityCode THe commodity ID.
	 */
	public void setCommodityCode(Integer commodityCode) {
		this.commodityCode = commodityCode;
	}

	/**
	 * Returns the sub-commodity ID.
	 *
	 * @return The sub-commodity ID.
	 */
	public Integer getSubCommodityCode() {
		return subCommodityCode;
	}

	/**
	 * Sets the sub-commodity ID.
	 *
	 * @param subCommodityCode The sub-commodity ID.
	 */
	public void setSubCommodityCode(Integer subCommodityCode) {
		this.subCommodityCode = subCommodityCode;
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

		SubCommodityKey that = (SubCommodityKey) o;

		if (classCode != null ? !classCode.equals(that.classCode) : that.classCode != null) return false;
		if (commodityCode != null ? !commodityCode.equals(that.commodityCode) : that.commodityCode != null)
			return false;
		return subCommodityCode != null ? subCommodityCode.equals(that.subCommodityCode) : that.subCommodityCode == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = classCode != null ? classCode.hashCode() : 0;
		result = SubCommodityKey.PRIME_NUMBER * result + (commodityCode != null ? commodityCode.hashCode() : 0);
		result = SubCommodityKey.PRIME_NUMBER * result + (subCommodityCode != null ? subCommodityCode.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "SubCommodityKey{" +
				"classCode=" + classCode +
				", commodityCode=" + commodityCode +
				", subCommodityCode=" + subCommodityCode +
				'}';
	}
}
