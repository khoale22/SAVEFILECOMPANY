/*
 *
 * VariantsService.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.productDetails.product.variants;

import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.pm.ws.ContentManagementServiceClient;
import com.heb.pm.ws.MasterDataServiceClient;
import com.heb.pm.ws.ProductAttributeManagementServiceClient;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.controller.ModifiedEntity;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This holds all of the business logic for Variants .
 *
 * @author vn87351
 * @since 2.16.0
 */
@Service
public class VariantsService {
	@Autowired
	private ProductRelationshipRepository productRelationshipRepository;
	@Autowired
	private ProductScanImageBannerRepository productScanImageBannerRepository;
	@Autowired
	private ContentManagementServiceClient contentManagementServiceClient;
	@Autowired
	ProductItemVariantRepository productItemVariantRepository;
	@Autowired
	ProductAttributeManagementServiceClient productAttributeManagementServiceClient;
	@Autowired
	ProductManagementServiceClient productManagementServiceClient;
	@Autowired
	private CandidateWorkRequestRepository candidateWorkRequestRepository;
	@Autowired
	MasterDataServiceClient masterDataServiceClient;
	@Autowired
	ProductScanImageURIRepository productScanImageURIRepository;
	@Autowired
	private CandidateProductMasterRepository candidateProductMasterRepository;

	private static final String URI_KEY = "uri";
	private static final String UPC_KEY = "upc";
	private static final String DATA_KEY = "data";

	private static final String YES_CHAR = "Y";

	/**
	 * Gets variants data based on the product Id.
	 *
	 * @param productId the product Id of the product.
	 * @return the variants data and related productMaster data.
	 */
	public List<ProductRelationship> getVariantsData(Long productId) {
		List<ProductRelationship> associatedVariants = this.productRelationshipRepository.findByRelatedProductProdIdAndKeyProductRelationshipCodeOrderByKeyProductId(productId, ProductRelationship.ProductRelationshipCode.Variant_Product.getValue());
		List<Long> variantsIds = new ArrayList<>();
		for (ProductRelationship relationship : associatedVariants) {
			variantsIds.add(relationship.getKey().getProductId());
		}
		if(variantsIds.isEmpty()){
			variantsIds.add(productId);
		}
		return this.productRelationshipRepository.findByKeyProductIdInAndKeyProductRelationshipCode(variantsIds, ProductRelationship.ProductRelationshipCode.Variant_Product.getValue());
	}

	/**
	 * This method gets a list of primary image (ProductScanImageURIs uploaded Images) based on a upc
	 *
	 * @param upcs the upc of the product
	 * @return list of uploaded images
	 */
	public ModifiedEntity<List<ProductScanImageURI>> getPrimaryImage(List<Long> upcs, String returnMessage) {
		List<ProductScanImageURI> productScanImageURIs = this.productScanImageURIRepository.findPrimaryImageByScnCodes(upcs, ProductScanImageURI.IMAGE_PRIORITY_CD_PRIMARY);
		return new ModifiedEntity<>(productScanImageURIs, returnMessage);
	}

	/**
	 * Find the  image by URI
	 *
	 * @param imageURI - The image URI
	 * @return byte data
	 */
	public byte[] findImageByUri(String imageURI) {
		return this.contentManagementServiceClient.getImage(imageURI);
	}

	/**
	 * save variant item
	 *
	 * @param lst list item variant
	 */
	public void saveVariantItem(List<ProductItemVariant> lst) {
		if (lst != null && !lst.isEmpty()) {
			if (lst.get(0).getParentItemMaster() != null) {
				productManagementServiceClient.saveItemMasterVariant(lst.get(0).getParentItemMaster());
			}
			productAttributeManagementServiceClient.saveProductItemVariant(lst);
		}
	}

	/**
	 * fill data for candidate work request
	 *
	 * @param candidateWorkRequest the candidate work request entity
	 * @param userId               user login
	 */
	private void setterDataCandidateWorkRequest(CandidateWorkRequest candidateWorkRequest, String userId) {
		CandidateProductMaster psRoot = null;
		List<CandidateProductMaster> lstCandidate = new ArrayList<>();
		candidateWorkRequest.setCandidateMasterDataExtensionAttributes(new ArrayList<>());
		for (CandidateProductMaster ps : candidateWorkRequest.getCandidateProductMaster()) {
			if (ps.getCandidateProductId() != null) {
				psRoot = ps;
				psRoot.setCandidateProductRelationships(new ArrayList<>());
			} else if (ps.getCandidateProductId() == null || ps.getCandidateProductId() == 0L) {
				Long seqNbr = 0L;
				List<CandidateMasterDataExtensionAttribute> candidateMasterDataExtensionAttributes = ps.getCandidateMasterDataExtensionAttributes();
				ps.setLstUpdtUsrId(userId);
				ps.setLastUpdateTs(LocalDateTime.now());
				ps.setNewDataSw(true);
				ps.setCandidateWorkRequest(candidateWorkRequest);
				//Setter relationship of candidate description with candidate product master and candidate work request.
				for (CandidateDescription candidateDescription : ps.getCandidateDescriptions()) {
					candidateDescription.setLastUpdateUserId(userId);
					candidateDescription.setLastUpdateDate(LocalDateTime.now());
					candidateDescription.setCandidateProductMaster(ps);
				}
				//Setter relationship of candidate scan code extension with candidate product master and candidate work request.
				if(ps.getCandidateScanCodeExtents() != null) {
					for (CandidateScanCodeExtent candidateScanCodeExtent : ps.getCandidateScanCodeExtents()) {
						candidateScanCodeExtent.setCandidateProductMaster(ps);
						candidateScanCodeExtent.setCandidateWorkRequest(candidateWorkRequest);
					}
				}
				//Save ps product master
				ps = candidateProductMasterRepository.save(ps);
				//Gen product relationship
				CandidateProductRelationship rlsh = new CandidateProductRelationship();
				CandidateProductRelationshipKey rlshKey = new CandidateProductRelationshipKey();
				rlshKey.setProductRelationshipCode(CandidateProductRelationship.PRODUCT_VARIANT_RELATIONSHIP_CODE);
				rlsh.setKey(rlshKey);
				rlsh.setLastUpdateTs(LocalDateTime.now());
				rlsh.setLstUpdtUsrId(userId);
				rlsh.setCandidateProductMaster(psRoot);
				rlsh.setCandidateRelatedProductId(ps);
				rlsh.setQuantity(0.0);
				rlsh.setProductScanCode(ps.getProductScanCodeVariant());
				psRoot.getCandidateProductRelationships().add(rlsh);
				lstCandidate.add(ps);
				//add ps_product_id for candidate master data extension
				if(candidateMasterDataExtensionAttributes != null){
					for (CandidateMasterDataExtensionAttribute candidateMasterDataExtensionAttribute : candidateMasterDataExtensionAttributes){
						candidateMasterDataExtensionAttribute.getKey().setKeyId(ps.getCandidateProductId());
						candidateMasterDataExtensionAttribute.getKey().setSequenceNumber(seqNbr++);
						candidateMasterDataExtensionAttribute.setCreateDate(LocalDateTime.now());
						candidateMasterDataExtensionAttribute.setLastUpdateDate(LocalDateTime.now());
						candidateMasterDataExtensionAttribute.setCreateUserId(userId);
						candidateMasterDataExtensionAttribute.setLastUpdateUserId(userId);
						candidateMasterDataExtensionAttribute.setNewData(YES_CHAR);
						candidateMasterDataExtensionAttribute.setCandidateWorkRequest(candidateWorkRequest);
					}
				}
				candidateWorkRequest.getCandidateMasterDataExtensionAttributes().addAll(candidateMasterDataExtensionAttributes);
			}
		}
		lstCandidate.add(psRoot);
		candidateWorkRequest.setCandidateProductMaster(lstCandidate);
	}

	/**
	 * update candidate work request
	 *
	 * @param candidateWorkRequest the candidate work request entity
	 * @param userId               user name
	 * @return candidate work request entity
	 */
	public CandidateWorkRequest updateCandidateWorkRequest(CandidateWorkRequest candidateWorkRequest, String userId) {
		setterDataCandidateWorkRequest(candidateWorkRequest, userId);
		candidateWorkRequest = candidateWorkRequestRepository.save(candidateWorkRequest);
		masterDataServiceClient.activateCandidate(candidateWorkRequest.getWorkRequestId().intValue());
		return candidateWorkRequest;
	}

	/**
	 * create candidate work request
	 *
	 * @param productId the product id
	 * @param userId    the user login
	 * @return candidate work request entity
	 */
	public CandidateWorkRequest createCandidateWorkRequest(Long productId, String userId) {
		CandidateWorkRequest workRequest = new CandidateWorkRequest();
		if (productId != null) {
			workRequest.setProductId(Long.valueOf(productId));
			workRequest.setIntent(CandidateWorkRequest.INTENT.INTRODUCE_A_VARIANT.getName());
			workRequest.setStatus(CandidateWorkRequest.StatusCode.IN_PROGRESS.getName());
			workRequest.setSourceSystem(CandidateWorkRequest.SRC_SYSTEM_ID);
			workRequest.setReadyToActivate(CandidateWorkRequest.RDY_TO_ACTVD_SW_DEFAULT);
			workRequest.setUserId(userId);
			workRequest.setCreateDate(LocalDateTime.now());
			workRequest.setLastUpdateUserId(userId);
			workRequest.setUserId(userId);
			workRequest.setLastUpdateDate(LocalDateTime.now());
			CandidateProductMaster candidateProductMaster = new CandidateProductMaster();
			candidateProductMaster.setProductId(productId);
			candidateProductMaster.setLastUpdateTs(LocalDateTime.now());
			candidateProductMaster.setCandidateWorkRequest(workRequest);
			candidateProductMaster.setLstUpdtUsrId(userId);
			List<CandidateProductMaster> candidateProductMasters = new ArrayList<>();
			candidateProductMasters.add(candidateProductMaster);
			workRequest.setCandidateProductMaster(candidateProductMasters);
		}
		return candidateWorkRequestRepository.save(workRequest);
	}

	/**
	 * Call webservice to Assign Image(s) to Variant(s) Product.
	 *
	 * @param images list of images
	 * @param upcs   list of upcs
	 * @param userId userid
	 * @return JSONObject
	 * @throws JSONException
	 */
	public JSONObject assignImages(List<ProductScanImageURI> images, List<Long> upcs, String userId) throws JSONException {
		List<ProductScanImageURI> variantImages = productScanImageURIRepository.findByKeyIdIn(upcs);
		List<ProductScanImageURI> updatePriorityImages = new ArrayList<>();
		JSONObject jsonReturn = new JSONObject();
		JSONArray array = new JSONArray();
		if (!variantImages.isEmpty()) {
			for (ProductScanImageURI image : images) {
				for (ProductScanImageURI variantImage : variantImages) {
					if (image.getImageURI().equals(variantImage.getImageURI())) {
						JSONObject item = new JSONObject();
						item.put(URI_KEY, image.getImageURI());
						item.put(UPC_KEY, variantImage.getKey().getId());
						array.put(item);
					}
				}
			}
			if (array.length() == 0) {
				//Handle if Image Priority is Primary.
				for (ProductScanImageURI image : images) {
					for (ProductScanImageURI variantImage : variantImages) {
						if (image.getImagePriorityCode().trim().equals(ProductScanImageURI.IMAGE_PRIORITY_CD_PRIMARY)
								&& variantImage.getImagePriorityCode().trim().equals(ProductScanImageURI.IMAGE_PRIORITY_CD_PRIMARY)) {
							variantImage.setImagePriorityCode(ProductScanImageURI.IMAGE_PRIORITY_CD_ALTERNATE);
							updatePriorityImages.add(variantImage);
						}
					}
				}
				if (!updatePriorityImages.isEmpty()) {
					this.changeImagePriority(updatePriorityImages, userId);
				}
				for (Long upc : upcs) {
					this.productAttributeManagementServiceClient.assignImages(images, upc, userId);
				}
			} else {
				jsonReturn.put(DATA_KEY, array);
			}
		} else {
			for (Long upc : upcs) {
				this.productAttributeManagementServiceClient.assignImages(images, upc, userId);
			}
		}
		return jsonReturn;
	}

	/**
	 * Change Priority Code of Image.
	 *
	 * @param images
	 * @param userId
	 */
	private void changeImagePriority(List<ProductScanImageURI> images, String userId) {
		this.productAttributeManagementServiceClient.updateImageInfo(images, userId);
	}
}
