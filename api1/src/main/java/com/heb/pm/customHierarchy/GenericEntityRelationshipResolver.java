package com.heb.pm.customHierarchy;

import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.util.jpa.LazyObjectResolver;

import java.util.List;

/**
 * Resolves lazy loaded objects for a GenericEntityRelationships.
 *
 * @author m314029
 * @since 2.12.0
 */
public class GenericEntityRelationshipResolver implements LazyObjectResolver<List<GenericEntityRelationship>> {

	/**
	 * Sorts the list of generic entity relationships, then fetches the lazy loaded objects:
	 *
	 * GenericEntityRelationship -> List<GenericEntityRelationship> childRelationships
	 *
	 * @param genericEntityRelationships The object to resolve.
	 */
	@Override
	public void fetch(List<GenericEntityRelationship> genericEntityRelationships){
		this.sortRelationships(genericEntityRelationships);
		this.fetchChildrenIfNoProductChildren(genericEntityRelationships);
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
	 *
	 * @param genericEntityRelationships The list of GenericEntityRelationship to fetch.
	 */
	private void fetchChildrenIfNoProductChildren(List<GenericEntityRelationship> genericEntityRelationships) {
		genericEntityRelationships.forEach(
				genericEntityRelationship -> {
					if (!genericEntityRelationship.isChildRelationshipOfProductEntityType()) {
						this.sortRelationships(genericEntityRelationship.getChildRelationships());
						this.fetchChildrenIfNoProductChildren(genericEntityRelationship.getChildRelationships());
					}
				});
	}
}
