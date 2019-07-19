package com.heb.pm.scaleManagement;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds all business logic related for ingredients.
 *
 * @author m594201
 * @since 2.0.9
 */
@Service
public class IngredientService {

	private static final Logger logger = LoggerFactory.getLogger(IngredientService.class);

	private static final String DESCRIPTION_REGEX = "%%%s%%";
	private static final long DEFAULT_STARTING_SEQUENCE_NUMBER = 1;
	private static final long MAX_INGREDIENT_CODE = 99999;
	private static final long LOWESET_ALLOWED_INGREDIENT_CODE = 1;
	private static final String UPDATE_MAINT_FUNCTION = "C";
	private static final String ADD_MAINT_FUNCTION = "A";
	private static final String DELETE_MAINT_FUNCTION = "D";

	private static final String INGREDIENT_REMOVE_FAIL_MESSAGE = "Ingredient %s cannot be removed because it exists ";
	private static final String INGREDIENT_CODES = "as a sub-ingredient of ingredient codes: %s";
	private static final String AND_INGREDIENT_STATEMENT_CODES = ", and as an ingredient on ingredient statement " +
			"codes: %s";
	private static final String INGREDIENT_STATEMENT_CODES = "as an ingredient on ingredient statement codes: %s";
	private static final String INGREDIENT_CODE_MAX_FAIL_MESSAGE = "Reached max ingredient code 99999.";
	private static final String REPEAT_INGREDIENT_IN_SOI_ERROR =
			"Ingredient %s is listed as a sub-ingredient multiple times.";
	private static final String INVALID_INGREDIENT_ERROR = "Ingredient %s does not exist.";
	private static final String INVALID_DESCRIPTION_ERROR = "Ingredient description cannot be blank.";

	private static final String INGREDIENT_EXPORT_HEADER =
			"\"Ingredient Code\",\"Category\",\"Ingredient Description\",\"Extended Description\",\"Ingredient Statements\",\"PLUs\",\"Sub Ingredients\"";
	private static final String INGREDIENT_EXPORT_FORMAT = "%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"";
	private static final String EXPORT_INTERACTED_DELIMITER = " ";

	private static final String INVALID_INGREDIENT_ADD_MESSAGE = "A parent ingredient cannot be sub-ingredient of its sub-ingredient";

	/**
	 * The Ingredient repository.
	 */
	@Autowired
	private IngredientRepository ingredientRepository;

	/**
	 * The Ingredient repository with count.
	 */
	@Autowired
	private IngredientRepositoryWithCount ingredientRepositoryWithCount;

	/**
	 * The Ingredient statement header repository.
	 */
	@Autowired
	private IngredientStatementHeaderRepository ingredientStatementHeaderRepository;

	/**
	 * The Ingredient sub repository.
	 */
	@Autowired
	private IngredientStatementDetailRepository ingredientStatementDetailRepository;

	/**
	 * The Ingredient sub repository.
	 */
	@Autowired
	private IngredientSubRepository ingredientSubRepository;

	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private UserInfo userInfo;

	private LazyObjectResolver<Ingredient> ingredientResolver = new IngredientResolver();

	/**
	 * Searches for ingredients that contain a requested ingredient code as a sub-ingredient.
	 *
	 * @param ingredientCode The ingredient code to search for.
	 * @param includeCounts Whether or not to include counts in the result.
	 * @param page The page of data to search for.
	 * @param pageSize The maximum number of records to return.
	 * @return A pageable list of ingredients that contain the requested ingredient as a sub-ingredient.
	 */
	public PageableResult<Ingredient> findSuperIngredients(String ingredientCode, boolean includeCounts, int page, int pageSize) {
		PageRequest pageRequest = new PageRequest(page, pageSize);
		if (includeCounts) {
			Page<Ingredient> ingredients =
					this.ingredientRepositoryWithCount.findBySoiChild(ingredientCode, pageRequest);
			return new PageableResult<>(page, ingredients.getTotalPages(), ingredients.getTotalElements(),
					ingredients.getContent());
		} else {
			List<Ingredient> ingredients = this.ingredientRepository.findBySoiChild(ingredientCode, pageRequest);
			return new PageableResult<>(page, ingredients);
		}
	}

	/**
	 * Streams a CSV of an ingredient. This includes all other ingredients this ingredient is part of,
	 * all the ingredient statements those are tied to, and all the UPCs those statements are tied to.
	 *
	 * @param outputStream The output stream to write the CSV to.
	 * @param ingredientCode The code the user requested data for.
	 */
	public void streamIngredient(ServletOutputStream outputStream, String ingredientCode) {
		// Print out the header and the delegate the ingredient details.
		try {
			outputStream.println(INGREDIENT_EXPORT_HEADER);
		} catch (IOException e) {
			IngredientService.logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
		this.streamIngredientDetails(outputStream, ingredientCode);
	}

	/**
	 * Calls to stream an ingredient AND its related ingredients to a CSV.
	 *
	 * @param outputStream The output stream to write the CSV to.
	 * @param ingredientCode The code the user requested data for.
	 */
	private void streamIngredientDetails(ServletOutputStream outputStream, String ingredientCode) {
		Ingredient ingredient = this.ingredientRepository.findOne(ingredientCode);
		this.streamSingleIngredientDetails(outputStream, ingredient);

		// Find all the ingredients the requested code is part of.
		List<IngredientSub> superIngredientList = this.ingredientSubRepository.findByKeyIngredientCode(ingredientCode);
		for (IngredientSub ingredientSub : superIngredientList) {

			// Stream each of those ingredients as well.
			this.streamIngredientDetails(outputStream, ingredientSub.getKey().getSoIngredientCode());
		}
	}

	/**
	 * Performs the actual work of streaming a single ingredient to a CSV.
	 *
	 * @param outputStream The output stream to write the CSV to.
	 * @param ingredient one ingredient the user requested details for.
	 */
	private void streamSingleIngredientDetails(ServletOutputStream outputStream, Ingredient ingredient){
		try {
			StringBuilder ingredientStatementBuilder = new StringBuilder();
			StringBuilder pluListBuilder = new StringBuilder();
			// Build up the ingredient statements this ingredient is on.

			// I know the details are mapped in ingredient, but because of spaces in the DB and whatnot, it doesn't
			// always work.
			for (IngredientStatementDetail detail :
					this.ingredientStatementDetailRepository.findByKeyIngredientCode(ingredient.getIngredientCode())) {
				ingredientStatementBuilder.append(detail.getKey().getStatementNumber()).append(EXPORT_INTERACTED_DELIMITER);

				// Add all the UPCs that ingredient statement is tied to.
				for (ScaleUpc upc : detail.getIngredientStatementHeader().getScaleUpcs()) {
					pluListBuilder.append(upc.getPlu()).append(EXPORT_INTERACTED_DELIMITER);
				}
			}
			// Write out the ingredient to the stream.
			outputStream.println(
					String.format(INGREDIENT_EXPORT_FORMAT, ingredient.getIngredientCode(),
							ingredient.getIngredientCategory().getDisplayText(),
							ingredient.getIngredientDescription(), ingredient.getIngredientCatDescription(),
							ingredientStatementBuilder.toString(), pluListBuilder.toString(),
							ingredient.getDisplayText())
			);
		} catch (IOException e) {
			IngredientService.logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Streams a page from an ingredient search item by item.
	 *
	 * @param outputStream The output stream to write the CSV to.
	 * @param page the PageableResult returned from the initial searh
	 */

	public void streamIngredientListPage(ServletOutputStream outputStream, PageableResult<Ingredient> page){
		Iterable<Ingredient> page1Iterable = page.getData();
		for (Ingredient ingredient : page1Iterable) {
			this.streamSingleIngredientDetails(outputStream, ingredient);
		}
	}

	/**
	 * Creates and calls for a list of ingredients found by description(without diving further to make an entry for each related ingredient) to be streamed to a CSV.
	 *
	 * @param outputStream The output stream to write the CSV to.
	 * @param categoryCode categoryCode
	 */
	public void streamIngredientsByDescription(ServletOutputStream outputStream, String description, Long categoryCode,
											IngredientController.IncludeCategory includeCategory) {
		//Export the header to the file
		try {
			outputStream.println(INGREDIENT_EXPORT_HEADER);
		} catch (IOException e) {
			IngredientService.logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}

		//Find the data and stream the data of from the first page and count the total number of pages returned by the search.
		PageableResult<Ingredient> ingredientListPage1 = this.findByDescription(description, categoryCode, includeCategory, true, 0, 100);
		int numberOfPages = ingredientListPage1.getPageCount();
		this.streamIngredientListPage(outputStream, ingredientListPage1);

		//If more than one page, loop through and stream all other pages.
		if(numberOfPages > 1){
			for(int currentPage = 1; currentPage < numberOfPages; currentPage++) {
				this.streamIngredientListPage(outputStream, this.findByDescription(description, categoryCode, includeCategory, false, currentPage, 100));
			}
		}
	}


	/**
	 * Creates and calls for a list of ingredients found by ingredient code(without diving further to make an entry for each related ingredient) to be streamed to a CSV.
	 *
	 * @param outputStream The output stream to write the CSV to.
	 * @param ingredientCodeList the list of ingredient codes to find and stream.
	 * @param categoryCode categoryCode
	 * @param includeCategory include of exclude.
	 */
	public void streamIngredientListByCode(ServletOutputStream outputStream, List<String> ingredientCodeList, Long categoryCode, IngredientController.IncludeCategory includeCategory){
		//Export the header to the file
		try {
			outputStream.println(INGREDIENT_EXPORT_HEADER);
		} catch (IOException e) {
			IngredientService.logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}

		PageableResult<Ingredient> ingredientListPage1 = this.findIngredientByCode(ingredientCodeList, categoryCode, includeCategory, true, 0, 100);
		int numberOfPages = ingredientListPage1.getPageCount();
		this.streamIngredientListPage(outputStream, ingredientListPage1);

		if(numberOfPages > 1){
			for(int currentPage = 1; currentPage < numberOfPages; currentPage++) {
				this.streamIngredientListPage(outputStream, this.findIngredientByCode(ingredientCodeList, categoryCode, includeCategory, false, currentPage, 100));
			}
		}
	}

	/**
	 * Creates and calls for a list of all ingredients to be streamed to a CSV.
	 *
	 * @param outputStream The output stream to write the CSV to.
	 * @param categoryCode categoryCode
	 * @param includeCategory include of exclude.
	 */
	public void streamAllIngredients(ServletOutputStream outputStream, Long categoryCode, IngredientController.IncludeCategory includeCategory){
		//Export the header to the file
		try {
			outputStream.println(INGREDIENT_EXPORT_HEADER);
		} catch (IOException e) {
			IngredientService.logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}

		PageableResult<Ingredient> ingredientListPage1 = this.findAll(categoryCode, includeCategory, true, 0, 100);
		int numberOfPages = ingredientListPage1.getPageCount();
		this.streamIngredientListPage(outputStream, ingredientListPage1);

		if(numberOfPages > 1){
			for(int currentPage = 1; currentPage < numberOfPages; currentPage++) {
				this.streamIngredientListPage(outputStream, this.findAll(categoryCode, includeCategory, false, currentPage, 100));
			}
		}
	}

	/**
	 * Find ingredient by code pageable result.
	 *
	 * @param ingredientCode  the ingredient code
	 * @param categoryCode    the category code
	 * @param includeCategory the include category
	 * @param includeCount    the include count
	 * @param page            the page
	 * @param pageSize        the page size
	 * @return the pageable result
	 */
	public PageableResult<Ingredient> findIngredientByCode(List<String> ingredientCode, Long categoryCode, IngredientController.IncludeCategory includeCategory,
														   boolean includeCount, int page, int pageSize) {
		if(ingredientCode == null){
			throw new IllegalArgumentException("Ingredient Code cannot be null");
		}

		Pageable request = new PageRequest(page, pageSize, Ingredient.getDescriptionSort(Sort.Direction.ASC));

		return includeCount ? this.findIngredientByCodeWithCount(ingredientCode, categoryCode, includeCategory, request) :
				this.findIngredientByCodeWithoutCount(ingredientCode, categoryCode, includeCategory, request);
	}

	/**
	 * Find ingredient by code pageable result with count.
	 * @param ingredientCode the category code
	 * @param categoryCode the category code
	 * @param includeCategory the include category
	 * @param request the request
	 * @return the pageable result
	 */

	private PageableResult<Ingredient> findIngredientByCodeWithCount(List<String> ingredientCode, Long categoryCode,
																	 IngredientController.IncludeCategory includeCategory,
																	 Pageable request) {
		Page<Ingredient> data;

		switch (includeCategory){
			case INCLUDE: data = this.ingredientRepositoryWithCount.findByIngredientCodeInAndCategoryCode(
					ingredientCode, categoryCode, request);
				break;
			case EXCLUDE: data = this.ingredientRepositoryWithCount.findByIngredientCodeInAndCategoryCodeNot(
					ingredientCode, categoryCode, request);
				break;
			default: {
				if(ingredientCode.size() > 0) {
					data = this.ingredientRepositoryWithCount.findByIngredientCodeIn(ingredientCode, request);
				} else {
					throw new IllegalArgumentException("No ingredients found.");
				}
				break;
			}
		}

		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Find ingredient by code pageable result without count.
	 * @param ingredientCode the category code
	 * @param categoryCode the category code
	 * @param includeCategory the include category
	 * @param request the request
	 * @return the pageable result
	 */
	private PageableResult<Ingredient> findIngredientByCodeWithoutCount(List<String> ingredientCode, Long categoryCode,
																		IngredientController.IncludeCategory includeCategory,
																		Pageable request) {
		List<Ingredient> data;
		if(includeCategory==null){
			includeCategory=IngredientController.IncludeCategory.EMPTY;
		}
		switch (includeCategory){
			case INCLUDE: data = this.ingredientRepository.findByIngredientCodeInAndCategoryCode(ingredientCode,
					categoryCode, request);
				break;
			case EXCLUDE: data = this.ingredientRepository.findByIngredientCodeInAndCategoryCodeNot(ingredientCode,
					categoryCode, request);
				break;
			default:  {
				if(ingredientCode.size() > 0) {
					data = this.ingredientRepository.findByIngredientCodeIn(ingredientCode, request);
				} else {
					throw new IllegalArgumentException("No ingredients found.");
				}
				break;
			}
		}

		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Find by description pageable result.	 *
	 * @param description     the description
	 * @param categoryCode    the category code
	 * @param includeCategory the include category
	 * @param includeCount    the include count
	 * @param page            the page
	 * @param pageSize        the page size
	 * @return the pageable result
	 */
	public PageableResult<Ingredient> findByDescription(String description, Long categoryCode,
														IngredientController.IncludeCategory includeCategory,
														boolean includeCount, int page, int pageSize) {

		if(description == null){
			throw new IllegalArgumentException("Description Code cannot be null");
		}

		Pageable request = new PageRequest(page, pageSize, Ingredient.getDescriptionSort(Sort.Direction.ASC));

		return includeCount ? this.findByDescriptionWithCount(String.format(DESCRIPTION_REGEX, description),
				categoryCode, includeCategory, request) :
				this.findByDescriptionWithoutCount(
						String.format(DESCRIPTION_REGEX, description), categoryCode, includeCategory, request);
	}

	/**
	 * Find Ingredients by Description Without counts
	 * @param description description entered
	 * @param categoryCode categoryCode of the ingredient
	 * @param includeCategory category Of the ingredient
	 * @param request the request
	 * @return the pageable result
	 */
	private PageableResult<Ingredient> findByDescriptionWithoutCount(String description, Long categoryCode,
																	 IngredientController.IncludeCategory includeCategory,
																	 Pageable request) {
		List<Ingredient> data;

		switch (includeCategory){
			case INCLUDE: data =
					this.ingredientRepository.findByIngredientDescriptionContainingAndCategoryCode(description.trim().toUpperCase(),
							categoryCode, request);
				break;
			case EXCLUDE: data =
					this.ingredientRepository.findByIngredientDescriptionContainingAndCategoryCodeNot(description.trim().toUpperCase(),
							categoryCode, request);
				break;
			default:  data = this.ingredientRepository.findByIngredientDescriptionContaining(description.trim().toUpperCase(), request);
				break;
		}
		return new PageableResult<>(request.getPageNumber(), data);
	}


	/**
	 * Find Ingredients by Description With count
	 * @param description description entered
	 * @param categoryCode categoryCode of the ingredient
	 * @param includeCategory category Of the ingredient
	 * @param request the request
	 * @return the pageable result
	 */
	private PageableResult<Ingredient> findByDescriptionWithCount(String description, Long categoryCode,
																  IngredientController.IncludeCategory includeCategory,
																  Pageable request) {
		Page<Ingredient> data;

		switch (includeCategory){
			case INCLUDE: data =
					this.ingredientRepositoryWithCount.findByIngredientDescriptionContainingAndCategoryCode(description.trim().toUpperCase(),
							categoryCode, request);
				break;
			case EXCLUDE: data =
					this.ingredientRepositoryWithCount.findByIngredientDescriptionContainingAndCategoryCodeNot(description.trim().toUpperCase(),
							categoryCode, request);
				break;
			default:  data =
					this.ingredientRepositoryWithCount.findByIngredientDescriptionContaining(description.trim().toUpperCase(), request);
				break;
		}
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Find by ingredient statement pageable result.
	 * @param statementCodes  the statement codes
	 * @param categoryCode    the category code
	 * @param includeCategory the include category
	 * @param ic              the ic
	 * @param pg              the pg
	 * @param ps              the ps
	 * @return the pageable result
	 */
	public PageableResult<Ingredient> findByIngredientStatement(List<Long> statementCodes, Long categoryCode,
																IngredientController.IncludeCategory includeCategory,
																boolean ic, int pg, int ps) {

		if(statementCodes == null){
			throw new IllegalArgumentException("Statement Code cannot be null");
		}

		return this.findIngredientByCode(this.findAllIngredientsInHeader(statementCodes), categoryCode, includeCategory,
				ic, pg, ps);
	}

	/**
	 * Find all ingredients in header
	 * @param statementCodes the statement codes to look for
	 * @return List of ingredientCodes.
	 */
	private List<String> findAllIngredientsInHeader(List<Long> statementCodes) {
		List <String> ingredientCodes = new ArrayList<>();

		for(IngredientStatementHeader header: this.ingredientStatementHeaderRepository.findAll(statementCodes)){
			ingredientCodes.addAll(header.getIngredientStatementDetails().stream().map(
					detail -> detail.getIngredient().getIngredientCode()).collect(Collectors.toList()));
		}
		return ingredientCodes;
	}

	/**
	 * Set up the ingredient to be saved.
	 * @param ingredient Ingredient to be added.
	 * @return added ingredient
	 */
	public Ingredient add(Ingredient ingredient) {

		ingredient.setCategoryCode(ingredient.getIngredientCategory().getCategoryCode());

		if(StringUtils.isEmpty(ingredient.getIngredientCatDescription())){
			ingredient.setIngredientCatDescription("");
		}

		long soiSequence = DEFAULT_STARTING_SEQUENCE_NUMBER;
		List<IngredientSub> actualSubs = new ArrayList<>();
		boolean alreadyInList;
		for(IngredientSub sub: ingredient.getIngredientSubs()){
			alreadyInList =
					this.listAlreadyContainsIngredientCode(actualSubs, sub.getSubIngredient().getIngredientCode());
			if(!alreadyInList) {
				sub.getKey().setIngredientCode(sub.getSubIngredient().getIngredientCode());
				sub.getKey().setSoIngredientCode(ingredient.getIngredientCode());
				sub.setSoiSequence(soiSequence++);
			} else {
				String message = String.format(
						IngredientService.REPEAT_INGREDIENT_IN_SOI_ERROR, sub.getSubIngredient().getIngredientCode());
				IngredientService.logger.error(message);
				throw new IllegalArgumentException(message);
			}
			actualSubs.add(sub);
		}

		// If there are sub-ingredients, the SOI flag needs to be set to true.
		if(ingredient.getIngredientSubs().size() == 0){
			ingredient.setSoiFlag(false);
		} else {
			ingredient.setSoiFlag(true);
		}

		ingredient.setMaintFunction(ADD_MAINT_FUNCTION);
		return this.ingredientRepository.save(ingredient);
	}

	/**
	 * Searches for a list of ingredients. This is a wildcard search, meaning that anything partially matching
	 * the text passed in will be returned. It treats any search string that can be converted to a number as
	 * searching by code and any search string that can't be converted as a search on description.
	 *
	 * @param searchString The text to search for ingredients by.
	 * @param currentIngredientCode The current ingredient code (when looking to add sub-ingredients).
	 * @param currentIngredientCodeList List of current ingredient codes and sub-ingredient codes.
	 * @param page The page to look for.
	 * @param pageSize The maximum size for the page.
	 * @return A PageableResult with ingredients matching the search criteria.
	 */
	public PageableResult<Ingredient> findByRegularExpression(String searchString, String currentIngredientCode,
															  List<String> currentIngredientCodeList,
															  int page, int pageSize) {

		List<Ingredient> ingredients;

		// Find all the codes that cannot be added based on the existing ingredients.
//		if(!StringUtils.isBlank(currentIngredientCode)) {
//			currentIngredientCodeList.addAll(
//					this.findAllInvalidIngredientMastersAndSubIngredients(currentIngredientCode));
//		}

		// See if the user is searching for a number
		boolean isSearchNumeric = false;
		try {
			// If the search string is less than six characters and can be converted to a whole number,
			// treat the search as by ingredient code.
			if (searchString.length() <= 5) {
				Integer.valueOf(searchString);
				isSearchNumeric = true;
			}
		} catch (NumberFormatException e) {
			// If we get here, they're searching by a string, so do a text search.
		}

		if (isSearchNumeric) {
			ingredients = this.findByIngredientCode(searchString, currentIngredientCodeList, page, pageSize);
		} else {
			ingredients = this.findByDescription(searchString, currentIngredientCodeList, page, pageSize);
		}

		// To remove duplicates and keep order
		return new PageableResult<>(page, new ArrayList<>(new LinkedHashSet<>(ingredients)));
	}

	/**
	 * Searches for ingredients treating the search string as an ingredient code.
	 *
	 * @param searchString The text to search for ingredients by.
	 * @param currentIngredientCodeList The list of ingredients to exclude from the search.
	 * @param page The page of data to look for.
	 * @param pageSize The maximum number of ingredients to search for.
	 * @return A list of ingredients that match the criteria.
	 */
	private List<Ingredient> findByIngredientCode(String searchString,  List<String> currentIngredientCodeList,
															  int page, int pageSize) {

		Sort sort = Ingredient.getIngredientCodeSort(Sort.Direction.ASC);

		List<Ingredient> ingredients;

		if (currentIngredientCodeList.isEmpty()) {
			// First, look for exact matches.
			ingredients =
					this.ingredientRepository.findByIngredientCodeAndMaintFunctionNot(searchString,
							Ingredient.DELETE_SW, new PageRequest(page, pageSize, sort));

			// Next, look for things that start with that code
			if (ingredients.size() < pageSize) {
				ingredients.addAll(this.ingredientRepository.findByIngredientCodeStartsWithAndMaintFunctionNot(searchString,
						Ingredient.DELETE_SW, new PageRequest(page, pageSize - ingredients.size(), sort)));
			}

			// If there's any space left, see if there are any codes that contains the number
			if (ingredients.size() < pageSize) {
				ingredients.addAll(this.ingredientRepository.findByIngredientCodeContainingAndMaintFunctionNot(searchString,
						Ingredient.DELETE_SW, new PageRequest(page, pageSize - ingredients.size(), sort)));
			}
		} else {
			// Same logic as above, it's just that we've got a list of codes to not include.
			// Look for an exact match
			ingredients =
					this.ingredientRepository.findByIngredientCodeAndIngredientCodeNotInAndMaintFunctionNot(
							searchString, currentIngredientCodeList, Ingredient.DELETE_SW,
							new PageRequest(page, pageSize, sort));

			// Next, look for things that start with the code.
			if (ingredients.size() < pageSize) {
				ingredients.addAll(
						this.ingredientRepository.findByIngredientCodeStartsWithAndIngredientCodeNotInAndMaintFunctionNot(
								searchString, currentIngredientCodeList, Ingredient.DELETE_SW,
								new PageRequest(page, pageSize - ingredients.size(), sort)));
			}

			// If there's any space left, see if there are any codes that contains the number
			if (ingredients.size() < pageSize) {
				ingredients.addAll(
						this.ingredientRepository.findByIngredientCodeContainingAndIngredientCodeNotInAndMaintFunctionNot(
								searchString, currentIngredientCodeList, Ingredient.DELETE_SW,
								new PageRequest(page, pageSize - ingredients.size(), sort)));
			}
		}

		return ingredients;
	}

	/**
	 * Searches for ingredients treating the search string as an ingredient description.
	 *
	 * @param searchString The text to search for ingredients by.
	 * @param currentIngredientCodeList The list of ingredients to exclude from the search.
	 * @param page The page of data to look for.
	 * @param pageSize The maximum number of ingredients to search for.
	 * @return A list of ingredients that match the criteria.
	 */
	private List<Ingredient> findByDescription(String searchString,  List<String> currentIngredientCodeList,
												 int page, int pageSize) {

		Sort sort = Ingredient.getDescriptionSort(Sort.Direction.ASC);

		List<Ingredient> ingredients;

		if (currentIngredientCodeList.isEmpty()) {
			// First, look for exact matches case sensitive.
			ingredients =
					this.ingredientRepository.findByIngredientDescriptionAndMaintFunctionNot(searchString,
							Ingredient.DELETE_SW, new PageRequest(page, pageSize, sort));

			// Next, look for exact matches case-insensitive.
			if (ingredients.size() < pageSize) {
				ingredients.addAll(
						this.ingredientRepository.findByIngredientDescriptionIgnoreCaseAndMaintFunctionNot(searchString,
								Ingredient.DELETE_SW, new PageRequest(page, pageSize - ingredients.size(), sort)
						)
				);
			}

			// Next, look for things that start with that description case sensitive.
			if (ingredients.size() < pageSize) {
				ingredients.addAll(this.ingredientRepository.findByIngredientDescriptionStartsWithAndMaintFunctionNot(
						searchString, Ingredient.DELETE_SW, new PageRequest(page, pageSize - ingredients.size(), sort)));
			}

			// Next, look for things that start with that description case insensitive.
			if (ingredients.size() < pageSize) {
				ingredients.addAll(this.ingredientRepository.findByIngredientDescriptionStartsWithIgnoreCaseAndMaintFunctionNot(
						searchString, Ingredient.DELETE_SW, new PageRequest(page, pageSize - ingredients.size(), sort)));
			}

			// Next, look for things that include that description case sensitive.
			if (ingredients.size() < pageSize) {
				ingredients.addAll(this.ingredientRepository.findByIngredientDescriptionContainingAndMaintFunctionNot(
						searchString, Ingredient.DELETE_SW, new PageRequest(page, pageSize - ingredients.size(), sort)));
			}

			// Next, look for things that include that description case insensitive.
			if (ingredients.size() < pageSize) {
				ingredients.addAll(this.ingredientRepository.findByIngredientDescriptionContainingIgnoreCaseAndMaintFunctionNot(
						searchString, Ingredient.DELETE_SW, new PageRequest(page, pageSize - ingredients.size(), sort)));
			}
		} else {
			// Same logic as above, it's just that we've got a list of codes to not include.
			// Look for an exact match case sensitive
			ingredients =
					this.ingredientRepository.findByIngredientDescriptionAndIngredientCodeNotInAndMaintFunctionNot(
							searchString, currentIngredientCodeList, Ingredient.DELETE_SW,
							new PageRequest(page, pageSize, sort));

			// Look for an exact match case insensitive
			if (ingredients.size() < pageSize) {
				ingredients.addAll(
						this.ingredientRepository.findByIngredientDescriptionIgnoreCaseAndIngredientCodeNotInAndMaintFunctionNot(
								searchString, currentIngredientCodeList, Ingredient.DELETE_SW,
								new PageRequest(page, pageSize, sort))
				);
			}

			// Next, look for things that start with the description case sensitive.
			if (ingredients.size() < pageSize) {
				ingredients.addAll(
						this.ingredientRepository.findByIngredientDescriptionStartsWithAndIngredientCodeNotInAndMaintFunctionNot(
								searchString, currentIngredientCodeList, Ingredient.DELETE_SW,
								new PageRequest(page, pageSize - ingredients.size(), sort)));
			}

			// Next, look for things that start with the description case insensitive.
			if (ingredients.size() < pageSize) {
				ingredients.addAll(
						this.ingredientRepository.findByIngredientDescriptionStartsWithIgnoreCaseAndIngredientCodeNotInAndMaintFunctionNot(
								searchString, currentIngredientCodeList, Ingredient.DELETE_SW,
								new PageRequest(page, pageSize - ingredients.size(), sort)));
			}

			// Next, look for things that include that description case sensitive.
			if (ingredients.size() < pageSize) {
				ingredients.addAll(
						this.ingredientRepository.findByIngredientDescriptionContainingAndIngredientCodeNotInAndMaintFunctionNot(
								searchString, currentIngredientCodeList, Ingredient.DELETE_SW,
								new PageRequest(page, pageSize - ingredients.size(), sort)));
			}

			// Next, look for things that include that description case insensitive.
			if (ingredients.size() < pageSize) {
				ingredients.addAll(
						this.ingredientRepository.findByIngredientDescriptionContainingIgnoreCaseAndIngredientCodeNotInAndMaintFunctionNot(
								searchString, currentIngredientCodeList, Ingredient.DELETE_SW,
								new PageRequest(page, pageSize - ingredients.size(), sort)));
			}
		}

		return ingredients;
	}


	/**
	 * Set up an ingredient to be sent to the repository to update.
	 *
	 * @param ingredient Ingredient to be updated.
	 * @return Updated ingredient.
	 */
	@CoreTransactional
	public Ingredient update(Ingredient ingredient) {

		// Validate the description is not empty.
		if (ingredient.getIngredientDescription() == null || ingredient.getIngredientDescription().isEmpty()) {
			IngredientService.logger.error(IngredientService.INVALID_DESCRIPTION_ERROR);
			throw new IllegalArgumentException(IngredientService.INVALID_DESCRIPTION_ERROR);
		}

		// Get the current ingredient from the DB.
		Ingredient currentIngredient = this.ingredientRepository.findOne(ingredient.getIngredientCode());

		if (currentIngredient == null) {
			String message = String.format(IngredientService.INVALID_INGREDIENT_ERROR, ingredient.getIngredientCode());
			IngredientService.logger.error(message);
			throw new IllegalArgumentException(message);
		}

		// Deal with the sub-ingredients first in case you need to flush the entity manager.

		// If the old has sub-ingredients, clear them out (we'll add the new ones in the next step.
		if (!currentIngredient.getIngredientSubs().isEmpty()) {
			currentIngredient.getIngredientSubs().clear();
			// Have to flush so that we can write the new ones.
			this.entityManager.flush(); 
		}

		// If the new list is not empty, then add them.
		if (!ingredient.getIngredientSubs().isEmpty()) {
			this.addSubIngredients(currentIngredient, ingredient.getIngredientSubs());
		}

		// Copy the ingredient level stuff.

		currentIngredient.setCategoryCode(ingredient.getIngredientCategory().getCategoryCode());
		currentIngredient.setIngredientCategory(ingredient.getIngredientCategory());
		currentIngredient.setIngredientDescription(ingredient.getIngredientDescription());

		// If the code is an A, that means it hasn't been added to the scales system yet so it needs to stay an 'A'.
		if(!currentIngredient.getMaintFunction().trim().equalsIgnoreCase(IngredientService.ADD_MAINT_FUNCTION)) {
			currentIngredient.setMaintFunction(IngredientService.UPDATE_MAINT_FUNCTION);
		}

		// If the extended description is empty, blank it out.
		if(StringUtils.isEmpty(ingredient.getIngredientCatDescription())){
			currentIngredient.setIngredientCatDescription(StringUtils.EMPTY);
		} else {
			currentIngredient.setIngredientCatDescription(ingredient.getIngredientCatDescription());
		}

		// Set the SOI flag to true if there are now sub-ingredients and false otherwise.
		if(ingredient.getIngredientSubs().isEmpty()){
			currentIngredient.setSoiFlag(false);
		} else {
			currentIngredient.setSoiFlag(true);
		}

		//PM-848 - Allow sub-ingredients that are already sub-ingredients of other sub-ingredients.
		//Validate the infinite loops in sub-ingredients
		List<String> newSubIngredients = currentIngredient.getIngredientSubs().stream().map(sub -> sub
				.getSubIngredient().getIngredientCode()).collect(Collectors.toList());

		if(this.validateHavingInfiniteLoopsInSubIngredients(ingredient.getIngredientCode(), newSubIngredients)){
			throw new IllegalArgumentException(IngredientService.INVALID_INGREDIENT_ADD_MESSAGE);
		}

		//Update Ingredient
		this.updateStatementsForIngredient(ingredient);
		return this.ingredientRepository.save(currentIngredient);
	}

	/**
	 * Helper method that will copy sub-ingredients into an ingredient.
	 *
	 * @param ingredient The ingredient to copy the sub-ingredients into.
	 * @param subsIngredients The sub-ingredients to copy.
	 */
	private void addSubIngredients(Ingredient ingredient, Iterable<IngredientSub> subsIngredients) {

		long soiSequence = DEFAULT_STARTING_SEQUENCE_NUMBER;
		for (IngredientSub subsIngredient : subsIngredients) {
			// Look up the real sub-ingredient from the DB
			Ingredient i = this.ingredientRepository.findOne(subsIngredient.getSubIngredient().getIngredientCode());
			// Make a new IngredientSub with it. If you don't do it this way, then you don't have all the
			// right objects after the save.
			IngredientSub is = new IngredientSub();
			// It'll copy the values to the key on the save.
			//is.setKey(new IngredientSubKey());
			is.setKey(subsIngredient.getKey()); // db2oracle fix vn00907
			is.setIngredientMaster(ingredient);
			is.setSubIngredient(i);
			is.setSoiSequence(soiSequence++);
			ingredient.getIngredientSubs().add(is);
		}
		
	}
	/**
	 * Sets the maintenance switches for all ingredient statements that an ingredient is attached to.
	 *
	 * @param ingredient The ingredient to update the statements of.
	 */
	@CoreTransactional
	private void updateStatementsForIngredient(Ingredient ingredient) {

		// This list will contain all the ingredient statement headers that
		// need to be updated.
		List<Long> headerIds = new ArrayList<>();
		this.findAllStatementsContainingIngredient(ingredient, headerIds);

		// Find all the ingredient statement headers that came from the above list.
		List<IngredientStatementHeader> headers =
				this.ingredientStatementHeaderRepository.findAll(headerIds);

		List<IngredientStatementHeader> headersToUpdate = new ArrayList<>();

		List<Long> ingredientStatementNumberChangeList = new ArrayList<>();
		for (IngredientStatementHeader header : headers) {

			// If it isn't already marked for deletion or add, then update it.
			if (header.getMaintenanceCode() != IngredientStatementHeader.DELETE_CODE &&
					header.getMaintenanceCode() != IngredientStatementHeader.ADD_CODE) {
				header.setMaintenanceDate(LocalDate.now());
				header.setMaintenanceCode(IngredientStatementHeader.UPDATE_CODE);
				header.setMaintenanceSwitch(true);
				headersToUpdate.add(header);
				ingredientStatementNumberChangeList.add(header.getStatementNumber());
			}
		}
		this.ingredientStatementHeaderRepository.save(headersToUpdate);
		IngredientStatementEvent ingredientStatementEvent = new IngredientStatementEvent(this,null, ingredientStatementNumberChangeList, this.userInfo.getUserId());
		applicationEventPublisher.publishEvent(ingredientStatementEvent);
	}

	/**
	 * Removes an ingredient.
	 * @param ingredient Ingredient to be removed.
	 */
	public void delete(Ingredient ingredient) {

		// Get the list of ingredients where this ingredient is a sub-ingredient.
		List<IngredientSub> ingredientAsSub =
				this.ingredientSubRepository.findByKeyIngredientCode(ingredient.getIngredientCode());

		// Get the list of ingredient statement details where this ingredient is on the statement.
		List<IngredientStatementDetail> ingredientAsDetailSub =
				this.ingredientStatementDetailRepository.findByKeyIngredientCode(ingredient.getIngredientCode());

		// If this ingredient is in neither of those lists, then it is safe to delete it.
		if((ingredientAsSub == null || ingredientAsSub.size() == 0) &&
				(ingredientAsDetailSub == null || ingredientAsDetailSub.size() == 0)) {
			Ingredient ingredientToRemove = this.ingredientRepository.findOne(ingredient.getIngredientCode());
			ingredientToRemove.getIngredientSubs().removeAll(ingredient.getIngredientSubs());
			// If the code is an A, that means it hasn't been added to the scales system yet
			// so it can be deleted completely.
			if(ingredientToRemove.getMaintFunction().trim().equalsIgnoreCase(ADD_MAINT_FUNCTION)) {
				this.ingredientRepository.delete(ingredientToRemove);
			} else {
				ingredientToRemove.setMaintFunction(DELETE_MAINT_FUNCTION);
				this.ingredientRepository.save(ingredientToRemove);
			}
		} else {
			// If the ingredient is part of the lists from above, return an error message that it cannot be removed.
			String failMessage = String.format(INGREDIENT_REMOVE_FAIL_MESSAGE, ingredient.getIngredientCode());
			Set<String> masterIngredients;
			if(ingredientAsSub != null && ingredientAsSub.size() > 0 &&
					ingredientAsDetailSub != null && ingredientAsDetailSub.size() > 0){
				// ingredient exists as sub-ingredient and on ingredient statement
				masterIngredients = ingredientAsSub.stream().map
						(sub -> sub.getKey().getSoIngredientCode()).collect(Collectors.toSet());
				failMessage = failMessage.concat(String.format(INGREDIENT_CODES, masterIngredients));
				masterIngredients = ingredientAsDetailSub.stream().map
						(sub -> String.valueOf(sub.getKey().getStatementNumber())).collect(Collectors.toSet());
				failMessage = failMessage.concat(String.format(AND_INGREDIENT_STATEMENT_CODES, masterIngredients));
			} else if(ingredientAsSub != null && ingredientAsSub.size() > 0) {
				// ingredient exists as sub-ingredient
				masterIngredients = ingredientAsSub.stream().map
						(sub -> sub.getKey().getSoIngredientCode()).collect(Collectors.toSet());
				failMessage = failMessage.concat(String.format(INGREDIENT_CODES, masterIngredients));
			} else if(ingredientAsDetailSub != null && ingredientAsDetailSub.size() > 0){
				// ingredient exists on ingredient statement
				masterIngredients = ingredientAsDetailSub.stream().map
						(sub -> String.valueOf(sub.getKey().getStatementNumber())).collect(Collectors.toSet());
				failMessage = failMessage.concat(String.format(INGREDIENT_STATEMENT_CODES, masterIngredients));
			}
			failMessage = failMessage.concat(".");
			throw new IllegalArgumentException(failMessage);
		}
	}

	/**
	 * Find ingredient by ingredient statement number ingredient statement header.
	 *
	 * @param ingredientStatement the ingredient statement
	 * @return the ingredient statement header
	 */
	public IngredientStatementHeader findIngredientByIngredientStatementNumber(long ingredientStatement) {

		return this.ingredientStatementHeaderRepository.findOne(ingredientStatement);
	}

	/**
	 * Find all pageable result.
	 *
	 * @param categoryCode    the category code
	 * @param includeCategory the include category
	 * @param includeCount    the include count
	 * @param page            the page
	 * @param pageSize        the page size
	 * @return the pageable result
	 */
	public PageableResult<Ingredient> findAll(Long categoryCode, IngredientController.IncludeCategory includeCategory,
											  boolean includeCount, int page, int pageSize) {
		Pageable request = new PageRequest(page, pageSize);

		return includeCount ? this.findAllWithCount(categoryCode, includeCategory, request) :
				this.findAllWithoutCount(categoryCode, includeCategory, request);
	}

	/**
	 * Finds all ingredients without count
	 * @param categoryCode category to filter on
	 * @param includeCategory whether or not to include category
	 * @param request request for data
	 * @return Page of ingredients
	 */
	private PageableResult<Ingredient> findAllWithoutCount(Long categoryCode,
														   IngredientController.IncludeCategory includeCategory,
														   Pageable request) {
		List<Ingredient> data;

		switch (includeCategory){
			case INCLUDE: data = this.ingredientRepository.findByCategoryCode(categoryCode, request);
				break;
			case EXCLUDE: data = this.ingredientRepository.findByCategoryCodeNot(categoryCode, request);
				break;
			default:  data = this.ingredientRepository.findByMaintFunctionNot(Ingredient.DELETE_SW, request);
				break;
		}

		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Finds all ingredients with count
	 * @param categoryCode category to filter on
	 * @param includeCategory whether or not to include category
	 * @param request request for data
	 * @return Page of ingredients
	 */
	private PageableResult<Ingredient> findAllWithCount(Long categoryCode,
														IngredientController.IncludeCategory includeCategory,
														Pageable request) {
		Page<Ingredient> data;

		switch (includeCategory){
			case INCLUDE: data = this.ingredientRepositoryWithCount.findByCategoryCode(categoryCode, request);
				break;
			case EXCLUDE: data = this.ingredientRepositoryWithCount.findByCategoryCodeNot(categoryCode, request);
				break;
			default:  data = this.ingredientRepositoryWithCount.findByMaintFunctionNot(Ingredient.DELETE_SW, request);
				break;
		}

		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Determines whether list contains an Ingredient with matching ingredient code already. This is used to prevent
	 * user adding the same ingredient more than once as a sub-ingredient.
	 * @param ingredientSubs list to search through
	 * @param ingredientCode ingredient code to search for
	 * @return boolean
	 */
	private boolean listAlreadyContainsIngredientCode(final List<IngredientSub> ingredientSubs,
													  final String ingredientCode){
		for(IngredientSub sub : ingredientSubs){
			if(sub.getKey().getIngredientCode().equals(ingredientCode)){
				return true;
			}
		}
		return false;
	}

	/**
	 * If a sub-ingredient is added that already exists as an ingredient master of it or a sub-ingredient of an
	 * already existing sub-ingredient, this would create an endless loop. This function finds all ingredients which
	 * match this criteria so that a user cannot add these ingredients as sub-ingredients.
	 *
	 * @param currentIngredientCode The current Ingredient code.
	 * @param subIngredients The list of sub ingredient of current ingredient.
	 * @return status of infinite loops in sub-ingredients
	 */
	private boolean validateHavingInfiniteLoopsInSubIngredients(String currentIngredientCode, List<String> subIngredients){
		boolean loopStatus = false;
		List<String> allParentIngredients = new ArrayList<String>();
		//Get list of parent ingredient by current ingredient. If sub ingredient contain parent of it, that mean the
		// infinite loops in sub-ingredients, break and inform to the user.
		List<String> tempList = new ArrayList<>(Collections.singletonList(currentIngredientCode));
		List<IngredientSub> subs;
		do{
			subs = this.ingredientSubRepository.findByKeyIngredientCodeIn(tempList);
			tempList = new ArrayList<>();
			for(IngredientSub sub : subs){
				tempList.add(sub.getKey().getSoIngredientCode());
				if(subIngredients.contains(StringUtils.trim(sub.getKey().getSoIngredientCode()))){
					loopStatus = true;
					break;
				}
			}
			if(tempList.contains(currentIngredientCode)){
				loopStatus = true;
			}
			allParentIngredients.addAll(tempList);
		} while (tempList.size() > 0 && !loopStatus);
		//Get list of sub of sub ingredient by current ingredient to lowest level. If sub of sub ingredient point to
		// parent of it, that mean the infinite loops in sub-ingredients, break and inform to the user.
		if(!loopStatus){
			tempList = subIngredients;
			do{
				subs = this.ingredientSubRepository.findByKeySoIngredientCodeIn(tempList);
				tempList = new ArrayList<>();
				for(IngredientSub sub : subs){
					tempList.add(sub.getKey().getIngredientCode());
					if(allParentIngredients.contains(StringUtils.trim(sub.getKey().getIngredientCode()))){
						loopStatus = true;
						break;
					}
				}
				if(tempList.contains(currentIngredientCode)){
					loopStatus = true;
				}
			} while (tempList.size() > 0 && !loopStatus);
		}
		return loopStatus;
	}

	/**
	 * This method finds the next available ingredient code and sends it back to the user.
	 *
	 * @param searchString Ingredient code to start at.
	 * @param page The page to get.
	 * @param pageSize The size of the page.
	 * @return List of ingredients found.
	 */
	public List<String> findByNextIngredientCode(Long searchString, int page, int pageSize) {
		boolean isFound = false;
		long ingredientCode = searchString;
		if(ingredientCode <= 0){
			ingredientCode = LOWESET_ALLOWED_INGREDIENT_CODE;
		}
		Pageable request = new PageRequest(page, pageSize);

		List<Ingredient> ingredients;
		do{
			if(ingredientCode > MAX_INGREDIENT_CODE){
				throw new IllegalArgumentException(INGREDIENT_CODE_MAX_FAIL_MESSAGE);
			}
			ingredients =
					this.ingredientRepository.findFirst100ByIngredientCodeGreaterThanEqual(ingredientCode, request);
			if(ingredients.isEmpty()){
				break;
			}
			for (Ingredient ingredient : ingredients) {
				if(!ingredient.getIngredientCode().equals(String.valueOf(ingredientCode))){
					isFound = true;
					break;
				} else {
					ingredientCode++;
				}
			}
		} while(!isFound);
		return new ArrayList<>(Collections.singletonList(String.valueOf(ingredientCode)));
	}

	/**
	 * Accumulates a list of ingredient statement header IDs that contain an ingredient. The ingredient can exist
	 * anywhere in the list of ingredients on the statement including sub-ingredients of sub-ingredients.
	 *
	 * @param ingredient The ingredient to look for.
	 * @param ingredientStatementNumbers A list of ingredient statment IDs that this method will populate.
	 */
	private void findAllStatementsContainingIngredient(Ingredient ingredient, List<Long> ingredientStatementNumbers) {

		// Start by getting any ingredient statements that this ingredient is part of.
		List<IngredientStatementDetail> headers =
				this.ingredientStatementDetailRepository.findByKeyIngredientCode(ingredient.getIngredientCode());

		// Add those to the list.
		headers.forEach((h) -> ingredientStatementNumbers.add(h.getKey().getStatementNumber()));

		// See if this ingredient is a sub-ingredient of any other ingredients.
		List<IngredientSub> superIngredients =
				this.ingredientSubRepository.findByKeyIngredientCode(ingredient.getIngredientCode());

		// For each ingredient this one is a sub of, recurse over that one.
		for (IngredientSub superIngredient : superIngredients) {
			this.findAllStatementsContainingIngredient(superIngredient.getIngredientMaster(),
					ingredientStatementNumbers);
		}
	}
}
