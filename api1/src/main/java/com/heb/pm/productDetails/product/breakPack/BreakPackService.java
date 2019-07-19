/*
 *  BreakPackService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.breakPack;

import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.ProductRelationship;
import com.heb.pm.entity.ProductRelationshipKey;
import com.heb.pm.entity.SellingUnit;
import com.heb.pm.repository.ProductRelationshipRepository;
import com.heb.pm.repository.SellingUnitRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.heb.pm.audit.AuditService;
import com.heb.util.audit.AuditRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds all business logic related to break pack information.
 *
 * @author vn70516
 * @since 2.15.0
 */
@Service
public class BreakPackService {

    private static final Logger logger = LoggerFactory.getLogger(BreakPackService.class);

    public static final String BPACK_PRODUCT_RELATIONSHIP_CODE = "BPACK";

    @Autowired
    private ProductRelationshipRepository repository;
    @Autowired
    private SellingUnitRepository sellingUnitRepository;
    @Autowired
    private ProductManagementServiceClient productManagementServiceClient;
    @Autowired
    private AuditService auditService;

    /**
     * Count all product break pack hierarchy by product id.
     * @param productId - The product id
     * @return The list of ProductRelationship object
     * @throws Exception
     */
    public int countProductRelationshipByProductId(Long productId) throws Exception {
        return this.repository.countByKeyRelatedProductIdAndKeyProductRelationshipCodeOrKeyProductIdAndKeyProductRelationshipCode(productId,
                BPACK_PRODUCT_RELATIONSHIP_CODE, productId, BPACK_PRODUCT_RELATIONSHIP_CODE);
    }

    /**
     * Get all product break pack hierarchy by product id.
     * @param productId - The product id
     * @return The list of ProductRelationship object
     * @throws Exception
     */
    public List<ProductRelationship> getProductBreakPackByProductId(Long productId) throws Exception {
        List<ProductRelationship> outersBreakPack = new ArrayList<>();
        List<Long> parentIds = new ArrayList<>();
        List<Long> parentNodes = new ArrayList<>();
        parentNodes.add(productId);
        parentIds.add(productId);
        boolean flag = true;
        while(flag){
            flag = this.findParentBreakPackByProductId(parentIds, parentNodes);
        }
        //find break pack full path
        List<Long> allNodes = new ArrayList<>();
        List<Long> currentSearch = (List<Long>)ObjectUtils.clone(parentIds);
        Map<Long, List<ProductRelationship>> mapRelatedProduct = new HashMap<>();
        flag = true;
        while (flag){
            flag = this.findChildrenBreakPackByProductId(currentSearch, allNodes, mapRelatedProduct);
        }
        if(mapRelatedProduct != null && !mapRelatedProduct.isEmpty()) {
            for (Long prodId : parentIds) {
                ProductRelationship relationship = new ProductRelationship();
                ProductRelationshipKey relationshipKey = new ProductRelationshipKey(prodId, BPACK_PRODUCT_RELATIONSHIP_CODE, Long.MIN_VALUE);
                relationship.setKey(relationshipKey);
                relationship.setChildren(mapRelatedProduct.get(prodId));
                relationship.setParentNodes(parentNodes);
                if (mapRelatedProduct.get(prodId) != null) {
                    if (mapRelatedProduct.get(prodId).get(0).getRelatedProduct() != null) {
                        mapRelatedProduct.get(prodId).get(0).getRelatedProduct().getProdId();
                        if (mapRelatedProduct.get(prodId).get(0).getRelatedProduct().getProductPrimarySellingUnit() != null) {
                            mapRelatedProduct.get(prodId).get(0).getRelatedProduct().getProductPrimarySellingUnit().getUpc();
                        }
                    }
                    relationship.setParentProduct(mapRelatedProduct.get(prodId).get(0).getRelatedProduct());
                }
                this.setChildrenNode(relationship, mapRelatedProduct);
                outersBreakPack.add(relationship);
            }
        }
        return outersBreakPack;
    }

    /**
     * Find the list of product id related to the list of product id
     * @param childrenIds   - The list of product id to find all product id related to them.
     * @param allNodes      - The array list contain all node, that for check for case data infinite loop. Need
     *                      handle break loop
     * @return flag stop loop
     * @throws Exception
     */
    private boolean findParentBreakPackByProductId(List<Long> childrenIds, List<Long> allNodes) throws
            Exception{
        boolean flagContinue = false;
        List<ProductRelationship> parentBreakPacks = this.repository
                .findByKeyProductIdInAndKeyProductRelationshipCode(childrenIds, BPACK_PRODUCT_RELATIONSHIP_CODE);
        if(parentBreakPacks != null && !parentBreakPacks.isEmpty()) {
            for(ProductRelationship productRelationship : parentBreakPacks){
                if(!allNodes.contains(productRelationship.getKey().getRelatedProductId())) {
                    childrenIds.add(productRelationship.getKey().getRelatedProductId());
                    flagContinue = true;
                }
                childrenIds.remove(productRelationship.getKey().getProductId());
                allNodes.add(productRelationship.getKey().getRelatedProductId());
            }
        }
        return flagContinue;
    }

    /**
     * Show tree product break pack by root product id.
     * @param parentIds - The list of product id to find all children product id
     * @param allNodes  - The array list contain all node, that for check for case data infinite loop. Need
     *                    handle break loop
     * @param mapRelatedProduct - Map contain the list of product relationship
     * @return flag stop loop
     * @throws Exception
     */
    private boolean findChildrenBreakPackByProductId(List<Long> parentIds, List<Long> allNodes, Map<Long, List<ProductRelationship>> mapRelatedProduct) throws Exception{
        boolean flagContinue = false;
        parentIds.removeAll(allNodes);
        List<ProductRelationship> childrenBreakPacks = this.repository
                .findByKeyRelatedProductIdInAndKeyProductRelationshipCode(parentIds, BPACK_PRODUCT_RELATIONSHIP_CODE);
        if(childrenBreakPacks != null && !childrenBreakPacks.isEmpty()) {
            for(ProductRelationship productRelationship : childrenBreakPacks){
                parentIds.remove(productRelationship.getKey().getRelatedProductId());
                parentIds.add(productRelationship.getKey().getProductId());
                if(mapRelatedProduct.get(productRelationship.getKey().getRelatedProductId()) == null){
                    mapRelatedProduct.put(productRelationship.getKey().getRelatedProductId(), new ArrayList<>());
                }
                mapRelatedProduct.get(productRelationship.getKey().getRelatedProductId()).add(productRelationship);
                flagContinue = true;
                allNodes.add(productRelationship.getKey().getRelatedProductId());
            }
        }
        return flagContinue;
    }

    /**
     * The method recursive to set children for all node.
     * @param relationship - The current product relationship
     * @param mapRelatedProduct - Map contain the list of product relationship
     */
    private void setChildrenNode(ProductRelationship relationship, Map<Long, List<ProductRelationship>> mapRelatedProduct){
        if(relationship.getChildren() != null) {
            relationship.getChildren().forEach(p -> {
                p.setChildren(mapRelatedProduct.get(p.getKey().getProductId()));
                setChildrenNode(p, mapRelatedProduct);
            });
        }
    }

    /**
     * Get all product information by upc number.
     * @param productId - The related product id
     * @param upc - The upc number
     * @return The ProductRelationship object contain product information will be relate to current product.
     * @throws Exception
     */
    public ProductRelationship getProductByUpc(long productId, long upc) throws Exception{
        SellingUnit sellingUnit = this.sellingUnitRepository.findOne(upc);
        ProductRelationship productRelationship = new ProductRelationship();
        if(sellingUnit != null && sellingUnit.getProductMaster() != null) {
            sellingUnit.getProductMaster().getProdId();
            ProductMaster productMaster = sellingUnit.getProductMaster();
            productRelationship.setKey(new ProductRelationshipKey(productMaster.getProdId(), BPACK_PRODUCT_RELATIONSHIP_CODE, productId));
            productRelationship.setParentProduct(productMaster);
        }else{
            productRelationship.setKey(new ProductRelationshipKey());
            productRelationship.setParentProduct(new ProductMaster());
            productRelationship.getParentProduct().setProductPrimarySellingUnit(new SellingUnit());
        }
        return productRelationship;
    }

    /**
     * Call webservice to update product relationship.
     *
     * @param productRelationship the ProductRelationship entity to be updated.
     * @return the modified entity.
     */
    public void updateProductRelationship(ProductRelationship productRelationship) {
        this.productManagementServiceClient.updateProductRelationships(productRelationship.getChildren());
    }
    /**
     * This returns a list of online attributes audits based on the prodId.
     * @param prodId way to uniquely ID the set of online attributes audits requested
     * @return a list of all the changes made to an product's online attributes audits.
     */
    List<AuditRecord> getBreakPackAttributesAuditInformation(Long prodId) {
        return this.auditService.getBreakPackAttributesAuditInformation(prodId);
    }
}