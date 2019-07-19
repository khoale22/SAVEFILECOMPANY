/*
 * CandidateVendorItemStore
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Represents a ps_vend_itm_str
 *
 * @author vn70516
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_vend_itm_str")
public class CandidateVendorItemStore implements Serializable {
    public static final String DEFAULT_EMPTY_STRING = " ";
    @EmbeddedId
    private CandidateVendorItemStoreKey key;

    @Column(name = "authn_sw")
    private Boolean authorized;

    /**
     * The unath dt.
     */
    @Column(name = "UNATH_DT")
    private LocalDate unAuthorizeDate;

    /**
     * The auth dt.
     */

    @Column(name = "AUTH_DT")
    private LocalDate authorizeDate;

    /**
     * The inv seq nbr.
     */
    @Column(name = "INV_SEQ_NBR")
    private Integer invSeqNbr;

    /**
     * The lst updt ts.
     */
//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LST_UPDT_TS", nullable = false, length = 0)
    private LocalDateTime lastUpdate;

    /**
     * The lst updt usr id.
     */
    @Column(name = "LST_UPDT_USR_ID", nullable = false, length = 8)
    @Type(type = "fixedLengthCharPK")
    private String lastUpdateUserId;

    /**
     * The dir cst amt.
     */
    @Column(name = "DIR_CST_AMT", precision = 11, scale = 4)
    private Double dirCstAmt;

    /**
     * The cst grp id.
     */
    @Column(name = "CST_GRP_ID")
    private Integer costGroupId;

    /**
     * The omit str sw.
     */
    @Column(name = "OMIT_STR_SW")
    private Boolean omitStrSWitch;
    /**
     * The ps vend loc itm.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "VEND_LOC_TYP_CD", referencedColumnName = "VEND_LOC_TYP_CD", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "VEND_LOC_NBR", referencedColumnName = "VEND_LOC_NBR", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "PS_ITM_ID", referencedColumnName = "PS_ITM_ID", nullable = false, insertable = false, updatable = false)
    })
    private CandidateVendorLocationItem candidateVendorLocationItem;

    /**
     * @return Gets the value of key and returns key
     */
    public void setKey(CandidateVendorItemStoreKey key) {
        this.key = key;
    }

    /**
     * Sets the key
     */
    public CandidateVendorItemStoreKey getKey() {
        return key;
    }

    /**
     * @return Gets the value of authorized and returns authorized
     */
    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }
    public LocalDate getUnAuthorizeDate() {
        return unAuthorizeDate;
    }

    public void setUnAuthorizeDate(LocalDate unAuthorizeDate) {
        this.unAuthorizeDate = unAuthorizeDate;
    }

    public LocalDate getAuthorizeDate() {
        return authorizeDate;
    }

    public void setAuthorizeDate(LocalDate authorizeDate) {
        this.authorizeDate = authorizeDate;
    }

    public Integer getInvSeqNbr() {
        return invSeqNbr;
    }

    public void setInvSeqNbr(Integer invSeqNbr) {
        this.invSeqNbr = invSeqNbr;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(String lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    public Double getDirCstAmt() {
        return dirCstAmt;
    }

    public void setDirCstAmt(Double dirCstAmt) {
        this.dirCstAmt = dirCstAmt;
    }

    public Integer getCostGroupId() {
        return costGroupId;
    }

    public void setCostGroupId(Integer costGroupId) {
        this.costGroupId = costGroupId;
    }

    public Boolean getOmitStrSWitch() {
        return omitStrSWitch;
    }

    public void setOmitStrSWitch(Boolean omitStrSWitch) {
        this.omitStrSWitch = omitStrSWitch;
    }
    /**
     * Sets the authorized
     */
    public Boolean isAuthorized() {
        return authorized;
    }
    /**
     * Sets the candidateVendorLocationItem
     */
    public CandidateVendorLocationItem getCandidateVendorLocationItem() {
        return candidateVendorLocationItem;
    }

    public void setCandidateVendorLocationItem(CandidateVendorLocationItem candidateVendorLocationItem) {
        this.candidateVendorLocationItem = candidateVendorLocationItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateVendorItemStore)) return false;

        CandidateVendorItemStore that = (CandidateVendorItemStore) o;

        if (isAuthorized() != that.isAuthorized()) return false;
        return getKey() != null ? getKey().equals(that.getKey()) : that.getKey() == null;
    }

    @Override
    public int hashCode() {
        int result = getKey() != null ? getKey().hashCode() : 0;
        result = 31 * result + (isAuthorized() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CandidateVendorItemStore{" +
                "key=" + key +
                ", authorized=" + authorized +
                '}';
    }
    /**
     * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
     * it is inserted into the work request table.
     */
    @PrePersist
    public void setCandidateItemId() {
        if (this.getKey().getCandidateItemId() == null) {
            this.getKey().setCandidateItemId(this.candidateVendorLocationItem.getKey().getCandidateItemId());
        }
    }
}