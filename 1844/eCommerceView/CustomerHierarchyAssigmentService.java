/*
 *  CustomerHierarchyAssigmentService.
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.eCommerceView;

import com.heb.pm.entity.GenericEntity;
import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.entity.HierarchyContext;
import com.heb.pm.repository.GenericEntityRelationshipRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Holds all business logic related to Hierarchy Assigment.
 *
 * @author vn55306
 * @since 2.14.0
 */
@Service
public class CustomerHierarchyAssigmentService {
    private String PRIMARY_PATH="Primary";
    private String ALTERNATE_PATH="Alternate";
    public static final String FONT_COLOR_RED_HTML_START = " <font color='#009900'>";
    public static final String FONT_TAG_HTML_END = " </font>";
    public static final String ARROW_HTML_CODE = "&#8594";
    /**
     * find Path of tree.
     * @param genericEntityRelationships
     *            the (List<GenericEntityRelationship>
     * @param parrrentPath
     *           String
     * @param parrentStylePath
     *         String
     * @param parrentDefault
     *           Boolean
     * @author vn55306
     */
    public void setPathTree(List<GenericEntityRelationship> genericEntityRelationships,String parrrentPath,String parrentStylePath,Boolean parrentDefault){
        for(GenericEntityRelationship genericEntityRelationship:genericEntityRelationships) {
            if(genericEntityRelationship.getChildDescription()!=null) {
                if (parrrentPath!=null) {
                    if (genericEntityRelationship.getDefaultParent()) {
                        genericEntityRelationship.setPath(parrrentPath + ARROW_HTML_CODE + genericEntityRelationship.getChildDescription().getShortDescription());
                        genericEntityRelationship.setPathStyle(parrentStylePath + FONT_COLOR_RED_HTML_START + ARROW_HTML_CODE + genericEntityRelationship.getChildDescription().getShortDescription() + FONT_TAG_HTML_END);
                    } else {
                        genericEntityRelationship.setPath(parrrentPath + ARROW_HTML_CODE + genericEntityRelationship.getChildDescription().getShortDescription());
                        genericEntityRelationship.setPathStyle(parrentStylePath + ARROW_HTML_CODE + genericEntityRelationship.getChildDescription().getShortDescription());
                    }
                } else {
                    genericEntityRelationship.setAllowPrimaryPath(true);
                    genericEntityRelationship.setPath(genericEntityRelationship.getChildDescription().getShortDescription());
                    genericEntityRelationship.setPathStyle(FONT_COLOR_RED_HTML_START +genericEntityRelationship.getChildDescription().getShortDescription()+ FONT_TAG_HTML_END);
                }
            }
            if(parrentDefault!=null){
                if(parrentDefault==false){
                    genericEntityRelationship.setDefaultParent(parrentDefault);
                }
            }
            if(genericEntityRelationship.getChildRelationships()!=null && !genericEntityRelationship.getChildRelationships().isEmpty()){
                this.setPathTree(genericEntityRelationship.getChildRelationships(),genericEntityRelationship.getPath(),genericEntityRelationship.getPathStyle(),genericEntityRelationship.getDefaultParent());
            } else if(StringUtils.isNotBlank(genericEntityRelationship.getPath())){
                genericEntityRelationship.setLowestLevel(true);
                if(genericEntityRelationship.getDefaultParent()){
                    genericEntityRelationship.setAllowPrimaryPath(true);
                }
            }
        }
    }
    /**
     * find Customer Hierarchy Assignment Suggest Path.
     * @param customerHierarchyAssignment
     *            the CustomerHierarchyAssigment
     * @param hierarchyContext
     *           List<GenericEntityRelationship>
     * @param lowestLevelByByCurrentPath
     *         List<GenericEntityRelationship>
     * @param lowestLevelBySubComm
     *            List<Long>
     * @param lowestLevelByBrick
     *            List<Long>
     * @author vn55306
     */
    public void findCustomerHierarchyAssignmentPath(CustomerHierarchyAssigment customerHierarchyAssignment,List<GenericEntityRelationship> hierarchyContext,List<GenericEntityRelationship> lowestLevelByByCurrentPath,List<GenericEntityRelationship> lowestLevelBySubComm,List<GenericEntityRelationship> lowestLevelByBrick){
        Map<Long, Boolean> mapHierarchyCurrent = new HashMap<Long, Boolean>();
        List<GenericEntityRelationship> subCommAndBrick = this.findHierarchyContextBySubCommAndBrick(lowestLevelBySubComm,lowestLevelByBrick);
        Map<Long, List<GenericEntityRelationship>> mapCntBrickSubComMatch = findMatchAndCountPath(subCommAndBrick);
        Map<Long, List<GenericEntityRelationship>> mapCntSubComMatch = findMatchAndCountPath(lowestLevelBySubComm);
        Map<Long, List<GenericEntityRelationship>> mapCntBrickMatch = findMatchAndCountPath(lowestLevelByBrick);
        if(lowestLevelByByCurrentPath!=null && !lowestLevelByByCurrentPath.isEmpty()) {
            lowestLevelByByCurrentPath.forEach(lowestLevel -> {
               mapHierarchyCurrent.put(lowestLevel.getKey().getParentEntityId(), lowestLevel.getDefaultParent());
            });
        }
        this.findSuggestionPath(customerHierarchyAssignment,hierarchyContext,mapHierarchyCurrent,mapCntBrickSubComMatch,mapCntSubComMatch,mapCntBrickMatch);
        Collections.sort(customerHierarchyAssignment.getHierarchyContextCurrentPath(), new Comparator<GenericEntityRelationship>() {
            /**
             * Compare.
             * @param attr1
             *            the attr1
             * @param attr2
             *            the attr2
             * @return the int
             */
            @Override
            public int compare(GenericEntityRelationship attr1, GenericEntityRelationship attr2) {
                return (attr1.getDefaultParentValue().compareTo(attr2.getDefaultParentValue())) * (-1);
            }
        });
        Collections.sort(customerHierarchyAssignment.getHierarchyContextSuggestPath(), new Comparator<GenericEntityRelationship>() {
            /**
             * Compare.
             * @param attr1
             *            the attr1
             * @param attr2
             *            the attr2
             * @return the int
             */
            @Override
            public int compare(GenericEntityRelationship attr1, GenericEntityRelationship attr2) {
                return (attr1.getCountOfProductChildren()- attr2.getCountOfProductChildren()) * (-1);
            }
        });
    }
    /**
     * Group Product by Entity Id.
     * @param lstGenericEntityRelationship
     *            List<GenericEntityRelationship>
     * @return Map<Long, List<GenericEntityRelationship>>
     * @author vn55306
     */
    private static Map<Long, List<GenericEntityRelationship>> findMatchAndCountPath(List<GenericEntityRelationship> lstGenericEntityRelationship) {
        Map<Long, List<GenericEntityRelationship>> map = new HashMap<Long, List<GenericEntityRelationship>>();
        if (lstGenericEntityRelationship != null) {
            for (GenericEntityRelationship genericEntityRelationship : lstGenericEntityRelationship) {
                if (map.containsKey(genericEntityRelationship.getKey().getParentEntityId())) {
                    map.get(genericEntityRelationship.getKey().getParentEntityId()).add(genericEntityRelationship);
                } else {
                    map.put(genericEntityRelationship.getKey().getParentEntityId(), new ArrayList<GenericEntityRelationship>());
                    map.get(genericEntityRelationship.getKey().getParentEntityId()).add(genericEntityRelationship);
                }
            }
        }
        return map;
    }

    /**
     * find Suggestion Path.
     * @param customerHierarchyAssigment
     *            the CustomerHierarchyAssigment
     * @param genericEntityRelationshipRoots
     *            List<GenericEntityRelationship>
     * @param mapHierarchyCurrent
     *              Map<Long, Boolean>
     * @param  mapCntBrickSubComMatch
     *         Map<Long, List<GenericEntityRelationship>>
     * @param  mapCntSubComMatch
     *         Map<Long, List<GenericEntityRelationship>>
     * @param  mapCntBrickMatch
     *         Map<Long, List<GenericEntityRelationship>>
     * @author vn55306
     */
    private void findSuggestionPath(CustomerHierarchyAssigment customerHierarchyAssigment, List<GenericEntityRelationship> genericEntityRelationshipRoots,Map<Long, Boolean> mapHierarchyCurrent, Map<Long, List<GenericEntityRelationship>> mapCntBrickSubComMatch,Map<Long, List<GenericEntityRelationship>> mapCntSubComMatch,Map<Long, List<GenericEntityRelationship>> mapCntBrickMatch){
         for(GenericEntityRelationship genericEntityRelationship :genericEntityRelationshipRoots) {
            if ((mapCntBrickSubComMatch.containsKey(genericEntityRelationship.getKey().getChildEntityId()) || mapCntSubComMatch.containsKey(genericEntityRelationship.getKey().getChildEntityId())
                    ||mapCntBrickMatch.containsKey(genericEntityRelationship.getKey().getChildEntityId()))&& genericEntityRelationship.getKey().getHierarchyContext().trim().equalsIgnoreCase(customerHierarchyAssigment.getHierachyContextCode())){
                GenericEntityRelationship genericEntity = new GenericEntityRelationship();
                genericEntity.setDefaultParentValue(ALTERNATE_PATH);
                genericEntity.setSuggestHierarchy(true);
                genericEntity.setAllowPrimaryPath(genericEntityRelationship.isAllowPrimaryPath());
                customerHierarchyAssigment.setProductId(customerHierarchyAssigment.getProductId());
                if(mapCntBrickSubComMatch.containsKey(genericEntityRelationship.getKey().getChildEntityId())){
                    genericEntity.setCountOfProductChildren(mapCntBrickSubComMatch.get(genericEntityRelationship.getKey().getChildEntityId()).size());
                    genericEntity.setMatchBy(customerHierarchyAssigment.getSubCommodityName()+"<br/>"+customerHierarchyAssigment.getBrick());
                } else if(mapCntSubComMatch.containsKey(genericEntityRelationship.getKey().getChildEntityId())){
                    genericEntity.setCountOfProductChildren(mapCntSubComMatch.get(genericEntityRelationship.getKey().getChildEntityId()).size());
                    genericEntity.setMatchBy(customerHierarchyAssigment.getSubCommodityName());
                }else if(mapCntBrickMatch.containsKey(genericEntityRelationship.getKey().getChildEntityId())){
                    genericEntity.setCountOfProductChildren(mapCntBrickMatch.get(genericEntityRelationship.getKey().getChildEntityId()).size());
                    genericEntity.setMatchBy(customerHierarchyAssigment.getBrick());
                }
                genericEntity.setPath(genericEntityRelationship.getPath());
                genericEntity.setPathStyle(genericEntityRelationship.getPathStyle());
                genericEntity.setKey(genericEntityRelationship.getKey());
                genericEntity.setProductId(customerHierarchyAssigment.getProductId());
                if(mapHierarchyCurrent.containsKey(genericEntityRelationship.getKey().getChildEntityId())){
                    if(!this.isExistPath(customerHierarchyAssigment.getHierarchyContextCurrentPath(),genericEntity)) {
                        if (mapHierarchyCurrent.get(genericEntityRelationship.getKey().getChildEntityId()) && genericEntityRelationship.getDefaultParent()) {
                            genericEntity.setDefaultParentValue(PRIMARY_PATH);
                        } else {
                            genericEntity.setDefaultParentValue(ALTERNATE_PATH);
                        }
                        customerHierarchyAssigment.getHierarchyContextCurrentPath().add(genericEntity);
                    }
                } else {
                    if(!this.isExistPath(customerHierarchyAssigment.getHierarchyContextSuggestPath(),genericEntity)) {
                        customerHierarchyAssigment.getHierarchyContextSuggestPath().add(genericEntity);
                    }
                }
                if(customerHierarchyAssigment.getLowestLevel().containsKey(genericEntity.getKey().getChildEntityId())){
                    customerHierarchyAssigment.getLowestLevel().get(genericEntity.getKey().getChildEntityId()).add(genericEntity);
                } else {
                    List<GenericEntityRelationship> lowestLevel = new ArrayList<GenericEntityRelationship>();
                    lowestLevel.add(genericEntity);
                    customerHierarchyAssigment.getLowestLevel().put(genericEntity.getKey().getChildEntityId(),lowestLevel);
                }
            } else {
                if (genericEntityRelationship.getChildRelationships() != null && !genericEntityRelationship.getChildRelationships().isEmpty()) {
                    if(genericEntityRelationship.isChildRelationshipOfProductEntityType()){
                        this.addHierarchyLowestLevel(customerHierarchyAssigment,genericEntityRelationship);
                    }
                    this.findSuggestionPath(customerHierarchyAssigment, genericEntityRelationship.getChildRelationships(),mapHierarchyCurrent, mapCntBrickSubComMatch,mapCntSubComMatch,mapCntBrickMatch);
                } else {
                   this.addHierarchyLowestLevel(customerHierarchyAssigment,genericEntityRelationship);
                }
            }
        }
    }
    /**
     * Check Exist path.
     * @param genericEntityRelationships
     *            the List<GenericEntityRelationship>
     * @param genericEntityRelationshipcheck
     *            the GenericEntityRelationship
     * @return false : not existed
     *          true: existed
     * @author vn55306
     */
    private boolean isExistPath(List<GenericEntityRelationship> genericEntityRelationships,GenericEntityRelationship genericEntityRelationshipcheck){
        if(genericEntityRelationships!=null && !genericEntityRelationships.isEmpty()){
           for(GenericEntityRelationship genericEntityRelationship:genericEntityRelationships){
               if(genericEntityRelationship.getPath()!=null && genericEntityRelationship.getPath().equals(genericEntityRelationshipcheck.getPath())){
                   return true;
               }
//                if(genericEntityRelationship.getKey().getParentEntityId().equals(genericEntityRelationshipcheck.getKey().getParentEntityId())
//                        && genericEntityRelationship.getKey().getChildEntityId().equals(genericEntityRelationshipcheck.getKey().getChildEntityId())){
//                    return true;
//                }
            };
        }
        return false;
    }
    /**
     * Find all Lowest Level.
     * @param customerHierarchyAssigment
     *            the CustomerHierarchyAssigment
     * @param genericEntityRelationship
     *            the GenericEntityRelationship
     * @return List<GenericEntityRelationship>
     * @author vn55306
     */
    public void addHierarchyLowestLevel(CustomerHierarchyAssigment customerHierarchyAssigment,GenericEntityRelationship genericEntityRelationship){
        GenericEntityRelationship genericEntity = new GenericEntityRelationship();
        genericEntity.setDefaultParentValue(ALTERNATE_PATH);
        genericEntity.setPath(genericEntityRelationship.getPath());
        genericEntity.setPathStyle(genericEntityRelationship.getPathStyle());
        genericEntity.setKey(genericEntityRelationship.getKey());
        genericEntity.setAllowPrimaryPath(genericEntityRelationship.isAllowPrimaryPath());
        if(customerHierarchyAssigment.getLowestLevel().containsKey(genericEntity.getKey().getChildEntityId())){
            customerHierarchyAssigment.getLowestLevel().get(genericEntity.getKey().getChildEntityId()).add(genericEntity);
        } else {
            List<GenericEntityRelationship> lowestLevel = new ArrayList<GenericEntityRelationship>();
            lowestLevel.add(genericEntity);
            customerHierarchyAssigment.getLowestLevel().put(genericEntity.getKey().getChildEntityId(),lowestLevel);
        }
    }
    /**
     * find Hierarchy Context By SubComm And Brick.
     * @param hierarchyContextBySubComm
     *            the List<GenericEntityRelationship>
     * @param hierarchyContextByBrick
     *            the List<GenericEntityRelationship>
     * @return List<GenericEntityRelationship>
     * @author vn55306
     */
    private List<GenericEntityRelationship> findHierarchyContextBySubCommAndBrick(List<GenericEntityRelationship> hierarchyContextBySubComm,List<GenericEntityRelationship> hierarchyContextByBrick){
        List<GenericEntityRelationship> hierarchyContextBySubCommAndBrick = new ArrayList<GenericEntityRelationship>();
        boolean subComBrick;
        if(hierarchyContextBySubComm!=null && hierarchyContextByBrick!=null) {
            for (int i = 0; i < hierarchyContextBySubComm.size() - 1; i++) {
                if (hierarchyContextBySubComm.get(i) != null) {
                    subComBrick = false;
                    for (int j = 0; j < hierarchyContextByBrick.size() - 1; j++) {
                        if (hierarchyContextBySubComm.get(i).getKey().getParentEntityId().equals(hierarchyContextByBrick.get(j).getKey().getParentEntityId())
                                && hierarchyContextBySubComm.get(i).getGenericChildEntity().getDisplayNumber().equals(hierarchyContextByBrick.get(j).getGenericChildEntity().getDisplayNumber())) {
                            hierarchyContextBySubCommAndBrick.add(hierarchyContextBySubComm.get(i));
                            subComBrick = true;
                            hierarchyContextByBrick.remove(j);
                            break;
                        }
                    }
                    if (subComBrick) {
                        hierarchyContextBySubComm.remove(i--);
                    }
                }
            }
        }
        return hierarchyContextBySubCommAndBrick;
    }
    /**
     * find Customer Hierarchy Assignment Suggest Path.
     * @param customerHierarchyAssignment
     *            the CustomerHierarchyAssigment
     * @param hierarchyContext
     *           List<GenericEntityRelationship>
     * @param lowestLevelByByCurrentPath
     *         List<GenericEntityRelationship>
     * @author vn70633
     */
    public void findCustomerHierarchyAssignmentCurrentPath(CustomerHierarchyAssigment customerHierarchyAssignment,List<GenericEntityRelationship> hierarchyContext,List<GenericEntityRelationship> lowestLevelByByCurrentPath){
        Map<Long, Boolean> mapHierarchyCurrent = new HashMap<Long, Boolean>();
        if(lowestLevelByByCurrentPath!=null && !lowestLevelByByCurrentPath.isEmpty()) {
            lowestLevelByByCurrentPath.forEach(lowestLevel -> {
                mapHierarchyCurrent.put(lowestLevel.getKey().getParentEntityId(), lowestLevel.getDefaultParent());
            });
        }
        this.findCurrentPath(customerHierarchyAssignment,hierarchyContext,mapHierarchyCurrent);
        Collections.sort(customerHierarchyAssignment.getHierarchyContextCurrentPath(), new Comparator<GenericEntityRelationship>() {
            /**
             * Compare.
             * @param attr1
             *            the attr1
             * @param attr2
             *            the attr2
             * @return the int
             */
            @Override
            public int compare(GenericEntityRelationship attr1, GenericEntityRelationship attr2) {
                return (attr1.getDefaultParentValue().compareTo(attr2.getDefaultParentValue())) * (-1);
            }
        });
    }
    /**
     * find Suggestion Path.
     * @param customerHierarchyAssigment
     *            the CustomerHierarchyAssigment
     * @param genericEntityRelationshipRoots
     *            List<GenericEntityRelationship>
     * @param mapHierarchyCurrent
     *              Map<Long, Boolean>
     * @author vn70633
     */
    private void findCurrentPath(CustomerHierarchyAssigment customerHierarchyAssigment, List<GenericEntityRelationship> genericEntityRelationshipRoots,Map<Long, Boolean> mapHierarchyCurrent){
        for(GenericEntityRelationship genericEntityRelationship :genericEntityRelationshipRoots) {
            if (genericEntityRelationship.getKey().getHierarchyContext().trim().equalsIgnoreCase(customerHierarchyAssigment.getHierachyContextCode())){
                GenericEntityRelationship genericEntity = new GenericEntityRelationship();
                genericEntity.setDefaultParentValue(ALTERNATE_PATH);
                genericEntity.setSuggestHierarchy(true);
                genericEntity.setAllowPrimaryPath(genericEntityRelationship.isAllowPrimaryPath());
                customerHierarchyAssigment.setProductId(customerHierarchyAssigment.getProductId());
                genericEntity.setPath(genericEntityRelationship.getPath());
                genericEntity.setPathStyle(genericEntityRelationship.getPathStyle());
                genericEntity.setKey(genericEntityRelationship.getKey());
                genericEntity.setProductId(customerHierarchyAssigment.getProductId());
                if (genericEntityRelationship.getChildRelationships() != null && !genericEntityRelationship.getChildRelationships().isEmpty()) {
                    if(genericEntityRelationship.isChildRelationshipOfProductEntityType()){
                        this.addHierarchyLowestLevel(customerHierarchyAssigment,genericEntityRelationship);
                    }
                } else {
                    this.addHierarchyLowestLevel(customerHierarchyAssigment,genericEntityRelationship);
                }
                if(mapHierarchyCurrent.containsKey(genericEntityRelationship.getKey().getChildEntityId())){
                    if(!this.isExistPath(customerHierarchyAssigment.getHierarchyContextCurrentPath(),genericEntity)) {
                        if (mapHierarchyCurrent.get(genericEntityRelationship.getKey().getChildEntityId()) && genericEntityRelationship.getDefaultParent()) {
                            genericEntity.setDefaultParentValue(PRIMARY_PATH);
                        } else {
                            genericEntity.setDefaultParentValue(ALTERNATE_PATH);
                        }
                        customerHierarchyAssigment.getHierarchyContextCurrentPath().add(genericEntity);
                    }
                }
                else {
                    if (genericEntityRelationship.getChildRelationships() != null && !genericEntityRelationship.getChildRelationships().isEmpty()) {
                        this.findCurrentPath(customerHierarchyAssigment, genericEntityRelationship.getChildRelationships(),mapHierarchyCurrent);
                    }
                }
            }
        }
    }
}
