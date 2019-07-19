/*
 *  CandidateItemWarehouseVendor
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents the item warehouse vendor of candidate.
 *
 * @author vn00602
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_itm_whse_vend")
public class CandidateItemWarehouseVendor implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String VEND_LOC_TYP_CD_WHS = "V";
    @EmbeddedId
    private CandidateItemWarehouseVendorKey key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "WHSE_LOC_TYP_CD", referencedColumnName = "WHSE_LOC_TYP_CD", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "WHSE_LOC_NBR", referencedColumnName = "WHSE_LOC_NBR", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "PS_ITM_ID", referencedColumnName = "PS_ITM_ID", nullable = false, insertable = false, updatable = false)
    })
    private CandidateWarehouseLocationItem candidateWarehouseLocationItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "VEND_LOC_TYP_CD", referencedColumnName = "VEND_LOC_TYP_CD", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "VEND_LOC_NBR", referencedColumnName = "VEND_LOC_NBR", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "PS_ITM_ID", referencedColumnName = "PS_ITM_ID", nullable = false, insertable = false, updatable = false)
    })
    private CandidateVendorLocationItem candidateVendorLocationItem;

    @Column(name = "LST_UPDT_TS", nullable = false, length = 0)
    private LocalDateTime lastUpdateDate;

    @Column(name = "LST_UPDT_USR_ID", nullable = false, length = 8)
    private String lastUpdateUserId;

    /**
     * The prim vend sw.
     */
    @Column(name = "PRIM_VEND_SW")
    private boolean primaryVendorSwitch;

    /**
     * The frt excp sw.
     */
    @Column(name = "FRT_EXCP_SW")
    private boolean frtExcpSwitch;

    /**
     * The terms excp sw.
     */
    @Column(name = "TERMS_EXCP_SW")
    private boolean termsExcpSwitch;

    /**
     * The itm up dn cst.
     */
    @Column(name = "ITM_UP_DN_CST", precision = 11, scale = 4)
    private Double itmUpDnCst;

    /**
     * The up dn cst cmt.
     */
    @Column(name = "UP_DN_CST_CMT", length = 30)
    private String upDnCstCmt;

    /**
     * The up dn pos or neg.
     */
    @Column(name = "UP_DN_POS_OR_NEG", length = 1)
    private String upDnPosOrNeg;

    /**
     * The frt free sw.
     */
    @Column(name = "FRT_FREE_SW")
    private boolean frtFreeSw;

    /**
     * The tot allow amt.
     */
    @Column(name = "TOT_ALLOW_AMT", precision = 11, scale = 4)
    private Double totAllowAmt;

    /**
     * The mfg srp amt.
     */
    @Column(name = "MFG_SRP_AMT", precision = 11, scale = 4)
    private Double mfgSrpAmt;

    /**
     * The sell amt.
     */
    @Column(name = "SELL_AMT", precision = 11, scale = 4)
    private Double sellAmt;

    /**
     * The frt bil amt.
     */
    @Column(name = "FRT_BIL_AMT", precision = 11, scale = 4)
    private Double frtBilAmt;

    /**
     * The bkhl amt.
     */
    @Column(name = "BKHL_AMT", precision = 11, scale = 4)
    private Double bkhlAmt;

    /**
     * The ppadd excp sw.
     */
    @Column(name = "PPADD_EXCP_SW")
    private boolean ppaddExcpSw;

    /**
     * The frt allow amt.
     */
    @Column(name = "FRT_ALLOW_AMT", precision = 11, scale = 4)
    private Double frtAllowAmt;
    /**
     * Returns the key for this record.
     *
     * @return the key for this record.
     */
    public CandidateItemWarehouseVendorKey getKey() {
        return this.key;
    }

    /**
     * Sets the key for this record.
     *
     * @param key the key for this record.
     */
    public void setKey(CandidateItemWarehouseVendorKey key) {
        this.key = key;
    }

    /**
     * Returns the candidate warehouse location item.
     *
     * @return the candidate warehouse location item.
     */
    public CandidateWarehouseLocationItem getCandidateWarehouseLocationItem() {
        return this.candidateWarehouseLocationItem;
    }

    /**
     * Sets the candidate warehouse location item.
     *
     * @param candidateWarehouseLocationItem the new candidate warehouse location item.
     */
    public void setCandidateWarehouseLocationItem(CandidateWarehouseLocationItem candidateWarehouseLocationItem) {
        this.candidateWarehouseLocationItem = candidateWarehouseLocationItem;
    }

    /**
     * Returns the candidate vendor location item.
     *
     * @return the candidate vendor location item.
     */
    public CandidateVendorLocationItem getCandidateVendorLocationItem() {
        return this.candidateVendorLocationItem;
    }

    /**
     * Sets the candidate vendor location item.
     *
     * @param candidateVendorLocationItem the new candidate vendor location item.
     */
    public void setCandidateVendorLocationItem(CandidateVendorLocationItem candidateVendorLocationItem) {
        this.candidateVendorLocationItem = candidateVendorLocationItem;
    }

    /**
     * Returns the date and time this record was last updated.
     *
     * @return the date and time this record was last updated.
     */
    public LocalDateTime getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    /**
     * Sets the date and time this record was last updated.
     *
     * @param lastUpdateDate the new date and time this record was last updated.
     */
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Returns the one-pass ID of the last user to update this record.
     *
     * @return the one-pass ID of the last user to update this record.
     */
    public String getLastUpdateUserId() {
        return this.lastUpdateUserId;
    }

    /**
     * Sets the one-pass ID of the last user to update this record.
     *
     * @param lastUpdateUserId the new one-pass ID of the last user to update this record.
     */
    public void setLastUpdateUserId(String lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }
    public boolean isPrimaryVendorSwitch() {
        return primaryVendorSwitch;
    }

    public void setPrimaryVendorSwitch(boolean primaryVendorSwitch) {
        this.primaryVendorSwitch = primaryVendorSwitch;
    }

    public boolean isFrtExcpSwitch() {
        return frtExcpSwitch;
    }

    public void setFrtExcpSwitch(boolean frtExcpSwitch) {
        this.frtExcpSwitch = frtExcpSwitch;
    }

    public boolean isTermsExcpSwitch() {
        return termsExcpSwitch;
    }

    public void setTermsExcpSwitch(boolean termsExcpSwitch) {
        this.termsExcpSwitch = termsExcpSwitch;
    }

    public Double getItmUpDnCst() {
        return itmUpDnCst;
    }

    public void setItmUpDnCst(Double itmUpDnCst) {
        this.itmUpDnCst = itmUpDnCst;
    }

    public String getUpDnCstCmt() {
        return upDnCstCmt;
    }

    public void setUpDnCstCmt(String upDnCstCmt) {
        this.upDnCstCmt = upDnCstCmt;
    }

    public String getUpDnPosOrNeg() {
        return upDnPosOrNeg;
    }

    public void setUpDnPosOrNeg(String upDnPosOrNeg) {
        this.upDnPosOrNeg = upDnPosOrNeg;
    }

    public boolean isFrtFreeSw() {
        return frtFreeSw;
    }

    public void setFrtFreeSw(boolean frtFreeSw) {
        this.frtFreeSw = frtFreeSw;
    }

    public Double getTotAllowAmt() {
        return totAllowAmt;
    }

    public void setTotAllowAmt(Double totAllowAmt) {
        this.totAllowAmt = totAllowAmt;
    }

    public Double getMfgSrpAmt() {
        return mfgSrpAmt;
    }

    public void setMfgSrpAmt(Double mfgSrpAmt) {
        this.mfgSrpAmt = mfgSrpAmt;
    }

    public Double getSellAmt() {
        return sellAmt;
    }

    public void setSellAmt(Double sellAmt) {
        this.sellAmt = sellAmt;
    }

    public Double getFrtBilAmt() {
        return frtBilAmt;
    }

    public void setFrtBilAmt(Double frtBilAmt) {
        this.frtBilAmt = frtBilAmt;
    }

    public Double getBkhlAmt() {
        return bkhlAmt;
    }

    public void setBkhlAmt(Double bkhlAmt) {
        this.bkhlAmt = bkhlAmt;
    }

    public boolean isPpaddExcpSw() {
        return ppaddExcpSw;
    }

    public void setPpaddExcpSw(boolean ppaddExcpSw) {
        this.ppaddExcpSw = ppaddExcpSw;
    }

    public Double getFrtAllowAmt() {
        return frtAllowAmt;
    }

    public void setFrtAllowAmt(Double frtAllowAmt) {
        this.frtAllowAmt = frtAllowAmt;
    }
    /**
     * Compares another object to this one. This is a deep compare.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateItemWarehouseVendor)) return false;

        CandidateItemWarehouseVendor that = (CandidateItemWarehouseVendor) o;

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
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateItemWarehouseVendor{" +
                "key=" + key +
                ", lastUpdateDate=" + lastUpdateDate +
                ", lastUpdateUserId='" + lastUpdateUserId + '\'' +
                '}';
    }
    /**
     * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
     * it is inserted into the work request table.
     */
    @PrePersist
    public void setCandidateItemId() {
        if (this.getKey().getCandidateItemId()== null) {
            if(this.getCandidateWarehouseLocationItem()!=null && this.getCandidateWarehouseLocationItem().getKey().getCandidateItemId()!=null){
                this.getKey().setCandidateItemId(this.getCandidateWarehouseLocationItem().getKey().getCandidateItemId());
            } else if(this.getCandidateVendorLocationItem()!=null && this.getCandidateVendorLocationItem().getKey().getCandidateItemId()!=null){
                this.getKey().setCandidateItemId(this.getCandidateVendorLocationItem().getKey().getCandidateItemId());
            }
        }

    }
}
