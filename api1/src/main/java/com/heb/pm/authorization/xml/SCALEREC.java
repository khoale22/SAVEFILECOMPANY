/*
 *  SCALEREC
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
 *         &lt;element ref="{http://xmlns.heb.com/ei/CDS_SCALE_REC}SCALE_HDR_REC" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.heb.com/ei/CDS_SCALE_REC}SCALE_DTL_REC" minOccurs="0"/>
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
    "scalehdrrec",
    "scaledtlrec"
})
@XmlRootElement(name = "SCALE_REC", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
public class SCALEREC {

    @XmlElement(name = "SCALE_HDR_REC", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected SCALEHDRREC scalehdrrec;
    @XmlElement(name = "SCALE_DTL_REC", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected SCALEDTLREC scaledtlrec;

    /**
     * Gets the value of the scalehdrrec property.
     * 
     * @return
     *     possible object is
     *     {@link SCALEHDRREC }
     *     
     */
    public SCALEHDRREC getSCALEHDRREC() {
        return scalehdrrec;
    }

    /**
     * Sets the value of the scalehdrrec property.
     * 
     * @param value
     *     allowed object is
     *     {@link SCALEHDRREC }
     *     
     */
    public void setSCALEHDRREC(SCALEHDRREC value) {
        this.scalehdrrec = value;
    }

    /**
     * Gets the value of the scaledtlrec property.
     * 
     * @return
     *     possible object is
     *     {@link SCALEDTLREC }
     *     
     */
    public SCALEDTLREC getSCALEDTLREC() {
        return scaledtlrec;
    }

    /**
     * Sets the value of the scaledtlrec property.
     * 
     * @param value
     *     allowed object is
     *     {@link SCALEDTLREC }
     *     
     */
    public void setSCALEDTLREC(SCALEDTLREC value) {
        this.scaledtlrec = value;
    }

}
