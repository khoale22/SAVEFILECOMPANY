/*
 *  G3HOSTLAYOUT
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.authorization.xml;

import javax.xml.bind.annotation.*;
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
 *         &lt;element ref="{http://xmlns.heb.com/ei/G3}G3_HDR_REC" minOccurs="0"/>
 *         &lt;element name="G3_COMP" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://xmlns.heb.com/ei/G3}G3_VNDR_REC" minOccurs="0"/>
 *                   &lt;element ref="{http://xmlns.heb.com/ei/G3}G3_DEPT_REC" minOccurs="0"/>
 *                   &lt;element ref="{http://xmlns.heb.com/ei/G3}G3_CONSM_UNT_REC" minOccurs="0"/>
 *                   &lt;element ref="{http://xmlns.heb.com/ei/G3}G3_PROD_REC" minOccurs="0"/>
 *                   &lt;element ref="{http://xmlns.heb.com/ei/G3}G3_PK_REC" minOccurs="0"/>
 *                   &lt;element ref="{http://xmlns.heb.com/ei/G3}G3_SPLR_PACK_REC" minOccurs="0"/>
 *                   &lt;element ref="{http://xmlns.heb.com/ei/CDS_SCALE_REC}SCALE_REC" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "g3HDRREC",
    "g3COMP"
})
@XmlRootElement(name = "G3_HOST_LAYOUT", namespace = "http://xmlns.heb.com/ei/G3")
public class G3HOSTLAYOUT {

    @XmlElement(name = "G3_HDR_REC", namespace = "http://xmlns.heb.com/ei/G3")
    protected G3HDRREC g3HDRREC;
    @XmlElement(name = "G3_COMP", namespace = "http://xmlns.heb.com/ei/G3")
    protected List<G3COMP> g3COMP;

    /**
     * Gets the value of the g3HDRREC property.
     * 
     * @return
     *     possible object is
     *     {@link G3HDRREC }
     *     
     */
    public G3HDRREC getG3HDRREC() {
        return g3HDRREC;
    }

    /**
     * Sets the value of the g3HDRREC property.
     * 
     * @param value
     *     allowed object is
     *     {@link G3HDRREC }
     *     
     */
    public void setG3HDRREC(G3HDRREC value) {
        this.g3HDRREC = value;
    }

    /**
     * Gets the value of the g3COMP property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the g3COMP property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getG3COMP().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link G3COMP }
     * 
     * 
     */
    public List<G3COMP> getG3COMP() {
        if (g3COMP == null) {
            g3COMP = new ArrayList<G3COMP>();
        }
        return this.g3COMP;
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
     *         &lt;element ref="{http://xmlns.heb.com/ei/G3}G3_VNDR_REC" minOccurs="0"/>
     *         &lt;element ref="{http://xmlns.heb.com/ei/G3}G3_DEPT_REC" minOccurs="0"/>
     *         &lt;element ref="{http://xmlns.heb.com/ei/G3}G3_CONSM_UNT_REC" minOccurs="0"/>
     *         &lt;element ref="{http://xmlns.heb.com/ei/G3}G3_PROD_REC" minOccurs="0"/>
     *         &lt;element ref="{http://xmlns.heb.com/ei/G3}G3_PK_REC" minOccurs="0"/>
     *         &lt;element ref="{http://xmlns.heb.com/ei/G3}G3_SPLR_PACK_REC" minOccurs="0"/>
     *         &lt;element ref="{http://xmlns.heb.com/ei/CDS_SCALE_REC}SCALE_REC" minOccurs="0"/>
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
        "g3VNDRREC",
        "g3DEPTREC",
        "g3CONSMUNTREC",
        "g3PRODREC",
        "g3PKREC",
        "g3SPLRPACKREC",
        "scalerec"
    })
    public static class G3COMP {

        @XmlElement(name = "G3_VNDR_REC", namespace = "http://xmlns.heb.com/ei/G3")
        protected G3VNDRREC g3VNDRREC;
        @XmlElement(name = "G3_DEPT_REC", namespace = "http://xmlns.heb.com/ei/G3")
        protected G3DEPTREC g3DEPTREC;
        @XmlElement(name = "G3_CONSM_UNT_REC", namespace = "http://xmlns.heb.com/ei/G3")
        protected G3CONSMUNTREC g3CONSMUNTREC;
        @XmlElement(name = "G3_PROD_REC", namespace = "http://xmlns.heb.com/ei/G3")
        protected G3PRODREC g3PRODREC;
        @XmlElement(name = "G3_PK_REC", namespace = "http://xmlns.heb.com/ei/G3")
        protected G3PKREC g3PKREC;
        @XmlElement(name = "G3_SPLR_PACK_REC", namespace = "http://xmlns.heb.com/ei/G3")
        protected G3SPLRPACKREC g3SPLRPACKREC;
        @XmlElement(name = "SCALE_REC", namespace = "http://xmlns.heb.com/ei/CDS_SCALE_REC")
        protected SCALEREC scalerec;

        /**
         * Gets the value of the g3VNDRREC property.
         * 
         * @return
         *     possible object is
         *     {@link G3VNDRREC }
         *     
         */
        public G3VNDRREC getG3VNDRREC() {
            return g3VNDRREC;
        }

        /**
         * Sets the value of the g3VNDRREC property.
         * 
         * @param value
         *     allowed object is
         *     {@link G3VNDRREC }
         *     
         */
        public void setG3VNDRREC(G3VNDRREC value) {
            this.g3VNDRREC = value;
        }

        /**
         * Gets the value of the g3DEPTREC property.
         * 
         * @return
         *     possible object is
         *     {@link G3DEPTREC }
         *     
         */
        public G3DEPTREC getG3DEPTREC() {
            return g3DEPTREC;
        }

        /**
         * Sets the value of the g3DEPTREC property.
         * 
         * @param value
         *     allowed object is
         *     {@link G3DEPTREC }
         *     
         */
        public void setG3DEPTREC(G3DEPTREC value) {
            this.g3DEPTREC = value;
        }

        /**
         * Gets the value of the g3CONSMUNTREC property.
         * 
         * @return
         *     possible object is
         *     {@link G3CONSMUNTREC }
         *     
         */
        public G3CONSMUNTREC getG3CONSMUNTREC() {
            return g3CONSMUNTREC;
        }

        /**
         * Sets the value of the g3CONSMUNTREC property.
         * 
         * @param value
         *     allowed object is
         *     {@link G3CONSMUNTREC }
         *     
         */
        public void setG3CONSMUNTREC(G3CONSMUNTREC value) {
            this.g3CONSMUNTREC = value;
        }

        /**
         * Gets the value of the g3PRODREC property.
         * 
         * @return
         *     possible object is
         *     {@link G3PRODREC }
         *     
         */
        public G3PRODREC getG3PRODREC() {
            return g3PRODREC;
        }

        /**
         * Sets the value of the g3PRODREC property.
         * 
         * @param value
         *     allowed object is
         *     {@link G3PRODREC }
         *     
         */
        public void setG3PRODREC(G3PRODREC value) {
            this.g3PRODREC = value;
        }

        /**
         * Gets the value of the g3PKREC property.
         * 
         * @return
         *     possible object is
         *     {@link G3PKREC }
         *     
         */
        public G3PKREC getG3PKREC() {
            return g3PKREC;
        }

        /**
         * Sets the value of the g3PKREC property.
         * 
         * @param value
         *     allowed object is
         *     {@link G3PKREC }
         *     
         */
        public void setG3PKREC(G3PKREC value) {
            this.g3PKREC = value;
        }

        /**
         * Gets the value of the g3SPLRPACKREC property.
         * 
         * @return
         *     possible object is
         *     {@link G3SPLRPACKREC }
         *     
         */
        public G3SPLRPACKREC getG3SPLRPACKREC() {
            return g3SPLRPACKREC;
        }

        /**
         * Sets the value of the g3SPLRPACKREC property.
         * 
         * @param value
         *     allowed object is
         *     {@link G3SPLRPACKREC }
         *     
         */
        public void setG3SPLRPACKREC(G3SPLRPACKREC value) {
            this.g3SPLRPACKREC = value;
        }

        /**
         * Gets the value of the scalerec property.
         * 
         * @return
         *     possible object is
         *     {@link SCALEREC }
         *     
         */
        public SCALEREC getSCALEREC() {
            return scalerec;
        }

        /**
         * Sets the value of the scalerec property.
         * 
         * @param value
         *     allowed object is
         *     {@link SCALEREC }
         *     
         */
        public void setSCALEREC(SCALEREC value) {
            this.scalerec = value;
        }

    }

}
