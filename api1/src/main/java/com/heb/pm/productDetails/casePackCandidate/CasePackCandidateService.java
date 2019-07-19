/*
 *  CasePackInfoCandidateService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.casePackCandidate;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.pm.ws.MasterDataServiceClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to case pack in candidate mode.
 *
 * @author vn73545
 * @since 2.7.0
 */
@Service
public class CasePackCandidateService {

	@Autowired
	private CandidateItemMasterRepository candidateItemMasterRepository;

	@Autowired
	private CandidateVendorLocationItemRepository candidateVendorLocationItemRepository;

	@Autowired
	private CandidateWarehouseLocationItemRepository candidateWarehouseLocationItemRepository;

	@Autowired
	private CandidateItemWarehouseCommentsRepository candidateItemWarehouseCommentsRepository;

	@Autowired
	private CandidateVendorItemFactoryXrfRepository candidateVendorItemFactoryXrfRepository;

    @Autowired
    private CandidateWorkRequestRepository candidateWorkRequestRepository;

    @Autowired
    private CandidateStatusRepository candidateStatusRepository;

    @Autowired
    private MasterDataServiceClient masterDataServiceClient;

    @Autowired
    private CandidateProductTypeRepository candidateProductTypeRepository;
    
    private static final String ACTIVATED_STATUS_CODE = "108";
    private static final String REJECTED_STATUS_CODE = "105";

    private static final long STATUS_CHANGE_REASON_SAME_USERID = 4;
    private static final long STATUS_CHANGE_REASON_DIFF_USERID = 2;
    
    /**
     * Get candidate information by ps item id.
     *
     * @param psItmId - The ps item id.
     * @return The list of CandidateItemMaster.
     */
    public List<CandidateItemMaster> getCandidateInformation(Integer psItmId) {
        return this.candidateItemMasterRepository.findByCandidateItemId(psItmId);
    }

    /**
	 * Get all candidate product types.
	 *
	 * @return list with all candidate product types.
	 */
    public List<CandidateProductType> getAllCandidateProductTypes() {
		return this.candidateProductTypeRepository.findAll();
	}

    /**
     * Updates case pack information in candidate mode.
     *
     * @param candidateItemMaster The item master that is sent to the web service to be updated.
     * @param userId The user id.
     */
    @CoreTransactional
    public void updateCasePackInfoCandidate(CandidateItemMaster candidateItemMaster, String userId) {
        Integer psItmId = candidateItemMaster.getCandidateItemId();
		CandidateItemMaster newCandidateItemMaster = this.candidateItemMasterRepository.getOne(psItmId);
        if(null != newCandidateItemMaster){
			newCandidateItemMaster.setCaseUpc(candidateItemMaster.getCaseUpc());
			newCandidateItemMaster.setItemDescription(candidateItemMaster.getItemDescription());
			newCandidateItemMaster.setLastUpdateUserId(userId);
			newCandidateItemMaster.setLastUpdateDate(LocalDateTime.now());
			newCandidateItemMaster.setAddedDate(LocalDate.now());
			newCandidateItemMaster.setAddedUserId(userId);
			newCandidateItemMaster.setCandidateItemId(psItmId);
            
            this.candidateItemMasterRepository.save(newCandidateItemMaster);
        }
    }

    /**
     * Updates case pack import in candidate mode.
	 *
	 * @param candidateVendorLocationItem The ps vend loc item to be updated.
     * @param userId The user id.
     */
    @CoreTransactional
    public void updateCasePackImportCandidate(CandidateVendorLocationItem candidateVendorLocationItem, String userId) {
		CandidateVendorLocationItemKey candidateVendorLocationItemKey = new CandidateVendorLocationItemKey();
		candidateVendorLocationItemKey.setCandidateItemId(candidateVendorLocationItem.getKey().getCandidateItemId());
		candidateVendorLocationItemKey.setVendorNumber(candidateVendorLocationItem.getKey().getVendorNumber());
		candidateVendorLocationItemKey.setVendorType(candidateVendorLocationItem.getKey().getVendorType());
		CandidateVendorLocationItem newCandidateVendorLocationItem = this.candidateVendorLocationItemRepository.getOne(candidateVendorLocationItemKey);
    	if(null != newCandidateVendorLocationItem){
			newCandidateVendorLocationItem.setHtsNumber(candidateVendorLocationItem.getHtsNumber());
			newCandidateVendorLocationItem.setHts2Number(candidateVendorLocationItem.getHts2Number());
			newCandidateVendorLocationItem.setHts3Number(candidateVendorLocationItem.getHts3Number());
			newCandidateVendorLocationItem.setMinTypeText(candidateVendorLocationItem.getMinTypeText());
			newCandidateVendorLocationItem.setMinOrderQuantity(candidateVendorLocationItem.getMinOrderQuantity());
			newCandidateVendorLocationItem.setPickupPoint(candidateVendorLocationItem.getPickupPoint());
			newCandidateVendorLocationItem.setContainerSizeCode(candidateVendorLocationItem.getContainerSizeCode());
			newCandidateVendorLocationItem.setIncoTermCode(candidateVendorLocationItem.getIncoTermCode());
			newCandidateVendorLocationItem.setSeason(candidateVendorLocationItem.getSeason());
			newCandidateVendorLocationItem.setSellByYear(candidateVendorLocationItem.getSellByYear());
			newCandidateVendorLocationItem.setColor(candidateVendorLocationItem.getColor());
			newCandidateVendorLocationItem.setCartonMarking(candidateVendorLocationItem.getCartonMarking());
			newCandidateVendorLocationItem.setAgentCommissionPercent(candidateVendorLocationItem.getAgentCommissionPercent());
			newCandidateVendorLocationItem.setDutyPercent(candidateVendorLocationItem.getDutyPercent());
			newCandidateVendorLocationItem.setDutyInfoText(candidateVendorLocationItem.getDutyInfoText());
			newCandidateVendorLocationItem.setProrationDate(candidateVendorLocationItem.getProrationDate());
			newCandidateVendorLocationItem.setInStoreDate(candidateVendorLocationItem.getInStoreDate());
			newCandidateVendorLocationItem.setWarehouseFlushDate(candidateVendorLocationItem.getWarehouseFlushDate());
			newCandidateVendorLocationItem.setDutyConfirmationText(candidateVendorLocationItem.getDutyConfirmationText());
			newCandidateVendorLocationItem.setFreightConfirmationText(candidateVendorLocationItem.getFreightConfirmationText());
			newCandidateVendorLocationItem.setContainerSizeCode(candidateVendorLocationItem.getContainerSizeCode());
			newCandidateVendorLocationItem.setCountryOfOriginId(candidateVendorLocationItem.getCountryOfOriginId());
			if(newCandidateVendorLocationItem.getSampProvideSwitch() == null || newCandidateVendorLocationItem.getSampProvideSwitch().length() == 0){
				newCandidateVendorLocationItem.setSampProvideSwitch(CandidateVendorLocationItem.DEFAULT_EMPTY_STRING);
			}
    		this.candidateVendorLocationItemRepository.save(newCandidateVendorLocationItem);
    	}
    }

    /**
     * Updates case pack import factories in candidate mode.
     *
     * @param candidateVendorLocationItem The ps vend loc item to be updated.
     * @param userId The user id.
     */
    @CoreTransactional
    public void updateCasePackImportFactoriesCandidate(CandidateVendorLocationItem candidateVendorLocationItem, String userId) {
    	CandidateVendorItemFactoryKey candidateVendorItemFactoryKeyKey = new CandidateVendorItemFactoryKey();
		candidateVendorItemFactoryKeyKey.setCandidateItemId(candidateVendorLocationItem.getKey().getCandidateItemId());
		candidateVendorItemFactoryKeyKey.setVendorNumber(candidateVendorLocationItem.getKey().getVendorNumber());
		candidateVendorItemFactoryKeyKey.setVendorType(candidateVendorLocationItem.getKey().getVendorType());
    	List<CandidateVendorItemFactory> oldList = this.candidateVendorItemFactoryXrfRepository.findByKeyCandidateItemIdAndKeyVendorNumberAndKeyVendorType(
				candidateVendorLocationItem.getKey().getCandidateItemId(), candidateVendorLocationItem.getKey().getVendorNumber(), candidateVendorLocationItem.getKey().getVendorType());
    	List<CandidateVendorItemFactory> newList = candidateVendorLocationItem.getCandidateVendorItemFactorys();
    	List<CandidateVendorItemFactory> removedFactories = new ArrayList<CandidateVendorItemFactory>();
    	List<CandidateVendorItemFactory> addedFactories = new ArrayList<CandidateVendorItemFactory>();
    	
    	//find factory ids in old list, but not new list;
		CandidateVendorItemFactory candidateVendorItemFactory;
		CandidateVendorItemFactoryKey candidateVendorItemFactoryKey;
    	boolean isNotFound;
    	for (CandidateVendorItemFactory oldFactory : oldList) {
    		isNotFound = true;
    		for (CandidateVendorItemFactory newFactory : newList) {
    			if (oldFactory.getKey().getFactoryId().equals(newFactory.getKey().getFactoryId())) {
    				isNotFound = false;
					break;
    			}
    		}
    		if (isNotFound) {
				removedFactories.add(oldFactory);
			}
    	}
    	//find factory ids in new list, but not old list
    	for (CandidateVendorItemFactory newFactory : newList) {
    		isNotFound = true;
    		for (CandidateVendorItemFactory oldFactory : oldList) {
    			if (oldFactory.getKey().getFactoryId().equals(newFactory.getKey().getFactoryId())) {
    				isNotFound = false;
    				break;
    			}
    		}
    		if (isNotFound) {
    			candidateVendorItemFactoryKey = new CandidateVendorItemFactoryKey();
    			candidateVendorItemFactoryKey.setCandidateItemId(candidateVendorLocationItem.getKey().getCandidateItemId());
    			candidateVendorItemFactoryKey.setVendorNumber(candidateVendorLocationItem.getKey().getVendorNumber());
    			candidateVendorItemFactoryKey.setVendorType(candidateVendorLocationItem.getKey().getVendorType());
    			candidateVendorItemFactoryKey.setFactoryId(newFactory.getKey().getFactoryId());
    			candidateVendorItemFactory = new CandidateVendorItemFactory();
    			candidateVendorItemFactory.setKey(candidateVendorItemFactoryKey);
				candidateVendorItemFactory.setFactory(new Factory());
				candidateVendorItemFactory.getFactory().setFactoryId(candidateVendorItemFactoryKey.getFactoryId());
    			candidateVendorItemFactory.setLastUpdateUserId(userId);
    			candidateVendorItemFactory.setLastUpdateTs(LocalDate.now());
    			addedFactories.add(candidateVendorItemFactory);
    		}
    	}
    	if(CollectionUtils.isNotEmpty(removedFactories)){
    		this.candidateVendorItemFactoryXrfRepository.delete(removedFactories);
    	}
    	if(CollectionUtils.isNotEmpty(addedFactories)){
    		this.candidateVendorItemFactoryXrfRepository.save(addedFactories);
    	}
    }

    /**
	 * Updates case pack vendor in candidate mode.
	 *
	 * @param candidateItemMaster The ps item master to be updated.
     * @param userId The user id.
     */
    @CoreTransactional
    public void updateCasePackVendorCandidate(CandidateItemMaster candidateItemMaster, String userId) {
    	CandidateVendorLocationItem candidateVendorLocationItem = candidateItemMaster.getCandidateVendorLocationItems().get(0);
    	Integer candidateItemId = candidateItemMaster.getCandidateItemId();
		CandidateItemMaster newCandidateItemMaster = this.candidateItemMasterRepository.getOne(candidateItemId);
    	if(null != newCandidateItemMaster){
			newCandidateItemMaster.setShipNestCube(candidateItemMaster.getShipNestCube());
			newCandidateItemMaster.setShipCube(candidateItemMaster.getShipCube());
			newCandidateItemMaster.setLength(candidateItemMaster.getLength());
			newCandidateItemMaster.setWeight(candidateItemMaster.getWeight());
			newCandidateItemMaster.setHeight(candidateItemMaster.getHeight());
			newCandidateItemMaster.setWidth(candidateItemMaster.getWidth());
			newCandidateItemMaster.setShipLength(candidateItemMaster.getShipLength());
			newCandidateItemMaster.setShipWeight(candidateItemMaster.getShipWeight());
			newCandidateItemMaster.setShipHeight(candidateItemMaster.getShipHeight());
			newCandidateItemMaster.setShipWidth(candidateItemMaster.getShipWidth());

    		this.candidateItemMasterRepository.save(newCandidateItemMaster);
    	}
		CandidateVendorLocationItemKey candidateVendorLocationItemKey = new CandidateVendorLocationItemKey();
		candidateVendorLocationItemKey.setCandidateItemId(candidateVendorLocationItem.getKey().getCandidateItemId());
		candidateVendorLocationItemKey.setVendorNumber(candidateVendorLocationItem.getKey().getVendorNumber());
		candidateVendorLocationItemKey.setVendorType(candidateVendorLocationItem.getKey().getVendorType());
		CandidateVendorLocationItem newCandidateVendorLocationItem = this.candidateVendorLocationItemRepository.getOne(candidateVendorLocationItemKey);
    	if(null != newCandidateVendorLocationItem){
			newCandidateVendorLocationItem.setVendItemId(candidateVendorLocationItem.getVendItemId());
			newCandidateVendorLocationItem.setPalletQuantity(candidateVendorLocationItem.getPalletQuantity());
			newCandidateVendorLocationItem.setOrderQuantityRestrictionCode(candidateVendorLocationItem.getOrderQuantityRestrictionCode());
			newCandidateVendorLocationItem.setCostLinkId(candidateVendorLocationItem.getCostLinkId());
			newCandidateVendorLocationItem.setTie(candidateVendorLocationItem.getTie());
			newCandidateVendorLocationItem.setTier(candidateVendorLocationItem.getTier());
			if(newCandidateVendorLocationItem.getSampProvideSwitch() == null || newCandidateVendorLocationItem.getSampProvideSwitch().length() == 0){
				newCandidateVendorLocationItem.setSampProvideSwitch(CandidateVendorLocationItem.DEFAULT_EMPTY_STRING);
			}
    		this.candidateVendorLocationItemRepository.save(newCandidateVendorLocationItem);
    	}
    }

    /**
     * Updates case pack warehouse in candidate mode.
     *
     * @param candidateItemMaster The ps item master to be updated.
     * @param userId The user id.
     */
    @CoreTransactional
    public void updateCasePackWarehouseCandidate(CandidateItemMaster candidateItemMaster, String userId) {
    	List<CandidateWarehouseLocationItem> candidateWarehouseLocationItems = candidateItemMaster.getCandidateWarehouseLocationItems();
    	Integer candidateItemId = candidateItemMaster.getCandidateItemId();
		CandidateItemMaster newCandidateItemMaster = this.candidateItemMasterRepository.getOne(candidateItemId);
    	if(null != newCandidateItemMaster){
			newCandidateItemMaster.setMaxShipQuantity(candidateItemMaster.getMaxShipQuantity());
			newCandidateItemMaster.setVariableWeight(candidateItemMaster.isVariableWeight());

    		this.candidateItemMasterRepository.save(newCandidateItemMaster);
    	}
    	List<CandidateWarehouseLocationItem> listCandidateWarehouseLocationItems = new ArrayList<CandidateWarehouseLocationItem>();
    	for (CandidateWarehouseLocationItem candidateWarehouseLocationItem : candidateWarehouseLocationItems) {
			CandidateWarehouseLocationItemKey candidateWarehouseLocationItemKey = new CandidateWarehouseLocationItemKey();
			candidateWarehouseLocationItemKey.setCandidateItemId(candidateWarehouseLocationItem.getKey().getCandidateItemId());
			candidateWarehouseLocationItemKey.setWarehouseNumber(candidateWarehouseLocationItem.getKey().getWarehouseNumber());
			candidateWarehouseLocationItemKey.setWarehouseType(candidateWarehouseLocationItem.getKey().getWarehouseType());
			CandidateWarehouseLocationItem newCandidateWarehouseLocationItem = this.candidateWarehouseLocationItemRepository.getOne(candidateWarehouseLocationItemKey);
        	if(null != newCandidateWarehouseLocationItem){
				newCandidateWarehouseLocationItem.setOrderQuantityTypeCode(candidateWarehouseLocationItem.getOrderQuantityTypeCode());
				newCandidateWarehouseLocationItem.setUnitFactor1(candidateWarehouseLocationItem.getUnitFactor2());
				newCandidateWarehouseLocationItem.setUnitFactor2(candidateWarehouseLocationItem.getUnitFactor2());
				newCandidateWarehouseLocationItem.setWhseTie(candidateWarehouseLocationItem.getWhseTie());
				newCandidateWarehouseLocationItem.setWhseTier(candidateWarehouseLocationItem.getWhseTie());
				newCandidateWarehouseLocationItem.setFlowTypeCode(candidateWarehouseLocationItem.getFlowTypeCode());
				newCandidateWarehouseLocationItem.setLastUpdateDt(LocalDate.now());
				newCandidateWarehouseLocationItem.setLastUpdatedOn(LocalDateTime.now());
				newCandidateWarehouseLocationItem.setLastUpdatedId(userId);
        		if(newCandidateWarehouseLocationItem.getMfgId() == null || newCandidateWarehouseLocationItem.getMfgId().length() == 0){
					newCandidateWarehouseLocationItem.setMfgId(CandidateWarehouseLocationItem.DEFAULT_EMPTY_STRING);
        		}
        		listCandidateWarehouseLocationItems.add(newCandidateWarehouseLocationItem);
        	}
		}
    	if(CollectionUtils.isNotEmpty(listCandidateWarehouseLocationItems)){
    		this.candidateWarehouseLocationItemRepository.save(listCandidateWarehouseLocationItems);
    	}
    }

    /**
     * Updates case pack warehouse comments in candidate mode.
     *
     * @param candidateWarehouseLocationItem The ps_whse_loc_itm to be updated.
     * @param userId The user id.
     */
    @CoreTransactional
    public void updateCasePackWarehouseCommentsCandidate(CandidateWarehouseLocationItem candidateWarehouseLocationItem, String userId) {
    	if(candidateWarehouseLocationItem.getComment() != null){
			CandidateWarehouseLocationItemKey candidateWarehouseLocationItemKey = new CandidateWarehouseLocationItemKey();
			candidateWarehouseLocationItemKey.setCandidateItemId(candidateWarehouseLocationItem.getKey().getCandidateItemId());
			candidateWarehouseLocationItemKey.setWarehouseNumber(candidateWarehouseLocationItem.getKey().getWarehouseNumber());
			candidateWarehouseLocationItemKey.setWarehouseType(candidateWarehouseLocationItem.getKey().getWarehouseType());
			CandidateWarehouseLocationItem newCandidateWarehouseLocationItem = this.candidateWarehouseLocationItemRepository.getOne(candidateWarehouseLocationItemKey);
    		if(null != newCandidateWarehouseLocationItem){
				newCandidateWarehouseLocationItem.setComment(candidateWarehouseLocationItem.getComment());
    			if(newCandidateWarehouseLocationItem.getMfgId() == null || newCandidateWarehouseLocationItem.getMfgId().length() == 0){
					newCandidateWarehouseLocationItem.setMfgId(CandidateWarehouseLocationItem.DEFAULT_EMPTY_STRING);
    			}
    			this.candidateWarehouseLocationItemRepository.save(newCandidateWarehouseLocationItem);
    		}
    	}
    	List<CandidateItemWarehouseComments> candidateItemWarehouseComments = candidateWarehouseLocationItem.getCandidateItemWarehouseComments();
    	if(CollectionUtils.isNotEmpty(candidateItemWarehouseComments)){
    		for (CandidateItemWarehouseComments candidateItemWarehouseComment : candidateItemWarehouseComments) {
				if(null == candidateItemWarehouseComment.getComments() 
						|| StringUtils.EMPTY.equalsIgnoreCase(candidateItemWarehouseComment.getComments())){
					candidateItemWarehouseComment.setComments(CandidateWarehouseLocationItem.DEFAULT_EMPTY_STRING);
				}
			}
    		this.candidateItemWarehouseCommentsRepository.save(candidateItemWarehouseComments);
    	}
    }

    /**
     * Call webservice to activate Candidate and update work status.
     *
     * @param psWorkId the Candidate work request id.
     */
    public void activateCandidate(Long psWorkId) {
        this.masterDataServiceClient.activateCandidate(psWorkId.intValue());
        this.updateWorkStatus(psWorkId, CasePackCandidateService.ACTIVATED_STATUS_CODE);
    }

    /**
     * Update Candidate work status by work request id.
     *
     * @param psWorkId   the Candidate work request id.
     * @param statusCode the status code to update.
     */
    private void updateWorkStatus(long psWorkId, String statusCode) {
    	CandidateWorkRequest candidateWorkRequest = this.candidateWorkRequestRepository.findOne(psWorkId);
    	if(candidateWorkRequest != null){
    		candidateWorkRequest.setStatus(statusCode);
    		candidateWorkRequest.setReadyToActivate(false);
    		this.candidateWorkRequestRepository.save(candidateWorkRequest);
    	}
    }

    /**
     * Reject Candidate by update work status and update PsCandidateStat table.
     *
     * @param psWorkId the Candidate work request id.
     * @param userId   the user id.
     */
    public void rejectCandidate(long psWorkId, String userId) {
    	this.updateWorkStatus(psWorkId, CasePackCandidateService.REJECTED_STATUS_CODE);
    	CandidateWorkRequest candidateWorkRequest = this.candidateWorkRequestRepository.findOne(psWorkId);
    	if(candidateWorkRequest != null){
    		candidateWorkRequest.setLastUpdateUserId(userId);
    		boolean isSameUserId = StringUtils.endsWithIgnoreCase(userId, candidateWorkRequest.getUserId());
    		candidateWorkRequest.setStatusChangeReason(isSameUserId ? CasePackCandidateService.STATUS_CHANGE_REASON_SAME_USERID : CasePackCandidateService.STATUS_CHANGE_REASON_DIFF_USERID);
    		CandidateStatus candidateStatus = this.convertCandidateWorkRequestToCandidateStatus(candidateWorkRequest);
    		this.candidateStatusRepository.save(candidateStatus);
    	}
    }
	/**
	 * Convert CandidateWorkRequest to PsCandidateStat.
	 *
	 * @param candidateWorkRequest the Candidate work request.
	 * @return the Ps candidate stat after convert.
	 */
	private CandidateStatus convertCandidateWorkRequestToCandidateStatus(CandidateWorkRequest candidateWorkRequest) {
		CandidateStatus candidateStatus = new CandidateStatus();
		CandidateStatusKey key = new CandidateStatusKey();
		key.setWorkRequestId(candidateWorkRequest.getWorkRequestId());
		key.setStatus(candidateWorkRequest.getStatus());
		key.setLastUpdateDate(LocalDateTime.now());
		candidateStatus.setKey(key);
		candidateStatus.setUpdateUserId(candidateWorkRequest.getUserId());
		candidateStatus.setStatusChangeReason(candidateWorkRequest.getStatusChangeReason());
		return candidateStatus;
	}
}
