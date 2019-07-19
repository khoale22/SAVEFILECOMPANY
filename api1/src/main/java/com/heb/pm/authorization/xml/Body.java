/*
 *  Body
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
 *         &lt;element ref="{http://xmlns.heb.com/ei/G3}G3_HOST_LAYOUT"/>
 *         &lt;element ref="{http://xmlns.heb.com/ei/G3}Fault" minOccurs="0"/>
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
    "g3HOSTLAYOUT"
})
@XmlRootElement(name = "Body", namespace = "http://xmlns.heb.com/ei/G3")
public class Body {

    @XmlElement(name = "G3_HOST_LAYOUT", namespace = "http://xmlns.heb.com/ei/G3", required = true)
    protected G3HOSTLAYOUT g3HOSTLAYOUT;

    /**
     * Gets the value of the g3HOSTLAYOUT property.
     * 
     * @return
     *     possible object is
     *     {@link G3HOSTLAYOUT }
     *     
     */
    public G3HOSTLAYOUT getG3HOSTLAYOUT() {
        return g3HOSTLAYOUT;
    }

    /**
     * Sets the value of the g3HOSTLAYOUT property.
     * 
     * @param value
     *     allowed object is
     *     {@link G3HOSTLAYOUT }
     *     
     */
    public void setG3HOSTLAYOUT(G3HOSTLAYOUT value) {
        this.g3HOSTLAYOUT = value;
    }
}
