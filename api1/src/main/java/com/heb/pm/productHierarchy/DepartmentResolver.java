/*
 * DepartmentResolver
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.entity.Department;
import com.heb.pm.entity.ItemClass;
import com.heb.pm.entity.SubDepartment;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Resolves the lazily loaded objects for a Department.
 *
 * @author m314029
 * @since 2.6.0
 */
public class DepartmentResolver extends BaseHierarchyResolver implements LazyObjectResolver<Department> {

	/**
	 * Loads the sub-departments, item-classes, commodities, and sub-commodities for a department.
	 *
	 * @param d The object to resolve.
	 */
	@Override
	public void fetch(Department d) {
		for(SubDepartment subDepartment : d.getSubDepartmentList()){
			this.resolveItemClassesOfSubDepartment(subDepartment.getItemClasses());
			for(ItemClass itemClass : subDepartment.getItemClasses()){
				this.resolveCommoditiesOfItemClass(itemClass.getCommodityList());
				for(ClassCommodity commodity : itemClass.getCommodityList()){
					this.resolveSubCommoditiesOfCommodity(commodity.getSubCommodityList());
				}
			}
		}
	}
}
