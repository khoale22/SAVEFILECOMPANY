/*
 *  G3VNDRREC
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.authorization.xml;

import javax.xml.bind.annotation.*;
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
 *         &lt;element name="G3_VNDR_REC_CD" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="SU" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
 *         &lt;element name="VNDR_RSULT_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_VNDR_ID" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="VNDR_REC_FLAG" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="VNDR_VNDR_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_DUNN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_NM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_ADD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_CTY" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_STATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_REP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_ZIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_PHN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_TOT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_DTL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_STYLE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_DEPT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_DEPT_2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_DEPT_3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_COD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_DSD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_CHECKIN_METHD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_NEX_EVAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_DEPT_4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_DEPT_5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_DEL_MISC_ALLOWNCES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_DEL_MISC_CHRGS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_RET_MISC_CHRGS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_RET_MISC_ALLOWANCES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_DUNN_LOC_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VNDR_RET_ONLY" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "g3VNDRRECCD",
    "crudcd",
    "vndrrsultcd",
    "vndrvndrid",
    "vndrrecflag",
    "vndrvndrnum",
    "vndrdunn",
    "vndrnm",
    "vndradd",
    "vndrcty",
    "vndrstate",
    "vndrrep",
    "vndrzip",
    "vndrphn",
    "vndrtot",
    "vndrdtl",
    "vndrstyle",
    "vndrdept",
    "vndrdept2",
    "vndrdept3",
    "vndrcod",
    "vndrdsd",
    "vndrcheckinmethd",
    "vndrnexeval",
    "vndrdept4",
    "vndrdept5",
    "vndrdelmiscallownces",
    "vndrdelmiscchrgs",
    "vndrretmiscchrgs",
    "vndrretmiscallowances",
    "vndrdunnloccd",
    "vndrretonly"
})
@XmlRootElement(name = "G3_VNDR_REC", namespace = "http://xmlns.heb.com/ei/G3")
public class G3VNDRREC {

    @XmlElement(name = "G3_VNDR_REC_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected G3VNDRRECCD g3VNDRRECCD;
    @XmlElement(name = "CRUD_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected Integer crudcd;
    @XmlElement(name = "VNDR_RSULT_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrrsultcd;
    @XmlElement(name = "VNDR_VNDR_ID", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger vndrvndrid;
    @XmlElement(name = "VNDR_REC_FLAG", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger vndrrecflag;
    @XmlElement(name = "VNDR_VNDR_NUM", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrvndrnum;
    @XmlElement(name = "VNDR_DUNN", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrdunn;
    @XmlElement(name = "VNDR_NM", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrnm;
    @XmlElement(name = "VNDR_ADD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndradd;
    @XmlElement(name = "VNDR_CTY", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrcty;
    @XmlElement(name = "VNDR_STATE", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrstate;
    @XmlElement(name = "VNDR_REP", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrrep;
    @XmlElement(name = "VNDR_ZIP", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrzip;
    @XmlElement(name = "VNDR_PHN", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrphn;
    @XmlElement(name = "VNDR_TOT", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrtot;
    @XmlElement(name = "VNDR_DTL", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrdtl;
    @XmlElement(name = "VNDR_STYLE", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrstyle;
    @XmlElement(name = "VNDR_DEPT", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrdept;
    @XmlElement(name = "VNDR_DEPT_2", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrdept2;
    @XmlElement(name = "VNDR_DEPT_3", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrdept3;
    @XmlElement(name = "VNDR_COD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrcod;
    @XmlElement(name = "VNDR_DSD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrdsd;
    @XmlElement(name = "VNDR_CHECKIN_METHD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrcheckinmethd;
    @XmlElement(name = "VNDR_NEX_EVAL", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrnexeval;
    @XmlElement(name = "VNDR_DEPT_4", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrdept4;
    @XmlElement(name = "VNDR_DEPT_5", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrdept5;
    @XmlElement(name = "VNDR_DEL_MISC_ALLOWNCES", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrdelmiscallownces;
    @XmlElement(name = "VNDR_DEL_MISC_CHRGS", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrdelmiscchrgs;
    @XmlElement(name = "VNDR_RET_MISC_CHRGS", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrretmiscchrgs;
    @XmlElement(name = "VNDR_RET_MISC_ALLOWANCES", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrretmiscallowances;
    @XmlElement(name = "VNDR_DUNN_LOC_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrdunnloccd;
    @XmlElement(name = "VNDR_RET_ONLY", namespace = "http://xmlns.heb.com/ei/G3")
    protected String vndrretonly;

    /**
     * Gets the value of the g3VNDRRECCD property.
     * 
     * @return
     *     possible object is
     *     {@link G3VNDRRECCD }
     *     
     */
    public G3VNDRRECCD getG3VNDRRECCD() {
        return g3VNDRRECCD;
    }

    /**
     * Sets the value of the g3VNDRRECCD property.
     * 
     * @param value
     *     allowed object is
     *     {@link G3VNDRRECCD }
     *     
     */
    public void setG3VNDRRECCD(G3VNDRRECCD value) {
        this.g3VNDRRECCD = value;
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
     * Gets the value of the vndrrsultcd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRRSULTCD() {
        return vndrrsultcd;
    }

    /**
     * Sets the value of the vndrrsultcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRRSULTCD(String value) {
        this.vndrrsultcd = value;
    }

    /**
     * Gets the value of the vndrvndrid property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getVNDRVNDRID() {
        return vndrvndrid;
    }

    /**
     * Sets the value of the vndrvndrid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setVNDRVNDRID(BigInteger value) {
        this.vndrvndrid = value;
    }

    /**
     * Gets the value of the vndrrecflag property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getVNDRRECFLAG() {
        return vndrrecflag;
    }

    /**
     * Sets the value of the vndrrecflag property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setVNDRRECFLAG(BigInteger value) {
        this.vndrrecflag = value;
    }

    /**
     * Gets the value of the vndrvndrnum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRVNDRNUM() {
        return vndrvndrnum;
    }

    /**
     * Sets the value of the vndrvndrnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRVNDRNUM(String value) {
        this.vndrvndrnum = value;
    }

    /**
     * Gets the value of the vndrdunn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRDUNN() {
        return vndrdunn;
    }

    /**
     * Sets the value of the vndrdunn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRDUNN(String value) {
        this.vndrdunn = value;
    }

    /**
     * Gets the value of the vndrnm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRNM() {
        return vndrnm;
    }

    /**
     * Sets the value of the vndrnm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRNM(String value) {
        this.vndrnm = value;
    }

    /**
     * Gets the value of the vndradd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRADD() {
        return vndradd;
    }

    /**
     * Sets the value of the vndradd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRADD(String value) {
        this.vndradd = value;
    }

    /**
     * Gets the value of the vndrcty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRCTY() {
        return vndrcty;
    }

    /**
     * Sets the value of the vndrcty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRCTY(String value) {
        this.vndrcty = value;
    }

    /**
     * Gets the value of the vndrstate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRSTATE() {
        return vndrstate;
    }

    /**
     * Sets the value of the vndrstate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRSTATE(String value) {
        this.vndrstate = value;
    }

    /**
     * Gets the value of the vndrrep property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRREP() {
        return vndrrep;
    }

    /**
     * Sets the value of the vndrrep property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRREP(String value) {
        this.vndrrep = value;
    }

    /**
     * Gets the value of the vndrzip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRZIP() {
        return vndrzip;
    }

    /**
     * Sets the value of the vndrzip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRZIP(String value) {
        this.vndrzip = value;
    }

    /**
     * Gets the value of the vndrphn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRPHN() {
        return vndrphn;
    }

    /**
     * Sets the value of the vndrphn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRPHN(String value) {
        this.vndrphn = value;
    }

    /**
     * Gets the value of the vndrtot property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRTOT() {
        return vndrtot;
    }

    /**
     * Sets the value of the vndrtot property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRTOT(String value) {
        this.vndrtot = value;
    }

    /**
     * Gets the value of the vndrdtl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRDTL() {
        return vndrdtl;
    }

    /**
     * Sets the value of the vndrdtl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRDTL(String value) {
        this.vndrdtl = value;
    }

    /**
     * Gets the value of the vndrstyle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRSTYLE() {
        return vndrstyle;
    }

    /**
     * Sets the value of the vndrstyle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRSTYLE(String value) {
        this.vndrstyle = value;
    }

    /**
     * Gets the value of the vndrdept property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRDEPT() {
        return vndrdept;
    }

    /**
     * Sets the value of the vndrdept property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRDEPT(String value) {
        this.vndrdept = value;
    }

    /**
     * Gets the value of the vndrdept2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRDEPT2() {
        return vndrdept2;
    }

    /**
     * Sets the value of the vndrdept2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRDEPT2(String value) {
        this.vndrdept2 = value;
    }

    /**
     * Gets the value of the vndrdept3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRDEPT3() {
        return vndrdept3;
    }

    /**
     * Sets the value of the vndrdept3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRDEPT3(String value) {
        this.vndrdept3 = value;
    }

    /**
     * Gets the value of the vndrcod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRCOD() {
        return vndrcod;
    }

    /**
     * Sets the value of the vndrcod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRCOD(String value) {
        this.vndrcod = value;
    }

    /**
     * Gets the value of the vndrdsd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRDSD() {
        return vndrdsd;
    }

    /**
     * Sets the value of the vndrdsd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRDSD(String value) {
        this.vndrdsd = value;
    }

    /**
     * Gets the value of the vndrcheckinmethd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRCHECKINMETHD() {
        return vndrcheckinmethd;
    }

    /**
     * Sets the value of the vndrcheckinmethd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRCHECKINMETHD(String value) {
        this.vndrcheckinmethd = value;
    }

    /**
     * Gets the value of the vndrnexeval property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRNEXEVAL() {
        return vndrnexeval;
    }

    /**
     * Sets the value of the vndrnexeval property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRNEXEVAL(String value) {
        this.vndrnexeval = value;
    }

    /**
     * Gets the value of the vndrdept4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRDEPT4() {
        return vndrdept4;
    }

    /**
     * Sets the value of the vndrdept4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRDEPT4(String value) {
        this.vndrdept4 = value;
    }

    /**
     * Gets the value of the vndrdept5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRDEPT5() {
        return vndrdept5;
    }

    /**
     * Sets the value of the vndrdept5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRDEPT5(String value) {
        this.vndrdept5 = value;
    }

    /**
     * Gets the value of the vndrdelmiscallownces property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRDELMISCALLOWNCES() {
        return vndrdelmiscallownces;
    }

    /**
     * Sets the value of the vndrdelmiscallownces property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRDELMISCALLOWNCES(String value) {
        this.vndrdelmiscallownces = value;
    }

    /**
     * Gets the value of the vndrdelmiscchrgs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRDELMISCCHRGS() {
        return vndrdelmiscchrgs;
    }

    /**
     * Sets the value of the vndrdelmiscchrgs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRDELMISCCHRGS(String value) {
        this.vndrdelmiscchrgs = value;
    }

    /**
     * Gets the value of the vndrretmiscchrgs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRRETMISCCHRGS() {
        return vndrretmiscchrgs;
    }

    /**
     * Sets the value of the vndrretmiscchrgs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRRETMISCCHRGS(String value) {
        this.vndrretmiscchrgs = value;
    }

    /**
     * Gets the value of the vndrretmiscallowances property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRRETMISCALLOWANCES() {
        return vndrretmiscallowances;
    }

    /**
     * Sets the value of the vndrretmiscallowances property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRRETMISCALLOWANCES(String value) {
        this.vndrretmiscallowances = value;
    }

    /**
     * Gets the value of the vndrdunnloccd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRDUNNLOCCD() {
        return vndrdunnloccd;
    }

    /**
     * Sets the value of the vndrdunnloccd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRDUNNLOCCD(String value) {
        this.vndrdunnloccd = value;
    }

    /**
     * Gets the value of the vndrretonly property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVNDRRETONLY() {
        return vndrretonly;
    }

    /**
     * Sets the value of the vndrretonly property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVNDRRETONLY(String value) {
        this.vndrretonly = value;
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
     *         &lt;element name="SU" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "su"
    })
    public static class G3VNDRRECCD {

        @XmlElement(name = "SU", namespace = "http://xmlns.heb.com/ei/G3")
        protected String su;

        /**
         * Gets the value of the su property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSU() {
            return su;
        }

        /**
         * Sets the value of the su property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSU(String value) {
            this.su = value;
        }

    }

}
