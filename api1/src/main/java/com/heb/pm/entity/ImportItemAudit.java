package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *@author s753601
 * @version 2.7.0
 */
@Entity
@Table(name = "VEND_LOC_IMPRT_AUD")
public class ImportItemAudit implements Serializable, Audit{

	private static final long serialVersionUID = 1L;
	private static final String DISPLAY_NAME_DESCRIPTION_WITH_ID = "%s[%s]";

	@EmbeddedId
	ImportItemAuditKey key;

	@Column(name = "ACT_CD")
	private String action;

	@AuditableField(displayName = "Proration Date")
	@Column(name = "PROR_DT")
	private LocalDate prorationDate;

	@AuditableField(displayName = "In Store Date")
	@Column(name = "IN_STR_DT")
	private LocalDate inStoreDate;

	@AuditableField(displayName = "Whs Flush")
	@Column(name = "WHSE_FLSH_DT")
	private LocalDate warehouseFlushDate;

	@AuditableField(displayName = "Sell By")
	@Column(name = "SELL_YY")
	private Integer sellByYear;

	@AuditableField(displayName = "Carton Marking")
	@Column(name = "CRTN_MRKNG_TXT")
	private String cartonMarking;

	@AuditableField(displayName = "Min. Order Quantity")
	@Column(name = "PRODN_MIN_ORD_QTY")
	private Long minOrderQuantity;

	@AuditableField(displayName = "Min. Order Description")
	@Column(name = "PRODN_MIN_ORD_DES")
	private String minOrderDescription;

	@AuditableField(displayName = "Season")
	@Column(name = "SEASN_TXT")
	private String season;

	@AuditableField(displayName = "Container Size")
	@Column(name = "CNTAN_SZ_CD")
	private String containerSizeCode;

	@AuditableField(displayName = "Inco Terms")
	@Column(name = "INCO_TRM_CD")
	private String incoTermCode;


	@AuditableField(displayName = "Pickup Point")
	@Column(name = "PCKUP_PNT_TXT")
	private String pickupPoint;

	@AuditableField(displayName = "HTS1")
	@Column(name = "HTS_NBR")
	private Long hts1;

	@AuditableField(displayName = "HTS2")
	@Column(name = "HTS2_NBR")
	private Long hts2;

	@AuditableField(displayName = "HTS3")
	@Column(name = "HTS3_NBR")
	private Long hts3;

	@AuditableField(displayName = "Country of Origin")
	@Column(name = "CNTRY_OF_ORIG_NM")
	private String countryOfOrigin;

	@AuditableField(displayName = "Duty %")
	@Column(name = "DUTY_RT_PCT")
	private Double dutyPercent;

	@AuditableField(displayName = "Duty Confirmation")
	@Column(name = "DUTY_CNFRM_TXT")
	private String dutyConfirmationDate;

	@AuditableField(displayName = "Freight Confirmed")
	@Column(name = "FRT_CNFRM_TXT")
	private String freightConfirmationDate;

	@AuditableField(displayName = "Agent %")
	@Column(name = "AGENT_COMSN_PCT")
	private Double agentCommissionPercent;

	@AuditableField(displayName = "Color")
	@Column(name = "COLOR_DES")
	private String color;

	@AuditableField(displayName = "Duty Information")
	@Column(name = "DUTY_INFO_TXT")
	private String dutyInformation;

	@Column(name = "IMPORT_UPDT_USR")
	private String changedBy;

	/**
	 * Returns the Key which holds a set of properties that will uniquely identify an import item audit.
	 * @return the key.
	 */
	public ImportItemAuditKey getKey() {
		return key;
	}

	/**
	 * Updates the key
	 * @param key the new key
	 */
	public void setKey(ImportItemAuditKey key) {
		this.key = key;
	}

	/**
	 * A code that describes what action took place in the database examples: 'ADD', 'UPDAT', and 'PURGE'
	 * @return the action code
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Updates the action
	 * @param action the new action
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 *Returns the date when the import product is scheduled to prorate to the store(determined by the Business Planner)
	 * @return prorationDate
	 */
	public LocalDate getProrationDate() {
		return prorationDate;
	}

	/**
	 * Updates the prorationDate
	 * @param prorationDate the new prorationDate
	 */
	public void setProrationDate(LocalDate prorationDate) {
		this.prorationDate = prorationDate;
	}

	/**
	 * Returns the date when import product must be in the stores(determined by the buyer and Business Planner)
	 * @return inStoreDate
	 */
	public LocalDate getInStoreDate() {
		return inStoreDate;
	}

	/**
	 * Updates the inStoreDate
	 * @param inStoreDate the new inStoreDate
	 */
	public void setInStoreDate(LocalDate inStoreDate) {
		this.inStoreDate = inStoreDate;
	}

	/**
	 * Returns the Date HEB will flush out the warehouse
	 * @return warehouseFlushDate
	 */
	public LocalDate getWarehouseFlushDate() {
		return warehouseFlushDate;
	}

	/**
	 * Updates the warehouseFlushDate
	 * @param warehouseFlushDate the new warehouseFlushDate
	 */
	public void setWarehouseFlushDate(LocalDate warehouseFlushDate) {
		this.warehouseFlushDate = warehouseFlushDate;
	}

	/**
	 * Gets the Year the product should sell by.
	 * @return the sellByYear
	 */
	public Integer getSellByYear() {
		return sellByYear;
	}

	/**
	 * Updates the sellByYear
	 * @param sellByYear the new sellByYear
	 */
	public void setSellByYear(Integer sellByYear) {
		this.sellByYear = sellByYear;
	}

	/**
	 * Returns the text value to indicate that type of marketing expected for product.
	 * @return the cartonMarking
	 */
	public String getCartonMarking() {
		return cartonMarking;
	}

	/**
	 * Updates the cartonMarking
	 * @param cartonMarking the new cartonMarking
	 */
	public void setCartonMarking(String cartonMarking) {
		this.cartonMarking = cartonMarking;
	}

	/**
	 * Returns the numeric minimum order quantity for a case pack.
	 * @return minOrderQuantity
	 */
	public Long getMinOrderQuantity() {
		return minOrderQuantity;
	}

	/**
	 * Updates the minOrderQuantity
	 * @param minOrderQuantity the new minOrderQuantity
	 */
	public void setMinOrderQuantity(Long minOrderQuantity) {
		this.minOrderQuantity = minOrderQuantity;
	}

	/**
	 * Returns the minimum order description of a case pack; examples are each, pallet, etc.
	 * @return minOrderDescription
	 */
	public String getMinOrderDescription() {
		return minOrderDescription;
	}

	/**
	 * Updates the minOrderDescription
	 * @param minOrderDescription the new minOrderDescription
	 */
	public void setMinOrderDescription(String minOrderDescription) {
		this.minOrderDescription = minOrderDescription;
	}

	/**
	 * Returns the Seasonality where the product is created for; examples for this are: Christmas, Valentines,
	 * Spring/Summer, Everyday
	 * @return the seaon
	 */
	public String getSeason() {
		return season;
	}

	/**
	 * Updates the season
	 * @param season the new season
	 */
	public void setSeason(String season) {
		this.season = season;
	}

	/**
	 * Values tied to the size of container product is shipped in, examples are: CFS, FCL20, FCL40, FCL40HQ, FCL45
	 * @return containerSizeCode
	 */
	public String getContainerSizeCode() {
		return containerSizeCode;
	}

	/**
	 * Updates the containerSizeCode
	 * @param containerSizeCode the new containerSizeCode
	 */
	public void setContainerSizeCode(String containerSizeCode) {
		this.containerSizeCode = containerSizeCode;
	}

	/**
	 * Returns the Incoming Terminal: Incoterm – Abbreviation for international commercial terminology. An
	 * internationally agreed on terminology for the division of cost, responsibility and liability in international
	 * transactions. Reference International Chamber of Commerce INCOTERMS 2000.
	 * An example is FOB - which is a value through out our environment.  FOB = Free on Board.  At named port of export,
	 * a pricing term indicating that the quoted price covers all expenses up to and including delivery of goods upon
	 * an overseas vessel provided by or for the buyer.
	 * @return incoTermCode
	 */
	public String getIncoTermCode() {
		return incoTermCode;
	}

	/**
	 * Updates the incoTermCode
	 * @param incoTermCode the new incoTermCode
	 */
	public void setIncoTermCode(String incoTermCode) {
		this.incoTermCode = incoTermCode;
	}

	/**
	 * Returns the Port of Origin - usually a city/state/country
	 * @return pickUpPoint
	 */
	public String getPickupPoint() {
		return pickupPoint;
	}

	/**
	 * Updates the pickupPoint
	 * @param pickupPoint the new pickupPoint
	 */
	public void setPickupPoint(String pickupPoint) {
		this.pickupPoint = pickupPoint;
	}

	/**
	 * Returns the Harmonized Tarrif Code: List of tariffs charged for all products imported into the US.  It
	 * classifies all goods according ot the international Harmonized Commodity Coding and Classificaiton System which
	 * as been established by the World Customs Org.  Vitually all countires base their tariff scheudles on this system,
	 * making it easier to conduct international trade.
	 * First  digits are standardized universalized; usually the last 2 - 4 digits are country and/or product specific.
	 * The US Customs enforces and collect the tariff while the US Dept of Commerce sets the rates.
	 * @return hts1
	 */
	public Long getHts1() {
		return hts1;
	}

	/**
	 * Updates the hts1
	 * @param hts1 the new hts1
	 */
	public void setHts1(Long hts1) {
		this.hts1 = hts1;
	}

	/**
	 * Returns the HTS2 for detailed explanation please refer to {@linkgetHts1()}
	 * @return hts2
	 */
	public Long getHts2() {
		return hts2;
	}

	/**
	 * Updates the hts2
	 * @param hts2 the new hts2
	 */
	public void setHts2(Long hts2) {
		this.hts2 = hts2;
	}

	/**
	 * Returns the HTS3 for detailed explanation please refer to {@linkgetHts1()}
	 * @return hts3
	 */
	public Long getHts3() {
		return hts3;
	}

	/**
	 * Updates the hts3
	 * @param hts3 the new hts3
	 */
	public void setHts3(Long hts3) {
		this.hts3 = hts3;
	}

	/**
	 * Returns the Country of Origin - Country only. Where the goods are manufactured or last country where goods were
	 * manufactured.  Special programs may have their own country of origin rules that must be met in order to import
	 * goods under such programs.
	 * @return countryOfOrigin
	 */
	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}

	/**
	 * Updates the countryOfOrigin
	 * @param countryOfOrigin the new countryOfOrigin
	 */
	public void setCountryOfOrigin(String countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}

	/**
	 * Customs Duty percentage as assigned by HS value.
	 * @return the dutyPercent
	 */
	public Double getDutyPercent() {
		return dutyPercent;
	}

	/**
	 * Updates the dutyPercent
	 * @param dutyPercent the new dutyPercent
	 */
	public void setDutyPercent(Double dutyPercent) {
		this.dutyPercent = dutyPercent;
	}

	/**
	 * Returns the Date the duty information was confirmed
	 * @return dutyConfirmationDate
	 */
	public String getDutyConfirmationDate() {
		return dutyConfirmationDate;
	}

	/**
	 * Updates the dutyConfirmationDate
	 * @param dutyConfirmationDate the new dutyConfirmationDate
	 */
	public void setDutyConfirmationDate(String dutyConfirmationDate) {
		this.dutyConfirmationDate = dutyConfirmationDate;
	}

	/**
	 * Returns the date the freight information was confirmed
	 * @return freightConfirmationDate
	 */
	public String getFreightConfirmationDate() {
		return freightConfirmationDate;
	}

	/**
	 * Updates the freightConfirmationDate
	 * @param freightConfirmationDate the new freightConfirmationDate
	 */
	public void setFreightConfirmationDate(String freightConfirmationDate) {
		this.freightConfirmationDate = freightConfirmationDate;
	}

	/**
	 * Returns the Agent Commission percentage (Customs broker overseas)
	 * @return agentCommissionPercent
	 */
	public Double getAgentCommissionPercent() {
		return agentCommissionPercent;
	}

	/**
	 * Updates the agentCommissionPercent
	 * @param agentCommissionPercent the new agentCommissionPercent
	 */
	public void setAgentCommissionPercent(Double agentCommissionPercent) {
		this.agentCommissionPercent = agentCommissionPercent;
	}

	/**
	 * Returns the Main color of the product; examples are red, blue, brown, multi color.
	 * @return color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Updates the color
	 * @param color the new color
	 */
	public void setColor(String color) {
		this.color = color;
	}

	public String getChangedBy(){
		return this.changedBy;
	}

	/**
	 * Returns the userID that changed the record.
	 * @return changedBy
	 */
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	/**
	 * Returns the time stamp for the object being changed.
	 * @return
	 */
	public LocalDateTime getChangedOn() {
		return this.key.getChangedOn();
	}

	public void setChangedOn(LocalDateTime changedOn){
		this.key.setChangedOn(changedOn);
	}


	/**
	 * Returns the Additional details around the Duty, if there is an additional amount of monies due and why, etc.
	 * @return dutyInformation
	 */
	public String getDutyInformation() {
		return dutyInformation;
	}

	/**
	 * Updates the dutyInformation
	 * @param dutyInformation the new dutyInformation
	 */
	public void setDutyInformation(String dutyInformation) {
		this.dutyInformation = dutyInformation;
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

		ImportItemAudit that = (ImportItemAudit) o;

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
				", action=" + action +
				", changedBy=" + changedBy +
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
				", dutyConfirmationDate=" + dutyConfirmationDate + '\'' +
				", freightConfirmationDate=" + freightConfirmationDate + '\'' +
				'}';
	}
}
