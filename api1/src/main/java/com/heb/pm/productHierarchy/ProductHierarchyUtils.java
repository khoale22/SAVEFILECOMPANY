package com.heb.pm.productHierarchy;

import com.heb.pm.entity.ItemClass;
import com.heb.pm.entity.SubDepartment;
import com.heb.pm.repository.ItemClassRepository;
import com.heb.pm.repository.SubDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Helper class for product hierarchy.
 *
 * @author m314029
 * @since 2.6.0
 */
@Component
public class ProductHierarchyUtils {

	private static final String ITEM_CLASS_DEPARTMENT_AS_STRING_CONVERSION = "%02d";

	@Autowired
	private ItemClassRepository itemClassRepository;

	@Autowired
	private SubDepartmentRepository subDepartmentRepository;

	/**
	 * This method sets the itemClasses for a subDepartmentList, since we were unable to get the JPA mapping between
	 * SubDepartment and ItemClass to work.
	 * (In the database, SubDepartment's department is a varchar; in ItemClass it is a decimal)
	 *
	 * @param subDepartments the subDepartments needing to load the itemClasses for.
	 */
	public void extrapolateItemClassListOfSubDepartmentList(List<? extends SubDepartment> subDepartments) {
		for(SubDepartment s : subDepartments){
			this.extrapolateItemClassListOfSubDepartment(s);
		}
	}

	/**
	 * This method sets the itemClasses for a subDepartmentList, since we were unable to get the JPA mapping between
	 * SubDepartment and ItemClass to work.
	 * (In the database, SubDepartment's department is a varchar; in ItemClass it is a decimal)
	 *
	 * @param subDepartment the subDepartments needing to load the itemClasses for.
	 */
	public void extrapolateItemClassListOfSubDepartment(SubDepartment subDepartment) {
		subDepartment.setItemClasses(this.itemClassRepository.
				findByDepartmentIdAndSubDepartmentId(
						Integer.valueOf(subDepartment.getKey().getDepartment().trim()),
						subDepartment.getKey().getSubDepartment()));
		subDepartment.getItemClasses().forEach(itemClass -> itemClass.setSubDepartmentMaster(subDepartment));
	}

	/**
	 * This method sets the subDepartments for an itemClassList, since we were unable to get the JPA mapping between
	 * SubDepartment and ItemClass to work.
	 * (In the database, SubDepartment's department is a varchar; in ItemClass it is a decimal)
	 *
	 * @param itemClasses the itemClasses needing to load the subDepartment for.
	 */
	public void extrapolateSubDepartmentOfItemClassList(List<? extends ItemClass> itemClasses) {
		for(ItemClass i : itemClasses){
			this.extrapolateSubDepartmentOfItemClass(i);
		}
	}

	/**
	 * This method sets the subDepartment for an itemClass, since we were unable to get the JPA mapping between
	 * SubDepartment and ItemClass to work.
	 * (In the database, SubDepartment's department is a varchar; in ItemClass it is a decimal)
	 *
	 * @param itemClass the itemClasses needing to load the subDepartment for.
	 */
	public void extrapolateSubDepartmentOfItemClass(ItemClass itemClass) {
		String subDepartment = itemClass.getSubDepartmentId().trim();
		String department = String.format(
				ProductHierarchyUtils.ITEM_CLASS_DEPARTMENT_AS_STRING_CONVERSION, itemClass.getDepartmentId());
		itemClass.setSubDepartmentMaster(
				this.subDepartmentRepository.findOneByKeyDepartmentAndKeySubDepartment(
						department, subDepartment));
		if(itemClass.getSubDepartmentMaster() != null && itemClass.getSubDepartmentMaster().getItemClasses() == null) {
			itemClass.getSubDepartmentMaster().setItemClasses(
					this.itemClassRepository.findByDepartmentIdAndSubDepartmentId(
							itemClass.getDepartmentId(), subDepartment));
		}
	}
}
