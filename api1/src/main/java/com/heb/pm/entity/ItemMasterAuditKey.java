package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Compound Key used to uniquely identify item master change records.
 * @author s753601
 * @version 2.8.0
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class ItemMasterAuditKey implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;

	@Column(name = "AUD_REC_CRE8_TS")
	private LocalDateTime changedOn;

	@Column(name = "ITM_ID")
	private Long itemCode;

	@Column(name = "ITM_KEY_TYP_CD")
	@Type(type = "fixedLengthCharPK")
	private String itemType;

	/**
	 * Get the timestamp for when the record was created
	 * @return the timestamp for when the record was created
	 */
	public LocalDateTime getChangedOn() {
		return changedOn;
	}

	/**
	 * Updates the timestamp for when the record was created
	 * @param changedOn the new timestamp for when the record was created
	 */
	public void setChangedOn(LocalDateTime changedOn) {
		this.changedOn = changedOn;
	}

	/**
	 * The numeric component to uniquely identify the Item Master that was changed
	 * @return The numeric component to uniquely identify the Item Master that was changed
	 */
	public Long getItemCode() {
		return itemCode;
	}

	/**
	 * Updates the numeric component to uniquely identify the Item Master that was changed
	 * @param itemCode The new numeric component to uniquely identify the Item Master that was changed
	 */
	public void setItemCode(Long itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * The classification component to uniquely identify the Item Master that was changed
	 * @return The classification component to uniquely identify the Item Master that was changed
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * Updates the classification component to uniquely identify the Item Master that was changed
	 * @param itemType The new classification component to uniquely identify the Item Master that was changed
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * Compares another object to this one. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ItemMasterAuditKey)) return false;

		ItemMasterAuditKey that = (ItemMasterAuditKey) o;

		if(itemType==null && that.itemType!=null){return false;}
		if(itemType!=null && (!itemType.equals(that.itemType))){ return false;}
		if(changedOn == null && that.changedOn !=null ){return false;}
		if(changedOn != null && (!changedOn.equals(that.changedOn))){return false;}
		return itemCode==that.itemCode;

	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = ItemMasterAuditKey.PRIME_NUMBER + (itemType != null ? itemType.hashCode() : 0);
		result = ItemMasterAuditKey.PRIME_NUMBER * result + (int) (itemCode ^ (itemCode >>> ItemMasterAuditKey.FOUR_BYTES));
		result = ItemMasterAuditKey.PRIME_NUMBER * result + (changedOn != null ? changedOn.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "ItemMasterAuditKey{" +
				"itemType='" + itemType + '\'' +
				", itemCode=" + itemCode + '\'' +
				", changedOn=" + changedOn +
				'}';
	}

}
