package com.heb.pm.scaleManagement;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.Hits;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.NutrientStatementDetail;
import com.heb.pm.entity.NutrientStatementHeader;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * REST endpoint for accessing Nutrient Statement information.
 *
 * @author m594201
 * @since 2.2.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + NutrientStatementController.NUTRIENTS_STATEMENT_URL)
@AuthorizedResource(ResourceConstants.SCALE_MANAGEMENT_NUTRIENT_STATEMENT)
public class NutrientStatementController {

	private static final Logger logger = LoggerFactory.getLogger(NutrientStatementController.class);


	/**
	 * The constant NUTRIENTS_STATEMENT_URL.
	 */
	protected static final String NUTRIENTS_STATEMENT_URL = "/nutrientsStatement";
	protected static final String NUTRIENT_STATEMENT_HITS_URL = "/hits/nutrientStatements";
	protected static final String NUTRIENT_CODES_HITS_URL = "/hits/nutrientCodes";

	private static final String UPDATE_SUCCESS_MESSAGE_KEY ="NutrientStatementController.updateSuccessful";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE ="Nutrient Statement: %d updated successfully.";
	private static final String NUTRIENT_STATEMENT_BY_STATEMENT_NUMBER_EXPORT ="User %s from IP %s has requested to " +
			"export to excel nutrient statement data for the following statement numbers: [%s]";
	private static final String NUTRIENT_STATEMENT_BY_NUTRIENT_CODE_EXPORT ="User %s from IP %s has requested to " +
			"export to excel nutrient statement data for the following nutrient codes: [%s]";
	// Keys to user facing messages in the message resource bundle.
	private static final String DEFAULT_NO_NUTRIENT_CD_MESSAGE = "Must search for at least one nutrient code.";
	private static final String NUTRIENT_CD_MESSAGE_KEY =
			"NutrientStatementController.missingNutrientCode";
	private static final String DEFAULT_NO_NUTRIENT_STATEMENT_MESSAGE =
			"Must search for at least one nutrient statement.";
	private static final String NUTRIENT_STATEMENT_MESSAGE_KEY =
			"NutrientStatementController.missingNutrientStatement";
	private static final String DEFAULT_DELETE_SUCCESS_MESSAGE ="Nutrient Statement: %d deleted successfully.";
	private static final String DELETE_SUCCESS_MESSAGE_KEY ="NutrientStatementController.deleteSuccessful";

	private static final String DELETE_NUTRIENT_STATEMENT = "User %s from IP %s has requested to delete the " +
			"Nutrient Statement with the id: %d.";

	// Log Messages
	private static final String FIND_ALL_MESSAGE = "User %s from IP %s requested all ingredient statements";
	private static final String FIND_BY_NUTRIENT_CODE_MESSAGE =
			"User %s from IP %s requested the nutrient statements with the following nutrient codes [%s];";
	private static final String FIND_BY_NUTRIENT_STATEMENT_MESSAGE =
			"User %s from IP %s requested the nutrient statements with the following " +
					"nutrient statements codes [%s]";
	private static final String ADD_STATEMENT_LOG_MESSAGE =
			"User %s from IP %s has requested to add nutrient statement %s";
	private static final String UPDATE_STATEMENT_LOG_MESSAGE =
			"User %s from IP %s has requested to update nutrient statement %d with the values %s";

	// Defaults
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final String MANDATED_NUTRIENTS_BY_STATEMENT_NUMBER =
			"User %s from IP %s has requested to find the mandated nutrients not currently on statement number %d";
	private static final String MANDATED_NUTRIENTS_NEW =
			"User %s from IP %s has requested to find the mandated nutrients for a new Nutrient Statement";
	private static final String APPLYING_NUTRIENT_ROUNDING_RULES =
			"User %s from IP %s has requested to apply the rounding rules for nutrient statement detail with nutrient" +
					" statement number: %d with nutrient code: %d.";
	private static final String CHECK_NLEA16_IS_EXIST_LOG_MESSAGE = "User %s from IP %s has requested to check NLEA2016 nutrient statement is exist with statement number: %d.";

	@Autowired
	private NutrientsService nutrientsService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	private LazyObjectResolver<NutrientStatementHeader> resolver = new NutrientStatementHeaderResolver();


	/**
	 * Find all by nutrient code pageable result.
	 *
	 * @param nutrientCodes the nutrient codes
	 * @param page          the page
	 * @param includeCounts the include counts
	 * @param request       the request
	 * @return the pageable result
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "nutrientStatementByCode")
	public PageableResult<NutrientStatementHeader> findAllByNutrientCode(
			@RequestParam("nutrientCodes")List<Long> nutrientCodes,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts, HttpServletRequest request){

		this.parameterValidator.validate(nutrientCodes, NutrientStatementController.DEFAULT_NO_NUTRIENT_CD_MESSAGE,
				NutrientStatementController.NUTRIENT_CD_MESSAGE_KEY, request.getLocale());
		this.logFindByNutrientCodes(request.getRemoteAddr(),nutrientCodes);

		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? NutrientStatementController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? NutrientStatementController.DEFAULT_PAGE_SIZE : pageSize;

		return this.resolveResults(this.nutrientsService.findNutrientStatementByNutrientCode(nutrientCodes,ic, ps, pg));
	}

	/**
	 * Find all by nutrient statement id pageable result.
	 *
	 * @param statementId   the statement id
	 * @param page          the page
	 * @param includeCounts the include counts
	 * @param request       the request
	 * @return the pageable result
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "nutrientStatementByNutrientStatementId")
	public PageableResult<NutrientStatementHeader> findAllByNutrientStatementId(
			@RequestParam("nutrientStatementId")List<Long> statementId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts, HttpServletRequest request){

		this.parameterValidator.validate(statementId, NutrientStatementController.DEFAULT_NO_NUTRIENT_STATEMENT_MESSAGE,
				NutrientStatementController.NUTRIENT_STATEMENT_MESSAGE_KEY, request.getLocale());
		this.logFindByNutrientStatements(request.getRemoteAddr(), statementId);

		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? NutrientStatementController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? NutrientStatementController.DEFAULT_PAGE_SIZE : pageSize;

		return this.resolveResults(this.nutrientsService.findNutrientStatementByStatementId(statementId,ic, ps, pg));
	}

	/**
	 * Search for available nutrient statement.
	 *
	 * @param nutrientStatementNumber the nutrient statement number
	 * @param request                 the request
	 * @return the string
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "searchForAvailableNutrientStatement")
	public String searchForAvailableNutrientStatement(@RequestParam("nutrientStatement") Long nutrientStatementNumber,
													  HttpServletRequest request) {

		return this.nutrientsService.searchForAvailableNutrientStatement(nutrientStatementNumber);
	}

	/**
	 * Find all statement ids pageable result.
	 *
	 * @param page          the page
	 * @param includeCounts the include counts
	 * @param request       the request
	 * @return the pageable result
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "queryAllNutrientStatement")
	public PageableResult<NutrientStatementHeader> findAllStatementIds(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			HttpServletRequest request){

		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? NutrientStatementController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? NutrientStatementController.DEFAULT_PAGE_SIZE : pageSize;

		return this.resolveResults(this.nutrientsService.findAllStatementId(ic, pg, ps));
	}


	/**
	 * Update nutrient statement data modified entity.
	 *
	 * @param nutrientStatementHeader the nutrient statement header
	 * @param request                 the request
	 * @return the modified entity
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT, value = "updateNutrientStatementData")
	public ModifiedEntity<NutrientStatementHeader> updateNutrientStatementData(
			@RequestBody NutrientStatementHeader nutrientStatementHeader, HttpServletRequest request) {

		this.logUpdateStatement(request.getRemoteAddr(), nutrientStatementHeader);


		NutrientStatementHeader updatedNutrientStatement = this.nutrientsService.updateNutrientStatementData(nutrientStatementHeader);

		// Was having significant problems refreshing the object after the update. The only way I was ever able
		// to make it work was to do the update and then, outside the function marked as a transaction, reorder
		// the nutrient details.
		this.nutrientsService.orderNutrientDetailsBySequence(updatedNutrientStatement.getNutrientStatementDetailList());

		String updateMessage = this.messageSource.getMessage(NutrientStatementController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{updatedNutrientStatement.getNutrientStatementNumber()},
				String.format(NutrientStatementController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
						nutrientStatementHeader.getNutrientStatementNumber()), request.getLocale());

		this.resolver.fetch(updatedNutrientStatement);

		return new ModifiedEntity<>(updatedNutrientStatement, updateMessage);
	}

	/**
	 * Add statement modified entity.
	 *
	 * @param statementHeader the statement header
	 * @param request         the request
	 * @return the modified entity
	 * @throws NotFoundException the not found exception
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "addStatement")
	public ModifiedEntity<NutrientStatementHeader> addStatement(@RequestBody NutrientStatementHeader statementHeader,
																HttpServletRequest request)
			throws IllegalArgumentException {

		this.logAddStatement(request.getRemoteAddr(), statementHeader);

		NutrientStatementHeader updatedNutrientStatement =
				this.nutrientsService.addNutrientStatementData(statementHeader);

		String updateMessage = this.messageSource.getMessage(NutrientStatementController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{updatedNutrientStatement.getNutrientStatementNumber()},
				String.format(NutrientStatementController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
						updatedNutrientStatement.getNutrientStatementNumber()), request.getLocale());

		return new ModifiedEntity<>(updatedNutrientStatement, updateMessage);
	}

	/**
	 * Find by nutrient statements export.
	 *
	 * @param nutrientStatements the nutrient statements
	 * @param totalRecordCount   the total record count
	 * @param downloadId         the download id
	 * @param request            the request
	 * @param response           the response
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "exportNutrientStatementByIdsToCsv",
			headers = "Accept=text/csv")
	public void findByNutrientStatementsExport(
			@RequestParam(name = "nutrientStatements", required = false) List<Long> nutrientStatements,
			@RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
			@RequestParam(value = "downloadId", required = false) String downloadId,
			HttpServletRequest request, HttpServletResponse response){
		this.logFindByNutrientStatementsExport(request.getRemoteAddr(), nutrientStatements);
		double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		try {
			for(int x=0; x<pageCount; x++) {

				if (x == 0) {
					response.getOutputStream().println(CreateNutrientStatementCsv.getHeading());
				}
				response.getOutputStream().print(CreateNutrientStatementCsv.createCsv(
						this.findAllByNutrientStatementId(nutrientStatements, x,
								NutrientStatementController.DEFAULT_PAGE_SIZE, false, request)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}


	/**
	 * Find all nutrient statements export.
	 *
	 * @param totalRecordCount the total record count
	 * @param downloadId       the download id
	 * @param request          the request
	 * @param response         the response
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "exportAllNutrientStatementsToCsv",
			headers = "Accept=text/csv")
	public void findAllNutrientStatementsExport(
			@RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
			@RequestParam(value = "downloadId", required = false) String downloadId,
			HttpServletRequest request, HttpServletResponse response){
		this.logFindAll(request.getRemoteAddr());
		double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		try {
			for(int x=0; x<pageCount; x++) {

				if (x == 0) {
					response.getOutputStream().println(CreateNutrientStatementCsv.getHeading());
				}
				response.getOutputStream().print(CreateNutrientStatementCsv.createCsv(
						this.nutrientsService.findAllStatementId(false, x,
								NutrientStatementController.DEFAULT_PAGE_SIZE)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}


	/**
	 * Find by nutrient codes export.
	 *
	 * @param nutrientCodes    the nutrient codes
	 * @param totalRecordCount the total record count
	 * @param downloadId       the download id
	 * @param request          the request
	 * @param response         the response
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "exportNutrientStatementByNutrientCodesToCsv",
			headers = "Accept=text/csv")
	public void findByNutrientCodesExport(
			@RequestParam(name = "nutrientCodes", required = false) List<Long> nutrientCodes,
			@RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
			@RequestParam(value = "downloadId", required = false) String downloadId,
			HttpServletRequest request, HttpServletResponse response){
		this.logFindByNutrientCodesExport(request.getRemoteAddr(), nutrientCodes);
		double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		try {
			for(int x=0; x<pageCount; x++) {

				if (x == 0) {
					response.getOutputStream().println(CreateNutrientStatementCsv.getHeading());
				}
				response.getOutputStream().print(CreateNutrientStatementCsv.createCsv(
						this.findAllByNutrientCode(nutrientCodes, x, NutrientStatementController.DEFAULT_PAGE_SIZE,
								false, request)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Finds the hits when searching for a list of nutrient statement numbers.
	 *
	 * @param nutrientStatements a list of ingredient statement numbers.
	 * @param request the HTTP request that initiated this call.
	 * @return the hits.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = NutrientStatementController.NUTRIENT_STATEMENT_HITS_URL)
	public Hits findHitsByNutrientStatementId(@RequestParam("nutrientStatements") List<Long> nutrientStatements,
												  HttpServletRequest request){
		this.parameterValidator.validate(nutrientStatements,
				NutrientStatementController.DEFAULT_NO_NUTRIENT_STATEMENT_MESSAGE,
				NutrientStatementController.NUTRIENT_STATEMENT_MESSAGE_KEY, request.getLocale());
		this.logFindByNutrientStatements(request.getRemoteAddr(), nutrientStatements);
		return this.nutrientsService.findNutrientStatementHitsByNutrientStatementCodes(nutrientStatements);
	}

	/**
	 * Finds the hits when searching for a list of ingredient statements by nutrient codes.
	 * @param nutrientCodes a list of nutrient codes.
	 * @param request the HTTP request that initiated this call.
	 * @return the hits.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value =  NutrientStatementController.NUTRIENT_CODES_HITS_URL)
	public Hits findHitsByIngredientCode(@RequestParam("nutrientCodes") List<Long> nutrientCodes,
										 HttpServletRequest request){

		this.parameterValidator.validate(nutrientCodes, NutrientStatementController.DEFAULT_NO_NUTRIENT_CD_MESSAGE,
				NutrientStatementController.NUTRIENT_CD_MESSAGE_KEY, request.getLocale());
		this.logFindByNutrientCodes(request.getRemoteAddr(), nutrientCodes);
		return this.nutrientsService.findNutrientStatementHitsByNutrientCodes(nutrientCodes);
	}


	/**
	 * Retrieves the mandated nutrients for the given statement Id.
	 *
	 * @param statementNumber The statement number to use to get mandated nutrients.
	 * @param request The request.
	 * @return The mandated nutrients for the statement id.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "mandatedNutrientsByStatementId")
	public List<NutrientStatementDetail> getMandatedNutrientsByStatementId(@RequestParam(
			value = "statementNumber", required = false) Long statementNumber, HttpServletRequest request){

		this.logMandatedNutrientsByStatementId(statementNumber, request.getRemoteAddr());
		return this.nutrientsService.findMandatedNutrientsByStatementId(statementNumber);
	}

	/**
	 * Deletes a nutrient statement.
	 *
	 * @param nutrientStatementNumber Nutrient statement number to be deleted.
	 * @param deptList The list of departments to send the delete down to.
	 * @param request The HTTP Request that initiated this call.
	 * @return The updated nutrient statement.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.DELETE, value = "deleteNutrientStatement")
	public ModifiedEntity<Long> deleteNutrientStatement(@RequestParam(value = "nutrientStatementNumber") Long nutrientStatementNumber,
														@RequestParam(value = "deptList", required = false) List<Long> deptList, HttpServletRequest request) {

		this.logDelete(request.getRemoteAddr(), nutrientStatementNumber);

		this.nutrientsService.deleteNutrientStatement(deptList, nutrientStatementNumber);

		String updateMessage = this.messageSource.getMessage(NutrientStatementController.DELETE_SUCCESS_MESSAGE_KEY,
				new Object[]{nutrientStatementNumber}, NutrientStatementController.DEFAULT_DELETE_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(nutrientStatementNumber, updateMessage);
	}

	/**
	 * Applies nutrient rounding rules to a nutrient statement detail.
	 *
	 * @param nutrientStatementDetail Nutrient statement detail that has the quantity to by updated.
	 * @param request The request.
	 * @return modified nutrient statement number.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT, value = "applyRoundingRuleToNutrient")
	public ModifiedEntity<NutrientStatementDetail> applyRoundingRuleToNutrient(
			@RequestBody NutrientStatementDetail nutrientStatementDetail, HttpServletRequest request){
		this.logApplyRoundingRuleToNutrient(request.getRemoteAddr(), nutrientStatementDetail);
		return new ModifiedEntity<>(this.nutrientsService.applyRoundingRuleToNutrient(nutrientStatementDetail),
				this.messageSource.getMessage("Success", new Object[]{nutrientStatementDetail},
						"Success", request.getLocale()));
	}

	/**
	 * Check NLEA16 nutrient statement is exist.
	 * @param nutrientStatementNumber Nutrient statement number.
	 * @param request The request.
	 * @return modified nutrient statement number.
	 */
	@ViewPermission
	@RequestMapping (method = RequestMethod.GET,value = "isNLEA16NutrientStatementExists")
	public ModifiedEntity<Boolean> isNLEA16NutrientStatementExists(@RequestParam("nutrientStatementNumber") Long nutrientStatementNumber,HttpServletRequest request) {
		NutrientStatementController.logger.info(String.format(NutrientStatementController.CHECK_NLEA16_IS_EXIST_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr(), nutrientStatementNumber));
		return new ModifiedEntity<>(nutrientsService.isNLEA16NutrientStatementExists(nutrientStatementNumber),null);
	}

	/**
	 * Logs applying nutrient rounding rule to a nutrient statement detail.
	 *
	 * @param remoteAddr The ip of the user.
	 * @param nutrientStatementDetail The nutrient statement detail.
	 */
	private void logApplyRoundingRuleToNutrient(String remoteAddr, NutrientStatementDetail nutrientStatementDetail) {
		NutrientStatementController.logger.info(String.format(
				NutrientStatementController.APPLYING_NUTRIENT_ROUNDING_RULES, this.userInfo.getUserId(), remoteAddr,
				nutrientStatementDetail.getKey().getNutrientStatementNumber(),
				nutrientStatementDetail.getKey().getNutrientLabelCode()));
	}

	/**
	 * Logs retrieval of mandated nutrients.
	 * @param nutrientStatementNumber The nutrient statement number.
	 * @param remoteAddr The ip of the user.
	 */
	private void logMandatedNutrientsByStatementId(Long nutrientStatementNumber, String remoteAddr) {
		if(nutrientStatementNumber != null) {
			NutrientStatementController.logger.info(String.format(
					NutrientStatementController.MANDATED_NUTRIENTS_BY_STATEMENT_NUMBER, this.userInfo.getUserId(),
					remoteAddr, nutrientStatementNumber));
		} else {
			NutrientStatementController.logger.info(String.format(
					NutrientStatementController.MANDATED_NUTRIENTS_NEW, this.userInfo.getUserId(),
					remoteAddr));
		}
	}

	/**
	 * Fetch details for the resolver
	 * @param results a pageable result of nutrient statement header.
	 * @return a nutrient statement header.
	 */
	private PageableResult<NutrientStatementHeader> resolveResults(PageableResult<NutrientStatementHeader> results){
		results.getData().forEach(this.resolver::fetch);
		return results;
	}

	/**
	 * Logs a user's request to get a nutrient statement CSV export by nutrient statement code.
	 * @param ip The IP address th user is logged in from.
	 * @param nutrientStatements The nutrients statements to search on.
	 */
	private void logFindByNutrientStatementsExport(String ip, List<Long> nutrientStatements) {
		NutrientStatementController.logger.info(String.format(NutrientStatementController.
				NUTRIENT_STATEMENT_BY_STATEMENT_NUMBER_EXPORT, this.userInfo.getUserId(), ip, nutrientStatements));
	}

	/**
	 * Logs a user's request to get a nutrient statement CSV export by nutrient statement code.
	 * @param ip The IP address th user is logged in from.
	 * @param nutrientStatements The nutrients statements to search on.
	 */
	private void logFindByNutrientCodesExport(String ip, List<Long> nutrientStatements) {
		NutrientStatementController.logger.info(String.format(NutrientStatementController.
				NUTRIENT_STATEMENT_BY_NUTRIENT_CODE_EXPORT, this.userInfo.getUserId(), ip, nutrientStatements));
	}

	/**
	 * Logs a user's request to get nutrient statements by nutrient statement code.
	 * @param ip The IP address th user is logged in from.
	 * @param nutrientStatements The nutrients statements to search on.
	 */
	private void logFindByNutrientStatements(String ip, List<Long> nutrientStatements) {
		NutrientStatementController.logger.info(String.format(NutrientStatementController.
				FIND_BY_NUTRIENT_STATEMENT_MESSAGE, this.userInfo.getUserId(), ip, nutrientStatements));
	}

	/**
	 * Logs a user's request to get nutrient statements by nutrient codes.
	 * @param ip The IP address th user is logged in from.
	 * @param nutrientCodes The nutrients codes to search on for nutrient statements.
	 */
	private void logFindByNutrientCodes(String ip, List<Long> nutrientCodes) {
		NutrientStatementController.logger.info(String.format(NutrientStatementController.
				FIND_BY_NUTRIENT_CODE_MESSAGE, this.userInfo.getUserId(), ip, nutrientCodes));

	}

	/**
	 * Logs a user's request to get all nutrient statements.
	 * @param ip The IP address th user is logged in from.
	 */
	private void logFindAll(String ip) {
		NutrientStatementController.logger.info(String.format(NutrientStatementController.FIND_ALL_MESSAGE,
				this.userInfo.getUserId(), ip));

	}
	/**
	 * Logs a user's request to delete a nutrient statement.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param nutrientStatementNumber The nutrient statement to be deleted.
	 */
	private void logDelete(String ip, Long nutrientStatementNumber){
		NutrientStatementController.logger.info(String.format(NutrientStatementController.DELETE_NUTRIENT_STATEMENT,
				this.userInfo.getUserId(), ip, nutrientStatementNumber));
	}

	/**
	 * Logs a user's request to add a new nutrient statment.
	 *
	 * @param ipAddress The IP the user logged in from.
	 * @param statementHeader The statment to log.
	 */
	private void logAddStatement(String ipAddress, NutrientStatementHeader statementHeader) {
		NutrientStatementController.logger.info(String.format(NutrientStatementController.ADD_STATEMENT_LOG_MESSAGE,
				this.userInfo.getUserId(), ipAddress, statementHeader.toString()));
	}

	/**
	 * Logs a user's request to update a nutrient statement.
	 *
	 * @param ipAddress The IP address the user is logging in from.
	 * @param header The NutrientStatementHeader they are trying to save.
	 */
	private void logUpdateStatement(String ipAddress, NutrientStatementHeader header) {
		NutrientStatementController.logger.info(String.format(NutrientStatementController.UPDATE_STATEMENT_LOG_MESSAGE,
				this.userInfo.getUserId(), ipAddress, header.getNutrientStatementNumber(),
				header.toString()));
	}

}
