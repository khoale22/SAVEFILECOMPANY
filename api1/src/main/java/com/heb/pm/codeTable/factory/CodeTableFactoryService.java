/*
 *  CodeTableFactoryService
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.factory;

import com.heb.pm.entity.Country;
import com.heb.pm.entity.Factory;
import com.heb.pm.repository.CountryRepository;
import com.heb.pm.repository.FactoryRepository;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to code table factory information.
 *
 * @author vn70516
 * @since 2.12.0
 */
@Service
public class CodeTableFactoryService {

    @Autowired
    private FactoryRepository factoryRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CodeTableManagementServiceClient serviceClient;

    /**
     * Returns a list of all factory default order by factory id.
     *
     * @return A List of all factory default order by factory id.
     */
    public List<Factory> findAllFactory() {
        return this.factoryRepository.findAll(Factory.getDefaultSort());
    }

    /**
     * Returns a list of all country default order by country name.
     *
     * @return A List of all country default order by country name.
     */
    public List<Country> findAllCountry() {
        return this.countryRepository.findAll(Country.getDefaultSort());
    }

    /**
     * Delete a list of factory in code table.
     *
     * @param factories The list of factory requested to delete.
     */
    public void deleteFactories(List<Factory> factories) {
        this.serviceClient.updateFactories(factories, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE
                .getValue());
    }

    /**
     * Add new a factory in code table.
     *
     * @param factory A factory requested to insert.
     */
    public void addNewFactory(Factory factory) {
        List<Factory> factories = new ArrayList<>();
        factories.add(factory);
        this.serviceClient.updateFactories(factories, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD
                .getValue());
    }

    /**
     * Update information factory in code table.
     *
     * @param factory A factory requested to update.
     */
    public void updateFactory(Factory factory) {
        List<Factory> factories = new ArrayList<>();
        factories.add(factory);
        this.serviceClient.updateFactories(factories, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE
                .getValue());
    }
}