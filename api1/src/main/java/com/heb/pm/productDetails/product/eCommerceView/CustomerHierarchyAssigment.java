/*
 *  CustomerHierarchyAssigment.
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.eCommerceView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.entity.HierarchyContext;

/**
 * Represents CustomerHierarchyAssigment.
 *
 * @author vn55306
 * @since 2.0.14
 */
public class CustomerHierarchyAssigment implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long entyProductId;
    private List<GenericEntityRelationship> hierarchyContextCurrentPath;
    private List<GenericEntityRelationship> hierarchyContextSuggestPath;
    Map<Long,List<GenericEntityRelationship>> lowestLevel;
    private HierarchyContext customerHierarchyContext;
    private Long productId;
    private Long upc;
    private Integer subCommodity;
    private Integer classCode;
    private Integer commodityCode;
    public String getBrick() {
        return brick;
    }

    public void setBrick(String brick) {
        this.brick = brick;
    }

    private String brick;

    public String getSubCommodityName() {
        return subCommodityName;
    }

    public void setSubCommodityName(String subCommodityName) {
        this.subCommodityName = subCommodityName;
    }

    private String subCommodityName;
    private Long entyId;
    private String hierachyContextCode;
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getUpc() {
        return upc;
    }

    public void setUpc(Long upc) {
        this.upc = upc;
    }

    public Integer getSubCommodity() {
        return subCommodity;
    }

    public void setSubCommodity(Integer subCommodity) {
        this.subCommodity = subCommodity;
    }

    public Long getEntyId() {
        return entyId;
    }

    public void setEntyId(Long entyId) {
        this.entyId = entyId;
    }

    public String getHierachyContextCode() {
        return hierachyContextCode;
    }

    public void setHierachyContextCode(String hierachyContextCode) {
        this.hierachyContextCode = hierachyContextCode;
    }



    public Long getEntyProductId() {
        return entyProductId;
    }

    public void setEntyProductId(Long entyProductId) {
        this.entyProductId = entyProductId;
    }

    public HierarchyContext getCustomerHierarchyContext() {
        return customerHierarchyContext;
    }

    public void setCustomerHierarchyContext(HierarchyContext customerHierarchyContext) {
        this.customerHierarchyContext = customerHierarchyContext;
    }

    public List<GenericEntityRelationship> getHierarchyContextCurrentPath() {
        if(hierarchyContextCurrentPath==null){
            hierarchyContextCurrentPath = new ArrayList<GenericEntityRelationship>();
        }

        return hierarchyContextCurrentPath;
    }

    public void setHierarchyContextCurrentPath(List<GenericEntityRelationship> hierarchyContextCurrentPath) {
        this.hierarchyContextCurrentPath = hierarchyContextCurrentPath;
    }

    public List<GenericEntityRelationship> getHierarchyContextSuggestPath() {
        if(hierarchyContextSuggestPath==null){
            hierarchyContextSuggestPath = new ArrayList<GenericEntityRelationship>();
        }

        return hierarchyContextSuggestPath;
    }

    public void setHierarchyContextSuggestPath(List<GenericEntityRelationship> hierarchyContextSuggestPath) {
        this.hierarchyContextSuggestPath = hierarchyContextSuggestPath;
    }

    public Integer getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(Integer commodityCode) {
        this.commodityCode = commodityCode;
    }
    public Integer getClassCode() {
        return classCode;
    }

    public void setClassCode(Integer classCode) {
        this.classCode = classCode;
    }
    public Map<Long,List<GenericEntityRelationship>> getLowestLevel() {
        if(lowestLevel==null){
            this.lowestLevel = new HashMap<Long,List<GenericEntityRelationship>>();
        }
        return lowestLevel;
    }

    public void setLowestLevel(Map<Long,List<GenericEntityRelationship>> lowestLevel) {
        this.lowestLevel = lowestLevel;
    }
}
