package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by s573181 on 6/3/2016.
 */
@Entity
@Table(name="pd_shipper")

public class Shipper implements Serializable{

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ShipperKey key;

    @Column(name="pd_shpr_typ_cd")   
    private char shipperTypeCode;

    @Column(name="pd_shpr_qty")
	private Long shipperQuantity;

    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnoreProperties("shipper")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pd_upc_no", referencedColumnName = "pd_upc_no", insertable = false, updatable = false)
	private PrimaryUpc primaryUpc;

    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnoreProperties({"productShipper","shipper","itemMasters"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="pd_shpr_upc_no", referencedColumnName = "pd_upc_no", insertable = false, updatable = false)
	private PrimaryUpc realUpc;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="pd_shpr_upc_no", referencedColumnName = "scn_cd_id", insertable = false, updatable = false)
    private SellingUnit sellingUnit;

    @Transient
	private String actionCode;

	/**
	 * Gets shipper quantity.
	 *
	 * @return the shipper quantity
	 */
	public Long getShipperQuantity() {
		return shipperQuantity;
	}

	/**
	 * Sets shipper quantity.
	 *
	 * @param shipperQuantity the shipper quantity
	 */
	public void setShipperQuantity(Long shipperQuantity) {
		this.shipperQuantity = shipperQuantity;
	}

	/**
	 * Returns the key for this object.
	 *
	 * @return The key for this object.
	 */
	public ShipperKey getKey() {
		return key;
	}

	/**
	 * Sets the key for this object.
	 *
	 * @param key The key for this object.
	 */
	public void setKey(ShipperKey key) {
		this.key = key;
	}

	/**
	 * Sets the type of shipper this is. This field may or may not be correct.
	 *
	 * @return The type of shipper this is.
	 */
	public char getShipperTypeCode() {
		return shipperTypeCode;
	}

	/**
	 * Sets the type of shipper this is.
	 *
	 * @param shipperTypeCode The type of shipper this is.
	 */
	public void setShipperTypeCode(char shipperTypeCode) {
		this.shipperTypeCode = shipperTypeCode;
	}

	/**
	 * Returns the primary UPC tied to this shipper.
	 *
	 * @return The primary UPC tied to this shipper.
	 */
	public PrimaryUpc getPrimaryUpc() {
		return primaryUpc;
	}

	/**
	 * Gets real upc.
	 *
	 * @return the real upc
	 */
	public PrimaryUpc getRealUpc() {
		return realUpc;
	}

	/**
	 * Sets real upc.
	 *
	 * @param realUpc the real upc
	 */
	public void setRealUpc(PrimaryUpc realUpc) {
		this.realUpc = realUpc;
	}

	/**
	 * Sets primary upc.
	 *
	 * @param primaryUpc the primary upc
	 */
	public void setPrimaryUpc(PrimaryUpc primaryUpc) {
		this.primaryUpc = primaryUpc;
	}

	/**
	 * @return Gets the value of sellingUnit and returns sellingUnit
	 */
	public void setSellingUnit(SellingUnit sellingUnit) {
		this.sellingUnit = sellingUnit;
	}

	/**
	 * Sets the sellingUnit
	 */
	public SellingUnit getSellingUnit() {
		return sellingUnit;
	}

	/**
	 * @return Gets the value of actionCode and returns actionCode
	 */
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	/**
	 * Sets the actionCode
	 */
	public String getActionCode() {
		return actionCode;
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

        Shipper shipper = (Shipper) o;

		return !(this.key != null ? !this.key.equals(shipper.key) : shipper.key != null);

	}

    /**
     * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
     * they have different hash codes.
     *
     * @return The hash code for this obejct.
     */
    @Override
    public int hashCode() {
		return this.key == null ? 0 : this.key.hashCode();
    }

    /**
     * Returns a printable representation of the object.
     *
     * @return A printable representation of the object.
     */
    @Override
    public String toString(){
        return "Shipper{" +
                "key" + key +
                ", shipperTypeCode'" + shipperTypeCode + '\'' +
                '}';
    }

}
