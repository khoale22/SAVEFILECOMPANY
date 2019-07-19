/*
 * com.heb.pm.entity.ItemNotDeletedKey
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;

/**
 * The ITM_NT_DELD table has a composite key. This class represents that key.
 *
 * @author vn40486
 * @since 2.0.4
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ItemNotDeletedKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int FOUR_BYTES = 32;
    private static final int PRIME_NUMBER = 31;

    @Column(name="itm_id")
    private long itemCode;

    @Column(name="itm_prod_key_cd")
  //db2o changes  vn00907
    @Type(type="fixedLengthCharPK") 
    private String itemType;

    @Column(name="itm_nt_deld_rsn_cd")
  //db2o changes  vn00907
    @Type(type="fixedLengthCharPK") 
    private String notDeletedReasonCode;

    /**
     * Returns the item code for the ProductDiscontinue object.
     *
     * @return The item code for the ProductDiscontinue object.
     */
    public long getItemCode() {
        return itemCode;
    }

    /**
     * Sets the item code for the ProductDiscontinue object.
     *
     * @param itemCode The item code for the ProductDiscontinue object.
     */
    public void setItemCode(long itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * Returns the item type for the ProductDiscontinue object.
     *
     * @return The item type for the ProductDiscontinue object.
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * Returns the item type for the ProductDiscontinue object.
     *
     * @param itemType The item type for the ProductDiscontinue object.
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * Returns the item not deleted reason code.
     * @return item not deleted reason code.
     */
    public String getNotDeletedReasonCode() {
        return notDeletedReasonCode;
    }

    /**
     * Sets the item not delted reason code.
     *
     * @param notDeletedReasonCode item not deleted reason code.
     */
    public void setNotDeletedReasonCode(String notDeletedReasonCode) {
        this.notDeletedReasonCode = notDeletedReasonCode;
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
        if (o == null || getClass() != o.getClass()) return false;

        ItemNotDeletedKey that = (ItemNotDeletedKey) o;

        if (itemCode != that.itemCode) return false;
        if (!itemType.equals(that.itemType)) return false;
        return notDeletedReasonCode.equals(that.notDeletedReasonCode);

    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = (int) (itemCode ^ (itemCode >>> ItemNotDeletedKey.FOUR_BYTES));
        result = ItemNotDeletedKey.PRIME_NUMBER * result + itemType.hashCode();
        result = ItemNotDeletedKey.PRIME_NUMBER * result + notDeletedReasonCode.hashCode();
        return result;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "ItemNotDeletedKey{" +
                "itemCode=" + itemCode +
                ", itemType='" + itemType + '\'' +
                ", notDeletedReasonCode='" + notDeletedReasonCode + '\'' +
                '}';
    }
}
