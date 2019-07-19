/*
 * ProductHierarchyTreeBuilder.java
 *
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.productHierarchy;

import com.heb.pm.entity.*;

/**
 * This class helps to build product hierarchy tree based on sub commodity, class commodity, item class, sub department and department.
 *
 * @author vn70529
 * @since 2.33.0
 */
public class ProductHierarchyTreeBuilder extends BaseProductHierarchyTreeBuilder {
    public ProductHierarchyTreeBuilder() {
    }

    /**
     * Build the sub department hierarchy based on sub department > department.
     *
     * @param subDepartment the sub department.
     */
    @Override
    protected void buildSubDepartmentHierarchy(SubDepartment subDepartment) {
        if (subDepartment.getDepartmentMaster() == null) {
            return;
        }
        // Create department node.
        Department department = new Department(subDepartment.getDepartmentMaster());
        // Create sub department node.
        subDepartment = new SubDepartment(subDepartment);
        //Add sub Department to search results and return true if it is added, otherwise return false.
        if (this.isAddedSubDeptToSearchResults(subDepartment, department)) {
            // Sub department is existing in search results, so we don't need to add it again.
            return;
        }
        // Add Department to search results.
        this.addDeptToSearchResults(department);
    }

    /**
     * Build item class hierarchy based on item class > sub department > department.
     *
     * @param itemClass the item class.
     */
    @Override
    protected void buildItemClassHierarchy(ItemClass itemClass) {
        if (itemClass.getSubDepartmentMaster() == null || itemClass.getSubDepartmentMaster().getDepartmentMaster() == null) {
            return;
        }
        // Create department node.
        Department department = new Department(itemClass.getSubDepartmentMaster().getDepartmentMaster());
        // Create sub department node.
        SubDepartment subDepartment = new SubDepartment(itemClass.getSubDepartmentMaster());
        // Create item class node.
        itemClass = new ItemClass(itemClass);

        // Add item class to search results and return true if it is added, otherwise return false.
        if (this.isAddedItemClassToSearchResults(itemClass, subDepartment)) {
            // Item class is existing in search results, so we don't need to add it again.
            return;
        }
        // Add sub department to search results and return true if it is added, otherwise return false.
        if (this.isAddedSubDeptToSearchResults(subDepartment, department)) {
            // Sub department is existing in search results, so we don't need to add it again.
            return;
        }
        // Add Department to search results.
        this.addDeptToSearchResults(department);
    }

    /**
     * Build class commodity hierarchy based on class commodity > item class > sub department > department.
     *
     * @param classCommodity the class commodity.
     */
    @Override
    protected void buildClassCommodityHierarchy(ClassCommodity classCommodity) {
        if (classCommodity.getItemClassMaster().getSubDepartmentMaster() == null ||
                classCommodity.getItemClassMaster().getSubDepartmentMaster().getDepartmentMaster() == null) {
            return;
        }
        // Create department node.
        Department department = new Department(classCommodity.getItemClassMaster().getSubDepartmentMaster().getDepartmentMaster());
        // Create sub department node.
        SubDepartment subDepartment = new SubDepartment(classCommodity.getItemClassMaster().getSubDepartmentMaster());
        // Create item class node.
        ItemClass itemClass = new ItemClass(classCommodity.getItemClassMaster());
        // Create class commodity node.
        classCommodity = new ClassCommodity(classCommodity);

        // Add class commodity to search results and return true if it is added, otherwise return false.
        if (this.isAddedClassCommodityToSearchResults(classCommodity, itemClass)) {
            // Class commodity is existing in search results, so we don't need to add it again.
            return;
        }
        // Add item class to search results and return true if it is added, otherwise return false.
        if (this.isAddedItemClassToSearchResults(itemClass, subDepartment)) {
            // Item class is existing in search results, so we don't need to add it again.
            return;
        }
        // Add sub department to search results and return true if it is added, otherwise return false.
        if (this.isAddedSubDeptToSearchResults(subDepartment, department)) {
            // Sub department is existing in search results, so we don't need to add it again.
            return;
        }
        // Add Department to search results.
        this.addDeptToSearchResults(department);
    }


    /**
     * Build sub commodity hierarchy based on sub commodity > class commodity > item class > sub department > department.
     *
     * @param subCommodity the sub commodity
     */
    @Override
    protected void buildSubCommodityHierarchy(SubCommodity subCommodity) {
        if (subCommodity.getCommodityMaster().getItemClassMaster().getSubDepartmentMaster() == null ||
                subCommodity.getCommodityMaster().getItemClassMaster().getSubDepartmentMaster().getDepartmentMaster() == null) {
            return;
        }
        // Create department node.
        Department department = new Department(subCommodity.getCommodityMaster().getItemClassMaster().getSubDepartmentMaster().getDepartmentMaster());
        // Create sub department node.
        SubDepartment subDepartment = new SubDepartment(subCommodity.getCommodityMaster().getItemClassMaster().getSubDepartmentMaster());
        // Create item class node.
        ItemClass itemClass = new ItemClass(subCommodity.getCommodityMaster().getItemClassMaster());
        // Create class commodity node.
        ClassCommodity classCommodity = new ClassCommodity(subCommodity.getCommodityMaster());
        // Create sub commodity node.
        subCommodity = new SubCommodity(subCommodity);

        // Add sub commodity to search results and return true if it is added, otherwise return false.
        if (this.isAddedSubCommodityToSearchResults(subCommodity, classCommodity)) {
            // cub commodity is existing in search results, so we don't need to add it again.
            return;
        }
        // Add class commodity to search results and return true if it is added, otherwise return false.
        if (this.isAddedClassCommodityToSearchResults(classCommodity, itemClass)) {
            // class commodity is existing in search results, so we don't need to add it again.
            return;
        }
        // Add item class to search results and return true if it is added, otherwise return false.
        if (this.isAddedItemClassToSearchResults(itemClass, subDepartment)) {
            // Item class is existing in search results, so we don't need to add it again.
            return;
        }
        // Add sub department to search results and return true if it is added, otherwise return false.
        if (this.isAddedSubDeptToSearchResults(subDepartment, department)) {
            // sub department is existing in search results, so we don't need to add it again.
            return;
        }
        // Add Department to search results.
        this.addDeptToSearchResults(department);
    }
}
