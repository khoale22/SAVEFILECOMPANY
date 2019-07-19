/*
 *  G3DEPTREC
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
 *         &lt;element name="G3_DEPT_REC_CODE" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="DP" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
 *         &lt;element name="DEPT_RSULT_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DEPT_DEPT_NUMBER" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="DEPT_DESCRIPTION" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DEPT_FOOD_STAMP" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="DEPT_TRADING_STAMP" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
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
    "g3DEPTRECCD",
    "crudcd",
    "deptrsultcd",
    "deptdeptnumber",
    "deptdescription",
    "deptfoodstamp",
    "depttradingstamp"
})
@XmlRootElement(name = "G3_DEPT_REC", namespace = "http://xmlns.heb.com/ei/G3")
public class G3DEPTREC {

    @XmlElement(name = "G3_DEPT_REC_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected G3DEPTRECCD g3DEPTRECCD;
    @XmlElement(name = "CRUD_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected Integer crudcd;
    @XmlElement(name = "DEPT_RSULT_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String deptrsultcd;
    @XmlElement(name = "DEPT_DEPT_NUMBER", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger deptdeptnumber;
    @XmlElement(name = "DEPT_DESCRIPTION", namespace = "http://xmlns.heb.com/ei/G3")
    protected String deptdescription;
    @XmlElement(name = "DEPT_FOOD_STAMP", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger deptfoodstamp;
    @XmlElement(name = "DEPT_TRADING_STAMP", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger depttradingstamp;

    /**
     * Gets the value of the g3DEPTRECCD property.
     * 
     * @return
     *     possible object is
     *     {@link G3DEPTRECCD }
     *     
     */
    public G3DEPTRECCD getG3DEPTRECCD() {
        return g3DEPTRECCD;
    }

    /**
     * Sets the value of the g3DEPTRECCD property.
     * 
     * @param value
     *     allowed object is
     *     {@link G3DEPTRECCD }
     *     
     */
    public void setG3DEPTRECCD(G3DEPTRECCD value) {
        this.g3DEPTRECCD = value;
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
     * Gets the value of the deptrsultcd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDEPTRSULTCD() {
        return deptrsultcd;
    }

    /**
     * Sets the value of the deptrsultcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDEPTRSULTCD(String value) {
        this.deptrsultcd = value;
    }

    /**
     * Gets the value of the deptdeptnumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDEPTDEPTNUMBER() {
        return deptdeptnumber;
    }

    /**
     * Sets the value of the deptdeptnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDEPTDEPTNUMBER(BigInteger value) {
        this.deptdeptnumber = value;
    }

    /**
     * Gets the value of the deptdescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDEPTDESCRIPTION() {
        return deptdescription;
    }

    /**
     * Sets the value of the deptdescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDEPTDESCRIPTION(String value) {
        this.deptdescription = value;
    }

    /**
     * Gets the value of the deptfoodstamp property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDEPTFOODSTAMP() {
        return deptfoodstamp;
    }

    /**
     * Sets the value of the deptfoodstamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDEPTFOODSTAMP(BigInteger value) {
        this.deptfoodstamp = value;
    }

    /**
     * Gets the value of the depttradingstamp property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDEPTTRADINGSTAMP() {
        return depttradingstamp;
    }

    /**
     * Sets the value of the depttradingstamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDEPTTRADINGSTAMP(BigInteger value) {
        this.depttradingstamp = value;
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
     *         &lt;element name="DP" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "dp"
    })
    public static class G3DEPTRECCD {

        @XmlElement(name = "DP", namespace = "http://xmlns.heb.com/ei/G3")
        protected String dp;

        /**
         * Gets the value of the dp property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDP() {
            return dp;
        }

        /**
         * Sets the value of the dp property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDP(String value) {
            this.dp = value;
        }

    }

}
