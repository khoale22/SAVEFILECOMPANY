/*
 *  Factory
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a Factory.
 *
 * @author s573181
 * @since 2.6.0
 */
@Entity
@Table(name = "fctry")
public class Factory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The constant DISPLAY_NAME_FORMAT.
     */
    private static final String DISPLAY_NAME_FORMAT = "%s [%s]";

    /**
     * The constand FILED_NAME_DEFAULT_SORT.
     */
    private static final String FACTORY_SORT_FIELD = "factoryId";

    @Id
    @Column(name = "fctry_id")
    private Integer factoryId;

    @Column(name = "fctry_nm")
    private String factoryName;

    @Column(name = "cntry_nm")
    private String country;

    @Column(name = "cty_txt")
    private String city;

    @Column(name = "fctry_stat_cd")
    private String status;

    @Column(name = "stret_one_txt")
    private String addressOne;

    @Column(name = "stret_two_txt")
    private String addressTwo;

    @Column(name = "stret_three_txt")
    private String addressThree;

    @Column(name = "st_nm")
    private String state;

    @Column(name = "cnty_txt")
    private String county;

    @Column(name = "zip")
    private String zip;

    @Column(name = "phn_nbr")
    private String phone;

    @Column(name = "fax_phn_nbr")
    private String fax;

    @Column(name = "fctry_contc_nm")
    private String contactName;

    @Column(name = "contc_email_adr")
    private String contactEmail;

    /**
     * Returns the factory id.
     *
     * @return the factory id.
     */
    public Integer getFactoryId() {
        return factoryId;
    }

    /**
     * Sets the factory id.
     *
     * @param factoryId the factory id.
     */
    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    /**
     * Returns the factory name.
     *
     * @return the factory name.
     */
    public String getFactoryName() {
        return factoryName;
    }

    /**
     * Sets the factory name.
     *
     * @param factoryName the factory name.
     */
    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    /**
     * Returns the country.
     *
     * @return the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country.
     *
     * @param country the country.
     */
    public void setCountry(String country) { this.country = country; }

    /**
     * Returns the city.
     *
     * @return the city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets  the city.
     *
     * @param city the city.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return Gets the value of status and returns status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return Gets the value of addressOne and returns addressOne
     */
    public void setAddressOne(String addressOne) {
        this.addressOne = addressOne;
    }

    /**
     * Sets the addressOne
     */
    public String getAddressOne() {
        return addressOne;
    }

    /**
     * @return Gets the value of addressTwo and returns addressTwo
     */
    public void setAddressTwo(String addressTwo) {
        this.addressTwo = addressTwo;
    }

    /**
     * Sets the addressTwo
     */
    public String getAddressTwo() {
        return addressTwo;
    }

    /**
     * @return Gets the value of addressThree and returns addressThree
     */
    public void setAddressThree(String addressThree) {
        this.addressThree = addressThree;
    }

    /**
     * Sets the addressThree
     */
    public String getAddressThree() {
        return addressThree;
    }

    /**
     * @return Gets the value of state and returns state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Sets the state
     */
    public String getState() {
        return state;
    }

    /**
     * @return Gets the value of county and returns county
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * Sets the county
     */
    public String getCounty() {
        return county;
    }

    /**
     * @return Gets the value of zip and returns zip
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * Sets the zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * @return Gets the value of phone and returns phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Sets the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @return Gets the value of fax and returns fax
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * Sets the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @return Gets the value of contactName and returns contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Sets the contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @return Gets the value of contactEmail and returns contactEmail
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * Sets the contactEmail
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Returns the choice type as it should be displayed on the GUI.
     *
     * @return A String representation of the choice type as it is meant to be displayed on the GUI.
     */
    public String getDisplayName() {
        return String.format(Factory.DISPLAY_NAME_FORMAT, StringUtils.trim(this.factoryName), this.factoryId);
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "Factory{" +
                "factoryId=" + factoryId +
                ", factoryName='" + factoryName + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    /**
     * Returns the default sort order for the factory table.
     *
     * @return The default sort order for the factory table.
     */
    public static Sort getDefaultSort() {
        return new Sort(
                new Sort.Order(Sort.Direction.ASC, Factory.FACTORY_SORT_FIELD)
        );
    }
}