/*
 * ChoiceType
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.heb.pm.dictionary.Vocabulary;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.*;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * Represents choice type in the code table.
 *
 * @author vn70516
 * @since 2.12.0
 */
@Entity
@Table(name = "choice_typ")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ChoiceType implements Serializable{

    private static final long serialVersionUID = 1L;
    /**
     * The constant DISPLAY_NAME_FORMAT.
     */
    private static final String DISPLAY_NAME_FORMAT = "%s [%s]";

    /**
     * The constand FILED_NAME_DEFAULT_SORT.
     */
    private static final String CHOICE_TYPE_SORT_FIELD = "choiceTypeCode";

    @Id
    @Column(name="chc_typ_cd")
    @Type(type="fixedLengthCharPK")
    private String choiceTypeCode;

    @Column(name="chc_typ_abb")
    private String abbreviation;

    @Column(name="chc_typ_des")
    private String description;

    @Column(name="chc_typ_alt_des")
    private String alternativeDescription;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parnt_chc_typ_cd", referencedColumnName = "chc_typ_cd", insertable = false, updatable = false)
    private ParentChoiceType parentChoiceType;


    @JsonIgnoreProperties("choiceType")
    @OneToMany(mappedBy = "choiceType",fetch = FetchType.LAZY)
    private List<ChoiceOption> choiceOptions;

    @JsonIgnoreProperties("choiceType")
    @OneToMany(mappedBy = "choiceType",fetch = FetchType.LAZY)
    private List<ProductGroupChoiceType> productGroupChoiceTypes;

    /**
     * @return Gets the value of choiceTypeCode and returns choiceTypeCode
     */
    public void setChoiceTypeCode(String choiceTypeCode) {
        this.choiceTypeCode = choiceTypeCode;
    }

    /**
     * Sets the choiceTypeCode
     */
    public String getChoiceTypeCode() {
        return choiceTypeCode;
    }

    /**
     * @return Gets the value of abbreviation and returns abbreviation
     */
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * Sets the abbreviation
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * @return Gets the value of description and returns description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Gets the value of alternativeDescription and returns alternativeDescription
     */
    public void setAlternativeDescription(String alternativeDescription) {
        this.alternativeDescription = alternativeDescription;
    }

    /**
     * Sets the alternativeDescription
     */
    public String getAlternativeDescription() {
        return alternativeDescription;
    }

    /**
     * @return Gets the value of parentChoiceType and returns parentChoiceType
     */
    public void setParentChoiceType(ParentChoiceType parentChoiceType) {
        this.parentChoiceType = parentChoiceType;
    }

    /**
     * Sets the parentChoiceType
     */
    public ParentChoiceType getParentChoiceType() {
        return parentChoiceType;
    }

    /**
     * Returns the choice type as it should be displayed on the GUI.
     *
     * @return A String representation of the choice type as it is meant to be displayed on the GUI.
     */
    public String getDisplayName() {
        return String.format(ChoiceType.DISPLAY_NAME_FORMAT, StringUtils.trim(this.description), StringUtils.trim(this.choiceTypeCode));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChoiceType)) return false;

        ChoiceType that = (ChoiceType) o;

        if (getChoiceTypeCode() != null ? !getChoiceTypeCode().equals(that.getChoiceTypeCode()) : that.getChoiceTypeCode() != null)
            return false;
        if (getAbbreviation() != null ? !getAbbreviation().equals(that.getAbbreviation()) : that.getAbbreviation() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        return getAlternativeDescription() != null ? getAlternativeDescription().equals(that.getAlternativeDescription()) : that.getAlternativeDescription() == null;
    }

    @Override
    public int hashCode() {
        int result = getChoiceTypeCode() != null ? getChoiceTypeCode().hashCode() : 0;
        result = 31 * result + (getAbbreviation() != null ? getAbbreviation().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getAlternativeDescription() != null ? getAlternativeDescription().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChoiceType{" +
                "choiceTypeCode='" + choiceTypeCode + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", description='" + description + '\'' +
                ", alternativeDescription='" + alternativeDescription + '\'' +
                '}';
    }

    /**
     * Returns the default sort order for the choice type table.
     *
     * @return The default sort order for the choice type table.
     */
    public static Sort getDefaultSort() {
        return  new Sort(
                new Sort.Order(Sort.Direction.ASC, ChoiceType.CHOICE_TYPE_SORT_FIELD)
        );
    }

    /**
     * Get the list of choiceOptions.
     *
     * @return the list choiceOptions
     */
    public List<ChoiceOption> getChoiceOptions() {
        return choiceOptions;
    }

    /**
     * Sets the list of choiceOptions.
     *
     * @param choiceOptions the list of choiceOptions.
     */
    public void setChoiceOptions(List<ChoiceOption> choiceOptions) {
        this.choiceOptions = choiceOptions;
    }

    /**
     * Get the list of productGroupChoiceTyp.
     *
     * @return the list productGroupChoiceTypes.
     */
    public List<ProductGroupChoiceType> getProductGroupChoiceTypes() {
        return productGroupChoiceTypes;
    }

    /**
     * Sets the list of productGroupChoiceTypes.
     *
     * @param productGroupChoiceTypes the list of productGroupChoiceTypes.
     */
    public void setProductGroupChoiceTypes(List<ProductGroupChoiceType> productGroupChoiceTypes) {
        this.productGroupChoiceTypes = productGroupChoiceTypes;
    }
}