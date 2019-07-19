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

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a product line.
 *
 * @author s769046
 * @since 2.26.0
 */
@Entity
@Table(name = "PROD_BRND_LIN")
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class ProductLineBrand implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_PRODUCT_LINE_BRAND_SORT_FIELD = "id";


    @EmbeddedId
    private ProductLineBrandKey key;

    @Column(name = "CRE8_TS")
    private LocalDateTime timeStamp;

    @Column(name = "CRE8_UID")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_brnd_id", referencedColumnName = "prod_brnd_id", insertable = false, updatable = false)
    private ProductBrand productBrand;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_lin_cd", referencedColumnName = "prod_lin_cd", insertable = false, updatable = false)
    private ProductLine productLine;

    public ProductLineBrandKey getKey() {
        return key;
    }

    public ProductLineBrand setKey(ProductLineBrandKey key) {
        this.key = key;
        return this;
    }


    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public ProductLineBrand setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public ProductLineBrand setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Returns the default sort order for the product line table.
     *
     * @return The default sort order for the product line table.
     */

    public static Sort getDefaultSort() {
        return new Sort(
                new Sort.Order(Sort.Direction.ASC, ProductLineBrand.DEFAULT_PRODUCT_LINE_BRAND_SORT_FIELD)
        );
    }


    public ProductBrand getProductBrand() {
        return productBrand;
    }

    public ProductLineBrand setProductBrand(ProductBrand productBrand) {
        this.productBrand = productBrand;
        return this;
    }

    public ProductLine getProductLine() {
        return productLine;
    }

    public ProductLineBrand setProductLine(ProductLine productLine) {
        this.productLine = productLine;
        return this;
    }

    /**
     * Compares another object to this one. The key is the only thing used to determine equality.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductLineBrand that = (ProductLineBrand) o;

        return key != null ? key.equals(that.key) : that.key == null;
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "ProductLine{" +
                "ProductLineKey='" + key + '\'' +
                '}';
    }
}
