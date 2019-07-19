/*
 * ChoiceOptionRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ChoiceOption;
import com.heb.pm.entity.ChoiceOptionKey;
import com.heb.pm.entity.ChoiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA Repository for Choice Option.
 *
 * @author vn70516
 * @since 2.12.0
 */
public interface ChoiceOptionRepository extends JpaRepository<ChoiceOption, ChoiceOptionKey> {
    /**
     * Returns a list of all Choice Option of Choice Type.
     *
     * @param choiceTypeCode the Choice Type id.
     * @return a list of all Choice Option of Choice Type.
     */
    List<ChoiceOption> findByKeyChoiceTypeCode(String choiceTypeCode);
}
