/*
* WICBaseDAO
*
* Copyright (c) 2018 H-E-B
* All rights reserved.
*
* This software is the confidential and proprietary information
* of H-E-B.
*/
package com.heb.batch.wic.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Properties;
@Service
public class JobParamDAO extends JdbcDaoSupport {
    private static final Logger LOGGER = LogManager.getLogger(JobParamDAO.class);
    public static final String SCHEMA_SQL ="[schema]";
    public static final String J50X100_UPDATE_PRODUCT_SCAN_CODE_ORACLE_URL="J50X100_UPDATE_PRODUCT_SCAN_CODE_ORACLE_URL";
    public static final String J50X100_INSERT_WIC_CAT_ORACLE_URL="J50X100_INSERT_WIC_CAT_ORACLE_URL";
    public static final String J50X100_INSERT_WIC_SUB_CAT_ORACLE_URL="J50X100_INSERT_WIC_SUB_CAT_ORACLE_URL";
    public static final String J50X100_INSERT_PRODUCT_SCAN_CODE_WIC_ORACLE_URL="J50X100_INSERT_PRODUCT_SCAN_CODE_WIC_ORACLE_URL";
    public static final String J50X100_UPDATE_PRODUCT_SCAN_CODE_WIC_ORACLE_URL="J50X100_UPDATE_PRODUCT_SCAN_CODE_WIC_ORACLE_URL";
    public static final String J50X100_DELETE_PRODUCT_SCAN_CODE_WIC_ORACLE_URL="J50X100_DELETE_PRODUCT_SCAN_CODE_WIC_ORACLE_URL";
    public static final String J50X100_APIKEY="J50X100_APIKEY";
    public static final String J50X100_INPUT_NAS="J50X100_INPUT_NAS";
    public static final String J50X100_EMAIL_ADDRESS_PRODUCTION_SUPPORT="J50X100_EMAIL_ADDRESS_PRODUCTION_SUPPORT";
    public static final String J50X100_EMAIL_FROM="J50X100_EMAIL_FROM";
    public static final String J50X100_EMAIL_WS_ENDPOINT="J50X100_EMAIL_WS_ENDPOINT";
    public static final String J50X100_EMAIL_WS_APIKEY="J50X100_EMAIL_WS_APIKEY";
    public static final String J50X100_EMAIL_ADDRESS_DATA_CHANGE="J50X100_EMAIL_ADDRESS_DATA_CHANGE";
    @Autowired
    @Qualifier("jobDataSource")
    private DataSource jobDataSource;
    @Autowired
    protected Properties properties;
    @Value("${jobDataSource.schema}")
    protected String schema;

    @Value("${texasState.fileName}")
    String fileNameInput;
    @Value("${dao.api.url.productscancode.update}")
    String updateProductScanCodeDaoUrl;
    @Value("${dao.api.url.wiccat.insert}")
    String insertWicCatDaoUrl;
    @Value("${dao.api.url.wicsubcat.insert}")
    String insertWicSubCatDaoUrl;
    @Value("${dao.api.url.productscancodewic.insert}")
    String insertProductScanCodeWicDaoUrl;
    @Value("${dao.api.url.productscancodewic.update}")
    String updateProductScanCodeWicDaoUrl;
    @Value("${dao.api.url.productscancodewic.delete}")
    String deleteProductScanCodeWicDaoUrl;
    @Value("${dao.api.apikey}")
    String apiKey;
    @Value("${texasState.fileName.pos}")
    String posFolder;
    @Value("${email.production.support.to}")
    String mailTo;
    @Value("${email.admin.from}")
    String mailFrom;
    @Value("${emailService.endpoint}")
    String emailEndpoint;
    @Value("${wsag.apiKey}")
    String emailApiKey;
    @Value("${email.pos.to}")
    String mailToPos;
    @Value("${job.dynamic.config.callout}")
    protected boolean jobConfiguration;
    @PostConstruct
    private void initialize() {
        setDataSource(jobDataSource);
    }


    public String getConfigurationInfor(String key){
        String query ="select PROP_VAL_TXT from [schema].BAT_ENVMT_CFG where PROP_NM=?";
        String value = null;
        boolean flagExistKey = false;
        query = query.replace(SCHEMA_SQL,schema);
        if(jobConfiguration) {
            try {
                value = this.getJdbcTemplate().queryForObject(query, String.class, key);
                flagExistKey = true;
            } catch (Exception e) {
                flagExistKey = false;
                LOGGER.error(e.getMessage());
            }
        }
        if(!flagExistKey){
            value = this.getDataFromProperfiesConfig(key);
        }
        return value;
    }
    public String getDataFromProperfiesConfig(String key){
        String value ="";
        switch (key){
            case J50X100_UPDATE_PRODUCT_SCAN_CODE_ORACLE_URL:
                value = updateProductScanCodeDaoUrl;
                break;
            case J50X100_INSERT_WIC_CAT_ORACLE_URL:
                value = insertWicCatDaoUrl;
                break;
            case J50X100_INSERT_WIC_SUB_CAT_ORACLE_URL:
                value = insertWicSubCatDaoUrl;
                break;
            case J50X100_INSERT_PRODUCT_SCAN_CODE_WIC_ORACLE_URL:
                value =  insertProductScanCodeWicDaoUrl;
                break;
            case J50X100_UPDATE_PRODUCT_SCAN_CODE_WIC_ORACLE_URL:
                value =  updateProductScanCodeWicDaoUrl;
                break;
            case J50X100_DELETE_PRODUCT_SCAN_CODE_WIC_ORACLE_URL:
                value =  deleteProductScanCodeWicDaoUrl;
                break;
            case J50X100_APIKEY:
                value =  apiKey;
                break;
            case J50X100_INPUT_NAS:
                value =  fileNameInput;
                break;
            case J50X100_EMAIL_ADDRESS_PRODUCTION_SUPPORT:
                value =  mailTo;
                break;
            case J50X100_EMAIL_FROM:
                value =  mailFrom;
                break;
            case J50X100_EMAIL_WS_ENDPOINT:
                value =  emailEndpoint;
                break;
            case J50X100_EMAIL_WS_APIKEY:
                value =  emailApiKey;
                break;
            case J50X100_EMAIL_ADDRESS_DATA_CHANGE:
                value =  mailToPos;
                break;
            default:

        }
        return value;
    }
}
