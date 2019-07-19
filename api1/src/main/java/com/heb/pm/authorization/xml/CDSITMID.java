/*
 *  CDSITMID
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.authorization.xml;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;


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
 *         &lt;element name="RETL_UNT_GTIN" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="PROD_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="HEB_ITM_CD" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="CS_PK_GTIN" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="MFG_CS_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NDC_CD" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="USDA_PROD_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
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
    "retluntgtin",
    "prodid",
    "hebitmcd",
    "cspkgtin",
    "mfgcsid",
    "ndccd",
    "usdaprodid"
})
@XmlRootElement(name = "CDS_ITM_ID", namespace = "http://xmlns.heb.com/ei/CDS_ITM_ID")
public class CDSITMID {

    @XmlElement(name = "RETL_UNT_GTIN", namespace = "http://xmlns.heb.com/ei/CDS_ITM_ID")
    protected BigDecimal retluntgtin;
    @XmlElement(name = "PROD_ID", namespace = "http://xmlns.heb.com/ei/CDS_ITM_ID")
    protected BigDecimal prodid;
    @XmlElement(name = "HEB_ITM_CD", namespace = "http://xmlns.heb.com/ei/CDS_ITM_ID")
    protected BigDecimal hebitmcd;
    @XmlElement(name = "CS_PK_GTIN", namespace = "http://xmlns.heb.com/ei/CDS_ITM_ID")
    protected BigDecimal cspkgtin;
    @XmlElement(name = "MFG_CS_ID", namespace = "http://xmlns.heb.com/ei/CDS_ITM_ID")
    protected String mfgcsid;
    @XmlElement(name = "NDC_CD", namespace = "http://xmlns.heb.com/ei/CDS_ITM_ID")
    protected BigDecimal ndccd;
    @XmlElement(name = "USDA_PROD_ID", namespace = "http://xmlns.heb.com/ei/CDS_ITM_ID")
    protected BigDecimal usdaprodid;

    /**
     * Gets the value of the retluntgtin property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRETLUNTGTIN() {
        return retluntgtin;
    }

    /**
     * Sets the value of the retluntgtin property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRETLUNTGTIN(BigDecimal value) {
        this.retluntgtin = value;
    }

    /**
     * Gets the value of the prodid property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPRODID() {
        return prodid;
    }

    /**
     * Sets the value of the prodid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPRODID(BigDecimal value) {
        this.prodid = value;
    }

    /**
     * Gets the value of the hebitmcd property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getHEBITMCD() {
        return hebitmcd;
    }

    /**
     * Sets the value of the hebitmcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setHEBITMCD(BigDecimal value) {
        this.hebitmcd = value;
    }

    /**
     * Gets the value of the cspkgtin property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCSPKGTIN() {
        return cspkgtin;
    }

    /**
     * Sets the value of the cspkgtin property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCSPKGTIN(BigDecimal value) {
        this.cspkgtin = value;
    }

    /**
     * Gets the value of the mfgcsid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMFGCSID() {
        return mfgcsid;
    }

    /**
     * Sets the value of the mfgcsid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMFGCSID(String value) {
        this.mfgcsid = value;
    }

    /**
     * Gets the value of the ndccd property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNDCCD() {
        return ndccd;
    }

    /**
     * Sets the value of the ndccd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNDCCD(BigDecimal value) {
        this.ndccd = value;
    }

    /**
     * Gets the value of the usdaprodid property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUSDAPRODID() {
        return usdaprodid;
    }

    /**
     * Sets the value of the usdaprodid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUSDAPRODID(BigDecimal value) {
        this.usdaprodid = value;
    }

}
