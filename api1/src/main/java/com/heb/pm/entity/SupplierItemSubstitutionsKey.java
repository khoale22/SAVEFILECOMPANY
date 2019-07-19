package com.heb.pm.entity;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key of the splr_item_subs table.
 *
 * @author m594201
 * @since 2.8.0
 */
@Embeddable
public class SupplierItemSubstitutionsKey implements Serializable{

	@Type(type="fixedLengthCharPK")
	@Column(name = "itm_key_typ_cd")
	private String itemKeyTypeCode;

	@Column(name = "itm_id")
	private Long itemId;

	@Type(type="fixedLengthCharPK")
	@Column(name = "loc_typ_cd")
	private String locationTypeCode;

	@Column(name = "loc_nbr")
	private Long locationNumber;

	@Column(name = "sub_rule_no")
	private Long subRuleNumber;

	/**
	 * Gets item key type code text that defines this as an item type code record.
	 *
	 * @return the item key type code
	 */
	public String getItemKeyTypeCode() {
		return itemKeyTypeCode;
	}

	/**
	 * Sets item key type code text that defines this as an item type code record.
	 *
	 * @param itemKeyTypeCode the item key type code
	 */
	public void setItemKeyTypeCode(String itemKeyTypeCode) {
		this.itemKeyTypeCode = itemKeyTypeCode;
	}

	/**
	 * Gets item id that defines this as the identifier for the item container.
	 *
	 * @return the item id
	 */
	public Long getItemId() {
		return itemId;
	}

	/**
	 * Sets item id.
	 *
	 * @param itemId the item id that defines the code which this belongs to.
	 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	/**
	 * Gets location type code, that defines the location source of the supplier item sub.
	 *
	 * @return the location type code
	 */
	public String getLocationTypeCode() {
		return locationTypeCode;
	}

	/**
	 * Sets location type code, that defines the location source of the supplier item sub.
	 *
	 * @param locationTypeCode the location type code
	 */
	public void setLocationTypeCode(String locationTypeCode) {
		this.locationTypeCode = locationTypeCode;
	}

	/**
	 * Gets location number of the tied warehouse.
	 *
	 * @return the location number
	 */
	public Long getLocationNumber() {
		return locationNumber;
	}

	/**
	 * Sets location number of the tied warehouse.
	 *
	 * @param locationNumber the location number
	 */
	public void setLocationNumber(Long locationNumber) {
		this.locationNumber = locationNumber;
	}

	/**
	 * Gets sub rule number.
	 *
	 * @return the sub rule number
	 */
	public Long getSubRuleNumber() {
		return subRuleNumber;
	}

	/**
	 * Sets sub rule number.
	 *
	 * @param subRuleNumber the sub rule number
	 */
	public void setSubRuleNumber(Long subRuleNumber) {
		this.subRuleNumber = subRuleNumber;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SupplierItemSubstitutionsKey that = (SupplierItemSubstitutionsKey) o;

		if (itemKeyTypeCode != null ? !itemKeyTypeCode.equals(that.itemKeyTypeCode) : that.itemKeyTypeCode != null)
			return false;
		if (itemId != null ? !itemId.equals(that.itemId) : that.itemId != null) return false;
		if (locationTypeCode != null ? !locationTypeCode.equals(that.locationTypeCode) : that.locationTypeCode != null)
			return false;
		if (locationNumber != null ? !locationNumber.equals(that.locationNumber) : that.locationNumber != null)
			return false;
		return subRuleNumber != null ? subRuleNumber.equals(that.subRuleNumber) : that.subRuleNumber == null;
	}

	@Override
	public int hashCode() {
		int result = itemKeyTypeCode != null ? itemKeyTypeCode.hashCode() : 0;
		result = 31 * result + (itemId != null ? itemId.hashCode() : 0);
		result = 31 * result + (locationTypeCode != null ? locationTypeCode.hashCode() : 0);
		result = 31 * result + (locationNumber != null ? locationNumber.hashCode() : 0);
		result = 31 * result + (subRuleNumber != null ? subRuleNumber.hashCode() : 0);
		return result;
	}
}
