/*
 * ProductPkVarAudit
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The persistent class for the prod_pk_variation database table.
 * @author vn70633
 * @since 2.15
 */
@Entity
@Table(name="prod_pk_var_aud")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductPkVarAudit implements Audit, Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ProductPkVarAuditKey key;

    @Column(name="prod_val_des")
    @Type(type="fixedLengthCharPK")
    private String productValueDescription;

    @Column(name="srvng_sz_qty")
    private Double servingSizeQuantity;

    @Column(name = "act_cd")
    private String action;

    @Column(name="srvng_sz_uom_cd")
    private String servingSizeUomCode;

    @Column(name="prod_pan_typ_cd")
    private String panelTypeCode;

    @Column(name="cre8_ts")
    private LocalDateTime createDate;

    @Column(name = "lst_updt_uid")
    private String changedBy;

    @Column(name = "LST_UPDT_TS",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, length = 0)
    private LocalDateTime lastUpdateTs;

    @Column(name="spcr_txt")
    @Type(type="fixedLengthCharPK")
    private String servingsPerContainerText;

    @Column(name="hshld_srvng_sz_txt")
    @Type(type="fixedLengthCharPK")
    private String houseHoldMeasurement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="src_system_id", referencedColumnName = "src_system_id", insertable = false, updatable = false)
    })
    private SourceSystem sourceSystem;

    /**
     * 	Returns the ActionCode. The action code pertains to the action of the audit. 'UPDAT', 'PURGE', or 'ADD'.
     * 	@return ActionCode
     */
    public String getAction() {
        return action;
    }

    /**
     * Updates the action code
     * @param action the new action
     */
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String getChangedBy() {
        return changedBy;
    }

    @Override
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    @Override
    public LocalDateTime getChangedOn() {
        return this.key.getChangedOn();
    }

    public void setChangedOn(LocalDateTime changedOn) {
        this.key.setChangedOn(changedOn);
    }

    /**
     * @return the key
     */
    public ProductPkVarAuditKey getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(ProductPkVarAuditKey key) {
        this.key = key;
    }

    /**
     * @return the panelTypeCode
     */
    public String getPanelTypeCode() {
        return panelTypeCode;
    }

    /**
     * @param panelTypeCode the panelTypeCode to set
     */
    public void setPanelTypeCode(String panelTypeCode) {
        this.panelTypeCode = panelTypeCode;
    }

    /**
     * @return the lastUpdateTs
     */
    public LocalDateTime getLastUpdateTs() {
        return lastUpdateTs;
    }

    /**
     * @param lastUpdateTs the lastUpdateTs to set
     */
    public void setLastUpdateTs(LocalDateTime lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }

    /**
     * @return the createDate
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the sourceSystem
     */
    public SourceSystem getSourceSystem() {
        return sourceSystem;
    }

    /**
     * @param sourceSystem the sourceSystem to set
     */
    public void setSourceSystem(SourceSystem sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    /**
     * @return the productValueDescription
     */
    public String getProductValueDescription() {
        return productValueDescription;
    }

    /**
     * @param productValueDescription the productValueDescription to set
     */
    public void setProductValueDescription(String productValueDescription) {
        this.productValueDescription = productValueDescription;
    }

    /**
     * @return the servingSizeQuantity
     */
    public Double getServingSizeQuantity() {
        return servingSizeQuantity;
    }

    /**
     * @param servingSizeQuantity the servingSizeQuantity to set
     */
    public void setServingSizeQuantity(Double servingSizeQuantity) {
        this.servingSizeQuantity = servingSizeQuantity;
    }

    /**
     * @return the servingSizeUomCode
     */
    public String getServingSizeUomCode() {
        return servingSizeUomCode;
    }

    /**
     * @param servingSizeUomCode the servingSizeUomCode to set
     */
    public void setServingSizeUomCode(String servingSizeUomCode) {
        this.servingSizeUomCode = servingSizeUomCode;
    }

    /**
     * @return the servingsPerContainerText
     */
    public String getServingsPerContainerText() {
        return servingsPerContainerText;
    }

    /**
     * @param servingsPerContainerText the servingsPerContainerText to set
     */
    public void setServingsPerContainerText(String servingsPerContainerText) {
        this.servingsPerContainerText = servingsPerContainerText;
    }

    /**
     * @return the houseHoldMeasurement
     */
    public String getHouseHoldMeasurement() {
        return houseHoldMeasurement;
    }

    /**
     * @param houseHoldMeasurement the houseHoldMeasurement to set
     */
    public void setHouseHoldMeasurement(String houseHoldMeasurement) {
        this.houseHoldMeasurement = houseHoldMeasurement;
    }
}

