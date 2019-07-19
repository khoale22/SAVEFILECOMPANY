/*
 *  ProductGroupTypeController.java
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.productGroupType;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.codeTable.CodeTablesService;
import com.heb.pm.entity.*;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST endpoint for functions related to get the list of code table product group types.
 *
 * @author vn87351
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductGroupTypeController.CODE_TABLE_PRODUCT_GROUP_TYPE_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_PRODUCT_GROUP_TYPE)
public class ProductGroupTypeController {

    private static final Logger logger = LoggerFactory.getLogger(ProductGroupTypeController.class);

    protected static final String CODE_TABLE_PRODUCT_GROUP_TYPE_URL = "/codeTable/productGroupType";
    private static final String URL_FIND_ALL_PRODUCT_GROUP_TYPES = "/findAllProductGroupTypes";
    private static final String URL_FIND_PRODUCT_GROUP_TYPE_DETAILS = "/findProductGroupTypeDetailsById";
    private static final String URL_FIND_ALL_DEPARTMENTS = "/findAllDepartments";
    private static final String URL_FIND_ALL_CHOICE_TYPES = "/findAllChoiceTypes";
    private static final String URL_FIND_SUB_DEPARTMENTS_DETAILS = "/findSubDepartmentsByDepartment";
    private static final String URL_FIND_CHOICE_OPTIONS = "/findChoiceOptionsByChoiceTypeCode";
    private static final String URL_FIND_THUMBNAIL_IMAGE = "/findThumbnailImageByUri";
    private static final String URL_ADD_PRODUCT_GROUP_TYPE = "/addProductGroupType";
    private static final String URL_UPDATE_PRODUCT_GROUP_TYPE = "/updateProductGroupType";
    private static final String FIND_ALL_PRODUCT_GROUP_TYPES_MESSAGE = "User %s from IP %s requested to find all product group types. ";
    private static final String FIND_ALL_DEPARTMENT_LOG_MESSAGE = "User %s from IP %s requested to find all department. ";
    private static final String FIND_ALL_CHOICE_TYPE_LOG_MESSAGE = "User %s from IP %s requested to find all choice type. ";
    private static final String FIND_PRODUCT_GROUP_TYPE_LOG_MESSAGE = "User %s from IP %s requested to find product group type by code %s.";
    private static final String FIND_CHOICE_OPTIONS_LOG_MESSAGE = "User %s from IP %s requested to find choice options by choice type code %s.";
    private static final String FIND_SUB_DEPARTMENTS_LOG_MESSAGE = "User %s from IP %s requested to find sub departments by department code %s.";
    private static final String FIND_THUMBNAIL_IMAGE_BY_URI_MESSAGE = "User %s from IP %s requested image for customer product group by uri %s.";
    private static final String ADD_PRODUCT_GROUP_TYPE_LOG_MESSAGE = "User %s from IP %s requested to add product group type.";
    private static final String UPDATE_PRODUCT_GROUP_TYPE_LOG_MESSAGE = "User %s from IP %s requested to update product group type.";
    private static final String DELETE_PRODUCT_GROUP_TYPE_LOG_MESSAGE = "User %s from IP %s requested to delete product group type.";
    private static final String URL_DELETE_PRODUCT_GROUP_TYPES = "/deleteProductGroupTypes";
    private static final String DELETE_SUCCESS_MESSAGE = "Successfully Deleted.";
    private static final String ADD_SUCCESS_MESSAGE = "Successfully Added.";
    private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";
    private static final String UPDATE_ERROR_MESSAGE = "Error";
    /*Constant*/
    private static final String PRODUCT_GROUP_TYPE_STR = "productGroupType";
    private static final String DEPARTMENTS_STR = "departments";
    private static final String SUB_DEPARTMENTS_STR = "subDepartments";
    private static final String THUMBNAI_URIS_STR = "thumbnailUris";
    @Autowired
    private CodeTablesService codeTablesService;
    @Autowired
    private ProductGroupTypeService productGroupTypeService;
    @Autowired
    private UserInfo userInfo;
    private ProductGroupTypeResolver productGroupTypeResolver = new ProductGroupTypeResolver();

    /**
     * Call service to get list of product group types from database.
     *
     * @param request The HTTP request that initiated this call.
     * @return the list of product group types.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupTypeController.URL_FIND_ALL_PRODUCT_GROUP_TYPES)
    public List<ProductGroupType> findAllProductGroupTypes(HttpServletRequest request) {
        ProductGroupTypeController.logger.info(String.format(ProductGroupTypeController.FIND_ALL_PRODUCT_GROUP_TYPES_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        return this.codeTablesService.findAllProductGroupTypes();
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
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupTypeController.URL_FIND_PRODUCT_GROUP_TYPE_DETAILS)
    public Map<String, Object> findProductGroupTypeDetailsByProductGroupTypeCode(@RequestParam(value = "productGroupTypeCode") String productGroupTypeCode, HttpServletRequest request){
        ProductGroupTypeController.logger.info(String.format(FIND_PRODUCT_GROUP_TYPE_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr(), productGroupTypeCode));
        ProductGroupType productGroupType = productGroupTypeService.findByProductGroupTypeCode(productGroupTypeCode);
        productGroupTypeResolver.fetch(productGroupType);
        // Get the list of departments to show on department select box.
        List<Department> departments = productGroupTypeService.findAllDepartments();
        // Load sub departments by productGroupType to show on sub department select box.
        List<SubDepartment> subDepartments = productGroupTypeService.findSubDepartmentsByDepartment(productGroupType.getDepartmentNumberString().trim());
        // Load thumbnail image uris for cust product group.
        Map<String, String> imageUris = productGroupTypeService.findThumbnailImageUrisByCustProductGroups(productGroupType.getCustomerProductGroups());
        Map<String, Object> results = new HashMap<>();
        results.put(PRODUCT_GROUP_TYPE_STR, productGroupType);
        results.put(DEPARTMENTS_STR, departments);
        results.put(SUB_DEPARTMENTS_STR, subDepartments);
        results.put(THUMBNAI_URIS_STR, imageUris);
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
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupTypeController.URL_FIND_SUB_DEPARTMENTS_DETAILS)
    public List<SubDepartment> findSubDepartmentByDepartmentCode(@RequestParam(value = "departmentCode") String departmentCode, HttpServletRequest request){
        ProductGroupTypeController.logger.info(String.format(FIND_SUB_DEPARTMENTS_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr(), departmentCode));
        return productGroupTypeService.findSubDepartmentsByDepartment(departmentCode);
    }
    /**
     * Find the ChoiceOptions by choiceTypeCode.
     *
     * @param choiceTypeCode the code of choice type.
     * @param request The HTTP request that initiated this call.
     * @return the list of ChoiceOptions.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupTypeController.URL_FIND_CHOICE_OPTIONS)
    public List<ChoiceOption> findChoiceOptionsByChoiceTypeCode(@RequestParam(value = "choiceTypeCode") String choiceTypeCode, HttpServletRequest request){
        ProductGroupTypeController.logger.info(String.format(FIND_CHOICE_OPTIONS_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr(), choiceTypeCode));
        return productGroupTypeService.findByKeyChoiceTypeCode(choiceTypeCode);
    }
    /**
     * Request to get the byte[] representation of the thumbnail image.
     *
     * @param request The HTTP request that initiated this call.
     * @return the byte[]
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupTypeController.URL_FIND_THUMBNAIL_IMAGE)
    public ModifiedEntity<byte[]> findThumbnailImageByUri(@RequestParam(value = "imageUri") String imageUri,
                                                          HttpServletRequest request){
        ProductGroupTypeController.logger.info(String.format(ProductGroupTypeController.FIND_THUMBNAIL_IMAGE_BY_URI_MESSAGE, this.userInfo.getUserId(),
                request.getRemoteAddr(), imageUri));
        byte[] image = this.productGroupTypeService.findThumbnailImageByUri(imageUri);
        return new ModifiedEntity<>(image, imageUri);
    }
    /**
     * Find all Departments.
     *
     * @param request The HTTP request that initiated this call.
     * @return the list of Departments.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupTypeController.URL_FIND_ALL_DEPARTMENTS)
    public List<Department> findAllDepartments(HttpServletRequest request){
        // Get the list of departments to show on department select box.
        ProductGroupTypeController.logger.info(String.format(FIND_ALL_DEPARTMENT_LOG_MESSAGE, this.userInfo.getUserId(),request.getRemoteAddr()));
        return productGroupTypeService.findAllDepartments();
    }
    /**
     * Find all Choice Type.
     *
     * @param request The HTTP request that initiated this call.
     * @return the list of Choice Type.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductGroupTypeController.URL_FIND_ALL_CHOICE_TYPES)
    public List<ChoiceType> findAllChoiceTypes(HttpServletRequest request){
        ProductGroupTypeController.logger.info(String.format(FIND_ALL_CHOICE_TYPE_LOG_MESSAGE, this.userInfo.getUserId(),request.getRemoteAddr()));
        // Get the list of departments to show on department select box.
        return productGroupTypeService.findAllChoiceTypes();
    }
    /**
     * Add Product Group Type.
     *
     * @param productGroupType the information of Product Group Type Add.
     * @param request The HTTP request that initiated this call.
     * @return the information of Product Group Type after Add.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductGroupTypeController.URL_ADD_PRODUCT_GROUP_TYPE)
    public ModifiedEntity<ProductGroupType> addProductGroupType(@RequestBody ProductGroupType productGroupType, HttpServletRequest request){
        ProductGroupTypeController.logger.info(String.format(ProductGroupTypeController.ADD_PRODUCT_GROUP_TYPE_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
        //Call service to update Product Group Type Code
        boolean resultUpdate =  productGroupTypeService.addProductGroupType(productGroupType);
        String resultMessage = resultUpdate?ADD_SUCCESS_MESSAGE:UPDATE_ERROR_MESSAGE;
        return new ModifiedEntity<>(productGroupType, resultMessage);
    }
    /**
     * Update Product Group Type.
     *
     * @param productGroupType the information of Product Group Type update.
     * @param request The HTTP request that initiated this call.
     * @return the information of Product Group Type after update.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductGroupTypeController.URL_UPDATE_PRODUCT_GROUP_TYPE)
    public ModifiedEntity<Map<String, Object>> updateProductGroupType(@RequestBody ProductGroupType productGroupType, HttpServletRequest request){
        ProductGroupTypeController.logger.info(String.format(ProductGroupTypeController.UPDATE_PRODUCT_GROUP_TYPE_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
        Map<String, Object> results = new HashMap<>();
        boolean resultUpdate = productGroupTypeService.updateProductGroupType(productGroupType);
        String resultMessage;
        if(resultUpdate){
            ProductGroupType productGroupTypeResponse =
                    productGroupTypeService.findByProductGroupTypeCode(productGroupType.getProductGroupTypeCode());
            productGroupTypeResolver.fetch(productGroupTypeResponse);
            results.put(PRODUCT_GROUP_TYPE_STR, productGroupTypeResponse);
            resultMessage = UPDATE_SUCCESS_MESSAGE;
        } else{
            resultMessage = UPDATE_ERROR_MESSAGE;
        }
        return new ModifiedEntity<>(results, resultMessage);
    }
    /**
     * Delete Product Group Type.
     *
     * @param productGroupType the information of Product Group Type delete.
     * @param request The HTTP request that initiated this call.
     * @return the information of Product Group Type after delete.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductGroupTypeController.URL_DELETE_PRODUCT_GROUP_TYPES)
    public ModifiedEntity<List<ProductGroupType>> deleteProductGroupTypes(@RequestBody ProductGroupType productGroupType,
                                                                          HttpServletRequest request) {
        ProductGroupTypeController.logger.info(String.format(ProductGroupTypeController.DELETE_PRODUCT_GROUP_TYPE_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
        //Call service to update Product Group Type Code
        productGroupTypeService.deleteProductGroupType(productGroupType);
        return new ModifiedEntity<>(this.codeTablesService.findAllProductGroupTypes(), DELETE_SUCCESS_MESSAGE);
    }
}