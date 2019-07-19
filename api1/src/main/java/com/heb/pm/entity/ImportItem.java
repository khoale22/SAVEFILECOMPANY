/*
 *  ImportItem
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents item/vendor information for import.
 *
 * @author s573181
 * @since 2.5.0
 */
@Entity
@Table(name = "vend_loc_imprt_itm")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ImportItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s[%d]";

	@EmbeddedId
	private ImportItemKey key;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="vend_loc_typ_cd", referencedColumnName = "loc_typ_cd", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="vend_loc_nbr", referencedColumnName = "loc_nbr", insertable = false, updatable = false, nullable = false)
	})
	private Location location;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name="itm_key_typ_cd", referencedColumnName = "itm_key_typ_cd", insertable = false, updatable = false),
			@JoinColumn(name="itm_id", referencedColumnName = "itm_id", insertable = false, updatable = false),
			@JoinColumn(name="vend_loc_typ_cd", referencedColumnName = "vend_loc_typ_cd", insertable = false, updatable = false),
			@JoinColumn(name="vend_loc_nbr", referencedColumnName = "vend_loc_nbr", insertable = false, updatable = false)
	})
	private List<VendorItemFactory> vendorItemFactory;

	@Column(name ="hts_nbr")
	private Long hts1;

	@Column(name ="hts2_nbr")
	private Long hts2;

	@Column(name ="hts3_nbr")
	private Long hts3;

	@Column(name ="prodn_min_ord_des")
	@Type(type="fixedLengthChar")
	private String minOrderDescription;

	@Column(name ="prodn_min_ord_qty")
	private Long minOrderQuantity;

	@Column(name ="pckup_pnt_txt")
	@Type(type="fixedLengthChar")
	private String pickupPoint;

	@Column(name ="cntry_of_orig_nm")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")  
	private String countryOfOrigin;

	@Column(name ="cntan_sz_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")  
	private String containerSizeCode;

	@Column(name ="inco_trm_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")  
	private String incoTermCode;

	@Column(name ="pror_dt")
	private LocalDate prorationDate;

	@Column(name ="in_str_dt")
	private LocalDate inStoreDate;

	@Column(name ="whse_flsh_dt")
	private LocalDate warehouseFlushDate;

	@Column(name ="seasn_txt")
	@Type(type="fixedLengthChar")
	private String season;

	@Column(name = "sell_yy")
	private Integer sellByYear;

	@Column(name = "color_des")
	@Type(type="fixedLengthChar")  
	private String color;

	@Column(name = "crtn_mrkng_txt")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")  
	private String cartonMarking;

	@Column(name = "agent_comsn_pct")
	private Double agentCommissionPercent;

	@Column(name = "duty_rt_pct")
	private double dutyPercent;

	@Column(name = "duty_info_txt")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")  
	private String dutyInformation;

	@Column(name = "duty_cnfrm_txt")
	private String dutyConfirmationDate;

	@Column(name = "frt_cnfrm_txt")
	private String freightConfirmationDate;
	@Column(name="IMPORT_UPDT_TS")
	private LocalDateTime importUpdtTs;

	@Column(name="IMPORT_UPDT_USR")
	private String importUpdtUsr;


	/**
	 * Returns the ImportItemKey.
	 *
	 * @return The ImportItemKey.
	 */
	public ImportItemKey getKey() {
		return key;
	}

	/**
	 * Sets the ImportItemKey.
	 *
	 * @param key The ImportItemKey.
	 */
	public void setKey(ImportItemKey key) {
		this.key = key;
	}

	/**
	 * Returns the hts1.
	 *
	 * @return The hts1.
	 */
	public Long getHts1() {
		return hts1;
	}

	/**
	 * Sets the hts1.
	 *
	 * @param hts1 The hts1.
	 */
	public void setHts1(long hts1) {
		this.hts1 = hts1;
	}

	/**
	 * Returns the hts2.
	 *
	 * @return The hts2.
	 */
	public Long getHts2() {
		return hts2;
	}

	/**
	 * Sets the hts2.
	 *
	 * @param hts2 The hts2.
	 */
	public void setHts2(long hts2) {
		this.hts2 = hts2;
	}

	/**
	 * Returns the hts3.
	 *
	 * @return The hts3.
	 */
	public Long getHts3() {
		return hts3;
	}

	/**
	 * Sets the hts3.
	 *
	 * @param hts3 The hts3.
	 */
	public void setHts3(long hts3) {
		this.hts3 = hts3;
	}

	/**
	 * Returns the minOrderDescription.
	 *
	 * @return The minOrderDescription.
	 */
	public String getMinOrderDescription() {
		return minOrderDescription;
	}

	/**
	 * Sets the minOrderDescription.
	 *
	 * @param minOrderDescription The minOrderDescription.
	 */
	public void setMinOrderDescription(String minOrderDescription) {
		this.minOrderDescription = minOrderDescription;
	}

	/**
	 * Returns the minOrderQuantity.
	 *
	 * @return The minOrderQuantity.
	 */
	public Long getMinOrderQuantity() {
		return minOrderQuantity;
	}

	/**
	 * Sets the minOrderQuantity.
	 *
	 * @param minOrderQuantity The minOrderQuantity.
	 */
	public void setMinOrderQuantity(long minOrderQuantity) {
		this.minOrderQuantity = minOrderQuantity;
	}

	/**
	 * Returns the pickupPoint.
	 *
	 * @return The pickupPoint.
	 */
	public String getPickupPoint() {
		return pickupPoint;
	}

	/**
	 * Sets the pickupPoint.
	 *
	 * @param pickupPoint The pickupPoint.
	 */
	public void setPickupPoint(String pickupPoint) {
		this.pickupPoint = pickupPoint;
	}

	/**
	 * Returns the countryOfOrigin.
	 *
	 * @return The countryOfOrigin.
	 */
	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}

	/**
	 * Sets the countryOfOrigin.
	 *
	 * @param countryOfOrigin The countryOfOrigin.
	 */
	public void setCountryOfOrigin(String countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}

	/**
	 * Returns the containerSizeCode.
	 *
	 * @return The containerSizeCode.
	 */
	public String getContainerSizeCode() {
		return containerSizeCode;
	}

	/**
	 * Sets the containerSizeCode.
	 *
	 * @param containerSizeCode The containerSizeCode.
	 */
	public void setContainerSizeCode(String containerSizeCode) {
		this.containerSizeCode = containerSizeCode;
	}

	/**
	 * Returns the incoTermCode.
	 *
	 * @return The incoTermCode.
	 */
	public String getIncoTermCode() {
		return incoTermCode;
	}

	/**
	 * Sets the incoTermCode.
	 *
	 * @param incoTermCode The incoTermCode.
	 */
	public void setIncoTermCode(String incoTermCode) {
		this.incoTermCode = incoTermCode;
	}

	/**
	 * Returns the prorationDate.
	 *
	 * @return The prorationDate.
	 */
	public LocalDate getProrationDate() {
		return prorationDate;
	}

	/**
	 * Sets the prorationDate.
	 *
	 * @param prorationDate The prorationDate.
	 */
	public void setProrationDate(LocalDate prorationDate) {
		this.prorationDate = prorationDate;
	}

	/**
	 * Returns the inStoreDate.
	 *
	 * @return The inStoreDate.
	 */
	public LocalDate getInStoreDate() {
		return inStoreDate;
	}

	/**
	 * Sets the inStoreDate.
	 *
	 * @param inStoreDate The inStoreDate.
	 */
	public void setInStoreDate(LocalDate inStoreDate) {
		this.inStoreDate = inStoreDate;
	}

	/**
	 * Returns the warehouseFlushDate.
	 *
	 * @return The warehouseFlushDate.
	 */
	public LocalDate getWarehouseFlushDate() {
		return warehouseFlushDate;
	}

	/**
	 * Sets the warehouseFlushDate.
	 *
	 * @param warehouseFlushDate The warehouseFlushDate.
	 */
	public void setWarehouseFlushDate(LocalDate warehouseFlushDate) {
		this.warehouseFlushDate = warehouseFlushDate;
	}

	/**
	 * Returns the season.
	 *
	 * @return The season.
	 */
	public String getSeason() {
		return season;
	}

	/**
	 * Sets the season.
	 *
	 * @param season The season.
	 */
	public void setSeason(String season) {
		this.season = season;
	}

	/**
	 * Returns the sellByYear.
	 *
	 * @return The sellByYear.
	 */
	public Integer getSellByYear() {
		return sellByYear;
	}

	/**
	 * Sets the sellByYear.
	 *
	 * @param sellByYear The sellByYear.
	 */
	public void setSellByYear(int sellByYear) {
		this.sellByYear = sellByYear;
	}

	/**
	 * Returns the color.
	 *
	 * @return The color.
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Sets the color.
	 *
	 * @param color The color.
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Returns the cartonMarking.
	 *
	 * @return The cartonMarking.
	 */
	public String getCartonMarking() {
		return cartonMarking;
	}

	/**
	 * Sets the cartonMarking.
	 *
	 * @param cartonMarking The cartonMarking.
	 */
	public void setCartonMarking(String cartonMarking) {
		this.cartonMarking = cartonMarking;
	}

	/**
	 * Returns the agentCommissionPercent.
	 *
	 * @return The agentCommissionPercent.
	 */
	public Double getAgentCommissionPercent() {
		return agentCommissionPercent;
	}

	/**
	 * Sets the agentCommissionPercent.
	 *
	 * @param agentCommissionPercent The agentCommissionPercent.
	 */
	public void setAgentCommissionPercent(double agentCommissionPercent) {
		this.agentCommissionPercent = agentCommissionPercent;
	}

	/**
	 * Returns the dutyPercent.
	 *
	 * @return The dutyPercent.
	 */
	public Double getDutyPercent() {
		return dutyPercent;
	}

	/**
	 * Sets the dutyPercent.
	 *
	 * @param dutyPercent The dutyPercent.
	 */
	public void setDutyPercent(double dutyPercent) {
		this.dutyPercent = dutyPercent;
	}

	/**
	 * Returns the dutyInformation.
	 *
	 * @return The dutyInformation.
	 */
	public String getDutyInformation() {
		return dutyInformation;
	}

	/**
	 * Sets the dutyInformation.
	 *
	 * @param dutyInformation The dutyInformation.
	 */
	public void setDutyInformation(String dutyInformation) {
		this.dutyInformation = dutyInformation;
	}

	/**
	 * Returns the dutyConfirmationDate.
	 *
	 * @return The dutyConfirmationDate.
	 */
	public String getDutyConfirmationDate() {
		return dutyConfirmationDate;
	}

	/**
	 * Sets the dutyConfirmationDate.
	 *
	 * @param dutyConfirmationDate The dutyConfirmationDate.
	 */
	public void setDutyConfirmationDate(String dutyConfirmationDate) {
		this.dutyConfirmationDate = dutyConfirmationDate;
	}

	/**
	 * Returns the freightConfirmationDate.
	 *
	 * @return The freightConfirmationDate.
	 */
	public String getFreightConfirmationDate() {
		return freightConfirmationDate;
	}

	/**
	 * Sets the freightConfirmationDate.
	 *
	 * @param freightConfirmationDate The freightConfirmationDate.
	 */
	public void setFreightConfirmationDate(String freightConfirmationDate) {
		this.freightConfirmationDate = freightConfirmationDate;
	}

	/**
	 * Returns the class as it should be displayed on the GUI.
	 *
	 * @return A String representation of the class as it is meant to be displayed on the GUI.
	 */
	public String getDisplayName() {
		return String.format(ImportItem.DISPLAY_NAME_FORMAT, this.getLocation().getLocationName().trim(),
				this.getKey().getVendorNumber());
	}

	/**
	 * Returns the location object.
	 *
	 * @return The location object.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets the location object.
	 *
	 * @param location The location object.
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * Returns the vendorItemFactory.
	 *
	 * @return the vendorItemFactory .
	 */
	public List<VendorItemFactory> getVendorItemFactory() {
		return vendorItemFactory;
	}

	/**
	 * Sets the vendorItemFactory.
	 * 
	 * @param vendorItemFactory The vendorItemFactory.
	 */
	public void setVendorItemFactory(List<VendorItemFactory> vendorItemFactory) {
		this.vendorItemFactory = vendorItemFactory;
	}
	public LocalDateTime getImportUpdtTs() {
		return importUpdtTs;
	}

	public void setImportUpdtTs(LocalDateTime importUpdtTs) {
		this.importUpdtTs = importUpdtTs;
	}

	public String getImportUpdtUsr() {
		return importUpdtUsr;
	}

	public void setImportUpdtUsr(String importUpdtUsr) {
		this.importUpdtUsr = importUpdtUsr;
	}
	/**
	 * Compares another object to this one. If that object is a ImportItem, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ImportItem that = (ImportItem) o;

		return this.key != null ? this.key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return this.key != null ? this.key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ImportItem{" +
				"key=" + key +
				", hts1=" + hts1 +
				", hts2=" + hts2 +
				", hts3=" + hts3 +
				", minOrderDescription='" + minOrderDescription + '\'' +
				", minOrderQuantity=" + minOrderQuantity +
				", pickupPoint='" + pickupPoint + '\'' +
				", countryOfOrigin='" + countryOfOrigin + '\'' +
				", containerSizeCode='" + containerSizeCode + '\'' +
				", incoTermCode='" + incoTermCode + '\'' +
				", prorationDate=" + prorationDate +
				", inStoreDate=" + inStoreDate +
				", warehouseFlushDate=" + warehouseFlushDate +
				", season='" + season + '\'' +
				", sellByYear=" + sellByYear +
				", color='" + color + '\'' +
				", cartonMarking='" + cartonMarking + '\'' +
				", agentCommissionPercent=" + agentCommissionPercent +
				", dutyPercent=" + dutyPercent +
				", dutyInformation='" + dutyInformation + '\'' +
				", dutyConfirmationDate=" + dutyConfirmationDate +
				", freightConfirmationDate=" + freightConfirmationDate +
				'}';
	}
}

