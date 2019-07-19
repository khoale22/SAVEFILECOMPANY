/*
 * SubCommodityStateWarningAuditKey
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * An embeddable key for sub commodity state warning audit.
 *
 * @author vn70529
 * @since 2.18.4
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class SubCommodityStateWarningAuditKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "AUD_REC_CRE8_TS")
    private LocalDateTime changedOn;

    @Column(name = "pd_omi_sub_com_cd")
    private Integer subCommodityCode;

    @Column(name = "st_cd")
    @Type(type="fixedLengthCharPK")
    private String stateCode;

    @Column(name = "st_prod_warn_cd")
    @Type(type="fixedLengthCharPK")
    private String stateProductWarningCode;

    /**
     * Get the timestamp for when the record was created
     * @return the timestamp for when the record was created
     */
    public LocalDateTime getChangedOn() {
        return changedOn;
    }

    /**
     * Updates the timestamp for when the record was created
     * @param changedOn the new timestamp for when the record was created
     */
    public void setChangedOn(LocalDateTime changedOn) {
        this.changedOn = changedOn;
    }

    /**
     * Returns code of the sub commodity this state warning refers to.
     *
     * @return The SubCommodityCode  that identifies a subcommodity  is a lower identification of the commodity and brand.
     **/
    public Integer getSubCommodityCode() {
        return subCommodityCode;
    }

    /**
     * Sets the SubCommodityCode  that identifies a subcommodity  is a lower identification of the commodity and brand.
     *
     * @param subCommodityCode The SubCommodityCode that identifies a subcommodity is a lower identification of the commodity and brand.
     **/
    public void setSubCommodityCode(Integer subCommodityCode) {
        this.subCommodityCode = subCommodityCode;
    }

    /**
     * Returns code of the state this state warning refers to.
     *
     * @return The StateCode that identifies the state the warning applies to.
     **/
    public String getStateCode() {
        return stateCode;
    }

    /**
     * Sets the StateCode that identifies the state the warning applies to.
     *
     * @param stateCode The StateCode that identifies the state the warning applies to.
     **/
    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    /**
     * Returns code of the product state warning this state warning refers to.
     *
     * @return The StateProductWarningCode that identifies the warning that is being set.
     **/
    public String getStateProductWarningCode() {
        return stateProductWarningCode;
    }

    /**
     * Sets the StateProductWarningCode that identifies the warning that is being set.
     *
     * @param stateProductWarningCode The StateProductWarningCode that identifies the warning that is being set.
     **/
    public void setStateProductWarningCode(String stateProductWarningCode) {
        this.stateProductWarningCode = stateProductWarningCode;
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "SubCommodityStateWarningKey{" +
                "changedOn=" + changedOn +
                "subCommodityCode=" + subCommodityCode +
                ", stateCode='" + stateCode + '\'' +
                ", stateProductWarningCode='" + stateProductWarningCode + '\'' +
                '}';
    }
}
