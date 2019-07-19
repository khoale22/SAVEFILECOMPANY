/*
 * ChoiceOption
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * Represents choice option in the code table.
 *
 * @author vn70516
 * @since 2.12.0
 */
@Entity
@Table(name = "choice_opt")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ChoiceOption implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int PRIME_NUMBER = 31;

    /**
     * The constant FILED_NAME_DEFAULT_SORT.
     */
    private static final String CHOICE_OPTION_SORT_FIELD = "key.choiceOptionCode";

    @EmbeddedId
    private ChoiceOptionKey key;

    @Column(name = "prod_chc_txt")
    @Type(type = "fixedLengthChar")
    private String productChoiceText;

    @Column(name = "seq_nbr")
    private Integer sequence;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chc_typ_cd", referencedColumnName = "chc_typ_cd", insertable = false, updatable = false)
    private ChoiceType choiceType;

    @Column(name="chc_opt_cd", insertable = false, updatable = false)
    @Type(type="fixedLengthCharPK")
    private String choiceOptionCode;

	/**
	 * image meta data for choice option
	 */
	@Transient
	private ImageMetaData imageMetaDataChoiceOption;

	/**
	 * get image meta data
	 * @return
	 */
	public ImageMetaData getImageMetaDataChoiceOption() {
		return imageMetaDataChoiceOption;
	}

	/**
	 * set image meta data
	 * @param imageMetaDataChoiceOption
	 */
	public void setImageMetaDataChoiceOption(ImageMetaData imageMetaDataChoiceOption) {
		this.imageMetaDataChoiceOption = imageMetaDataChoiceOption;
	}

    /**
     * @return Gets the value of key and returns key
     */
    public void setKey(ChoiceOptionKey key) {
        this.key = key;
    }

    /**
     * Sets the key
     */
    public ChoiceOptionKey getKey() {
        return key;
    }

    /**
     * @return Gets the value of productChoiceText and returns productChoiceText
     */
    public void setProductChoiceText(String productChoiceText) {
        this.productChoiceText = productChoiceText;
    }

    /**
     * Sets the productChoiceText
     */
    public String getProductChoiceText() {
        return productChoiceText;
    }

    /**
     * @return Gets the value of sequence and returns sequence
     */
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    /**
     * Sets the sequence
     */
    public Integer getSequence() {
        return sequence;
    }

    /**
     * @return Gets the value of choiceType and returns choiceType
     */
    public void setChoiceType(ChoiceType choiceType) {
        this.choiceType = choiceType;
    }

    /**
     * Sets the choiceType
     */
    public ChoiceType getChoiceType() {
        return choiceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChoiceOption)) return false;

        ChoiceOption that = (ChoiceOption) o;

        if (getKey() != null ? !getKey().equals(that.getKey()) : that.getKey() != null) return false;
        if (getProductChoiceText() != null ? !getProductChoiceText().equals(that.getProductChoiceText()) : that.getProductChoiceText() != null)
            return false;
        return getSequence() != null ? getSequence().equals(that.getSequence()) : that.getSequence() == null;
    }

    @Override
    public int hashCode() {
        int result = getKey() != null ? getKey().hashCode() : 0;
        result = PRIME_NUMBER * result + (getProductChoiceText() != null ? getProductChoiceText().hashCode() : 0);
        result = PRIME_NUMBER * result + (getSequence() != null ? getSequence().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChoiceOption{" +
                "key=" + key +
                ", productChoiceText='" + productChoiceText + '\'' +
                ", sequence=" + sequence +
                '}';
    }

    /**
     * Returns the default sort order for the choice option table.
     *
     * @return The default sort order for the choice option table.
     */
    public static org.springframework.data.domain.Sort getDefaultSort() {
        return new org.springframework.data.domain.Sort(new org.springframework.data.domain.Sort.Order(org
                .springframework.data.domain.Sort.Direction.ASC, ChoiceOption.CHOICE_OPTION_SORT_FIELD)
        );
    }
}