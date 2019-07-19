/*
 *  ProductGroupChoiceOptionRepository.java
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductGroupChoiceOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/**
 * product group choice option repository
 *
 * @author vn70529
 * @since 2.15.0
 */
public interface ProductGroupChoiceOptionRepository extends JpaRepository<ProductGroupChoiceOption, String> {

    /**
     * Find the list of ProductGroupChoiceOption by productGroupTypeCode.
     * @param productGroupTypeCode the productGroupTypeCode.
     * @return List<ProductGroupChoiceOption>
     */
    List<ProductGroupChoiceOption> findByKeyProductGroupTypeCode(String productGroupTypeCode);

    /**
     * Find the list of ProductGroupChoiceOption by productGroupTypeCode.
     * @param productGroupTypeCode the product group type code.
     * @param productGroupTypeCode the choice type codes
     * @return List<ProductGroupChoiceOption>
     */
    List<ProductGroupChoiceOption> findByKeyProductGroupTypeCodeAndKeyChoiceTypeCodeIn(String productGroupTypeCode, List<String> choiceTypeCodes);

    /**
     * Find the list of ProductGroupChoiceOption by productGroupTypeCode and choiceTypeCode.
     * @param productGroupTypeCode the product group type code.
     * @param choiceTypeCode the choice type code.
     * @return List<ProductGroupChoiceOption>
     */
    List<ProductGroupChoiceOption> findByKeyProductGroupTypeCodeAndKeyChoiceTypeCode(String productGroupTypeCode, String choiceTypeCode);

}
