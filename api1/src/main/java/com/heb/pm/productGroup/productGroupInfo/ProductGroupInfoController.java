/*
 * ProductGroupInfoController
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productGroup.productGroupInfo;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.audit.AuditService;
import com.heb.pm.codeTable.productGroupType.ProductGroupTypeResolver;
import com.heb.pm.codeTable.productGroupType.ProductGroupTypeService;
import com.heb.pm.entity.*;
import com.heb.pm.productDetails.product.eCommerceView.CustomerHierarchyAssigment;
import com.heb.pm.productDetails.product.eCommerceView.GenericEntityRelationshipResolver;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST endpoint for get product group info.
 *
 * @author vn87351
 * @since 2.14.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_GROUP_SEARCH)
public class ProductGroupInfoController {
    public static final Logger logger = LoggerFactory.getLogger(ProductGroupInfoController.class);

    // urls
    private static final String PRODUCT_GROUP_INFO_URL = "/productGroup/findProductGroupInfo";
    private static final String PRODUCT_GROUP_TYPE_URL = "/productGroup/findProductGroupType";
    private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";
    private static final String GET_CUSTOMER_HIERARCHY_ASSIGNMENT = "/productGroup/getCustomerHierarchyAssignment";
    private static final String SAVE_CUSTOMER_HIERARCHY_ASSIGNMENT = "/productGroup/saveCustomerHierarchyAssignment";
    private static final String GET_CUSTOMER_HIERARCHY = "/productGroup/getCustomerHierarchy";
    private static final String GET_PRIMARY_IMAGE_FOR_PRODUCT = "/productGroup/getImagePrimaryForProduct";
    private static final String GET_CHOICE_URL = "/productGroup/getChoice";
    private static final String GET_PRODUCT_PRIMARY_SCAN_CODE = "/productGroup/getProductPrimaryScanCode";
    private static final String URL_FIND_PRODUCT_GROUP_TYPE_DETAILS = "/productGroup/findProductGroupTypeDetailsById";
    private static final String URL_FIND_SUB_DEPARTMENTS_DETAILS = "/productGroup/findSubDepartmentsByDepartment";
    private static final String URL_FIND_ALL_CHOICE_TYPES = "/productGroup/findAllChoiceTypes";
    private static final String URL_GET_DATA_FOR_PRODUCT_GROUP_HIERARCHY = "/productGroup/getDataForProductGroupHierarchy";
    private static final String URL_CREATE_PRODUCT_GROUP_INFO = "/productGroup/createProductGroupInfo";
    private static final String URL_UPDATE_PRODUCT_GROUP_INFO = "/productGroup/updateProductGroupInfo";
    private static final String URL_DELETE_ASSOCIATED_PRODUCT = "/productGroup/deleteAssociatedProduct";
    private static final String URL_DELETE_PRODUCT_GROUP_INFO = "/productGroup/deleteProductGroupInfo";
    private static final String URL_GET_ASSOCIATED_PRODUCT = "/productGroup/getAssociatedProduct";
    private static final String URL_GET_PRODUCT_GROUP_INFO_AUDIT = "/productGroup/getProductGroupInfoAudit";

    /**
     * logs
     */
    private static final String SAVE_CUSTOMER_HIERARCHY_ASSIGNMENT_LOG_MESSAGE = "User %s from IP %s has requested save customer hierarchy assignment";
    private static final String GET_CUSTOMER_HIERARCHY_LOG_MESSAGE = "User %s from IP %s has requested get customer hierarchy";
    private static final String GET_CUSTOMER_HIERARCHY_ASSIGNMENT_LOG_MESSAGE = "User %s from IP %s has requested get customer hierarchy assignment";
    private static final String GET_PRIMARY_IMAGE_FOR_PRODUCT_LOG_MESSAGE = "User %s from IP %s has requested get primary image for product";
    private static final String GET_CHOICE_LOG_MESSAGE = "User %s from IP %s has requested get choice by product id %s";
    private static final String GET_PRODUCT_MASTER_LOG_MESSAGE = "User %s from IP %s has requested get product master by product id %s";
    private static final String FIND_PRODUCT_GROUP_TYPE_LOG_MESSAGE = "User %s from IP %s requested to find product group type by code %s.";
    private static final String FIND_ALL_CHOICE_TYPE_LOG_MESSAGE =
            "User %s from IP %s requested to find all choice type.";
    private static final String FIND_SUB_DEPARTMENT_BY_DEPARTMENT_CODE_LOG_MESSAGE =
            "User %s from IP %s requested to find sub department by department code %s.";
    private static final String GET_PRODUCT_GROUP_CHOICE_TYPE_BY_CODE_LOG_MESSAGE =
            "User %s from IP %s requested to get product group choice type by code %s.";
    private static final String FIND_ALL_PRODUCT_GROUP_TYPE_LOG_MESSAGE = "User %s from IP %s requested to find product group type.";
    private static final String GET_PRODUCT_GROUP_INFO_LOG_MESSAGE =
            "User %s from IP %s requested to get product group info by id=%s and sale chanel=%s and hierarchy context code = %s.";
    private static final String DELETE_PRODUCT_GROUP_LOG_MESSAGE =
            "User %s from IP %s requested to delete product group info by id=%s";
    private static final String SAVE_PRODUCT_GROUP_INFO_LOG_MESSAGE =
            "User %s from IP %s requested to save product group info by id=%s";
    private static final String GET_ASSOCIATED_PRODUCTP_LOG_MESSAGE =
            "User %s from IP %s requested to get Associated Products by product group id=%s";
    private static final String DELETE_ASSOCIATED_PRODUCTP_LOG_MESSAGE =
            "User %s from IP %s requested to delete Associated Products by product group id=%s";
    private static final String GET_PRODUCT_GROUP_INFO_AUDIT_LOG_MESSAGE = "User %s from IP %s has requested get product group info audit by productGroupId = %s and saleChannel = %s and hierarchyContextCd = %s";

    private static final String DELETE_SUCCESS_MESSAGE = "Successfully Deleted.";
    /**
     * Holds the message to show when add new or update or delete success.
     */
    private static final String ADD_SUCCESS_MESSAGE = "Successfully Added.";
    /**
     * inject necessary beans
     */
    @Autowired
    private ProductGroupInfoService productGroupInfoService;
    @Autowired
    private UserInfo userInfo;
    @Autowired
    private CodeTableManagementServiceClient codeTableManagementServiceClient;
    @Autowired
    private ProductGroupTypeService productGroupTypeService;
    @Autowired
    private AuditService auditService;

    /**
     * resolver to fetch data lazy
     */
    private ProductGroupTypeResolver productGroupTypeResolver = new ProductGroupTypeResolver();
    private ChoiceTypeResolver choiceTypeResolver = new ChoiceTypeResolver();
    private ProductGroupChoiceTypeResolver productGroupChoiceTypeResolver = new ProductGroupChoiceTypeResolver();
    private ProductGroupChoiceOptionResolver productGroupChoiceOptionResolver = new ProductGroupChoiceOptionResolver();
    private LazyObjectResolver<List<GenericEntityRelationship>> objectResolver = new GenericEntityRelationshipResolver();

    /**
     * Endpoint to find product group info data.
     *
     * @param productGroupId The text you are looking for.
     * @param request        The HTTP request that initiated this call.
     * @return A list of commodities whose name or number match the supplied search string.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupInfoController.PRODUCT_GROUP_INFO_URL)
    public ProductGroupInfo findProductGroupInfoById(
            @RequestParam("productGroupId") Long productGroupId,
            @RequestParam("saleChannel") String saleChannel,
            @RequestParam("hierCntxtCd") String hierCntxtCd,
            HttpServletRequest request) {
        ProductGroupInfoController.logger.info(String.format(
                ProductGroupInfoController.GET_PRODUCT_GROUP_INFO_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr(),productGroupId,saleChannel,hierCntxtCd));
        return productGroupInfoService.findProductGroupInfo(productGroupId,saleChannel,hierCntxtCd);
    }

    /**
     * Endpoint to find product group type
     * @param request the HTTP request in this call
     * @return a list of product group type
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupInfoController.PRODUCT_GROUP_TYPE_URL)
    public List<ProductGroupType> findProductGroupType(HttpServletRequest request) {
        ProductGroupInfoController.logger.info(String.format(
                ProductGroupInfoController.FIND_ALL_PRODUCT_GROUP_TYPE_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));

        return productGroupInfoService.findProductGroupType();
    }


    /**
     * Get information for eCommerce View screen. Load all basic information.
     * @param customerHierarchyAssigment - The CustomerHierarchyAssigment
     * @param request - Get eCommerce View Information
     * @return CustomerHierarchyAssigment - Contain information Customer Hierarchy Assigment
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductGroupInfoController.GET_CUSTOMER_HIERARCHY_ASSIGNMENT)
    public CustomerHierarchyAssigment getCustomerHierarchyAssignment(@RequestBody CustomerHierarchyAssigment customerHierarchyAssigment,
                                                                   HttpServletRequest request) throws Exception{
        ProductGroupInfoController.logger.info(String.format(
                ProductGroupInfoController.GET_CUSTOMER_HIERARCHY_ASSIGNMENT_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));

        return this.productGroupInfoService.getCustomerHierarchyAssignment(customerHierarchyAssigment);
    }
    /**
     * Get information for eCommerce View screen. Load all basic information.
     * @param request - Get eCommerce View Information
     * @return CustomerHierarchyAssigment - Contain information Customer Hierarchy Assigment
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupInfoController.GET_CUSTOMER_HIERARCHY)
    public CustomerHierarchyAssigment getCustomerHierarchy(@RequestParam(value = "hierachyContext") String hierachyContext,
                                                         HttpServletRequest request) throws Exception{
        ProductGroupInfoController.logger.info(String.format(
                ProductGroupInfoController.GET_CUSTOMER_HIERARCHY_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));

        CustomerHierarchyAssigment customerHierarchyAssigment = new CustomerHierarchyAssigment();
        HierarchyContext hierarchyContexts = this.productGroupInfoService.findById(hierachyContext);
        this.objectResolver.fetch(hierarchyContexts.getChildRelationships());
        customerHierarchyAssigment.setCustomerHierarchyContext(hierarchyContexts);
        return customerHierarchyAssigment;
    }

    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupInfoController.GET_PRIMARY_IMAGE_FOR_PRODUCT )
    public AssociatedProduct getImagePrimaryForProduct(@RequestParam(value = "productId") long productId,
                                                       HttpServletRequest request) throws Exception{
        //Show log for method
        ProductGroupInfoController.logger.info(String.format(ProductGroupInfoController
                .GET_PRIMARY_IMAGE_FOR_PRODUCT_LOG_MESSAGE , this.userInfo
                .getUserId(), request.getRemoteAddr(), productId));

        //call service get information
        return this.productGroupInfoService.getImagePrimaryForProduct(productId);
    }
    /**
     * Returns a list of product choices to the front end.
     * @param prodId the product id
     * @param productGroupTypeCode the product group type code
     * @param request the httpservlet request
     * @return a list of product choices to the front end.
     */
    @ViewPermission
    @RequestMapping(method= RequestMethod.GET, value=ProductGroupInfoController.GET_CHOICE_URL)
    public AssociatedProductDetail getChoicesForProduct(@RequestParam(value = "prodId") Long prodId,
                                                        @RequestParam(value = "productGroupTypeCode") String productGroupTypeCode, HttpServletRequest request) {
        ProductGroupInfoController.logger.info(String.format(ProductGroupInfoController
                .GET_CHOICE_LOG_MESSAGE ,this.userInfo.getUserId(),request.getRemoteAddr(), prodId));
        AssociatedProductDetail associatedProductDetail = this.productGroupInfoService.getChoicesForProduct(prodId, productGroupTypeCode);
        productGroupChoiceOptionResolver.fetch(associatedProductDetail.getProductGroupChoiceOptions());
        return associatedProductDetail;
    }
    /**
     * Returns a associated product to the front end.
     * @param prodId the product id or upc.
     * @param prodGroupTypeCode the product group type code.
     * @param choiceOptions the choice options.
     * @param request the httpservlet request
     * @return a AssociatedProduct to the front end.
     */
    @ViewPermission
    @RequestMapping(method= RequestMethod.GET, value=ProductGroupInfoController.GET_PRODUCT_PRIMARY_SCAN_CODE)
    public AssociatedProduct getProductPrimaryScanCodeId(@RequestParam(value = "prodId") Long prodId,
                                                         @RequestParam(value = "prodGroupId") Long prodGroupId,
                                                         @RequestParam(value = "prodGroupTypeCode") String prodGroupTypeCode,
                                                         @RequestParam(value = "choiceOptions") String choiceOptions,
                                                         HttpServletRequest request){
        ProductGroupInfoController.logger.info(String.format(ProductGroupInfoController
                .GET_PRODUCT_MASTER_LOG_MESSAGE ,this.userInfo.getUserId(),request.getRemoteAddr(), prodId));

        return this.productGroupInfoService.getProductPrimaryScanCodeId(prodId, prodGroupId, prodGroupTypeCode, choiceOptions);
    }

    /**
     * Gets the product group type details by product group type code.
     *
     * @param productGroupTypeCode the code of group type code.
     * @param request The HTTP request that initiated this call.
     * @return the map object to holds the productGroupType and the list of departments.
     * @throws Exception
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupInfoController.URL_FIND_PRODUCT_GROUP_TYPE_DETAILS)
    public Map<String, Object> findProductGroupTypeDetailsByProductGroupTypeCode(
            @RequestParam(value = "productGroupTypeCode") String productGroupTypeCode, HttpServletRequest request){
        ProductGroupInfoController.logger.info(String.format(ProductGroupInfoController
                .FIND_PRODUCT_GROUP_TYPE_LOG_MESSAGE ,this.userInfo.getUserId(),request.getRemoteAddr(), productGroupTypeCode));

        ProductGroupType productGroupType = productGroupTypeService.findByProductGroupTypeCode(productGroupTypeCode);
        productGroupTypeResolver.fetch(productGroupType);
        // Get the list of departments to show on department select box.
        List<Department> departments = productGroupTypeService.findAllDepartments();
        // Load sub departments by productGroupType to show on sub department select box.
        List<SubDepartment> subDepartments = productGroupTypeService.findSubDepartmentsByDepartment(productGroupType.getDepartmentNumberString().trim());
        // Load thumbnail image uris for cust product group.
        Map<String, Object> results = new HashMap<>();
        results.put("productGroupType", productGroupType);
        results.put("departments", departments);
        results.put("subDepartments", subDepartments);
        return results;
    }

    /**
     * Find sub departments by department code.
     *
     * @param departmentCode the code of department.
     * @param request The HTTP request that initiated this call.
     * @return the list of sub departments.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupInfoController.URL_FIND_SUB_DEPARTMENTS_DETAILS)
    public List<SubDepartment> findSubDepartmentByDepartmentCode(@RequestParam(value = "departmentCode") String departmentCode,
                                                                 HttpServletRequest request){
        ProductGroupInfoController.logger.info(String.format(ProductGroupInfoController
                .FIND_SUB_DEPARTMENT_BY_DEPARTMENT_CODE_LOG_MESSAGE ,this.userInfo.getUserId(),request.getRemoteAddr(), departmentCode));

        return productGroupTypeService.findSubDepartmentsByDepartment(departmentCode);
    }

    /**
     * Find all Choice Type.
     *
     * @param request The HTTP request that initiated this call.
     * @return the list of Choice Type.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupInfoController.URL_FIND_ALL_CHOICE_TYPES)
    public List<ChoiceType> findAllChoiceTypes(HttpServletRequest request){
        ProductGroupInfoController.logger.info(String.format(ProductGroupInfoController
                .FIND_ALL_CHOICE_TYPE_LOG_MESSAGE ,this.userInfo.getUserId(),request.getRemoteAddr()));

        List<ChoiceType> lstChoiceTypes = productGroupTypeService.findAllChoiceTypes();
        choiceTypeResolver.fetch(lstChoiceTypes);
        return lstChoiceTypes;
    }

    /**
     * Get data for Product Group Hierachy.
     *
     * @param choiceTypeCode the code of ChoiceType.
     * @param request The HTTP request that initiated this call.
     * @return the list of ProductGroupChoiceType.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupInfoController.URL_GET_DATA_FOR_PRODUCT_GROUP_HIERARCHY)
    public List<ProductGroupChoiceType> getProductGroupChoiceTypeByCode(@RequestParam(value = "choiceTypeCode") String choiceTypeCode,
                                                                        HttpServletRequest request){
        ProductGroupInfoController.logger.info(String.format(ProductGroupInfoController
                .GET_PRODUCT_GROUP_CHOICE_TYPE_BY_CODE_LOG_MESSAGE ,this.userInfo.getUserId(),request.getRemoteAddr(),choiceTypeCode));

        List<ProductGroupChoiceType> lstProductGroupChoiceType = productGroupInfoService.findProductGroupChoiceTypeByChoiceTypeCode(choiceTypeCode);
        productGroupChoiceTypeResolver.fetch(lstProductGroupChoiceType);
        return lstProductGroupChoiceType;
    }

    /**
     * Create data for Product Group.
     *
     * @param productGroupInfo the information of ProductGroupInfo.
     * @param request The HTTP request that initiated this call.
     * @return ModifiedEntity<ProductGroupInfo> the result save.
     * @throws Exception
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductGroupInfoController.URL_CREATE_PRODUCT_GROUP_INFO)
    public ModifiedEntity<String> createProductGroupInfo(@RequestBody ProductGroupInfo productGroupInfo,
                                                         HttpServletRequest request) throws Exception{
        ProductGroupInfoController.logger.info(String.format(
                ProductGroupInfoController.SAVE_PRODUCT_GROUP_INFO_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr(),productGroupInfo.getCustomerProductGroup().getCustProductGroupId()));
        return new ModifiedEntity<>(productGroupInfoService.createProductGroupInfo(productGroupInfo), ADD_SUCCESS_MESSAGE);
    }

    /**
     * This method is used to update product group.
     *
     * @param productGroupInfo the product group object.
     * @param request  The HTTP request that initiated this call.
     * @return ModifiedEntity<ProductGroupInfo> the result save.
     * @throws Exception
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductGroupInfoController.URL_UPDATE_PRODUCT_GROUP_INFO)
    public ModifiedEntity<String> updateProductGroupInfo(@RequestBody ProductGroupInfo productGroupInfo,
                                                         HttpServletRequest request) throws Exception{
        ProductGroupInfoController.logger.info(String.format(
                ProductGroupInfoController.SAVE_PRODUCT_GROUP_INFO_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr(),productGroupInfo.getCustomerProductGroup().getCustProductGroupId()));
        return new ModifiedEntity<>(productGroupInfoService.updateProductGroupInfo(productGroupInfo), UPDATE_SUCCESS_MESSAGE);
    }
    /**
     * Delete Product Group.
     *
     * @param productGroupCode the productGroupCode of ProductGroup.
     * @param request The HTTP request that initiated this call.
     * @return ModifiedEntity<String> the result delete.
     */
    @ViewPermission
    @RequestMapping(method= RequestMethod.POST, value=ProductGroupInfoController.URL_DELETE_PRODUCT_GROUP_INFO)
    public ModifiedEntity<String> deleteProductGroup(@RequestBody String productGroupCode, HttpServletRequest request)throws Exception {
        ProductGroupInfoController.logger.info(String.format(
                ProductGroupInfoController.DELETE_PRODUCT_GROUP_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr(),productGroupCode));
        this.productGroupInfoService.deleteProductGroup(productGroupCode);
        return new ModifiedEntity<>(productGroupCode, DELETE_SUCCESS_MESSAGE);
    }

    @ViewPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductGroupInfoController.SAVE_CUSTOMER_HIERARCHY_ASSIGNMENT)
    public ModifiedEntity saveCustomHierarchyAssigment(@RequestBody List<GenericEntityRelationship> currentHierarchys, HttpServletRequest request) throws Exception{
        ProductGroupInfoController.logger.info(String.format(
                ProductGroupInfoController.SAVE_CUSTOMER_HIERARCHY_ASSIGNMENT_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        this.codeTableManagementServiceClient.updateCustomerHierarchyAssignmentForProductGroup(currentHierarchys,currentHierarchys.get(0).getProductId(),userInfo.getUserId());
        return new ModifiedEntity<>(null, UPDATE_SUCCESS_MESSAGE);
    }

    /**
     * Get data for Associated Products.
     *
     * @param productGroupId the customer product group id
     * @param customerProductGroupCode customer product group code
     * @param request The HTTP request that initiated this call.
     * @return AssociatedProduct
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupInfoController.URL_GET_ASSOCIATED_PRODUCT)
    public AssociatedProduct getAssociatedProduct(
            @RequestParam("productGroupId") Long productGroupId,
            @RequestParam("customerProductGroupCode") String customerProductGroupCode,
            HttpServletRequest request) {
        ProductGroupInfoController.logger.info(String.format(
                ProductGroupInfoController.GET_ASSOCIATED_PRODUCTP_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr(),productGroupId));
        return productGroupInfoService.getAssociatedProduct(productGroupId,customerProductGroupCode);
    }

    /**
     * Save data for Product Group info.
     *
     * @param productGroupInfo the information of ProductGroupInfo.
     * @param request The HTTP request that initiated this call.
     * @return ModifiedEntity<ProductGroupInfo> the result save.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductGroupInfoController.URL_DELETE_ASSOCIATED_PRODUCT)
    public ModifiedEntity<AssociatedProduct> deleteAssociatedProduct(@RequestBody ProductGroupInfo productGroupInfo,
                                                                     HttpServletRequest request) throws Exception{
        ProductGroupInfoController.logger.info(String.format(
                ProductGroupInfoController.DELETE_ASSOCIATED_PRODUCTP_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr(),productGroupInfo.getCustomerProductGroup().getCustProductGroupId()));


        return new ModifiedEntity<>(this.productGroupInfoService.deleteAssociatedProduct(productGroupInfo), UPDATE_SUCCESS_MESSAGE);
    }

    /**
     * Get the list of product group info audit by custProductGroupId and saleChannel and hierCntxtCd.
     *
     * @param custProductGroupId - The custProductGroupId of Product Group.
     * @param salesChannel - The sale channel code.
     * @param hierCntxtCd - The Hierarchy contenxt code.
     * @param request - The HTTP request that initiated this call.
     * @return List<AuditRecord> the list of product group info audit.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupInfoController.URL_GET_PRODUCT_GROUP_INFO_AUDIT)
    public List<AuditRecord> getProductGroupInfoAuditsInformation(@RequestParam(value = "custProductGroupId") long custProductGroupId, @RequestParam(value = "salesChannel") String salesChannel,@RequestParam (value = "hierarchyContext") String hierCntxtCd, HttpServletRequest request) {
        ProductGroupInfoController.logger.info(String.format(ProductGroupInfoController.GET_PRODUCT_GROUP_INFO_AUDIT_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr(),custProductGroupId, salesChannel,hierCntxtCd));
        return this.auditService.getProductGroupInfoAuditsInformation(custProductGroupId, salesChannel,hierCntxtCd);
    }
}