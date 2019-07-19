/*
 *  ProductGroupTypeService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.productGroupType;

import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.pm.ws.ContentManagementServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds all of the business logic for Product Group Type Service.
 *
 * @author vn70529
 * @since 2.12.0
 */
@Service
public class ProductGroupTypeService {
	private static final Logger logger = LoggerFactory.getLogger(ProductGroupTypeService.class);
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private SubDepartmentRepository subDepartmentRepository;
	@Autowired
	private ProductGroupTypeRepository productGroupTypeRepository;
	@Autowired
	private ImageMetaDataRepository imageMetaDataRepository;
	@Autowired
	private ChoiceOptionRepository choiceOptionRepository;
	@Autowired
	private ContentManagementServiceClient contentManagementServiceClient;
	@Autowired
	private ChoiceTypeRepository choiceTypeRepository;
	@Autowired
	private ProductGroupTypeManagementServiceClient productGroupTypeManagementServiceClient;
	@Autowired
	private ProductGroupChoiceTypeRepository productGroupChoiceTypeRepository;
	@Autowired
	private GenericEntityRelationshipRepository genericEntityRelationshipRepository;
	@Autowired
	private GenericEntityRepository genericEntityRepository;
	@Autowired
	private CustomerProductChoiceRepository customerProductChoiceRepository;
	@Autowired
	private ProductGroupChoiceOptionRepository productGroupChoiceOptionRepository;
	private GenericParentEntityResolver genericParentEntityResolver = new GenericParentEntityResolver();
	/*Constant*/
	private static final String ACTION_CODE_UPDATE = "";
	private static final String ACTION_CODE_DELETE = "D";
	private static final String ACTION_CODE_ADD = "A";

	/**
	 * Returns the list of ProductGroupTypes by code.
	 *
	 * @param code the code of ProductGroupType.
	 * @return the list of ProductGroupTypes.
	 */
	public ProductGroupType findByProductGroupTypeCode(String code){
		ProductGroupType productGroupType = productGroupTypeRepository.findOne(code);
		for (int i = 0; i< productGroupType.getCustomerProductGroups().size(); i++){
			CustomerProductGroup customerProductGroup = productGroupType.getCustomerProductGroups().get(i);
			GenericEntity genericEntity = this.genericEntityRepository.findTop1ByDisplayNumberAndType(customerProductGroup.getCustProductGroupId(), GenericEntity.EntyType.PGRP.getName());
			if (genericEntity != null) {
				customerProductGroup.setGenericEntity(genericEntity);
				GenericEntityRelationship rlsh = genericEntityRelationshipRepository.findTop1ByKeyChildEntityIdAndHierarchyContextAndDefaultParent(
						customerProductGroup.getGenericEntity().getId(), HierarchyContext.HierarchyContextCode.CUST.getName(), true);
				if (rlsh != null) {
					genericParentEntityResolver.fetch(rlsh);
					if(rlsh.getGenericParentEntity().getDisplayNumber()>0){
						rlsh.getGenericParentEntity().setHierarchyPathDisplay(rlsh.getParentDescription().getLongDescription());
					}else{
						rlsh.getGenericParentEntity().setHierarchyPathDisplay(rlsh.getGenericParentEntity().getDisplayText());
					}
					customerProductGroup.setLowestEntity(rlsh.getGenericParentEntity());
				}
			}
		}
		return productGroupType;
	}
	/**
	 * Returns the all departments.
	 *
	 * @return the list of departments.
	 */
	public List<Department> findAllDepartments(){
		return departmentRepository.findAll();
	}
	/**
	 * Returns the list of sub departments by department Code.
	 *
	 * @param departmentCode
	 * @return the list of sub department.
	 */
	public List<SubDepartment> findSubDepartmentsByDepartment(String departmentCode){
		return subDepartmentRepository.findByKeyDepartment(departmentCode);
	}
	/**
	 * Find the ProductGroupChoiceType by pickerSw is selected.
	 *
	 * @param pickerSw the productGroupTypeCode.
	 * @return the list of ProductGroupChoiceType.
	 */
	public List<ProductGroupChoiceType> findProductGroupChoiceTypeByPickerSw(boolean pickerSw){
		return this.productGroupChoiceTypeRepository.findByPickerSwitch(pickerSw);
	}
	/**
	 * Find thumbnail image uris for CustomerProductGroup.
	 *
	 * @param custProductGroups the list of CustProductGroups to find image.
	 * @return instance of Map with CustomerProductGroup key and image url.
	 */
	public Map<String, String> findThumbnailImageUrisByCustProductGroups(List<CustomerProductGroup> custProductGroups){
		Map<String, String> imageUrls = new HashMap<>();
		custProductGroups.forEach((custProductGroup)->{
			ImageMetaData imageMetaData = imageMetaDataRepository.findThumbnailImageUrisByCustProductGroups(
					custProductGroup.getCustProductGroupId(),
					ImageMetaData.IMAGE_PRODUCT_GROUP,
					ImageMetaData.IMAGE_ACTIVATED,
					ImageMetaData.IMAGE_ACTIVATED_ONLINE,
					ImageMetaData.IMAGE_PRIORITY_TYPE);
			if(imageMetaData!=null){
				imageUrls.put(String.valueOf(custProductGroup.getCustProductGroupId()), imageMetaData.getUriText());
			}
		});
		return imageUrls;
	}
	/**
	 * This method contacts a client and gets the image data from it
	 * @param imageUri unique image identifier
	 * @return byte array.
	 */
	public byte[] findThumbnailImageByUri(String imageUri){
		return this.contentManagementServiceClient.getImage(imageUri);
	}
	/**
	 * Find the choice Options by choice type code.
	 *
	 * @param choiceTypeCode the code of choice type.
	 * @return the list of ChoiceOptions.
	 */
	public List<ChoiceOption> findByKeyChoiceTypeCode(String choiceTypeCode){
		return choiceOptionRepository.findByKeyChoiceTypeCode(choiceTypeCode);
	}
	/**
	 * Find all Choice Type.
	 *
	 * @return the list of Choice Type.
	 */
	public List<ChoiceType> findAllChoiceTypes(){
		return choiceTypeRepository.findAll();
	}
	/**
	 * Update Product Group Type.
	 *
	 * @param productGroupType the information of Product Group Type update.
	 */
	public boolean updateProductGroupType(ProductGroupType productGroupType){
		boolean result = false;
		if(!this.checkChoiceOptionHasExist(productGroupType.getProductGroupTypeCode(),productGroupType.getProductGroupChoiceTypes())){
			productGroupTypeManagementServiceClient.updateProductGroupType(productGroupType);
			result = true;
		}
		return result;
	}
	/**
	 * Add Product Group Type.
	 * @param productGroupType the information of Product Group Type Add.
	 */
	public boolean addProductGroupType(ProductGroupType productGroupType){
		boolean result = false;
		if(!this.checkChoiceOptionHasExist(productGroupType.getProductGroupTypeCode(),productGroupType.getProductGroupChoiceTypes())){
			productGroupTypeManagementServiceClient.addProductGroupType(productGroupType);
			result = true;
		}
		return result;
	}
	/**
	 * Delete Product Group Type.
	 *
	 * @param productGroupType the information of Product Group Type delete.
	 */
	public void deleteProductGroupType(ProductGroupType productGroupType){
		productGroupTypeManagementServiceClient.deleteProductGroupType(productGroupType);
	}

	/**
	 * Check choice option has use for associated product.
	 *
	 * @param productGroupTypeCode the product group type code.
	 * @param productGroupChoiceTypeList the list of choice type.
	 * @return boolean the result check.
	 */
	private boolean checkChoiceOptionHasExist(String productGroupTypeCode, List<ProductGroupChoiceType> productGroupChoiceTypeList){
		boolean hasExist = false;
		for (ProductGroupChoiceType productGroupChoiceType : productGroupChoiceTypeList) {
			List<CustomerProductChoice> customerProductChoiceList = new ArrayList<>();
			if (productGroupChoiceType.getAction() != null && (productGroupChoiceType.getAction().equals(ACTION_CODE_DELETE)
					|| (productGroupChoiceType.getAction().equals(ACTION_CODE_UPDATE) && !productGroupChoiceType.getPickerSwitch()))) {
				customerProductChoiceList = this.customerProductChoiceRepository.findByKeyProductGroupTypeCodeAndKeyChoiceTypeCode(productGroupTypeCode, productGroupChoiceType.getChoiceType().getChoiceTypeCode());
			}else if(productGroupChoiceType.getAction() != null && productGroupChoiceType.getAction().equals(ACTION_CODE_UPDATE) && productGroupChoiceType.getPickerSwitch()){
				List<ProductGroupChoiceOption> productGroupChoiceOptionList = this.productGroupChoiceOptionRepository.findByKeyProductGroupTypeCodeAndKeyChoiceTypeCode(productGroupTypeCode, productGroupChoiceType.getChoiceType().getChoiceTypeCode());
				List<String> choiceOptionCodeList = new ArrayList<>();
				for (ProductGroupChoiceOption productGroupChoiceOption : productGroupChoiceOptionList){
					boolean isNotDel = true;
					for (ProductGroupChoiceOption productGroupChoiceOptionCur : productGroupChoiceType.getProductGroupChoiceOptions()) {
						if(productGroupChoiceOption.getKey().getChoiceOptionCode() == productGroupChoiceOptionCur.getKey().getChoiceOptionCode()
								&& productGroupChoiceOptionCur.getAction()!=null && productGroupChoiceOptionCur.getAction().equals(ACTION_CODE_DELETE)){
							isNotDel = false;
						}
					}
					if(isNotDel){
						choiceOptionCodeList.add(productGroupChoiceOption.getKey().getChoiceOptionCode());
					}
				}
				productGroupChoiceType.getProductGroupChoiceOptions().forEach((productGroupChoiceOptionCur) -> {
					if(productGroupChoiceOptionCur.getAction()!=null && productGroupChoiceOptionCur.getAction().equals(ACTION_CODE_ADD)){
						choiceOptionCodeList.add(productGroupChoiceOptionCur.getKey().getChoiceOptionCode());
					}
				});
				customerProductChoiceList = this.customerProductChoiceRepository.findByKeyChoiceTypeCodeAndKeyChoiceOptionCodeIn(productGroupChoiceType.getChoiceType().getChoiceTypeCode(), choiceOptionCodeList);
			}else if(productGroupChoiceType.getPickerSwitch()){
				List<String> choiceOptionCodeList = new ArrayList<>();
				productGroupChoiceType.getProductGroupChoiceOptions().forEach((productGroupChoiceOption) -> {
					choiceOptionCodeList.add(productGroupChoiceOption.getKey().getChoiceOptionCode());
				});
				customerProductChoiceList = this.customerProductChoiceRepository.findByKeyChoiceTypeCodeAndKeyChoiceOptionCodeIn(productGroupChoiceType.getChoiceType().getChoiceTypeCode(), choiceOptionCodeList);
			}
			if (customerProductChoiceList != null && customerProductChoiceList.size() > 0) {
				hasExist = true;
				break;
			}
		}
		return hasExist;
	}

}
