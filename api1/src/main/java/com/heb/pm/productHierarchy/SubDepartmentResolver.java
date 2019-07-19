package com.heb.pm.productHierarchy;

import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.entity.ItemClass;
import com.heb.pm.entity.SubDepartment;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Resolves the lazily loaded objects for a SubDepartment.
 *
 * @author m314029
 * @since 2.6.0
 */
public class SubDepartmentResolver extends BaseHierarchyResolver implements LazyObjectResolver<SubDepartment> {

	/**
	 * Loads department, item-classes, commodities, and sub-commodities for a sub-department.
	 *
	 * @param s The object to resolve.
	 */
	@Override
	public void fetch(SubDepartment s) {
		if(s.getDepartmentMaster() != null) {
			s.getDepartmentMaster().getKey().getDepartment();
		}
		this.resolveItemClassesOfSubDepartment(s.getItemClasses());
		for(ItemClass itemClass : s.getItemClasses()){
			this.resolveCommoditiesOfItemClass(itemClass.getCommodityList());
			for(ClassCommodity commodity : itemClass.getCommodityList()){
				this.resolveSubCommoditiesOfCommodity(commodity.getSubCommodityList());
			}
		}
	}
}
