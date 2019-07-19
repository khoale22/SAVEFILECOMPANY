package com.heb.pm.productDiscontinue;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.DiscontinueExceptionParameters;
import com.heb.pm.entity.DiscontinueExceptionParametersAudit;
import com.heb.pm.entity.ProductDiscontinueExceptionType;
import com.heb.pm.product.UpcService;
import com.heb.pm.repository.DiscontinueExceptionParametersAuditRepository;
import com.heb.pm.repository.DiscontinueExceptionParametersRepository;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to exceptions to the default product discontinue parameters.
 *
 * @author s573181
 * @since 2.0.2
 */
@Service
public class DiscontinueExceptionParametersService {

	private static final Logger logger = LoggerFactory.getLogger(DiscontinueExceptionParametersService.class);

	@Autowired
	private DiscontinueExceptionParametersRepository discontinueExceptionParametersRepository;

	@Autowired
	DiscontinueExceptionParametersAuditRepository discontinueExceptionParametersAuditRepository;

	@Autowired
	private DiscontinueParametersToDiscontinueRules converter;

	@Autowired
	private UpcService upcService;

	@Autowired
	private UserInfo userInfo;

	/**
	 * String action codes used in the Database.
	 */
	private static final String DELETE_ACTION_CODE = "DEL";
	private static final String ADD_ACTION_CODE = "ADD";
	private static final String MODIFY_ACTION_CODE = "MOD";

	/**
	 * Returns a list of all product discontinue exception rules.
	 *
	 * @return A list of all product discontinue exception rules.
	 */
	public List<DiscontinueRules> findAll() {

		return this.toDiscontinueRules(
				this.discontinueExceptionParametersRepository.findAll(DiscontinueExceptionParameters.getDefaultSort()));
	}

	/**
	 * Returns a list of DiscontinueRules for a particular exception type (Vendor, UPC, etc.).
	 *
	 * @param exceptionType The type of exception rules to look for.
	 * @return A list of exceptions to the default discontinue rules.
	 */
	public List<DiscontinueRules> findByExceptionType(ProductDiscontinueExceptionType exceptionType) {

		return this.toDiscontinueRules(
				this.discontinueExceptionParametersRepository.findByExceptionType(exceptionType.getType(),
						DiscontinueExceptionParameters.getDefaultSort()));
	}

	/**
	 * Updates an existing set of discontinue exception rules.
	 *
	 * @param updatedRule The updated discontinue exception rules.
	 * @return The updated discontinue rules.
	 */
	@CoreTransactional
	public DiscontinueRules update(DiscontinueRules updatedRule) {

		List<DiscontinueExceptionParameters> existingRules =
				this.findByExceptionTypeAndId(updatedRule.getExceptionType(), updatedRule.getExceptionTypeId());
		List<DiscontinueExceptionParameters> rules = this.converter.toDiscontinueExceptionParameters(updatedRule);

		rules = this.getUpdateChanges(existingRules, rules);

		rules.forEach((c) -> DiscontinueExceptionParametersService.logger.debug(c.toString()));

		// Saves the rules and updates the audit change to reflect the changes.
		createExceptionParametersAudits(this.discontinueExceptionParametersRepository.save(rules), MODIFY_ACTION_CODE);

		return updatedRule;
	}

	/**
	 * Adds a new set of discontinue exception rules.
	 *
	 * @param newRules The new discontinue exception rules.
	 * @return The discontinue rules that were added.
	 */
	@CoreTransactional
	public DiscontinueRules add(DiscontinueRules newRules) {

		// if a record already exists that shares same exception type and id, the user should not be able to add exception
		if(!this.toDiscontinueRules(this.findByExceptionTypeAndId(newRules.getExceptionType(),
				newRules.getExceptionTypeId())).isEmpty()){
			throw new IllegalArgumentException("Record already exists. Cannot create more than one record of same type " +
					"and ID.");
		}

		// if this is an SBT exception, the user should not be able to add an exception if one already exists
		else if(newRules.getExceptionType().equalsIgnoreCase(ProductDiscontinueExceptionType.SBT.getType())){
			if(this.findByExceptionType(ProductDiscontinueExceptionType.SBT).size() > 0){
				throw new IllegalArgumentException("Only one SBT exception can exist.");
			}
		}

		// else this is a UPC exception, the UPC should be a valid one
		else if(newRules.getExceptionType().equalsIgnoreCase(ProductDiscontinueExceptionType.UPC.getType())){
			if(this.upcService.find(Long.parseLong(newRules.getExceptionTypeId())) == null){
				throw new IllegalArgumentException("UPC " + newRules.getExceptionTypeId() + " not found.");
			}
		}

		// For a new rule, it won't have a sequence number, so grab the highest number and add one.
		int maxExceptionNumber = this.discontinueExceptionParametersRepository.getMaxExceptionSequenceNumber();
		newRules.setExceptionNumber(maxExceptionNumber + 1);

		// The front end doesn't know the correct priority, so set it before the object is converted.
		newRules.setPriorityNumber(
				ProductDiscontinueExceptionType.fromString(newRules.getExceptionType()).getPriority());

		List<DiscontinueExceptionParameters> rules = this.converter.toDiscontinueExceptionParameters(newRules);
		List<DiscontinueExceptionParameters> persistedRules = this.discontinueExceptionParametersRepository.save(rules);

		//Update the audit tables to reflect the changes made during the save.
		createExceptionParametersAudits(persistedRules, ADD_ACTION_CODE);
		return newRules;
	}

	/**
	 * Deletes all the exceptions tied to a supplied exception number and updates the audit list.
	 *
	 * @param exceptionNumber The exception sequence number to delete.
	 */
	@CoreTransactional
	public void delete(int exceptionNumber) {

		DiscontinueExceptionParametersService.logger.debug(Integer.toString(exceptionNumber));
		List<DiscontinueExceptionParameters> discontinueExceptionParametersList =
				this.discontinueExceptionParametersRepository.findByExceptionNumber(exceptionNumber);
		this.discontinueExceptionParametersRepository.deleteByExceptionNumber(exceptionNumber);
		this.createExceptionParametersAudits(discontinueExceptionParametersList, DELETE_ACTION_CODE);
	}

	/**
	 * Takes a list of normalized DiscontinueExceptionParameters that will include multiple actual rule sets
	 * and converts them to a list of DiscontinueRules.
	 *
	 * @param params A list of multiple sets of product discontinue exception rules.
	 * @return The rule sets de-normalized into a list of DiscontinueRules.
	 */
	private List<DiscontinueRules> toDiscontinueRules(List<DiscontinueExceptionParameters> params) {

		List<DiscontinueRules> returnList = new ArrayList<>();

		if (params.isEmpty()) {
			return returnList;
		}

		int startRow = 0;
		int endRow = 0;

		// Get the ID of the first record, this will prime the value to look for the first new exception.
		int currentExceptionId = params.get(0).getKey().getExceptionNumber();

		// Loop through all the exception parameters.
		for (DiscontinueExceptionParameters param : params) {

			// Once we've read to the next parameter, we have all the records that make up a given rule.
			if (param.getKey().getExceptionNumber() != currentExceptionId) {

				// Strip out the exceptions that make up the rule.
				List<DiscontinueExceptionParameters> ruleSet = params.subList(startRow, endRow);
				// Use the converter to turn them into a DiscontinueRules.
				DiscontinueRules rules = this.converter.toDiscontinueRulesFromExceptions(ruleSet);
				returnList.add(rules);

				// Now, the next set of rules will start on this record.
				startRow = endRow;
				// Save the ID that I'm now looking for.
				currentExceptionId = param.getKey().getExceptionNumber();
			}
			endRow++;
		}

		// Once I've left the loop, then I still have one set of rules I need to convert and add to the list.
		List<DiscontinueExceptionParameters> ruleSet = params.subList(startRow, endRow);
		DiscontinueRules rules = this.converter.toDiscontinueRulesFromExceptions(ruleSet);
		returnList.add(rules);

		return returnList;
	}

	/**
	 * Sets the DiscontinueExceptionParametersRepository. This method is used to set mock data for testing.
	 *
	 * @param repository The DiscontinueExceptionParametersRepository.
	 */
	public void setRepository(DiscontinueExceptionParametersRepository repository) {
		this.discontinueExceptionParametersRepository = repository;
	}

	/**
	 *  Sets the DiscontinueExceptionParametersAuditRepository. This method is used to set mock data for testing.
	 *
	 * @param auditRepository The discontinueExceptionParametersAuditRepository.
	 */
	public void setAuditRepository(DiscontinueExceptionParametersAuditRepository auditRepository) {
		this.discontinueExceptionParametersAuditRepository = auditRepository;
	}

	/**
	 * Sets the DiscontinueParametersToDiscontinueRules converter. This method is used to set mock data for testing.
	 *
	 * @param converter The DiscontinueParametersToDiscontinueRules converter.
	 */
	public void setConverter(DiscontinueParametersToDiscontinueRules converter) {
		this.converter = converter;
	}

	/**
	 * Sets the user info for this class. This method is used to set mock data for testing.
	 *
	 * @param userInfo The user info for this class.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Returns a list of DiscontinueRules for a particular exception type (Vendor, UPC, etc.).
	 *
	 * @param exceptionType The type of exception rules to look for.
	 * @param exceptionTypeId The id of exception rules to look for.
	 *
	 * @return A list of exceptions that match the exceptionType and exceptionTypeId.
	 */
	public List<DiscontinueExceptionParameters> findByExceptionTypeAndId(String exceptionType, String exceptionTypeId) {
		return this.discontinueExceptionParametersRepository.findByExceptionTypeAndExceptionTypeId(exceptionType, exceptionTypeId);
	}

	/**
	 *  Extracts the audit information from the DiscontinueExceptionParameters entity and saves it into the audit table.
	 * @param discontinueExceptionParametersList The list of exception parameters to be saved to the audit table.
	 * @param actionCode The action code reflecting the change made to exception parameters (ADD/MOD/DEL).
	 */
	private void createExceptionParametersAudits(List<DiscontinueExceptionParameters> discontinueExceptionParametersList, String actionCode) {
		List<DiscontinueExceptionParametersAudit> exceptionParametersAuditList = new ArrayList<>();
		DiscontinueExceptionParametersAudit exceptionParametersAudit = null;
		LocalDateTime timestamp= null;
		for(int x = 0; x<discontinueExceptionParametersList.size(); x++){

			// Time is the only key in the Audit table, so to ensure that each time is different we added one microsecond
			// to each TS.
			timestamp = LocalDateTime.now();
			exceptionParametersAudit = new DiscontinueExceptionParametersAudit();
			exceptionParametersAudit.setId(discontinueExceptionParametersList.get(x).getKey().getId());
			exceptionParametersAudit.setSequenceNumber(discontinueExceptionParametersList.get(x).getKey().getSequenceNumber());
			exceptionParametersAudit.setParameterValue(discontinueExceptionParametersList.get(x).getParameterValue());
			exceptionParametersAudit.setPriority(discontinueExceptionParametersList.get(x).getPriority());
			exceptionParametersAudit.setActive(discontinueExceptionParametersList.get(x).isActive());
			exceptionParametersAudit.setAction(actionCode);
			exceptionParametersAudit.setUserId(userInfo.getUserId());
			exceptionParametersAudit.setExceptionNumber(discontinueExceptionParametersList.get(x).getKey().getExceptionNumber());
			exceptionParametersAudit.setExceptionType(discontinueExceptionParametersList.get(x).getExceptionType());
			exceptionParametersAudit.setExceptionTypeId(discontinueExceptionParametersList.get(x).getExceptionTypeId());
			exceptionParametersAudit.setNeverDelete(discontinueExceptionParametersList.get(x).isNeverDelete());
			exceptionParametersAudit.setTimestamp(timestamp);
			exceptionParametersAuditList.add(exceptionParametersAudit);
		}
		this.discontinueExceptionParametersAuditRepository.save(exceptionParametersAuditList);
	}

	/**
	 * Returns a list of changes made comparing object send from front end, and object acquired from database.
	 *
	 * @param existingRulesList The excpetion rules that exist in the database.
	 * @param newRulesList The new exception rules sent from front end.
	 *
	 * @return A List of exceptions that are the changes made by a user.
	 */
	private List<DiscontinueExceptionParameters> getUpdateChanges(
			List<DiscontinueExceptionParameters> existingRulesList, List<DiscontinueExceptionParameters> newRulesList) {
		List<DiscontinueExceptionParameters> changeList = new ArrayList<>();
		boolean foundExistingException;

		for(DiscontinueExceptionParameters newRule : newRulesList){
			foundExistingException = false;
			for(DiscontinueExceptionParameters existingRule : existingRulesList){

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
	 * Sets the upc service for this class. This method is used to set mock data for testing.
	 *
	 * @param upcService The upc service for this class.
	 */
	public void setUpcService(UpcService upcService) {
		this.upcService = upcService;
	}
}
