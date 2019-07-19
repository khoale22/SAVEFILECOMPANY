/*
 *  G3SPLRPACKREC
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.authorization.xml;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
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
 *         &lt;element name="G3_SPLR_PK_REC_CD" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="SP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CRUD_CD" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="1"/>
 *               &lt;maxInclusive value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SPLR_PK_RSULT_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SPLR_PK_ID" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://xmlns.heb.com/ei/CDS_ITM_ID}CDS_ITM_ID"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="SPLR_ITM_VNDR_NUM" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="SPLR_PK_SKU_ID" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="SPLR_PK_TYP_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SPLR_PK_QTY" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="SPLR_PK_ORD_CD" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="SPLR_ITM_VNDR_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="SPLR_PK_PRIM_SW" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="SPLR_PK_RNDM_WT_SW" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="SPLR_PK_RECV_AUTH_SW" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="SPLR_PK_CST" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="SPLR_PK_STR_NBR" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
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
    "g3SPLRPKRECCD",
    "crudcd",
    "splrpkrsultcd",
    "splrpkid",
    "splritmvndrnum",
    "splrpkskuid",
    "splrpktypcd",
    "splrpkqty",
    "splrpkordcd",
    "splrpkprimsw",
    "splrpkrndmwtsw",
    "splrpkrecvauthsw",
    "splrpkcst",
    "splrpkstrnbr"
})
@XmlRootElement(name = "G3_SPLR_PACK_REC", namespace = "http://xmlns.heb.com/ei/G3")
public class G3SPLRPACKREC {

    @XmlElement(name = "G3_SPLR_PK_REC_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected G3SPLRPKRECCD g3SPLRPKRECCD;
    @XmlElement(name = "CRUD_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected Integer crudcd;
    @XmlElement(name = "SPLR_PK_RSULT_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String splrpkrsultcd;
    @XmlElement(name = "SPLR_PK_ID", namespace = "http://xmlns.heb.com/ei/G3")
    protected SPLRPKID splrpkid;
    @XmlElement(name = "SPLR_ITM_VNDR_NUM", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger splritmvndrnum;
    @XmlElement(name = "SPLR_PK_SKU_ID", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger splrpkskuid;
    @XmlElement(name = "SPLR_PK_TYP_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String splrpktypcd;
    @XmlElement(name = "SPLR_PK_QTY", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger splrpkqty;
    @XmlElement(name = "SPLR_PK_ORD_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected SPLRPKORDCD splrpkordcd;
    @XmlElement(name = "SPLR_PK_PRIM_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger splrpkprimsw;
    @XmlElement(name = "SPLR_PK_RNDM_WT_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger splrpkrndmwtsw;
    @XmlElement(name = "SPLR_PK_RECV_AUTH_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger splrpkrecvauthsw;
    @XmlElement(name = "SPLR_PK_CST", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigDecimal splrpkcst;
    @XmlElement(name = "SPLR_PK_STR_NBR", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger splrpkstrnbr;

    /**
     * Gets the value of the g3SPLRPKRECCD property.
     * 
     * @return
     *     possible object is
     *     {@link G3SPLRPKRECCD }
     *     
     */
    public G3SPLRPKRECCD getG3SPLRPKRECCD() {
        return g3SPLRPKRECCD;
    }

    /**
     * Sets the value of the g3SPLRPKRECCD property.
     * 
     * @param value
     *     allowed object is
     *     {@link G3SPLRPKRECCD }
     *     
     */
    public void setG3SPLRPKRECCD(G3SPLRPKRECCD value) {
        this.g3SPLRPKRECCD = value;
    }

    /**
     * Gets the value of the crudcd property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCRUDCD() {
        return crudcd;
    }

    /**
     * Sets the value of the crudcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCRUDCD(Integer value) {
        this.crudcd = value;
    }

    /**
     * Gets the value of the splrpkrsultcd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPLRPKRSULTCD() {
        return splrpkrsultcd;
    }

    /**
     * Sets the value of the splrpkrsultcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPLRPKRSULTCD(String value) {
        this.splrpkrsultcd = value;
    }

    /**
     * Gets the value of the splrpkid property.
     * 
     * @return
     *     possible object is
     *     {@link SPLRPKID }
     *     
     */
    public SPLRPKID getSPLRPKID() {
        return splrpkid;
    }

    /**
     * Sets the value of the splrpkid property.
     * 
     * @param value
     *     allowed object is
     *     {@link SPLRPKID }
     *     
     */
    public void setSPLRPKID(SPLRPKID value) {
        this.splrpkid = value;
    }

    /**
     * Gets the value of the splritmvndrnum property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSPLRITMVNDRNUM() {
        return splritmvndrnum;
    }

    /**
     * Sets the value of the splritmvndrnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSPLRITMVNDRNUM(BigInteger value) {
        this.splritmvndrnum = value;
    }

    /**
     * Gets the value of the splrpkskuid property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSPLRPKSKUID() {
        return splrpkskuid;
    }

    /**
     * Sets the value of the splrpkskuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSPLRPKSKUID(BigInteger value) {
        this.splrpkskuid = value;
    }

    /**
     * Gets the value of the splrpktypcd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPLRPKTYPCD() {
        return splrpktypcd;
    }

    /**
     * Sets the value of the splrpktypcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPLRPKTYPCD(String value) {
        this.splrpktypcd = value;
    }

    /**
     * Gets the value of the splrpkqty property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSPLRPKQTY() {
        return splrpkqty;
    }

    /**
     * Sets the value of the splrpkqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSPLRPKQTY(BigInteger value) {
        this.splrpkqty = value;
    }

    /**
     * Gets the value of the splrpkordcd property.
     * 
     * @return
     *     possible object is
     *     {@link SPLRPKORDCD }
     *     
     */
    public SPLRPKORDCD getSPLRPKORDCD() {
        return splrpkordcd;
    }

    /**
     * Sets the value of the splrpkordcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link SPLRPKORDCD }
     *     
     */
    public void setSPLRPKORDCD(SPLRPKORDCD value) {
        this.splrpkordcd = value;
    }

    /**
     * Gets the value of the splrpkprimsw property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSPLRPKPRIMSW() {
        return splrpkprimsw;
    }

    /**
     * Sets the value of the splrpkprimsw property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSPLRPKPRIMSW(BigInteger value) {
        this.splrpkprimsw = value;
    }

    /**
     * Gets the value of the splrpkrndmwtsw property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSPLRPKRNDMWTSW() {
        return splrpkrndmwtsw;
    }

    /**
     * Sets the value of the splrpkrndmwtsw property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSPLRPKRNDMWTSW(BigInteger value) {
        this.splrpkrndmwtsw = value;
    }

    /**
     * Gets the value of the splrpkrecvauthsw property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSPLRPKRECVAUTHSW() {
        return splrpkrecvauthsw;
    }

    /**
     * Sets the value of the splrpkrecvauthsw property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSPLRPKRECVAUTHSW(BigInteger value) {
        this.splrpkrecvauthsw = value;
    }

    /**
     * Gets the value of the splrpkcst property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSPLRPKCST() {
        return splrpkcst;
    }

    /**
     * Sets the value of the splrpkcst property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSPLRPKCST(BigDecimal value) {
        this.splrpkcst = value;
    }

    /**
     * Gets the value of the splrpkstrnbr property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSPLRPKSTRNBR() {
        return splrpkstrnbr;
    }

    /**
     * Sets the value of the splrpkstrnbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSPLRPKSTRNBR(BigInteger value) {
        this.splrpkstrnbr = value;
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
     *         &lt;element name="SP" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "sp"
    })
    public static class G3SPLRPKRECCD {

        @XmlElement(name = "SP", namespace = "http://xmlns.heb.com/ei/G3")
        protected String sp;

        /**
         * Gets the value of the sp property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSP() {
            return sp;
        }

        /**
         * Sets the value of the sp property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSP(String value) {
            this.sp = value;
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
     *         &lt;element ref="{http://xmlns.heb.com/ei/CDS_ITM_ID}CDS_ITM_ID"/>
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
        "cdsitmid"
    })
    public static class SPLRPKID {

        @XmlElement(name = "CDS_ITM_ID", namespace = "http://xmlns.heb.com/ei/CDS_ITM_ID", required = true)
        protected CDSITMID cdsitmid;

        /**
         * Gets the value of the cdsitmid property.
         * 
         * @return
         *     possible object is
         *     {@link CDSITMID }
         *     
         */
        public CDSITMID getCDSITMID() {
            return cdsitmid;
        }

        /**
         * Sets the value of the cdsitmid property.
         * 
         * @param value
         *     allowed object is
         *     {@link CDSITMID }
         *     
         */
        public void setCDSITMID(CDSITMID value) {
            this.cdsitmid = value;
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
     *         &lt;element name="SPLR_ITM_VNDR_CD" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "splritmvndrcd"
    })
    public static class SPLRPKORDCD {

        @XmlElement(name = "SPLR_ITM_VNDR_CD", namespace = "http://xmlns.heb.com/ei/G3", required = true)
        protected String splritmvndrcd;

        /**
         * Gets the value of the splritmvndrcd property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPLRITMVNDRCD() {
            return splritmvndrcd;
        }

        /**
         * Sets the value of the splritmvndrcd property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPLRITMVNDRCD(String value) {
            this.splritmvndrcd = value;
        }

    }

}
