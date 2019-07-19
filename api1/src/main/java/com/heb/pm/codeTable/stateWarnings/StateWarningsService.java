/*
 *  StateWarningsService.java
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.stateWarnings;

import com.heb.pm.entity.ProductStateWarning;
import com.heb.pm.repository.ProductStateWarningRepository;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Code table state warnings service.
 *
 * @author vn00602
 * @since 2.12.0
 */
@Service
public class StateWarningsService {

    /**
     * Repository to process State Warning table
     */
    @Autowired
    private ProductStateWarningRepository productStateWarningRepository;

    @Autowired
    private CodeTableManagementServiceClient serviceClient;

    /**
     * find all state warning with default sort by warning code.
     *
     * @return list state warning
     */
    public List<ProductStateWarning> findAllStateWarnings() {
        return productStateWarningRepository.findAll(ProductStateWarning.getDefaultSort());
    }

    /**
     * Add new a list of state warnings.
     *
     * @param stateWarnings the list of new state warnings to add.
     */
    public void addStateWarnings(List<ProductStateWarning> stateWarnings) {
        this.serviceClient.updateStateWarnings(stateWarnings, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
    }

    /**
     * Update a list of state warnings.
     *
     * @param stateWarnings the list of new state warnings to update.
     */
    public void updateStateWarning(List<ProductStateWarning> stateWarnings) {
        this.serviceClient.updateStateWarnings(stateWarnings, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
    }

    /**
     * Delete a list of state warnings.
     *
     * @param stateWarnings the list of new state warnings to delete.
     */
    public void deleteStateWarning(List<ProductStateWarning> stateWarnings) {
        this.serviceClient.updateStateWarnings(stateWarnings, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
    }
}
