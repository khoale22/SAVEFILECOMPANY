/*
 *  SCALEHDRREC
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.authorization.xml;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;


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
 *         &lt;element name="BATCH_NAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BATCH_NUMBER" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="BATCH_DATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="BATCH_TIME" type="{http://www.w3.org/2001/XMLSchema}time" minOccurs="0"/>
 *         &lt;element name="ITEM_COUNT" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="BATCH_EFFECTIVE_DATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="BATCH_EFFECTIVE_TIME" type="{http://www.w3.org/2001/XMLSchema}time" minOccurs="0"/>
 *         &lt;element name="BATCH_TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="REGION_NUMBER" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="STORE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
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
    "batchname",
    "batchnumber",
    "batchdate",
    "batchtime",
    "itemcount",
    "batcheffectivedate",
    "batcheffectivetime",
    "batchtype",
    "regionnumber",
    "storenumber"
})
@XmlRootElement(name = "SCALE_HDR_REC", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
public class SCALEHDRREC {

    @XmlElement(name = "BATCH_NAME", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String batchname;
    @XmlElement(name = "BATCH_NUMBER", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected BigInteger batchnumber;
    @XmlElement(name = "BATCH_DATE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar batchdate;
    @XmlElement(name = "BATCH_TIME", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar batchtime;
    @XmlElement(name = "ITEM_COUNT", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected BigInteger itemcount;
    @XmlElement(name = "BATCH_EFFECTIVE_DATE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar batcheffectivedate;
    @XmlElement(name = "BATCH_EFFECTIVE_TIME", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar batcheffectivetime;
    @XmlElement(name = "BATCH_TYPE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String batchtype;
    @XmlElement(name = "REGION_NUMBER", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected BigInteger regionnumber;
    @XmlElement(name = "STORE_NUMBER", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected BigInteger storenumber;

    /**
     * Gets the value of the batchname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBATCHNAME() {
        return batchname;
    }

    /**
     * Sets the value of the batchname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBATCHNAME(String value) {
        this.batchname = value;
    }

    /**
     * Gets the value of the batchnumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBATCHNUMBER() {
        return batchnumber;
    }

    /**
     * Sets the value of the batchnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBATCHNUMBER(BigInteger value) {
        this.batchnumber = value;
    }

    /**
     * Gets the value of the batchdate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBATCHDATE() {
        return batchdate;
    }

    /**
     * Sets the value of the batchdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBATCHDATE(XMLGregorianCalendar value) {
        this.batchdate = value;
    }

    /**
     * Gets the value of the batchtime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBATCHTIME() {
        return batchtime;
    }

    /**
     * Sets the value of the batchtime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBATCHTIME(XMLGregorianCalendar value) {
        this.batchtime = value;
    }

    /**
     * Gets the value of the itemcount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getITEMCOUNT() {
        return itemcount;
    }

    /**
     * Sets the value of the itemcount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setITEMCOUNT(BigInteger value) {
        this.itemcount = value;
    }

    /**
     * Gets the value of the batcheffectivedate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBATCHEFFECTIVEDATE() {
        return batcheffectivedate;
    }

    /**
     * Sets the value of the batcheffectivedate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBATCHEFFECTIVEDATE(XMLGregorianCalendar value) {
        this.batcheffectivedate = value;
    }

    /**
     * Gets the value of the batcheffectivetime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBATCHEFFECTIVETIME() {
        return batcheffectivetime;
    }

    /**
     * Sets the value of the batcheffectivetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBATCHEFFECTIVETIME(XMLGregorianCalendar value) {
        this.batcheffectivetime = value;
    }

    /**
     * Gets the value of the batchtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBATCHTYPE() {
        return batchtype;
    }

    /**
     * Sets the value of the batchtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBATCHTYPE(String value) {
        this.batchtype = value;
    }

    /**
     * Gets the value of the regionnumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getREGIONNUMBER() {
        return regionnumber;
    }

    /**
     * Sets the value of the regionnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setREGIONNUMBER(BigInteger value) {
        this.regionnumber = value;
    }

    /**
     * Gets the value of the storenumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSTORENUMBER() {
        return storenumber;
    }

    /**
     * Sets the value of the storenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSTORENUMBER(BigInteger value) {
        this.storenumber = value;
    }

}
