package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;

/**
 * Created by s573181 on 6/1/2016.
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ProdItemKey implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DSD = "DSD";
    public static final String WAREHOUSE = "ITMCD";

    private static final int FOUR_BYTES = 32;
    private static final int PRIME_NUMBER = 31;

    @Column(name="itm_key_typ_cd")
    //db2o changes  vn00907
    @Type(type="fixedLengthCharPK")  
    private String itemType;

    @Column(name="itm_id")
    private long itemCode;

    @Column(name="prod_id")
    private long productId;

	/**
	 * Gets item type.
	 *
	 * @return the item type
	 */
	public String getItemType() {
        return itemType;
    }

	/**
	 * Sets item type.
	 *
	 * @param itemType the item type
	 */
	public void setItemType(String itemType) {
        this.itemType = itemType;
    }

	/**
	 * Gets item code.
	 *
	 * @return the item code
	 */
	public long getItemCode() {
        return itemCode;
    }

	/**
	 * Sets item code.
	 *
	 * @param itemCode the item code
	 */
	public void setItemCode(long itemCode) {
        this.itemCode = itemCode;
    }

	/**
	 * Gets product id.
	 *
	 * @return the product id
	 */
	public long getProductId() {
        return productId;
    }

	/**
	 * Sets product id.
	 *
	 * @param productId the product id
	 */
	public void setProductId(long productId) {
        this.productId = productId;
    }

    /**
     * Returns whether or not this item is DSD.
     *
     * @return True if the item is DSD and false otherwise.
     */
    public boolean isDsd(){
        return ProdItemKey.DSD.equals(this.getItemType());
    }

    /**
     * Returns whether or not this item is warehouse.
     *
     * @return True if the item is warehouse and false otherwise.
     */
    public boolean isWarehouse() {
        return ProdItemKey.WAREHOUSE.equals(this.getItemType());
    }

	/**
	 * Compares another object to this one. If that object is an PrimaryUpc, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProdItemKey)) return false;

        ProdItemKey itemKey = (ProdItemKey) o;

        if (itemCode != itemKey.itemCode) return false;
        if (productId != itemKey.productId) return false;
        return !(itemType != null ? !itemType.equals(itemKey.itemType) : itemKey.itemType != null);

    }

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
    @Override
    public int hashCode() {
        int result = itemType != null ? itemType.hashCode() : 0;
        result = PRIME_NUMBER * result + (int) (itemCode ^ (itemCode >>> FOUR_BYTES));
        result = PRIME_NUMBER * result + (int) (productId ^ (productId >>> FOUR_BYTES));
        return result;
    }

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
    @Override
    public String toString() {
        return "ProdItemKey{" +
                "itemType='" + itemType + '\'' +
                ", itemCode=" + itemCode +
                ", productId=" + productId +
                '}';
    }

}
