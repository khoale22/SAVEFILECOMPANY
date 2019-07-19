/*
 *  ProductAttributeOverwriteRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductAttributeOverwrite;
import com.heb.pm.entity.ProductAttributeOverwriteKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository for Product Attribute Overwrite.
 *
 * @author vn70516
 * @since 2.14.0
 */
public interface ProductAttributeOverwriteRepository extends JpaRepository<ProductAttributeOverwrite, ProductAttributeOverwriteKey> {

   /**
     * Returns the ProductAttributeOverwrite by itemProductKeyCode, productId and attributeId.
     *
     * @param itemProductKeyCode the itemProductKeyCode.
     * @param productId the id of product.
     * @param logicAttributeId the id of attribute.
     * @return the ProductAttributeOverwrite;
     */
	ProductAttributeOverwrite findFirstByKeyItemProductKeyCodeAndKeyKeyIdAndKeyLogicAttributeId(String itemProductKeyCode, long productId, long logicAttributeId);
    /**
     * Returns the ProductAttributeOverwrite by itemProductKeyCode, productId and attributeId.
     *
     * @param itemProductKeyCode the itemProductKeyCode.
     * @param productId the id of product.
     * @param logicAttributeId the id of attribute.
     * @return the ProductAttributeOverwrite;
     */
    ProductAttributeOverwrite findByKeyItemProductKeyCodeAndKeyKeyIdAndKeyLogicAttributeId(String itemProductKeyCode, long productId, long logicAttributeId);
    /**
     * Returns the list of ProductAttributeOverwrite by itemProductKeyCode, productId and list of attributeId.
     *
     * @param itemProductKeyCode the itemProductKeyCode.
     * @param productId the id of product.
     * @param logicAttributeIds the list of attribute id.
     * @return the ProductAttributeOverwrite;
     */
    List<ProductAttributeOverwrite> findByKeyItemProductKeyCodeAndKeyKeyIdAndKeyLogicAttributeIdIn(String itemProductKeyCode,
                                                                                                    long productId,
                                                                                                    List<Long> logicAttributeIds);

}
