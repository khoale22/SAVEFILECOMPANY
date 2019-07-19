package com.heb.pm.productHierarchy;

import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.entity.SubCommodity;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Resolves the lazily loaded objects for a ClassCommodity.
 *
 * @author m314029
 * @since 2.6.0
 */
public class ClassCommodityResolver extends BaseHierarchyResolver implements LazyObjectResolver<ClassCommodity> {

	/**
	 * Loads department, sub-department, item-class, and sub-commodities for a class-commodity.
	 *
	 * @param c The object to resolve.
	 */
	@Override
	public void fetch(ClassCommodity c) {
		this.resolveCommodity(c);
		if(c.getItemClassMaster() != null) {
			c.getItemClassMaster().getItemClassCode();
			this.resolveItemClass(c.getItemClassMaster());
			if(c.getItemClassMaster().getSubDepartmentMaster() != null) {
				c.getItemClassMaster().getSubDepartmentMaster().getKey().getSubDepartment();
				if(c.getItemClassMaster().getSubDepartmentMaster().getDepartmentMaster() != null) {
					c.getItemClassMaster().getSubDepartmentMaster().getDepartmentMaster().getKey().getDepartment();
				}
			}
		}
		c.getSubCommodityList().size();
		for(SubCommodity subCommodity : c.getSubCommodityList()){
			this.resolveSubCommodity(subCommodity);
		}
	}
}
