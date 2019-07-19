/*
 * ProductTrashCan
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Product Trash Can entity.
 *
 * @author vn70529
 * @since 2.12.0
 */
@Entity
@Table(name = "prod_trash_can")
public class ProductTrashCan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "prod_id")
    private long productId;

    @Column(name = "onlin_sale_only_sw")
    private Boolean onlineSaleOnlySw;

    @Column(name = "pblsh_to_ecomm_cd")
    private String publishToECommerceCd;

    /**
     * Returns the product id.
     *
     * @return the product id.
     */
    public long getProductId() {
        return productId;
    }

    /**
     * Sets the product id.
     *
     * @param productId the product id.
     */
    public void setProductId(long productId) {
        this.productId = productId;
    }

    /**
     * Returns the OnlineSaleOnlySw.
     *
     * @return the OnlineSaleOnlySw.
     */
    public Boolean getOnlineSaleOnlySw() {
        return onlineSaleOnlySw;
    }

    /**
     * Sets the OnlineSaleOnlySw.
     *
     * @param onlineSaleOnlySw the OnlineSaleOnlySw.
     */
    public void setOnlineSaleOnlySw(Boolean onlineSaleOnlySw) {
        this.onlineSaleOnlySw = onlineSaleOnlySw;
    }

    /**
     * Returns the PublishToECommerceCd.
     *
     * @return the PublishToECommerceCd.
     */
    public String getPublishToECommerceCd() {
        return publishToECommerceCd;
    }

    /**
     * Sets the PublishToECommerceCd.
     *
     * @param publishToECommerceCd the PublishToECommerceCd.
     */
    public void setPublishToECommerceCd(String publishToECommerceCd) {
        this.publishToECommerceCd = publishToECommerceCd;
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
        if (!(o instanceof ProductTrashCan)) return false;
        ProductTrashCan that = (ProductTrashCan) o;
        return productId == that.productId;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "ProductTrashCan{" +
                "productId='" + productId + '\'' +
                ", onlineSaleOnlySw='" + onlineSaleOnlySw + '\'' +
                ", publishToECommerceCd='" + publishToECommerceCd + '\'' +
                '}';
    }
}