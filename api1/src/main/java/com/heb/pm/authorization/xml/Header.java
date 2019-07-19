/*
 *  Header
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.authorization.xml;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProjectName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Source_Domain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Destination_Domain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Transaction_Type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TimeStamp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "projectName",
    "sourceDomain",
    "destinationDomain",
    "transactionType",
    "timeStamp"
})
@XmlRootElement(name = "Header", namespace = "http://xmlns.heb.com/ei/G3")
public class Header {

    @XmlElement(name = "ProjectName", namespace = "http://xmlns.heb.com/ei/G3", required = true)
    protected String projectName;
    @XmlElement(name = "Source_Domain", namespace = "http://xmlns.heb.com/ei/G3")
    protected String sourceDomain;
    @XmlElement(name = "Destination_Domain", namespace = "http://xmlns.heb.com/ei/G3")
    protected String destinationDomain;
    @XmlElement(name = "Transaction_Type", namespace = "http://xmlns.heb.com/ei/G3")
    protected String transactionType;
    @XmlElement(name = "TimeStamp", namespace = "http://xmlns.heb.com/ei/G3", required = true)
    protected String timeStamp;

    /**
     * Gets the value of the projectName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets the value of the projectName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProjectName(String value) {
        this.projectName = value;
    }

    /**
     * Gets the value of the sourceDomain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceDomain() {
        return sourceDomain;
    }

    /**
     * Sets the value of the sourceDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceDomain(String value) {
        this.sourceDomain = value;
    }

    /**
     * Gets the value of the destinationDomain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationDomain() {
        return destinationDomain;
    }

    /**
     * Sets the value of the destinationDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationDomain(String value) {
        this.destinationDomain = value;
    }

    /**
     * Gets the value of the transactionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * Sets the value of the transactionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionType(String value) {
        this.transactionType = value;
    }

    /**
     * Gets the value of the timeStamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the value of the timeStamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeStamp(String value) {
        this.timeStamp = value;
    }

}
