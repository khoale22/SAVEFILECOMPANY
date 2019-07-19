/*
 *  G3CONSMUNTREC
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.authorization.xml;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
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
 *         &lt;element name="G3_CU_REC_CD" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="IT" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
 *         &lt;element name="CU_RSULT_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CU_UPC" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_ODR_CD" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_EFF_DT" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="CU_TAG_CNTL_CD" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_DISC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CU_EXT_DES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CU_PACK_SZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://xmlns.heb.com/ei/CDS_PROD_HIER}CDS_PROD_HIER" minOccurs="0"/>
 *         &lt;element name="CU_SCAN_PSS_DEPT" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_DES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CU_TAX_SW" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="CU_FD_STMP" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_FAM_CD" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="2" minOccurs="0"/>
 *         &lt;element name="CU_SCL_ITM_SW" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_XFOR_QTY" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_RETL_PRC_AMT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="CU_DSD_XFOR_QTY" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_DSD_PRC_AMT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="CU_MXMAT_NUM" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_NXT_EFF_DT" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_SLBL_SZ" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_LOW_VEL_SW" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_UOM_FACTR_NBR" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="CU_UOM_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CU_SELL_UNITS_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CU_CMPTR_CK_DT" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CU_CMPTR_CK_CC" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="CU_CMPTR_CK_YY" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="CU_CMPTR_CK_MM" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="CU_CMPTR_CK_DD" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CU_PROMO_DES" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CU_CMPAR_RETL_AMT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="CU_CMPAR_XFOR" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_CMPAR_SZ" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="CU_LANG_DESC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CU_LINK_CD" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_TAX_EXMT_AMT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="CU_PRC_REQ_SW" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_QTY_REQ_SW" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_DSD_ITM_SW" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_COUP_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CU_STR_COUP" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_POS_ITM" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_QTY_PRHBT_SW" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_PRE_PRC_AMT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="CU_PRE_PRC_XFOR_QTY" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_AGE_LIM_NBR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CU_TM_LIM_CD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CU_LIM_QTY" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_LIM_PRC_AMT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="CU_LIM_XFOR_QTY" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_BIG1_OFF_QTY" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_BIG1_DISC_AMT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="CU_WIC_SW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CU_TARE_CD" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_RETL_AMT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="CU_PRNT_LBS_SW" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_WHS_TAG_SW" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_NTRNT_SW" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_SELFMANU" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_NON_REPL" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_ATTR" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_WHS_PK_QTY" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_USR31" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CU_OLD_XFOR_QTY" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CU_USR16">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CU_OLD_PRC_AMT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CU_PROD_ID" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_RGC_PRHBT_RTL" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CU_STR_NBR" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
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
    "g3CURECCD",
    "crudcd",
    "cursultcd",
    "cuupc",
    "cuodrcd",
    "cueffdt",
    "cutagcntlcd",
    "cudisc",
    "cuextdes",
    "cupacksz",
    "cdsprodhier",
    "cuscanpssdept",
    "cudes",
    "cutaxsw",
    "cufdstmp",
    "cufamcd",
    "cusclitmsw",
    "cuxforqty",
    "curetlprcamt",
    "cudsdxforqty",
    "cudsdprcamt",
    "cumxmatnum",
    "cunxteffdt",
    "cuslblsz",
    "culowvelsw",
    "cuuomfactrnbr",
    "cuuomcd",
    "cusellunitscd",
    "cucmptrckdt",
    "cupromodes",
    "cucmparretlamt",
    "cucmparxfor",
    "cucmparsz",
    "culangdesc",
    "culinkcd",
    "cutaxexmtamt",
    "cuprcreqsw",
    "cuqtyreqsw",
    "cudsditmsw",
    "cucoupcd",
    "custrcoup",
    "cupositm",
    "cuqtyprhbtsw",
    "cupreprcamt",
    "cupreprcxforqty",
    "cuagelimnbr",
    "cutmlimcd",
    "culimqty",
    "culimprcamt",
    "culimxforqty",
    "cubig1OFFQTY",
    "cubig1DISCAMT",
    "cuwicsw",
    "cutarecd",
    "curetlamt",
    "cuprntlbssw",
    "cuwhstagsw",
    "cuntrntsw",
    "cuselfmanu",
    "cunonrepl",
    "cuattr",
    "cuwhspkqty",
    "cuusr31",
    "cuusr16",
    "cuprodid",
    "curgcprhbtrtl",
    "custrnbr"
})
@XmlRootElement(name = "G3_CONSM_UNT_REC", namespace = "http://xmlns.heb.com/ei/G3")
public class G3CONSMUNTREC {

    @XmlElement(name = "G3_CU_REC_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected G3CURECCD g3CURECCD;
    @XmlElement(name = "CRUD_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected Integer crudcd;
    @XmlElement(name = "CU_RSULT_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String cursultcd;
    @XmlElement(name = "CU_UPC", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cuupc;
    @XmlElement(name = "CU_ODR_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cuodrcd;
    @XmlElement(name = "CU_EFF_DT", namespace = "http://xmlns.heb.com/ei/G3")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar cueffdt;
    @XmlElement(name = "CU_TAG_CNTL_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cutagcntlcd;
    @XmlElement(name = "CU_DISC", namespace = "http://xmlns.heb.com/ei/G3")
    protected String cudisc;
    @XmlElement(name = "CU_EXT_DES", namespace = "http://xmlns.heb.com/ei/G3")
    protected String cuextdes;
    @XmlElement(name = "CU_PACK_SZ", namespace = "http://xmlns.heb.com/ei/G3")
    protected String cupacksz;
    @XmlElement(name = "CDS_PROD_HIER", namespace = "http://xmlns.heb.com/ei/CDS_PROD_HIER")
    protected CDSPRODHIER cdsprodhier;
    @XmlElement(name = "CU_SCAN_PSS_DEPT", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cuscanpssdept;
    @XmlElement(name = "CU_DES", namespace = "http://xmlns.heb.com/ei/G3")
    protected String cudes;
    @XmlElement(name = "CU_TAX_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected List<BigInteger> cutaxsw;
    @XmlElement(name = "CU_FD_STMP", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cufdstmp;
    @XmlElement(name = "CU_FAM_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected List<BigInteger> cufamcd;
    @XmlElement(name = "CU_SCL_ITM_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cusclitmsw;
    @XmlElement(name = "CU_XFOR_QTY", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cuxforqty;
    @XmlElement(name = "CU_RETL_PRC_AMT", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigDecimal curetlprcamt;
    @XmlElement(name = "CU_DSD_XFOR_QTY", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cudsdxforqty;
    @XmlElement(name = "CU_DSD_PRC_AMT", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigDecimal cudsdprcamt;
    @XmlElement(name = "CU_MXMAT_NUM", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cumxmatnum;
    @XmlElement(name = "CU_NXT_EFF_DT", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cunxteffdt;
    @XmlElement(name = "CU_SLBL_SZ", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cuslblsz;
    @XmlElement(name = "CU_LOW_VEL_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger culowvelsw;
    @XmlElement(name = "CU_UOM_FACTR_NBR", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigDecimal cuuomfactrnbr;
    @XmlElement(name = "CU_UOM_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String cuuomcd;
    @XmlElement(name = "CU_SELL_UNITS_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String cusellunitscd;
    @XmlElement(name = "CU_CMPTR_CK_DT", namespace = "http://xmlns.heb.com/ei/G3")
    protected CUCMPTRCKDT cucmptrckdt;
    @XmlElement(name = "CU_PROMO_DES", namespace = "http://xmlns.heb.com/ei/G3")
    protected String cupromodes;
    @XmlElement(name = "CU_CMPAR_RETL_AMT", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigDecimal cucmparretlamt;
    @XmlElement(name = "CU_CMPAR_XFOR", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cucmparxfor;
    @XmlElement(name = "CU_CMPAR_SZ", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigDecimal cucmparsz;
    @XmlElement(name = "CU_LANG_DESC", namespace = "http://xmlns.heb.com/ei/G3")
    protected String culangdesc;
    @XmlElement(name = "CU_LINK_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger culinkcd;
    @XmlElement(name = "CU_TAX_EXMT_AMT", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigDecimal cutaxexmtamt;
    @XmlElement(name = "CU_PRC_REQ_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cuprcreqsw;
    @XmlElement(name = "CU_QTY_REQ_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cuqtyreqsw;
    @XmlElement(name = "CU_DSD_ITM_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cudsditmsw;
    @XmlElement(name = "CU_COUP_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String cucoupcd;
    @XmlElement(name = "CU_STR_COUP", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger custrcoup;
    @XmlElement(name = "CU_POS_ITM", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cupositm;
    @XmlElement(name = "CU_QTY_PRHBT_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cuqtyprhbtsw;
    @XmlElement(name = "CU_PRE_PRC_AMT", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigDecimal cupreprcamt;
    @XmlElement(name = "CU_PRE_PRC_XFOR_QTY", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cupreprcxforqty;
    @XmlElement(name = "CU_AGE_LIM_NBR", namespace = "http://xmlns.heb.com/ei/G3")
    protected String cuagelimnbr;
    @XmlElement(name = "CU_TM_LIM_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected String cutmlimcd;
    @XmlElement(name = "CU_LIM_QTY", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger culimqty;
    @XmlElement(name = "CU_LIM_PRC_AMT", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigDecimal culimprcamt;
    @XmlElement(name = "CU_LIM_XFOR_QTY", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger culimxforqty;
    @XmlElement(name = "CU_BIG1_OFF_QTY", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cubig1OFFQTY;
    @XmlElement(name = "CU_BIG1_DISC_AMT", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigDecimal cubig1DISCAMT;
    @XmlElement(name = "CU_WIC_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected String cuwicsw;
    @XmlElement(name = "CU_TARE_CD", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cutarecd;
    @XmlElement(name = "CU_RETL_AMT", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigDecimal curetlamt;
    @XmlElement(name = "CU_PRNT_LBS_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cuprntlbssw;
    @XmlElement(name = "CU_WHS_TAG_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cuwhstagsw;
    @XmlElement(name = "CU_NTRNT_SW", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cuntrntsw;
    @XmlElement(name = "CU_SELFMANU", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cuselfmanu;
    @XmlElement(name = "CU_NON_REPL", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cunonrepl;
    @XmlElement(name = "CU_ATTR", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cuattr;
    @XmlElement(name = "CU_WHS_PK_QTY", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cuwhspkqty;
    @XmlElement(name = "CU_USR31", namespace = "http://xmlns.heb.com/ei/G3")
    protected CUUSR31 cuusr31;
    @XmlElement(name = "CU_USR16", namespace = "http://xmlns.heb.com/ei/G3", required = true)
    protected CUUSR16 cuusr16;
    @XmlElement(name = "CU_PROD_ID", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger cuprodid;
    @XmlElement(name = "CU_RGC_PRHBT_RTL", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger curgcprhbtrtl;
    @XmlElement(name = "CU_STR_NBR", namespace = "http://xmlns.heb.com/ei/G3")
    protected BigInteger custrnbr;

    /**
     * Gets the value of the g3CURECCD property.
     * 
     * @return
     *     possible object is
     *     {@link G3CURECCD }
     *     
     */
    public G3CURECCD getG3CURECCD() {
        return g3CURECCD;
    }

    /**
     * Sets the value of the g3CURECCD property.
     * 
     * @param value
     *     allowed object is
     *     {@link G3CURECCD }
     *     
     */
    public void setG3CURECCD(G3CURECCD value) {
        this.g3CURECCD = value;
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
     * Gets the value of the cursultcd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCURSULTCD() {
        return cursultcd;
    }

    /**
     * Sets the value of the cursultcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCURSULTCD(String value) {
        this.cursultcd = value;
    }

    /**
     * Gets the value of the cuupc property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUUPC() {
        return cuupc;
    }

    /**
     * Sets the value of the cuupc property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUUPC(BigInteger value) {
        this.cuupc = value;
    }

    /**
     * Gets the value of the cuodrcd property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUODRCD() {
        return cuodrcd;
    }

    /**
     * Sets the value of the cuodrcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUODRCD(BigInteger value) {
        this.cuodrcd = value;
    }

    /**
     * Gets the value of the cueffdt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCUEFFDT() {
        return cueffdt;
    }

    /**
     * Sets the value of the cueffdt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCUEFFDT(XMLGregorianCalendar value) {
        this.cueffdt = value;
    }

    /**
     * Gets the value of the cutagcntlcd property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUTAGCNTLCD() {
        return cutagcntlcd;
    }

    /**
     * Sets the value of the cutagcntlcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUTAGCNTLCD(BigInteger value) {
        this.cutagcntlcd = value;
    }

    /**
     * Gets the value of the cudisc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUDISC() {
        return cudisc;
    }

    /**
     * Sets the value of the cudisc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUDISC(String value) {
        this.cudisc = value;
    }

    /**
     * Gets the value of the cuextdes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUEXTDES() {
        return cuextdes;
    }

    /**
     * Sets the value of the cuextdes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUEXTDES(String value) {
        this.cuextdes = value;
    }

    /**
     * Gets the value of the cupacksz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUPACKSZ() {
        return cupacksz;
    }

    /**
     * Sets the value of the cupacksz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUPACKSZ(String value) {
        this.cupacksz = value;
    }

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

    /**
     * Gets the value of the cuscanpssdept property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUSCANPSSDEPT() {
        return cuscanpssdept;
    }

    /**
     * Sets the value of the cuscanpssdept property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUSCANPSSDEPT(BigInteger value) {
        this.cuscanpssdept = value;
    }

    /**
     * Gets the value of the cudes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUDES() {
        return cudes;
    }

    /**
     * Sets the value of the cudes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUDES(String value) {
        this.cudes = value;
    }

    /**
     * Gets the value of the cutaxsw property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cutaxsw property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCUTAXSW().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getCUTAXSW() {
        if (cutaxsw == null) {
            cutaxsw = new ArrayList<BigInteger>();
        }
        return this.cutaxsw;
    }

    /**
     * Gets the value of the cufdstmp property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUFDSTMP() {
        return cufdstmp;
    }

    /**
     * Sets the value of the cufdstmp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUFDSTMP(BigInteger value) {
        this.cufdstmp = value;
    }

    /**
     * Gets the value of the cufamcd property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cufamcd property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCUFAMCD().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getCUFAMCD() {
        if (cufamcd == null) {
            cufamcd = new ArrayList<BigInteger>();
        }
        return this.cufamcd;
    }

    /**
     * Gets the value of the cusclitmsw property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUSCLITMSW() {
        return cusclitmsw;
    }

    /**
     * Sets the value of the cusclitmsw property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUSCLITMSW(BigInteger value) {
        this.cusclitmsw = value;
    }

    /**
     * Gets the value of the cuxforqty property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUXFORQTY() {
        return cuxforqty;
    }

    /**
     * Sets the value of the cuxforqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUXFORQTY(BigInteger value) {
        this.cuxforqty = value;
    }

    /**
     * Gets the value of the curetlprcamt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCURETLPRCAMT() {
        return curetlprcamt;
    }

    /**
     * Sets the value of the curetlprcamt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCURETLPRCAMT(BigDecimal value) {
        this.curetlprcamt = value;
    }

    /**
     * Gets the value of the cudsdxforqty property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUDSDXFORQTY() {
        return cudsdxforqty;
    }

    /**
     * Sets the value of the cudsdxforqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUDSDXFORQTY(BigInteger value) {
        this.cudsdxforqty = value;
    }

    /**
     * Gets the value of the cudsdprcamt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCUDSDPRCAMT() {
        return cudsdprcamt;
    }

    /**
     * Sets the value of the cudsdprcamt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCUDSDPRCAMT(BigDecimal value) {
        this.cudsdprcamt = value;
    }

    /**
     * Gets the value of the cumxmatnum property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUMXMATNUM() {
        return cumxmatnum;
    }

    /**
     * Sets the value of the cumxmatnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUMXMATNUM(BigInteger value) {
        this.cumxmatnum = value;
    }

    /**
     * Gets the value of the cunxteffdt property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUNXTEFFDT() {
        return cunxteffdt;
    }

    /**
     * Sets the value of the cunxteffdt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUNXTEFFDT(BigInteger value) {
        this.cunxteffdt = value;
    }

    /**
     * Gets the value of the cuslblsz property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUSLBLSZ() {
        return cuslblsz;
    }

    /**
     * Sets the value of the cuslblsz property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUSLBLSZ(BigInteger value) {
        this.cuslblsz = value;
    }

    /**
     * Gets the value of the culowvelsw property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCULOWVELSW() {
        return culowvelsw;
    }

    /**
     * Sets the value of the culowvelsw property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCULOWVELSW(BigInteger value) {
        this.culowvelsw = value;
    }

    /**
     * Gets the value of the cuuomfactrnbr property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCUUOMFACTRNBR() {
        return cuuomfactrnbr;
    }

    /**
     * Sets the value of the cuuomfactrnbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCUUOMFACTRNBR(BigDecimal value) {
        this.cuuomfactrnbr = value;
    }

    /**
     * Gets the value of the cuuomcd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUUOMCD() {
        return cuuomcd;
    }

    /**
     * Sets the value of the cuuomcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUUOMCD(String value) {
        this.cuuomcd = value;
    }

    /**
     * Gets the value of the cusellunitscd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUSELLUNITSCD() {
        return cusellunitscd;
    }

    /**
     * Sets the value of the cusellunitscd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUSELLUNITSCD(String value) {
        this.cusellunitscd = value;
    }

    /**
     * Gets the value of the cucmptrckdt property.
     * 
     * @return
     *     possible object is
     *     {@link CUCMPTRCKDT }
     *     
     */
    public CUCMPTRCKDT getCUCMPTRCKDT() {
        return cucmptrckdt;
    }

    /**
     * Sets the value of the cucmptrckdt property.
     * 
     * @param value
     *     allowed object is
     *     {@link CUCMPTRCKDT }
     *     
     */
    public void setCUCMPTRCKDT(CUCMPTRCKDT value) {
        this.cucmptrckdt = value;
    }

    /**
     * Gets the value of the cupromodes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUPROMODES() {
        return cupromodes;
    }

    /**
     * Sets the value of the cupromodes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUPROMODES(String value) {
        this.cupromodes = value;
    }

    /**
     * Gets the value of the cucmparretlamt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCUCMPARRETLAMT() {
        return cucmparretlamt;
    }

    /**
     * Sets the value of the cucmparretlamt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCUCMPARRETLAMT(BigDecimal value) {
        this.cucmparretlamt = value;
    }

    /**
     * Gets the value of the cucmparxfor property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUCMPARXFOR() {
        return cucmparxfor;
    }

    /**
     * Sets the value of the cucmparxfor property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUCMPARXFOR(BigInteger value) {
        this.cucmparxfor = value;
    }

    /**
     * Gets the value of the cucmparsz property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCUCMPARSZ() {
        return cucmparsz;
    }

    /**
     * Sets the value of the cucmparsz property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCUCMPARSZ(BigDecimal value) {
        this.cucmparsz = value;
    }

    /**
     * Gets the value of the culangdesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCULANGDESC() {
        return culangdesc;
    }

    /**
     * Sets the value of the culangdesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCULANGDESC(String value) {
        this.culangdesc = value;
    }

    /**
     * Gets the value of the culinkcd property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCULINKCD() {
        return culinkcd;
    }

    /**
     * Sets the value of the culinkcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCULINKCD(BigInteger value) {
        this.culinkcd = value;
    }

    /**
     * Gets the value of the cutaxexmtamt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCUTAXEXMTAMT() {
        return cutaxexmtamt;
    }

    /**
     * Sets the value of the cutaxexmtamt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCUTAXEXMTAMT(BigDecimal value) {
        this.cutaxexmtamt = value;
    }

    /**
     * Gets the value of the cuprcreqsw property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUPRCREQSW() {
        return cuprcreqsw;
    }

    /**
     * Sets the value of the cuprcreqsw property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUPRCREQSW(BigInteger value) {
        this.cuprcreqsw = value;
    }

    /**
     * Gets the value of the cuqtyreqsw property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUQTYREQSW() {
        return cuqtyreqsw;
    }

    /**
     * Sets the value of the cuqtyreqsw property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUQTYREQSW(BigInteger value) {
        this.cuqtyreqsw = value;
    }

    /**
     * Gets the value of the cudsditmsw property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUDSDITMSW() {
        return cudsditmsw;
    }

    /**
     * Sets the value of the cudsditmsw property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUDSDITMSW(BigInteger value) {
        this.cudsditmsw = value;
    }

    /**
     * Gets the value of the cucoupcd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUCOUPCD() {
        return cucoupcd;
    }

    /**
     * Sets the value of the cucoupcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUCOUPCD(String value) {
        this.cucoupcd = value;
    }

    /**
     * Gets the value of the custrcoup property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUSTRCOUP() {
        return custrcoup;
    }

    /**
     * Sets the value of the custrcoup property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUSTRCOUP(BigInteger value) {
        this.custrcoup = value;
    }

    /**
     * Gets the value of the cupositm property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUPOSITM() {
        return cupositm;
    }

    /**
     * Sets the value of the cupositm property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUPOSITM(BigInteger value) {
        this.cupositm = value;
    }

    /**
     * Gets the value of the cuqtyprhbtsw property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUQTYPRHBTSW() {
        return cuqtyprhbtsw;
    }

    /**
     * Sets the value of the cuqtyprhbtsw property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUQTYPRHBTSW(BigInteger value) {
        this.cuqtyprhbtsw = value;
    }

    /**
     * Gets the value of the cupreprcamt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCUPREPRCAMT() {
        return cupreprcamt;
    }

    /**
     * Sets the value of the cupreprcamt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCUPREPRCAMT(BigDecimal value) {
        this.cupreprcamt = value;
    }

    /**
     * Gets the value of the cupreprcxforqty property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUPREPRCXFORQTY() {
        return cupreprcxforqty;
    }

    /**
     * Sets the value of the cupreprcxforqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUPREPRCXFORQTY(BigInteger value) {
        this.cupreprcxforqty = value;
    }

    /**
     * Gets the value of the cuagelimnbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUAGELIMNBR() {
        return cuagelimnbr;
    }

    /**
     * Sets the value of the cuagelimnbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUAGELIMNBR(String value) {
        this.cuagelimnbr = value;
    }

    /**
     * Gets the value of the cutmlimcd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUTMLIMCD() {
        return cutmlimcd;
    }

    /**
     * Sets the value of the cutmlimcd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUTMLIMCD(String value) {
        this.cutmlimcd = value;
    }

    /**
     * Gets the value of the culimqty property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCULIMQTY() {
        return culimqty;
    }

    /**
     * Sets the value of the culimqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCULIMQTY(BigInteger value) {
        this.culimqty = value;
    }

    /**
     * Gets the value of the culimprcamt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCULIMPRCAMT() {
        return culimprcamt;
    }

    /**
     * Sets the value of the culimprcamt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCULIMPRCAMT(BigDecimal value) {
        this.culimprcamt = value;
    }

    /**
     * Gets the value of the culimxforqty property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCULIMXFORQTY() {
        return culimxforqty;
    }

    /**
     * Sets the value of the culimxforqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCULIMXFORQTY(BigInteger value) {
        this.culimxforqty = value;
    }

    /**
     * Gets the value of the cubig1OFFQTY property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUBIG1OFFQTY() {
        return cubig1OFFQTY;
    }

    /**
     * Sets the value of the cubig1OFFQTY property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUBIG1OFFQTY(BigInteger value) {
        this.cubig1OFFQTY = value;
    }

    /**
     * Gets the value of the cubig1DISCAMT property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCUBIG1DISCAMT() {
        return cubig1DISCAMT;
    }

    /**
     * Sets the value of the cubig1DISCAMT property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCUBIG1DISCAMT(BigDecimal value) {
        this.cubig1DISCAMT = value;
    }

    /**
     * Gets the value of the cuwicsw property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUWICSW() {
        return cuwicsw;
    }

    /**
     * Sets the value of the cuwicsw property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUWICSW(String value) {
        this.cuwicsw = value;
    }

    /**
     * Gets the value of the cutarecd property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUTARECD() {
        return cutarecd;
    }

    /**
     * Sets the value of the cutarecd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUTARECD(BigInteger value) {
        this.cutarecd = value;
    }

    /**
     * Gets the value of the curetlamt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCURETLAMT() {
        return curetlamt;
    }

    /**
     * Sets the value of the curetlamt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCURETLAMT(BigDecimal value) {
        this.curetlamt = value;
    }

    /**
     * Gets the value of the cuprntlbssw property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUPRNTLBSSW() {
        return cuprntlbssw;
    }

    /**
     * Sets the value of the cuprntlbssw property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUPRNTLBSSW(BigInteger value) {
        this.cuprntlbssw = value;
    }

    /**
     * Gets the value of the cuwhstagsw property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUWHSTAGSW() {
        return cuwhstagsw;
    }

    /**
     * Sets the value of the cuwhstagsw property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUWHSTAGSW(BigInteger value) {
        this.cuwhstagsw = value;
    }

    /**
     * Gets the value of the cuntrntsw property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUNTRNTSW() {
        return cuntrntsw;
    }

    /**
     * Sets the value of the cuntrntsw property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUNTRNTSW(BigInteger value) {
        this.cuntrntsw = value;
    }

    /**
     * Gets the value of the cuselfmanu property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUSELFMANU() {
        return cuselfmanu;
    }

    /**
     * Sets the value of the cuselfmanu property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUSELFMANU(BigInteger value) {
        this.cuselfmanu = value;
    }

    /**
     * Gets the value of the cunonrepl property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUNONREPL() {
        return cunonrepl;
    }

    /**
     * Sets the value of the cunonrepl property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUNONREPL(BigInteger value) {
        this.cunonrepl = value;
    }

    /**
     * Gets the value of the cuattr property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUATTR() {
        return cuattr;
    }

    /**
     * Sets the value of the cuattr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUATTR(BigInteger value) {
        this.cuattr = value;
    }

    /**
     * Gets the value of the cuwhspkqty property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUWHSPKQTY() {
        return cuwhspkqty;
    }

    /**
     * Sets the value of the cuwhspkqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUWHSPKQTY(BigInteger value) {
        this.cuwhspkqty = value;
    }

    /**
     * Gets the value of the cuusr31 property.
     * 
     * @return
     *     possible object is
     *     {@link CUUSR31 }
     *     
     */
    public CUUSR31 getCUUSR31() {
        return cuusr31;
    }

    /**
     * Sets the value of the cuusr31 property.
     * 
     * @param value
     *     allowed object is
     *     {@link CUUSR31 }
     *     
     */
    public void setCUUSR31(CUUSR31 value) {
        this.cuusr31 = value;
    }

    /**
     * Gets the value of the cuusr16 property.
     * 
     * @return
     *     possible object is
     *     {@link CUUSR16 }
     *     
     */
    public CUUSR16 getCUUSR16() {
        return cuusr16;
    }

    /**
     * Sets the value of the cuusr16 property.
     * 
     * @param value
     *     allowed object is
     *     {@link CUUSR16 }
     *     
     */
    public void setCUUSR16(CUUSR16 value) {
        this.cuusr16 = value;
    }

    /**
     * Gets the value of the cuprodid property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUPRODID() {
        return cuprodid;
    }

    /**
     * Sets the value of the cuprodid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUPRODID(BigInteger value) {
        this.cuprodid = value;
    }

    /**
     * Gets the value of the curgcprhbtrtl property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCURGCPRHBTRTL() {
        return curgcprhbtrtl;
    }

    /**
     * Sets the value of the curgcprhbtrtl property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCURGCPRHBTRTL(BigInteger value) {
        this.curgcprhbtrtl = value;
    }

    /**
     * Gets the value of the custrnbr property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCUSTRNBR() {
        return custrnbr;
    }

    /**
     * Sets the value of the custrnbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCUSTRNBR(BigInteger value) {
        this.custrnbr = value;
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
     *         &lt;element name="CU_CMPTR_CK_CC" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="CU_CMPTR_CK_YY" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="CU_CMPTR_CK_MM" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="CU_CMPTR_CK_DD" type="{http://www.w3.org/2001/XMLSchema}integer"/>
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
        "cucmptrckcc",
        "cucmptrckyy",
        "cucmptrckmm",
        "cucmptrckdd"
    })
    public static class CUCMPTRCKDT {

        @XmlElement(name = "CU_CMPTR_CK_CC", namespace = "http://xmlns.heb.com/ei/G3", required = true)
        protected BigInteger cucmptrckcc;
        @XmlElement(name = "CU_CMPTR_CK_YY", namespace = "http://xmlns.heb.com/ei/G3", required = true)
        protected BigInteger cucmptrckyy;
        @XmlElement(name = "CU_CMPTR_CK_MM", namespace = "http://xmlns.heb.com/ei/G3", required = true)
        protected BigInteger cucmptrckmm;
        @XmlElement(name = "CU_CMPTR_CK_DD", namespace = "http://xmlns.heb.com/ei/G3", required = true)
        protected BigInteger cucmptrckdd;

        /**
         * Gets the value of the cucmptrckcc property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getCUCMPTRCKCC() {
            return cucmptrckcc;
        }

        /**
         * Sets the value of the cucmptrckcc property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setCUCMPTRCKCC(BigInteger value) {
            this.cucmptrckcc = value;
        }

        /**
         * Gets the value of the cucmptrckyy property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getCUCMPTRCKYY() {
            return cucmptrckyy;
        }

        /**
         * Sets the value of the cucmptrckyy property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setCUCMPTRCKYY(BigInteger value) {
            this.cucmptrckyy = value;
        }

        /**
         * Gets the value of the cucmptrckmm property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getCUCMPTRCKMM() {
            return cucmptrckmm;
        }

        /**
         * Sets the value of the cucmptrckmm property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setCUCMPTRCKMM(BigInteger value) {
            this.cucmptrckmm = value;
        }

        /**
         * Gets the value of the cucmptrckdd property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getCUCMPTRCKDD() {
            return cucmptrckdd;
        }

        /**
         * Sets the value of the cucmptrckdd property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setCUCMPTRCKDD(BigInteger value) {
            this.cucmptrckdd = value;
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
     *         &lt;element name="CU_OLD_PRC_AMT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
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
        "cuoldprcamt"
    })
    public static class CUUSR16 {

        @XmlElement(name = "CU_OLD_PRC_AMT", namespace = "http://xmlns.heb.com/ei/G3")
        protected BigDecimal cuoldprcamt;

        /**
         * Gets the value of the cuoldprcamt property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCUOLDPRCAMT() {
            return cuoldprcamt;
        }

        /**
         * Sets the value of the cuoldprcamt property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCUOLDPRCAMT(BigDecimal value) {
            this.cuoldprcamt = value;
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
     *         &lt;element name="CU_OLD_XFOR_QTY" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
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
        "cuoldxforqty"
    })
    public static class CUUSR31 {

        @XmlElement(name = "CU_OLD_XFOR_QTY", namespace = "http://xmlns.heb.com/ei/G3")
        protected BigInteger cuoldxforqty;

        /**
         * Gets the value of the cuoldxforqty property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getCUOLDXFORQTY() {
            return cuoldxforqty;
        }

        /**
         * Sets the value of the cuoldxforqty property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setCUOLDXFORQTY(BigInteger value) {
            this.cuoldxforqty = value;
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
     *       &lt;choice>
     *         &lt;element name="IT" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "it"
    })
    public static class G3CURECCD {

        @XmlElement(name = "IT", namespace = "http://xmlns.heb.com/ei/G3")
        protected String it;

        /**
         * Gets the value of the it property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIT() {
            return it;
        }

        /**
         * Sets the value of the it property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIT(String value) {
            this.it = value;
        }

    }

}
