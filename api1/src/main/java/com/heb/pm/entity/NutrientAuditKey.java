/*
 * NutrientAuditKey
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents Nutrient Audit Key.
 * @author vn70633
 * @since 2.15.0
 */
@Embeddable
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class NutrientAuditKey implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String KEY_SORT_FIELD = "key.key";

    @Column(name = "SCN_CD_ID")
    private Long upc;

    @Column(name = "ntrnt_mst_id")
    private Integer masterId;

    @Column(name = "val_preprd_typ_cd")
    private Integer valPreprdTypCd;

    @Column(name="src_system_id")
    private int sourceSystem;

    @Column(name = "AUD_REC_CRE8_TS", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, length = 0)
    private LocalDateTime changedOn;

    public Long getUpc() {
        return upc;
    }

    public void setUpc(Long upc) {
        this.upc = upc;
    }
    /**
     * Getter for changedOn attribute
     * @return
     */
    public LocalDateTime getChangedOn() {
        return changedOn;
    }

    /**
     * Setter for changedOn attribute
     * @param changedOn - new attribute to set
     */
    public void setChangedOn(LocalDateTime changedOn) {
        this.changedOn = changedOn;
    }

    /**
     * Returns the ID of the attribute this key is for.
     *
     * @return The ID of the attribute this key is for.
     */
    public int getMasterId() {
        return masterId;
    }

    /**
     * Sets the ID of the attribute this key is for.
     *
     * @param masterId The ID of the attribute this key is for.
     */
    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    /**
     * Returns the ID of the attribute this key is for.
     *
     * @return The ID of the attribute this key is for.
     */
    public int getValPreprdTypCd() {
        return valPreprdTypCd;
    }

    /**
     * Sets the ID of the attribute this key is for.
     *
     * @param valPreprdTypCd The ID of the attribute this key is for.
     */
    public void setValPreprdTypCd(int valPreprdTypCd) {
        this.valPreprdTypCd = valPreprdTypCd;
    }

    /**
     * Returns the system this attribute value came from.
     *
     * @return The system this attribute value came from.
     */
    public int getSourceSystem() {
        return sourceSystem;
    }

    /**
     * Sets the system this attribute value came from.
     *
     * @param sourceSystem the system this attribute value came from.
     */
    public void setSourceSystem(int sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "NutrientAuditKey{" +
                "scan code=" + upc +
                ", sourceSystem=" + sourceSystem +
                '}';
    }

    public static Sort getDefaultSort() {
        return new Sort(new Sort.Order(Sort.Direction.ASC, NutrientAuditKey.KEY_SORT_FIELD));
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
        if (!(o instanceof NutrientAuditKey)) return false;

        NutrientAuditKey that = (NutrientAuditKey) o;

        if (sourceSystem != that.sourceSystem) return false;
        if (upc != null ? !upc.equals(that.upc) : that.upc != null) return false;
        if (masterId != null ? !masterId.equals(that.masterId) : that.masterId != null) return false;
        if (valPreprdTypCd != null ? !valPreprdTypCd.equals(that.valPreprdTypCd) : that.valPreprdTypCd != null)
            return false;
        return changedOn != null ? changedOn.equals(that.changedOn) : that.changedOn == null;
    }

    /**
     * Returns a hash code for this object.
     *
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = upc != null ? upc.hashCode() : 0;
        result = 31 * result + (masterId != null ? masterId.hashCode() : 0);
        result = 31 * result + (valPreprdTypCd != null ? valPreprdTypCd.hashCode() : 0);
        result = 31 * result + sourceSystem;
        result = 31 * result + (changedOn != null ? changedOn.hashCode() : 0);
        return result;
    }
}

