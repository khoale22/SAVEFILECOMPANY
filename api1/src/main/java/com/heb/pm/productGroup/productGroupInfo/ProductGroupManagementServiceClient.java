/*
 * ProductGroupManagementServiceClient
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productGroup.productGroupInfo;

import com.heb.pm.entity.CustomerProductChoice;
import com.heb.pm.entity.CustomerProductGroup;
import com.heb.pm.entity.CustomerProductGroupMembershipKey;
import com.heb.pm.repository.CustomerProductChoiceRepository;
import com.heb.pm.repository.CustomerProductGroupMembershipRepository;
import com.heb.pm.repository.ProductOnlineRepository;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import com.heb.pm.ws.ProductAttributeManagementServiceClient;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.controller.UserInfo;
import com.heb.xmlns.ei.cust_prod_grp.CUSTPRODGRP;
import com.heb.xmlns.ei.productmaster.ProductMaster;
import com.tibco.schemas.productmanagementservice.shared.resources.schema.definitions.custom.schema.CustProdChoice;
import com.tibco.schemas.productmanagementservice.shared.resources.schema.definitions.custom.schema.CustProdGroup;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.heb.pm.entity.ProductOnline;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
/**
 * Provides access to service endpoint for product group.
 *
 * @author vn70529
 * @since 2.14.0
 */
@Service
public class ProductGroupManagementServiceClient {

    @Autowired
    private CodeTableManagementServiceClient codeTableManagementServiceClient;
    @Autowired
    private ProductManagementServiceClient productManagementServiceClient;
    @Autowired
    private ProductAttributeManagementServiceClient productAttributeManagementServiceClient;
    @Autowired
    private ProductOnlineRepository productOnlineRepository;
    @Autowired
    private UserInfo userInfo;
    @Autowired
    private CustomerProductChoiceRepository customerProductChoiceRepository;
    @Autowired
    private CustomerProductGroupMembershipRepository customerProductGroupMembershipRepository;

    private static final String STRING_EMPTY = "";
    private static final String STRING_ZERO = "0";
    private static final String STRING_SPACE = " ";
    private static final String PICKER_SWITCH_N = "N";
    private static final String SALE_CHANNEL_CD_DEFAULT = "00001";
    /**
     * DATE_YYYY_MM_DD.
     */
    public static final String DATE_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * Create a new Product Group.
     *
     * @param productGroupInfo the information of ProductGroupInfo.
     * @return productGroupCode of Product Group.
     */
    public String createProductGroupInfo(ProductGroupInfo productGroupInfo) throws Exception{
        return this.codeTableManagementServiceClient.updateProductGroupInfo(this.convertToCustProdGrp(productGroupInfo.getCustomerProductGroup()));
    }

    /**
     * Update data for Product Group info. It includes product group and product online.
     *
     * @param productGroupInfo the information of ProductGroupInfo.
     */
    public void updateProductGroupInfo(ProductGroupInfo productGroupInfo) throws Exception{
        if(productGroupInfo.getCustomerProductGroup().getAction() != null && productGroupInfo.getCustomerProductGroup().getAction().equals(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue())){
            this.codeTableManagementServiceClient.updateProductGroupInfo(this.convertToCustProdGrp(productGroupInfo.getCustomerProductGroup()));
        }
        if(productGroupInfo.getProductOnline() != null && productGroupInfo.getProductOnline().getAction() != null){
            this.updateShowOnSiteForProductGroup(productGroupInfo.getProductOnline());
        }
    }

    /**
     * Delete Product Group.
     *
     * @param productGroupCode the productGroupCode of ProductGroup.
     */
    public void deleteProductGroup(String productGroupCode) throws Exception{
        CUSTPRODGRP custProdGrp = new CUSTPRODGRP();
        custProdGrp.setCUSTPRODGRPID(productGroupCode);
        custProdGrp.setACTIONCD(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
        this.codeTableManagementServiceClient.updateProductGroupInfo(custProdGrp);
    }

    /**
     * update data for Show On Site in product group
     *
     * @param productOnline the ProductOnline
     */
    private void updateShowOnSiteForProductGroup(ProductOnline productOnline) {
        List<ProductOnline> productOnlines = new ArrayList<ProductOnline>();
        ProductOnline productOnlineOrg = this.productOnlineRepository
                .findTop1ByKeyProductIdAndKeySaleChannelCodeOrderByKeyEffectiveDateDesc(productOnline.getKey().getProductId(),productOnline.getKey().getSaleChannelCode());
        if(productOnline.getKey().getProductId() != null){
            if(productOnline.isShowOnSite()) {
                if (productOnlineOrg != null) {
                    productOnlineOrg.setAction(ProductAttributeManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
                    productOnlines.add(productOnlineOrg);
                }
                productOnline.setAction(ProductAttributeManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
                productOnlines.add(productOnline);
            }else {
                if(this.compareGreaterThanCurrentDate(productOnlineOrg.getKey().getEffectiveDate())){
                    productOnlineOrg.setAction(ProductAttributeManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
                    productOnlines.add(productOnlineOrg);
                }else {
                    productOnlineOrg.setAction(ProductAttributeManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
                    productOnlineOrg.getKey().setEffectiveDate(LocalDate.now());
                    productOnlineOrg.setExpirationDate(LocalDate.now());
                    productOnlines.add(productOnlineOrg);
                }

            }
        }
        this.productAttributeManagementServiceClient.updateShowOnSiteForProductGroup(this.convertToProductOnline(productOnlines));
    }
    /**
     * This method is used to create the associated product and create the list of choice options or
     * update the list of choice options for edit associated product.
     *
     * @param productGroupInfo the productGroupInfo
     * @param prodGrpId the product group id.
     */
    public void updateAssociatedProducts(ProductGroupInfo productGroupInfo,  String prodGrpId) {
        ProductMaster productMaster = new ProductMaster();
        List<CustProdChoice> custProdChoices = new ArrayList<>();
        List<CustProdGroup> custProdGroupsAdd = new ArrayList<>();
        productGroupInfo.getDataAssociatedProduct().getRows().forEach(associatedProduct -> {
            // Create a new associated product.
            if(associatedProduct.containsKey(ProductGroupInfoService.PRODUCT_ID_COLUMN_KEY) && associatedProduct.get(ProductGroupInfoService.PRODUCT_ID_COLUMN_KEY) != null
                    && associatedProduct.containsKey(ProductGroupInfoService.ACTION_CODE_KEY) && CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue().equals(associatedProduct.get(ProductGroupInfoService.ACTION_CODE_KEY))){
                CustomerProductGroupMembershipKey key = new CustomerProductGroupMembershipKey();
                key.setProdId(Long.valueOf(associatedProduct.get(ProductGroupInfoService.PRODUCT_ID_COLUMN_KEY).toString()));
                key.setCustomerProductGroupId(Long.valueOf(prodGrpId));
                if(customerProductGroupMembershipRepository.findOne(key) == null) {
                    custProdGroupsAdd.add(convertToCustProdGrp(associatedProduct.get(ProductGroupInfoService.PRODUCT_ID_COLUMN_KEY).toString(), prodGrpId, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue()));
                }
            }
        });
        // Update the list of choice options.
        custProdChoices.addAll(convertToLstCustProdChoice(productGroupInfo.getDataAssociatedProduct().getCustomerProductChoices(), productGroupInfo.getCustomerProductGroup().getProductGroupType().getProductGroupTypeCode()));
        if(!custProdChoices.isEmpty() || !custProdGroupsAdd.isEmpty()){
            productMaster.getCustProdChoice().addAll(custProdChoices);
            productMaster.getCustProdGroup().addAll(custProdGroupsAdd);
            this.productManagementServiceClient.updateProductManagement(productMaster);
        }
    }
    /**
     * Delete CustProdGroup.
     *
     * @param custProdGroupDel the CustProdGroup delete.
     * @param productGroupTypeCode the productGroupTypeCode of Product Group.
     */
    private void deleteCustProdGroup(CustProdGroup custProdGroupDel, String productGroupTypeCode ){
        List<CustProdGroup> custProdGroupsDel = new ArrayList<CustProdGroup>();
        custProdGroupsDel.add(custProdGroupDel);
        ProductMaster productMaster= new ProductMaster();
        List<CustomerProductChoice> customerProductChoices = this.customerProductChoiceRepository.findByKeyProductIdAndKeyProductGroupTypeCode(Long.parseLong(custProdGroupDel.getPRODID()), productGroupTypeCode);
        List<CustProdChoice> lst = new ArrayList<CustProdChoice>();
        if(customerProductChoices != null){
            for (CustomerProductChoice customerProductChoice : customerProductChoices) {
                if (customerProductGroupMembershipRepository.findByKeyProdId(customerProductChoice.getKey().getProductId()).size() <= 1) {
                    lst.add(convertToCustProdChoice(customerProductChoice, productGroupTypeCode));
                }
            }
        }
        productMaster.getCustProdChoice().addAll(lst);
        productMaster.getCustProdGroup().addAll(custProdGroupsDel);
        this.productManagementServiceClient.updateProductManagement(productMaster);
    }

    /**
     * Convert customerProductGroup to CUSTPRODGRP in web service.
     *
     * @param customerProductGroup the information of ProductGroupInfo.
     * @return the information CUSTPRODGRP
     */
    private CUSTPRODGRP convertToCustProdGrp(CustomerProductGroup customerProductGroup){
        CUSTPRODGRP custProdGrp = this.initialCustProdGrp();
        if (customerProductGroup.getCustProductGroupId() == 0) {
            custProdGrp.setACTIONCD(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
        } else {
            custProdGrp.setACTIONCD(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
            custProdGrp.setCUSTPRODGRPID(Long.toString(customerProductGroup.getCustProductGroupId()));
        }

        if (!this.isEmpty(customerProductGroup.getProductGroupType().getProductGroupTypeCode())) {
            custProdGrp.setPRODGRPTYPCD(customerProductGroup.getProductGroupType().getProductGroupTypeCode());
        }
        if (!this.isEmpty(customerProductGroup.getCustProductGroupName())) {
            custProdGrp.setCUSTPRODGRPNM(customerProductGroup.getCustProductGroupName());
        }
        if (!this.isEmpty(customerProductGroup.getCustProductGroupDescription())) {
            custProdGrp.setCUSTPRODGRPDES(customerProductGroup.getCustProductGroupDescription());
        }
        if (!this.isEmpty(customerProductGroup.getCustProductGroupDescriptionLong())) {
            custProdGrp.setCUSTPRODGRPLONG(customerProductGroup.getCustProductGroupDescriptionLong());
        }
        custProdGrp.setLSTUPDTUID(userInfo.getUserId());
        return custProdGrp;
    }

    /**
     * Initial CUSTPRODGRP object with set empty for default fields.
     *
     * @return CUSTPRODGRP object.
     */
    private CUSTPRODGRP initialCustProdGrp(){
        CUSTPRODGRP custProdGrp = new CUSTPRODGRP();
        custProdGrp.setWorkId(STRING_ZERO);
        custProdGrp.setCUSTPRODGRPID(STRING_ZERO);
        custProdGrp.setPRODGRPTYPCD(STRING_SPACE);
        custProdGrp.setCUSTPRODGRPNM(STRING_SPACE);
        custProdGrp.setCUSTPRODGRPDES(STRING_SPACE);
        custProdGrp.setCUSTPRODGRPLONG(STRING_SPACE);
        return custProdGrp;
    }
    /**
     * Convert to CustProdGroup.
     *
     * @param productId the id of Product.
     * @param prodGrpId the id of product group.
     * @param action the action.
     * @return the CustProdGroup.
     */
    private CustProdGroup convertToCustProdGrp(String productId, String prodGrpId,String action) {
        CustProdGroup obj = new CustProdGroup();
        if (CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue().equalsIgnoreCase(action)) {
            obj.setACTIONCD(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
        } else if (CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue().equalsIgnoreCase(action)) {
            obj.setACTIONCD(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
        } else {
            obj.setACTIONCD(action);
        }
        obj.setCUSTPRODGRPID(prodGrpId);
        obj.setPRODID(productId);
        obj.setLSTUPDTUID(userInfo.getUserId());
        return obj;
    }

    /**
     * Convert list of CustomerProductChoice to list of CustProdChoice.
     *
     * @param customerProductChoices the list of CustomerProductChoice.
     * @param productGroupTypeCode the product group type code.
     * @return the list of CustProdChoice.
     */
    private List<CustProdChoice> convertToLstCustProdChoice(List<CustomerProductChoice> customerProductChoices, String productGroupTypeCode) {
        List<CustProdChoice> lst = new ArrayList<CustProdChoice>();
        if(customerProductChoices != null){
            for (CustomerProductChoice customerProductChoice : customerProductChoices) {
                if (CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue().equalsIgnoreCase(customerProductChoice.getAction())) {
                    if(!isExistingCustomerProductChoice(customerProductChoice.getKey().getProductId(), productGroupTypeCode, customerProductChoice.getKey().getChoiceTypeCode(), customerProductChoice.getKey().getChoiceOptionCode())){
                        lst.add(convertToCustProdChoice(customerProductChoice, productGroupTypeCode));
                    }
                }else{
                    if (CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue().equalsIgnoreCase(customerProductChoice.getAction())) {
                        if (customerProductGroupMembershipRepository.findByKeyProdId(customerProductChoice.getKey().getProductId()).size() <= 1) {
                            lst.add(convertToCustProdChoice(customerProductChoice, productGroupTypeCode));
                        }
                    }else{
                        lst.add(convertToCustProdChoice(customerProductChoice, productGroupTypeCode));
                    }
                }
            }
        }
        return lst;
    }

    /**
     * Check the customer choice option is existing in CustomerProductChoice.
     *
     * @param productId the product id.
     * @param productGroupTypeCode the productGroupTypeCode.
     * @param choideTypeCode the choiceTypeCode.
     * @param choiceOptionCode the choiceOptionCode.
     * @return true if that customerProductChoice is existing or not.
     */
    private boolean isExistingCustomerProductChoice(Long productId, String productGroupTypeCode, String choideTypeCode, String choiceOptionCode){
        List<CustomerProductChoice> customerProductChoices = customerProductChoiceRepository.findByKeyProductIdAndKeyProductGroupTypeCodeAndKeyChoiceTypeCodeAndKeyChoiceOptionCode(productId, productGroupTypeCode, choideTypeCode, choiceOptionCode);
        return !customerProductChoices.isEmpty();
    }
    /**
     * Convert to CustProdChoice in webservice.
     *
     * @param customerProductChoice the entity CustomerProductChoice.
     * @param productGroupTypeCode the productGroupTypeCode.
     * @return the CustProdChoice.
     */
    private CustProdChoice convertToCustProdChoice(CustomerProductChoice customerProductChoice, String productGroupTypeCode) {
        CustProdChoice obj = new CustProdChoice();
        if (CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue().equalsIgnoreCase(customerProductChoice.getAction())) {
            obj.setACTIONCD(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
            obj.setCRE8UID(userInfo.getUserId());
        } else {
            obj.setACTIONCD(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
        }
        obj.setCHCOPTCD(customerProductChoice.getKey().getChoiceOptionCode());
        obj.setCHCTYPCD(customerProductChoice.getKey().getChoiceTypeCode());
        if (customerProductChoice.getKey().getProductChoiceSequenceNumber() == null) {
            obj.setPRODCHCSEQNBR(STRING_ZERO);
        } else {
            obj.setPRODCHCSEQNBR(customerProductChoice.getKey().getProductChoiceSequenceNumber().toString());
        }
        obj.setPRODGRPTYPCD(productGroupTypeCode);
        obj.setPRODID(customerProductChoice.getKey().getProductId().toString());
        obj.setLSTUPDTUID(userInfo.getUserId());
        return obj;
    }

    /**
     * Convert to ProductOnline in webservice.
     *
     * @param productOnlines the list entity ProductOnline.
     * @return the list ProductOnline.
     */
    private List<com.heb.xmlns.ei.productonline.ProductOnline> convertToProductOnline(List<ProductOnline> productOnlines) {
        List<com.heb.xmlns.ei.productonline.ProductOnline> productOnlinesWS = new ArrayList<com.heb.xmlns.ei.productonline.ProductOnline>();
        for (ProductOnline productOnline : productOnlines) {
            com.heb.xmlns.ei.productonline.ProductOnline productOnlineWS = new com.heb.xmlns.ei.productonline.ProductOnline();
            productOnlineWS.setWORKID(STRING_ZERO);
            productOnlineWS.setPRODID(productOnline.getKey().getProductId().toString());
            productOnlineWS.setACTIONCD(productOnline.getAction());
            productOnlineWS.setPRODIDSW(PICKER_SWITCH_N);
            if(productOnline.getKey().getSaleChannelCode() == null || productOnline.getKey().getSaleChannelCode().equals(STRING_EMPTY)){
                productOnlineWS.setSALSCHNLCD(SALE_CHANNEL_CD_DEFAULT);
            }else{
                productOnlineWS.setSALSCHNLCD(productOnline.getKey().getSaleChannelCode());
            }
            productOnlineWS.setEFFDT(this.convertDateToStringDateYYYYMMDD(productOnline.getKey().getEffectiveDate()));
            productOnlineWS.setEXPRNDT(this.convertDateToStringDateYYYYMMDD(productOnline.getExpirationDate()));
            productOnlineWS.setLSTUPDTUID(userInfo.getUserId());
            productOnlinesWS.add(productOnlineWS);
        }
        return productOnlinesWS;
    }

    /**
     * Check empty String.
     *
     * @param obj the string object.
     * @return the result check.
     */
    private boolean isEmpty(String obj) {
        return obj == null || STRING_EMPTY.equals(obj.trim());
    }

    /**
     * Convert local date to string.
     *
     * @param date the local date
     * @return the date string
     */
    private String convertDateToStringDateYYYYMMDD(final LocalDate date) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_YYYY_MM_DD);
        return date.format(formatter);
    }

    /**
     * Gets current date.
     *
     * @return the current date.
     */
    private Date getCurrentDate(){
        return getDate(getCurrentDateByDateYYYYMMDD(), DATE_YYYY_MM_DD);
    }

    /**
     * Get current date as string with format YYYYMMDD.
     *
     * @return the date as string.
     */
    private String getCurrentDateByDateYYYYMMDD(){
        return new SimpleDateFormat(DATE_YYYY_MM_DD, Locale.getDefault()).format(new Date());
    }

    /**
     * Returns the Date object from date string and format.
     *
     * @param dateString  the string of date.
     * @param format the format date.
     * @return Date type.
     */
    private Date getDate(final String dateString, final String format) {
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Date date;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            date = null;
        }
        return date;
    }

    /**
     * Compare input date greater than current date.
     *
     * @param date the input date.
     * @return true if input greater than current date.
     */
    private boolean compareGreaterThanCurrentDate(LocalDate date) {
        boolean check = false;
        Date varDate = convertLocalDateToDate(date);
        Date currentDate = getCurrentDate();
        if (varDate.compareTo(currentDate) > 0) {
            check = true;
        }
        return check;
    }

    /**
     * Convert Local date to date.
     *
     * @param date the local date.
     * @return the date.
     */
    private Date convertLocalDateToDate(final LocalDate date) {
        if (date == null) {
            return null;
        }
        return getDate(convertDateToStringDateYYYYMMDD(date),DATE_YYYY_MM_DD);
    }
    /**
     * delete data for associated Product.
     *
     * @param productGroupInfo the information of ProductGroupInfo.
     */
    public void deleteAssociatedProduct(ProductGroupInfo productGroupInfo) throws Exception {
        for (Map<String, Object> map : productGroupInfo.getDataAssociatedProduct().getRows()) {
            deleteCustProdGroup(convertToCustProdGrp(String.valueOf(map.get(ProductGroupInfoService.PRODUCT_ID_COLUMN_KEY)),
                    String.valueOf(productGroupInfo.getCustomerProductGroup().getCustProductGroupId()),
                    String.valueOf(map.get(ProductGroupInfoService.ACTION_CODE_KEY))),productGroupInfo.getCustomerProductGroup().getProductGroupTypeCode());
        }
    }
}
