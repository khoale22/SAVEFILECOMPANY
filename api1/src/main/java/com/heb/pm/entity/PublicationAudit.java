/*
 * PublicationAudit
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents the publication audit.
 *
 * @author vn70516
 * @since 2.14.0
 */
@Entity
@Table(name = "pblctn_aud")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class)
})
public class PublicationAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private PublicationAuditKey key;

    @Column(name = "trgt_sys_id")
    private Long targetSystemId;

    @Column(name = "pblsher_uid")
    private String publishBy;

    /**
     * @return Gets the value of key and returns key
     */
    public void setKey(PublicationAuditKey key) {
        this.key = key;
    }

    /**
     * Sets the key
     */
    public PublicationAuditKey getKey() {
        return key;
    }

    /**
     * @return Gets the value of targetSystemId and returns targetSystemId
     */
    public void setTargetSystemId(Long targetSystemId) {
        this.targetSystemId = targetSystemId;
    }

    /**
     * Sets the targetSystemId
     */
    public Long getTargetSystemId() {
        return targetSystemId;
    }

    /**
     * @return Gets the value of publishBy and returns publishBy
     */
    public void setPublishBy(String publishBy) {
        this.publishBy = publishBy;
    }

    /**
     * Sets the publishBy
     */
    public String getPublishBy() {
        return publishBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationAudit)) return false;

        PublicationAudit that = (PublicationAudit) o;

        if (getKey() != null ? !getKey().equals(that.getKey()) : that.getKey() != null) return false;
        if (getTargetSystemId() != null ? !getTargetSystemId().equals(that.getTargetSystemId()) : that.getTargetSystemId() != null)
            return false;
        return getPublishBy() != null ? getPublishBy().equals(that.getPublishBy()) : that.getPublishBy() == null;
    }

    @Override
    public int hashCode() {
        int result = getKey() != null ? getKey().hashCode() : 0;
        result = 31 * result + (getTargetSystemId() != null ? getTargetSystemId().hashCode() : 0);
        result = 31 * result + (getPublishBy() != null ? getPublishBy().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PublicationAudit{" +
                "key=" + key +
                ", targetSystemId=" + targetSystemId +
                ", publishBy='" + publishBy + '\'' +
                '}';
    }
}