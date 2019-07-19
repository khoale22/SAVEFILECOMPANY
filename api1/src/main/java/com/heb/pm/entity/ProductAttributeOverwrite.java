/*
 * ProductAttributeOverwrite
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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents the product attribute overwrite.
 *
 * @author vn70516
 * @since 2.14.0
 */
@Entity
@Table(name = "prod_attr_ovrd")
public class ProductAttributeOverwrite implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ProductAttributeOverwriteKey key;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumns({
            @JoinColumn(name="trgt_sys_id", referencedColumnName = "trgt_sys_id", insertable = false, updatable = false),
            @JoinColumn(name = "logic_attr_id", referencedColumnName = "logic_attr_id", insertable = false, updatable = false),
            @JoinColumn(name="dta_src_sys_id", referencedColumnName = "dta_src_sys_id", insertable = false, updatable = false),
            @JoinColumn(name = "phy_attr_id", referencedColumnName = "phy_attr_id", insertable = false, updatable = false),
            @JoinColumn(name = "rlshp_grp_typ_cd", referencedColumnName = "rlshp_grp_typ_cd", insertable = false, updatable = false)
    })
    private TargetSystemAttributePriority targetSystemAttributePriority;

    /**
     * @return Gets the value of key and returns key
     */
    public void setKey(ProductAttributeOverwriteKey key) {
        this.key = key;
    }

    /**
     * Sets the key
     */
    public ProductAttributeOverwriteKey getKey() {
        return key;
    }

    /**
     * @return Gets the value of targetSystemAttributePriority and returns targetSystemAttributePriority
     */
    public void setTargetSystemAttributePriority(TargetSystemAttributePriority targetSystemAttributePriority) {
        this.targetSystemAttributePriority = targetSystemAttributePriority;
    }

    /**
     * Sets the targetSystemAttributePriority
     */
    public TargetSystemAttributePriority getTargetSystemAttributePriority() {
        return targetSystemAttributePriority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductAttributeOverwrite)) return false;

        ProductAttributeOverwrite that = (ProductAttributeOverwrite) o;

        if (getKey() != null ? !getKey().equals(that.getKey()) : that.getKey() != null) return false;
        return getTargetSystemAttributePriority() != null ? getTargetSystemAttributePriority().equals(that.getTargetSystemAttributePriority()) : that.getTargetSystemAttributePriority() == null;
    }

    @Override
    public int hashCode() {
        int result = getKey() != null ? getKey().hashCode() : 0;
        result = 31 * result + (getTargetSystemAttributePriority() != null ? getTargetSystemAttributePriority().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductAttributeOverwrite{" +
                "key=" + key +
                '}';
    }
}