/*
 *  ProductGroupInfoService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productGroup.productGroupInfo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.heb.pm.entity.*;
import com.heb.pm.productDetails.product.eCommerceView.CustomerHierarchyAssigment;
import com.heb.pm.productDetails.product.eCommerceView.CustomerHierarchyAssigmentService;
import com.heb.pm.repository.*;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import com.heb.pm.ws.ContentManagementServiceClient;
import com.heb.util.controller.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Holds all business functions related to productGroup.
 *
 * @author vn87351
 * @since 2.14.0
 */
@Service
public class ProductGroupInfoService {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductGroupInfoService.class);
	/**
	 * PRODUCT_PRIMARY_PATH.
	 */
	public static final boolean PRODUCT_PRIMARY_PATH = true;
	/**
	 * product id column to generate on dynamic table ui
	 */
	public static final String PRODUCT_ID_COLUMN_KEY = "productId";
	public static final String ACTION_CODE_KEY = "actionCode";
	private static final String OPTION_CODES_KEY = "optionCodes";
	private static final String DEFAULT_OPTION_LIST_KEY = "defaultOptionList";
	private static final String WARNING_MESSAGE_KEY = "warning";
	private static final String ERROR_MESSAGE_KEY = "error";
	private static final String CHOICE_KEY_FORMAT = "%s_%s";

	private static final String PRODUCT_NOT_EXISTING = "This Product ID / UPC does not exist.";
	private static final String PRODUCT_ASSOCIATED_WITH_PRODUCT_GROUP_TYPE = "The Product ID %s is already part of Product Group Type: %s.";
	private static final String PRODUCT_ASSOCIATED_WITH_ANOTHER_PRODUCT_GROUP = "The Product ID %s is also part of another Product Group %s. The existing choice information for the product will be retained.";
	private static final String PRODUCT_ASSOCIATED_WITH_TWO_DIFFERENCE_COMBINATION = "The Product ID %s cannot be associated with two different Picker combinations.";
	/**
	 * inject necessary bean
	 */
	@Autowired
	private GenericEntityRepository genericEntityRepository;
	@Autowired
	private GenericEntityRelationshipRepository genericEntityRelationshipRepository;
	@Autowired
	private CustomerProductGroupRepository customerProductGroupRepository;
	@Autowired
	private ProductOnlineRepository productOnlineRepository;
	@Autowired
	private com.heb.pm.repository.ProductGroupTypeRepository productGroupTypeRepository;
	@Autowired
	private HierarchyContextRepository hierarchyContextRepository;
	@Autowired
	private CustomerHierarchyAssigmentService customerHierarchyAssigmentService;
	@Autowired
	private CustomerProductGroupMembershipRepository customerProductGroupMembershipRepository;
	@Autowired
	private CustomerProductChoiceRepository customerProductChoiceRepository;
	@Autowired
	private ProductGroupChoiceTypeRepository productGroupChoiceTypeRepository;
	@Autowired
	private SellingUnitRepository sellingUnitRepository;
	@Autowired
	private ProductScanImageBannerRepository productScanImageBannerRepository;
	@Autowired
	private ContentManagementServiceClient contentManagementServiceClient;
	@Autowired
	private ProductInfoRepository productInfoRepository;
	@Autowired
	private ProductGroupChoiceOptionRepository productGroupChoiceOptionRepository;
	@Autowired
	private CodeTableManagementServiceClient codeTableManagementServiceClient;
	@Autowired
	private ProductGroupManagementServiceClient productGroupManagementServiceClient;
	@Autowired
	private UserInfo userInfo;

	/**
	 * Find product group info data.
	 *
	 * @param productGroupId product group id
	 * @param channel chanel code
	 * @param hierCntxtCd hierarchy context code
	 * @return ProductGroupInfo
	 */
	public ProductGroupInfo findProductGroupInfo(Long productGroupId,String channel,String hierCntxtCd){
		ProductGroupInfo pg=new ProductGroupInfo();
		pg.setPrimaryPath(getPrimaryHierarchyPath(productGroupId, hierCntxtCd));
		pg.setCustomerProductGroup(customerProductGroupRepository.findOne(productGroupId));
		pg.setProductOnline(productOnlineRepository.findTop1ByKeyProductIdAndKeySaleChannelCodeOrderByKeyEffectiveDateDesc(productGroupId,channel));
		pg.setDataAssociatedProduct(findAssociatedProduct(productGroupId, pg.getCustomerProductGroup().getProductGroupType().getProductGroupTypeCode(), null));
		pg.setRetailDrivingPicker(validateRetailDrivingPicker(pg.getCustomerProductGroup()));
		return pg;
	}


	/**
	 * validate retail driving picker.
	 *
	 * @param pg the CustomerProductGroup object.
	 * @return the list of ProductGroupChoiceTypes.
	 */
	private boolean validateRetailDrivingPicker(CustomerProductGroup pg){
		return productGroupChoiceTypeRepository.findByKeyProductGroupTypeCodeAndPickerSwitch(pg.getProductGroupType().getProductGroupTypeCode(),true).size()>0;
	}

	/**
	 * Get list of product group types.
	 *
	 * @return data the list of ProductGroupTypes.
	 */
	public List<ProductGroupType> findProductGroupType(){
		return productGroupTypeRepository.findAll(ProductGroupType.getDefaultSort());
	}

	/**
	 * Get primary hierarchy context path.
	 *
	 * @param productGroupId product group id
	 * @param hierCntxtCd the context code
	 * @return path the primary hierarchy context path.
	 */
	private String getPrimaryHierarchyPath(Long productGroupId,String hierCntxtCd){
		//get customer hierarchy
		String primaryPath = StringUtils.EMPTY;
		GenericEntity genericEntity = this.genericEntityRepository.findTop1ByDisplayNumberAndType(productGroupId, GenericEntity.EntyType.PGRP.getName());
		if (genericEntity != null) {
			GenericEntityRelationship genericEntityRelationship = this.genericEntityRelationshipRepository
					.findTop1ByKeyChildEntityIdAndHierarchyContextAndDefaultParent(genericEntity.getId(), hierCntxtCd,
							PRODUCT_PRIMARY_PATH);
			while (genericEntityRelationship != null && genericEntityRelationship.getParentRelationships() != null &&
					!genericEntityRelationship.getParentRelationships().isEmpty()) {
				String description;
				if(genericEntityRelationship.getGenericParentEntity().getDisplayNumber()>0){
					description=genericEntityRelationship.getParentDescription().getLongDescription();
				}else{
					description=genericEntityRelationship.getGenericParentEntity().getDisplayText();
				}
				if (StringUtils.isNotBlank(primaryPath)) {
					primaryPath = description.concat("&#8594").concat(primaryPath);
				} else {
					primaryPath = description;
				}
				genericEntityRelationship = this.genericEntityRelationshipRepository
						.findTop1ByKeyChildEntityIdAndHierarchyContextAndDefaultParent(genericEntityRelationship
								.getKey().getParentEntityId(), hierCntxtCd, PRODUCT_PRIMARY_PATH);
			}

		}
		return primaryPath;
	}

	/**
	 * Find data for associate product table.
	 *
	 * @param custProdId customer product group id.
	 * @param customerProductGroupCode customer product group code.
	 * @param defaultChoiceOptions pass a List<Map<String,String>> if you want to the list of of choice options or pass it to null.
	 * @return AssociatedProduct object.
	 */
	private AssociatedProduct findAssociatedProduct(long custProdId,String customerProductGroupCode, List<Map<String,String>> defaultChoiceOptions){
		AssociatedProduct associatedProduct = new AssociatedProduct();
		List<ChoiceType> choiceTypes = new ArrayList<>();
		/**
		 * find product group info data.
		 * get product selected on associated product table
		 * then generate map store choice type code as key and choice option text as value
		 * using that map to check on matrix
		 */
		List<CustomerProductGroupMembership> customerProductGroupMembership = customerProductGroupMembershipRepository.findByKeyCustomerProductGroupId(custProdId);
		List<Map<String,String>> selectedProducts=new ArrayList<>();
		associatedProduct.setCheckAssociated(false);
		customerProductGroupMembership.forEach(prodMem -> {
			List<CustomerProductChoice> customerProductChoices = customerProductChoiceRepository.findByKeyProductIdAndProductGroupChoiceTypePickerSwitch(prodMem.getKey().getProdId(),true);
			Map<String,String> rowSelect= new HashMap<>();
			if(!customerProductChoices.isEmpty()){
				customerProductChoices.forEach(value->{
					rowSelect.put(value.getKey().getChoiceTypeCode(),value.getKey().getChoiceOptionCode());
				});
				rowSelect.put(PRODUCT_ID_COLUMN_KEY,prodMem.getKey().getProdId().toString());
				associatedProduct.setCheckAssociated(true);
				selectedProducts.add(rowSelect);
			}
		});
		/**
		 * find product group info data.
		 * get choice type and list choice option by product group type code and picker flag is true
		 */
		List<ProductGroupChoiceType> productGroupChoiceTypes = productGroupChoiceTypeRepository.findByKeyProductGroupTypeCodeAndPickerSwitch(customerProductGroupCode,true);
		Map<String,ProductGroupChoiceType> dataChoice=new HashMap<>();
		Map<ChoiceType,List<ProductGroupChoiceOption>> datas=new HashMap<>();
		productGroupChoiceTypes.forEach(choice -> {
			if(!dataChoice.containsKey(String.format(CHOICE_KEY_FORMAT,choice.getKey().getProductGroupTypeCode(),choice.getKey().getChoiceTypeCode()))) {
				dataChoice.put(
						String.format(CHOICE_KEY_FORMAT,
								choice.getKey().getProductGroupTypeCode(),
								choice.getKey().getChoiceTypeCode()),choice);
				choiceTypes.add(choice.getChoiceType());
				datas.put(choice.getChoiceType(),
						choice.getProductGroupChoiceOptions());
			}
		});
		/**
		 * Holds the associated products.
		 */
		ArrayList associatedProducts = new ArrayList<>();
		/*
		 * Load associated product and default choice option.
		 * */
		loadAssociatedProductAndDefaultChoiceOption(removeEmptyOption(datas),1,new HashMap<>(),selectedProducts, associatedProducts, defaultChoiceOptions);
		/**
		 * find product group info data.
		 * set data and header to generate dynamic table ui
		 */
		associatedProduct.setDataHeader(choiceTypes);
		associatedProduct.setRows(associatedProducts);
		return associatedProduct;
	}
	/**
	 * remove empty option
	 * @param datas remove empty list
	 * @return list data
	 */
	private List<List<ProductGroupChoiceOption>> removeEmptyOption(Map<ChoiceType,List<ProductGroupChoiceOption>> datas){
		List<List<ProductGroupChoiceOption>> lst = new ArrayList<>();
		datas.forEach((key,value)->{
			if(value!=null && !value.isEmpty()){
				lst.add(value);
			}
		});
		return lst;
	}
	/**
	 * Get product id by associated product.
	 *
	 * @param custProdId customer product group id
	 * @param maps select option
	 * @return product id
	 */
	public Long getProductIdByMapsOption(long custProdId,Map<String,String> maps){
		Long productId = 0L;
		List<Map<String,String>> defaultChoiceOptions = new ArrayList();
		findAssociatedProduct(custProdId,customerProductGroupRepository.findOne(custProdId).getProductGroupTypeCode(), defaultChoiceOptions);
		if(defaultChoiceOptions!=null){
			for(Map<String,String> mapRow : defaultChoiceOptions){
				if(mapRow.containsKey(PRODUCT_ID_COLUMN_KEY)){
					Map<String,String> selectedTmp=new HashMap<>(mapRow);
					String productIdTmp=selectedTmp.get(PRODUCT_ID_COLUMN_KEY);
					selectedTmp.remove(PRODUCT_ID_COLUMN_KEY);
					if(maps.equals(selectedTmp)){
						productId= Long.valueOf(productIdTmp);
						break;
					}
				}
			}
		}
		return productId;
	}
	/**
	 * Load associated product and default choice option.
	 *
	 * @param dataLoop the data to loop
	 * @param dept the current node
	 * @param row the data row
	 * @param selectedProducts select product mapping
	 * @param associatedProducts holds the list of associated product that loaded.
	 * @param defaultChoiceOptions holds the list of default choice option that loaded.
	 */
	private void loadAssociatedProductAndDefaultChoiceOption(List<List<ProductGroupChoiceOption>> dataLoop,
															 int dept, Map<String ,ChoiceOption> row, List<Map<String,String>> selectedProducts, List associatedProducts, List defaultChoiceOptions){
		if(dept<dataLoop.size()){
			for(ProductGroupChoiceOption option:dataLoop.get(dept-1)){
				row.put(option.getKey().getChoiceTypeCode(),option.getChoiceOption());
				loadAssociatedProductAndDefaultChoiceOption(dataLoop,dept+1,row,selectedProducts, associatedProducts, defaultChoiceOptions);
			}
		}else if(dept==dataLoop.size()){
			for(ProductGroupChoiceOption option:dataLoop.get(dept-1)){
				Map<String,ChoiceOption> rowTmp = new HashMap<>(row);
				if(option!=null)
					rowTmp.put(option.getKey().getChoiceTypeCode(),option.getChoiceOption());
				Map<String,Object> rowValueTmp = convertMapDataToMapObject(rowTmp,false);
				rowValueTmp.put(DEFAULT_OPTION_LIST_KEY, new ArrayList<>(rowTmp.values()));
				rowValueTmp.put(ACTION_CODE_KEY, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
				Map<String,String> mapKey = convertMapDataToMapString(rowTmp,true);
				for(Map<String,String> selectedProduct:selectedProducts){
					String productId=selectedProduct.get(PRODUCT_ID_COLUMN_KEY);
					Map<String,String> selectedTmp=new HashMap<>(selectedProduct);
					selectedTmp.remove(PRODUCT_ID_COLUMN_KEY);
					if(selectedTmp.equals(convertMapDataToMapString(rowTmp,true))){
						rowValueTmp.put(PRODUCT_ID_COLUMN_KEY,productId);
						mapKey.put(PRODUCT_ID_COLUMN_KEY,productId);
						rowValueTmp.put(ACTION_CODE_KEY, StringUtils.EMPTY);
					}
				}
				rowValueTmp.put(OPTION_CODES_KEY, mapKey);
				associatedProducts.add(rowValueTmp);
				if(defaultChoiceOptions != null) {
					defaultChoiceOptions.add(mapKey);
				}
			}
		}
	}

	/**
	 * convert map data choice option to map string.
	 * @param row data choice option
	 * @param isGetCode flag get map code or map value text
	 * @return map string
	 */
	private Map<String,Object> convertMapDataToMapObject(Map<String ,ChoiceOption> row,boolean isGetCode){
		Map<String,Object> maps=new HashMap<>();
		row.forEach((key,value)->{
			if(isGetCode)
				maps.put(key,value.getKey().getChoiceOptionCode());
			else
				maps.put(key,value.getProductChoiceText());
		});
		return maps;
	}

	/**
	 * Convert the map to map string.
	 *
	 * @param row the map data needs to convert.
	 * @param isGetCode true then return the map with ChoiceOptionCode or ProductChoiceText.
	 * @return the map with ChoiceOptionCode or ProductChoiceText.
	 */
	private Map<String,String> convertMapDataToMapString(Map<String ,ChoiceOption> row,boolean isGetCode){
		Map<String,String> maps=new HashMap<>();
		row.forEach((key,value)->{
			if(isGetCode)
				maps.put(key,value.getKey().getChoiceOptionCode());
			else
				maps.put(key,value.getProductChoiceText());
		});
		return maps;
	}
	/**
	 * Find HierarchyContext by hierarchyContext Id.
	 *
	 * @param hierarchyContextId the hierarchyContextId
	 * @return HierarchyContext
	 */
	public HierarchyContext findById(String hierarchyContextId) {
		HierarchyContext hierarchyContext = this.hierarchyContextRepository.findOne(hierarchyContextId);
		hierarchyContext.setChildRelationships(this.findByHierarchyContext(hierarchyContext));
		return hierarchyContext;
	}

	/**
	 * This method returns all generic entity relationships matching a given hierarchy context's context and parent id.
	 *
	 * @param hierarchyContext
	 * @return the list of GenericEntityRelationships.
	 */
	public List<GenericEntityRelationship> findByHierarchyContext(HierarchyContext hierarchyContext) {
		return this.genericEntityRelationshipRepository.
				findByKeyParentEntityIdAndHierarchyContext(hierarchyContext.getParentEntityId(), hierarchyContext.getId());
	}

	/**
	 * Gets the customer Hierarchy Assignment.
	 *
	 * @param customerHierarchyAssigment CustomerHierarchyAssignment object.
	 * @return CustomerHierarchyAssignment object.
	 */
	public CustomerHierarchyAssigment getCustomerHierarchyAssignment(CustomerHierarchyAssigment customerHierarchyAssigment) {
		this.customerHierarchyAssigmentService.setPathTree(customerHierarchyAssigment.getCustomerHierarchyContext().getChildRelationships(),null,null,null);
		CustomerHierarchyAssigment customerHierarchyAssigmentPath = new CustomerHierarchyAssigment();
		List<GenericEntityRelationship> lowestLevelByByCurrentPath = null;
		List<Long> productCurrentPath = new ArrayList<Long>();
		productCurrentPath.add(customerHierarchyAssigment.getProductId());
		customerHierarchyAssigmentPath.setCustomerHierarchyContext(customerHierarchyAssigment.getCustomerHierarchyContext());
		customerHierarchyAssigmentPath.setProductId(customerHierarchyAssigment.getProductId());
		customerHierarchyAssigmentPath.setHierachyContextCode(customerHierarchyAssigment.getHierachyContextCode());
		if(customerHierarchyAssigment.getProductId()!=null) {
			lowestLevelByByCurrentPath = this.genericEntityRelationshipRepository.findByCurrentHierarchy(productCurrentPath, customerHierarchyAssigment.getHierachyContextCode(), GenericEntity.EntyType.PGRP.getName());
		}
		this.customerHierarchyAssigmentService.findCustomerHierarchyAssignmentCurrentPath(customerHierarchyAssigmentPath, customerHierarchyAssigment.getCustomerHierarchyContext().getChildRelationships(), lowestLevelByByCurrentPath);
		return customerHierarchyAssigmentPath;
	}

	/**
	 * Get more information for eCommerce View screen. Load all information by tab(sale channel).
	 *
	 * @param productId - The product id
	 * @return AssociatedProduct - Contain information eCommerce View
	 */
	public AssociatedProduct getImagePrimaryForProduct(long productId) throws Exception {
		AssociatedProduct associatedProduct = new AssociatedProduct();
		//get image
		List<SellingUnit> sellingUnits = this.sellingUnitRepository.findByProdId(productId);
		if(sellingUnits != null){
			List<Long> upcs = sellingUnits.stream().map(sellingUnit -> sellingUnit.getUpc()).collect(Collectors.toList());
			List<ProductScanImageBanner> productScanImageBanners = this.productScanImageBannerRepository
					.findByKeyIdIn(upcs);
			//set primary image
			if (productScanImageBanners != null) {
				productScanImageBanners.forEach(p -> {
					if (p.getProductScanImageURI() != null && ProductScanImageBanner.IMAGE_APPROVE_CODE.equalsIgnoreCase(StringUtils.trim(p
							.getProductScanImageURI().getImageStatusCode())) && p.getProductScanImageURI().isActiveOnline()) {
						if (ProductScanImageBanner.PRIMARY_IMAGE.equalsIgnoreCase(StringUtils.trim(p.getProductScanImageURI().getImagePriorityCode()))) {
							associatedProduct.setImagePrimary(this.contentManagementServiceClient.getImage(p.getProductScanImageURI()
									.getImageURI()));
						}

					}
				});
			}
		}
		return associatedProduct;
	}
	/**
	 * Finds a list of product choices by their product id and product group type code
	 * @param productId
	 * @param productGroupTypeCode
	 * @return list of product choices.
	 */
	public AssociatedProductDetail getChoicesForProduct(Long productId, String productGroupTypeCode) {
		AssociatedProductDetail  associatedProductDetail = new  AssociatedProductDetail();
		//Get productGroupChoiceType none picker
		List<ProductGroupChoiceType> productGroupChoiceTypes = this.productGroupChoiceTypeRepository.findByKeyProductGroupTypeCodeAndPickerSwitch(productGroupTypeCode, false);
		List<String> choiceTypeCodes = new ArrayList<>();
		//Get choice type codes for none picker
		for(ProductGroupChoiceType productGroupChoiceType : productGroupChoiceTypes){
			choiceTypeCodes.add(productGroupChoiceType.getChoiceTypeCode());
		}
		List<CustomerProductChoice> customerProductChoices = this.customerProductChoiceRepository.findByKeyProductIdAndKeyProductGroupTypeCode(productId, productGroupTypeCode);
		List<ProductGroupChoiceOption> productGroupChoiceOptions = productGroupChoiceOptionRepository.findByKeyProductGroupTypeCodeAndKeyChoiceTypeCodeIn(productGroupTypeCode, choiceTypeCodes);
		associatedProductDetail.setCustomerProductChoices(customerProductChoices);
		associatedProductDetail.setProductGroupChoiceOptions(productGroupChoiceOptions);
		return  associatedProductDetail;
	}
	/**
	 * Finds a upc by product id.
	 *
	 * @param productId the product id or upc.
	 * @param prodGroupId the id of product group.
	 * @param productGroupTypeCode the code of product group type.
	 * @return associated product.
	 */
	public AssociatedProduct getProductPrimaryScanCodeId(Long productId,Long prodGroupId,String productGroupTypeCode,
														 String choiceOptions) {
		AssociatedProduct associatedProduct = new AssociatedProduct();
		ProductMaster productMaster = this.productInfoRepository.findOne(productId);
		if(productMaster == null){
			// Find product by upc.
			SellingUnit sellingUnit = this.sellingUnitRepository.findOne(productId);
			if(sellingUnit != null){
				productMaster = sellingUnit.getProductMaster();
			}
		}
		if(productMaster != null){
			// This case is used to check when search new associated product.
			// If the product id is already part of Product type group, then throws error message.
			associatedProduct.setErrorMessage(checkAssociatedProductInUsing(productMaster.getProdId(), prodGroupId, productGroupTypeCode, convertStringToMapData(choiceOptions)));
			associatedProduct.setProductPrimaryScanCodeId(productMaster.getProductPrimaryScanCodeId());
			associatedProduct.setProductId(productMaster.getProdId());
		}else{
			Map<String,String> message = new HashMap<>();
			message.put(ERROR_MESSAGE_KEY,PRODUCT_NOT_EXISTING);
			associatedProduct.setErrorMessage(message);
		}
		return associatedProduct;
	}
	/**
	 * convert json to map data
	 * @param json string
	 * @return map data
	 */
	private Map<String,String> convertStringToMapData(String json){
		Map<String, String> map = new HashMap<String, String>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type    = factory.constructMapType(HashMap.class, String.class, String.class);
			map  = mapper.readValue(json, type);
		}
		catch(IOException e){
			logger.error("error:",e);
		}
		return map;
	}

	/**
	 * This method is used to check the product id is already part of Product type group or not.
	 *
	 * @param productId the id of product.
	 */
	private Map<String,String> checkAssociatedProductInUsing(Long productId, Long prodGroupId, String productGroupTypeCode, Map<String,String> choiceOptions){
		List<CustomerProductGroupMembership> customerProductGroupMemberships = customerProductGroupMembershipRepository.findByKeyProdId(productId);
		Map<String,String> message = new HashMap<>();
		List<Map<String,String>> defaultChoiceOptions = new ArrayList();
		for (CustomerProductGroupMembership customerProductGroupMembership:customerProductGroupMemberships) {
			List<CustomerProductChoice> lstCustomerProductChoice = customerProductChoiceRepository.findByKeyProductIdAndProductGroupChoiceTypePickerSwitch(customerProductGroupMembership.getKey().getProdId(), true);
			if (!lstCustomerProductChoice.isEmpty()) {
				if(!lstCustomerProductChoice.get(0).getProductGroupChoiceType().getProductGroupType().getProductGroupTypeCode().equals(productGroupTypeCode)){
					// When user types a Product ID/UPC which was associated to another Product Group Type
					message.put(ERROR_MESSAGE_KEY ,String.format(PRODUCT_ASSOCIATED_WITH_PRODUCT_GROUP_TYPE, productId,
							lstCustomerProductChoice.get(0).getProductGroupChoiceType().getProductGroupType().getProductGroupType()));
				}
				else if(prodGroupId==null || customerProductGroupMembership.getCustomerProductGroup().getCustProductGroupId() != prodGroupId.intValue()){
					if(lstCustomerProductChoice.get(0).getProductGroupChoiceType().getProductGroupType().getProductGroupTypeCode().equals(productGroupTypeCode)){
						// When user types a Product ID/UPC which was associated to
						// the SAME current picker choice value of the SAME current Product Group Type in ANOTHER Product Group
						findAssociatedProduct(customerProductGroupMembership.getCustomerProductGroup().getCustProductGroupId(),
								customerProductGroupMembership.getCustomerProductGroup().getProductGroupTypeCode(), defaultChoiceOptions);
						for(Map<String,String> map : defaultChoiceOptions){
							if(map.containsKey(PRODUCT_ID_COLUMN_KEY) && map.get(PRODUCT_ID_COLUMN_KEY).equals(String.valueOf(productId))){
								map.remove(PRODUCT_ID_COLUMN_KEY);
								if(choiceOptions.equals(map)){
									message.put(WARNING_MESSAGE_KEY,String.format(PRODUCT_ASSOCIATED_WITH_ANOTHER_PRODUCT_GROUP, productId,
											customerProductGroupMembership.getCustomerProductGroup().getCustProductGroupName()));

								}else{
									message.put(ERROR_MESSAGE_KEY,String.format(PRODUCT_ASSOCIATED_WITH_TWO_DIFFERENCE_COMBINATION, productId));
								}
								break;
							}

						}

					}
				}
			}
		}
		return message;
	}
	/**
	 * Get list ProductGroupChoiceType by choiceTypeCode.
	 *
	 * @param choiceTypeCode the code of ChoiceType.
	 * @return the list of ProductGroupChoiceType.
	 */
	public List<ProductGroupChoiceType> findProductGroupChoiceTypeByChoiceTypeCode(String choiceTypeCode) {
		return this.productGroupChoiceTypeRepository.findAllByChoiceTypeChoiceTypeCode(choiceTypeCode);
	}

	/**
	 * Creates data for Product Group info.
	 *
	 * @param productGroupInfo the information of ProductGroupInfo.
	 * @return productGroupCode of Product Group.
	 */
	public String createProductGroupInfo(ProductGroupInfo productGroupInfo) throws Exception{
		String productGroupId = this.productGroupManagementServiceClient.createProductGroupInfo(productGroupInfo);
		// Save Hierarchies.
		if(productGroupInfo.getHierarchies() != null && !productGroupInfo.getHierarchies().isEmpty()) {
			this.codeTableManagementServiceClient.updateCustomerHierarchyAssignmentForProductGroup(productGroupInfo.getHierarchies(), Long.parseLong(productGroupId), userInfo.getUserId());
			this.productGroupManagementServiceClient.updateAssociatedProducts(productGroupInfo, productGroupId);
		}
		return productGroupId;
	}

	/**
	 * Update the product group info and create a new associated product or the list of choice options of a selected associated product..
	 *
	 * @param productGroupInfo the ProductGroupInfo object.
	 * @return productGroupCode of Product Group.
	 * @throws Exception
	 */
	public String updateProductGroupInfo(ProductGroupInfo productGroupInfo) throws Exception{
		this.productGroupManagementServiceClient.updateProductGroupInfo(productGroupInfo);
		String productGroupId = Long.toString(productGroupInfo.getCustomerProductGroup().getCustProductGroupId());
		this.productGroupManagementServiceClient.updateAssociatedProducts(productGroupInfo, productGroupId);
		return  productGroupId;
	}
	/**
	 * delete data for associated Product.
	 *
	 * @param productGroupInfo the information of ProductGroupInfo.
	 */
	public AssociatedProduct deleteAssociatedProduct(ProductGroupInfo productGroupInfo) throws Exception {
		this.productGroupManagementServiceClient.deleteAssociatedProduct(productGroupInfo);
		return getAssociatedProduct(productGroupInfo.getCustomerProductGroup().getCustProductGroupId(),
				productGroupInfo.getCustomerProductGroup().getProductGroupTypeCode());
	}

	/**
	 * Delete Product Group.
	 *
	 * @param productGroupCode the productGroupCode of ProductGroup.
	 */
	public void deleteProductGroup(String productGroupCode) throws Exception{
		this.productGroupManagementServiceClient.deleteProductGroup(productGroupCode);
	}

	/**
	 * Get data for Associated Products.
	 *
	 * @param custProdId customer product group id
	 * @param customerProductGroupCode customer product group code
	 * @return AssociatedProduct
	 */
	public AssociatedProduct getAssociatedProduct(long custProdId,String customerProductGroupCode){
		return this.findAssociatedProduct(custProdId, customerProductGroupCode, null);
	}
}