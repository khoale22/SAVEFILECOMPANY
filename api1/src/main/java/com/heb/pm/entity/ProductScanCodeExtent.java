package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

/**
 * Represents a product scan code extent data that is extended attributes for UPCs.
 *
 * @author m594201
 * @since 2.7.0
 */
@Entity
@Table(name = "prod_scn_cd_extent")
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductScanCodeExtent implements Serializable {

	//Gladson data dimensions constants
	private static final String DEPTH_DIMENSION_CODE = "DEPTH";
	private static final String HEIGH_DIMENSION_CODE = "HEIGH";
	private static final String WEIGH_DIMENSION_CODE = "WEIGH";
	private static final String WIDTH_DIMENSION_CODE = "WIDTH";
	public static final String BRAND_DIMENSION_CODE = "BRAND";
	public static final String PLINE_DIMENSION_CODE = "PLINE";
	public static final String IDESC_DIMENSION_CODE = "IDESC";
	public static final String ESHRT_DIMENSION_CODE = "ESHRT";
	public static final String ESIZE_DIMENSION_CODE = "ESIZE";
	public static final String ISIZE_DIMENSION_CODE = "ISIZE";
	public static final String IUOM_DIMENSION_CODE = "IUOM ";
	public static final String PDETL_PRODUCT_DESCRIPTION_CODE = "PDETL";
	public static final String INDCN_PRODUCT_DESCRIPTION_CODE = "INDCN";
	public static final String ELONG_PRODUCT_DESCRIPTION_CODE = "ELONG";
	/**
	 * PROD EXT DTA CD for favor item description .
	 */
	public static final String FAVOR_ITEM_DESCRIPTION_CODE = "MBDES";
	public static final String INGREDIENTS_CODE = "INGRE";
	public static final String GUARANTEED_CODE = "GRNTD";
	public static final String DIRECTION_CODE = "DIRCT";
	public static final String WARNING_CODE = "WARN";
	private static final int PRIME_NUMBER = 31;
	private static final int FOUR_BYTES = 32;

	@EmbeddedId
	private ProductScanCodeExtentKey key;

	@Column(name = "prod_des_txt")
	@Type(type="fixedLengthCharPK")
	private String prodDescriptionText;
	
	@Column(name = "src_system_id")
	private Integer sourceSystem;

	@Column(name = "LST_UPDT_TS")
	private LocalDate lstUpdtTs;

	@Column(name = "LST_UPDT_UID")
	@Type(type="fixedLengthCharPK")
	private String lastUpdateUserId;

	@Column(name = "CRE8_ID")
	@Type(type="fixedLengthCharPK")
	private String createUserId;
	
	@Transient
	private List<String> dimensionDataCodes = new ArrayList<>(Arrays.asList(DEPTH_DIMENSION_CODE, HEIGH_DIMENSION_CODE, WEIGH_DIMENSION_CODE, WIDTH_DIMENSION_CODE));

	/**
	 * Get the lastUpdateUserId.
	 *
	 * @return the lastUpdateUserId
	 */
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	/**
	 * Set the lastUpdateUserId.
	 *
	 * @param lastUpdateUserId the lastUpdateUserId to set
	 */
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	/**
	 * Get the createUserId.
	 *
	 * @return the createUserId
	 */
	public String getCreateUserId() {
		return createUserId;
	}

	/**
	 * Set the createUserId.
	 *
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
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
	 * Gets dimension data codes corresponding to the Gladson dimensional data to pull.
	 *
	 * @return the dimension data codes corresponding to the Gladson dimensional data to pull.
	 */
	public List<String> getDimensionDataCodes() {
		return dimensionDataCodes;
	}

	/**
	 * Gets the key composite key to represent the primary keys for the prod_scn_cd_extent table.
	 *
	 * @return the key composite key to represent the primary keys for the prod_scn_cd_extent table.
	 */
	public ProductScanCodeExtentKey getKey() {
		return key;
	}

	/**
	 * Sets the key composite key to represent the primary keys for the prod_scn_cd_extent table.
	 *
	 * @param key the key composite key to represent the primary keys for the prod_scn_cd_extent table.
	 */
	public void setKey(ProductScanCodeExtentKey key) {
		this.key = key;
	}

	/**
	 * Gets prod description text. That holds values tied to the prod_ext_dta_cd within the key.
	 *
	 * @return the prod description text that holds values tied to the prod_ext_dta_cd within the key.
	 */
	public String getProdDescriptionText() {
		return prodDescriptionText;
	}

	/**
	 * Sets prod description text that holds values tied to the prod_ext_dta_cd within the key.
	 *
	 * @param prodDescriptionText the prod description text that holds values tied to the prod_ext_dta_cd within the key.
	 */
	public void setProdDescriptionText(String prodDescriptionText) {
		this.prodDescriptionText = prodDescriptionText;
	}

	/**
	 * Sets the lstUpdtTs
	 */
	public LocalDate getLstUpdtTs() {
		return lstUpdtTs;
	}

	/**
	 * @return Gets the value of lstUpdtTs and returns lstUpdtTs
	 */
	public void setLstUpdtTs(LocalDate lstUpdtTs) {
		this.lstUpdtTs = lstUpdtTs;
	}

	/**
	 * Compares another object to this one. If that object is an ProductScanCodeExtent, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductScanCodeExtent)) return false;
		ProductScanCodeExtent that = (ProductScanCodeExtent) o;

		return key == that.key;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return this.key != null ? this.key.hashCode() : 0;
	}
}
