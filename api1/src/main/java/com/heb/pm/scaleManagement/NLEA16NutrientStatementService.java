/*
 *
 * NLEA16NutrientStatementService.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.scaleManagement;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related for NLEA nutrient statements.
 * @author vn70633
 * @since 2.20.0
 */
@Service
public class NLEA16NutrientStatementService {

    private static final long SRC_SYSTEM_ID_SCALE = 17;
	private static final Integer PANEL_NUMBER_DEFAULT_ADD = 1;
	private static final String PANEL_TYPE_CODE_DEFAULT_ADD = "N2016";
	private static final char PUBED_SWITCH_DEFAULT_ADD = 'N';
	private static final char LESS_THAN_SWITCH_DEFAULT_ADD = 'N';
    private static final Long PANEL_HEADER_ID_ONE = 1L;

    @Autowired
    private NutrientStatementPanelHeaderRepository nutrientStatementPanelHeaderRepository;

    @Autowired
    private NutrientStatementPanelHeaderRepositoryWithCount nutrientStatementHeaderRepositoryWithCount;

    @Autowired
	private NutrientPanelColumnHeaderRepository nutrientPanelColumnHeaderRepository;

    @Autowired
	private NutrientPanelDetailRepository nutrientPanelDetailRepository;

	@Autowired
	private NLEA16NutrientRepository nlea16NutrientRepository;

	/**
	 * Find nutrient statement panel header by source system reference id.
	 *
	 * @param sourceSystemReferenceId the source system reference id.
	 * @return the NutrientStatementPanelHeader
	 */
	public NutrientStatementPanelHeader getNutrientStatementPanelHeaderBySourceSystemReferenceId(String sourceSystemReferenceId){
		return this.nutrientStatementPanelHeaderRepository.findFirstBySourceSystemReferenceIdAndSourceSystemIdAndStatementMaintenanceSwitch(sourceSystemReferenceId, SRC_SYSTEM_ID_SCALE, NutrientStatementPanelHeader.ACTIVE_SW_Y);
	}

    /**
     * Find nutrient statement by statement id pageable result.
     *
     * @param statementIds the statement id
     * @param ic          the ic
     * @param ps          the ps
     * @param pg          the pg
     * @return the pageable result
     */
    public PageableResult<NutrientStatementPanelHeader> findNutrientStatementByStatementId(List<String> statementIds, boolean ic, int ps, int pg) {
        List<String> statIds = new ArrayList<String>();
        for(int i=0; i<statementIds.size(); i++){
            statIds.add(String.valueOf(statementIds.get(i)));
        }
        Pageable pageRequest = new PageRequest(pg, ps);
        return ic ? this.findNutrientStatementByStatementIdWithCount(
                statIds, pageRequest) :
                this.findNutrientStatementByStatementIdWithoutCount(statIds, pageRequest);
    }

    /**
     * Find nutrient statement information by id.
     *
     * @param statementIds id that will search by.
     * @param pageRequest the page request
     * @return the pageable result

     */
    private PageableResult<NutrientStatementPanelHeader> findNutrientStatementByStatementIdWithCount(
            List<String> statementIds, Pageable pageRequest) {
        Page<NutrientStatementPanelHeader> data = this.nutrientStatementHeaderRepositoryWithCount.
                findBySourceSystemReferenceIdInAndSourceSystemIdAndStatementMaintenanceSwitch(statementIds, SRC_SYSTEM_ID_SCALE, NutrientStatementPanelHeader.ACTIVE_SW_Y, pageRequest);
        return new PageableResult<>(pageRequest.getPageNumber(),
                data.getTotalPages(),
                data.getTotalElements(),
                data.getContent());
    }

    /**
     * Find nutrient statement information by id.
     *
     * @param statementIds id to search by
     * @param pageRequest the page request
     * @return the pageable result
     */
    private PageableResult<NutrientStatementPanelHeader> findNutrientStatementByStatementIdWithoutCount(
            List<String> statementIds, Pageable pageRequest) {
        List<NutrientStatementPanelHeader> data = this.nutrientStatementPanelHeaderRepository.
                findBySourceSystemReferenceIdInAndSourceSystemIdAndStatementMaintenanceSwitch(statementIds, SRC_SYSTEM_ID_SCALE, NutrientStatementPanelHeader.ACTIVE_SW_Y, pageRequest);
        return new PageableResult<>(pageRequest.getPageNumber(), data);
    }

    /**
     * Find all statement id pageable result.
     *
     * @param includeCount the include count
     * @param page         the page
     * @return the pageable result
     */
    public PageableResult<NutrientStatementPanelHeader> findAllNutrientStatements(boolean includeCount, int page, int pageSize) {
        Pageable pageRequest = new PageRequest(page, pageSize);
        return includeCount ? this.findAllStatementIdWithCount(pageRequest) :
                this.findAllStatementIdWithoutCount(pageRequest);
    }

    /**
     * Find all nutrient statement information based on statement id with count.
     *
	 * @param pageRequest the page request
     * @return the pagable result
     */
    private PageableResult<NutrientStatementPanelHeader> findAllStatementIdWithCount(Pageable pageRequest) {
        Page<NutrientStatementPanelHeader> data = this.nutrientStatementHeaderRepositoryWithCount.findBySourceSystemIdAndStatementMaintenanceSwitch(SRC_SYSTEM_ID_SCALE, NutrientStatementPanelHeader.ACTIVE_SW_Y, pageRequest);
        return new PageableResult<>(pageRequest.getPageNumber(), data.getTotalPages(), data.getTotalElements(),
                data.getContent());
    }

    /**
     * Find all nutrient Statement details via statement id without count.
     *
	 * @param pageRequest the page request
     * @return the pageable result
     */
    private PageableResult<NutrientStatementPanelHeader> findAllStatementIdWithoutCount(Pageable pageRequest) {
        List<NutrientStatementPanelHeader> data =
                this.nutrientStatementPanelHeaderRepository.findBySourceSystemIdAndStatementMaintenanceSwitch(SRC_SYSTEM_ID_SCALE, NutrientStatementPanelHeader.ACTIVE_SW_Y, pageRequest);
        return new PageableResult<>(pageRequest.getPageNumber(), data);
    }

	/**
	 * Update nutrient statement.
	 *
	 * @param nutrientStatementHeader The nutrient statement header to add.
	 * @return The nutrient statement header after processing.
	 * @throws IllegalArgumentException Any exception in validating the nutrient statement before save.
	 */
	@CoreTransactional
	public NutrientStatementPanelHeader updateNutrientStatement(NutrientStatementPanelHeader nutrientStatementHeader)
			throws IllegalArgumentException {
		NutrientStatementPanelHeader newNutrientStatementPanelHeader = new NutrientStatementPanelHeader();

		NutrientStatementPanelHeader existNutrientStatementPanelHeader =
				this.nutrientStatementPanelHeaderRepository.findOne(nutrientStatementHeader.getNutrientPanelHeaderId());
		if (existNutrientStatementPanelHeader != null) {
			//When saving, if there is an existing panel, pull it from the DB and set its active switch to N.
			existNutrientStatementPanelHeader.setStatementMaintenanceSwitch(NutrientStatementPanelHeader.ACTIVE_SW_N);
			//Save old panel
			this.nutrientStatementPanelHeaderRepository.save(existNutrientStatementPanelHeader);

			BeanUtils.copyProperties(nutrientStatementHeader, newNutrientStatementPanelHeader);

			//Effective date is uneditable. Once the record is saved, it is set to today.
			newNutrientStatementPanelHeader.setEffectiveDate(LocalDate.now());
			//Save the new panel with an active switch of Y.
			newNutrientStatementPanelHeader.setStatementMaintenanceSwitch(NutrientStatementPanelHeader.ACTIVE_SW_Y);
			newNutrientStatementPanelHeader.setNutrientPanelColumnHeaders(null);
			newNutrientStatementPanelHeader.setNutrientPanelHeaderId(nutrientStatementPanelHeaderRepository.getLastestId() + 1);

			//Save new panel
			this.nutrientStatementPanelHeaderRepository.save(newNutrientStatementPanelHeader);

			List<NutrientPanelColumnHeader> newNutrientPanelColumnHeaders = new ArrayList<>();
			List<NutrientPanelDetail> newNutrientPanelDetails = new ArrayList<>();
			List<NutrientPanelColumnHeader> nutrientPanelColumnHeaders = nutrientStatementHeader.getNutrientPanelColumnHeaders();
			if(CollectionUtils.isNotEmpty(nutrientPanelColumnHeaders)){
				for (NutrientPanelColumnHeader nutrientPanelColumnHeader : nutrientPanelColumnHeaders) {
					NutrientPanelColumnHeader newNutrientPanelColumnHeader = new NutrientPanelColumnHeader();
					newNutrientPanelColumnHeader.setKey(nutrientPanelColumnHeader.getKey());
					newNutrientPanelColumnHeader.getKey().setNutrientPanelHeaderId(newNutrientStatementPanelHeader.getNutrientPanelHeaderId());
					newNutrientPanelColumnHeader.setCaloriesQuantity(nutrientPanelColumnHeader.getCaloriesQuantity());
					newNutrientPanelColumnHeader.setServingSizeLabel(nutrientPanelColumnHeader.getServingSizeLabel());
					newNutrientPanelColumnHeaders.add(newNutrientPanelColumnHeader);
					
					List<NutrientPanelDetail> nutrientPanelDetails = nutrientPanelColumnHeader.getNutrientPanelDetails();
					if(CollectionUtils.isNotEmpty(nutrientPanelDetails)){
						for (NutrientPanelDetail nutrientPanelDetail : nutrientPanelDetails) {
							nutrientPanelDetail.getKey().setNutrientStatementId(newNutrientStatementPanelHeader.getNutrientPanelHeaderId());
							newNutrientPanelDetails.add(nutrientPanelDetail);
						}
					}
				}
				this.nutrientPanelColumnHeaderRepository.save(newNutrientPanelColumnHeaders);
				if(CollectionUtils.isNotEmpty(newNutrientPanelDetails)){
					this.nutrientPanelDetailRepository.save(newNutrientPanelDetails);
				}
			}
			
			//Duplicate data when 2 users save at the same time => remove once
			List<NutrientStatementPanelHeader> listNutrientStatementPanelHeaders = 
					this.nutrientStatementPanelHeaderRepository.findAllBySourceSystemReferenceIdAndSourceSystemIdAndStatementMaintenanceSwitch(
					nutrientStatementHeader.getSourceSystemReferenceId(), nutrientStatementHeader.getSourceSystemId(), NutrientStatementPanelHeader.ACTIVE_SW_Y);
			if(CollectionUtils.isNotEmpty(listNutrientStatementPanelHeaders)){
				for (NutrientStatementPanelHeader nutrientStatementPanelHeader : listNutrientStatementPanelHeaders) {
					if(nutrientStatementPanelHeader.getNutrientPanelHeaderId() != newNutrientStatementPanelHeader.getNutrientPanelHeaderId()){
						nutrientStatementPanelHeader.setStatementMaintenanceSwitch(NutrientStatementPanelHeader.ACTIVE_SW_N);
					}
				}
				this.nutrientStatementPanelHeaderRepository.save(listNutrientStatementPanelHeaders);
			}
		}

		return nutrientStatementHeader;
	}

	/**
	 * Check the nutrient statement number is exists.
	 *
	 * @param nutrientStatementNumber the nutrient statement number to be adding.
	 * @return String that determines whether it is available or not.
	 */
	public boolean isNutrientStatementExists(String nutrientStatementNumber) {
		NutrientStatementPanelHeader nutrientStatementPanelHeader = null;
		if(nutrientStatementNumber != null) {
			nutrientStatementPanelHeader =
					this.nutrientStatementPanelHeaderRepository.findFirstBySourceSystemReferenceIdAndSourceSystemIdAndStatementMaintenanceSwitch(nutrientStatementNumber, SRC_SYSTEM_ID_SCALE,NutrientStatementPanelHeader.ACTIVE_SW_Y);
		}
		return nutrientStatementPanelHeader == null;
	}

	/**
	 * Finds Mandated NLEA16Nutrients.
	 *
	 * @return The mandated nutrients for the statement id.
	 */
	public List<NutrientPanelDetail> findMandatedNLEA16Nutrients() {
		List<NutrientPanelDetail> nutrientPanelDetails = new ArrayList<>();
		List<NLEA16Nutrient> nlea16Nutrients = this.nlea16NutrientRepository.findAll(NLEA16Nutrient.getDefaultSort());
		NutrientPanelDetail detail;
		for (NLEA16Nutrient nlea16Nutrient : nlea16Nutrients) {
			detail = new NutrientPanelDetail();
			detail.setKey(new NutrientPanelDetailKey());
			detail.getKey().setNutrientId(nlea16Nutrient.getNutrientId());
			detail.setNutrient(nlea16Nutrient);
			nutrientPanelDetails.add(detail);
		}
		return nutrientPanelDetails;
	}

	/**
	 * Add a new nutrient statement.
	 *
	 * @param nutrientStatementPanelHeaderAdd The nutrient statement panel header to add.
	 * @return The nutrient statement panel header after processing.
	 * @throws IllegalArgumentException Any exception in validating the nutrient statement before save.
	 */
	@CoreTransactional
	public NutrientStatementPanelHeader addNutrientStatement(NutrientStatementPanelHeader nutrientStatementPanelHeaderAdd)
			throws IllegalArgumentException {
		NutrientStatementPanelHeader nutrientStatementPanelHeader =
				this.nutrientStatementPanelHeaderRepository.findFirstBySourceSystemReferenceIdAndSourceSystemIdAndStatementMaintenanceSwitch(nutrientStatementPanelHeaderAdd.getSourceSystemReferenceId(), SRC_SYSTEM_ID_SCALE,NutrientStatementPanelHeader.ACTIVE_SW_Y);
		if (nutrientStatementPanelHeader == null) {
			//Add new nutrient statement panel header.
			NutrientStatementPanelHeader newNutrientStatementPanelHeader = this.saveNutrientStatementPanelHeader(nutrientStatementPanelHeaderAdd);
			List<NutrientPanelColumnHeader> nutrientPanelColumnHeaders = nutrientStatementPanelHeaderAdd.getNutrientPanelColumnHeaders();
			if(CollectionUtils.isNotEmpty(nutrientPanelColumnHeaders)){
                long nutrientPanelColumnId = 0;
				for (NutrientPanelColumnHeader nutrientPanelColumnHeader : nutrientPanelColumnHeaders) {
                    nutrientPanelColumnId = nutrientPanelColumnId + 1;
					//Add new nutrient panel column header for nutrient statement panel header.
					NutrientPanelColumnHeader newNutrientPanelColumnHeader = this.saveNutrientPanelColumnHeader(newNutrientStatementPanelHeader.getNutrientPanelHeaderId(), nutrientPanelColumnId, nutrientPanelColumnHeader);
					//Add new nutrient panel detail nutrient panel column header.
					if(newNutrientPanelColumnHeader != null ){
						this.saveNutrientPanelDetails(newNutrientPanelColumnHeader.getKey().getNutrientPanelHeaderId(),
								newNutrientPanelColumnHeader.getKey().getNutrientPanelColumnId(), nutrientPanelColumnHeader.getNutrientPanelDetails());
					}
				}
			}
			nutrientStatementPanelHeader = this.nutrientStatementPanelHeaderRepository.findOne(newNutrientStatementPanelHeader.getNutrientPanelHeaderId());
		}
		return nutrientStatementPanelHeader;
	}

	/**
	 * Add a new nutrient statement panel header.
	 *
	 * @param nutrientStatementPanelHeader the nutrient statement panel header.
	 * @return NutrientStatementPanelHeader
	 */
	private NutrientStatementPanelHeader saveNutrientStatementPanelHeader(NutrientStatementPanelHeader nutrientStatementPanelHeader){
		NutrientStatementPanelHeader newNutrientStatementPanelHeader = new NutrientStatementPanelHeader();
		BeanUtils.copyProperties(nutrientStatementPanelHeader, newNutrientStatementPanelHeader);
		//Set data for nutrient statement panel header
		newNutrientStatementPanelHeader.setNutrientPanelHeaderId(nutrientStatementPanelHeaderRepository.getLastestId()==null?PANEL_HEADER_ID_ONE:nutrientStatementPanelHeaderRepository.getLastestId()+1);
		newNutrientStatementPanelHeader.setImperialServingSizeUomId(nutrientStatementPanelHeader.getNutrientImperialUom().getUomCode());
		newNutrientStatementPanelHeader.setMetricServingSizeUomId(nutrientStatementPanelHeader.getNutrientMetricUom().getUomCode());		//Save new panel
		newNutrientStatementPanelHeader.setStatementMaintenanceSwitch(NutrientStatementPanelHeader.ACTIVE_SW_Y);
		newNutrientStatementPanelHeader.setSourceSystemId(NutrientStatementPanelHeader.SRC_SYSTEM_ID_17);
		newNutrientStatementPanelHeader.setPanelNumber(PANEL_NUMBER_DEFAULT_ADD);
		newNutrientStatementPanelHeader.setPanelTypeCode(PANEL_TYPE_CODE_DEFAULT_ADD);
		newNutrientStatementPanelHeader.setPubedSwitch(PUBED_SWITCH_DEFAULT_ADD);
		newNutrientStatementPanelHeader.setEffectiveDate(LocalDate.now());
		newNutrientStatementPanelHeader.setNutrientPanelColumnHeaders(null);
		return this.nutrientStatementPanelHeaderRepository.save(newNutrientStatementPanelHeader);
	}

	/**
	 * Add a new nutrient panel column header for nutrient statement panel header.
	 *
	 * @param nutrientPanelHeaderId the id of nutrient statement panel header.
     * @param nutrientPanelColumnId the id of nutrient panel column header.
	 * @param nutrientPanelColumnHeader the NutrientPanelColumnHeader to add.
	 * @return NutrientPanelColumnHeader
	 */
    private NutrientPanelColumnHeader saveNutrientPanelColumnHeader(Long nutrientPanelHeaderId, Long nutrientPanelColumnId, NutrientPanelColumnHeader nutrientPanelColumnHeader) {
		NutrientPanelColumnHeader newNutrientPanelColumnHeader = new NutrientPanelColumnHeader();
		// Set data for add new NutrientPanelColumnHeader
		NutrientPanelColumnHeaderKey nutrientPanelColumnHeaderKey = new NutrientPanelColumnHeaderKey();
		nutrientPanelColumnHeaderKey.setNutrientPanelHeaderId(nutrientPanelHeaderId);
		nutrientPanelColumnHeaderKey.setNutrientPanelColumnId(nutrientPanelColumnId);
		newNutrientPanelColumnHeader.setKey(nutrientPanelColumnHeaderKey);
		newNutrientPanelColumnHeader.setCaloriesQuantity(nutrientPanelColumnHeader.getCaloriesQuantity());
		newNutrientPanelColumnHeader.setServingSizeLabel(nutrientPanelColumnHeader.getServingSizeLabel());
		return this.nutrientPanelColumnHeaderRepository.save(newNutrientPanelColumnHeader);

	}

	/**
	 * Add list nutrient panel detail for nutrient panel column.
	 *
	 * @param nutrientPanelHeaderId the id of nutrient statement panel header
	 * @param nutrientPanelColumnId the id of nutrient panel column header
	 * @param nutrientPanelDetails
	 */
	private void saveNutrientPanelDetails(Long nutrientPanelHeaderId, Long nutrientPanelColumnId,List<NutrientPanelDetail> nutrientPanelDetails) {
		List<NutrientPanelDetail> newNutrientPanelDetails = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(nutrientPanelDetails)){
			for (NutrientPanelDetail nutrientPanelDetail : nutrientPanelDetails) {
				//Set data for add new NutrientPanelDetail
				NutrientPanelDetailKey nutrientPanelDetailKey = new NutrientPanelDetailKey();
				nutrientPanelDetailKey.setNutrientStatementId(nutrientPanelHeaderId);
				nutrientPanelDetailKey.setNutrientPanelColumnId(nutrientPanelColumnId);
				nutrientPanelDetailKey.setNutrientId(nutrientPanelDetail.getNutrient().getNutrientId());
				nutrientPanelDetail.setKey(nutrientPanelDetailKey);
				nutrientPanelDetail.setLessThanSwitch(LESS_THAN_SWITCH_DEFAULT_ADD);
				newNutrientPanelDetails.add(nutrientPanelDetail);
			}
		}
		if(CollectionUtils.isNotEmpty(newNutrientPanelDetails)){
			this.nutrientPanelDetailRepository.save(newNutrientPanelDetails);
		}
	}

	/**
	 * Delete nutrient statement panel header.
	 *
	 * @param nutrientPanelHeaderId the id of nutrient statement panel header
	 * @throws IllegalArgumentException
	 */
	public void deleteNutrientStatement(Long nutrientPanelHeaderId)
			throws IllegalArgumentException {
		NutrientStatementPanelHeader nutrientStatementPanelHeader =
				this.nutrientStatementPanelHeaderRepository.findOne(nutrientPanelHeaderId);
		nutrientStatementPanelHeader.setStatementMaintenanceSwitch(NutrientStatementPanelHeader.ACTIVE_SW_N);
		this.nutrientStatementPanelHeaderRepository.save(nutrientStatementPanelHeader);
	}

}
