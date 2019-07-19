/*
 * SellingRestrictionHierarchyLevelAuditKey
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
 * Represents the key for sals_rstr_grp_dflt_aud.
 *
 * @author vn20529
 * @since 2.18.4
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class SellingRestrictionHierarchyLevelAuditKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "AUD_REC_CRE8_TS")
    private LocalDateTime changedOn;

    @Column(name = "str_dept_nbr")
    @Type(type="fixedLengthCharPK")
    private String department;

    @Column(name = "str_sub_dept_id")
    @Type(type="fixedLengthCharPK")
    private String subDepartment;

    @Column(name = "item_cls_code")
    private Integer itemClass;

    @Column(name = "pd_omi_com_cd")
    private Integer commodity;

    @Column(name = "pd_omi_sub_com_cd")
    private Integer subCommodity;

    @Column(name = "sals_rstr_grp_cd")
    @Type(type="fixedLengthCharPK")
    private String restrictionGroupCode;

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
     * Returns Department.
     *
     * @return The Department.
     **/
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the Department.
     *
     * @param department The Department.
     **/
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Returns SubDepartment.
     *
     * @return The SubDepartment.
     **/
    public String getSubDepartment() {
        return subDepartment;
    }

    /**
     * Sets the SubDepartment.
     *
     * @param subDepartment The SubDepartment.
     **/
    public void setSubDepartment(String subDepartment) {
        this.subDepartment = subDepartment;
    }

    /**
     * Returns ItemClass.
     *
     * @return The ItemClass.
     **/
    public Integer getItemClass() {
        return itemClass;
    }

    /**
     * Sets the ItemClass.
     *
     * @param itemClass The ItemClass.
     **/
    public void setItemClass(Integer itemClass) {
        this.itemClass = itemClass;
    }

    /**
     * Returns Commodity.
     *
     * @return The Commodity.
     **/
    public Integer getCommodity() {
        return commodity;
    }

    /**
     * Sets the Commodity.
     *
     * @param commodity The Commodity.
     **/
    public void setCommodity(Integer commodity) {
        this.commodity = commodity;
    }

    /**
     * Returns SubCommodity.
     *
     * @return The SubCommodity.
     **/
    public Integer getSubCommodity() {
        return subCommodity;
    }

    /**
     * Sets the SubCommodity.
     *
     * @param subCommodity The SubCommodity.
     **/
    public void setSubCommodity(Integer subCommodity) {
        this.subCommodity = subCommodity;
    }

    /**
     * Returns RestrictionGroupCode.
     *
     * @return The RestrictionGroupCode.
     **/
    public String getRestrictionGroupCode() {
        return restrictionGroupCode;
    }

    /**
     * Sets the RestrictionGroupCode.
     *
     * @param restrictionGroupCode The RestrictionGroupCode.
     **/
    public void setRestrictionGroupCode(String restrictionGroupCode) {
        this.restrictionGroupCode = restrictionGroupCode;
    }

    /**
     * Returns a printable representation of the object.
     *
     * @return A printable representation of the object.
     */
    @Override
    public String toString() {
        return "SellingRestrictionHierarchyLevelAuditKey{" +
                "changedOn=" + changedOn +
                "department='" + department + '\'' +
                ", subDepartment='" + subDepartment + '\'' +
                ", itemClass=" + itemClass +
                ", commodity=" + commodity +
                ", subCommodity=" + subCommodity +
                ", restrictionGroupCode='" + restrictionGroupCode + '\'' +
                '}';
    }
}
