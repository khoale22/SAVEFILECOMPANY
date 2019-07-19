/*
 * com.heb.pm.entity.ItemMasterKey
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
import java.util.Objects;

/**
 * Composite key for Item_Master.
 *
 * @author d116773
 * @since 2.0.0
 */
@Embeddable
public class ProductLineBrandKey implements Serializable {

    private static final long serialVersionUID = 1L;


    @Column(name="prod_brnd_id")
    private Long brandId;

    @Column(name="prod_lin_cd")
    private String lineCode;

    /**
     * Returns the brand id for the ProductDiscontinue object.
     *
     * @return The brand id for the ProductDiscontinue object.
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * Sets the brand id for the ProductDiscontinue object.
     *
     * @param brandId The item code for the ProductDiscontinue object.
     */
    public ProductLineBrandKey setBrandId(Long brandId) {

        this.brandId = brandId;
        return this;
    }

    /**
     * Returns the line code for the ProductDiscontinue object.
     *
     * @return The line code for the ProductDiscontinue object.
     */
    public String getLineCode() {
        return lineCode;
    }

    /**
     * Returns the line code for the ProductDiscontinue object.
     *
     * @param lineCode The line code for the ProductDiscontinue object.
     */
    public ProductLineBrandKey setLineCode(String lineCode) {

        this.lineCode = lineCode;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductLineBrandKey that = (ProductLineBrandKey) o;
        return brandId.equals(that.brandId) &&
                lineCode.equals(that.lineCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandId, lineCode);
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "ProductLineBrandKey{" +
                "brandId='" + brandId + '\'' +
                ", lineCode=" + lineCode +
                '}';
    }
}
