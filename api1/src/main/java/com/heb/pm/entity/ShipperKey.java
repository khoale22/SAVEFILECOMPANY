/*
 *
 *  ShipperKey
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The type Shipper key.
 *
 * @author s573181
 * @since  6/2/2016
 */
@Embeddable
public class ShipperKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int FOUR_BYTES = 32;
    private static final int PRIME_NUMBER = 31;

    @Column(name="pd_upc_no")
    private long upc;

    @Column(name="pd_shpr_upc_no")
    private long shipperUpc;

    /**
     * Returns the upc for the ProductDiscontinue object.
     *
     * @return The upc for the ProductDiscontinue object.
     */
    public long getUpc() {
        return upc;
    }

    /**
     * Sets the upc for the ProductDiscontinue object.
     *
     * @param upc The upc for the ProductDiscontinue object.
     */
    public void setUpc(long upc) {
        this.upc = upc;
    }

    /**
     * Returns the shipperUpc for the ProductDiscontinue object.
     *
     * @return The shipperUpc for the ProductDiscontinue object.
     */
    public long getShipperUpc() {
        return shipperUpc;
    }

    /**
     * Sets the shipperUpc for the ProductDiscontinue object.
     *
     * @param shipperUpc The shipperUpc code for the ProductDiscontinue object.
     */
    public void setShipperUpc(long shipperUpc) {
        this.shipperUpc = shipperUpc;
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

        ShipperKey that = (ShipperKey) o;

        if (upc != that.upc) return false;
        return shipperUpc == that.shipperUpc;

    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = (int) (upc ^ (upc >>> FOUR_BYTES));
        result = PRIME_NUMBER * result + (int) (shipperUpc ^ (shipperUpc >>> FOUR_BYTES));
        return result;
    }


    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString(){
        return "PdShipperKey{" +
                "upc='" + upc + '\'' +
                ", shipperUpc=" + shipperUpc +
                '}';
    }
}
