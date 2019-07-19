/*
 * IngredientStatementEventListener
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.scaleManagement;

import com.heb.pm.entity.AlertStaging;
import com.heb.pm.alert.AlertService;
import com.heb.pm.entity.IngredientStatementDetail;
import com.heb.pm.entity.ScaleUpc;
import com.heb.pm.entity.SellingUnit;
import com.heb.pm.repository.IngredientStatementDetailRepository;
import com.heb.pm.repository.ScaleUpcRepository;
import com.heb.pm.repository.SellingUnitRepository;
import com.heb.pm.ws.IngredientServiceClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The IngredientStatementEventListener implement the ApplicationListener interface.
 * Listener for update ingredient(and Statement) events.
 *
 * @author vn70529
 * @since 2.19.0
 */
@Component
public class IngredientStatementEventListener implements ApplicationListener<IngredientStatementEvent> {

    private static final String PRODUCT_ID_AS_ALERT_KEY_CONVERSION = "%09d";
    private static final String ALERT_TYPE_PRODUCT_UPDATE = "PRUPD";

    @Autowired
    private ScaleUpcRepository scaleUpcRepository;

    @Autowired
    private SellingUnitRepository sellingUnitRepository;

    @Autowired
    private AlertService alertService;

    @Autowired
    private IngredientServiceClient ingredientServiceClient;


    /**
     * Called when update ingredient(Statement).
     *
     * @param event the ingredientStatementEvent.
     */
    @Override
    public void onApplicationEvent(IngredientStatementEvent event) {
        if(event.getUpc() != null){
            this.createProductUpdateAlert(event.getUpc(), event.getCreatedBy());
        }else if(event.getIngredientStatementNumberChangeList() != null){
            for (Long ingredientStatementNumber : event.getIngredientStatementNumberChangeList()) {
                this.createProductUpdateAlertForUpdateIngredientStatement(ingredientStatementNumber, event.getCreatedBy());
            }
        }

    }

    /**
     * Create product update alert for ingredient statement.
     *
     * @param ingredientStatementNumber the ingredient statement number.
     * @param userId the user id.
     */
    public void createProductUpdateAlertForUpdateIngredientStatement(long ingredientStatementNumber, String userId){
        List<ScaleUpc> tiedUpcs = this.scaleUpcRepository.findByIngredientStatement(ingredientStatementNumber);
        if (CollectionUtils.isNotEmpty(tiedUpcs)) {
            for (ScaleUpc scaleUpc : tiedUpcs) {
                Long upc = scaleUpc.getUpc();
                this.createProductUpdateAlert(upc, userId);
            }

        }
    }

    /**
     * Create product update alert.
     *
     * @param upc the upc key.
     * @param userId the user id.
     */
    public void createProductUpdateAlert(Long upc, String userId){
        SellingUnit sellingUnit = this.sellingUnitRepository.findOne(upc);
        String alertKey = String.format(PRODUCT_ID_AS_ALERT_KEY_CONVERSION,sellingUnit.getProdId());
        AlertStaging alertStaging = this.alertService.findByAlertTypeCDAndAlertStatusCDAndAlertKey(
                ALERT_TYPE_PRODUCT_UPDATE, AlertStaging.AlertStatusCD.ACTIVE.getName(), alertKey);
        if (alertStaging != null && alertStaging.getAlertID() > 0){
            this.ingredientServiceClient.updateAlertForIngredientChange(alertStaging.getAlertID(), alertStaging.getAlertDataTxt(), userId);
        }else{
            this.ingredientServiceClient.createAlertForIngredientChange(alertKey, StringUtils.trim(sellingUnit
                    .getProductMaster()
                    .getClassCommodity
                            ().geteBMid()), userId);
        }
    }
}
