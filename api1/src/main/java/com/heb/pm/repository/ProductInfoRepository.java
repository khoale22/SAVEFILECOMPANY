package com.heb.pm.repository;

import com.heb.pm.entity.ProductMaster;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The interface Product info without counts repository.
 * Updated by s753601 on 3/24/2017
 * @since 2.4.0
 */
public interface ProductInfoRepository extends JpaRepository<ProductMaster, Long>, ProductInfoRepositoryCommon{

	String DISTINCT_VERTEX_TAX_CATEGORIES_BY_SUB_COMMODITY = "select distinct " +
			"productMaster.goodsProduct.vertexTaxCategoryCode from ProductMaster productMaster " +
			"where productMaster.subCommodityCode = :subCommodityCode";
	/**
	 * Query to find product id by alert type and search criteria session. It use to count product id in Hits at nutrient task screen.
	 */
	static final String QUERY_FIND_PRODUCT_ID_IN_ALERT = "select CAST(alertStaging.alertKey AS long) from AlertStaging  alertStaging , " +
			"SearchCriteria searchCriteria where alertStaging.alertTypeCD = :alertTypeCD and alertStaging.alertStatusCD = :alertStatusCD " +
			"and TRIM(alertStaging.trimmedAlertKey) = CAST(searchCriteria.productId AS string) and searchCriteria.sessionId = :sessionId and searchCriteria.productId>0";

	/**
	 * Search prod_del by a list of product IDs.
	 *
	 * @param productIds  The list of prodcut IDs to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of records from prod master.
	 */
	List<ProductMaster> findByProdIdIn(@Param("productIds") List<Long> productIds, Pageable pageRequest);

	/**
	 * Find by upcs List.
	 *
	 * @param upcs       the upcs
	 * @param upcRequest the upc request
	 * @return A list of records from prod master.
	 * */
	List<ProductMaster> findBySellingUnitsUpcIn(@Param("upcs") List<Long> upcs, Pageable upcRequest);

	/**
	 * Find by itemCodes list.
	 *
	 * @param itemCodes       the itemCodes
	 * @param itemType the itemType.
	 * @param itemCodeRequest the itemCode request
	 * @return A list of records from prod master.
	 * */
	List<ProductMaster> findByProdItemsItemMasterKeyItemCodeInAndProdItemsItemMasterKeyItemType(
			List<Long> itemCodes, String itemType, Pageable itemCodeRequest);

	/**
	 * Find by department with count page.
	 *
	 * @param departmentCode the department
	 * @param request        the request
	 * @return A list of records from prod master.
	 * */
	List<ProductMaster> findByDepartmentCode(@Param("departmentCode") String departmentCode, Pageable request);

	/**
	 * Find by department and sub department list.
	 *
	 * @param departmentCode the department
	 * @param subDepartment  the sub department
	 * @param request        the request
	 * @return A list of records from prod master.
	 * */
	List<ProductMaster> findByDepartmentCodeAndSubDepartmentCode(@Param("departmentCode") String departmentCode,@Param("subDepartmentCode") String subDepartment, Pageable request);

	/**
	 * Find by class code page.
	 *
	 * @param classCode the class code
	 * @param request   the request
	 * @return A list of records from prod master.
	 * */
	List<ProductMaster> findByClassCode(@Param("classCode") int classCode, Pageable request);

	/**
	 * Find by class code and commodity code page.
	 *
	 * @param classCode     the class code
	 * @param commodityCode the commodity code
	 * @param request       the request
	 * @return A list of records from prod master.
	 * */
	List<ProductMaster> findByClassCodeAndCommodityCode(@Param("classCode")int classCode,@Param("commodityCode") int commodityCode, Pageable request);

	/**
	 * Find by class code and commodity code and sub commodity code page.
	 *
	 * @param classCode        the class code
	 * @param commodityCode    the commodity code
	 * @param subCommodityCode the sub commodity code
	 * @param request          the request
	 * @return A list of records from prod master.
	 * */
	List<ProductMaster> findByClassCodeAndCommodityCodeAndSubCommodityCode(@Param("classCode")int classCode,@Param("commodityCode") int commodityCode,
																		   @Param("subCommodityCode") int subCommodityCode, Pageable request);

	/**
	 * Find by class commodity bdm code page.
	 *
	 * @param bdmCode the bdm code
	 * @param request the request
	 * @return A list of records from prod master.
	 * */
	List<ProductMaster> findByClassCommodityBdmCode(@Param("bdmCode") String bdmCode, Pageable request);

	/**
	 * Find list by MRT item code.
	 *
	 * @param itemCode the item code
	 * @param request the request
	 * @return A list of records from prod master.
	 * */
	List<ProductMaster> findByProdItemsItemMasterKeyItemCodeAndProdItemsItemMasterMrt(Long itemCode, boolean mrt, Pageable request);

	/**
	 * Find list by MRT case UPC.
	 *
	 * @param caseUpc the case UPC
	 * @param request the request
	 * @return A list of records from prod master.
	 * */
	List<ProductMaster> findByProdItemsItemMasterCaseUpcAndProdItemsItemMasterMrt(Long caseUpc, boolean mrt, Pageable request);

	/**
	 * Find list by Location ap vendor number where ap type is "AP" or "DS".
	 * --(m314029) Added custom query because the count for the distinct objects did not match the size of the list of
	 * objects returned (i.e. count query returned 844, but there were only 277 records)
	 *
	 * @param apVendorNumber the ap vendor number
	 * @param apTypeCodes "AP"(Warehouse) or "DS"(DSD)
	 * @param request the request
	 * @return A list of records from prod master.
	 */
	@Query(value = PRODUCT_QUERY_FOR_VENDOR)
	List<ProductMaster> findExistsByProdItemsItemMasterVendorLocationItemsLocationApVendorNumberAndProdItemsItemMasterVendorLocationItemsLocationApTypeCodeIn(@Param("apVendorNumber") int apVendorNumber,@Param("apTypeCodes") List<String> apTypeCodes, Pageable request);

	/**
	 * Find products whose description matches the phrase
	 * @param description the phrase to be compared to products descriptions
	 * @param request the request
	 * @return A list of products whose descriptions have the phrase
	 */
	@Query(value = PRODUCT_DESCRIPTION_QUERY)
	List<ProductMaster> findByDescription(@Param("description") String description, Pageable request);

	/**
	 * Find by upcs List.
	 *
	 * @param upc       the upc
	 * @return A list of records from prod master.
	 * */
	ProductMaster findBySellingUnitsUpc(Long upc);

	/**
	 * Returns the list of product IDs  that exist that are part of a user's search criteria in their search session.
	 *
	 * @param sessionId The ID of the user's session.
	 * @return A list of product IDs that match the list the user has provided.
	 */
	@Query(value="select productMaster.prodId from ProductMaster  productMaster , SearchCriteria searchCriteria where productMaster.prodId = searchCriteria.productId and searchCriteria.sessionId = :sessionId and searchCriteria.productId>0")
	List<Long> findAllProductIdsForSearch(@Param("sessionId") String sessionId);

	@Query(value = "select productMaster.prodId from ProductMaster productMaster where productMaster.subCommodityCode=:subCommodityCode")
	List<Long> findProductIdBySubCommodityCode(@Param("subCommodityCode") Integer subCommodityCode);

	@Query(value = "select productMaster.prodId from ProductMaster productMaster where productMaster.productPrimaryScanCodeId in :productPrimaryScanCodeId")
	List<Long> findProductIdByProductPrimaryScanCodeId(@Param("productPrimaryScanCodeId") List<Long> productPrimaryScanCodeId);

	/**
	 * Find products whose retail link matches the given retail link;
	 *
	 * @param retailLink Retail link to search for.
	 * @return List of matching records from product master.
	 */
	List<ProductMaster> findByRetailLink(Long retailLink);

	/**
	 * Find distinct goods product's vertex tax categories by sub commodity code.
	 *
	 * @param subCommodityCode The sub commodity code to search for.
	 * @return List of vertex tax category codes matching search.
	 */
	@Query(value = DISTINCT_VERTEX_TAX_CATEGORIES_BY_SUB_COMMODITY)
	List<String> findDistinctVertexTaxCategoryBySubCommodityCode(@Param("subCommodityCode") Integer subCommodityCode);

	/**
	 * Find product masters by goods product vertex tax category codes.
	 *
	 * @param vertexTaxCategoryCode Vertex tax category code to search for.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of records from prod master.
	 */
	List<ProductMaster> findByGoodsProductVertexTaxCategoryCodeOrderByProdId(String vertexTaxCategoryCode, Pageable pageRequest);

	/**
	 * Find alert by product IDs. Return the list of product IDs that exist that are part of a user's search criteria in
	 * their search session.
	 *
	 * @param alertTypeCD   the alert type.
	 * @param alertStatusCD the alert status.
	 * @param sessionId     the session Id of search nutrient updates.
	 * @return A list of product IDs that match the list the user has provided.
	 */
	@Query(value = QUERY_FIND_PRODUCT_ID_IN_ALERT)
	List<Long> findAllProductIdsForSearchNutritionUpdate(@Param("alertTypeCD") String alertTypeCD,
														 @Param("alertStatusCD") String alertStatusCD,
														 @Param("sessionId") String sessionId);
}
