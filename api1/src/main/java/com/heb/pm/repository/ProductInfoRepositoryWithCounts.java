package com.heb.pm.repository;

import com.heb.pm.entity.ProductMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The interface Product info repository.
 */
public interface ProductInfoRepositoryWithCounts extends JpaRepository<ProductMaster, Long>, ProductInfoRepositoryCommon{

    /**
     * Search prod_del by a list of product IDs.
     *
     * @param productIds  The list of prodcut IDs to search by.
     * @param pageRequest Page information to include page, page size, and sort order.
     * @return A page of records from prod master. Will include total available record counts and number of available pages.
     */
    Page<ProductMaster> findByProdIdIn(@Param("productIds") List<Long> productIds, Pageable pageRequest);

    /**
     * Find by upcs List.
     *
     * @param upcs       the upcs
     * @param upcRequest the upc request
     * @return A page of records from prod master. Will include total available record counts and number of available pages.
     * */
    Page<ProductMaster> findBySellingUnitsUpcIn(@Param("upcs") List<Long> upcs, Pageable upcRequest);

    /**
     * Find by itemCodes list.
     *
     * @param itemCodes       the itemCodes
	 * @param itemType the itemType.
     * @param itemCodeRequest the itemCode request
     * @return A page of records from prod master. Will include total available record counts and number of available pages.
     * */
    Page<ProductMaster> findByProdItemsItemMasterKeyItemCodeInAndProdItemsItemMasterKeyItemType(
    		List<Long> itemCodes, String itemType, Pageable itemCodeRequest);

    /**
     * Find by department with count page.
     *
     * @param departmentCode the department
     * @param request        the request
     * @return A page of records from prod master. Will include total available record counts and number of available pages.
     * */
    Page<ProductMaster> findByDepartmentCode(@Param("departmentCode") String departmentCode, Pageable request);

    /**
     * Find by department and sub department list.
     *
     * @param departmentCode the department
     * @param subDepartment  the sub department
     * @param request        the request
     * @return A page of records from prod master. Will include total available record counts and number of available pages.
     * */
    Page<ProductMaster> findByDepartmentCodeAndSubDepartmentCode(@Param("departmentCode") String departmentCode,@Param("subDepartmentCode") String subDepartment, Pageable request);

    /**
     * Find by class code page.
     *
     * @param classCode the class code
     * @param request   the request
     * @return A page of records from prod master. Will include total available record counts and number of available pages.
     * */
    Page<ProductMaster> findByClassCode(@Param("classCode") int classCode, Pageable request);

    /**
     * Find by class code and commodity code page.
     *
     * @param classCode     the class code
     * @param commodityCode the commodity code
     * @param request       the request
     * @return A page of records from prod master. Will include total available record counts and number of available pages.
     * */
    Page<ProductMaster> findByClassCodeAndCommodityCode(@Param("classCode")int classCode,@Param("commodityCode") int commodityCode, Pageable request);

    /**
     * Find by class code and commodity code and sub commodity code page.
     *
     * @param classCode        the class code
     * @param commodityCode    the commodity code
     * @param subCommodityCode the sub commodity code
     * @param request          the request
     * @return A page of records from prod master. Will include total available record counts and number of available pages.
     * */
    Page<ProductMaster> findByClassCodeAndCommodityCodeAndSubCommodityCode(@Param("classCode")int classCode,@Param("commodityCode") int commodityCode,
                                                 @Param("subCommodityCode") int subCommodityCode, Pageable request);

    /**
     * Find by class commodity bdm code page.
     *
     * @param bdmCode the bdm code
     * @param request the request
     * @return A page of records from prod master. Will include total available record counts and number of available pages.
     * */
    Page<ProductMaster> findByClassCommodityBdmCode(@Param("bdmCode") String bdmCode, Pageable request);

    /**
     * Find page by MRT item code.
     *
     * @param itemCode the item code
     * @param request the request
     * @return A page of records from prod master. Will include total available record counts and number of available pages.
     * */
    Page<ProductMaster> findByProdItemsItemMasterKeyItemCodeAndProdItemsItemMasterMrt(Long itemCode, boolean mrt, Pageable request);

    /**
     * Find page by MRT case UPC.
     *
     * @param caseUpc the case UPC
     * @param request the request
     * @return A page of records from prod master. Will include total available record counts and number of available pages.
     * */
    Page<ProductMaster> findByProdItemsItemMasterCaseUpcAndProdItemsItemMasterMrt(Long caseUpc, boolean mrt, Pageable request);

	/**
	 * Find list by Location ap vendor number where ap type is "AP" or "DS".
	 * --(m314029) Added custom query because the count for the distinct objects did not match the size of the list of
	 * objects returned (i.e. count query returned 844, but there were only 277 records)
	 *
	 * @param apVendorNumber the ap vendor number
	 * @param apTypeCodes "AP"(Warehouse) or "DS"(DSD)
	 * @param request the request
	 * @return A page of records from prod master. Will include total available record counts and number of available pages.
	 */
	@Query(value = PRODUCT_QUERY_FOR_VENDOR)
    Page<ProductMaster> findExistsByProdItemsItemMasterVendorLocationItemsLocationApVendorNumberAndProdItemsItemMasterVendorLocationItemsLocationApTypeCodeIn(@Param("apVendorNumber") int apVendorNumber,@Param("apTypeCodes") List<String> apTypeCodes, Pageable request);

    /**
     * Finds products whose description match the phrase.
     * @param description the phrase to be compared to products descriptions
     * @param request the request
     * @return A page of products whose descriptions have the phrase
     */
    @Query(value = PRODUCT_DESCRIPTION_QUERY)
    Page<ProductMaster> findByDescription(@Param("description") String description, Pageable request);

	/**
	 * Find product masters by goods product vertex tax category codes.
	 *
	 * @param vertexTaxCategoryCode Vertex tax category code to search for.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of records from prod master.
	 */
	Page<ProductMaster> findByGoodsProductVertexTaxCategoryCodeOrderByProdId(String vertexTaxCategoryCode, Pageable pageRequest);
}
