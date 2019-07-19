/*
 * ProductSubBrand
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents a product sub brand.
 *
 * @author m594201
 * @since 2.7.0
 */
@Entity
@Table(name = "prod_sub_brnd")
public class ProductSubBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_SUB_BRAND_SORT_FIELD = "prodSubBrandId";

    @Id
    @Column(name = "prod_sub_brnd_id")
    private Long prodSubBrandId;

    @Column(name = "prod_sub_brnd_nm")
    private String prodSubBrandName;

    /**
     * Gets prod sub brand id to link it to a prod brand id.
     *
     * @return the prod sub brand id to link it to a prod brand id.
     */
    public Long getProdSubBrandId() {
        return prodSubBrandId;
    }

    /**
     * Sets prod sub brand id to link it to a prod brand id.
     *
     * @param prodSubBrandId the prod sub brand id to link it to a prod brand id.
     */
    public void setProdSubBrandId(Long prodSubBrandId) {
        this.prodSubBrandId = prodSubBrandId;
    }

    /**
     * Gets prod sub brand name that the upc is under.
     *
     * @return the prod sub brand name that the upc is under.
     */
    public String getProdSubBrandName() {
        return prodSubBrandName;
    }

    /**
     * Sets prod sub brand name that the upc is under.
     *
     * @param prodSubBrandName the prod sub brand name that the upc is under.
     */
    public void setProdSubBrandName(String prodSubBrandName) {
        this.prodSubBrandName = prodSubBrandName;
    }

    /**
     * Returns the default sort order for the product brand tier table.
     *
     * @return The default sort order for the product brand tier table.
     */
    public static Sort getDefaultSort() {
        return new Sort(
                new Sort.Order(Sort.Direction.ASC, ProductSubBrand.DEFAULT_SUB_BRAND_SORT_FIELD)
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
        if (!(o instanceof ProductSubBrand)) return false;

        ProductSubBrand that = (ProductSubBrand) o;

        if (prodSubBrandId != null ? !prodSubBrandId.equals(that.prodSubBrandId) : that.prodSubBrandId != null)
            return false;
        return prodSubBrandName != null ? prodSubBrandName.equals(that.prodSubBrandName) : that.prodSubBrandName == null;
    }

    /**
     * Returns a hash code for this object. Equal objects have the same hash code. Unequal objects have
     * different hash codes.
     *
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = prodSubBrandId != null ? prodSubBrandId.hashCode() : 0;
        result = 31 * result + (prodSubBrandName != null ? prodSubBrandName.hashCode() : 0);
        return result;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "ProductSubBrand{" +
                "prodSubBrandId=" + prodSubBrandId +
                ", prodSubBrandName='" + prodSubBrandName + '\'' +
                '}';
    }
}
