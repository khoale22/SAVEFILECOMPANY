/*
 * ProductAttributeOverwriteKey
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

/**
 * The key for the product attribute overwrite
 *
 * @author vn70516
 * @since 2.14.0
 */
@Embeddable
public class ProductAttributeOverwriteKey implements Serializable{

    private static final long serialVersionUID = 1L;

    @Column(name = "trgt_sys_id")
    private Long systemId;

    @Column(name = "itm_prod_key_cd")
    private String itemProductKeyCode;

    @Column(name = "key_id")
    private Long keyId;

    @Column(name = "logic_attr_id")
    private Long logicAttributeId;

    @Column(name = "dta_src_sys_id")
    private Long sourceSystemId;

    @Column(name = "phy_attr_id")
    private Long physicalAttributeId;

    @Column(name = "rlshp_grp_typ_cd")
    private String relationshipGroupTypeCode;

    /**
     * @return Gets the value of systemId and returns systemId
     */
    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    /**
     * Sets the systemId
     */
    public Long getSystemId() {
        return systemId;
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
     * @return Gets the value of keyId and returns keyId
     */
    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    /**
     * Sets the keyId
     */
    public Long getKeyId() {
        return keyId;
    }

    /**
     * @return Gets the value of logicAttributeId and returns logicAttributeId
     */
    public void setLogicAttributeId(Long logicAttributeId) {
        this.logicAttributeId = logicAttributeId;
    }

    /**
     * Sets the logicAttributeId
     */
    public Long getLogicAttributeId() {
        return logicAttributeId;
    }

    /**
     * @return Gets the value of sourceSystemId and returns sourceSystemId
     */
    public void setSourceSystemId(Long sourceSystemId) {
        this.sourceSystemId = sourceSystemId;
    }

    /**
     * Sets the sourceSystemId
     */
    public Long getSourceSystemId() {
        return sourceSystemId;
    }

    /**
     * @return Gets the value of physicalAttributeId and returns physicalAttributeId
     */
    public void setPhysicalAttributeId(Long physicalAttributeId) {
        this.physicalAttributeId = physicalAttributeId;
    }

    /**
     * Sets the physicalAttributeId
     */
    public Long getPhysicalAttributeId() {
        return physicalAttributeId;
    }

    /**
     * @return Gets the value of relationshipGroupTypeCode and returns relationshipGroupTypeCode
     */
    public void setRelationshipGroupTypeCode(String relationshipGroupTypeCode) {
        this.relationshipGroupTypeCode = relationshipGroupTypeCode;
    }

    /**
     * Sets the relationshipGroupTypeCode
     */
    public String getRelationshipGroupTypeCode() {
        return relationshipGroupTypeCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductAttributeOverwriteKey)) return false;

        ProductAttributeOverwriteKey that = (ProductAttributeOverwriteKey) o;

        if (getItemProductKeyCode() != null ? !getItemProductKeyCode().equals(that.getItemProductKeyCode()) : that.getItemProductKeyCode() != null)
            return false;
        if (getKeyId() != null ? !getKeyId().equals(that.getKeyId()) : that.getKeyId() != null) return false;
        if (getLogicAttributeId() != null ? !getLogicAttributeId().equals(that.getLogicAttributeId()) : that.getLogicAttributeId() != null)
            return false;
        if (getSourceSystemId() != null ? !getSourceSystemId().equals(that.getSourceSystemId()) : that.getSourceSystemId() != null)
            return false;
        if (getPhysicalAttributeId() != null ? !getPhysicalAttributeId().equals(that.getPhysicalAttributeId()) : that.getPhysicalAttributeId() != null)
            return false;
        return getRelationshipGroupTypeCode() != null ? getRelationshipGroupTypeCode().equals(that.getRelationshipGroupTypeCode()) : that.getRelationshipGroupTypeCode() == null;
    }

    @Override
    public int hashCode() {
        int result = getItemProductKeyCode() != null ? getItemProductKeyCode().hashCode() : 0;
        result = 31 * result + (getKeyId() != null ? getKeyId().hashCode() : 0);
        result = 31 * result + (getLogicAttributeId() != null ? getLogicAttributeId().hashCode() : 0);
        result = 31 * result + (getSourceSystemId() != null ? getSourceSystemId().hashCode() : 0);
        result = 31 * result + (getPhysicalAttributeId() != null ? getPhysicalAttributeId().hashCode() : 0);
        result = 31 * result + (getRelationshipGroupTypeCode() != null ? getRelationshipGroupTypeCode().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductAttributeOverwriteKey{" +
                "itemProductKeyCode='" + itemProductKeyCode + '\'' +
                ", keyId=" + keyId +
                ", logicAttributeId=" + logicAttributeId +
                ", sourceSystemId=" + sourceSystemId +
                ", physicalAttributeId=" + physicalAttributeId +
                ", relationshipGroupTypeCode=" + relationshipGroupTypeCode +
                '}';
    }
}