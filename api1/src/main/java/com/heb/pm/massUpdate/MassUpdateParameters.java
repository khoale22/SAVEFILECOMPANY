package com.heb.pm.massUpdate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.entity.ProductFullfilmentChanel;
import com.heb.pm.entity.SalesChannel;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Stores the parameters the user wishes to update a list of products with.
 *
 * @author d116773
 * @since 2.12.0
 */
public class MassUpdateParameters {

	/**
	 * Represents the different types of attributes the user can use with the mass update functions.
	 */
	public enum Attribute {

		THIRD_PARTY_SELLABLE("3rd Party Sellable"),
		SELECT_INGREDIENTS("Select Ingredients"),
		GO_LOCAL("Go Local"),
		TOTALLY_TEXAS("Totally Texas"),
		FOOD_STAMP("Food Stamp"),
		FSA("FSA"),
		TAX_FLAG("Tax Flag"),
		SELF_MANUFACTURED("Self Manufactured"),
		TAX_CATEGORY("Tax Category"),
		PRIMO_PICK("Primo Pick"),
		DISTINCTIVE("Distinctive"),
		PRODUCE_ORGANIC("Produce Organic"),
		UNASSIGN_PRODUCTS("Unassign Products For Hierarchy"),
		TAG_TYPE("Tag Type"),
		ADD_HIERARCHY_PRODUCT("Add Product of Lowest Level"),
		REMOVE_HIERARCHY_PRODUCT("Delete Product of Lowest Level"),
		MOVE_HIERARCHY_PRODUCT("Move Products for Hierarchy"),
		PDP_TEMPLATE("Update Product of Lowest Level Attributes"),
		FULFILLMENT_CHANNEL("Update Product of Lowest Level Attributes"),
		ADD_HIERARCHY_PRODUCT_GROUP("Add Product Groups of Lowest Level"),
		REMOVE_HIERARCHY_PRODUCT_GROUP("Delete Product Groups of Lowest Level"),
		PRIMARY_PATH("Update Product of Lowest Level Attributes"),
		SHOW_ON_SITE("Show On Site");

		private String attributeName;

		/**
		 * Constructs a new Attribute.
		 *
		 * @param attributeName The name for this attribute.
		 */
		Attribute(String attributeName) {
			this.attributeName = attributeName;
		}

		/**
		 * Called by the Jackson framework to construct an Attribute from JSON.
		 *
		 * @param name The name of attribute to create. This should be the name for the enum (i.e. THIRD_PARTY_SELLABLE)
		 * @return The Attribute matching the supplied name.
		 */
		@JsonCreator
		public static Attribute forValue(String name) {
			return Attribute.valueOf(name);
		}

		/**
		 * Returns the name of the attribute.
		 *
		 * @return The name of the attribute.
		 */
		public String getAttributeName() {
			return this.attributeName;
		}

	}

	/**
	 * Represents the different functions you can apply to a Primo Pick.
	 */
	public enum PrimoPickFunction {
		APPROVE_PRIMO_PICK("Approve Primo Pick"),
		REJECT_PRIMO_PICK("Reject Primo Pick"),
		TURN_ON_PRIMO_PICK("Turn on Primo Pick"),
		TURN_OFF_PRIMO_PICK("Turn off Primo Pick");

		private String name;

		/**
		 * Constructs a new PrimoPickFunction.
		 *
		 * @param name The description of the function.
		 */
		PrimoPickFunction(String name) {
			this.name = name;
		}

		/**
		 * Called by the Jackson framework to construct an Attribute from JSON.
		 *
		 * @param name The name of function to create. This should be the name for the enum (i.e. THIRD_PARTY_SELLABLE)
		 * @return The PrimoPickFunction matching the supplied name.
		 */
		@JsonCreator
		public PrimoPickFunction forValue(String name) {
			return PrimoPickFunction.valueOf(name);
		}

		/**
		 * Returns the description of this function.
		 *
		 * @return The description of this function.
		 */
		public String getName() {
			return this.name;
		}
	}

	/**
	 * Represents the different functions you can apply to a Distinctive.
	 */
	public enum DistinctiveFunction {
		TURN_ON_DISTINCTIVE("Turn on Distinctive"),
		TURN_OFF_DISTINCTIVE("Turn off Distinctive");

		private String name;

		/**
		 * Constructs a new DistinctiveFunction.
		 *
		 * @param name The description of the function.
		 */
		DistinctiveFunction(String name) {
			this.name = name;
		}

		/**
		 * Called by the Jackson framework to construct an Attribute from JSON.
		 *
		 * @param name The name of function to create. This should be the name for the enum (i.e. TURN_ON_DISTINCTIVE)
		 * @return The DistinctiveFunction matching the supplied name.
		 */
		@JsonCreator
		public DistinctiveFunction forValue(String name) {
			return DistinctiveFunction.valueOf(name);
		}

		/**
		 * Returns the description of this function.
		 *
		 * @return The description of this function.
		 */
		public String getName() {
			return this.name;
		}
	}


	private Attribute attribute;
	private PrimoPickFunction primoPickFunction;
	private DistinctiveFunction distinctiveFunction;
	private Boolean booleanValue;
	private String stringValue;
	private LocalDate effectiveDate;
	private LocalDate endDate;
	private String userId;
	private String description;
	private Map<Long, String> primoPickDescriptions;
	private Long rootId;
	private GenericEntityRelationship entityRelationship;
	private String pdpTemplate;
	private List<ProductFullfilmentChanel> productFullfilmentChanels;
	private String changeReason;
	private List<SalesChannel> selectedSaleChannels;

	/**
	 * Returns the name of the attribute the user wants to update.
	 *
	 * @return The name of the attribute the user wants to update.
	 */
	public Attribute getAttribute() {
		return attribute;
	}

	/***
	 * Sets the name of the attribute the user wants to update.
	 *
	 * @param attribute The name of the attribute the user wants to update.
	 */
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	/**
	 * If the attribute the user wishes to update is a String, this will contain the values. This may return null.
	 *
	 * @return The value the user wants to set the attribute to if it holds a String.
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * Sets the value the user wants to set the attribute to.
	 *
	 * @param stringValue The value the user wants to set the attribute to.
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * Returns the effective date of the change of the attribute. This may return null.
	 *
	 * @return The effective date of the change of the attribute.
	 */
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * Sets the effective date of the change of the attribute.
	 *
	 * @param effectiveDate The effective date of the change of the attribute.
	 */
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	/**
	 * Returns the end date of the change of the attribute. This may return null.
	 *
	 * @return The end date of the change of the attribute.
	 */
	public LocalDate getEndDate() {
		return endDate;
	}
	/**
	 * Sets the end date of the change of the attribute.
	 *
	 * @param endDate The end date of the change of the attribute.
	 */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * If the attribute the user wishes to update is a Boolean, this will contain the values. This may return null.
	 *
	 * @return The value the user wants to set the attribute to if it holds a Boolean.
	 */
	public Boolean getBooleanValue() {
		return booleanValue;
	}

	/**
	 * Sets the value the user wants to set the attribute to.
	 *
	 * @param booleanValue The value the user wants to set the attribute to.
	 */
	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	/**
	 * Returns the user who initiated this request for update.
	 *
	 * @return The one-pass ID of the logged in user.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 *  Sets the user who initiated this request for update.
	 *
	 * @param userId The one-pass ID of the logged in user.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Returns the user-provided description of this mass update.
	 *
	 * @return The user-provided description of this mass update.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the user-provided description of this mass update.
	 *
	 * @param description The user-provided description of this mass update.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the type of Primo Pick change this is.
	 *
	 * @return The type of Primo Pick change this is.
	 */
	public PrimoPickFunction getPrimoPickFunction() {
		return primoPickFunction;
	}

	/**
	 * Sets the type of Primo Pick change this is.
	 *
	 * @param primoPickFunction The type of Primo Pick change this is.
	 */
	public void setPrimoPickFunction(PrimoPickFunction primoPickFunction) {
		this.primoPickFunction = primoPickFunction;
	}

	/**
	 * Returns the primo pick descriptions for this mass update request.
	 *
	 * @return The primo pick descriptions for this mass update request.
	 */
	public Map<Long, String> getPrimoPickDescriptions() {
		return primoPickDescriptions;
	}

	/**
	 * Sets the primo pick descriptions for this mass update request.
	 *
	 * @param primoPickDescriptions The primo pick descriptions for this mass update request.
	 */
	public void setPrimoPickDescriptions(Map<Long, String> primoPickDescriptions) {
		this.primoPickDescriptions = primoPickDescriptions;
	}

	/**
	 * Returns a primo pick description for a requested product ID. Will return null if one is not available.
	 *
	 * @param productId The product ID being requested.
	 * @return The approved primo pick description for that product ID.
	 */
	public String primoPickDescriptionFor(Long productId) {
		return this.primoPickDescriptions.get(productId);
	}

	/**
	 * Returns the RootId. This is the root id of the hierarchy that the product is being unassigned from.
	 *
	 * @return RootId
	 */
	public Long getRootId() {
		return rootId;
	}

	/**
	 * Sets the RootId
	 *
	 * @param rootId The RootId
	 */
	public void setRootId(Long rootId) {
		this.rootId = rootId;
	}

	/**
	 * Returns the EntityRelationship
	 *
	 * @return EntityRelationship
	 */
	public GenericEntityRelationship getEntityRelationship() {
		return entityRelationship;
	}

	/**
	 * Sets the EntityRelationship
	 *
	 * @param entityRelationship The EntityRelationship
	 */
	public void setEntityRelationship(GenericEntityRelationship entityRelationship) {
		this.entityRelationship = entityRelationship;
	}

	/**
	 * Returns the PdpTemplate
	 *
	 * @return PdpTemplate
	 */
	public String getPdpTemplate() {
		return pdpTemplate;
	}

	/**
	 * Sets the PdpTemplate
	 *
	 * @param pdpTemplate The PdpTemplate
	 */
	public void setPdpTemplate(String pdpTemplate) {
		this.pdpTemplate = pdpTemplate;
	}

	/**
	 * Returns the changeReason
	 *
	 * @return changeReason
	 */
	public String getChangeReason() {
		return changeReason;
	}

	/**
	 * Sets the changeReason
	 *
	 * @param changeReason The changeReason
	 */
	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}

	/**
	 * Returns the ProductFullfilmentChanels
	 *
	 * @return ProductFullfilmentChanels
	 */
	public List<ProductFullfilmentChanel> getProductFullfilmentChanels() {
		return productFullfilmentChanels;
	}

	/**
	 * Sets the ProductFullfilmentChanels
	 *
	 * @param productFullfilmentChanels The ProductFullfilmentChanels
	 */
	public void setProductFullfilmentChanels(List<ProductFullfilmentChanel> productFullfilmentChanels) {
		this.productFullfilmentChanels = productFullfilmentChanels;
	}

	/**
	 * Returns the list of selected sale channels for show on site mass update.
	 * @return the list of sale channels.
	 */
	public List<SalesChannel> getSelectedSaleChannels() {
		return selectedSaleChannels;
	}

	/**
	 * Sets the list of sale channels for show on site mass update.
	 * @param selectedSaleChannels the list of sale channels.
	 */
	public void setSelectedSaleChannels(List<SalesChannel> selectedSaleChannels) {
		this.selectedSaleChannels = selectedSaleChannels;
	}

	/**
	 * Returns the type of Distinctive change this is.
	 *
	 * @return The type of Distinctive change this is.
	 */
	public DistinctiveFunction getDistinctiveFunction() {
		return distinctiveFunction;
	}

	/**
	 * Sets the type of Distinctive change this is.
	 *
	 * @param distinctiveFunction The type of Primo Pick change this is.
	 */
	public void setDistinctiveFunction(DistinctiveFunction distinctiveFunction) {
		this.distinctiveFunction = distinctiveFunction;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "MassUpdateParameters{" +
				"attribute=" + attribute +
				", primoPickFunction=" + primoPickFunction +
				", booleanValue=" + booleanValue +
				", stringValue='" + stringValue + '\'' +
				", effectiveDate=" + effectiveDate +
				", userId='" + userId + '\'' +
				", description='" + description + '\'' +
				", primoPickDescriptions=" + primoPickDescriptions +
				'}';
	}
}