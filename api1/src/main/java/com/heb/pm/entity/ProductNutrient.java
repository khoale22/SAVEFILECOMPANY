package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a dynamic attribute of a ProductNutrient.
 *
 * @author vn73545
 * @since 2.1.0
 */
@Entity
@Table(name = "nutrient")
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductNutrient implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProductNutrientKey key;

	@Column(name = "ntrnt_qty")
	private Double nutrientQuantity;

	@Column(name = "srvng_sz_uom_cd")
	private String servingSizeUomCode;

	@Column(name = "daly_val_srvng_pct")
	private Double dalyValSrvngPct;
	
	@Column(name = "dclr_on_lbl_sw")
	@Type(type="fixedLengthCharPK")
	private String dclrOnLblSw;

	@Column(name="cre8_ts")
	private LocalDateTime createDate;

	@Column(name="lst_updt_ts")
	private LocalDateTime lastUpdateDate;

//	@Column(name = "ntrnt_measr_txt")
//	private String ntrnt_measr_txt;
//	@Column(name = "cre8_id")
//	private String cre8_id;
//	@Column(name = "lst_updt_uid")
//	private String lst_updt_uid;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ntrnt_mst_id", referencedColumnName = "ntrnt_mst_id", insertable = false, updatable = false, nullable = false)
	private NutrientMaster nutrientMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "srvng_sz_uom_cd", referencedColumnName = "srvng_sz_uom_cd", insertable = false, updatable = false, nullable = false)
	private ServingSizeUOM servingSizeUOM;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="src_system_id", referencedColumnName = "src_system_id", insertable = false, updatable = false)
    })
    private SourceSystem sourceSystem;

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
	 * @return the dclrOnLblSw
	 */
	public String getDclrOnLblSw() {
		return dclrOnLblSw;
	}

	/**
	 * @param dclrOnLblSw the dclrOnLblSw to set
	 */
	public void setDclrOnLblSw(String dclrOnLblSw) {
		this.dclrOnLblSw = dclrOnLblSw;
	}

	/**
	 * @return the servingSizeUOM
	 */
	public ServingSizeUOM getServingSizeUOM() {
		return servingSizeUOM;
	}

	/**
	 * @param servingSizeUOM the servingSizeUOM to set
	 */
	public void setServingSizeUOM(ServingSizeUOM servingSizeUOM) {
		this.servingSizeUOM = servingSizeUOM;
	}

	/**
	 * @return the key
	 */
	public ProductNutrientKey getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(ProductNutrientKey key) {
		this.key = key;
	}

	/**
	 * @return the nutrientQuantity
	 */
	public Double getNutrientQuantity() {
		return nutrientQuantity;
	}

	/**
	 * @param nutrientQuantity the nutrientQuantity to set
	 */
	public void setNutrientQuantity(Double nutrientQuantity) {
		this.nutrientQuantity = nutrientQuantity;
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
	 * @return the dalyValSrvngPct
	 */
	public Double getDalyValSrvngPct() {
		return dalyValSrvngPct;
	}

	/**
	 * @param dalyValSrvngPct the dalyValSrvngPct to set
	 */
	public void setDalyValSrvngPct(Double dalyValSrvngPct) {
		this.dalyValSrvngPct = dalyValSrvngPct;
	}

	/**
	 * @return the nutrientMaster
	 */
	public NutrientMaster getNutrientMaster() {
		return nutrientMaster;
	}

	/**
	 * @param nutrientMaster the nutrientMaster to set
	 */
	public void setNutrientMaster(NutrientMaster nutrientMaster) {
		this.nutrientMaster = nutrientMaster;
	}
}


