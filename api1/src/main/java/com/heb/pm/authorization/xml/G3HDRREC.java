/*
 *  G3HDRREC
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
 *         &lt;element name="G3_HDR_REC_CD" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="HD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="HDR_APLY_TYP_CD" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="HDR_RSULT_PTR_CD" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="HDR_DES" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="HDR_BAT_STR" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *                   &lt;element name="HDR_BAT_DT" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="HDR_CRE8_DT" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="HDR_CRE8_TM" type="{http://www.w3.org/2001/XMLSchema}time" minOccurs="0"/>
 *         &lt;element name="HDR_APLY_DT" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="HDR_APLY_TM" type="{http://www.w3.org/2001/XMLSchema}time" minOccurs="0"/>
 *         &lt;element name="HDR_STR_NBR" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
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
    "g3HDRRECCD",
    "hdraplytypcd",
    "hdrrsultptrcd",
    "hdrdes",
    "hdrcre8DT",
    "hdrcre8TM",
    "hdraplydt",
    "hdraplytm",
    "hdrstrnbr"
})
@XmlRootElement(name = "G3_HDR_REC", namespace = "http://xmlns.heb.com/ei/G3")
public class G3HDRREC {

    @XmlElement(name = "G3_HDR_REC_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected G3HDRRECCD g3HDRRECCD;
    @XmlElement(name = "HDR_APLY_TYP_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger hdraplytypcd;
    @XmlElement(name = "HDR_RSULT_PTR_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger hdrrsultptrcd;
    @XmlElement(name = "HDR_DES", namespace = "http://xmlns.heb.com/ei/G3")
    protected HDRDES hdrdes;
    @XmlElement(name = "HDR_CRE8_DT", namespace = "http://xmlns.heb.com/ei/G3")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar hdrcre8DT;
    @XmlElement(name = "HDR_CRE8_TM", namespace = "http://xmlns.heb.com/ei/G3")
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar hdrcre8TM;
    @XmlElement(name = "HDR_APLY_DT", namespace = "http://xmlns.heb.com/ei/G3")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar hdraplydt;
    @XmlElement(name = "HDR_APLY_TM", namespace = "http://xmlns.heb.com/ei/G3")
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar hdraplytm;
    @XmlElement(name = "HDR_STR_NBR", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger hdrstrnbr;

    /**
     * Gets the value of the g3HDRRECCD property.
     * 
     * @return
     *     possible object is
     *     {@link G3HDRRECCD }
     *     
     */
    public G3HDRRECCD getG3HDRRECCD() {
        return g3HDRRECCD;
    }

    /**
     * Sets the value of the g3HDRRECCD property.
     * 
     * @param value
     *     allowed object is
     *     {@link G3HDRRECCD }
     *     
     */
    public void setG3HDRRECCD(G3HDRRECCD value) {
        this.g3HDRRECCD = value;
    }

    /**
     * Gets the value of the hdraplytypcd property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHDRAPLYTYPCD() {
        return hdraplytypcd;
    }

    /**
     * Sets the value of the hdraplytypcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHDRAPLYTYPCD(BigInteger value) {
        this.hdraplytypcd = value;
    }

    /**
     * Gets the value of the hdrrsultptrcd property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHDRRSULTPTRCD() {
        return hdrrsultptrcd;
    }

    /**
     * Sets the value of the hdrrsultptrcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHDRRSULTPTRCD(BigInteger value) {
        this.hdrrsultptrcd = value;
    }

    /**
     * Gets the value of the hdrdes property.
     * 
     * @return
     *     possible object is
     *     {@link HDRDES }
     *     
     */
    public HDRDES getHDRDES() {
        return hdrdes;
    }

    /**
     * Sets the value of the hdrdes property.
     * 
     * @param value
     *     allowed object is
     *     {@link HDRDES }
     *     
     */
    public void setHDRDES(HDRDES value) {
        this.hdrdes = value;
    }

    /**
     * Gets the value of the hdrcre8DT property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getHDRCRE8DT() {
        return hdrcre8DT;
    }

    /**
     * Sets the value of the hdrcre8DT property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setHDRCRE8DT(XMLGregorianCalendar value) {
        this.hdrcre8DT = value;
    }

    /**
     * Gets the value of the hdrcre8TM property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getHDRCRE8TM() {
        return hdrcre8TM;
    }

    /**
     * Sets the value of the hdrcre8TM property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setHDRCRE8TM(XMLGregorianCalendar value) {
        this.hdrcre8TM = value;
    }

    /**
     * Gets the value of the hdraplydt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getHDRAPLYDT() {
        return hdraplydt;
    }

    /**
     * Sets the value of the hdraplydt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setHDRAPLYDT(XMLGregorianCalendar value) {
        this.hdraplydt = value;
    }

    /**
     * Gets the value of the hdraplytm property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getHDRAPLYTM() {
        return hdraplytm;
    }

    /**
     * Sets the value of the hdraplytm property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setHDRAPLYTM(XMLGregorianCalendar value) {
        this.hdraplytm = value;
    }

    /**
     * Gets the value of the hdrstrnbr property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHDRSTRNBR() {
        return hdrstrnbr;
    }

    /**
     * Sets the value of the hdrstrnbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHDRSTRNBR(BigInteger value) {
        this.hdrstrnbr = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="HD" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "hd"
    })
    public static class G3HDRRECCD {

        @XmlElement(name = "HD", namespace = "http://xmlns.heb.com/ei/G3")
        protected String hd;

        /**
         * Gets the value of the hd property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHD() {
            return hd;
        }

        /**
         * Sets the value of the hd property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHD(String value) {
            this.hd = value;
        }

    }


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
     *         &lt;element name="HDR_BAT_STR" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
     *         &lt;element name="HDR_BAT_DT" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
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
        "hdrbatstr",
        "hdrbatdt"
    })
    public static class HDRDES {

        @XmlElement(name = "HDR_BAT_STR", namespace = "http://xmlns.heb.com/ei/G3")
        protected BigInteger hdrbatstr;
        @XmlElement(name = "HDR_BAT_DT", namespace = "http://xmlns.heb.com/ei/G3")
        protected BigInteger hdrbatdt;

        /**
         * Gets the value of the hdrbatstr property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getHDRBATSTR() {
            return hdrbatstr;
        }

        /**
         * Sets the value of the hdrbatstr property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setHDRBATSTR(BigInteger value) {
            this.hdrbatstr = value;
        }

        /**
         * Gets the value of the hdrbatdt property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getHDRBATDT() {
            return hdrbatdt;
        }

        /**
         * Sets the value of the hdrbatdt property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setHDRBATDT(BigInteger value) {
            this.hdrbatdt = value;
        }

    }

}
