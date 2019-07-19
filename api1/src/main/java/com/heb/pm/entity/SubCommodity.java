package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a sub-commodity.
 *
 * @author d116773
 * @since 2.0.2
 */
@Entity
@Table(name = "pd_cls_com_sub_com")
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class)
public class SubCommodity implements Serializable {

	// default constructor
	public SubCommodity(){super();}

	// copy constructor
	// this does not call 'setCommodityMaster' to prevent infinite loop -- if this method
	// needs to be called, it needs to be done after creating all of the objects
	public SubCommodity(SubCommodity subCommodity){
		super();
		this.setKey(new SubCommodityKey(subCommodity.getKey()));
		this.setSubCommodityActive(subCommodity.getSubCommodityActive());
		this.setFoodStampEligible(subCommodity.getFoodStampEligible());
		this.setHighMarginPercent(subCommodity.getHighMarginPercent());
		this.setImsClassCode(subCommodity.getImsClassCode());
		this.setImsCommodityCode(subCommodity.getImsCommodityCode());
		this.setImsSubCommodityCode(subCommodity.getImsSubCommodityCode());
		this.setLowMarginPercent(subCommodity.getLowMarginPercent());
		this.setName(subCommodity.getName());
		this.setNonTaxCategoryCode(subCommodity.getNonTaxCategoryCode());
		this.setTaxCategoryCode(subCommodity.getTaxCategoryCode());
		this.setProductCategoryId(subCommodity.getProductCategoryId());
		this.setProductCategory(subCommodity.getProductCategory());
		this.setProductPreferredUnitOfMeasureList(new ArrayList<>());
		this.setTaxEligible(subCommodity.getTaxEligible());
		for(ProductPreferredUnitOfMeasure preferredUnitOfMeasure : subCommodity.getProductPreferredUnitOfMeasureList()){
			this.getProductPreferredUnitOfMeasureList().add(new ProductPreferredUnitOfMeasure(preferredUnitOfMeasure));
		}
		this.setStateWarningList(new ArrayList<>());
		for(SubCommodityStateWarning stateWarning : subCommodity.getStateWarningList()){
			this.getStateWarningList().add(new SubCommodityStateWarning(stateWarning));
		}
	}

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s[%d]";

	@EmbeddedId
	private SubCommodityKey key;

	@Column(name = "pd_omi_com_des")
	@Type(type="fixedLengthChar")
	private String name;

	// This was added to facilitate JPA mappings as it was not looking inside a SubCommodityKey for just
	//  subCommodityCode. Getters and setters not added as this should not be used besides JPA mapping.
	@Column(name="pd_omi_sub_com_cd", insertable = false, updatable = false)
	private Integer subCommodityCode;

	@Column(name = "prod_cat_id")
	private Integer productCategoryId;

	@Column(name = "rl_sub_com_hgm_pct")
	private Double lowMarginPercent;

	@Column(name = "rl_sub_com_lgm_pct")
	private Double highMarginPercent;

	@Column(name = "pd_item_class_cd")
	private Integer imsClassCode;

	@Column(name = "pd_com_cd")
	private Integer imsCommodityCode;

	@Column(name = "pd_sub_com_cd")
	private String imsSubCommodityCode;

	@Column(name = "pc_sub_com_actv_cd")
	private Character subCommodityActive;

	@Column(name = "pd_fd_stamp_cd")
	private Boolean foodStampEligible;

	@Column(name = "pd_crg_tax_cd")
	private Boolean taxEligible;

	@Column(name = "vertex_non_tax_cd")
	@Type(type="fixedLengthChar")
	private String nonTaxCategoryCode;

	@Column(name = "vertex_tax_cat_cd")
	@Type(type="fixedLengthChar")
	private String taxCategoryCode;

	@JsonIgnoreProperties("subCommodityList")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "pd_omi_com_cls_cd", referencedColumnName = "pd_omi_com_cls_cd", insertable = false, updatable = false),
			@JoinColumn(name = "pd_omi_com_cd", referencedColumnName = "pd_omi_com_cd", insertable = false, updatable = false)
	})
	private ClassCommodity commodityMaster;

	@OneToMany(fetch = FetchType.LAZY)
	@OrderBy(value = "pref_uom_seq_nbr")
	@JoinColumn(name = "pd_omi_sub_com_cd", referencedColumnName = "pd_omi_sub_com_cd")
	private List<ProductPreferredUnitOfMeasure> productPreferredUnitOfMeasureList;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "pd_omi_sub_com_cd", referencedColumnName = "pd_omi_sub_com_cd")
	private List<SubCommodityStateWarning> stateWarningList;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_cat_id", referencedColumnName = "prod_cat_id", insertable = false, updatable = false)
	private ProductCategory productCategory;

	/**
	 * Get the productCategory.
	 *
	 * @return the productCategory
	 */
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	/**
	 * Set the productCategory.
	 *
	 * @param productCategory the productCategory to set
	 */
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	/**
	 * Gets commodityMaster.
	 *
	 * @return the commodityMaster
	 */
	public ClassCommodity getCommodityMaster() {
		return commodityMaster;
	}

	/**
	 * Sets commodityMaster.
	 *
	 * @param commodityMaster the commodityMaster
	 */
	public void setCommodityMaster(ClassCommodity commodityMaster) {
		this.commodityMaster = commodityMaster;
	}

	/**
	 * Returns the key for this object.
	 *
	 * @return The key for this object.
	 */
	public SubCommodityKey getKey() {
		return key;
	}

	/**
	 * Sets the key for this object.
	 *
	 * @param key They key for this object.
	 */
	public void setKey(SubCommodityKey key) {
		this.key = key;
	}

	/**
	 * Returns the name of the sub-commodity.
	 *
	 * @return The name of the sub-commodity.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the sub-commodity.
	 *
	 * @param name The name of the sub-commodity.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns a description sub-commodity to display on the GUI.
	 *
	 * @return A description sub-commodity to display on the GUI.
	 */
	public String getDisplayName() {
		return this.key == null ? "" :
				String.format(SubCommodity.DISPLAY_NAME_FORMAT, this.name.trim(), this.getKey().getSubCommodityCode());
	}

	/**
	 * Returns a unique ID for this sub-commodity that can be used where a distinct number is needed like in a list.
	 * It relies on the fact that, thought the table does not enforce it, sub-commodity IDs are unique to the
	 * sub-commodity. If the key is not set for this object, it returns 0.
	 *
	 * @return A unique ID for this sub-commodity.
	 */
	public int getNormalizedId() {

		return this.key == null ? 0 : this.key.getSubCommodityCode();
	}

	/**
	 * Returns ProductCategoryId.
	 *
	 * @return The ProductCategoryId.
	 **/
	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	/**
	 * Sets the ProductCategoryId.
	 *
	 * @param productCategoryId The ProductCategoryId.
	 **/
	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	/**
	 * Returns LowMarginPercent.
	 *
	 * @return The LowMarginPercent.
	 **/
	public Double getLowMarginPercent() {
		return lowMarginPercent;
	}

	/**
	 * Sets the LowMarginPercent.
	 *
	 * @param lowMarginPercent The LowMarginPercent.
	 **/
	public void setLowMarginPercent(Double lowMarginPercent) {
		this.lowMarginPercent = lowMarginPercent;
	}

	/**
	 * Returns HighMarginPercent.
	 *
	 * @return The HighMarginPercent.
	 **/
	public Double getHighMarginPercent() {
		return highMarginPercent;
	}

	/**
	 * Sets the HighMarginPercent.
	 *
	 * @param highMarginPercent The HighMarginPercent.
	 **/
	public void setHighMarginPercent(Double highMarginPercent) {
		this.highMarginPercent = highMarginPercent;
	}

	/**
	 * Returns ImsClassCode.
	 *
	 * @return The ImsClassCode.
	 **/
	public Integer getImsClassCode() {
		return imsClassCode;
	}

	/**
	 * Sets the ImsClassCode.
	 *
	 * @param imsClassCode The ImsClassCode.
	 **/
	public void setImsClassCode(Integer imsClassCode) {
		this.imsClassCode = imsClassCode;
	}

	/**
	 * Returns ImsCommodityCode.
	 *
	 * @return The ImsCommodityCode.
	 **/
	public Integer getImsCommodityCode() {
		return imsCommodityCode;
	}

	/**
	 * Sets the ImsCommodityCode.
	 *
	 * @param imsCommodityCode The ImsCommodityCode.
	 **/
	public void setImsCommodityCode(Integer imsCommodityCode) {
		this.imsCommodityCode = imsCommodityCode;
	}

	/**
	 * Get the imsSubCommodityCode.
	 *
	 * @return the imsSubCommodityCode
	 */
	public String getImsSubCommodityCode() {
		return imsSubCommodityCode;
	}

	/**
	 * Set the imsSubCommodityCode.
	 *
	 * @param imsSubCommodityCode the imsSubCommodityCode to set
	 */
	public void setImsSubCommodityCode(String imsSubCommodityCode) {
		this.imsSubCommodityCode = imsSubCommodityCode;
	}

	/**
	 * Returns SubCommodityActive.
	 *
	 * @return The SubCommodityActive.
	 **/
	public Character getSubCommodityActive() {
		return subCommodityActive;
	}

	/**
	 * Sets the SubCommodityActive.
	 *
	 * @param subCommodityActive The SubCommodityActive.
	 **/
	public void setSubCommodityActive(Character subCommodityActive) {
		this.subCommodityActive = subCommodityActive;
	}

	/**
	 * Returns FoodStampEligible.
	 *
	 * @return The FoodStampEligible.
	 **/
	public Boolean getFoodStampEligible() {
		return foodStampEligible;
	}

	/**
	 * Sets the FoodStampEligible.
	 *
	 * @param foodStampEligible The FoodStampEligible.
	 **/
	public void setFoodStampEligible(Boolean foodStampEligible) {
		this.foodStampEligible = foodStampEligible;
	}

	/**
	 * Returns TaxEligible.
	 *
	 * @return The TaxEligible.
	 **/
	public Boolean getTaxEligible() {
		return taxEligible;
	}

	/**
	 * Sets the TaxEligible.
	 *
	 * @param taxEligible The TaxEligible.
	 **/
	public void setTaxEligible(Boolean taxEligible) {
		this.taxEligible = taxEligible;
	}

	/**
	 * Returns NonTaxCategoryCode.
	 *
	 * @return The NonTaxCategoryCode.
	 **/
	public String getNonTaxCategoryCode() {
		return nonTaxCategoryCode;
	}

	/**
	 * Sets the NonTaxCategoryCode.
	 *
	 * @param nonTaxCategoryCode The NonTaxCategoryCode.
	 **/
	public void setNonTaxCategoryCode(String nonTaxCategoryCode) {
		this.nonTaxCategoryCode = nonTaxCategoryCode;
	}

	/**
	 * Returns TaxCategoryCode.
	 *
	 * @return The TaxCategoryCode.
	 **/
	public String getTaxCategoryCode() {
		return taxCategoryCode;
	}

	/**
	 * Sets the TaxCategoryCode.
	 *
	 * @param taxCategoryCode The TaxCategoryCode.
	 **/
	public void setTaxCategoryCode(String taxCategoryCode) {
		this.taxCategoryCode = taxCategoryCode;
	}

	/**
	 * Returns a list of preferred units of measure for this sub-commodity.
	 *
	 * @return The ProductPreferredUnitOfMeasureList.
	 **/
	public List<ProductPreferredUnitOfMeasure> getProductPreferredUnitOfMeasureList() {
		return productPreferredUnitOfMeasureList;
	}

	/**
	 * Sets the list of preferred units of measure.
	 *
	 * @param productPreferredUnitOfMeasureList The ProductPreferredUnitOfMeasureList.
	 **/
	public void setProductPreferredUnitOfMeasureList(List<ProductPreferredUnitOfMeasure> productPreferredUnitOfMeasureList) {
		this.productPreferredUnitOfMeasureList = productPreferredUnitOfMeasureList;
	}

	/**
	 * Returns list of state warnings for this sub-commodity.
	 *
	 * @return The StateWarningList.
	 **/
	public List<SubCommodityStateWarning> getStateWarningList() {
		return stateWarningList;
	}

	/**
	 * Sets the state warnings for this sub-commodity.
	 *
	 * @param stateWarningList The StateWarningList.
	 **/
	public void setStateWarningList(List<SubCommodityStateWarning> stateWarningList) {
		this.stateWarningList = stateWarningList;
	}

	/**
	 * Tests for equality with another object. Equality is based on the key.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SubCommodity)) return false;

		SubCommodity that = (SubCommodity) o;

		return !(key != null ? !key.equals(that.key) : that.key != null);

	}

	/**
	 * Returns a hash code for the object. Equal objects return the same falue. Unequal objects (probably) return
	 * different values.
	 *
	 * @return A hash code for the object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "SubCommodity{" +
				"key=" + key +
				", name='" + name + '\'' +
				", subCommodityCode=" + subCommodityCode +
				", productCategoryId=" + productCategoryId +
				", lowMarginPercent=" + lowMarginPercent +
				", highMarginPercent=" + highMarginPercent +
				", imsClassCode=" + imsClassCode +
				", imsCommodityCode=" + imsCommodityCode +
				", subCommodityActive=" + subCommodityActive +
				", foodStampEligible=" + foodStampEligible +
				", taxEligible=" + taxEligible +
				", nonTaxCategoryCode='" + nonTaxCategoryCode + '\'' +
				", taxCategoryCode='" + taxCategoryCode + '\'' +
				'}';
	}
}
