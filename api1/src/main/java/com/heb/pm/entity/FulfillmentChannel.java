/*
 *  FulfillmentChannel
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * The persistent class for the FLFL_CHNL database table.
 *
 * @author vn70516
 * @since 2.14.0
 */
@Entity
@Table(name = "flfl_chnl")
public class FulfillmentChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DISPLAY_NAME_FORMAT = "%s [%s]";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
    @EmbeddedId
    private FulfillmentChannelKey key;

    @Column(name = "flfl_chnl_abb")
    private String abbreviation;

    @Column(name = "flfl_chnl_des")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="sals_chnl_cd", referencedColumnName = "sals_chnl_cd", insertable = false, updatable = false)
    private SalesChannel salesChannel;

    @Transient
	@JsonFormat(
			pattern=DATE_FORMAT,
			shape=JsonFormat.Shape.STRING)
	private LocalDate expirationDate;

	@Transient
	@JsonFormat(
			pattern=DATE_FORMAT,
			shape=JsonFormat.Shape.STRING)
	private LocalDate effectiveDate;

	/**
	 * Return the expiration date.
	 * @return
	 */
	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	/**
	 * Set the expiration date.
	 * @param expirationDate
	 */
	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * Get effective date.
	 * @return
	 */
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * Set effective date.
	 * @param effectiveDate
	 */
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

    /**
     * @return Gets the value of key and returns key
     */
    public void setKey(FulfillmentChannelKey key) {
        this.key = key;
    }

    /**
     * Sets the key
     */
    public FulfillmentChannelKey getKey() {
        return key;
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
     * Returns the sales channel this fulfilment channel is tied to.
     *
     * @return The sales channel this fulfilment channel is tied to.
     */
    public SalesChannel getSalesChannel() {
        return salesChannel;
    }

    /**
     * Sets the sales channel this fulfilment channel is tied to.
     *
     * @param salesChannel The sales channel this fulfilment channel is tied to.
     */
    public void setSalesChannel(SalesChannel salesChannel) {
        this.salesChannel = salesChannel;
    }

    /**
     * Returns a string to display this fulfilment channel on the GUI.
     *
     * @return A string to display this fulfilment channel on the GUI.
     */
    public String getDisplayName() {
        return String.format(FulfillmentChannel.DISPLAY_NAME_FORMAT,
                this.description, this.salesChannel.getDescription());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FulfillmentChannel)) return false;

        FulfillmentChannel that = (FulfillmentChannel) o;

        if (getKey() != null ? !getKey().equals(that.getKey()) : that.getKey() != null) return false;
        if (getAbbreviation() != null ? !getAbbreviation().equals(that.getAbbreviation()) : that.getAbbreviation() != null)
            return false;
        return getDescription() != null ? getDescription().equals(that.getDescription()) : that.getDescription() == null;
    }

    @Override
    public int hashCode() {
        int result = getKey() != null ? getKey().hashCode() : 0;
        result = 31 * result + (getAbbreviation() != null ? getAbbreviation().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FulfillmentChannel{" +
                "key=" + key +
                ", abbreviation='" + abbreviation + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    /**
     * Returns a default sort for this object.
     *
     * @return A default sort for this object.
     */
    public static Sort getDefaultSort() {
        return new Sort(
                new Sort.Order(Sort.Direction.ASC, "key.salesChannelCode"),
                new Sort.Order(Sort.Direction.ASC, "key.fulfillmentChannelCode")
        );
    }
}