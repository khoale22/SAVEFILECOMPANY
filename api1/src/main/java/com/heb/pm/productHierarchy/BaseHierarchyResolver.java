package com.heb.pm.productHierarchy;

import com.heb.pm.entity.*;

import java.util.List;

/**
 * Base resolver class for product hierarchy that all levels of product hierarchy use to resolve the entities outside
 * the product hierarchy structure.
 *
 * @author m314029
 * @since 2.6.0
 */
public class BaseHierarchyResolver {

	/**
	 * Loads the productTemperatureControl for
	 * an item-class.
	 *
	 * @param i The object to resolve.
	 */
	public void resolveItemClass(ItemClass i) {
		if(i.getTemperatureControlCode() != null) {
			i.getTemperatureControl().getId();
		}
	}

	/**
	 * Loads the pdpTemplate for a commodity.
	 *
	 * @param c The object to resolve.
	 */
	public void resolveCommodity(ClassCommodity c) {
		if (c.getPdpTemplateCode() != null) {
			c.getPdpTemplate().getId();
		}
	}

	/**
	 * Loads the commodities of a list of itemClasses.
	 *
	 * @param itemClasses list of itemClasses to resolve.
	 */
	public void resolveItemClassesOfSubDepartment(List<? extends ItemClass> itemClasses){
		for(ItemClass itemClass : itemClasses){
			this.resolveItemClass(itemClass);
		}
	}

	/**
	 * Loads the sub-commodities of a list of commodities.
	 *
	 * @param commodities list of commodities to resolve.
	 */
	public void resolveCommoditiesOfItemClass(List<? extends ClassCommodity> commodities){
		for(ClassCommodity commodity : commodities){
			this.resolveCommodity(commodity);
		}
	}

	/**
	 * Loads the subCommodities of a commodity
	 *
	 * @param subCommodityList list of subCommodites to resolve.
	 */
	public void resolveSubCommoditiesOfCommodity(List<SubCommodity> subCommodityList) {
		for(SubCommodity subCommodity : subCommodityList){
			this.resolveSubCommodity(subCommodity);
		}
	}

	/**
	 * Loads the productPreferredUnitOfMeasures and stateWarnings for a subCommodity.
	 *
	 * @param subCommodity the subCommodity to resolve.
	 */
	public void resolveSubCommodity(SubCommodity subCommodity) {
		subCommodity.getProductPreferredUnitOfMeasureList().size();
		subCommodity.getStateWarningList().size();
		if (subCommodity.getProductCategory() != null) {
			subCommodity.getProductCategory().getProductCategoryId();
		}
	}
}
