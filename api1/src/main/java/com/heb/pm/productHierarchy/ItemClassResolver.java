package com.heb.pm.productHierarchy;

import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.entity.ItemClass;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Resolves the lazily loaded objects for an ItemClass.
 *
 * @author m314029
 * @since 2.7.0
 */
public class ItemClassResolver extends BaseHierarchyResolver implements LazyObjectResolver<ItemClass> {

	/**
	 * Loads department, sub-department, commodities, and sub-commodities for an itemClass.
	 *
	 * @param i The object to resolve.
	 */
	@Override
	public void fetch(ItemClass i) {
		this.resolveItemClass(i);
		if(i.getSubDepartmentMaster() != null) {
			i.getSubDepartmentMaster().getKey().getSubDepartment();
			if(i.getSubDepartmentMaster().getDepartmentMaster() != null) {
				i.getSubDepartmentMaster().getDepartmentMaster().getKey().getDepartment();
			}
		}
		this.resolveCommoditiesOfItemClass(i.getCommodityList());
		for(ClassCommodity commodity : i.getCommodityList()){
			this.resolveSubCommoditiesOfCommodity(commodity.getSubCommodityList());
		}
	}
}
