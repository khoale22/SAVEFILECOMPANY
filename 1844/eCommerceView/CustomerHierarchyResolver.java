package com.heb.pm.productDetails.product.eCommerceView;

import com.heb.pm.entity.GenericEntityRelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Used in places like Ecommerce View screen (Customer Hierarchy Assignment popup), Custom hierarchy screen.
 * It's used to display lazy loaded objects for Hierarchy data. After the lazy entity loaded, It will be convert to
 * POJO object base on constructor. This one is useful when we call recursively to get Hierarchy Path, Suggested Hierarchies
 * and Current  Hierarchies for each product, because the recursively generate many nested query and it makes app too slow.
 *
 * @author vn87351
 * @since 2.19.0
 */
public class CustomerHierarchyResolver {
	private static final Logger logger = LoggerFactory.getLogger(CustomerHierarchyResolver.class);

	/**
	 * Sorts the list of generic entity relationships, then fetches the lazy loaded objects:
	 *
	 * GenericEntityRelationship -> List<GenericEntityRelationship> childRelationships
	 *
	 * @param genericEntityRelationships The object to resolve.
	 */
	public List<GenericEntityRelationship> fetch(List<GenericEntityRelationship> genericEntityRelationships){
		this.sortRelationships(genericEntityRelationships);
		return this.fetchChildrenIfNoProductChildren(genericEntityRelationships);
	}

	/**
	 * This method sorts a list of generic entity relationships by their child description's short description.
	 *
	 * @param genericEntityRelationships The GenericEntityRelationships to sort.
	 */
	private void sortRelationships(List<GenericEntityRelationship> genericEntityRelationships){
		genericEntityRelationships.sort(GenericEntityRelationship::compareTo);
	}

	/**
	 * This method takes in a list of relationships, and for each of those relationships, if the relationship does not
	 * have product children, sort its child relationships, then recursively call this method again to fetch the
	 * children of that relationship.
	 * It will be convert to POJO object base on constructor. It is useful when get set data on recursively.
	 *
	 * @param genericEntityRelationships The list of GenericEntityRelationship to fetch.
	 */
	private List<GenericEntityRelationship> fetchChildrenIfNoProductChildren(List<GenericEntityRelationship> genericEntityRelationships) {
		List<GenericEntityRelationship> relationships=new ArrayList<>();
		for(GenericEntityRelationship genericEntityRelationship:genericEntityRelationships){
			GenericEntityRelationship genericEntityRelationshipTemp=new GenericEntityRelationship(genericEntityRelationship);
			if (!genericEntityRelationship.isChildRelationshipOfProductEntityType()) {
				this.sortRelationships(genericEntityRelationship.getChildRelationships());
				genericEntityRelationshipTemp.setChildRelationships(this.fetchChildrenIfNoProductChildren(genericEntityRelationship.getChildRelationships()));
			}
			relationships.add(genericEntityRelationshipTemp);
		}
		return relationships;
	}
}
