/*
 * ProductBrandTier
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents the product brand tier.
 *
 * @author vn00602
 * @since 2.12.0
 */
@Entity
@Table(name = "prod_brnd_tier")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class)
})
public class ProductBrandTier implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DISPLAY_NAME_FORMAT = "%s [%d]";
    private static final String DEFAULT_TIER_SORT_FIELD = "productBrandTierCode";
    public static final String DEFAULT_OWN_BRND_SW = "0";
    public static final long DEFAULT_PRODUCT_BRAND_TIER_ID = 0L;

    @Id
    @Column(name = "PROD_BRND_TIER_ID")
    private Integer productBrandTierCode;

    @Column(name = "PROD_BRND_ABB")
    @Type(type = "fixedLengthChar")
    private String productBrandAbbreviation;

    @Column(name = "PROD_BRND_NM")
    @Type(type = "fixedLengthChar")
    private String productBrandName;

    @Column(name = "OWN_BRND_SW", length = 1)
    private String ownerBrand;

    @Formula("TRIM(BOTH ' ' FROM PROD_BRND_NM)")
    private String trimmedProductBrandName;

    /**
     * Returns the productBrandTierCode.
     *
     * @return the productBrandTierCode.
     */
    public Integer getProductBrandTierCode() {
        return productBrandTierCode;
    }

    /**
     * Sets the productBrandTierCode.
     *
     * @param productBrandTierCode the productBrandTierCode to set.
     */
    public void setProductBrandTierCode(Integer productBrandTierCode) {
        this.productBrandTierCode = productBrandTierCode;
    }

    /**
     * Returns the productBrandAbbreviation.
     *
     * @return the productBrandAbbreviation.
     */
    public String getProductBrandAbbreviation() {
        return productBrandAbbreviation;
    }

    /**
     * Sets the productBrandAbbreviation.
     *
     * @param productBrandAbbreviation the productBrandAbbreviation to set.
     */
    public void setProductBrandAbbreviation(String productBrandAbbreviation) {
        this.productBrandAbbreviation = productBrandAbbreviation;
    }

    /**
     * Returns the productBrandName.
     *
     * @return the productBrandName.
     */
    public String getProductBrandName() {
        return productBrandName;
    }

    /**
     * Sets the productBrandName.
     *
     * @param productBrandName the productBrandName to set.
     */
    public void setProductBrandName(String productBrandName) {
        this.productBrandName = productBrandName;
    }

    /**
     * Returns the ownerBrand.
     *
     * @return the ownerBrand.
     */
    public String getOwnerBrand() {
        return ownerBrand;
    }

    /**
     * Sets the ownerBrand.
     *
     * @param ownerBrand the ownerBrand to set.
     */
    public void setOwnerBrand(String ownerBrand) {
        this.ownerBrand = ownerBrand;
    }

    /**
     * Get the trimmedProductBrandName of the TierCode
     *
     * @return value of trimmedProductBrandName
     */
    public String getTrimmedProductBrandName() {
        return trimmedProductBrandName;
    }

    /**
     * Set the trimmedProductBrandName of the TierCode
     */
    public void setTrimmedProductBrandName(String trimmedProductBrandName) {
        this.trimmedProductBrandName = trimmedProductBrandName;
    }

    /**
     * Returns the display name for the brand tier.
     *
     * @return the display name in the format like "UNASSIGNED [0]".
     */
    public String getDisplayName() {
        return String.format(ProductBrandTier.DISPLAY_NAME_FORMAT, this.getProductBrandName().trim(),
                this.getProductBrandTierCode());
    }

    /**
     * Returns the default sort order for the product brand tier table.
     *
     * @return The default sort order for the product brand tier table.
     */
    public static Sort getDefaultSort() {
        return new Sort(
                new Sort.Order(Sort.Direction.ASC, ProductBrandTier.DEFAULT_TIER_SORT_FIELD)
        );
    }

    /**
     * Compares this object to another for equality.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductBrandTier)) return false;

        ProductBrandTier that = (ProductBrandTier) o;

        if (productBrandTierCode != null ? !productBrandTierCode.equals(that.productBrandTierCode) : that.productBrandTierCode != null)
            return false;
        if (productBrandAbbreviation != null ? !productBrandAbbreviation.equals(that.productBrandAbbreviation) : that.productBrandAbbreviation != null)
            return false;
        if (productBrandName != null ? !productBrandName.equals(that.productBrandName) : that.productBrandName != null)
            return false;
        return ownerBrand != null ? ownerBrand.equals(that.ownerBrand) : that.ownerBrand == null;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "ProductBrandTier{" +
                "productBrandTierCode=" + productBrandTierCode +
                ", productBrandAbbreviation='" + productBrandAbbreviation + '\'' +
                ", productBrandName='" + productBrandName + '\'' +
                ", ownerBrand=" + ownerBrand +
                '}';
    }

    /**
     * Returns a hash code for this object. Equal objects have the same hash code. Unequal objects have
     * different hash codes.
     *
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = productBrandTierCode != null ? productBrandTierCode.hashCode() : 0;
        result = 31 * result + (productBrandAbbreviation != null ? productBrandAbbreviation.hashCode() : 0);
        result = 31 * result + (productBrandName != null ? productBrandName.hashCode() : 0);
        result = 31 * result + (ownerBrand != null ? ownerBrand.hashCode() : 0);
        return result;
    }
}
