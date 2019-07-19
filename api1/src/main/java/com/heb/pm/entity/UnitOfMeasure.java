/*
 * UnitOfMeasure
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import java.io.Serializable;

/**
 * Represents a nutrients unit of measure data.
 * @author vn70633
 * @since 2.20.0
 */
@Entity
@Table(name = "uom")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class UnitOfMeasure implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DISPLAY_NAME_FORMAT = "[%d]%s";

    @Id
    @Column(name = "uom_id")
    private Long uomCode;

    @Column(name = "uom_des")
    private String uomDescription;

    @Column(name = "uom_dsply_nm")
    @Type(type="fixedLengthCharPK")
    private String uomDisplayName;

    @Column(name = "met_or_imprl_uom_sw")
    private String metricOrImperialUomSwitch;

    /**
     * Gets nutrient uom description.
     *
     * @return the nutrient uom description
     */
    public String getUomDisplayName() {
        return uomDisplayName;
    }

    /**
     * Sets nutrient uom description.
     *
     * @param uomDisplayName the nutrient uom description
     */
    public void setUomDisplayName(String uomDisplayName) {
        this.uomDisplayName = uomDisplayName;
    }

    /**
     * Returns the unique ID for this code.
     *
     * @return The unique ID for this code.
     */
    public Long getUomCode() {
        return uomCode;
    }

    /**
     * Sets the unique ID for this code.
     *
     * @param uomCode The unique ID for this code.
     */
    public void setUomCode(Long uomCode) {
        this.uomCode = uomCode;
    }

    /**
     * Get display name string.
     *
     * @return the string
     */
    public String getDisplayUOM(){
        return String.format(UnitOfMeasure.DISPLAY_NAME_FORMAT, this.uomCode,
                this.uomDisplayName.trim());
    }

    /**
     * Get uom description.
     *
     * @return the string
     */
    public String getUomDescription() {
        return uomDescription;
    }

    /**
     * Sets the uomDescription.
     *
     * @param uomDescription The uom description.
     */
    public void setUomDescription(String uomDescription) {
        this.uomDescription=uomDescription;
    }

    /**
     * Get metricOrImperialUomSwitch.
     *
     * @return the metricOrImperialUomSwitch.
     */
    public String getMetricOrImperialUomSwitch() {
        return metricOrImperialUomSwitch;
    }

    /**
     * Sets the metricOrImperialUomSwitch.
     *
     * @param metricOrImperialUomSwitch The metricOrImperialUomSwitch.
     */
    public void setMetricOrImperialUomSwitch(String metricOrImperialUomSwitch) {
        this.metricOrImperialUomSwitch=metricOrImperialUomSwitch;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "UnitOfMeasure{" +
                "uomCode=" + uomCode +
                "uomDescription=" + uomDescription +
                ", uomDisplayName=" + uomDisplayName +
                ", metricOrImperialUomSwitch=" + metricOrImperialUomSwitch +
                '}';
    }
}

