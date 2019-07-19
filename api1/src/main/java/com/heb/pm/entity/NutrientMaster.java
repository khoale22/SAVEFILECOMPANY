package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a dynamic attribute of a Nutrient Master.
 *
 * @author vn73545
 * @since 2.1.0
 */
@Entity
@Table(name = "nutrient_master")
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class NutrientMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ntrnt_mst_id")
	private Integer masterId;

	@Column(name = "ntrnt_nm")
	@Type(type="fixedLengthCharPK")
	private String nutrientName;

	@Column(name = "src_system_id")
	private Integer sourceSystem;
	
	@Column(name = "SEQ_NBR")
	private Integer sequence;
	
//	private String std_Ntrnt_Sw;
//	private String xtrnl_Id;
//	private String ntrnt_Txt;
//	private String ntrnt_Typ_Cd;

	@JsonIgnoreProperties("nutrientMaster")
	@OneToMany(mappedBy = "nutrientMaster", fetch = FetchType.LAZY)
	private List<ProductNutrient> productNutrients;

	/**
	 * @return the masterId
	 */
	public Integer getMasterId() {
		return masterId;
	}

	/**
	 * @param masterId the masterId to set
	 */
	public void setMasterId(Integer masterId) {
		this.masterId = masterId;
	}

	/**
	 * @return the nutrientName
	 */
	public String getNutrientName() {
		return nutrientName;
	}

	/**
	 * @param nutrientName the nutrientName to set
	 */
	public void setNutrientName(String nutrientName) {
		this.nutrientName = nutrientName;
	}

	/**
	 * @return the sourceSystem
	 */
	public Integer getSourceSystem() {
		return sourceSystem;
	}

	/**
	 * @param sourceSystem the sourceSystem to set
	 */
	public void setSourceSystem(Integer sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	/**
	 * @return the productNutrients
	 */
	public List<ProductNutrient> getProductNutrients() {
		return productNutrients;
	}

	/**
	 * @param productNutrients the productNutrients to set
	 */
	public void setProductNutrients(List<ProductNutrient> productNutrients) {
		this.productNutrients = productNutrients;
	}

	/**
	 * Get the sequence.
	 *
	 * @return the sequence
	 */
	public Integer getSequence() {
		return sequence;
	}

	/**
	 * Set the sequence.
	 *
	 * @param sequence the sequence to set
	 */
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}


