package com.heb.pm.productSearch;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.CoreTransactional;
import com.heb.pm.Hits;
import com.heb.pm.HitsBase;
import com.heb.pm.entity.*;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.product.ProductInfoService;
import com.heb.pm.productDetails.casePack.VendorInfoService;
import com.heb.pm.productDetails.product.OnlineAttributes.OnlineAttributesService;
import com.heb.pm.productDetails.product.ProductInformationService;
import com.heb.pm.productDetails.product.ShelfAttributesService;
import com.heb.pm.productDetails.product.eCommerceView.ProductECommerceViewService;
import com.heb.pm.productDetails.product.eCommerceView.ProductECommerceViewUtil;
import com.heb.pm.productDetails.product.eCommerceView.ProductECommerceViewService.LogicAttributeCode;
import com.heb.pm.productDetails.product.eCommerceView.ProductECommerceViewService.SourceSystemNumber;
import com.heb.pm.productSearch.predicateBuilders.ExistsClause;
import com.heb.pm.productSearch.predicateBuilders.PredicateBuilderList;
import com.heb.pm.repository.*;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;

/**
 * Contains the business logic related to searching for products.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
public class ProductSearchService {

	private static final Logger logger = LoggerFactory.getLogger(ProductSearchService.class);

	private static final int EXPORT_PAGE_SIZE = 100;
	private static final String CSV_SEPARATOR = ",";

	private static final String EXPORT_HEADER_BEGIN_COLS = "\"UPC\",\"Item Code\",\"Channel\",\"Product ID\"," +
			"\"Product Description\",\"Product Size\",\"UPC Size\",\"Sub Commodity\",\"BDM\",\"Sub Department\"," +
			"\"Product Primary UPC\"";
	private static final String EXPORT_HEADER_END_COLS = "\"Last Modified On\",\"Last Modified By\"";
	private static final String EXPORT_HEADER_PRIMO_PICK = "\"Primo Pick\",\"Distintive\",\"Primo Pick Story\",\"Primo Pick Shelf Tag\",\"Primo Pick Status\",\"Go Local\"";
	private static final String EXPORT_HEADER_TAX_FLAG = "\"Tax Flag\"";
	private static final String EXPORT_HEADER_FOOD_STAMP_FLAG = "\"Food Stamp Flag\"";
	private static final String EXPORT_HEADER_SERVICE_CASE_ATTRIBUTES = 
			"\"Tag Effective Date\",\"Tag Size\",\"Tag Item Code\",\"English Customer Friendly Desc-1\",\"English Customer Friendly Desc-2\"," +
			"\"Service Case Status\",\"Proposed Servicecase Sign Description (110 chars)\",\"Approved Servicecase Sign Description (110 chars)\",\"Tag Type\"";
	private static final String EXPORT_HEADER_SERVICE_CASE_ATTRIBUTES_WITH_PRIMO_PICK = 
			"\"Primo Pick\",\"Distintive\",\"Primo Pick Story\",\"Primo Pick Shelf Tag\",\"Go Local\",\"Tag Effective Date\"," +
			"\"Tag Size\",\"Tag Item Code\",\"English Customer Friendly Desc-1\",\"English Customer Friendly Desc-2\",\"Service Case Status\"," +
			"\"Proposed Servicecase Sign Description (110 chars)\",\"Approved Servicecase Sign Description (110 chars)\",\"Tag Type\"";
	private static final String EXPORT_HEADER_ECOMMERCE = 
			"\"Commodity\",\"Dept\",\"eBM\",\"Brand\",\"Online Brand\",\"Online Display Name\",\"Online Size\",\"Online Romance Copy\"," + 
			"\"Online Ingredients\",\"Online Directions\",\"Online Warnings\",\"Pub'd on heb.com\",\"Pub'd on h2u\"," +
			"\"hebcom Lowest Level ID\",\"h2u Lowest Level ID\",\"heb.com Hierarchy\",\"hebtoyou Hierarchy\",\"Show on Site hebcom\",\"Show on Site heb2u\",\"Fulfillment Channel\",\"3rd Party\",\"SSA Count\",\"AP Vendor\",\"Purchasing Status\"";

	private static final String EXPORT_ROW_FORMAT_BEGIN_COLS = "%d,%d,\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%d";
	private static final String EXPORT_ROW_FORMAT_END_COLS = "%tD,\"%s\"";
	private static final String EXPORT_FORMAT_PROM_PICK = "\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"";
	private static final String EXPORT_FORMAT_SERVICE_CASE_ATTRIBUTES_WITH_PRIMO_PICK = "\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%tD,\"%s\",%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"";
	private static final String EXPORT_FORMAT_SERVICE_CASE_ATTRIBUTES = "%tD,\"%s\",%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"";
	private static final String EXPORT_FORMAT_ECOMMERCE = "\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%s,%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"";
	private static final String STRING_Y = "Y";
	private static final String STRING_N = "N";

	private static final String EXPORT_COL_PRIMO_PICK = "PRIMO_PICK";
	private static final String EXPORT_COL_TAX_FLAG = "TAX_FLAG";
	private static final String EXPORT_COL_FOOD_STAMP_FLAG = "FOOD_STAMP_FLAG";
	private static final String EXPORT_COL_SERVICE_CASE_ATTRIBUTES = "SERVICE_CASE_ATTRIBUTES";
	private static final String EXPORT_COL_ECOMMERCE = "ECOMMERCE";

	private static final String USER_NAME_FORMAT =  "%s %s [%s]";

	private static final String PRIMO_PICK_YES = "Y";
	private static final String PRIMO_PICK_NO = "N";
	private static final String DISTINCTIVE_YES = "Y";
	private static final String DISTINCTIVE_NO = "N";
	private static final String EXP_PRIMO_PICK_STATUS_APPROVED = "APPROVED [A]";
	private static final String EXP_PRIMO_PICK_STATUS_SUBMITTED = "SUBMITTED [S]";
	private static final String EXP_PRIMO_PICK_STATUS_REJECTED = "REJECTED [R]";
	private static final String DOUBLE_QUOTES_FORMAT = "\"";
	private static final String ESCAPED_DOUBLE_QUOTES_FORMAT = "\"\"";

	private static final String COMMA_CHARACTER = ", ";
	private static final String PURCHASING_STATUS_SEPARATOR = "/ ";

	public enum ServiceCaseSignCode {
		REJECTED("105", "Rejected"),
		APPROVED("108", "Approved"),
		SUBMITTED("111", "Submitted");

		private String code;
		private String name;

		ServiceCaseSignCode(String code, String name) {
			this.code = code;
			this.name = name;
		}

		/**
		 * Gets code.
		 *
		 * @return the code
		 */
		public String getCode() {
			return this.code;
		}

		/**
		 * Gets name.
		 *
		 * @return the name
		 */
		public String getName() {
			return this.name;
		}
	}

	@Autowired
	private ProductInfoResolver productInfoResolver;

	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	@Autowired
	private PredicateBuilderList predicateBuilderList;

	@Autowired
	private LdapSearchRepository ldapSearchRepository;

	@Autowired
	private SearchCriteriaRepository searchCriteriaRepository;

	@Autowired
	private ProductInfoRepository productInfoRepository;

	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	@Autowired
	private ItemMasterRepository itemMasterRepository;

	@Autowired
	private ItemClassRepository itemClassRepository;

	@Autowired
	private ClassCommodityRepository classCommodityRepository;

	@Autowired
	private SubCommodityRepository subCommodityRepository;

	@Autowired
	private ProductInfoRepositoryWithCounts productInfoRepositoryWithCounts;
	
	@Autowired
	private CandidateWorkRequestRepository candidateWorkRequestRepository;
	
	@Autowired
	private ProductAttributeOverwriteRepository productAttributeOverwriteRepository;
	
	@Autowired
        private TargetSystemAttributePriorityRepository targetSystemAttributePriorityRepository;

       @Autowired
	private ProductFullfilmentChanelRepository productFullfilmentChanelRepository;

	@Autowired
	private OnlineAttributesService onlineAttributesService;

	@Autowired
	private ProductInformationService productInformationService;

	@Autowired
	private ProductOnlineRepository productOnlineRepository;

	/**
	 * Streams a list of products to the front end. It will use the search criteria to page through all search results
	 * and write it out to the servlet output stream. Since this streams the result, the browser will not time out
	 * as it is continually receiving results.
	 *
	 * @param printWriter The print writer to write the export to.
	 * @param productSearchCriteria The search criteria to use to find the products to export.
	 * @param additionalColumns selected export options or additional columns.
	 */
	@CoreTransactional
	public void streamProductSearch(PrintWriter printWriter, ProductSearchCriteria productSearchCriteria,
									List<String> additionalColumns) {

		boolean hadData;
		boolean hadPrimoPick = additionalColumns != null ? additionalColumns.contains(EXPORT_COL_PRIMO_PICK) : false;
		boolean hadEbm = additionalColumns != null ? additionalColumns.contains(EXPORT_COL_ECOMMERCE) : false;
		
		List<TargetSystemAttributePriority> targetSystemAttributePriorities = this.targetSystemAttributePriorityRepository.findAll(
				TargetSystemAttributePriority.getAttributePrioritySort());
		
		int currentPage = 0;

		Map<String, String> userIdMap = new HashMap<>();

		PageableResult<ProductMaster> productData;

		// Print the header of the file.
		try {
			printWriter.println(buildExportHeader(additionalColumns));
		} catch (Exception e) {
			ProductSearchService.logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}

		// Keep looping till we get nothing back from the search results.
		do {
			hadData = false;

			if (currentPage % 5 == 0) {
				System.gc();
			}
			
			// Search for the next page of data.
			productData = this.searchForProducts(productSearchCriteria, currentPage,
					ProductSearchService.EXPORT_PAGE_SIZE, false);

			// First load the userIds into the map if we don't already have it.
			this.lookupUsers(userIdMap, productData.getData(), hadEbm);
			resolveExportResults(productData.getData(), additionalColumns);
			// Loop through all the data and write it to the output stream.
			for (ProductMaster pm : productData.getData()) {

				hadData = true;

				try {

					if (CollectionUtils.isEmpty(pm.getProdItems())) {
						printWriter.println(String.format("Product %d not tied to items", pm.getProdId()));
					} else {
						if (hadEbm && pm.getClassCommodity() != null && pm.getClassCommodity().geteBMid() != null) {
							String ebmName = userIdMap.get(StringUtils.trimToEmpty(pm.getClassCommodity().geteBMid()));
							pm.setEbmName(ebmName);
						}
						// Loop through all the items tied to the product.
						for (ProdItem prodItem : pm.getProdItems()) {
							try {
								this.validateItems(prodItem);
								// Loop through all the UPCs tied to the item.
								if(prodItem.getItemMaster() != null
										&& prodItem.getItemMaster().getPrimaryUpc() != null
										&& prodItem.getItemMaster().getPrimaryUpc().getAssociateUpcs() != null) {
									for (AssociatedUpc associatedUpc : prodItem.getItemMaster().getPrimaryUpc().getAssociateUpcs()) {
										String userName = userIdMap.get(pm.getLastUpdateUserId().trim());
										/** Write the product (at the UPC level) to the output stream. -- Starts**/
										//1. Build - Begins with Columns
										String regularBeginColumns = this.exportRegular(pm, prodItem, associatedUpc);
										StringBuilder dataRowBuilder = new StringBuilder(regularBeginColumns);
										//2. Build - Export Options or additional columns
										if(additionalColumns != null && !additionalColumns.isEmpty()){
											Iterator<String> selectedColumnsIterator = additionalColumns.iterator();
											do {
												switch (selectedColumnsIterator.next()) {
												case EXPORT_COL_PRIMO_PICK:
													dataRowBuilder.append(CSV_SEPARATOR).append(this.exportPrimoPick(pm));
													break;
												case EXPORT_COL_TAX_FLAG:
													if(pm.getGoodsProduct() != null && pm.getGoodsProduct().getRetailTaxSwitch() != null){
														dataRowBuilder.append(CSV_SEPARATOR).append(pm.getGoodsProduct().getRetailTaxSwitch()?STRING_Y:STRING_N);
													}else{
														dataRowBuilder.append(CSV_SEPARATOR).append(STRING_N);
													}
													break;
												case EXPORT_COL_FOOD_STAMP_FLAG:
													if(pm.getGoodsProduct() != null && pm.getGoodsProduct().getFoodStampSwitch() != null){
														String foodStampFlag = pm.getGoodsProduct().getFoodStampSwitch() ? STRING_Y : STRING_N;
														dataRowBuilder.append(CSV_SEPARATOR).append(foodStampFlag);
													}else{
														dataRowBuilder.append(CSV_SEPARATOR).append(STRING_N);
													}
													break;
												case EXPORT_COL_SERVICE_CASE_ATTRIBUTES:
													dataRowBuilder.append(CSV_SEPARATOR).append(this.exportServiceCaseAttributes(pm, hadPrimoPick));
													break;
												case EXPORT_COL_ECOMMERCE:
													dataRowBuilder.append(CSV_SEPARATOR).append(this.exportEcommerce(pm, prodItem, targetSystemAttributePriorities));
													break;
												default:
													break;
												}
											} while (selectedColumnsIterator.hasNext());
										}
										//3. Build - Ends with columns.
										String regularEndColumns = String.format(ProductSearchService.EXPORT_ROW_FORMAT_END_COLS,
												pm.getLastUpdateTime(),userName);
										dataRowBuilder.append(CSV_SEPARATOR).append(regularEndColumns);
										printWriter.println(dataRowBuilder.toString());
										/** Write the product (at the UPC level) to the output stream. -- Ends**/
									}
								}
							} catch (IllegalArgumentException | EntityNotFoundException e) {
								ProductSearchService.logger.error(e.getMessage());
								printWriter.println(e.getMessage());
							}
						}
					}

				} catch (Exception e) {
					ProductSearchService.logger.error(e.getMessage());
					throw new StreamingExportException(e.getMessage(), e.getCause());
				}
				entityManager.clear();
			}
			currentPage++;
		} while (hadData);
	}

	/**
	 * Used to build data row for regular type.
	 * @param productMaster The product master.
	 * @param prodItem The product item.
	 * @param associatedUpc The associated upc.
	 * @return a CSV string.
	 */
	private String exportRegular(ProductMaster productMaster, ProdItem prodItem, AssociatedUpc associatedUpc) {
		String productSize = StringUtils.EMPTY;
		String upcSize = StringUtils.EMPTY;
		String subCommodity = StringUtils.EMPTY;
		String classCommodity = StringUtils.EMPTY;
		String subDepartment = StringUtils.EMPTY;
		
		if (productMaster.getProductPrimarySellingUnit() != null) {
			productSize = StringUtils.trimToEmpty(productMaster.getProductPrimarySellingUnit().getTagSize());
		}
		if (associatedUpc.getSellingUnit() != null) {
			upcSize = StringUtils.trimToEmpty(associatedUpc.getSellingUnit().getTagSize());
		}
		if (productMaster.getSubCommodity() != null) {
			subCommodity = StringUtils.trimToEmpty(productMaster.getSubCommodity().getDisplayName());
		}
		if (productMaster.getClassCommodity() != null && productMaster.getClassCommodity().getBdm() != null) {
			classCommodity = StringUtils.trimToEmpty(productMaster.getClassCommodity().getBdm().getDisplayName());
		}
		if (productMaster.getSubDepartment() != null) {
			subDepartment = StringUtils.trimToEmpty(productMaster.getSubDepartment().getDisplayName());
		}
		
		return String.format(ProductSearchService.EXPORT_ROW_FORMAT_BEGIN_COLS,
				associatedUpc.getUpc(),
				prodItem.getKey().getItemCode(),
				prodItem.getKey().getItemType().equals(ItemMasterKey.WAREHOUSE) ? "WHS" : "DSD",
				productMaster.getProdId(),
				this.formatCsvData(productMaster.getDescription()),
				this.formatCsvData(productSize),
				this.formatCsvData(upcSize),
				subCommodity,
				classCommodity,
				subDepartment,
				productMaster.getProductPrimaryScanCodeId());
	}

	/**
	 * Escaped the double-quotes with another double quote (RFC4180). Refer to https://tools.ietf.org/html/rfc4180.
	 * @param value The string will be formatted.
	 * @return a formatted string.
	 */
	private String formatCsvData(String value) {
		String result = value;
		if (result != null && result.contains(DOUBLE_QUOTES_FORMAT)) {
			result = result.replace(DOUBLE_QUOTES_FORMAT, ESCAPED_DOUBLE_QUOTES_FORMAT);
		}
		return result;
	}

	/**
	 * Checks the data tied to the product to make sure it'll export correctly. Throws an IllegalArgumetException
	 * if the product is not setup correctly.
	 *
	 * @param prodItem The product to validate.
	 */
	private void validateItems(ProdItem prodItem) {
		if (prodItem.getItemMaster() == null) {
			throw new IllegalArgumentException(
					String.format("Item code %d does not exist", prodItem.getKey().getItemCode()));
		}
		if (prodItem.getItemMaster().getPrimaryUpc() == null) {
			throw new IllegalArgumentException(
					String.format(String.format("Item code %d not tied to primary UPC",
							prodItem.getKey().getItemCode())));
		}
		if (prodItem.getItemMaster().getPrimaryUpc().getAssociateUpcs() == null) {
			throw new IllegalArgumentException(
					String.format(String.format("Primary UPC %d not tied to associates",
							prodItem.getItemMaster().getPrimaryUpc().getUpc())));
		}
	}

	/**
	 * Loops through a list of products and adds the name of the user to a map with the one-pass ID as the
	 * key and the name as the value.
	 *
	 * @param userIdMap The map to add user IDs to.
	 * @param data The list of products to go through.
	 * @param hadEbm The ebm user.
	 */
	private void lookupUsers(Map<String, String> userIdMap, Iterable<ProductMaster> data, boolean hadEbm) {

		Set<String> usersToLookUp = new HashSet<>();

		// Extract out the list of users from this set of data that we don't already have details for.
		for (ProductMaster pm : data) {
			if (pm.getLastUpdateUserId() != null) {

				String trimmedUserId = pm.getLastUpdateUserId().trim();

				// If they're already looked up, no need to do so again.
				if (!trimmedUserId.isEmpty() && userIdMap.get(trimmedUserId) == null) {
					usersToLookUp.add(trimmedUserId);
				}
			}
			if (hadEbm && pm.getClassCommodity() != null && pm.getClassCommodity().geteBMid() != null) {
				
				String trimmedUserId = pm.getClassCommodity().geteBMid().trim();
				
				// If they're already looked up, no need to do so again.
				if (!trimmedUserId.isEmpty() && userIdMap.get(trimmedUserId) == null) {
					usersToLookUp.add(trimmedUserId);
				}
			}
		}

		if (!usersToLookUp.isEmpty()) {
			try {
				// Look up the users and add the ones that came back to the hash table.
				List<User> users = this.ldapSearchRepository.getUserList(usersToLookUp);

				// For each user we found, add it to the map. Remove it from the usersToLookUp.
				for (User u : users) {
					userIdMap.put(u.getUid(), String.format(ProductSearchService.USER_NAME_FORMAT,
							u.getGivenName().trim(), u.getLastName(), u.getUid()));
					usersToLookUp.remove(u.getUid());
				}

				// Whatever is left in the usersToLookUp can't be found in LDAP, so just add the user ID as the name.
				usersToLookUp.forEach((u) -> userIdMap.put(u, u));

			} catch (Exception e) {
				ProductSearchService.logger.error(e.getMessage());
			}
		}
	}



	/**
	 * A flexible function that will search for a list of ProductMasters that match a set of criteria. In order to
	 * add more types of criteria, you must create a class that extends PredicateBuilder and add that class to the
	 * PredicateBuilderList class.
	 *
	 * @param searchCriteria The user's search criteria.  Note that this method ignores the excluded products. Those
	 *                       will have to be excluded by the calling function.
	 * @param page The page of data being requested.
	 * @param pageSize The maximum number of records to return.
	 * @param firstSearch Whether or not this is the first search for this criteria. This will cause the function
	 *                    to return product counts.
	 * @return A pageable result with the data and (optionally) the total number of records.
	 */
	@CoreTransactional
	public PageableResult<ProductMaster> searchForProducts(ProductSearchCriteria searchCriteria,
														   int page, int pageSize, boolean firstSearch) {

		this.validateSearchCriteria(searchCriteria);

		this.loadTempTable(searchCriteria, firstSearch);

		// Get the objects needed to build the query.
		CriteriaBuilder criteriaBuilder =  this.entityManager.getCriteriaBuilder();

		// Builds the criteria for the main query
		CriteriaQuery<ProductMaster> queryBuilder = criteriaBuilder.createQuery(ProductMaster.class);

		// The bind variable the query builders will use to tie to session ID.
		ParameterExpression<String> sessionIdBindVariable = criteriaBuilder.parameter(String.class);

		// Select from product master.
		Root<ProductMaster> pmRoot = queryBuilder.from(ProductMaster.class);

		// Build the where clause
		Predicate[] whereClause = this.getWhereClause(criteriaBuilder, pmRoot, queryBuilder, searchCriteria,
				sessionIdBindVariable);
		queryBuilder.where(criteriaBuilder.and(whereClause));

		// Add the sort
		queryBuilder.orderBy(this.getSort(criteriaBuilder, pmRoot));

		// Get the query
		TypedQuery<ProductMaster> pmTQuery = this.entityManager.createQuery(queryBuilder);

		// Sets the first row to grab and the maximum number to grab for pagination.
		pmTQuery.setFirstResult(page * pageSize).setMaxResults(pageSize);

		// Bind the session ID to the parameter from above.
		if (!pmTQuery.getParameters().isEmpty()) {
			pmTQuery.setParameter(sessionIdBindVariable, searchCriteria.getSessionId());
		}

		// Execute the query.
		List<ProductMaster> results = pmTQuery.getResultList();

		// If the user requested counts, build and execute that query.
		if (firstSearch) {
			long count;
			int pageCount;

			// It's a new query
			CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
			// Same from and where, just wrapping a count around it.
			countQuery.select(criteriaBuilder.count(countQuery.from(ProductMaster.class)));
			countQuery.where(whereClause);

			// Run the query
			TypedQuery<Long> countTQuery = this.entityManager.createQuery(countQuery);

			// Bind the session ID parameter.
			if (!countTQuery.getParameters().isEmpty()) {
				countTQuery.setParameter(sessionIdBindVariable, searchCriteria.getSessionId());
			}

			count = countTQuery.getSingleResult();

			// Calculate how many pages of data there are.
			pageCount = (int) count / pageSize;
			pageCount += (int) count % pageSize == 0 ? 0 : 1;
			
			return new PageableResult<>(page, pageCount, count, results);
		} else {
			return new PageableResult<>(page, results);
		}
	}

	/**
	 * Builds the WHERE clause for the main and count query.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to.
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param searchCriteria The user's search criteria.
	 * @return The WHERE clause for the main and count query.
	 */
	private Predicate[] getWhereClause(CriteriaBuilder criteriaBuilder,
									   Root<ProductMaster> pmRoot,
									   CriteriaQuery<? extends ProductMaster> queryBuilder,
									   ProductSearchCriteria searchCriteria,
									   ParameterExpression<String> sessionIdBindVariable) {

		List<Predicate> exists = new LinkedList<>();

		this.predicateBuilderList.getPredicateBuilders().forEach((pb) -> {
			List<Predicate> basicWhere = pb.buildBasicWhereClause(criteriaBuilder, pmRoot, searchCriteria);
			if (basicWhere != null) {
				exists.addAll(basicWhere);
			}
			ExistsClause<?> existsClause = pb.buildPredicate(criteriaBuilder, pmRoot, queryBuilder,
					searchCriteria, sessionIdBindVariable);
			if (existsClause != null) {
				if (existsClause.isExists()) {
					exists.add(criteriaBuilder.exists(existsClause.getSubQuery()));
				} else {
					exists.add(criteriaBuilder.not(criteriaBuilder.exists(existsClause.getSubQuery())));
				}
			}
		});

		// If we don't get a search at all, then throw an error.
		if (exists.size() == 0) {
			throw new IllegalArgumentException("There are no search criteria defined");
		}

		return exists.toArray(new Predicate[exists.size()]);
	}


	/**
	 * For a simple search generates counts of matches and misses with a full list of what is missing.
	 *
	 * @param searchCriteria The user's search criteria
	 * @return An object containing hit and miss counts and a list of misses.
	 */
	@CoreTransactional
	public HitsBase<?> getSearchHits(ProductSearchCriteria searchCriteria) {

		this.validateSearchCriteria(searchCriteria);

		// Make sure that only a simple search was passed in.
		if (!searchCriteria.isSimpleSearch()) {
			throw new IllegalArgumentException("Only simple searches can be used to find hits");
		}

		this.loadTempTable(searchCriteria, false);

		// Depending on the type of search the user is doing, you need to use
		// a different query.

		if (searchCriteria.hasProductSearch()) {
			List<Long> productIds =
					this.productInfoRepository.findAllProductIdsForSearch(searchCriteria.getSessionId());
			return Hits.from(searchCriteria.getProductIds(), productIds, "Product", "Products");
		}

		if (searchCriteria.hasUpcSearch()) {
			List<Long> upcs = this.sellingUnitRepository.findAllForSearch(searchCriteria.getSessionId());
			return Hits.from(searchCriteria.getUpcs(), upcs, "UPC", "UPCs");
		}

		if (searchCriteria.hasItemCodeSearch()) {
			List<Long> itemCodes = this.itemMasterRepository.findAllForSearch(searchCriteria.getSessionId());
			return Hits.from(searchCriteria.getItemCodes(), itemCodes, "Item Code", "Item Codes");
		}

		if (searchCriteria.hasCaseUpcSearch()) {
			List<Long> caseUpcs = this.itemMasterRepository.findAllCaseUpcsForSearch(searchCriteria.getSessionId());
			return Hits.from(searchCriteria.getCaseUpcs(), caseUpcs, "Case UPC", "Case UPCs");
		}

		if (searchCriteria.hasClassSearch()) {
			List<Integer> itemClasses = this.itemClassRepository.findAllForSearch(searchCriteria.getSessionId());
			return HitsBase.from(searchCriteria.getClassCodes(), itemClasses, "Class", "Classes");
		}

		if (searchCriteria.hasCommoditySearch()) {
			List<Integer> commodities = this.classCommodityRepository.findAllForSearch(searchCriteria.getSessionId());
			return HitsBase.from(searchCriteria.getCommodityCodes(), commodities, "Commodity", "Commodities");
		}

		if (searchCriteria.hasSubCommoditySearch()) {
			List<Integer> subCommodities = this.subCommodityRepository.findAllForSearch(searchCriteria.getSessionId());
			return HitsBase.from(searchCriteria.getSubCommodityCodes(), subCommodities, "Sub Commodity", "Sub Commodities");
		}

		return null;
	}

	/**
	 * Converts the Sort as defined in ProductMaster into the one needed by JPA criteria query.
	 *
	 * @param criteriaBuilder The CriteriaBuilder to use.
	 * @param pmRoot The root object for the query.

	 * @return A list of sort orders to be added to the order by clause.
	 */
	private List<Order> getSort(CriteriaBuilder criteriaBuilder, Root<ProductMaster> pmRoot) {

		List<Order> sortOrders = new LinkedList<>();

		ProductMaster.getDefaultSort().iterator().forEachRemaining((s) -> {
			if (s.isAscending()) {
				sortOrders.add(criteriaBuilder.asc(pmRoot.get(s.getProperty())));
			}
		});

		return sortOrders;
	}


	/**
	 * Checks to make sure a user's search criteria is valid.
	 *
	 * @param searchCriteria The search criteria to check.
	 */
	private void validateSearchCriteria(ProductSearchCriteria searchCriteria) {
		if (searchCriteria.getSessionId() == null) {
			throw new IllegalArgumentException("Session ID is required");
		}
		if (!searchCriteria.hasCriteria()) {
			throw new IllegalArgumentException("No search parameters set");
		}
	}

	/**
	 * Loads the temp table with the user's search criteria.
	 *
	 * @param searchCriteria The user's search criteria.
	 * @param firstSearch Whether or not this is the first time the user has performed this search.
	 */
	@CoreTransactional
	protected void loadTempTable(ProductSearchCriteria searchCriteria, boolean firstSearch) {
		// If this is the first search for a set of criteria, clear out any existing criteria from the temp table.
		// If there isn't a session ID for this request, then throw an error.
		if (firstSearch) {
			this.searchCriteriaRepository.deleteBySessionId(searchCriteria.getSessionId());
			// Flush the entity manager so that all the rows are actually removed.
			this.entityManager.flush();
		}

		// If this is a first search or there's no criteria for this session in the table, then loop through each
		// predicate builder and have it populate the temp table.
		if (firstSearch || this.searchCriteriaRepository.countBySessionId(searchCriteria.getSessionId()) <= 0) {
			this.predicateBuilderList.getPredicateBuilders().forEach((pb) -> pb.populateTempTable(searchCriteria));
		}

		// Flush the entity manager so that all the rows are in the temp table.
		this.entityManager.flush();
	}

	/**
	 * Finds a product given a product id.
	 *
	 * @param productId Product id to search for.
	 * @return Product matching the product id given.
	 */
	public ProductMaster findByProdId(Long productId) {
		return this.productInfoRepository.findOne(productId);
	}

	/**
	 * Used to generate the Header of the export file. The header comprises of a default set of columns and other
	 * additional columns based on the selected export options(additionalColumns).
	 *
	 * @param additionalColumns selected export options.
	 * @return a CSV string of column names for the CSV file Header.
	 */
	private String buildExportHeader(List<String> additionalColumns) {
		boolean hadPrimoPick = additionalColumns != null ? additionalColumns.contains(EXPORT_COL_PRIMO_PICK) : false;
		StringBuilder exportHeaderBuilder = new StringBuilder();
		exportHeaderBuilder.append(EXPORT_HEADER_BEGIN_COLS);
		if(additionalColumns != null && !additionalColumns.isEmpty()){
			Iterator<String> selectedColumnsIterator = additionalColumns.iterator();
			do {
				switch (selectedColumnsIterator.next()) {
					case EXPORT_COL_PRIMO_PICK:
						exportHeaderBuilder.append(CSV_SEPARATOR).append(EXPORT_HEADER_PRIMO_PICK);
						break;
					case EXPORT_COL_TAX_FLAG:
						exportHeaderBuilder.append(CSV_SEPARATOR).append(EXPORT_HEADER_TAX_FLAG);
						break;
					case EXPORT_COL_FOOD_STAMP_FLAG:
						exportHeaderBuilder.append(CSV_SEPARATOR).append(EXPORT_HEADER_FOOD_STAMP_FLAG);
						break;
					case EXPORT_COL_SERVICE_CASE_ATTRIBUTES:
						if(hadPrimoPick){
							exportHeaderBuilder.append(CSV_SEPARATOR).append(EXPORT_HEADER_SERVICE_CASE_ATTRIBUTES);
						}else{
							exportHeaderBuilder.append(CSV_SEPARATOR).append(EXPORT_HEADER_SERVICE_CASE_ATTRIBUTES_WITH_PRIMO_PICK);
						}
						break;
					case EXPORT_COL_ECOMMERCE:
						exportHeaderBuilder.append(CSV_SEPARATOR).append(EXPORT_HEADER_ECOMMERCE);
						break;
					default:
						break;
				}
			} while (selectedColumnsIterator.hasNext());
		}
		exportHeaderBuilder.append(CSV_SEPARATOR).append(EXPORT_HEADER_END_COLS);
		return exportHeaderBuilder.toString();
	}

	/**
	 * Used to resolve more results over the JPA ResultSet based on the selected export options.
	 *
	 * @param productMasterList product master result set.
	 * @param additionalColumns selected export options.
	 */
	private void resolveExportResults(Iterable<ProductMaster> productMasterList, List<String> additionalColumns) {
		if(additionalColumns == null || additionalColumns.isEmpty()) return;
		productMasterList.forEach(productMaster -> {
			Iterator<String> selectedColumnsIterator = additionalColumns.iterator();
			do {
				switch (selectedColumnsIterator.next()) {
					case EXPORT_COL_PRIMO_PICK:
						resolveMarketingClaimResult(productMaster);
						break;
					case EXPORT_COL_SERVICE_CASE_ATTRIBUTES:
						resolveMarketingClaimResult(productMaster);
						break;
					default:
						break;
				}
			} while (selectedColumnsIterator.hasNext());
		});
	}

	/**
	 * Resolves result set for marketing claims (like primo pick, distinctive etc).
	 * @param productMaster product master result set.
	 */
	private void resolveMarketingClaimResult(ProductMaster productMaster) {
		List<ProductMarketingClaim> productMarketingClaims = productMaster.getProductMarketingClaims();
		if (CollectionUtils.isNotEmpty(productMarketingClaims)) {
			this.initializeMarketingClaimsToFalse(productMaster);
			for (ProductMarketingClaim productMarketingClaim : productMarketingClaims) {
				this.setMarketingClaimTrueValue(productMaster, productMarketingClaim);
				if(productMarketingClaim.getMarketingClaim() != null){
					if (MarketingClaim.Codes.PRIMO_PICK.getCode().equals(productMarketingClaim.getMarketingClaim().getMarketingClaimCode())) {
						productMaster.setPrimoPickStatus(productMarketingClaim.getMarketingClaimStatusCode());
					}
				}
			}
		}
	}
	
	/**
	 * This method sets all marketing claim values on a product master to false. These values are Booleans, so before
	 * doing this all of these values are null. If this product master was not resolved, they would stay as null.
	 * Since this product master is being resolved, they are initialized to false. If the product master does have a
	 * particular marketing claim code, the value will be set to true when the product master resolves its product
	 * marketing claims.
	 *
	 * @param productMaster Product master being resolved.
	 */
	private void initializeMarketingClaimsToFalse(ProductMaster productMaster) {
		productMaster.setDistinctive(false);
		productMaster.setPrimoPick(false);
		productMaster.setOwnBrand(false);
		productMaster.setGoLocal(false);
		productMaster.setTotallyTexas(false);
		productMaster.setSelectIngredients(false);
	}
	
	/**
	 * This method sets the appropriate marketing claim value on product master to true, based on the given
	 * marketing claim's marketing claim code.
	 *
	 * @param productMaster Product master being resolved.
	 * @param productMarketingClaim Product marketing claim currently being looked at.
	 */
	private void setMarketingClaimTrueValue(ProductMaster productMaster, ProductMarketingClaim productMarketingClaim) {
		if(MarketingClaim.Codes.DISTINCTIVE.getCode().equals(
				StringUtils.trimToEmpty(productMarketingClaim.getKey().getMarketingClaimCode()))){
			productMaster.setDistinctive(true);
		} else if(MarketingClaim.Codes.PRIMO_PICK.getCode().equals(
				StringUtils.trimToEmpty(productMarketingClaim.getKey().getMarketingClaimCode()))){
			productMaster.setPrimoPick(true);
		} else if(MarketingClaim.Codes.OWN_BRAND.getCode().equals(
				StringUtils.trimToEmpty(productMarketingClaim.getKey().getMarketingClaimCode()))){
			productMaster.setOwnBrand(true);
		} else if(MarketingClaim.Codes.GO_LOCAL.getCode().equals(
				StringUtils.trimToEmpty(productMarketingClaim.getKey().getMarketingClaimCode()))){
			productMaster.setGoLocal(true);
		} else if(MarketingClaim.Codes.TOTALLY_TEXAS.getCode().equals(
				StringUtils.trimToEmpty(productMarketingClaim.getKey().getMarketingClaimCode()))){
			productMaster.setTotallyTexas(true);
		} else if(MarketingClaim.Codes.SELECT_INGREDIENTS.getCode().equals(
				StringUtils.trimToEmpty(productMarketingClaim.getKey().getMarketingClaimCode()))){
			productMaster.setSelectIngredients(true);
		}
	}

	/**
	 * Used to build data row for primo pick info.
	 * @param productMaster product master result.
	 * @return a CSV string containing primo pick, distinctive, description and status.
	 */
	private String exportPrimoPick(ProductMaster productMaster) {
		String primoPickSwitch = ProductSearchService.PRIMO_PICK_NO;
		String distinctiveSwitch = ProductSearchService.DISTINCTIVE_NO;
		String primoPickProposedDescription = StringUtils.EMPTY;
		String primoPickDescription = StringUtils.EMPTY;
		String primoPickStatus = StringUtils.EMPTY;
		String goLocalSwitch = ProductSearchService.STRING_N;

		if (productMaster.getPrimoPick() != null) {
			primoPickSwitch = productMaster.getPrimoPick() ? ProductSearchService.PRIMO_PICK_YES : ProductSearchService.PRIMO_PICK_NO;
		}
		if(productMaster.getDistinctive() != null) {
			distinctiveSwitch = productMaster.getDistinctive() ? ProductSearchService.DISTINCTIVE_YES : ProductSearchService.DISTINCTIVE_NO;
		}
		if (productMaster.getPrimoPickProposedDescription() != null) {
			primoPickProposedDescription = StringUtils.trimToEmpty(productMaster.getPrimoPickProposedDescription());
		}
		if(productMaster.getPrimoPickDescription() != null) {
			primoPickDescription = StringUtils.trimToEmpty(productMaster.getPrimoPickDescription());
		}
		if(productMaster.getPrimoPickStatus() != null) {
			if (ProductMarketingClaim.APPROVED.trim().equalsIgnoreCase(StringUtils.trimToEmpty(productMaster.getPrimoPickStatus()))) {
				primoPickStatus = ProductSearchService.EXP_PRIMO_PICK_STATUS_APPROVED;
			} else if (ProductMarketingClaim.SUBMITTED.trim().equalsIgnoreCase(StringUtils.trimToEmpty(productMaster.getPrimoPickStatus()))) {
				primoPickStatus = ProductSearchService.EXP_PRIMO_PICK_STATUS_SUBMITTED;
			} else if (ProductMarketingClaim.REJECTED.trim().equalsIgnoreCase(StringUtils.trimToEmpty(productMaster.getPrimoPickStatus()))) {
				primoPickStatus = ProductSearchService.EXP_PRIMO_PICK_STATUS_REJECTED;
			}
		}
		if (productMaster.getGoLocal() != null) {
			goLocalSwitch = productMaster.getGoLocal() ? ProductSearchService.STRING_Y : ProductSearchService.STRING_N;
		}
		return String.format(EXPORT_FORMAT_PROM_PICK,
				primoPickSwitch,
				distinctiveSwitch,
				this.formatCsvData(primoPickProposedDescription),
				this.formatCsvData(primoPickDescription),
				primoPickStatus,
				goLocalSwitch);
	}

	/**
	 * Used to build data row for Service Case Attributes info.
	 * @param productMaster product master result.
	 * @param hadPrimoPick The primo pick data is already exported.
	 * @return a CSV string.
	 */
	private String exportServiceCaseAttributes(ProductMaster productMaster, boolean hadPrimoPick) {
		String returnString = StringUtils.EMPTY;
		String primoPickSwitch = ProductSearchService.PRIMO_PICK_NO;
		String distinctiveSwitch = ProductSearchService.DISTINCTIVE_NO;
		String primoPickProposedDescription = StringUtils.EMPTY;
		String primoPickDescription = StringUtils.EMPTY;
		String goLocalSwitch = ProductSearchService.STRING_N;

		LocalDate tagEffectiveDate = null;
		String shelfTagSizeCode = StringUtils.EMPTY;
		String tagItemCode = StringUtils.EMPTY;
		String englishCustomerFriendlyDescription1 = StringUtils.EMPTY;
		String englishCustomerFriendlyDescription2 = StringUtils.EMPTY;
		String serviceCaseStatus = StringUtils.EMPTY;
		String proposedDescription = StringUtils.EMPTY;
		String approvedDescription = StringUtils.EMPTY;
		String tagType = StringUtils.EMPTY;

		if(!hadPrimoPick){
			if (productMaster.getPrimoPick() != null) {
				primoPickSwitch = productMaster.getPrimoPick() ? ProductSearchService.PRIMO_PICK_YES : ProductSearchService.PRIMO_PICK_NO;
			}
			if(productMaster.getDistinctive() != null) {
				distinctiveSwitch = productMaster.getDistinctive() ? ProductSearchService.DISTINCTIVE_YES : ProductSearchService.DISTINCTIVE_NO;
			}
			if (productMaster.getPrimoPickProposedDescription() != null) {
				primoPickProposedDescription = StringUtils.trimToEmpty(productMaster.getPrimoPickProposedDescription());
			}
			if(productMaster.getPrimoPickDescription() != null) {
				primoPickDescription = StringUtils.trimToEmpty(productMaster.getPrimoPickDescription());
			}

			if (productMaster.getGoLocal() != null) {
				goLocalSwitch = productMaster.getGoLocal() ? ProductSearchService.STRING_Y : ProductSearchService.STRING_N;
			}
		}

		if (productMaster.getGoodsProduct() != null) {
			tagEffectiveDate = productMaster.getGoodsProduct().getTagEffectiveDate();
			shelfTagSizeCode = StringUtils.trim(productMaster.getGoodsProduct().getShelfTagSizeCode());
			if (productMaster.getGoodsProduct().getTagTypeAttribute() != null) {
				tagType = StringUtils.trimToEmpty(productMaster.getGoodsProduct().getTagTypeAttribute().getTagTypeDescription());
			}
		}

		tagItemCode = productMaster.getTagItemCode().toString();

		List<ProductDescription> productDescriptions = productMaster.getProductDescriptions();
		if (CollectionUtils.isNotEmpty(productDescriptions)) {
			for (ProductDescription productDescription : productDescriptions) {
				if(DescriptionType.Codes.TAG_CUSTOMER_FRIENDLY_LINE_ONE.getId().trim().equalsIgnoreCase(StringUtils.trim(productDescription.getKey().getDescriptionType()))
						&& ProductDescription.ENGLISH.equalsIgnoreCase(StringUtils.trim(productDescription.getKey().getLanguageType()))){
					englishCustomerFriendlyDescription1 = StringUtils.trimToEmpty(productDescription.getDescription());
				}
				if(DescriptionType.Codes.TAG_CUSTOMER_FRIENDLY_LINE_TWO.getId().trim().equalsIgnoreCase(StringUtils.trim(productDescription.getKey().getDescriptionType()))
						&& ProductDescription.ENGLISH.equalsIgnoreCase(StringUtils.trim(productDescription.getKey().getLanguageType()))){
					englishCustomerFriendlyDescription2 = StringUtils.trimToEmpty(productDescription.getDescription());
				}
				if(DescriptionType.Codes.SIGN_ROMANCE_COPY.getId().equalsIgnoreCase(StringUtils.trim(productDescription.getKey().getDescriptionType()))
						&& ProductDescription.ENGLISH.equalsIgnoreCase(StringUtils.trim(productDescription.getKey().getLanguageType()))){
					approvedDescription = StringUtils.trimToEmpty(productDescription.getDescription());
				}
			}
		}

		CandidateWorkRequest workRequest = null;
		List<CandidateWorkRequest> candidateWorkRequests = productMaster.getCandidateWorkRequests();
		if (CollectionUtils.isNotEmpty(candidateWorkRequests)) {
			for (CandidateWorkRequest candidateWorkRequest : candidateWorkRequests) {
				if(candidateWorkRequest.getIntent() == ShelfAttributesService.SHELF_CASE_TAG_INTENT_CD
						&& !CandidateWorkRequest.REQUEST_STATUS_DELETED.equals(candidateWorkRequest.getStatus())){
					workRequest = candidateWorkRequest;
					break;
				}
			}
			if (workRequest != null) {
				String status = workRequest.getStatus();
				if(ServiceCaseSignCode.SUBMITTED.getCode().equals(status)){
					serviceCaseStatus = ServiceCaseSignCode.SUBMITTED.getName();
				}else if(ServiceCaseSignCode.APPROVED.getCode().equals(status)){
					serviceCaseStatus = ServiceCaseSignCode.APPROVED.getName();
				}else if(ServiceCaseSignCode.REJECTED.getCode().equals(status)){
					serviceCaseStatus = ServiceCaseSignCode.REJECTED.getName();
				}
				if (CollectionUtils.isNotEmpty(workRequest.getCandidateProductMaster()) 
						&& CollectionUtils.isNotEmpty(workRequest.getCandidateProductMaster().get(0).getCandidateDescriptions())) {
					proposedDescription = StringUtils.trimToEmpty(workRequest.getCandidateProductMaster().get(0).getCandidateDescriptions().get(0).getDescription());
				}
			}
		}

		if(hadPrimoPick){
			returnString = String.format(EXPORT_FORMAT_SERVICE_CASE_ATTRIBUTES,
					tagEffectiveDate,
					shelfTagSizeCode,
					tagItemCode,
					this.formatCsvData(englishCustomerFriendlyDescription1),
					this.formatCsvData(englishCustomerFriendlyDescription2),
					serviceCaseStatus,
					this.formatCsvData(proposedDescription),
					this.formatCsvData(approvedDescription),
					tagType);
		}else{
			returnString = String.format(EXPORT_FORMAT_SERVICE_CASE_ATTRIBUTES_WITH_PRIMO_PICK,
					primoPickSwitch,
					distinctiveSwitch,
					this.formatCsvData(primoPickProposedDescription),
					this.formatCsvData(primoPickDescription),
					goLocalSwitch,
					tagEffectiveDate,
					shelfTagSizeCode,
					tagItemCode,
					this.formatCsvData(englishCustomerFriendlyDescription1),
					this.formatCsvData(englishCustomerFriendlyDescription2),
					serviceCaseStatus,
					this.formatCsvData(proposedDescription),
					this.formatCsvData(approvedDescription),
					tagType);
		}

		return returnString;
	}

	/**
	 * Used to build data row for eCommerce.
	 * @param productMaster product master result.
	 * @param prodItem product item.
	 * @param targetSystemAttributePriorities The list of TargetSystemAttributePriority.
	 * @return a CSV string.
	 */
	private String exportEcommerce(ProductMaster productMaster, ProdItem prodItem, List<TargetSystemAttributePriority> targetSystemAttributePriorities) {
		String department = StringUtils.EMPTY;
		String commodity = StringUtils.EMPTY;
		String ebm = StringUtils.EMPTY;
		String productBrand = StringUtils.EMPTY;
		
		String onlineBrand = null;
		String onlineDisplayName = null;
		String onlineSize = null;
		String onlineRomanceCopy = null;
		String onlineIngredients = null;
		String onlineDirections = null;
		String onlineWarnings = null;
		
		String publishHebcom = ProductOnline.SHOW_ON_SITE_NO;
		String publishHeb2u = ProductOnline.SHOW_ON_SITE_NO;
		
		String hebcomLowestLevelId = StringUtils.EMPTY;
		String heb2uLowestLevelId = StringUtils.EMPTY;
		
		String hebcomHierarchy = StringUtils.EMPTY;
		String heb2uHierarchy = StringUtils.EMPTY;

		String showOnSiteHebDotCom = null;
		String showOnSiteHebToYou = null;
		String fulfillmentChannel = null;
		String thirdParty = DistributionFilter.THIRD_PARTY_NO;
		long ssaCount;
		String apVendor = StringUtils.EMPTY;
		String purchasingStatus = StringUtils.EMPTY;

		if (productMaster.getClassCommodity() != null) {
			commodity = productMaster.getClassCommodity().getDisplayName();
		}
		if (productMaster.getSubDepartment() != null) {
			department = productMaster.getSubDepartment().getDisplayName();
		}		
		if (productMaster.getProductBrand() != null) {
			productBrand = productMaster.getProductBrand().getDisplayName();
		}		
		ebm = StringUtils.trimToEmpty(productMaster.getEbmName());
		//ebm = productMaster.getClassCommodity().geteBMid();

		List<MasterDataExtensionAttribute> exportMasterDataExtensionAttributes = productMaster.getExportMasterDataExtensionAttributes();
		if (CollectionUtils.isNotEmpty(exportMasterDataExtensionAttributes)) {
			for (MasterDataExtensionAttribute masterDataExtensionAttribute : exportMasterDataExtensionAttributes) {
				if(SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_ECOMMERCE.getValue().longValue() == masterDataExtensionAttribute.getKey().getDataSourceSystem().longValue()){
					if(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_BRAND.getValue().equals(masterDataExtensionAttribute.getKey().getAttributeId())){
						onlineBrand = StringUtils.trimToEmpty(masterDataExtensionAttribute.getAttributeValueText());
					}else if(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_DISPLAY_NAME.getValue().equals(masterDataExtensionAttribute.getKey().getAttributeId())){
						onlineDisplayName = StringUtils.trimToEmpty(masterDataExtensionAttribute.getAttributeValueText());
					}else if(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_SIZE.getValue().equals(masterDataExtensionAttribute.getKey().getAttributeId())){
						onlineSize = StringUtils.trimToEmpty(masterDataExtensionAttribute.getAttributeValueText());
					}else if(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_INGREDIENT_STATEMENT.getValue().equals(masterDataExtensionAttribute.getKey().getAttributeId())){
						onlineIngredients = StringUtils.trimToEmpty(masterDataExtensionAttribute.getAttributeValueText());
					}else if(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_DIRECTIONS.getValue().equals(masterDataExtensionAttribute.getKey().getAttributeId())){
						onlineDirections = StringUtils.trimToEmpty(masterDataExtensionAttribute.getAttributeValueText());
					}else if(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_WARNING.getValue().equals(masterDataExtensionAttribute.getKey().getAttributeId())){
						onlineWarnings = StringUtils.trimToEmpty(masterDataExtensionAttribute.getAttributeValueText());
					}else if(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_ROMANCE_COPY.getValue().equals(masterDataExtensionAttribute.getKey().getAttributeId())){
						onlineRomanceCopy = StringUtils.trimToEmpty(masterDataExtensionAttribute.getAttributeValueText());
					}
				}
			}
		}
		
		List<ProductAttributeOverwrite> productAttributeOverwrites = productMaster.getProductAttributeOverwrites();
		if (CollectionUtils.isNotEmpty(productAttributeOverwrites)) {
			for (ProductAttributeOverwrite productAttributeOverwrite : productAttributeOverwrites) {
				if(onlineBrand == null 
						&& Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_BRAND.getValue().longValue() == productAttributeOverwrite.getKey().getLogicAttributeId().longValue()){
					onlineBrand = this.getLogicalAttributeData(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_BRAND.getValue().longValue(), 
							productAttributeOverwrite.getKey().getPhysicalAttributeId(),
							productAttributeOverwrite.getKey().getSourceSystemId(),
							productMaster);
					onlineBrand = (onlineBrand == null) ? StringUtils.EMPTY : onlineBrand;
				}
				if(onlineDisplayName == null 
						&& Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_DISPLAY_NAME.getValue().longValue() == productAttributeOverwrite.getKey().getLogicAttributeId().longValue()){
					onlineDisplayName = this.getLogicalAttributeData(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_DISPLAY_NAME.getValue().longValue(), 
							productAttributeOverwrite.getKey().getPhysicalAttributeId(),
							productAttributeOverwrite.getKey().getSourceSystemId(),
							productMaster);
					onlineDisplayName = (onlineDisplayName == null) ? StringUtils.EMPTY : onlineDisplayName;
				}
				if(onlineSize == null 
						&& Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_SIZE.getValue().longValue() == productAttributeOverwrite.getKey().getLogicAttributeId().longValue()){
					onlineSize = this.getLogicalAttributeData(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_SIZE.getValue().longValue(), 
							productAttributeOverwrite.getKey().getPhysicalAttributeId(),
							productAttributeOverwrite.getKey().getSourceSystemId(),
							productMaster);
					onlineSize = (onlineSize == null) ? StringUtils.EMPTY : onlineSize;
				}
				if(onlineIngredients == null 
						&& Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_INGREDIENT_STATEMENT.getValue().longValue() == productAttributeOverwrite.getKey().getLogicAttributeId().longValue()){
					onlineIngredients = this.getLogicalAttributeData(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_INGREDIENT_STATEMENT.getValue().longValue(), 
							productAttributeOverwrite.getKey().getPhysicalAttributeId(),
							productAttributeOverwrite.getKey().getSourceSystemId(),
							productMaster);
					onlineIngredients = (onlineIngredients == null) ? StringUtils.EMPTY : onlineIngredients;
				}
				if(onlineDirections == null 
						&& Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_DIRECTIONS.getValue().longValue() == productAttributeOverwrite.getKey().getLogicAttributeId().longValue()){
					onlineDirections = this.getLogicalAttributeData(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_DIRECTIONS.getValue().longValue(), 
							productAttributeOverwrite.getKey().getPhysicalAttributeId(),
							productAttributeOverwrite.getKey().getSourceSystemId(),
							productMaster);
					onlineDirections = (onlineDirections == null) ? StringUtils.EMPTY : onlineDirections;
				}
				if(onlineWarnings == null 
					&& Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_WARNING.getValue().longValue() == productAttributeOverwrite.getKey().getLogicAttributeId().longValue()){
					onlineWarnings = this.getLogicalAttributeData(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_WARNING.getValue().longValue(), 
							productAttributeOverwrite.getKey().getPhysicalAttributeId(),
							productAttributeOverwrite.getKey().getSourceSystemId(),
							productMaster);
					onlineWarnings = (onlineWarnings == null) ? StringUtils.EMPTY : onlineWarnings;
				}
				if(onlineRomanceCopy == null 
						&& Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_ROMANCE_COPY.getValue().longValue() == productAttributeOverwrite.getKey().getLogicAttributeId().longValue()){
					onlineRomanceCopy = this.getLogicalAttributeData(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_ROMANCE_COPY.getValue().longValue(), 
							productAttributeOverwrite.getKey().getPhysicalAttributeId(),
							productAttributeOverwrite.getKey().getSourceSystemId(),
							productMaster);
					onlineRomanceCopy = (onlineRomanceCopy == null) ? StringUtils.EMPTY : onlineRomanceCopy;
				}
			}
		}
		
		if (CollectionUtils.isNotEmpty(targetSystemAttributePriorities)) {
			for (TargetSystemAttributePriority targetSystemAttributePriority : targetSystemAttributePriorities) {
				if(onlineBrand == null 
						&& Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_BRAND.getValue().intValue() == targetSystemAttributePriority.getKey().getLogicalAttributeId()){
					onlineBrand = this.getLogicalAttributeData(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_BRAND.getValue().longValue(), 
							targetSystemAttributePriority.getKey().getPhysicalAttributeId(),
							targetSystemAttributePriority.getKey().getDataSourceSystemId(),
							productMaster);
				}
				if(onlineDisplayName == null 
						&& Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_DISPLAY_NAME.getValue().intValue() == targetSystemAttributePriority.getKey().getLogicalAttributeId()){
					onlineDisplayName = this.getLogicalAttributeData(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_DISPLAY_NAME.getValue().longValue(), 
							targetSystemAttributePriority.getKey().getPhysicalAttributeId(),
							targetSystemAttributePriority.getKey().getDataSourceSystemId(),
							productMaster);
				}
				if(onlineSize == null 
						&& Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_SIZE.getValue().intValue() == targetSystemAttributePriority.getKey().getLogicalAttributeId()){
					onlineSize = this.getLogicalAttributeData(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_SIZE.getValue().longValue(), 
							targetSystemAttributePriority.getKey().getPhysicalAttributeId(),
							targetSystemAttributePriority.getKey().getDataSourceSystemId(),
							productMaster);
				}
				if(onlineIngredients == null 
						&& Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_INGREDIENT_STATEMENT.getValue().intValue() == targetSystemAttributePriority.getKey().getLogicalAttributeId()){
					onlineIngredients = this.getLogicalAttributeData(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_INGREDIENT_STATEMENT.getValue().longValue(), 
							targetSystemAttributePriority.getKey().getPhysicalAttributeId(),
							targetSystemAttributePriority.getKey().getDataSourceSystemId(),
							productMaster);
				}
				if(onlineDirections == null 
						&& Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_DIRECTIONS.getValue().intValue() == targetSystemAttributePriority.getKey().getLogicalAttributeId()){
					onlineDirections = this.getLogicalAttributeData(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_DIRECTIONS.getValue().longValue(), 
							targetSystemAttributePriority.getKey().getPhysicalAttributeId(),
							targetSystemAttributePriority.getKey().getDataSourceSystemId(),
							productMaster);
				}
				if(onlineWarnings == null
						&& Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_WARNING.getValue().intValue() == targetSystemAttributePriority.getKey().getLogicalAttributeId()){
					onlineWarnings = this.getLogicalAttributeData(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_WARNING.getValue().longValue(),
							targetSystemAttributePriority.getKey().getPhysicalAttributeId(),
							targetSystemAttributePriority.getKey().getDataSourceSystemId(),
							productMaster);
				}
				if(onlineRomanceCopy == null 
						&& Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_ROMANCE_COPY.getValue().intValue() == targetSystemAttributePriority.getKey().getLogicalAttributeId()){
					onlineRomanceCopy = this.getLogicalAttributeData(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_ROMANCE_COPY.getValue().longValue(), 
							targetSystemAttributePriority.getKey().getPhysicalAttributeId(),
							targetSystemAttributePriority.getKey().getDataSourceSystemId(),
							productMaster);
				}
			}
		}
		
		List<ProductOnline> productOnlines = productMaster.getProductOnlines();
		if (CollectionUtils.isNotEmpty(productOnlines)) {
			for (ProductOnline productOnline : productOnlines) {
				if(productOnline.isShowOnSite() && ProductECommerceViewUtil.compareGreaterThanOrEqualToCurrentDate(productOnline.getExpirationDate())){
					if(SalesChannel.SALES_CHANNEL_HEB_COM.equals(productOnline.getKey().getSaleChannelCode())){
						publishHebcom = ProductOnline.SHOW_ON_SITE_YES;
					}
					if(SalesChannel.SALES_CHANNEL_HEB_2U.equals(productOnline.getKey().getSaleChannelCode())){
						publishHeb2u = ProductOnline.SHOW_ON_SITE_YES;
					}
				}
			}
		}

		List<GenericEntity> genericEntities = productMaster.getGenericEntities();
		if (CollectionUtils.isNotEmpty(genericEntities)) {
			GenericEntity genericEntity = genericEntities.get(0);
			List<ExportGenericEntityRelationship> exportGenericEntityRelationships = genericEntity.getExportGenericEntityRelationships();
			if (CollectionUtils.isNotEmpty(exportGenericEntityRelationships)) {
				for (ExportGenericEntityRelationship exportGenericEntityRelationship : exportGenericEntityRelationships) {
					if(exportGenericEntityRelationship.getDefaultParent()){
						if(HierarchyContext.HierarchyContextCode.CUST.getName().equals(exportGenericEntityRelationship.getKey().getHierarchyContext())){
							hebcomLowestLevelId = exportGenericEntityRelationship.getKey().getParentEntityId().toString();
							hebcomHierarchy = this.getHierarchyPrimaryPath(exportGenericEntityRelationship, HierarchyContext.HierarchyContextCode.CUST.getName());
						}
						if(HierarchyContext.HierarchyContextCode.HEBTO.getName().equals(exportGenericEntityRelationship.getKey().getHierarchyContext())){
							heb2uLowestLevelId = exportGenericEntityRelationship.getKey().getParentEntityId().toString();
							heb2uHierarchy = this.getHierarchyPrimaryPath(exportGenericEntityRelationship, HierarchyContext.HierarchyContextCode.HEBTO.getName());
						}
					}
				}
			}
		}

		//PM-1632 Add new filed: show on site HEB.COM
		showOnSiteHebDotCom = getShowOnSiteByProductIdAndSaleChannel(productMaster, SalesChannel.SALES_CHANNEL_HEB_COM)
				? ProductOnline.SHOW_ON_SITE_YES : ProductOnline.SHOW_ON_SITE_NO;

		//PM-1632 Add new filed: show on site HEB TO YOU
		showOnSiteHebToYou = getShowOnSiteByProductIdAndSaleChannel(productMaster, SalesChannel.SALES_CHANNEL_HEB_2U)
				? ProductOnline.SHOW_ON_SITE_YES : ProductOnline.SHOW_ON_SITE_NO;

		//PM-1632 Add new filed: Fulfillment channel
		List<String> fulfillmentChannelList = new ArrayList<>();
		for (ProductFullfilmentChanel productFullfilmentChanel :
				productFullfilmentChanelRepository.findByKeyProductId(productMaster.getProdId())) {
			if(productFullfilmentChanel.getFulfillmentChannel().getSalesChannel().getDescription() != null
					&& productFullfilmentChanel.getFulfillmentChannel().getDescription() != null) {
				fulfillmentChannelList.add(
						productFullfilmentChanel.getFulfillmentChannel().getSalesChannel().getDescription().trim()
								+ ProductECommerceViewService.SPACE_SEPARATOR + productFullfilmentChanel.getFulfillmentChannel().getDescription().trim());
			}
		}
		fulfillmentChannel =  String.join(COMMA_CHARACTER, fulfillmentChannelList);

		//PM-1632 Add new filed: 3rd Party
		DistributionFilter distributionFilter = this.onlineAttributesService.getThirdPartySellableStatus(productMaster.getProdId());
		if(distributionFilter != null
				&& distributionFilter.getKey().getTargetSystemId() == SourceSystem.SOURCE_SYSTEM_INSTACART
				&& DistributionFilterKey.PRODUCT_KEY_TYPE.trim().equals(distributionFilter.getKey().getKeyType().trim())) {
			thirdParty = DistributionFilter.THIRD_PARTY_YES;
		}

		//PM-1632 Add new filed: SSA
		ssaCount = productInformationService.getTotalCount(String.valueOf(productMaster.getProdId())).getStoreCount();

		//PM-1632 Add new filed: apVendor
		if(StringUtils.EMPTY.equals(apVendor)) {
			for(VendorLocationItem vendorLocationItem: prodItem.getItemMaster().getVendorLocationItems()) {
				apVendor = vendorLocationItem.getDisplayApLocationApVendorName();
				break;
			}
		}

		//PM-1632 Add new filed: Purchasing Status
		// Get purchasing status
		Set<String> purchasingStatusList = new TreeSet<>();
		for (WarehouseLocationItem warehouseLocationItem : prodItem.getItemMaster().getWarehouseLocationItems()) {
			purchasingStatusList.add(warehouseLocationItem.getPurchasingStatusCode().getDescription().trim());
		}
		// Convert list purchasing status to string
		if (purchasingStatusList.size() > 0) {
			purchasingStatus = StringUtils.join(purchasingStatusList, PURCHASING_STATUS_SEPARATOR);
		}

		return String.format(EXPORT_FORMAT_ECOMMERCE,
				commodity,
				department,
				ebm,
				productBrand,
				(onlineBrand != null ? this.formatCsvData(onlineBrand) : StringUtils.EMPTY),
				(onlineDisplayName != null ? this.formatCsvData(onlineDisplayName) : StringUtils.EMPTY),
				(onlineSize != null ? this.formatCsvData(onlineSize) : StringUtils.EMPTY),
				(onlineRomanceCopy != null ? this.formatCsvData(onlineRomanceCopy) : StringUtils.EMPTY),
				(onlineIngredients != null ? this.formatCsvData(onlineIngredients) : StringUtils.EMPTY),
				(onlineDirections != null ? this.formatCsvData(onlineDirections) : StringUtils.EMPTY),
				(onlineWarnings != null ? this.formatCsvData(onlineWarnings) : StringUtils.EMPTY),
				publishHebcom,
				publishHeb2u,
				hebcomLowestLevelId,
				heb2uLowestLevelId,
				hebcomHierarchy,
				heb2uHierarchy,
				showOnSiteHebDotCom,
				showOnSiteHebToYou,
				fulfillmentChannel,
				thirdParty,
				ssaCount,
				apVendor,
				purchasingStatus);
	}

	/**
	 * Get ShowOnSiteByExpirationDate status by productId and sale channel
	 * @param productMaster The product master.
	 * @param hebSaleChannel The heb sale channel
	 * @return boolean
	 */
	private boolean getShowOnSiteByProductIdAndSaleChannel(ProductMaster productMaster, String hebSaleChannel) {
		boolean result = false;
		ProductOnline hebDotComItem = this.productOnlineRepository.findTop1ByKeyProductIdAndKeySaleChannelCodeAndShowOnSiteOrderByKeyEffectiveDateDesc(productMaster.getProdId(), hebSaleChannel, ProductECommerceViewService.PRODUCT_SHOW_ON_SITE);

		if(hebDotComItem != null) {
			result = hebDotComItem.getShowOnSiteByExpirationDate();
		}
		return result;
	}

	/**
     * Get hierarchy primary path by hierarchy context code.
     * @param exportGenericEntityRelationship  - The ExportGenericEntityRelationship type.
     * @param hierarchyContextCode - The hierarchy context code.
     * @return The hierarchy primary path.
     */
	private String getHierarchyPrimaryPath(ExportGenericEntityRelationship exportGenericEntityRelationship, String hierarchyContextCode) {
		String primaryPath = StringUtils.EMPTY;
		while (exportGenericEntityRelationship != null && exportGenericEntityRelationship.getParentRelationships() != null &&
				!exportGenericEntityRelationship.getParentRelationships().isEmpty()) {
			String description = GenericEntityDescription.EMPTY_DESCRIPTION;
			if(exportGenericEntityRelationship.getGenericParentEntity().getDisplayNumber() > 0){
				description = exportGenericEntityRelationship.getParentDescription().getLongDescription();
			}else{
				description = exportGenericEntityRelationship.getGenericParentEntity().getDisplayText();
			}
			if (StringUtils.isNotBlank(primaryPath)) {
				primaryPath = description.concat("-->").concat(primaryPath);
			} else {
				primaryPath = description;
			}
			exportGenericEntityRelationship = 
					this.getExportGenericEntityRelationship(exportGenericEntityRelationship.getParentRelationships(), hierarchyContextCode);
		}
		return primaryPath;
	}

	/**
     * Get ExportGenericEntityRelationship object by hierarchy context code.
     * @param exportGenericEntityRelationships  - The list of ExportGenericEntityRelationship type.
     * @param hierarchyContextCode - The hierarchy context code.
     * @return The ExportGenericEntityRelationship type.
     */
	private ExportGenericEntityRelationship getExportGenericEntityRelationship(List<ExportGenericEntityRelationship> exportGenericEntityRelationships,
																				String hierarchyContextCode) {
		ExportGenericEntityRelationship returnObject = null;
		if (CollectionUtils.isNotEmpty(exportGenericEntityRelationships)) {
			for (ExportGenericEntityRelationship exportGenericEntityRelationship : exportGenericEntityRelationships) {
				if (exportGenericEntityRelationship.getDefaultParent() 
						&& hierarchyContextCode.equals(exportGenericEntityRelationship.getKey().getHierarchyContext())) {
					returnObject = exportGenericEntityRelationship;
					break;
				}
			}
		}
		return returnObject;
	}
	
	/**
     * Get content of source system by logical attributes priority.
     * @param logicalAttributeId  - The logical attribute id.
     * @param physicalAttributeId - The physical attribute id.
     * @param sourceSystemId      - The sourcce system id.
     * @param productMaster       - The product master.
     * @return The String type.
     */
    private String getLogicalAttributeData(long logicalAttributeId, long physicalAttributeId, long sourceSystemId, ProductMaster productMaster) {
    	String returnString = null;
        switch (Attribute.LogicalAttribute.getAttribute(logicalAttributeId)) {
            case LOGICAL_ATTRIBUTE_BRAND:
            	returnString = this.getDataSourceSystemForBrand(physicalAttributeId, sourceSystemId, productMaster);
                break;
            case LOGICAL_ATTRIBUTE_WARNING:
            	returnString = this.getDataSourceSystemForWarning(physicalAttributeId, sourceSystemId, productMaster);
                break;
            case LOGICAL_ATTRIBUTE_DIRECTIONS:
            	returnString = this.getDataSourceSystemForDirection(physicalAttributeId, sourceSystemId, productMaster);
                break;
            case LOGICAL_ATTRIBUTE_INGREDIENT_STATEMENT:
            	returnString = this.getDataSourceSystemForIngredient(physicalAttributeId, sourceSystemId, productMaster);
                break;
            case LOGICAL_ATTRIBUTE_DISPLAY_NAME:
            	returnString = this.getDataSourceSystemForDisplayName(physicalAttributeId, sourceSystemId, productMaster);
                break;
            case LOGICAL_ATTRIBUTE_SIZE:
            	returnString = this.getDataSourceSystemForSize(physicalAttributeId, sourceSystemId, productMaster);
                break;
            case LOGICAL_ATTRIBUTE_ROMANCE_COPY:
            	returnString = this.getDataSourceSystemForRomanceCopy(physicalAttributeId, sourceSystemId, productMaster);
            	break;
            default:
                break;
        }
        return returnString;
    }

    /**
     * Get source system content of Brand logical attribute.
     * @param physicalAttributeId - The physical attribute id.
     * @param sourceSystemId      - The sourcce system id.
     * @param productMaster       - The product master.
     * @return The String type.
     */
	private String getDataSourceSystemForBrand(long physicalAttributeId, long sourceSystemId, ProductMaster productMaster) {
		String returnString = null;
		StringBuilder contentBuilder = new StringBuilder();
		if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_GLADSON.getValue().longValue()) {
			List<ProductScanCodeExtent> productScanCodeExtents = productMaster.getProductScanCodeExtents();
			if (CollectionUtils.isNotEmpty(productScanCodeExtents)) {
				for (ProductScanCodeExtent productScanCodeExtent : productScanCodeExtents) {
					if (physicalAttributeId == 312) {
						if(ProductScanCodeExtent.BRAND_DIMENSION_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))
								|| ProductScanCodeExtent.PLINE_DIMENSION_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))){
							this.addDataToStringBuilder(contentBuilder, productScanCodeExtent.getProdDescriptionText());
						}
					} else if (physicalAttributeId == 1602) {
						if(ProductScanCodeExtent.BRAND_DIMENSION_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))){
							this.addDataToStringBuilder(contentBuilder, productScanCodeExtent.getProdDescriptionText());
						}
					} else if (physicalAttributeId == 1612) {
						if(ProductScanCodeExtent.PLINE_DIMENSION_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))){
							this.addDataToStringBuilder(contentBuilder, productScanCodeExtent.getProdDescriptionText());
						}
					}
				}
			}

		} else if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_PRODUCT_MAINTENANCE.getValue().longValue()) {
			if (physicalAttributeId == 523) {
				ProductMaster newProductMaster = null;
				List<SellingUnit> sellingUnits = productMaster.getSellingUnits();
				if (CollectionUtils.isNotEmpty(sellingUnits)) {
					for (SellingUnit sellingUnit : sellingUnits) {
						if(sellingUnit.getUpc() == productMaster.getProductPrimaryScanCodeId().longValue()){
							newProductMaster = sellingUnit.getProductMaster();
							break;
						}
					}
				}
				if (newProductMaster != null && newProductMaster.getProductBrand() != null) {
					contentBuilder.append(newProductMaster.getProductBrand().getDisplayName());
				}
			} else if (physicalAttributeId == 1642) {
				this.addLogicalAttributeDataToStringBuilder(physicalAttributeId, sourceSystemId, productMaster, contentBuilder);
			}
		}else{
			this.addLogicalAttributeDataToStringBuilder(physicalAttributeId, sourceSystemId, productMaster, contentBuilder);
		}
		if (StringUtils.isNotBlank(contentBuilder.toString())) {
			returnString = contentBuilder.toString();
		}
		return returnString;
	}

	/**
     * Get source system content of Display Name logical attribute.
     * @param physicalAttributeId - The physical attribute id.
     * @param sourceSystemId      - The sourcce system id.
     * @param productMaster       - The product master.
     * @return The String type.
     */
	private String getDataSourceSystemForDisplayName(long physicalAttributeId, long sourceSystemId, ProductMaster productMaster) {
		String returnString = null;
		StringBuilder contentBuilder = new StringBuilder();
		List<ProductScanCodeExtent> productScanCodeExtents = productMaster.getProductScanCodeExtents();
		if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_GLADSON.getValue().longValue()) {
			if (CollectionUtils.isNotEmpty(productScanCodeExtents)) {
				for (ProductScanCodeExtent productScanCodeExtent : productScanCodeExtents) {
					if(ProductScanCodeExtent.IDESC_DIMENSION_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))){
						this.addDataToStringBuilder(contentBuilder, productScanCodeExtent.getProdDescriptionText());
					}
				}
			}
		} else if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_PRODUCT_MAINTENANCE.getValue().longValue()) {
			if (physicalAttributeId == 310) {
				ProductMaster newProductMaster = null;
				List<ProductDescription> productDescriptions = productMaster.getProductDescriptions();
				if (CollectionUtils.isNotEmpty(productDescriptions)) {
					for (ProductDescription productDescription : productDescriptions) {
						if(DescriptionType.Codes.TAG_CUSTOMER_FRIENDLY_LINE_ONE.getId().trim().equalsIgnoreCase(StringUtils.trim(productDescription.getKey().getDescriptionType()))
								&& ProductDescription.ENGLISH.equalsIgnoreCase(StringUtils.trim(productDescription.getKey().getLanguageType()))){
							this.addDataToStringBuilder(contentBuilder, productDescription.getDescription());
						}
						if(DescriptionType.Codes.TAG_CUSTOMER_FRIENDLY_LINE_TWO.getId().trim().equalsIgnoreCase(StringUtils.trim(productDescription.getKey().getDescriptionType()))
								&& ProductDescription.ENGLISH.equalsIgnoreCase(StringUtils.trim(productDescription.getKey().getLanguageType()))){
							this.addDataToStringBuilder(contentBuilder, productDescription.getDescription());
						}
					}
				}
			} else if (physicalAttributeId == 1601) {
				if (CollectionUtils.isNotEmpty(productScanCodeExtents)) {
					for (ProductScanCodeExtent productScanCodeExtent : productScanCodeExtents) {
						if(ProductScanCodeExtent.ESHRT_DIMENSION_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))){
							this.addDataToStringBuilder(contentBuilder, productScanCodeExtent.getProdDescriptionText());
						}
					}
				}
			}
		}else{
			this.addLogicalAttributeDataToStringBuilder(physicalAttributeId, sourceSystemId, productMaster, contentBuilder);
		}
		if (StringUtils.isNotBlank(contentBuilder.toString())) {
			returnString = contentBuilder.toString();
		}
		return returnString;
	}

	/**
     * Get source system content of Size logical attribute.
     * @param physicalAttributeId - The physical attribute id.
     * @param sourceSystemId      - The sourcce system id.
     * @param productMaster       - The product master.
     * @return The String type.
     */
	private String getDataSourceSystemForSize(long physicalAttributeId, long sourceSystemId, ProductMaster productMaster) {
		String returnString = null;
		StringBuilder contentBuilder = new StringBuilder();
		List<ProductScanCodeExtent> productScanCodeExtents = productMaster.getProductScanCodeExtents();
		List<MasterDataExtensionAttribute> exportUpcMasterDataExtensionAttributes = productMaster.getExportUpcMasterDataExtensionAttributes();
		if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_GLADSON.getValue().longValue()) {
			if (CollectionUtils.isNotEmpty(productScanCodeExtents)) {
				for (ProductScanCodeExtent productScanCodeExtent : productScanCodeExtents) {
					if (physicalAttributeId == 1606) {
						if(ProductScanCodeExtent.ESIZE_DIMENSION_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))){
							this.addDataToStringBuilder(contentBuilder, productScanCodeExtent.getProdDescriptionText());
						}
					}else{
						if(ProductScanCodeExtent.ISIZE_DIMENSION_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))
								|| ProductScanCodeExtent.IUOM_DIMENSION_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))){
							this.addDataToStringBuilder(contentBuilder, productScanCodeExtent.getProdDescriptionText());
						}
					}
				}
			}
		} else if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_PRODUCT_MAINTENANCE.getValue().longValue()) {
			if (physicalAttributeId == 313) {
				List<SellingUnit> sellingUnits = productMaster.getSellingUnits();
				if (CollectionUtils.isNotEmpty(sellingUnits)) {
					for (SellingUnit sellingUnit : sellingUnits) {
						if(sellingUnit.getUpc() == productMaster.getProductPrimaryScanCodeId().longValue()){
							if (sellingUnit.getQuantity() != null) {
								contentBuilder.append(sellingUnit.getQuantity());
							}
							if (sellingUnit.getRetailUnitOfMeasure() != null && sellingUnit.getRetailUnitOfMeasure().getDescription() != null) {
								contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR).append(sellingUnit.getRetailUnitOfMeasure().getDescription());
							}
							break;
						}
					}
				}
			} else if (physicalAttributeId == 1636) {
				this.addLogicalAttributeDataToStringBuilder(physicalAttributeId, sourceSystemId, productMaster, contentBuilder);
			} else if (physicalAttributeId == 591) {
				List<SellingUnit> sellingUnits = productMaster.getSellingUnits();
				if (CollectionUtils.isNotEmpty(sellingUnits)) {
					for (SellingUnit sellingUnit : sellingUnits) {
						if(sellingUnit.getUpc() == productMaster.getProductPrimaryScanCodeId().longValue()){
							if (sellingUnit.getTagSize() != null) {
								contentBuilder.append(sellingUnit.getTagSize());
							}
							break;
						}
					}
				}
			}
		} else if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_GS1.getValue().longValue()) {
			if (CollectionUtils.isNotEmpty(exportUpcMasterDataExtensionAttributes)) {
				for (MasterDataExtensionAttribute masterDataExtensionAttribute : exportUpcMasterDataExtensionAttributes) {
					if (sourceSystemId == masterDataExtensionAttribute.getKey().getDataSourceSystem().longValue()){
						if(Attribute.NET_CONTENT == masterDataExtensionAttribute.getKey().getAttributeId().longValue()) {
							this.addDataToStringBuilder(contentBuilder, masterDataExtensionAttribute.getAttributeValueNumber().toString());
						}
						if(Attribute.NET_CONTENT_UNIT_OF_MEASURE == masterDataExtensionAttribute.getKey().getAttributeId().longValue()) {
							this.addDataToStringBuilder(contentBuilder, masterDataExtensionAttribute.getAttributeValueText());
						}
					}
				}
			}
		} else if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_KWIKEE.getValue().longValue()) {
			if (CollectionUtils.isNotEmpty(exportUpcMasterDataExtensionAttributes)) {
				for (MasterDataExtensionAttribute masterDataExtensionAttribute : exportUpcMasterDataExtensionAttributes) {
					if (sourceSystemId == masterDataExtensionAttribute.getKey().getDataSourceSystem().longValue()){
						if(Attribute.SIZE == masterDataExtensionAttribute.getKey().getAttributeId().longValue()) {
							this.addDataToStringBuilder(contentBuilder, masterDataExtensionAttribute.getAttributeValueText());
						}
						if(Attribute.PRODUCT_UOM == masterDataExtensionAttribute.getKey().getAttributeId().longValue()) {
							this.addDataToStringBuilder(contentBuilder, masterDataExtensionAttribute.getAttributeValueText());
						}
					}
				}
			}
		} else{
			this.addLogicalAttributeDataToStringBuilder(physicalAttributeId, sourceSystemId, productMaster, contentBuilder);
		}
		if (StringUtils.isNotBlank(contentBuilder.toString())) {
			returnString = contentBuilder.toString();
		}
		return returnString;
	}

	/**
     * Get source system content of Ingredient logical attribute.
     * @param physicalAttributeId - The physical attribute id.
     * @param sourceSystemId      - The sourcce system id.
     * @param productMaster       - The product master.
     * @return The String type.
     */
	private String getDataSourceSystemForIngredient(long physicalAttributeId, long sourceSystemId, ProductMaster productMaster) {
		String returnString = null;
		StringBuilder contentBuilder = new StringBuilder();
		List<ProductScanCodeExtent> productScanCodeExtents = productMaster.getProductScanCodeExtents();
		if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_GLADSON.getValue().longValue()) {
			if (CollectionUtils.isNotEmpty(productScanCodeExtents)) {
				for (ProductScanCodeExtent productScanCodeExtent : productScanCodeExtents) {
					if(ProductScanCodeExtent.INGREDIENTS_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))){
						this.addDataToStringBuilder(contentBuilder, productScanCodeExtent.getProdDescriptionText());
					}
					if(ProductScanCodeExtent.GUARANTEED_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))){
						this.addDataToStringBuilder(contentBuilder, productScanCodeExtent.getProdDescriptionText());
					}
				}
			}
		} else if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_SCALE_MANAGEMENT.getValue().longValue()) {
			ExportScaleUpc exportScaleUpc = productMaster.getExportScaleUpc();
			if(exportScaleUpc != null){
				List<IngredientStatementDetail> ingredientStatementDetails = exportScaleUpc.getIngredientStatementDetails();
				if (CollectionUtils.isNotEmpty(ingredientStatementDetails)) {
					for (IngredientStatementDetail ingredientStatementDetail : ingredientStatementDetails) {
						Ingredient ingredient = ingredientStatementDetail.getIngredient();
						if(ingredient != null && !(Ingredient.DELETE_SW.equals(ingredient.getMaintFunction()))){
							this.addDataToStringBuilder(contentBuilder, ingredient.getDisplayText());
						}
					}
				}
			}
		} else if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_OBPS.getValue().longValue()) {
			if (physicalAttributeId == Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_INGREDIENTS.getValue().longValue()) {
				this.addLogicalAttributeDataToStringBuilder(physicalAttributeId, sourceSystemId, productMaster, contentBuilder);
			} else if (physicalAttributeId == 1803) {
				this.addLogicalAttributeDataToStringBuilder(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_INGREDIENTS.getValue(), sourceSystemId,
						productMaster, contentBuilder);
				if (CollectionUtils.isNotEmpty(productScanCodeExtents)) {
					for (ProductScanCodeExtent productScanCodeExtent : productScanCodeExtents) {
						if(ProductScanCodeExtent.GUARANTEED_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))){
							this.addDataToStringBuilder(contentBuilder, productScanCodeExtent.getProdDescriptionText());
						}
					}
				}
			}
		} else{
			this.addLogicalAttributeDataToStringBuilder(physicalAttributeId, sourceSystemId, productMaster, contentBuilder);
		}
		if (StringUtils.isNotBlank(contentBuilder.toString())) {
			returnString = contentBuilder.toString();
		}
		return returnString;
	}

	/**
     * Get source system content of Direction logical attribute.
     * @param physicalAttributeId - The physical attribute id.
     * @param sourceSystemId      - The sourcce system id.
     * @param productMaster       - The product master.
     * @return The String type.
     */
	private String getDataSourceSystemForDirection(long physicalAttributeId, long sourceSystemId, ProductMaster productMaster) {
		String returnString = null;
		StringBuilder contentBuilder = new StringBuilder();
		List<ProductScanCodeExtent> productScanCodeExtents = productMaster.getProductScanCodeExtents();
		if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_GLADSON.getValue().longValue()) {
			if (CollectionUtils.isNotEmpty(productScanCodeExtents)) {
				for (ProductScanCodeExtent productScanCodeExtent : productScanCodeExtents) {
					if(ProductScanCodeExtent.DIRECTION_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))){
						this.addDataToStringBuilder(contentBuilder, productScanCodeExtent.getProdDescriptionText());
					}
				}
			}
		} else{
			this.addLogicalAttributeDataToStringBuilder(physicalAttributeId, sourceSystemId, productMaster, contentBuilder);
		}
		if (StringUtils.isNotBlank(contentBuilder.toString())) {
			returnString = contentBuilder.toString();
		}
		return returnString;
	}

	/**
     * Get source system content of Warning logical attribute.
     * @param physicalAttributeId - The physical attribute id.
     * @param sourceSystemId      - The sourcce system id.
     * @param productMaster       - The product master.
     * @return The String type.
     */
	private String getDataSourceSystemForWarning(long physicalAttributeId, long sourceSystemId, ProductMaster productMaster) {
		String returnString = null;
		StringBuilder contentBuilder = new StringBuilder();
		List<ProductScanCodeExtent> productScanCodeExtents = productMaster.getProductScanCodeExtents();
		if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_GLADSON.getValue().longValue()) {
			if (CollectionUtils.isNotEmpty(productScanCodeExtents)) {
				for (ProductScanCodeExtent productScanCodeExtent : productScanCodeExtents) {
					if(ProductScanCodeExtent.WARNING_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))){
						this.addDataToStringBuilder(contentBuilder, productScanCodeExtent.getProdDescriptionText());
					}
				}
			}
		} else if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_OBPS.getValue().longValue()) {
			this.addLogicalAttributeDataToStringBuilder(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_WARNINGS.getValue(), sourceSystemId,
					productMaster, contentBuilder);
			this.addLogicalAttributeDataToStringBuilder(Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_ALLERGENS.getValue(), sourceSystemId,
					productMaster, contentBuilder);
		} else{
			this.addLogicalAttributeDataToStringBuilder(physicalAttributeId, sourceSystemId, productMaster, contentBuilder);
		}
		if (StringUtils.isNotBlank(contentBuilder.toString())) {
			returnString = contentBuilder.toString();
		}
		return returnString;
	}

	/**
     * Get source system content of Romance Copy logical attribute.
     * @param physicalAttributeId - The physical attribute id.
     * @param sourceSystemId      - The sourcce system id.
     * @param productMaster       - The product master.
     * @return The String type.
     */
	private String getDataSourceSystemForRomanceCopy(long physicalAttributeId, long sourceSystemId, ProductMaster productMaster) {
		String returnString = null;
		StringBuilder contentBuilder = new StringBuilder();
		List<ProductScanCodeExtent> productScanCodeExtents = productMaster.getProductScanCodeExtents();
		if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_GLADSON.getValue().longValue()) {
			if (CollectionUtils.isNotEmpty(productScanCodeExtents)) {
				for (ProductScanCodeExtent productScanCodeExtent : productScanCodeExtents) {
					if(ProductScanCodeExtent.PDETL_PRODUCT_DESCRIPTION_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))){
						this.addDataToStringBuilder(contentBuilder, productScanCodeExtent.getProdDescriptionText());
					}
					if(ProductScanCodeExtent.INDCN_PRODUCT_DESCRIPTION_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))){
						this.addDataToStringBuilder(contentBuilder, productScanCodeExtent.getProdDescriptionText());
					}
				}
			}
		} else if (sourceSystemId == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_PRODUCT_MAINTENANCE.getValue().longValue()) {
			if (CollectionUtils.isNotEmpty(productScanCodeExtents)) {
				for (ProductScanCodeExtent productScanCodeExtent : productScanCodeExtents) {
					if(ProductScanCodeExtent.ELONG_PRODUCT_DESCRIPTION_CODE.equals(StringUtils.trimToEmpty(productScanCodeExtent.getKey().getProdExtDataCode()))){
						this.addDataToStringBuilder(contentBuilder, productScanCodeExtent.getProdDescriptionText());
					}
				}
			}
		} else{
			this.addLogicalAttributeDataToStringBuilder(physicalAttributeId, sourceSystemId, productMaster, contentBuilder);
		}
		if (StringUtils.isNotBlank(contentBuilder.toString())) {
			returnString = contentBuilder.toString();
		}
		return returnString;
	}

	/**
     * Add logical attribute data into StringBuilder.
     * @param physicalAttributeId - The physical attribute id.
     * @param sourceSystemId      - The sourcce system id.
     * @param productMaster       - The product master.
     * @param contentBuilder 	  - The StringBuilder type.
     */
	private void addLogicalAttributeDataToStringBuilder(long physicalAttributeId, long sourceSystemId, ProductMaster productMaster,
			StringBuilder contentBuilder) {
		String attributeData = this.getLogicalAttributeDataFromMasterDataExtensionAttribute(physicalAttributeId, sourceSystemId, productMaster);
		if(attributeData != null){
			contentBuilder.append(attributeData);
		}
	}

	/**
     * Add data into StringBuilder follow right format.
     * @param contentBuilder 	  - The StringBuilder type.
     * @param data      		  - The String type.
     */
	private void addDataToStringBuilder(StringBuilder contentBuilder, String data) {
		if (StringUtils.isNotBlank(contentBuilder.toString())) {
			contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR);
		}
		if (data != null) {
			contentBuilder.append(data);
		}
	}

	/**
     * Get source system content from MasterDataExtensionAttribute table.
     * @param physicalAttributeId - The physical attribute id.
     * @param sourceSystemId      - The sourcce system id.
     * @param productMaster       - The product master.
     * @return The String type.
     */
	private String getLogicalAttributeDataFromMasterDataExtensionAttribute(long physicalAttributeId, long sourceSystemId, ProductMaster productMaster) {
		String returnString = null;
		List<MasterDataExtensionAttribute> exportUpcMasterDataExtensionAttributes = productMaster.getExportUpcMasterDataExtensionAttributes();
		if (CollectionUtils.isNotEmpty(exportUpcMasterDataExtensionAttributes)) {
			for (MasterDataExtensionAttribute masterDataExtensionAttribute : exportUpcMasterDataExtensionAttributes) {
				if (sourceSystemId == masterDataExtensionAttribute.getKey().getDataSourceSystem().longValue() 
						&& physicalAttributeId == masterDataExtensionAttribute.getKey().getAttributeId().longValue()) {
					returnString = StringUtils.trimToEmpty(masterDataExtensionAttribute.getAttributeValueText());
				}
			}
		}
		return returnString;
	}
}
