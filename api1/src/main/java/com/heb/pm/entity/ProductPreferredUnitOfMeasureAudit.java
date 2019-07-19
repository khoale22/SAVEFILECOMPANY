/*
 * ProductPreferredUnitOfMeasureAudit
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents the preferred unit of measure audit for a product attached to a product hierarchy -> sub commodity.
 *
 * @author vn70529
 * @since 2.18.4
 */
@Entity
@Table(name = "prod_pref_uom_aud")
public class ProductPreferredUnitOfMeasureAudit implements Serializable , Audit {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ProductPreferredUnitOfMeasureAuditKey key;

    @Column(name = "act_cd")
    private String action;

    @Column(name = "lst_updt_uid")
    private String changedBy;

    @Column(name="retl_sell_sz_cd")
    @Type(type="fixedLengthCharPK")
    private String retailUnitOfMeasureCode;

    @AuditableField(displayName = "Default UOM", codeTableDisplayNameMethod = "getPrefUOMSequenceNumberDisplayName", filter = FilterConstants.SUB_COMMODITY_DEFAULTS_AUDIT)
    @Column(name = "pref_uom_seq_nbr")
    private Integer sequenceNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "retl_sell_sz_cd", referencedColumnName = "retl_sell_sz_cd", insertable = false, updatable = false)
    private RetailUnitOfMeasure retailUnitOfMeasure;

    /**
     * Returns the composite key which includes subCommodityCode and retailUnitOfMeasureCode.
     *
     * @return the composite key which includes subCommodityCode and retailUnitOfMeasureCode.
     **/
    public ProductPreferredUnitOfMeasureAuditKey getKey() {
        return key;
    }

    /**
     * Sets the composite key which includes subCommodityCode and retailUnitOfMeasureCode.
     *
     * @param key the composite key which includes subCommodityCode and retailUnitOfMeasureCode.
     **/
    public void setKey(ProductPreferredUnitOfMeasureAuditKey key) {
        this.key = key;
    }

    /**
     * 	Returns the ActionCode. The action code pertains to the action of the audit. 'UPDAT', 'PURGE', or 'ADD'.
     * 	@return ActionCode
     */
    @Override
    public String getAction() {
        return action;
    }

    /**
     * Updates the action code
     * @param action the new action
     */
    @Override
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Returns changed by. The changed by shows who was doing the action that is being audited. This is the uid(login) that a
     * user has.
     *
     * @return the changed by
     */
    @Override
    public String getChangedBy() {
        return changedBy;
    }

    /**
     * Sets the changed by uid received from the database.
     *
     * @param changedBy the changed by
     */
    @Override
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    /**
     * Gets the changed on time. This is when the modification was done.
     *
     * @return The time the modification was done.
     */
    @Override
    public LocalDateTime getChangedOn() {
        return this.key.getChangedOn();
    }

    /**
     * Sets the changed on time.
     *
     * @param changedOn The time the modification was done.
     */
    @Override
    public void setChangedOn(LocalDateTime changedOn) {
        this.key.setChangedOn(changedOn);
    }

    /**
     * Returns code of the retail unit of measure this preferred unit of measure refers to.
     *
     * @return The RetailUOMCode.
     **/
    public String getRetailUnitOfMeasureCode() {
        return retailUnitOfMeasureCode;
    }

    /**
     * Sets the Retail Unit of Measure Code that is sent to the scales.
     *
     * @param retailUnitOfMeasureCode The Retail Unit of Measure Code that is sent to the scales.
     **/
    public void setRetailUnitOfMeasureCode(String retailUnitOfMeasureCode) {
        this.retailUnitOfMeasureCode = retailUnitOfMeasureCode;
    }

    /**
     * Returns sequence number of this preferred unit of measure. There can be up to two.
     *
     * @return The SequenceNumber which is used to determine which row to display.  Higher seq number is displayed.
     **/
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Sets the SequenceNumber which is used to determine which row to display.  Higher seq number is displayed.
     *
     * @param sequenceNumber The SequenceNumber which is used to determine which row to display.  Higher seq number is displayed.
     **/
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Returns retail unit of measure matching retailUnitOfMeasureCode for code table specifics.
     *
     * @return The RetailUOM object that is tied to a productPreferredUnitOfMeasure.
     **/
    public RetailUnitOfMeasure getRetailUnitOfMeasure() {
        return retailUnitOfMeasure;
    }

    /**
     * Sets the RetailUOM object that is tied to a productPreferredUnitOfMeasure.
     *
     * @param retailUnitOfMeasure The RetailUOM object that is tied to a productPreferredUnitOfMeasure.
     **/
    public void setRetailUnitOfMeasure(RetailUnitOfMeasure retailUnitOfMeasure) {
        this.retailUnitOfMeasure = retailUnitOfMeasure;
    }

    /**
     * Get preferred unit of measure sequence number display name in audit table on the GUI.
     *
     * @return A string is the sequence number of preferred unit of measure to display on the GUI.
     */
    public String getPrefUOMSequenceNumberDisplayName(){
        RetailUnitOfMeasure retailUnitOfMeasure = this.getRetailUnitOfMeasure();
        if(retailUnitOfMeasure != null){
            return this.getSequenceNumber().toString();
        } else {
            return String.format(Audit.DISPLAY_NAME_FORMAT_FOR_CODE_TABLE_NOT_FOUND, this.getRetailUnitOfMeasureCode().trim());
        }
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "ProductPreferredUnitOfMeasureAudit{" +
                "key=" + key +
                ", action='" + action +
                ", changedBy='" + changedBy +
                ", sequenceNumber='" + sequenceNumber +
                '}';
    }

}
