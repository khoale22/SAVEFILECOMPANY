package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key for sals_rstr_grp_dflt.
 *
 * @author m314029
 * @since 2.6.0
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
//dB2Oracle changes vn00907
public class SellingRestrictionHierarchyLevelKey implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int PRIME_NUMBER = 31;

	// default constructor
	public SellingRestrictionHierarchyLevelKey(){super();}

	// copy constructor
	public SellingRestrictionHierarchyLevelKey(SellingRestrictionHierarchyLevelKey key){
		super();
		this.setDepartment(key.getDepartment());
		this.setSubDepartment(key.getSubDepartment());
		this.setItemClass(key.getItemClass());
		this.setCommodity(key.getCommodity());
		this.setSubCommodity(key.getSubCommodity());
		this.setRestrictionGroupCode(key.getRestrictionGroupCode());
	}

	@Column(name = "str_dept_nbr")
	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK")
	private String department;

	@Column(name = "str_sub_dept_id")
	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK")
	private String subDepartment;

	@Column(name = "item_cls_code")
	private Integer itemClass;

	@Column(name = "pd_omi_com_cd")
	private Integer commodity;

	@Column(name = "pd_omi_sub_com_cd")
	private Integer subCommodity;

	@Column(name = "sals_rstr_grp_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK")
	private String restrictionGroupCode;

	/**
	 * Returns Department.
	 *
	 * @return The Department.
	 **/
	public String getDepartment() {
		return department;
	}

	/**
	 * Sets the Department.
	 *
	 * @param department The Department.
	 **/
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * Returns SubDepartment.
	 *
	 * @return The SubDepartment.
	 **/
	public String getSubDepartment() {
		return subDepartment;
	}

	/**
	 * Sets the SubDepartment.
	 *
	 * @param subDepartment The SubDepartment.
	 **/
	public void setSubDepartment(String subDepartment) {
		this.subDepartment = subDepartment;
	}

	/**
	 * Returns ItemClass.
	 *
	 * @return The ItemClass.
	 **/
	public Integer getItemClass() {
		return itemClass;
	}

	/**
	 * Sets the ItemClass.
	 *
	 * @param itemClass The ItemClass.
	 **/
	public void setItemClass(Integer itemClass) {
		this.itemClass = itemClass;
	}

	/**
	 * Returns Commodity.
	 *
	 * @return The Commodity.
	 **/
	public Integer getCommodity() {
		return commodity;
	}

	/**
	 * Sets the Commodity.
	 *
	 * @param commodity The Commodity.
	 **/
	public void setCommodity(Integer commodity) {
		this.commodity = commodity;
	}

	/**
	 * Returns SubCommodity.
	 *
	 * @return The SubCommodity.
	 **/
	public Integer getSubCommodity() {
		return subCommodity;
	}

	/**
	 * Sets the SubCommodity.
	 *
	 * @param subCommodity The SubCommodity.
	 **/
	public void setSubCommodity(Integer subCommodity) {
		this.subCommodity = subCommodity;
	}

	/**
	 * Returns RestrictionGroupCode.
	 *
	 * @return The RestrictionGroupCode.
	 **/
	public String getRestrictionGroupCode() {
		return restrictionGroupCode;
	}

	/**
	 * Sets the RestrictionGroupCode.
	 *
	 * @param restrictionGroupCode The RestrictionGroupCode.
	 **/
	public void setRestrictionGroupCode(String restrictionGroupCode) {
		this.restrictionGroupCode = restrictionGroupCode;
	}

	/**
	 * Compares another object to this one. If that object is an SellingRestrictionHierarchyLevelKey, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SellingRestrictionHierarchyLevelKey that = (SellingRestrictionHierarchyLevelKey) o;

		if (department != null ? !department.equals(that.department) : that.department != null) return false;
		if (subDepartment != null ? !subDepartment.equals(that.subDepartment) : that.subDepartment != null)
			return false;
		if (itemClass != null ? !itemClass.equals(that.itemClass) : that.itemClass != null) return false;
		if (commodity != null ? !commodity.equals(that.commodity) : that.commodity != null) return false;
		if (subCommodity != null ? !subCommodity.equals(that.subCommodity) : that.subCommodity != null) return false;
		return restrictionGroupCode != null ? restrictionGroupCode.equals(that.restrictionGroupCode) : that.restrictionGroupCode == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = department != null ? department.hashCode() : 0;
		result = SellingRestrictionHierarchyLevelKey.PRIME_NUMBER * result + (subDepartment != null ? subDepartment.hashCode() : 0);
		result = SellingRestrictionHierarchyLevelKey.PRIME_NUMBER * result + (itemClass != null ? itemClass.hashCode() : 0);
		result = SellingRestrictionHierarchyLevelKey.PRIME_NUMBER * result + (commodity != null ? commodity.hashCode() : 0);
		result = SellingRestrictionHierarchyLevelKey.PRIME_NUMBER * result + (subCommodity != null ? subCommodity.hashCode() : 0);
		result = SellingRestrictionHierarchyLevelKey.PRIME_NUMBER * result + (restrictionGroupCode != null ? restrictionGroupCode.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "SellingRestrictionHierarchyLevelKey{" +
				"department='" + department + '\'' +
				", subDepartment='" + subDepartment + '\'' +
				", itemClass=" + itemClass +
				", commodity=" + commodity +
				", subCommodity=" + subCommodity +
				", restrictionGroupCode='" + restrictionGroupCode + '\'' +
				'}';
	}
}
