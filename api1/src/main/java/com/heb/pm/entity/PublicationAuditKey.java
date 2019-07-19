/*
 * PublicationAuditKey
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
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a publication audit key. A publish audit key contains the id of the publication audit.
 *
 * @author vn70516
 * @since 2.14.0
 */
@TypeDefs({
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
@Embeddable
public class PublicationAuditKey implements Serializable {

    public static final String PRODUCT_KEY_TYPE = "PROD ";

    private static final long serialVersionUID = 1L;

    @Column(name = "itm_prod_id")
    private Long itemProductId;

    @Column(name = "itm_prod_key_cd")
    @Type(type = "fixedLengthChar")
    private String itemProductKeyCode;

    @Column(name = "ENTY_TYP_CD")
    @Type(type = "fixedLengthChar")
    private String entityTypeCode;

    @Column(name = "pblctn_ts")
    private LocalDateTime publishDate;

    /**
     * @return Gets the value of itemProductId and returns itemProductId
     */
    public void setItemProductId(Long itemProductId) {
        this.itemProductId = itemProductId;
    }

    /**
     * Sets the itemProductId
     */
    public Long getItemProductId() {
        return itemProductId;
    }

    /**
     * @return Gets the value of itemProductKeyCode and returns itemProductKeyCode
     */
    public void setItemProductKeyCode(String itemProductKeyCode) {
        this.itemProductKeyCode = itemProductKeyCode;
    }

    /**
     * Sets the itemProductKeyCode
     */
    public String getItemProductKeyCode() {
        return itemProductKeyCode;
    }

    /**
     * @return Gets the value of entityTypeCode and returns entityTypeCode
     */
    public void setEntityTypeCode(String entityTypeCode) {
        this.entityTypeCode = entityTypeCode;
    }

    /**
     * Sets the entityTypeCode
     */
    public String getEntityTypeCode() {
        return entityTypeCode;
    }

    /**
     * @return Gets the value of publishDate and returns publishDate
     */
    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * Sets the publishDate
     */
    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationAuditKey)) return false;

        PublicationAuditKey that = (PublicationAuditKey) o;

        if (getItemProductId() != null ? !getItemProductId().equals(that.getItemProductId()) : that.getItemProductId() != null)
            return false;
        if (getItemProductKeyCode() != null ? !getItemProductKeyCode().equals(that.getItemProductKeyCode()) : that.getItemProductKeyCode() != null)
            return false;
        if (getEntityTypeCode() != null ? !getEntityTypeCode().equals(that.getEntityTypeCode()) : that.getEntityTypeCode() != null)
            return false;
        return getPublishDate() != null ? getPublishDate().equals(that.getPublishDate()) : that.getPublishDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getItemProductId() != null ? getItemProductId().hashCode() : 0;
        result = 31 * result + (getItemProductKeyCode() != null ? getItemProductKeyCode().hashCode() : 0);
        result = 31 * result + (getEntityTypeCode() != null ? getEntityTypeCode().hashCode() : 0);
        result = 31 * result + (getPublishDate() != null ? getPublishDate().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PublicationAuditKey{" +
                "itemProductId='" + itemProductId + '\'' +
                ", itemProductKeyCode='" + itemProductKeyCode + '\'' +
                ", entityTypeCode='" + entityTypeCode + '\'' +
                ", publishDate=" + publishDate +
                '}';
    }
}