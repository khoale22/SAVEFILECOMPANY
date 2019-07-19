/*
 *  ProductGroupECommerceViewService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productGroup.productGroupECommerceView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.heb.pm.entity.*;
import com.heb.pm.productGroup.productGroupInfo.ProductGroupInfo;
import com.heb.pm.productGroup.productGroupInfo.ProductGroupInfoService;
import com.heb.pm.repository.ImageMetaDataRepository;
import com.heb.pm.repository.ProductScanImageBannerRepository;
import com.heb.pm.repository.SellingUnitRepository;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import com.heb.pm.ws.ContentManagementServiceClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.heb.pm.entity.CustomerProductGroup;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Holds all business logic related to product group e-commerce view.
 *
 * @author vn87351
 * @since 2.15.0
 */
@Service
public class ProductGroupECommerceViewService {
	public static final Logger logger = LoggerFactory.getLogger(ProductGroupECommerceViewService.class);

	/**
	 * inject necessary bean
	 */
	@Autowired
	private SellingUnitRepository sellingUnitRepository;
	@Autowired
	private ProductScanImageBannerRepository productScanImageBannerRepository;
	@Autowired
	private ContentManagementServiceClient contentManagementServiceClient;
	@Autowired
	private ProductGroupInfoService productGroupInfoService;
	@Autowired
	private ImageMetaDataRepository imageMetaDataRepository;
	@Autowired
	private CodeTableManagementServiceClient codeTableManagementServiceClient;
	/**
	 * get image metadata by product group and map option selected.
	 * will look up product id on matrix associated product and return the image of product
	 * @param productGroupId product group id
	 * @param salesChannel sale chanel id
	 * @param dataOptionSelected map choice option selected
	 * @return image product
	 */
	public List<ProductScanImageURI> getImageByProductGroupIdAndChoiceOption(Long productGroupId,String salesChannel,String dataOptionSelected){
		List<ProductScanImageURI> productScanImageURI = new ArrayList<>();
		Map<String,String > dataOption = convertStringToMapData(dataOptionSelected);
		Long productId= productGroupInfoService.getProductIdByMapsOption(productGroupId,dataOption);
		if(productId!=0L) {
			List<SellingUnit> sellingUnits = this.sellingUnitRepository.findByProdId(productId);
			if (sellingUnits != null) {
				List<Long> upcs = sellingUnits.stream().map(sellingUnit -> sellingUnit.getUpc()).collect(Collectors.toList());
				List<String> lstImagePriorityCode= new ArrayList<>();
				lstImagePriorityCode.add(ProductScanImageURI.IMAGE_PRIORITY_CD_ALTERNATE);
				lstImagePriorityCode.add(ProductScanImageURI.IMAGE_PRIORITY_CD_PRIMARY);
				productScanImageURI =this.productScanImageBannerRepository.findImageActiveOnline(upcs, StringUtils.trim(salesChannel),
						ProductScanImageURI.IMAGE_STATUS_CD_APPROVED,true,lstImagePriorityCode);
			}
		}
		return productScanImageURI;
	}

	/**
	 * Find the  image by URI
	 * @param imageURI - The image URI
	 * @return byte data
	 */
	public byte[] findImageByUri(String imageURI){
		return this.contentManagementServiceClient.getImage(imageURI);
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
	 * get product group data on e-commerce view product
	 * @param productGroupId product group id
	 * @param salesChannel the channel
	 * @param hierCntxtCd the hierarchy context code
	 * @return product group data
	 */
	public ProductGroupInfo getDataECommerceView(Long productGroupId,String salesChannel,String hierCntxtCd){
		ProductGroupInfo pg=productGroupInfoService.findProductGroupInfo(productGroupId,salesChannel,hierCntxtCd);
		findPrimaryImageChoiceOption(pg);
		return pg;
	}

	/**
	 * find primary image for choice option
	 * @param pg product group  e-commerce view
	 */
	private void findPrimaryImageChoiceOption(ProductGroupInfo pg){
		for(ProductGroupChoiceType choiceType:pg.getCustomerProductGroup().getProductGroupType().getProductGroupChoiceTypes()){
			for(ProductGroupChoiceOption choiceOpt:choiceType.getProductGroupChoiceOptions()){
				Long id=Long.parseLong(choiceOpt.getChoiceOption().getKey().getChoiceOptionCode());
				choiceOpt.getChoiceOption().setImageMetaDataChoiceOption(getPrimaryImageChoiceOptionByCode(id));
			}
		}
		/**
		 * sort choice option
		 */
		Collections.sort(pg.getCustomerProductGroup().getProductGroupType().getProductGroupChoiceTypes(), new Comparator<ProductGroupChoiceType>() {
			/**
			 * Compare.
			 * @param attr1
			 *            the attr1
			 * @param attr2
			 *            the attr2
			 * @return the int
			 */
			@Override
			public int compare(ProductGroupChoiceType attr1, ProductGroupChoiceType attr2) {
				return attr2.getPickerSwitch().compareTo(attr1.getPickerSwitch());
			}
		});
	}
	/**
	 * get image from image metadata repository
	 * @param choiceOptionCode the code
	 * @return
	 */
	private ImageMetaData getPrimaryImageChoiceOptionByCode(Long choiceOptionCode){
		return imageMetaDataRepository.findPrimaryChoiceOptionImages
				(ImageMetaData.IMAGE_CHOICE_OPTION_SUBJECT_TYPE, ImageMetaData.IMAGE_ACTIVATED,
						ImageMetaData.IMAGE_ACTIVATED_ONLINE, ImageMetaData.IMAGE_PRIORITY_TYPE,choiceOptionCode);
	}
	/**
	 * Delete information for product group.
	 *
	 * @param customerProductGroup The customer product group.
	 * @author vn70633
	 */
	public void deleteProductGroup(CustomerProductGroup customerProductGroup) {
		this.codeTableManagementServiceClient.updateProductGroup(customerProductGroup, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
	}

	/**
	 * Update information for product group.
	 *
	 * @param customerProductGroup The customer product group.
	 * @author vn70633
	 */
	public void updateProductGroup(CustomerProductGroup customerProductGroup) {
		this.codeTableManagementServiceClient.updateProductGroup(customerProductGroup, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
	}

}
