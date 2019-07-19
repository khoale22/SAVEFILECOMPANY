package com.heb.pm.productHierarchy;

import com.heb.pm.entity.SubCommodity;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Resolves the lazily loaded objects for a SubCommodity.
 *
 * @author m314029
 * @since 2.6.0
 */
public class SubCommodityResolver extends BaseHierarchyResolver implements LazyObjectResolver<SubCommodity> {

	/**
	 * Loads department, sub-department, item-class, and commodities for a sub-commodity.
	 *
	 * @param d The object to resolve.
	 */
	@Override
	public void fetch(SubCommodity d) {
		this.resolveSubCommodity(d);
		if(d.getCommodityMaster() != null) {
			d.getCommodityMaster().getKey().getCommodityCode();
			this.resolveCommodity(d.getCommodityMaster());
			if(d.getCommodityMaster().getItemClassMaster() != null) {
				d.getCommodityMaster().getItemClassMaster().getItemClassCode();
				this.resolveItemClass(d.getCommodityMaster().getItemClassMaster());
				if(d.getCommodityMaster().getItemClassMaster().getSubDepartmentMaster() != null) {
					d.getCommodityMaster().getItemClassMaster().getSubDepartmentMaster().getKey().getSubDepartment();
					if(d.getCommodityMaster().getItemClassMaster().getSubDepartmentMaster().getDepartmentMaster() != null) {
						d.getCommodityMaster().getItemClassMaster().getSubDepartmentMaster().getDepartmentMaster().getKey().getDepartment();
					}
				}
			}
		}
	}
}
