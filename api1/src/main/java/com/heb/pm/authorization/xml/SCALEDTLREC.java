/*
 *  SCALEDTLREC
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
 *         &lt;element name="SCALE_PK_REC_CD" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="SC" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
 *         &lt;element name="COMMAND_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="STORE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DEPARTMENT_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PLU_NO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="UPC_CODE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="DESC_ONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESC_SIZE1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESC_TWO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESC_SIZE2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESC_THREE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESC_SIZE3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESC_FOUR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESC_SIZE4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UNIT_PRICE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="EXCEPT_PRICE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="FIXED_WEIGHT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="UNIT_MEASURE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BY_COUNT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WRAPPED_TARE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SHELF_LIFE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="USE_BY" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMMON_CLASS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LOGO_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GRADE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MESS_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INGR_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TRAY_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUTRI_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ACTION_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FORCED_TARE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LABEL_FORMAT1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LABEL_FORMAT2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LABEL_ROTATE1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LABEL_ROTATE2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INGRED_TEXT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FS_PRICE1" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="FS_DISC_TYPEE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PERCENTAGE_TARE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PRICE_CHANGE_TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EAS_LABEL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="USER_FIELD1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="USER_FIELD2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="USER_FIELD3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="USER_FIELD4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="USER_FIELD5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="USER_FIELD6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="USER_FIELD7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="USER_FIELD8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "scalepkreccd",
    "crudcd",
    "commandcode",
    "storenumber",
    "departmentnumber",
    "pluno",
    "upccode",
    "descone",
    "descsize1",
    "desctwo",
    "descsize2",
    "descthree",
    "descsize3",
    "descfour",
    "descsize4",
    "unitprice",
    "exceptprice",
    "fixedweight",
    "unitmeasure",
    "bycount",
    "wrappedtare",
    "shelflife",
    "useby",
    "commonclass",
    "logonumber",
    "gradenumber",
    "messnumber",
    "ingrnumber",
    "traynumber",
    "nutrinumber",
    "actionnumber",
    "forcedtare",
    "labelformat1",
    "labelformat2",
    "labelrotate1",
    "labelrotate2",
    "ingredtext",
    "fsprice1",
    "fsdisctypee",
    "percentagetare",
    "pricechangetype",
    "easlabel",
    "userfield1",
    "userfield2",
    "userfield3",
    "userfield4",
    "userfield5",
    "userfield6",
    "userfield7",
    "userfield8"
})
@XmlRootElement(name = "SCALE_DTL_REC", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
public class SCALEDTLREC {

    @XmlElement(name = "SCALE_PK_REC_CD", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected SCALEPKRECCD scalepkreccd;
    @XmlElement(name = "CRUD_CD", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected Integer crudcd;
    @XmlElement(name = "COMMAND_CODE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String commandcode;
    @XmlElement(name = "STORE_NUMBER", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String storenumber;
    @XmlElement(name = "DEPARTMENT_NUMBER", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String departmentnumber;
    @XmlElement(name = "PLU_NO", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected BigDecimal pluno;
    @XmlElement(name = "UPC_CODE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected BigDecimal upccode;
    @XmlElement(name = "DESC_ONE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String descone;
    @XmlElement(name = "DESC_SIZE1", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String descsize1;
    @XmlElement(name = "DESC_TWO", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String desctwo;
    @XmlElement(name = "DESC_SIZE2", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String descsize2;
    @XmlElement(name = "DESC_THREE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String descthree;
    @XmlElement(name = "DESC_SIZE3", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String descsize3;
    @XmlElement(name = "DESC_FOUR", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String descfour;
    @XmlElement(name = "DESC_SIZE4", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String descsize4;
    @XmlElement(name = "UNIT_PRICE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected BigDecimal unitprice;
    @XmlElement(name = "EXCEPT_PRICE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected BigDecimal exceptprice;
    @XmlElement(name = "FIXED_WEIGHT", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected BigDecimal fixedweight;
    @XmlElement(name = "UNIT_MEASURE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String unitmeasure;
    @XmlElement(name = "BY_COUNT", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String bycount;
    @XmlElement(name = "WRAPPED_TARE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String wrappedtare;
    @XmlElement(name = "SHELF_LIFE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String shelflife;
    @XmlElement(name = "USE_BY", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String useby;
    @XmlElement(name = "COMMON_CLASS", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String commonclass;
    @XmlElement(name = "LOGO_NUMBER", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String logonumber;
    @XmlElement(name = "GRADE_NUMBER", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String gradenumber;
    @XmlElement(name = "MESS_NUMBER", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String messnumber;
    @XmlElement(name = "INGR_NUMBER", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String ingrnumber;
    @XmlElement(name = "TRAY_NUMBER", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String traynumber;
    @XmlElement(name = "NUTRI_NUMBER", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String nutrinumber;
    @XmlElement(name = "ACTION_NUMBER", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String actionnumber;
    @XmlElement(name = "FORCED_TARE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String forcedtare;
    @XmlElement(name = "LABEL_FORMAT1", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String labelformat1;
    @XmlElement(name = "LABEL_FORMAT2", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String labelformat2;
    @XmlElement(name = "LABEL_ROTATE1", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String labelrotate1;
    @XmlElement(name = "LABEL_ROTATE2", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String labelrotate2;
    @XmlElement(name = "INGRED_TEXT", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String ingredtext;
    @XmlElement(name = "FS_PRICE1", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected BigDecimal fsprice1;
    @XmlElement(name = "FS_DISC_TYPEE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String fsdisctypee;
    @XmlElement(name = "PERCENTAGE_TARE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String percentagetare;
    @XmlElement(name = "PRICE_CHANGE_TYPE", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String pricechangetype;
    @XmlElement(name = "EAS_LABEL", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String easlabel;
    @XmlElement(name = "USER_FIELD1", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String userfield1;
    @XmlElement(name = "USER_FIELD2", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String userfield2;
    @XmlElement(name = "USER_FIELD3", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String userfield3;
    @XmlElement(name = "USER_FIELD4", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String userfield4;
    @XmlElement(name = "USER_FIELD5", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String userfield5;
    @XmlElement(name = "USER_FIELD6", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String userfield6;
    @XmlElement(name = "USER_FIELD7", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String userfield7;
    @XmlElement(name = "USER_FIELD8", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
    protected String userfield8;

    /**
     * Gets the value of the scalepkreccd property.
     * 
     * @return
     *     possible object is
     *     {@link SCALEPKRECCD }
     *     
     */
    public SCALEPKRECCD getSCALEPKRECCD() {
        return scalepkreccd;
    }

    /**
     * Sets the value of the scalepkreccd property.
     * 
     * @param value
     *     allowed object is
     *     {@link SCALEPKRECCD }
     *     
     */
    public void setSCALEPKRECCD(SCALEPKRECCD value) {
        this.scalepkreccd = value;
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
     * Gets the value of the commandcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMMANDCODE() {
        return commandcode;
    }

    /**
     * Sets the value of the commandcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMMANDCODE(String value) {
        this.commandcode = value;
    }

    /**
     * Gets the value of the storenumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTORENUMBER() {
        return storenumber;
    }

    /**
     * Sets the value of the storenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTORENUMBER(String value) {
        this.storenumber = value;
    }

    /**
     * Gets the value of the departmentnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDEPARTMENTNUMBER() {
        return departmentnumber;
    }

    /**
     * Sets the value of the departmentnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDEPARTMENTNUMBER(String value) {
        this.departmentnumber = value;
    }

    /**
     * Gets the value of the pluno property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPLUNO() {
        return pluno;
    }

    /**
     * Sets the value of the pluno property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPLUNO(BigDecimal value) {
        this.pluno = value;
    }

    /**
     * Gets the value of the upccode property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUPCCODE() {
        return upccode;
    }

    /**
     * Sets the value of the upccode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUPCCODE(BigDecimal value) {
        this.upccode = value;
    }

    /**
     * Gets the value of the descone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCONE() {
        return descone;
    }

    /**
     * Sets the value of the descone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCONE(String value) {
        this.descone = value;
    }

    /**
     * Gets the value of the descsize1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCSIZE1() {
        return descsize1;
    }

    /**
     * Sets the value of the descsize1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCSIZE1(String value) {
        this.descsize1 = value;
    }

    /**
     * Gets the value of the desctwo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCTWO() {
        return desctwo;
    }

    /**
     * Sets the value of the desctwo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCTWO(String value) {
        this.desctwo = value;
    }

    /**
     * Gets the value of the descsize2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCSIZE2() {
        return descsize2;
    }

    /**
     * Sets the value of the descsize2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCSIZE2(String value) {
        this.descsize2 = value;
    }

    /**
     * Gets the value of the descthree property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCTHREE() {
        return descthree;
    }

    /**
     * Sets the value of the descthree property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCTHREE(String value) {
        this.descthree = value;
    }

    /**
     * Gets the value of the descsize3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCSIZE3() {
        return descsize3;
    }

    /**
     * Sets the value of the descsize3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCSIZE3(String value) {
        this.descsize3 = value;
    }

    /**
     * Gets the value of the descfour property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCFOUR() {
        return descfour;
    }

    /**
     * Sets the value of the descfour property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCFOUR(String value) {
        this.descfour = value;
    }

    /**
     * Gets the value of the descsize4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCSIZE4() {
        return descsize4;
    }

    /**
     * Sets the value of the descsize4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCSIZE4(String value) {
        this.descsize4 = value;
    }

    /**
     * Gets the value of the unitprice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUNITPRICE() {
        return unitprice;
    }

    /**
     * Sets the value of the unitprice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUNITPRICE(BigDecimal value) {
        this.unitprice = value;
    }

    /**
     * Gets the value of the exceptprice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEXCEPTPRICE() {
        return exceptprice;
    }

    /**
     * Sets the value of the exceptprice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEXCEPTPRICE(BigDecimal value) {
        this.exceptprice = value;
    }

    /**
     * Gets the value of the fixedweight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFIXEDWEIGHT() {
        return fixedweight;
    }

    /**
     * Sets the value of the fixedweight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFIXEDWEIGHT(BigDecimal value) {
        this.fixedweight = value;
    }

    /**
     * Gets the value of the unitmeasure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUNITMEASURE() {
        return unitmeasure;
    }

    /**
     * Sets the value of the unitmeasure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUNITMEASURE(String value) {
        this.unitmeasure = value;
    }

    /**
     * Gets the value of the bycount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBYCOUNT() {
        return bycount;
    }

    /**
     * Sets the value of the bycount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBYCOUNT(String value) {
        this.bycount = value;
    }

    /**
     * Gets the value of the wrappedtare property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWRAPPEDTARE() {
        return wrappedtare;
    }

    /**
     * Sets the value of the wrappedtare property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWRAPPEDTARE(String value) {
        this.wrappedtare = value;
    }

    /**
     * Gets the value of the shelflife property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHELFLIFE() {
        return shelflife;
    }

    /**
     * Sets the value of the shelflife property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHELFLIFE(String value) {
        this.shelflife = value;
    }

    /**
     * Gets the value of the useby property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSEBY() {
        return useby;
    }

    /**
     * Sets the value of the useby property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSEBY(String value) {
        this.useby = value;
    }

    /**
     * Gets the value of the commonclass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMMONCLASS() {
        return commonclass;
    }

    /**
     * Sets the value of the commonclass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMMONCLASS(String value) {
        this.commonclass = value;
    }

    /**
     * Gets the value of the logonumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOGONUMBER() {
        return logonumber;
    }

    /**
     * Sets the value of the logonumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOGONUMBER(String value) {
        this.logonumber = value;
    }

    /**
     * Gets the value of the gradenumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGRADENUMBER() {
        return gradenumber;
    }

    /**
     * Sets the value of the gradenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGRADENUMBER(String value) {
        this.gradenumber = value;
    }

    /**
     * Gets the value of the messnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMESSNUMBER() {
        return messnumber;
    }

    /**
     * Sets the value of the messnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMESSNUMBER(String value) {
        this.messnumber = value;
    }

    /**
     * Gets the value of the ingrnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINGRNUMBER() {
        return ingrnumber;
    }

    /**
     * Sets the value of the ingrnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINGRNUMBER(String value) {
        this.ingrnumber = value;
    }

    /**
     * Gets the value of the traynumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRAYNUMBER() {
        return traynumber;
    }

    /**
     * Sets the value of the traynumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRAYNUMBER(String value) {
        this.traynumber = value;
    }

    /**
     * Gets the value of the nutrinumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUTRINUMBER() {
        return nutrinumber;
    }

    /**
     * Sets the value of the nutrinumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUTRINUMBER(String value) {
        this.nutrinumber = value;
    }

    /**
     * Gets the value of the actionnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACTIONNUMBER() {
        return actionnumber;
    }

    /**
     * Sets the value of the actionnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACTIONNUMBER(String value) {
        this.actionnumber = value;
    }

    /**
     * Gets the value of the forcedtare property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFORCEDTARE() {
        return forcedtare;
    }

    /**
     * Sets the value of the forcedtare property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFORCEDTARE(String value) {
        this.forcedtare = value;
    }

    /**
     * Gets the value of the labelformat1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLABELFORMAT1() {
        return labelformat1;
    }

    /**
     * Sets the value of the labelformat1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLABELFORMAT1(String value) {
        this.labelformat1 = value;
    }

    /**
     * Gets the value of the labelformat2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLABELFORMAT2() {
        return labelformat2;
    }

    /**
     * Sets the value of the labelformat2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLABELFORMAT2(String value) {
        this.labelformat2 = value;
    }

    /**
     * Gets the value of the labelrotate1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLABELROTATE1() {
        return labelrotate1;
    }

    /**
     * Sets the value of the labelrotate1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLABELROTATE1(String value) {
        this.labelrotate1 = value;
    }

    /**
     * Gets the value of the labelrotate2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLABELROTATE2() {
        return labelrotate2;
    }

    /**
     * Sets the value of the labelrotate2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLABELROTATE2(String value) {
        this.labelrotate2 = value;
    }

    /**
     * Gets the value of the ingredtext property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINGREDTEXT() {
        return ingredtext;
    }

    /**
     * Sets the value of the ingredtext property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINGREDTEXT(String value) {
        this.ingredtext = value;
    }

    /**
     * Gets the value of the fsprice1 property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFSPRICE1() {
        return fsprice1;
    }

    /**
     * Sets the value of the fsprice1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFSPRICE1(BigDecimal value) {
        this.fsprice1 = value;
    }

    /**
     * Gets the value of the fsdisctypee property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFSDISCTYPEE() {
        return fsdisctypee;
    }

    /**
     * Sets the value of the fsdisctypee property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFSDISCTYPEE(String value) {
        this.fsdisctypee = value;
    }

    /**
     * Gets the value of the percentagetare property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPERCENTAGETARE() {
        return percentagetare;
    }

    /**
     * Sets the value of the percentagetare property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPERCENTAGETARE(String value) {
        this.percentagetare = value;
    }

    /**
     * Gets the value of the pricechangetype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRICECHANGETYPE() {
        return pricechangetype;
    }

    /**
     * Sets the value of the pricechangetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRICECHANGETYPE(String value) {
        this.pricechangetype = value;
    }

    /**
     * Gets the value of the easlabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEASLABEL() {
        return easlabel;
    }

    /**
     * Sets the value of the easlabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEASLABEL(String value) {
        this.easlabel = value;
    }

    /**
     * Gets the value of the userfield1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERFIELD1() {
        return userfield1;
    }

    /**
     * Sets the value of the userfield1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERFIELD1(String value) {
        this.userfield1 = value;
    }

    /**
     * Gets the value of the userfield2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERFIELD2() {
        return userfield2;
    }

    /**
     * Sets the value of the userfield2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERFIELD2(String value) {
        this.userfield2 = value;
    }

    /**
     * Gets the value of the userfield3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERFIELD3() {
        return userfield3;
    }

    /**
     * Sets the value of the userfield3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERFIELD3(String value) {
        this.userfield3 = value;
    }

    /**
     * Gets the value of the userfield4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERFIELD4() {
        return userfield4;
    }

    /**
     * Sets the value of the userfield4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERFIELD4(String value) {
        this.userfield4 = value;
    }

    /**
     * Gets the value of the userfield5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERFIELD5() {
        return userfield5;
    }

    /**
     * Sets the value of the userfield5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERFIELD5(String value) {
        this.userfield5 = value;
    }

    /**
     * Gets the value of the userfield6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERFIELD6() {
        return userfield6;
    }

    /**
     * Sets the value of the userfield6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERFIELD6(String value) {
        this.userfield6 = value;
    }

    /**
     * Gets the value of the userfield7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERFIELD7() {
        return userfield7;
    }

    /**
     * Sets the value of the userfield7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERFIELD7(String value) {
        this.userfield7 = value;
    }

    /**
     * Gets the value of the userfield8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERFIELD8() {
        return userfield8;
    }

    /**
     * Sets the value of the userfield8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERFIELD8(String value) {
        this.userfield8 = value;
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
     *         &lt;element name="SC" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "sc"
    })
    public static class SCALEPKRECCD {

        @XmlElement(name = "SC", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
        protected String sc;

        /**
         * Gets the value of the sc property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSC() {
            return sc;
        }

        /**
         * Sets the value of the sc property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSC(String value) {
            this.sc = value;
        }

    }

}
