package com.heb.pm.productDetails.product.taxonomy;

import com.heb.pm.entity.GenericEntityDescription;
import com.heb.pm.entity.GenericEntityRelationship;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a product attribute hierarchy level model.
 *
 * @author m314029
 * @since 2.18.4
 */
public class ProductAttributeHierarchyLevel implements Serializable {
	private static final long serialVersionUID = -887126801189600021L;

	private GenericEntityDescription entityDescription;
	private List<GenericEntityRelationship> paths;

	public ProductAttributeHierarchyLevel() {
	}

	public ProductAttributeHierarchyLevel(
			GenericEntityDescription entityDescription,
			List<GenericEntityRelationship> paths) {
		this.entityDescription = entityDescription;
		this.paths = paths;
	}

	/**
	 * Returns EntityDescription.
	 *
	 * @return The EntityDescription.
	 **/
	public GenericEntityDescription getEntityDescription() {
		return entityDescription;
	}

	/**
	 * Sets the EntityDescription.
	 *
	 * @param entityDescription The EntityDescription.
	 **/
	public ProductAttributeHierarchyLevel setEntityDescription(GenericEntityDescription entityDescription) {
		this.entityDescription = entityDescription;
		return this;
	}

	/**
	 * Returns Paths.
	 *
	 * @return The Paths.
	 **/
	public List<GenericEntityRelationship> getPaths() {
		return paths;
	}

	/**
	 * Sets the Paths.
	 *
	 * @param paths The Paths.
	 **/
	public ProductAttributeHierarchyLevel setPaths(List<GenericEntityRelationship> paths) {
		this.paths = paths;
		return this;
	}
}
