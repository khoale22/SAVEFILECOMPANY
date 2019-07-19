package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * SupplierItemSubstitutions -> Item Substitution Page.
 *
 * @author m594201
 * @since 2.8.0
 */
@Entity
@Table(name = "splr_item_subs")
public class SupplierItemSubstitutions implements Serializable {

	@EmbeddedId
	private SupplierItemSubstitutionsKey key;

	@Column(name = "sub_to_itm_id")
	private Long subToItemId;

	@Column(name = "sub_rule_seq_no")
	private Long subRuleSequenceNumber;

	@Column(name = "sub_typ_cd")
	private String subTypeCode;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "loc_nbr", referencedColumnName = "whse_loc_nbr", insertable = false, updatable = false),
			@JoinColumn(name = "loc_typ_cd", referencedColumnName = "whse_loc_typ_cd", insertable = false, updatable = false),
			@JoinColumn(name = "sub_to_itm_id", referencedColumnName = "itm_id", insertable = false, updatable = false),
			@JoinColumn(name = "itm_key_typ_cd", referencedColumnName = "itm_key_typ_cd", insertable = false, updatable = false)
	})
	private WarehouseLocationItem supItemsWarehouseLocationItem;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_typ_cd", insertable = false, updatable = false)
	private SubTypeCode supplierSubTypeCode;

	/**
	 * CONSTANT used to query based on the text 'W' which in this case stands for Warehouse.
	 *
	 */
	public static final String WAREHOUSE_LOCATION = "W";

	/**
	 * Gets the composite key.
	 *
	 * @return the key
	 */
	public SupplierItemSubstitutionsKey getKey() {
		return key;
	}

	/**
	 * Sets key the composite key.
	 *
	 * @param key the key
	 */
	public void setKey(SupplierItemSubstitutionsKey key) {
		this.key = key;
	}

	/**
	 * Gets sub to item id that is used to tie record to ItemMaster.
	 *
	 * @return the sub to item id
	 */
	public Long getSubToItemId() {
		return subToItemId;
	}

	/**
	 * Sets sub to item id that is used to tie record to ItemMaster.
	 *
	 * @param subToItemId the sub to item id
	 */
	public void setSubToItemId(Long subToItemId) {
		this.subToItemId = subToItemId;
	}

	/**
	 * Gets sub rule sequence number that is entered by the user.  Sequence in which Item Substitution rules should be followed.
	 *
	 * @return the sub rule sequence number
	 */
	public Long getSubRuleSequenceNumber() {
		return subRuleSequenceNumber;
	}

	/**
	 * Sets sub rule sequence number that is entered by the user. Sequence in which Item Substitution rules should be followed.
	 *
	 * @param subRuleSequenceNumber the sub rule sequence number
	 */
	public void setSubRuleSequenceNumber(Long subRuleSequenceNumber) {
		this.subRuleSequenceNumber = subRuleSequenceNumber;
	}

	/**
	 * Gets sub type code selected in front end by user.  Which describes the kind/type of substitution. Tied to code table SUB_TYPE_CD.
	 *
	 * @return the sub type code
	 */
	public String getSubTypeCode() {
		return subTypeCode;
	}

	/**
	 * Sets sub type code. Which describes the kind/type of substitution.  Tied to code table SUB_TYPE_CD.
	 *
	 * @param subTypeCode the sub type code selected in front end by user.
	 */
	public void setSubTypeCode(String subTypeCode) {
		this.subTypeCode = subTypeCode;
	}

	/**
	 * Gets sup items warehouse location item information for this supplieritemsubstitutuion.
	 *
	 * @return the sup items warehouse location item
	 */
	public WarehouseLocationItem getSupItemsWarehouseLocationItem() {
		return supItemsWarehouseLocationItem;
	}

	/**
	 * Sets sup items warehouse location item information for this supplieritemsubstitutuion.
	 *
	 * @param supItemsWarehouseLocationItem the sup items warehouse location item
	 */
	public void setSupItemsWarehouseLocationItem(WarehouseLocationItem supItemsWarehouseLocationItem) {
		this.supItemsWarehouseLocationItem = supItemsWarehouseLocationItem;
	}

	/**
	 * Gets supplier sub type code.
	 *
	 * @return the supplier sub type code
	 */
	public SubTypeCode getSupplierSubTypeCode() {
		return supplierSubTypeCode;
	}

	/**
	 * Sets supplier sub type code.
	 *
	 * @param supplierSubTypeCode the supplier sub type code
	 */
	public void setSupplierSubTypeCode(SubTypeCode supplierSubTypeCode) {
		this.supplierSubTypeCode = supplierSubTypeCode;
	}

	/**
	 * Compares another object to this one. If that object is an Shipper, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SupplierItemSubstitutions supplierItemSubstitutions = (SupplierItemSubstitutions) o;

		return !(this.key != null ? !this.key.equals(supplierItemSubstitutions.key) : supplierItemSubstitutions.key != null);
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return this.key == null ? 0 : this.key.hashCode();
	}
}
