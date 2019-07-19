/*
 * PLUMaintenanceEventListener
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.scaleManagement;

import com.heb.pm.alert.AlertService;
import com.heb.pm.entity.AlertStaging;
import com.heb.pm.entity.ScaleUpc;
import com.heb.pm.entity.SellingUnit;
import com.heb.pm.repository.SellingUnitRepository;
import com.heb.pm.ws.PLUMaintenanceServiceClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * The PLUMaintenanceEventListener implement the ApplicationListener interface.
 * Listener for PLU maintenance events.
 *
 * @author vn70529
 * @since 2.20.0
 */
@Component
public class PLUMaintenanceEventListener implements ApplicationListener<PLUMaintenanceEvent> {

    private static final String PRODUCT_ID_AS_ALERT_KEY_CONVERSION = "%09d";
    private static final String ALERT_TYPE_PRODUCT_UPDATE = "PRUPD";

    @Autowired
    private SellingUnitRepository sellingUnitRepository;

    @Autowired
    private AlertService alertService;

    @Autowired
    private PLUMaintenanceServiceClient pluMaintenanceServiceClient;

    /**
     * Called when update ingredient(Statement).
     *
     * @param event the ingredientStatementEvent.
     */
    @Override
    public void onApplicationEvent(PLUMaintenanceEvent event) {
        if(event.getScaleUpcs() != null){
            for (ScaleUpc scaleUpc : event.getScaleUpcs()) {
                createProductUpdateAlert(scaleUpc.getUpc(), event.getCreatedBy());
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
            this.pluMaintenanceServiceClient.updateAlertForPLUMaintenance(alertStaging.getAlertID(), alertStaging.getAlertDataTxt(), userId);
        }else{
            this.pluMaintenanceServiceClient.createAlertForPLUMaintenance(alertKey, StringUtils.trim(sellingUnit
                    .getProductMaster()
                    .getClassCommodity
                            ().geteBMid()), userId);
        }
    }

}
