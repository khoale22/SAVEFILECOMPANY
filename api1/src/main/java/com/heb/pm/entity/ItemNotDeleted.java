/*
 * com.heb.pm.entity.ItemNotDeleted
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents the Reason(s) for why a particular item was not deleted when the item went through the Delete process.
 *
 * @author vn40486
 * @since 2.0.4
 */
@Entity
@Table(name = "itm_nt_deld")
public class ItemNotDeleted implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ItemNotDeletedKey key;

    @Column(name="lst_updt_ts")
    private LocalDateTime lastUpdateTime;

    @NotFound(action = NotFoundAction.IGNORE)
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="itm_id", referencedColumnName = "itm_id", insertable = false, updatable = false, nullable = false),
            @JoinColumn(name="itm_prod_key_cd", referencedColumnName = "itm_key_typ_cd", insertable = false, updatable = false, nullable = false)
    })
    private ItemMaster itemMaster;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itm_nt_deld_rsn_cd", referencedColumnName = "itm_nt_deld_rsn_cd", insertable = false, updatable = false, nullable = false)
    private ItemNotDeletedReason itemNotDeletedReason;

    /**
     * Returns item not deleted key.
     *
     * @return item not deleted key.
     */
    public ItemNotDeletedKey getKey() {
        return key;
    }

    /**
     * Sets item not deleted key.
     *
     * @param key item not deleted key.
     */
    public void setKey(ItemNotDeletedKey key) {
        this.key = key;
    }

    /**
     * Returns Last Update Time.
     *
     * @return  Last Update Time.
     */
    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * Sets Last Update Time.
     *
     * @param lastUpdateTime Last Update Time.
     */
    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * Returns the item master info associated to this item.
     * @return the item master info.
     */
    public ItemMaster getItemMaster() {
        return itemMaster;
    }

    /**
     * Sets the item master info of this item.
     * @param itemMaster item master info.
     */
    public void setItemMaster(ItemMaster itemMaster) {
        this.itemMaster = itemMaster;
    }

    /**
     * Returns item not deleted reason detail of the code that this item is record associated with.
     *
     * @return item not deleted reason detail.
     */
    public ItemNotDeletedReason getItemNotDeletedReason() {
        return itemNotDeletedReason;
    }

    /**
     * Sets item not deleted reason detail of the code that this item is record associated with.
     *
     * @param itemNotDeletedReason item not deleted reason detail.
     */
    public void setItemNotDeletedReason(ItemNotDeletedReason itemNotDeletedReason) {
        this.itemNotDeletedReason = itemNotDeletedReason;
    }

    /**
     * Compares another object to this one. If that object is an ItemNotDeleted, it uses they keys
     * to determine if they are equal and ignores non-key values for the comparison.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemNotDeleted that = (ItemNotDeleted) o;

        return !(key != null ? !key.equals(that.key) : that.key != null);

    }

    /**
     * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
     * they have different hash codes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "ItemNotDeleted{" +
                "key=" + key +
                ", lastUpdateTime=" + lastUpdateTime +
                ", itemMaster=" + itemMaster +
                ", itemNotDeletedReason=" + itemNotDeletedReason +
                '}';
    }
}
