/*
 *  NutritionFactsService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.nutritionFacts;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.pm.ws.*;
import com.heb.util.list.LongPopulator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds all business logic related to product nutrition facts.
 *
 * @author vn73545
 * @since 2.15.0
 */
@Service
public class NutritionFactsService {

	private static final Logger logger = LoggerFactory.getLogger(NutritionFactsService.class);

	private static final String ESHA_GENESIS_SOURCE_SYSTEM = "ESHA GENESIS";
	private static final String SCALE_MANAGEMENT_SOURCE_SYSTEM = "Scale Management";

	@Autowired
	private TargetSystemAttributePriorityRepository targetSystemAttributePriorityRepository;

	@Autowired
	private ProductNutrientRepository productNutrientRepository;

	@Autowired
	private CandidateNutrientRepository candidateNutrientRepository;

	@Autowired
	private ScaleUpcRepository scaleUpcRepository;

	@Autowired
	private ProductPkVariationRepository productPkVariationRepository;

	@Autowired
	private CandidateProductPkVariationRepository candidateProductPkVariationRepository;

	@Autowired
	private CandidateWorkRequestRepository candidateWorkRequestRepository;

	@Autowired
	private MasterDataExtensionAttributeRepository masterDataExtensionAttributeRepository;

	@Autowired
	private NutrientPanelHeaderRepository nutrientPanelHeaderRepository;

	@Autowired
	private CandidateNutrientPanelHeaderRepository candidateNutrientPanelHeaderRepository;

	@Autowired
	private MasterDataServiceClient masterDataServiceClient;

	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	// The default number of items to search for. This number
	// and fewer will have the maximum performance.
	private static final int DEFAULT_ITEM_COUNT = 100;

	// Used to get consistent size lists to query runners.
	private LongPopulator longPopulator = new LongPopulator();

	/**
	 * Get all nutrition facts information.
	 *
	 * @param productId - The product id.
	 * @param primaryUpc - The primary scan code id.
	 * @return The list of NutritionFact.
	 */
	public List<NutritionFact> getAllNutritionFactsInformation(Long productId, Long primaryUpc) {
		List<NutritionFact> returnList = new ArrayList<NutritionFact>();
		List<Long> upcs = this.getAllUpcsByProductId(productId, primaryUpc);
		if(CollectionUtils.isNotEmpty(upcs)){
			for (Long upc : upcs) {
				List<NutritionFact> listUnApprovedNutritionFacts = this.getAllUnApprovedNutritionFacts(upc);
				if (listUnApprovedNutritionFacts != null && listUnApprovedNutritionFacts.size() > 0) {
					returnList.addAll(listUnApprovedNutritionFacts);
				}

				List<NutritionFact> listApprovedNutritionFacts = this.getAllApprovedNutritionFacts(upc);
				if (listApprovedNutritionFacts != null && listApprovedNutritionFacts.size() > 0) {
					returnList.addAll(listApprovedNutritionFacts);
				}
			}
		}
		return returnList;
	}

	/**
	 * Get all upcs by product id.
	 *
	 * @param productId - The product id.
	 * @param upc - The scan code id.
	 * @return The list of scan code id.
	 */
	private List<Long> getAllUpcsByProductId(Long productId, Long upc) {
		List<Long> returnList = new ArrayList<Long>();
		returnList.add(upc);
		List<SellingUnit> sellingUnits = this.sellingUnitRepository.findByProdIdOrderByUpcAsc(productId);
		if(CollectionUtils.isNotEmpty(sellingUnits)){
			for (SellingUnit sellingUnit : sellingUnits) {
				if(!returnList.contains(sellingUnit.getUpc())){
					returnList.add(sellingUnit.getUpc());
				}
			}
		}
		return returnList;
	}

	/**
	 * Get all unapproved nutrition facts.
	 *
	 * @param upc - The scan code id.
	 * @return The list of NutritionFact.
	 */
	private List<NutritionFact> getAllUnApprovedNutritionFacts(Long upc) {
		List<NutritionFact> returnList = new ArrayList<NutritionFact>();
		boolean hasData = false;
		NutritionFact nutritionFact = new NutritionFact();
		List<CandidateNutrient> candidateNutrientList = this.getCandidateNutritionFactData(upc);
		List<CandidateProductPkVariation> candidateProductPkVariationList = this.getCandidateNutritionFactHeader(upc);
		if (candidateNutrientList != null && candidateNutrientList.size() > 0) {
			nutritionFact.setCandidateNutrients(candidateNutrientList);
			nutritionFact.setSourceSystemDescription(NutritionFactsService.ESHA_GENESIS_SOURCE_SYSTEM);
			nutritionFact.setSourceSystemId(SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_ESHA_GENESIS.getValue());
			nutritionFact.setSourceSystemDefault(true);
			hasData = true;
		}
		if (candidateProductPkVariationList != null && candidateProductPkVariationList.size() > 0) {
			nutritionFact.setCandidateProductPkVariations(candidateProductPkVariationList);
			nutritionFact.setSourceSystemDescription(NutritionFactsService.ESHA_GENESIS_SOURCE_SYSTEM);
			nutritionFact.setSourceSystemId(SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_ESHA_GENESIS.getValue());
			nutritionFact.setSourceSystemDefault(true);
			hasData = true;
		}
		//get Ingredients and Allergens
		this.getUnApprovedIngredientsAndAllergensData(upc, nutritionFact);
		if(hasData){
			returnList.add(nutritionFact);
		}
		return returnList;
	}

	/**
	 * Get ingredients and allergens of unapproved nutrition fact.
	 *
	 * @param upc - The scan code id.
	 * @param nutritionFact - The nutrition fact.
	 */
	private void getUnApprovedIngredientsAndAllergensData(Long upc, NutritionFact nutritionFact) {
		List<Long> upcs = new ArrayList<Long>();
		upcs.add(upc);
		List<String> lstStatus = new ArrayList<String>();
		lstStatus.add(CandidateWorkRequest.StatusCode.DELETED.getName());
		lstStatus.add(CandidateWorkRequest.StatusCode.SUCCESS.getName());
		lstStatus.add(CandidateWorkRequest.StatusCode.REJECTED.getName());
		this.longPopulator.populate(upcs, DEFAULT_ITEM_COUNT);
		List<CandidateWorkRequest> lstCandidateWorkRqst = 
				this.candidateWorkRequestRepository.findByIntentAndStatusNotInAndUpcIn(SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_ESHA_GENESIS.getValue().intValue(), lstStatus, upcs);
		if(CollectionUtils.isNotEmpty(lstCandidateWorkRqst)){
			List<CandidateMasterDataExtensionAttribute> candidateMasterDataExtensionAttributes = lstCandidateWorkRqst.get(0).getCandidateMasterDataExtensionAttributes();
			if(CollectionUtils.isNotEmpty(candidateMasterDataExtensionAttributes)){
				for (CandidateMasterDataExtensionAttribute candidateMasterDataExtensionAttribute : candidateMasterDataExtensionAttributes) {
					if(candidateMasterDataExtensionAttribute.getKey().getAttributeId() == 1643){//Ingredients
						nutritionFact.setIngredients(candidateMasterDataExtensionAttribute.getAttributeValueText());
					}
					if(candidateMasterDataExtensionAttribute.getKey().getAttributeId() == 1644){//Allergens
						nutritionFact.setAllergens(candidateMasterDataExtensionAttribute.getAttributeValueText());
					}
				}
			}
			List<CandidateNutrientPanelHeader> candidateNutrientPanelHeaders = lstCandidateWorkRqst.get(0).getCandidateNutrientPanelHeaders();
			if(CollectionUtils.isNotEmpty(candidateNutrientPanelHeaders) && candidateNutrientPanelHeaders.get(0).getSourceTime() != null){
				nutritionFact.setCreatedDate(candidateNutrientPanelHeaders.get(0).getSourceTime().toLocalDate());
			}

		}
	}

	/**
	 * Get all approved nutrition facts.
	 *
	 * @param upc - The scan code id.
	 * @return The list of NutritionFact.
	 */
	private List<NutritionFact> getAllApprovedNutritionFacts(Long upc) {
		List<NutritionFact> returnList = new ArrayList<NutritionFact>();
		List<NutritionFact> sourceList = new ArrayList<NutritionFact>();

		//get source 13(eCommerce)
		NutritionFact nutritionFactSourceEcommerce = this.getNutritionFactBySourceSystem(upc, SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_ECOMMERCE.getValue());
		if(nutritionFactSourceEcommerce != null){
			returnList.add(nutritionFactSourceEcommerce);
		}
		//get other sources
		List<TargetSystemAttributePriority> targetSystemAttributePriorities = this
				.targetSystemAttributePriorityRepository.findByKeyLogicalAttributeIdOrderByAttributePriorityNumber(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_NUTRITION_FACTS.getValue().intValue());

		if(targetSystemAttributePriorities != null){
			for (TargetSystemAttributePriority targetSystemAttributePriority : targetSystemAttributePriorities) {
				NutritionFact nutritionFact =
						this.getNutritionFactBySourceSystem(upc, targetSystemAttributePriority.getKey().getDataSourceSystemId());
				if(nutritionFact != null){
					sourceList.add(nutritionFact);
				}
			}
		}
		if(sourceList != null && sourceList.size() > 0) {
			returnList.addAll(sourceList);
		}
		return returnList;
	}

	/**
	 * Get nutrition fact information by source system.
	 *
	 * @param upc - The scan code id.
	 * @param sourceSystem - The sources system.
	 * @return The NutritionFact.
	 */
	private NutritionFact getNutritionFactBySourceSystem(Long upc, Long sourceSystem) {
		NutritionFact nutritionFact = new NutritionFact();
		nutritionFact.setSourceSystemId(sourceSystem);
		boolean hasData = false;
		if(sourceSystem == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_SCALE_MANAGEMENT.getValue()){//source17
			ScaleUpc scaleUpc = this.getNutritionFactByScaleSource(upc);
			if(scaleUpc != null){
				nutritionFact.setScaleUpc(scaleUpc);
				nutritionFact.setSourceSystemDescription(NutritionFactsService.SCALE_MANAGEMENT_SOURCE_SYSTEM);
				hasData = true;
			}
		}else{
			List<ProductNutrient> productNutrientList = this.getNutritionFactDataBySourceSystem(upc, sourceSystem);
			List<ProductPkVariation> productPkVariationList = this.getNutritionFactHeaderBySourceSystem(upc, sourceSystem);
			if (productNutrientList != null && productNutrientList.size() > 0) {
				nutritionFact.setProductNutrients(productNutrientList);
				nutritionFact.setSourceSystemDescription(productNutrientList.get(0).getSourceSystem().getDescription());
				hasData = true;
			}
			if (productPkVariationList != null && productPkVariationList.size() > 0) {
				nutritionFact.setProductPkVariations(productPkVariationList);
				nutritionFact.setSourceSystemDescription(productPkVariationList.get(0).getSourceSystem().getDescription());
				hasData = true;
			}

			//get Ingredients and Allergens of source26
			if(sourceSystem == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_ESHA_GENESIS.getValue()){//source26
				this.getApprovedIngredientsAndAllergensData(upc, sourceSystem, nutritionFact);
			}
		}
		return hasData ? nutritionFact : null;
	}

	/**
	 * Get ingredients and allergens of approved nutrition fact.
	 *
	 * @param upc - The scan code id.
	 * @param sourceSystem - The source system.
	 * @param nutritionFact - The nutrition fact.
	 */
	private void getApprovedIngredientsAndAllergensData(Long upc, Long sourceSystem, NutritionFact nutritionFact) {
		List<MasterDataExtensionAttribute> masterDataExtensionAttributes = 
				this.masterDataExtensionAttributeRepository.findByKeyIdAndKeyDataSourceSystem(upc, sourceSystem);
		if(CollectionUtils.isNotEmpty(masterDataExtensionAttributes)){
			for (MasterDataExtensionAttribute masterDataExtensionAttribute : masterDataExtensionAttributes) {
				if(masterDataExtensionAttribute.getKey().getAttributeId() == 1643){//Ingredients
					nutritionFact.setIngredients(masterDataExtensionAttribute.getAttributeValueText());
				}
				if(masterDataExtensionAttribute.getKey().getAttributeId() == 1644){//Allergens
					nutritionFact.setAllergens(masterDataExtensionAttribute.getAttributeValueText());
				}
			}
		}
		List<NutrientPanelHeader> nutrientPanelHeaders = this.nutrientPanelHeaderRepository.findByKeyUpcAndKeySourceSystemId(upc, sourceSystem);
		if(CollectionUtils.isNotEmpty(nutrientPanelHeaders) 
				&& nutrientPanelHeaders.get(0).getSourceTime() != null){
			nutritionFact.setCreatedDate(nutrientPanelHeaders.get(0).getSourceTime().toLocalDate());
		}
	}

	/**
	 * Get list of product nutrient by source system.
	 *
	 * @param upc - The scan code id.
	 * @param sourceSystem - The source system.
	 * @return The list of ProductNutrient.
	 */
	private List<ProductNutrient> getNutritionFactDataBySourceSystem(Long upc, Long sourceSystem) {
		List<ProductNutrient> returnList = null;
		if(sourceSystem != SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_SCALE_MANAGEMENT.getValue()){
			returnList = this.productNutrientRepository.findByKeyUpcAndKeySourceSystemOrderByKeyValPreprdTypCdDescKeyMasterIdAsc(upc, sourceSystem.intValue());
		}
		return returnList;
	}

	/**
	 * Get list of candidate nutrition fact.
	 *
	 * @param upc - The scan code id.
	 * @return The list of CandidateNutrient.
	 */
	private List<CandidateNutrient> getCandidateNutritionFactData(Long upc) {
		List<CandidateNutrient> returnList = new ArrayList<CandidateNutrient>();
		List<CandidateNutrient> candidateNutrientList = this.candidateNutrientRepository.findByKeyUpcOrderByValPreprdTypCdDescMasterIdAsc(upc);
		for (CandidateNutrient candidateNutrient : candidateNutrientList) {
			if(candidateNutrient.getCandidateWorkRequest() != null 
					&& candidateNutrient.getCandidateWorkRequest().getIntent() == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_ESHA_GENESIS.getValue().intValue()
					&& !candidateNutrient.getCandidateWorkRequest().getStatus().equals(CandidateWorkRequest.StatusCode.SUCCESS.getName())
					&& !candidateNutrient.getCandidateWorkRequest().getStatus().equals(CandidateWorkRequest.StatusCode.DELETED.getName())
					&& !candidateNutrient.getCandidateWorkRequest().getStatus().equals(CandidateWorkRequest.StatusCode.REJECTED.getName())){
				returnList.add(candidateNutrient);
			}
		}
		return returnList;
	}

	/**
	 * Get nutrition fact information of scale source.
	 *
	 * @param upc - The scan code id.
	 * @return The ScaleUpc.
	 */
	private ScaleUpc getNutritionFactByScaleSource(Long upc) {
		ScaleUpc returnScaleUpc = null;
		ScaleUpc scaleUpc = this.scaleUpcRepository.findOne(upc);
		if(scaleUpc != null){
			if(scaleUpc.getNutrientStatementDetails() != null && scaleUpc.getNutrientStatementDetails().size() > 0) {
				returnScaleUpc = scaleUpc;
			}
			if(scaleUpc.getNutrientStatementHeader() != null) {
				returnScaleUpc = scaleUpc;
			}
		}
		return returnScaleUpc;
	}

	/**
	 * Get header information of nutrition fact by source system.
	 *
	 * @param upc - The scan code id.
	 * @param sourceSystem - The source system.
	 * @return The list of ProductPkVariation.
	 */
	private List<ProductPkVariation> getNutritionFactHeaderBySourceSystem(Long upc, Long sourceSystem) {
		List<ProductPkVariation> returnList = null;
		if(sourceSystem != SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_SCALE_MANAGEMENT.getValue()){
			returnList = this.productPkVariationRepository.findByKeyUpcAndKeySourceSystem(upc, sourceSystem.intValue());
		}
		return returnList;
	}

	/**
	 * Get header information of candidate nutrition fact.
	 *
	 * @param upc - The scan code id.
	 * @return The list of CandidateProductPkVariation.
	 */
	private List<CandidateProductPkVariation> getCandidateNutritionFactHeader(Long upc) {
		List<CandidateProductPkVariation> returnList = new ArrayList<CandidateProductPkVariation>();
		List<CandidateProductPkVariation> candidateProductPkVariationList = this.candidateProductPkVariationRepository.findByKeyUpc(upc);
		for (CandidateProductPkVariation candidateProductPkVariation : candidateProductPkVariationList) {
			if(candidateProductPkVariation.getCandidateWorkRequest() != null 
					&& candidateProductPkVariation.getCandidateWorkRequest().getIntent() == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_ESHA_GENESIS.getValue().intValue()
					&& !candidateProductPkVariation.getCandidateWorkRequest().getStatus().equals(CandidateWorkRequest.StatusCode.SUCCESS.getName())
					&& !candidateProductPkVariation.getCandidateWorkRequest().getStatus().equals(CandidateWorkRequest.StatusCode.DELETED.getName())
					&& !candidateProductPkVariation.getCandidateWorkRequest().getStatus().equals(CandidateWorkRequest.StatusCode.REJECTED.getName())){
				returnList.add(candidateProductPkVariation);
			}
		}
		return returnList;
	}

	/**
	 * Approve nutrition fact information.
	 *
	 * @param psWorkIds The list of candidate work id.
	 */
	@CoreTransactional
	public void approveNutritionFactInformation(List<Long> psWorkIds){
		if(psWorkIds != null ){
			for (Long psWorkId: psWorkIds) {
				List<CandidateNutrientPanelHeader> lstCandidateNutrientPanelHeader = this.candidateNutrientPanelHeaderRepository.findByKeyWorkRequestId(psWorkId);
				if(CollectionUtils.isNotEmpty(lstCandidateNutrientPanelHeader)){
					for (CandidateNutrientPanelHeader candidateNutrientPanelHeader: lstCandidateNutrientPanelHeader) {
						candidateNutrientPanelHeader.setApproved(true);
					}
					this.candidateNutrientPanelHeaderRepository.save(lstCandidateNutrientPanelHeader);
					this.masterDataServiceClient.activateCandidate(psWorkId.intValue());
				}
			}
		}
	}
}