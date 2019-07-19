package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents the global temporary table. This is used to do searches for products. Values can be inserted here and
 * then this table can be joined to core tables to provide filtering.
 *
 * @author d116773
 * @since 2.13.0
 */
@Entity
@Table(name="temp_gbl_prod_mgmt")
public class SearchCriteria {

	@Id
	@Column(name="uu_id")
	private String uniqueId;

	@Column(name="sson_id")
	private String sessionId;

	@Column(name="itm_id")
	private Long itemCode;

	@Column(name="itm_key_typ_cd")
	private String itemType;

	@Column(name="prod_id")
	private Long productId;

	@Column(name="scn_cd_id")
	private Long upc;

	@Column(name="str_dept_nbr")
	private String departmentCode;

	@Column(name="str_sub_dept_id")
	private String subDepartmentCode;

	@Column(name="pd_omi_com_cls_cd")
	private Integer classCode;

	@Column(name="pd_omi_com_cd")
	private Integer commodityCode;

	@Column(name="pd_omi_sub_com_cd")
	private Integer subCommodityCode;

	@Column(name="bdm_cd")
	private String bdmCode;

	@Column(name="vend_loc_typ_cd")
	private String vendorLocationTypeCode;

	@Column(name="vend_loc_nbr")
	private Integer vendorLocationNumber;

	/**
	 * Returns the unique ID for this record. This is not particularly useful as there is no key on the table, but
	 * this is required by JPA.
	 *
	 * @return The unique ID for this record.
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * Sets the unique ID for this record.
	 *
	 * @param uniqueId The unique ID for this record.
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * Returns an item code the user is trying to search for.
	 *
	 * @return An item code the user is trying to search for.
	 */
	public Long getItemCode() {
		return itemCode;
	}

	/**
	 * Sets an item code the user is trying to search for.
	 *
	 * @param itemCode An item code the user is trying to search for.
	 */
	public void setItemCode(Long itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * Returns the type of the value returned by getItemCode (DSD or Warehouse).
	 *
	 * @return The type of the value returned by getItemCode.
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * Sets the type of the value returned by getItemCode.
	 *
	 * @param itemType The type of the value returned by getItemCode.
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * Returns a product ID the user is searching for.
	 *
	 * @return A product ID the user is searching for.
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * Sets a product ID the user is searching for.
	 *
	 * @param productId A product ID the user is searching for.
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * Returns a UPC the user is searching for.
	 *
	 * @return A UPC the user is searching for.
	 */
	public Long getUpc() {
		return upc;
	}

	/**
	 * Sets a UPC the user is searching for.
	 *
	 * @param upc A UPC the user is searching for.
	 */
	public void setUpc(Long upc) {
		this.upc = upc;
	}

	/**
	 * Returns the session ID for this data. Since we are pooling database connections, multiple user sessions
	 * may store data in this table in the same database session. This ID should be unique across user sessions
	 * and a constraint here should be included in any query against this table.
	 *
	 * @return The session ID for this data.
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Sets the session ID for this data.
	 *
	 * @param sessionId The session ID for this data.
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * Returns an item class the user wants to constrain against.
	 *
	 * @return An item class the user wants to constrain against.
	 */
	public Integer getClassCode() {
		return classCode;
	}

	/**
	 * Sets an item class the user wants to constrain against.
	 *
	 * @param classCode An item class the user wants to constrain against.
	 */
	public void setClassCode(Integer classCode) {
		this.classCode = classCode;
	}

	/**
	 * Returns a department the user wants to constrain against.
	 *
	 * @return A department the user wants to constrain against.
	 */
	public String getDepartmentCode() {
		return departmentCode;
	}

	/**
	 * Sets a department the user wants to constrain against.
	 *
	 * @param departmentCode A department the user wants to constrain against.
	 */
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	/**
	 * Returns a sub-department the user wants to constrain against.
	 *
	 * @return A sub-department the user wants to constrain against.
	 */
	public String getSubDepartmentCode() {
		return subDepartmentCode;
	}

	/**
	 * Sets a sub-department the user wants to constrain against.
	 *
	 * @param subDepartmentCode A sub-department the user wants to constrain against.
	 */
	public void setSubDepartmentCode(String subDepartmentCode) {
		this.subDepartmentCode = subDepartmentCode;
	}

	/**
	 * Sets a commodity the user wants to constrain against.
	 *
	 * @return A commodity the user wants to constrain against.
	 */
	public Integer getCommodityCode() {
		return commodityCode;
	}

	/**
	 * Sets a commodity the user wants to constrain against.
	 *
	 * @param commodityCode A commodity the user wants to constrain against.
	 */
	public void setCommodityCode(Integer commodityCode) {
		this.commodityCode = commodityCode;
	}

	/**
	 * Returns a sub-commodity the user wants to constrain against.
	 *
	 * @return A sub-commodity the user wants to constrain against.
	 */
	public Integer getSubCommodityCode() {
		return subCommodityCode;
	}

	/**
	 * Sets a sub-commodity the user wants to constrain against.
	 *
	 * @param subCommodityCode A sub-commodity the user wants to constrain against.
	 */
	public void setSubCommodityCode(Integer subCommodityCode) {
		this.subCommodityCode = subCommodityCode;
	}

	/**
	 * Returns a BDM the user wants to constrain against.
	 *
	 * @return A BDM the user wants to constrain against.
	 */
	public String getBdmCode() {
		return bdmCode;
	}

	/**
	 * Sets a BDM the user wants to constrain against.
	 *
	 * @param bdmCode A BDM the user wants to constrain against.
	 */
	public void setBdmCode(String bdmCode) {
		this.bdmCode = bdmCode;
	}

	/**
	 * Returns a vendor location type the user wants to constrain against.
	 *
	 * @return A vendor location type the user wants to constrain against.
	 */
	public String getVendorLocationTypeCode() {
		return vendorLocationTypeCode;
	}

	/**
	 * Sets a vendor location type the user wants to constrain against.
	 *
	 * @param vendorLocationTypeCode A vendor location type the user wants to constrain against.
	 */
	public void setVendorLocationTypeCode(String vendorLocationTypeCode) {
		this.vendorLocationTypeCode = vendorLocationTypeCode;
	}

	/**
	 * Returns a vendor number the user wants to constrain against.
	 *
	 * @return A vendor number the user wants to constrain against.
	 */
	public Integer getVendorLocationNumber() {
		return vendorLocationNumber;
	}

	/**
	 * Sets a vendor number the user wants to constrain against.
	 *
	 * @param vendorLocationNumber A vendor number the user wants to constrain against.
	 */
	public void setVendorLocationNumber(Integer vendorLocationNumber) {
		this.vendorLocationNumber = vendorLocationNumber;
	}

	/**
	 * Returns a string representation of this class.
	 *
	 * @return A string representation of this class.
	 */
	@Override
	public String toString() {
		return "SearchCriteria{" +
				"uniqueId='" + uniqueId + '\'' +
				", sessionId='" + sessionId + '\'' +
				", itemCode=" + itemCode +
				", itemType='" + itemType + '\'' +
				", productId=" + productId +
				", upc=" + upc +
				", departmentCode='" + departmentCode + '\'' +
				", subDepartmentCode='" + subDepartmentCode + '\'' +
				", classCode=" + classCode +
				", commodityCode=" + commodityCode +
				", subCommodityCode=" + subCommodityCode +
				", bdmCode='" + bdmCode + '\'' +
				", vendorLocationTypeCode='" + vendorLocationTypeCode + '\'' +
				", vendorLocationNumber=" + vendorLocationNumber +
				'}';
	}

	/**
	 * Compares this object to another for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SearchCriteria)) return false;

		SearchCriteria that = (SearchCriteria) o;

		return !(uniqueId != null ? !uniqueId.equals(that.uniqueId) : that.uniqueId != null);

	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		return uniqueId != null ? uniqueId.hashCode() : 0;
	}
}
