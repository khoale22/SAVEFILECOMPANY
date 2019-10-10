package com.heb.pm.productDetails.product.eCommerceView;

import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.util.jpa.LazyObjectResolver;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Resolves lazy loaded objects for a GenericEntityRelationships.
 *
 * @author m314029
 * @since 2.12.0
 */
public class GenericEntityRelationshipResolver implements LazyObjectResolver<List<GenericEntityRelationship>> {
	private static final Logger logger = LoggerFactory.getLogger(GenericEntityRelationshipResolver.class);

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
		this.fetchChildrenIfNoProductChildren(genericEntityRelationships,null);
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
	private void fetchChildrenIfNoProductChildren(List<GenericEntityRelationship> genericEntityRelationships,String pathParent) {
		for(GenericEntityRelationship genericEntityRelationship:genericEntityRelationships){
			if (!genericEntityRelationship.isChildRelationshipOfProductEntityType()) {
//				if(genericEntityRelationship.getChildDescription()!=null) {
//					if (StringUtils.isNotBlank(pathParent)) {
//						if (genericEntityRelationship.getDefaultParent()) {
//							genericEntityRelationship.setPath(pathParent + "&#8594" + genericEntityRelationship.getChildDescription().getLongDescription());
//							genericEntityRelationship.setPathStyle(FONT_COLOR_RED_HTML_START + pathParent + "&#8594" + genericEntityRelationship.getChildDescription().getLongDescription() + FONT_TAG_HTML_END);
//						} else {
//							genericEntityRelationship.setPath(pathParent + "&#8594" + genericEntityRelationship.getChildDescription().getLongDescription());
//							genericEntityRelationship.setPathStyle(pathParent + "&#8594" + genericEntityRelationship.getChildDescription().getLongDescription());
//						}
//					} else {
//						genericEntityRelationship.setAllowPrimaryPath(true);
//						genericEntityRelationship.setPath(genericEntityRelationship.getChildDescription().getLongDescription());
//						genericEntityRelationship.setPathStyle(genericEntityRelationship.getChildDescription().getLongDescription());
//					}
//				}
				this.sortRelationships(genericEntityRelationship.getChildRelationships());
				this.fetchChildrenIfNoProductChildren(genericEntityRelationship.getChildRelationships(), genericEntityRelationship.getPath());
			}
//			else {
//				if (genericEntityRelationship.getChildDescription() != null) {
//					if (StringUtils.isNotBlank(pathParent)) {
//						if (genericEntityRelationship.getDefaultParent()) {
//							genericEntityRelationship.setPath(pathParent + "&#8594" + genericEntityRelationship.getChildDescription().getLongDescription());
//							genericEntityRelationship.setPathStyle(FONT_COLOR_RED_HTML_START + pathParent + "&#8594" + genericEntityRelationship.getChildDescription().getLongDescription() + FONT_TAG_HTML_END);
//						} else {
//							genericEntityRelationship.setPath(pathParent + "&#8594" + genericEntityRelationship.getChildDescription().getLongDescription());
//							genericEntityRelationship.setPathStyle(pathParent + "&#8594" + genericEntityRelationship.getChildDescription().getLongDescription());
//						}
//					} else {
//						genericEntityRelationship.setPath(genericEntityRelationship.getChildDescription().getLongDescription());
//						genericEntityRelationship.setPathStyle(genericEntityRelationship.getChildDescription().getLongDescription());
//					}
//					if (StringUtils.isNotBlank(genericEntityRelationship.getPathStyle()) && genericEntityRelationship.getPathStyle().contains(FONT_COLOR_RED_HTML_START)) {
//						genericEntityRelationship.setAllowPrimaryPath(true);
//					}
//				}
//			}
		}
	}
}
