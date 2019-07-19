package com.heb.pm.customHierarchy;

import com.heb.pm.entity.CustomerProductGroup;
import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.entity.ProductMaster;

import java.io.Serializable;
import java.util.List;

/**
 * @author m314029
 * @since
 */
public class CustomHierarchyProductLevelInformation  implements Serializable {
	private static final long serialVersionUID = 1955977280789502258L;
	private ProductMaster productMaster;
	private CustomerProductGroup customerProductGroup;
	private List<GenericEntityRelationship> defaultParentRelationships;
	private Long entityId;

	/**
	 * Returns ProductMaster of the custom hierarchy product level information.
	 *
	 * @return The ProductMaster.
	 **/
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	/**
	 * Sets the ProductMaster.
	 *
	 * @param productMaster The ProductMaster.
	 **/
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	/**
	 * Returns CustomerProductGroup of the custom hierarchy product level information.
	 *
	 * @return The CustomerProductGroup.
	 **/
	public CustomerProductGroup getCustomerProductGroup() {
		return customerProductGroup;
	}

	/**
	 * Sets the CustomerProductGroup.
	 *
	 * @param customerProductGroup The CustomerProductGroup.
	 **/
	public void setCustomerProductGroup(CustomerProductGroup customerProductGroup) {
		this.customerProductGroup = customerProductGroup;
	}

	/**
	 * Returns DefaultParentRelationships of the custom hierarchy product level information.
	 *
	 * @return The DefaultParentRelationships.
	 **/
	public List<GenericEntityRelationship> getDefaultParentRelationships() {
		return defaultParentRelationships;
	}

	/**
	 * Sets the DefaultParentRelationships.
	 *
	 * @param defaultParentRelationships The DefaultParentRelationships.
	 **/
	public void setDefaultParentRelationships(List<GenericEntityRelationship> defaultParentRelationships) {
		this.defaultParentRelationships = defaultParentRelationships;
	}

	/**
	 * Returns EntityId. This is the id of the generic entity associated with the product or product group.
	 *
	 * @return The EntityId.
	 **/
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * Sets the EntityId.
	 *
	 * @param entityId The EntityId.
	 **/
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
}
