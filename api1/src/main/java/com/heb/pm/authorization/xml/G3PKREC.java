/*
 *  G3PKREC
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
 *         &lt;element name="G3_PACK_REC_CD" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="PK" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
 *         &lt;element name="PACK_RSULT_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PKG_PK_TYP_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PKG_CU_UPC" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_PK_QTY" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="PKG_PROD_ID" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="PACK_STR_NBR" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
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
    "g3PACKRECCD",
    "crudcd",
    "packrsultcd",
    "pkgpktypcd",
    "pkgcuupc",
    "cupkqty",
    "pkgprodid",
    "packstrnbr"
})
@XmlRootElement(name = "G3_PK_REC", namespace = "http://xmlns.heb.com/ei/G3")
public class G3PKREC {

    @XmlElement(name = "G3_PACK_REC_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected G3PACKRECCD g3PACKRECCD;
    @XmlElement(name = "CRUD_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected Integer crudcd;
    @XmlElement(name = "PACK_RSULT_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String packrsultcd;
    @XmlElement(name = "PKG_PK_TYP_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String pkgpktypcd;
    @XmlElement(name = "PKG_CU_UPC", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger pkgcuupc;
    @XmlElement(name = "CU_PK_QTY", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cupkqty;
    @XmlElement(name = "PKG_PROD_ID", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger pkgprodid;
    @XmlElement(name = "PACK_STR_NBR", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger packstrnbr;

    /**
     * Gets the value of the g3PACKRECCD property.
     * 
     * @return
     *     possible object is
     *     {@link G3PACKRECCD }
     *     
     */
    public G3PACKRECCD getG3PACKRECCD() {
        return g3PACKRECCD;
    }

    /**
     * Sets the value of the g3PACKRECCD property.
     * 
     * @param value
     *     allowed object is
     *     {@link G3PACKRECCD }
     *     
     */
    public void setG3PACKRECCD(G3PACKRECCD value) {
        this.g3PACKRECCD = value;
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
     * Gets the value of the packrsultcd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPACKRSULTCD() {
        return packrsultcd;
    }

    /**
     * Sets the value of the packrsultcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPACKRSULTCD(String value) {
        this.packrsultcd = value;
    }

    /**
     * Gets the value of the pkgpktypcd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPKGPKTYPCD() {
        return pkgpktypcd;
    }

    /**
     * Sets the value of the pkgpktypcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPKGPKTYPCD(String value) {
        this.pkgpktypcd = value;
    }

    /**
     * Gets the value of the pkgcuupc property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPKGCUUPC() {
        return pkgcuupc;
    }

    /**
     * Sets the value of the pkgcuupc property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPKGCUUPC(BigInteger value) {
        this.pkgcuupc = value;
    }

    /**
     * Gets the value of the cupkqty property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUPKQTY() {
        return cupkqty;
    }

    /**
     * Sets the value of the cupkqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUPKQTY(BigInteger value) {
        this.cupkqty = value;
    }

    /**
     * Gets the value of the pkgprodid property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPKGPRODID() {
        return pkgprodid;
    }

    /**
     * Sets the value of the pkgprodid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPKGPRODID(BigInteger value) {
        this.pkgprodid = value;
    }

    /**
     * Gets the value of the packstrnbr property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPACKSTRNBR() {
        return packstrnbr;
    }

    /**
     * Sets the value of the packstrnbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPACKSTRNBR(BigInteger value) {
        this.packstrnbr = value;
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
     *         &lt;element name="PK" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "pk"
    })
    public static class G3PACKRECCD {

        @XmlElement(name = "PK", namespace = "http://xmlns.heb.com/ei/G3")
        protected String pk;

        /**
         * Gets the value of the pk property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPK() {
            return pk;
        }

        /**
         * Sets the value of the pk property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPK(String value) {
            this.pk = value;
        }

    }

}
