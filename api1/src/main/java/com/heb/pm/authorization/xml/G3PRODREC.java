/*
 *  G3PRODREC
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.authorization.xml;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


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
 *         &lt;element name="G3_PROD_REC_CD" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="SK" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
 *         &lt;element name="PROD_RSULT_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.heb.com/ei/CDS_ITM_ID}CDS_ITM_ID"/>
 *         &lt;element name="PROD_SKU_TYP_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROD_AUTH_SW" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="PROD_ACTV_SW" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="PROD_DISC_SW" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="PROD_STAT_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROD_PRIM_UPC_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="PROD_PRIM_DES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROD_DEPT_NBR" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://xmlns.heb.com/ei/CDS_PROD_HIER}CDS_PROD_HIER"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="PROD_VNDR_ID" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="PROD_PACK_QTY" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROD_PACK_SZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROD_REPT_CD" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="PROD_DSD_TAX_CD" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PROD_ENG_CFD" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PROD_SPNSH_CFD" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PROD_SPNSH_ITM_SZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROD_STR_NBR" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
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
    "g3PRODRECCD",
    "crudcd",
    "prodrsultcd",
    "cdsitmid",
    "prodskutypcd",
    "prodauthsw",
    "prodactvsw",
    "proddiscsw",
    "prodstatcd",
    "prodprimupcid",
    "prodprimdes",
    "proddeptnbr",
    "prodvndrid",
    "prodpackqty",
    "prodpacksz",
    "prodreptcd",
    "proddsdtaxcd",
    "prodengcfd",
    "prodspnshcfd",
    "prodspnshitmsz",
    "prodstrnbr"
})
@XmlRootElement(name = "G3_PROD_REC", namespace = "http://xmlns.heb.com/ei/G3")
public class G3PRODREC {

    @XmlElement(name = "G3_PROD_REC_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected G3PRODRECCD g3PRODRECCD;
    @XmlElement(name = "CRUD_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected Integer crudcd;
    @XmlElement(name = "PROD_RSULT_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String prodrsultcd;
    @XmlElement(name = "CDS_ITM_ID", namespace = "http://xmlns.heb.com/ei/CDS_ITM_ID", required = true)
    protected CDSITMID cdsitmid;
    @XmlElement(name = "PROD_SKU_TYP_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String prodskutypcd;
    @XmlElement(name = "PROD_AUTH_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger prodauthsw;
    @XmlElement(name = "PROD_ACTV_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger prodactvsw;
    @XmlElement(name = "PROD_DISC_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger proddiscsw;
    @XmlElement(name = "PROD_STAT_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String prodstatcd;
    @XmlElement(name = "PROD_PRIM_UPC_ID", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigDecimal prodprimupcid;
    @XmlElement(name = "PROD_PRIM_DES", namespace = "http://xmlns.heb.com/ei/G3")
    protected String prodprimdes;
    @XmlElement(name = "PROD_DEPT_NBR", namespace = "http://xmlns.heb.com/ei/G3")
    protected PRODDEPTNBR proddeptnbr;
    @XmlElement(name = "PROD_VNDR_ID", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger prodvndrid;
    @XmlElement(name = "PROD_PACK_QTY", namespace = "http://xmlns.heb.com/ei/G3")
    protected String prodpackqty;
    @XmlElement(name = "PROD_PACK_SZ", namespace = "http://xmlns.heb.com/ei/G3")
    protected String prodpacksz;
    @XmlElement(name = "PROD_REPT_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger prodreptcd;
    @XmlElement(name = "PROD_DSD_TAX_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected List<String> proddsdtaxcd;
    @XmlElement(name = "PROD_ENG_CFD", namespace = "http://xmlns.heb.com/ei/G3")
    protected List<String> prodengcfd;
    @XmlElement(name = "PROD_SPNSH_CFD", namespace = "http://xmlns.heb.com/ei/G3")
    protected List<String> prodspnshcfd;
    @XmlElement(name = "PROD_SPNSH_ITM_SZ", namespace = "http://xmlns.heb.com/ei/G3")
    protected String prodspnshitmsz;
    @XmlElement(name = "PROD_STR_NBR", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger prodstrnbr;

    /**
     * Gets the value of the g3PRODRECCD property.
     * 
     * @return
     *     possible object is
     *     {@link G3PRODRECCD }
     *     
     */
    public G3PRODRECCD getG3PRODRECCD() {
        return g3PRODRECCD;
    }

    /**
     * Sets the value of the g3PRODRECCD property.
     * 
     * @param value
     *     allowed object is
     *     {@link G3PRODRECCD }
     *     
     */
    public void setG3PRODRECCD(G3PRODRECCD value) {
        this.g3PRODRECCD = value;
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
     * Gets the value of the prodrsultcd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRODRSULTCD() {
        return prodrsultcd;
    }

    /**
     * Sets the value of the prodrsultcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRODRSULTCD(String value) {
        this.prodrsultcd = value;
    }

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

    /**
     * Gets the value of the prodskutypcd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRODSKUTYPCD() {
        return prodskutypcd;
    }

    /**
     * Sets the value of the prodskutypcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRODSKUTYPCD(String value) {
        this.prodskutypcd = value;
    }

    /**
     * Gets the value of the prodauthsw property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPRODAUTHSW() {
        return prodauthsw;
    }

    /**
     * Sets the value of the prodauthsw property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPRODAUTHSW(BigInteger value) {
        this.prodauthsw = value;
    }

    /**
     * Gets the value of the prodactvsw property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPRODACTVSW() {
        return prodactvsw;
    }

    /**
     * Sets the value of the prodactvsw property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPRODACTVSW(BigInteger value) {
        this.prodactvsw = value;
    }

    /**
     * Gets the value of the proddiscsw property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPRODDISCSW() {
        return proddiscsw;
    }

    /**
     * Sets the value of the proddiscsw property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPRODDISCSW(BigInteger value) {
        this.proddiscsw = value;
    }

    /**
     * Gets the value of the prodstatcd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRODSTATCD() {
        return prodstatcd;
    }

    /**
     * Sets the value of the prodstatcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRODSTATCD(String value) {
        this.prodstatcd = value;
    }

    /**
     * Gets the value of the prodprimupcid property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPRODPRIMUPCID() {
        return prodprimupcid;
    }

    /**
     * Sets the value of the prodprimupcid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPRODPRIMUPCID(BigDecimal value) {
        this.prodprimupcid = value;
    }

    /**
     * Gets the value of the prodprimdes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRODPRIMDES() {
        return prodprimdes;
    }

    /**
     * Sets the value of the prodprimdes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRODPRIMDES(String value) {
        this.prodprimdes = value;
    }

    /**
     * Gets the value of the proddeptnbr property.
     * 
     * @return
     *     possible object is
     *     {@link PRODDEPTNBR }
     *     
     */
    public PRODDEPTNBR getPRODDEPTNBR() {
        return proddeptnbr;
    }

    /**
     * Sets the value of the proddeptnbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRODDEPTNBR }
     *     
     */
    public void setPRODDEPTNBR(PRODDEPTNBR value) {
        this.proddeptnbr = value;
    }

    /**
     * Gets the value of the prodvndrid property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPRODVNDRID() {
        return prodvndrid;
    }

    /**
     * Sets the value of the prodvndrid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPRODVNDRID(BigInteger value) {
        this.prodvndrid = value;
    }

    /**
     * Gets the value of the prodpackqty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRODPACKQTY() {
        return prodpackqty;
    }

    /**
     * Sets the value of the prodpackqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRODPACKQTY(String value) {
        this.prodpackqty = value;
    }

    /**
     * Gets the value of the prodpacksz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRODPACKSZ() {
        return prodpacksz;
    }

    /**
     * Sets the value of the prodpacksz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRODPACKSZ(String value) {
        this.prodpacksz = value;
    }

    /**
     * Gets the value of the prodreptcd property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPRODREPTCD() {
        return prodreptcd;
    }

    /**
     * Sets the value of the prodreptcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPRODREPTCD(BigInteger value) {
        this.prodreptcd = value;
    }

    /**
     * Gets the value of the proddsdtaxcd property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the proddsdtaxcd property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPRODDSDTAXCD().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPRODDSDTAXCD() {
        if (proddsdtaxcd == null) {
            proddsdtaxcd = new ArrayList<String>();
        }
        return this.proddsdtaxcd;
    }

    /**
     * Gets the value of the prodengcfd property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the prodengcfd property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPRODENGCFD().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPRODENGCFD() {
        if (prodengcfd == null) {
            prodengcfd = new ArrayList<String>();
        }
        return this.prodengcfd;
    }

    /**
     * Gets the value of the prodspnshcfd property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the prodspnshcfd property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPRODSPNSHCFD().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPRODSPNSHCFD() {
        if (prodspnshcfd == null) {
            prodspnshcfd = new ArrayList<String>();
        }
        return this.prodspnshcfd;
    }

    /**
     * Gets the value of the prodspnshitmsz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRODSPNSHITMSZ() {
        return prodspnshitmsz;
    }

    /**
     * Sets the value of the prodspnshitmsz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRODSPNSHITMSZ(String value) {
        this.prodspnshitmsz = value;
    }

    /**
     * Gets the value of the prodstrnbr property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPRODSTRNBR() {
        return prodstrnbr;
    }

    /**
     * Sets the value of the prodstrnbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPRODSTRNBR(BigInteger value) {
        this.prodstrnbr = value;
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
     *         &lt;element name="SK" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "sk"
    })
    public static class G3PRODRECCD {

        @XmlElement(name = "SK", namespace = "http://xmlns.heb.com/ei/G3")
        protected String sk;

        /**
         * Gets the value of the sk property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSK() {
            return sk;
        }

        /**
         * Sets the value of the sk property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSK(String value) {
            this.sk = value;
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
     *         &lt;element ref="{http://xmlns.heb.com/ei/CDS_PROD_HIER}CDS_PROD_HIER"/>
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
        "cdsprodhier"
    })
    public static class PRODDEPTNBR {

        @XmlElement(name = "CDS_PROD_HIER", namespace = "http://xmlns.heb.com/ei/CDS_PROD_HIER", required = true)
        protected CDSPRODHIER cdsprodhier;

        /**
         * Gets the value of the cdsprodhier property.
         * 
         * @return
         *     possible object is
         *     {@link CDSPRODHIER }
         *     
         */
        public CDSPRODHIER getCDSPRODHIER() {
            return cdsprodhier;
        }

        /**
         * Sets the value of the cdsprodhier property.
         * 
         * @param value
         *     allowed object is
         *     {@link CDSPRODHIER }
         *     
         */
        public void setCDSPRODHIER(CDSPRODHIER value) {
            this.cdsprodhier = value;
        }

    }

}
