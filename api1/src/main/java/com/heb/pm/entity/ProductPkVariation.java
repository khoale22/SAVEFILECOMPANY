/*
 *  ProductPkVariation
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * The persistent class for the prod_pk_variation database table.
 * @author vn73545
 * @since 2.12
 */
@Entity
@Table(name="prod_pk_variation")
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductPkVariation implements Serializable{
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProductPkVariationKey key;

	@Column(name="prod_val_des")
	@Type(type="fixedLengthCharPK")
	private String productValueDescription;
	
	@Column(name="srvng_sz_qty")
	private Double servingSizeQuantity;
	
	@Column(name="srvng_sz_uom_cd")
	private String servingSizeUomCode;

	@Column(name="prod_pan_typ_cd")
	private String panelTypeCode;

	@Column(name="cre8_ts")
	private LocalDateTime createDate;

	@Column(name="lst_updt_ts")
	private LocalDateTime lastUpdateDate;

	@Column(name="spcr_txt")
	@Type(type="fixedLengthCharPK")
	private String servingsPerContainerText;
	
	@Column(name="hshld_srvng_sz_txt")
	@Type(type="fixedLengthCharPK")
	private String houseHoldMeasurement;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="src_system_id", referencedColumnName = "src_system_id", insertable = false, updatable = false)
    })
    private SourceSystem sourceSystem;

	/*
	private String min_spcr_qty;
	private String max_spcr_qty;
	private String preprd_prod_sw;
	private String cre8_id;
	private String lst_updt_uid;
	private String ntrnt_pan_nbr;
	private String clrs_qty;
	*/

	/**
	 * @return the key
	 */
	public ProductPkVariationKey getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(ProductPkVariationKey key) {
		this.key = key;
	}

	/**
	 * @return the panelTypeCode
	 */
	public String getPanelTypeCode() {
		return panelTypeCode;
	}

	/**
	 * @param panelTypeCode the panelTypeCode to set
	 */
	public void setPanelTypeCode(String panelTypeCode) {
		this.panelTypeCode = panelTypeCode;
	}

	/**
	 * @return the lastUpdateDate
	 */
	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	/**
	 * @param lastUpdateDate the lastUpdateDate to set
	 */
	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	/**
	 * @return the createDate
	 */
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the sourceSystem
	 */
	public SourceSystem getSourceSystem() {
		return sourceSystem;
	}

	/**
	 * @param sourceSystem the sourceSystem to set
	 */
	public void setSourceSystem(SourceSystem sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	/**
	 * @return the productValueDescription
	 */
	public String getProductValueDescription() {
		return productValueDescription;
	}

	/**
	 * @param productValueDescription the productValueDescription to set
	 */
	public void setProductValueDescription(String productValueDescription) {
		this.productValueDescription = productValueDescription;
	}

	/**
	 * @return the servingSizeQuantity
	 */
	public Double getServingSizeQuantity() {
		return servingSizeQuantity;
	}

	/**
	 * @param servingSizeQuantity the servingSizeQuantity to set
	 */
	public void setServingSizeQuantity(Double servingSizeQuantity) {
		this.servingSizeQuantity = servingSizeQuantity;
	}

	/**
	 * @return the servingSizeUomCode
	 */
	public String getServingSizeUomCode() {
		return servingSizeUomCode;
	}

	/**
	 * @param servingSizeUomCode the servingSizeUomCode to set
	 */
	public void setServingSizeUomCode(String servingSizeUomCode) {
		this.servingSizeUomCode = servingSizeUomCode;
	}

	/**
	 * @return the servingsPerContainerText
	 */
	public String getServingsPerContainerText() {
		return servingsPerContainerText;
	}

	/**
	 * @param servingsPerContainerText the servingsPerContainerText to set
	 */
	public void setServingsPerContainerText(String servingsPerContainerText) {
		this.servingsPerContainerText = servingsPerContainerText;
	}

	/**
	 * @return the houseHoldMeasurement
	 */
	public String getHouseHoldMeasurement() {
		return houseHoldMeasurement;
	}

	/**
	 * @param houseHoldMeasurement the houseHoldMeasurement to set
	 */
	public void setHouseHoldMeasurement(String houseHoldMeasurement) {
		this.houseHoldMeasurement = houseHoldMeasurement;
	}
}
