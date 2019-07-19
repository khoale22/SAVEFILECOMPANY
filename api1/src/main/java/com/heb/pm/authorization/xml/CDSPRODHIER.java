/*
 *  CDSPRODHIER
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
 *         &lt;element name="DEPT_NBR" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="DEPT_NM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SUB_DEPT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SUB_DEPT_NM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITM_CLS_NBR" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="ITM_CLS_NM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IMS_COM_NBR" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="COM_NM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IMS_SUB_COM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SUB_COM_NM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OMI_COM_NBR" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="OMI_SUB_COM_NBR" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
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
    "deptnbr",
    "deptnm",
    "subdept",
    "subdeptnm",
    "itmclsnbr",
    "itmclsnm",
    "imscomnbr",
    "comnm",
    "imssubcom",
    "subcomnm",
    "omicomnbr",
    "omisubcomnbr"
})
@XmlRootElement(name = "CDS_PROD_HIER", namespace = "http://xmlns.heb.com/ei/CDS_PROD_HIER")
public class CDSPRODHIER {

    @XmlElement(name = "DEPT_NBR", namespace = "http://xmlns.heb.com/ei/CDS_PROD_HIER")
    protected BigDecimal deptnbr;
    @XmlElement(name = "DEPT_NM", namespace = "http://xmlns.heb.com/ei/CDS_PROD_HIER")
    protected String deptnm;
    @XmlElement(name = "SUB_DEPT", namespace = "http://xmlns.heb.com/ei/CDS_PROD_HIER")
    protected String subdept;
    @XmlElement(name = "SUB_DEPT_NM", namespace = "http://xmlns.heb.com/ei/CDS_PROD_HIER")
    protected String subdeptnm;
    @XmlElement(name = "ITM_CLS_NBR", namespace = "http://xmlns.heb.com/ei/CDS_PROD_HIER")
    protected BigDecimal itmclsnbr;
    @XmlElement(name = "ITM_CLS_NM", namespace = "http://xmlns.heb.com/ei/CDS_PROD_HIER")
    protected String itmclsnm;
    @XmlElement(name = "IMS_COM_NBR", namespace = "http://xmlns.heb.com/ei/CDS_PROD_HIER")
    protected BigDecimal imscomnbr;
    @XmlElement(name = "COM_NM", namespace = "http://xmlns.heb.com/ei/CDS_PROD_HIER")
    protected String comnm;
    @XmlElement(name = "IMS_SUB_COM", namespace = "http://xmlns.heb.com/ei/CDS_PROD_HIER")
    protected String imssubcom;
    @XmlElement(name = "SUB_COM_NM", namespace = "http://xmlns.heb.com/ei/CDS_PROD_HIER")
    protected String subcomnm;
    @XmlElement(name = "OMI_COM_NBR", namespace = "http://xmlns.heb.com/ei/CDS_PROD_HIER")
    protected BigDecimal omicomnbr;
    @XmlElement(name = "OMI_SUB_COM_NBR", namespace = "http://xmlns.heb.com/ei/CDS_PROD_HIER")
    protected BigDecimal omisubcomnbr;

    /**
     * Gets the value of the deptnbr property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDEPTNBR() {
        return deptnbr;
    }

    /**
     * Sets the value of the deptnbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDEPTNBR(BigDecimal value) {
        this.deptnbr = value;
    }

    /**
     * Gets the value of the deptnm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDEPTNM() {
        return deptnm;
    }

    /**
     * Sets the value of the deptnm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDEPTNM(String value) {
        this.deptnm = value;
    }

    /**
     * Gets the value of the subdept property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUBDEPT() {
        return subdept;
    }

    /**
     * Sets the value of the subdept property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUBDEPT(String value) {
        this.subdept = value;
    }

    /**
     * Gets the value of the subdeptnm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUBDEPTNM() {
        return subdeptnm;
    }

    /**
     * Sets the value of the subdeptnm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUBDEPTNM(String value) {
        this.subdeptnm = value;
    }

    /**
     * Gets the value of the itmclsnbr property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getITMCLSNBR() {
        return itmclsnbr;
    }

    /**
     * Sets the value of the itmclsnbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setITMCLSNBR(BigDecimal value) {
        this.itmclsnbr = value;
    }

    /**
     * Gets the value of the itmclsnm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITMCLSNM() {
        return itmclsnm;
    }

    /**
     * Sets the value of the itmclsnm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITMCLSNM(String value) {
        this.itmclsnm = value;
    }

    /**
     * Gets the value of the imscomnbr property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIMSCOMNBR() {
        return imscomnbr;
    }

    /**
     * Sets the value of the imscomnbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIMSCOMNBR(BigDecimal value) {
        this.imscomnbr = value;
    }

    /**
     * Gets the value of the comnm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMNM() {
        return comnm;
    }

    /**
     * Sets the value of the comnm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMNM(String value) {
        this.comnm = value;
    }

    /**
     * Gets the value of the imssubcom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIMSSUBCOM() {
        return imssubcom;
    }

    /**
     * Sets the value of the imssubcom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIMSSUBCOM(String value) {
        this.imssubcom = value;
    }

    /**
     * Gets the value of the subcomnm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUBCOMNM() {
        return subcomnm;
    }

    /**
     * Sets the value of the subcomnm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUBCOMNM(String value) {
        this.subcomnm = value;
    }

    /**
     * Gets the value of the omicomnbr property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOMICOMNBR() {
        return omicomnbr;
    }

    /**
     * Sets the value of the omicomnbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOMICOMNBR(BigDecimal value) {
        this.omicomnbr = value;
    }

    /**
     * Gets the value of the omisubcomnbr property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOMISUBCOMNBR() {
        return omisubcomnbr;
    }

    /**
     * Sets the value of the omisubcomnbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOMISUBCOMNBR(BigDecimal value) {
        this.omisubcomnbr = value;
    }

}
