/*
 * ProductPanelType
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents the product panel type.
 *
 * @author vn70516
 * @since 2.14.0
 */
@Entity
@Table(name = "prod_panel_typ")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class)
})
public class ProductPanelType implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_PANEL_SORT_FIELD = "panelTypeCode";

    @Id
    @Column(name = "prod_pan_typ_cd")
    private String panelTypeCode;

    @Column(name = "prod_pan_typ_abb")
    @Type(type = "fixedLengthChar")
    private String panelAbbreviation;

    @Column(name = "prod_pan_typ_des")
    @Type(type = "fixedLengthChar")
    private String panelDescription;

    /**
     * @return Gets the value of panelTypeCode and returns panelTypeCode
     */
    public void setPanelTypeCode(String panelTypeCode) {
        this.panelTypeCode = panelTypeCode;
    }

    /**
     * Sets the panelTypeCode
     */
    public String getPanelTypeCode() {
        return panelTypeCode;
    }

    /**
     * @return Gets the value of panelAbbreviation and returns panelAbbreviation
     */
    public void setPanelAbbreviation(String panelAbbreviation) {
        this.panelAbbreviation = panelAbbreviation;
    }

    /**
     * Sets the panelAbbreviation
     */
    public String getPanelAbbreviation() {
        return panelAbbreviation;
    }

    /**
     * @return Gets the value of panelDescription and returns panelDescription
     */
    public void setPanelDescription(String panelDescription) {
        this.panelDescription = panelDescription;
    }

    /**
     * Sets the panelDescription
     */
    public String getPanelDescription() {
        return panelDescription;
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
        if (!(o instanceof ProductPanelType)) return false;

        ProductPanelType that = (ProductPanelType) o;

        if (getPanelTypeCode() != null ? !getPanelTypeCode().equals(that.getPanelTypeCode()) : that.getPanelTypeCode() != null)
            return false;
        if (getPanelAbbreviation() != null ? !getPanelAbbreviation().equals(that.getPanelAbbreviation()) : that.getPanelAbbreviation() != null)
            return false;
        return getPanelDescription() != null ? getPanelDescription().equals(that.getPanelDescription()) : that.getPanelDescription() == null;
    }

    @Override
    public int hashCode() {
        int result = getPanelTypeCode() != null ? getPanelTypeCode().hashCode() : 0;
        result = 31 * result + (getPanelAbbreviation() != null ? getPanelAbbreviation().hashCode() : 0);
        result = 31 * result + (getPanelDescription() != null ? getPanelDescription().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductPanelType{" +
                "panelTypeCode=" + panelTypeCode +
                ", panelAbbreviation='" + panelAbbreviation + '\'' +
                ", panelDescription='" + panelDescription + '\'' +
                '}';
    }

    /**
     * Returns the default sort order for the product brand tier table.
     *
     * @return The default sort order for the product brand tier table.
     */
    public static Sort getDefaultSort() {
        return new Sort(
                new Sort.Order(Sort.Direction.ASC, ProductPanelType.DEFAULT_PANEL_SORT_FIELD)
        );
    }
}