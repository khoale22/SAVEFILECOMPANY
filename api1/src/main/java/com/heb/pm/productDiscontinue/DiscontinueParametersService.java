/*
 *
 *  DiscontinueExceptionService
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.*;
import com.heb.pm.repository.DiscontinueParametersAuditRepository;
import com.heb.pm.repository.DiscontinueParametersRepository;
import com.heb.util.controller.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to default product discontinue parameters.
 *
 * @author s573181
 * @since 2.0.2
 */
@Service
public class DiscontinueParametersService {

	@Autowired
	private DiscontinueParametersRepository discontinueParametersRepository;

	@Autowired
	private DiscontinueParametersAuditRepository discontinueParametersAuditRepository;

	@Autowired
	private DiscontinueParametersToDiscontinueRules converter;

	@Autowired
	private UserInfo userInfo;

	/**
	 * String action code used in the Database.
	 */
	private static final String MODIFY_ACTION_CODE = "MOD";

	/**
	 * Returns a DiscontinueRules containing the default product discontinue rules.
	 *
	 * @return A DiscontinueRules containing the default product discontinue rules..
	 */
	public DiscontinueRules findAll() {

		return this.converter.toDiscontinueRules(this.discontinueParametersRepository.findAll());
	}

	/**
	 *  Updates default product discontinue rules.
	 *
	 * @param newParameters The updated product discontinue rules.
	 */
	@CoreTransactional
	public DiscontinueRules update(DiscontinueRules newParameters) {

		// Saves the updated rules, converts the returned result into a DiscontinueRules object, sends the information
		// to the audit table, then returns the saved DiscontinueRules.
		List<DiscontinueParameters> parametersList = this.discontinueParametersRepository.save(this.getUpdateChanges(
				discontinueParametersRepository.findAll(), this.converter.toDiscontinueParameters(newParameters)));
		this.createParametersAudits(parametersList, MODIFY_ACTION_CODE);
		return newParameters;
	}

	/**
	 * Returns a list of changes made comparing object send from front end, and object acquired from database.
	 *
	 * @param existingRulesList The excpetion rules that exist in the database.
	 * @param newRulesList The new exception rules sent from front end.
	 *
	 * @return A List of exceptions that are the changes made by a user.
	 */
	private List<DiscontinueParameters> getUpdateChanges(
			List<DiscontinueParameters> existingRulesList, List<DiscontinueParameters> newRulesList) {
		List<DiscontinueParameters> changeList = new ArrayList<>();
		boolean foundExistingException;

		for(DiscontinueParameters newRule : newRulesList){
			foundExistingException = false;
			for(DiscontinueParameters existingRule : existingRulesList){

				// if the key in newRule matches key in existingRule
				if(newRule.equals(existingRule)){
					foundExistingException = true;

					// if value in newRule does not match value in existingRule
					if(!newRule.fullItemCompare(existingRule)){
						changeList.add(newRule);
					}
					break;
				}
			}

			// if the record does not exist in the database
			if(!foundExistingException) {
				changeList.add(newRule);
			}
		}

		return changeList;
	}


	/**
	 *  Extracts the audit information from the DiscontinueParameters entities and saves them to the audit table.
	 * @param discontinueParametersList The list of exception parameters to be saved to the audit table.
	 * @param actionCode The action code reflecting the change made to exception parameters (ADD/MOD/DEL).
	 */
	private void createParametersAudits(List<DiscontinueParameters> discontinueParametersList, String actionCode) {
		List<DiscontinueParametersAudit> parametersAuditList = new ArrayList<>();
		DiscontinueParametersAudit parametersAudit = null;
		LocalDateTime timestamp = null;
		String parameterName = null;
		for(int x = 0; x<discontinueParametersList.size(); x++){

			parameterName = DiscontinueParameterType.getTypeById(discontinueParametersList.get(x).getKey().getId()).getParameterName();
			timestamp = LocalDateTime.now();
			parametersAudit = new DiscontinueParametersAudit();
			parametersAudit.setParameterName(parameterName);
			parametersAudit.setId(discontinueParametersList.get(x).getKey().getId());
			parametersAudit.setSequenceNumber(discontinueParametersList.get(x).getKey().getSequenceNumber());
			parametersAudit.setParameterValue(discontinueParametersList.get(x).getParameterValue());
			parametersAudit.setActive(discontinueParametersList.get(x).isActive());
			parametersAudit.setAction(actionCode);
			parametersAudit.setUserId(userInfo.getUserId());
			parametersAudit.setTimestamp(timestamp);
			parametersAuditList.add(parametersAudit);
		}
		this.discontinueParametersAuditRepository.save(parametersAuditList);
	}
}
