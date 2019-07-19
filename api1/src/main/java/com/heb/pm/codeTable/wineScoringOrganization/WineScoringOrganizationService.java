/*
 *  WineScoringOrganizationService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.codeTable.wineScoringOrganization;

import com.heb.pm.entity.ScoringOrganization;
import com.heb.pm.repository.ScoringOrganizationRepository;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds all business logic related to Scoring OrganizationService.
 *
 * @author vn70529
 * @since 2.12.0
 */
@Service
public class WineScoringOrganizationService {

	@Autowired
	private ScoringOrganizationRepository repository;

	@Autowired
	private CodeTableManagementServiceClient serviceClient;

	/**
	 * Get all scoring organizations ordered by ScoringOrganization id.
	 *
	 * @return all scoring organizations ordered by ScoringOrganization id.
	 */
	public List<ScoringOrganization> findAllByOrderByScoringOrganizationIdAsc(){
		return this.repository.findAllByOrderByScoringOrganizationIdAsc();
	}

	/**
	 * Add new the list of scoringOrganizations.
	 *
	 * @param scoringOrganizations The list of scoringOrganizations to add new.
	 */
	public void addScoringOrganizations(List<ScoringOrganization> scoringOrganizations) {
		this.serviceClient.updateWineScoringOrganizations(scoringOrganizations, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
	}

	/**
	 * Update information for the list of scoringOrganizations.
	 *
	 * @param scoringOrganizations The list of scoringOrganizations to edit.
	 */
	public void updateScoringOrganizations(List<ScoringOrganization> scoringOrganizations) {
		this.serviceClient.updateWineScoringOrganizations(scoringOrganizations, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
	}

	/**
	 * Delete the list of scoringOrganizations.
	 *
	 * @param scoringOrganizations The list of scoringOrganizations to delete.
	 */
	public void deleteScoringOrganizations(List<ScoringOrganization> scoringOrganizations) {
		this.serviceClient.updateWineScoringOrganizations(scoringOrganizations, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
	}

}
