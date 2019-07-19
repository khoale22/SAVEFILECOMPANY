/*
 *  BicepVendor
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

/**
 * This represents a bicep vendor. A bicep vendor contain information vendor location number, bicep, ap location
 * number, warehouse number, facility.
 *
 * @author vn70516
 * @since 2.8.0
 */
public class BicepVendor implements java.io.Serializable {

    private Integer vendorLocationNumber;
    private String warehouseNumber;
    private Integer facility;

    private Integer apVendor;
    private Integer bicepVendorNumber ;

    private Long productId;
    private Long itemId;
    private String itemType;


    /**
     * Sets the vendorLocationNumber
     */
    public Integer getVendorLocationNumber() {
        return vendorLocationNumber;
    }

    /**
     * @return Gets the value of vendorLocationNumber and returns vendorLocationNumber
     */
    public void setVendorLocationNumber(Integer vendorLocationNumber) {
        this.vendorLocationNumber = vendorLocationNumber;
    }

    /**
     * Sets the warehouseNumber
     */
    public String getWarehouseNumber() {
        return warehouseNumber;
    }

    /**
     * @return Gets the value of warehouseNumber and returns warehouseNumber
     */
    public void setWarehouseNumber(String warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    /**
     * Sets the facility
     */
    public Integer getFacility() {
        return facility;
    }

    /**
     * @return Gets the value of facility and returns facility
     */
    public void setFacility(Integer facility) {
        this.facility = facility;
    }

    /**
     * Sets the apVendor
     */
    public Integer getApVendor() {
        return apVendor;
    }

    /**
     * @return Gets the value of apVendor and returns apVendor
     */
    public void setApVendor(Integer apVendor) {
        this.apVendor = apVendor;
    }

    /**
     * Sets the bicepVendorNumber
     */
    public Integer getBicepVendorNumber() {
        return bicepVendorNumber;
    }

    /**
     * @return Gets the value of bicepVendorNumber and returns bicepVendorNumber
     */
    public void setBicepVendorNumber(Integer bicepVendorNumber) {
        this.bicepVendorNumber = bicepVendorNumber;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BicepVendor)) return false;

        BicepVendor that = (BicepVendor) o;

        if (getVendorLocationNumber() != null ? !getVendorLocationNumber().equals(that.getVendorLocationNumber()) : that.getVendorLocationNumber() != null)
            return false;
        if (getWarehouseNumber() != null ? !getWarehouseNumber().equals(that.getWarehouseNumber()) : that.getWarehouseNumber() != null)
            return false;
        if (getFacility() != null ? !getFacility().equals(that.getFacility()) : that.getFacility() != null)
            return false;
        if (getApVendor() != null ? !getApVendor().equals(that.getApVendor()) : that.getApVendor() != null)
            return false;
        return getBicepVendorNumber() != null ? getBicepVendorNumber().equals(that.getBicepVendorNumber()) : that.getBicepVendorNumber() == null;
    }

    @Override
    public int hashCode() {
        int result = getVendorLocationNumber() != null ? getVendorLocationNumber().hashCode() : 0;
        result = 31 * result + (getWarehouseNumber() != null ? getWarehouseNumber().hashCode() : 0);
        result = 31 * result + (getFacility() != null ? getFacility().hashCode() : 0);
        result = 31 * result + (getApVendor() != null ? getApVendor().hashCode() : 0);
        result = 31 * result + (getBicepVendorNumber() != null ? getBicepVendorNumber().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BicepVendor{" +
                "vendorLocationNumber=" + vendorLocationNumber +
                ", warehouseNumber='" + warehouseNumber + '\'' +
                ", facility=" + facility +
                ", apVendor=" + apVendor +
                ", bicepVendorNumber=" + bicepVendorNumber +
                '}';
    }
}
