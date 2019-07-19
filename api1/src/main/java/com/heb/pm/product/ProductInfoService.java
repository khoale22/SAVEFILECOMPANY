package com.heb.pm.product;

import com.heb.pm.Hits;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.entity.Location;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.repository.ItemMasterRepository;
import com.heb.pm.repository.ProductInfoRepository;
import com.heb.pm.repository.ProductInfoRepositoryWithCounts;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds business logic related to product.
 *
 * @author s573181
 * @since 2.0.1
 */
@Service
public class ProductInfoService {


    private static final String DESCRIPTION_REGEX = "%%%s%%";

    private static final String NULL_ITEM_ERROR = "Item key cannot be null.";
	private static final String NULL_PRODUCT_ERROR = "Product ID cannot be null.";
	private static final String NULL_PAGE_ERROR = "PAGE cannot be null.";
	private static final String NULL_BDM_ERROR = "BDM cannot be null or empty.";
	private static final String NULL_DEPARTMENT_ERROR = "BDM cannot be null or empty.";


	/**
     * The Product info repository.
     */
    @Autowired
    ProductInfoRepositoryWithCounts productInfoRepositoryWithCounts;

	@Autowired
	private ItemMasterRepository itemMasterRepository;

    /**
     * The Product info repository without count.
     */
    @Autowired
    ProductInfoRepository productInfoRepository;

    /**
     * Returns product data for a given product ID.
     *
     * @param productId The product ID to search for.
     * @return Data about that product.
     */
    public ProductMaster findProductInfoByProdId(Long productId){
        if (productId == null){
            throw new IllegalArgumentException(NULL_PRODUCT_ERROR);
        }

        return productInfoRepositoryWithCounts.findOne(productId);
    }

	/**
	 * Returns item data for a given item ID.
	 * @param itemKey the item key to search for.
	 * @return Data about that item.
	 */
	public ItemMaster findItemByItemId(ItemMasterKey itemKey){
		if (itemKey == null){
			throw new IllegalArgumentException(NULL_ITEM_ERROR);
		}
		return itemMasterRepository.findOne(itemKey);
	}

    /**
     * Find by bdm pageable result.
     *
     * @param bdmCode the bdm code
     * @param ic      the ic
     * @param pg      the pg
     * @param ps      the ps
     * @return the pageable result
     */
    public PageableResult<ProductMaster> findByBdm(String bdmCode, boolean ic, int pg, int ps) {
        Pageable request = new PageRequest(pg, ps, this.toSort());

        if (StringUtils.isBlank(bdmCode)) {
            throw new IllegalArgumentException(NULL_BDM_ERROR);
        }

        return ic ? this.searchByBdmWithCounts(bdmCode, request) :
                this.searchByBdmWithoutCounts(bdmCode, request);
    }

    /**
     * Find by sub commodity search pageable result.
     *
     * @param classCode        the class code
     * @param commodityCode    the commodity code
     * @param subCommodityCode the sub commodity code
     * @param ic               the ic
     * @param pg               the pg
     * @param ps               the ps
     * @return the pageable result
     */
    public PageableResult<ProductMaster> findBySubCommoditySearch(int classCode, int commodityCode, int subCommodityCode, boolean ic, int pg, int ps) {
        Pageable request = new PageRequest(pg, ps, this.toSort());


        return ic ? this.searchBySubCommodityWithCounts(classCode, commodityCode, subCommodityCode, request) :
                this.searchBySubCommodityWithoutCounts(classCode, commodityCode, subCommodityCode, request);
    }

    /**
     * Find by sub commodity without counts search pageable result.
     *
     * @param classCode        the class code
     * @param commodityCode    the commodity code
     * @param subCommodityCode the sub commodity code
     * @return the pageable result
     */
    private PageableResult<ProductMaster> searchBySubCommodityWithoutCounts(int classCode, int commodityCode, int subCommodityCode, Pageable request) {
        List<ProductMaster> productMasters = this.productInfoRepository.findByClassCodeAndCommodityCodeAndSubCommodityCode(classCode, commodityCode, subCommodityCode, request);
        return new PageableResult<>(request.getPageNumber(), productMasters);
    }

    /**
     * Find by sub commodity with counts pageable result.
     *
     * @param classCode        the class code
     * @param commodityCode    the commodity code
     * @param subCommodityCode the sub commodity code
     * @return the pageable result
     */
    private PageableResult<ProductMaster> searchBySubCommodityWithCounts(int classCode, int commodityCode, int subCommodityCode, Pageable request) {
        Page<ProductMaster> page = this.productInfoRepositoryWithCounts.findByClassCodeAndCommodityCodeAndSubCommodityCode(classCode, commodityCode, subCommodityCode, request);

        return new PageableResult<>(request.getPageNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getContent());
    }

    /**
     * Handles the bdm search when the user wants counts.
     *
     * @param bdmCode A bdm code to search for records on.
     * @param request The request with page and page size
     * @return An object containing the product records based on search criteria.
     */
    private PageableResult<ProductMaster> searchByBdmWithCounts(String bdmCode, Pageable request) {
        Page<ProductMaster> page = this.productInfoRepositoryWithCounts.findByClassCommodityBdmCode(bdmCode, request);

        return new PageableResult<>(request.getPageNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getContent());

    }

    /**
     * Handles the bdm search when the user does not want counts.
     *
     * @param bdmCode A bdm code to search for records on.
     * @param request The request with page and page size
     * @return An object containing the product records based on search criteria.
     */
    private PageableResult<ProductMaster> searchByBdmWithoutCounts(String bdmCode, Pageable request) {

        List<ProductMaster> productMasters = this.productInfoRepository.findByClassCommodityBdmCode(bdmCode, request);
        return new PageableResult<>(request.getPageNumber(), productMasters);
    }

    /**
     * Find product info by prod i ds pageable result.
     *
     * @param productIds   the product ids
     * @param includeCount the include count
     * @param page         the page
     * @param pageSize     the page size
     * @return the pageable result
     */
    public PageableResult<ProductMaster> findProductInfoByProdIDs(List<Long> productIds,
                                                                  boolean includeCount, int page,
                                                                  int pageSize) {
        Pageable request = new PageRequest(page, pageSize, this.toSort());

        return includeCount ? this.searchByProductIdsWithCounts(productIds, request) :
                this.searchByProductIdWithoutCounts(productIds, request);
    }

    /**
     * Searches for product records based on a list of upcs.
     *
     * @param upcs         The list of upc to search for records on.
     * @param includeCount Set to true to include page and record counts.
     * @param page         The page you are looking for.
     * @param pageSize     The maximum number of records to return.
     * @return An object containing the product records based on search criteria.
     */
    public PageableResult<ProductMaster> findByUpcs(List<Long> upcs,
                                                    boolean includeCount, int page,
                                                    int pageSize) {

        Pageable request = new PageRequest(page, pageSize, this.toSort());

        return includeCount ? this.searchByUpcsWithCounts(upcs, request) :
                this.searchByUpcsWithoutCounts(upcs, request);
    }

    /**
     * Searches for product records based on a list of upcs.
     *
     * @param itemCodes    The list of itemcodes to search for records on.
     * @param includeCount Set to true to include page and record counts.
     * @param page         The page you are looking for.
     * @param pageSize     The maximum number of records to return.
     * @return An object containing the product records based on search criteria.
     */
    public PageableResult<ProductMaster> findByItemCodes(List<Long> itemCodes,
                                                         boolean includeCount, int page,
                                                         int pageSize) {

        Pageable request = new PageRequest(page, pageSize, this.toSort());

        return includeCount ? this.searchByItemCodesWithCounts(itemCodes, request) :
                this.searchByItemCodesWithoutCounts(itemCodes, request);
    }

    /**
     * Find by sub department pageable result.
     *
     * @param departmentCode    the department code
     * @param subDepartmentCode the sub department code
     * @param pg                the pg
     * @param ps                the ps
     * @param ic                the ic
     * @return the pageable result
     */
    public PageableResult<ProductMaster> findBySubDepartment(String departmentCode,String subDepartmentCode, int pg, int ps, boolean ic) {

        Pageable request = new PageRequest(pg, ps, this.toSort());

        return this.findBySubDepartments(departmentCode, subDepartmentCode, request, ic);

    }

    /**
     * Searches for product records based on class and commodity.
     *
     * @param classCode     A class to search for records on.
     * @param commodityCode A commodity to search for records on.
     * @param pg            the pg
     * @param ps            the ps
     * @param includeCount  Set to true to include page and record counts.
     * @return An object containing the product records based on search criteria.
     */
    public PageableResult<ProductMaster> findByClassAndCommodity(int classCode, int commodityCode, int pg, int ps,
                                                                 boolean includeCount) {
        Pageable request = new PageRequest(pg, ps, this.toSort());

        if(commodityCode == 0) {
            return includeCount ? this.searchByClassWithCounts(classCode, request) :
                    this.searchByClassWithoutCounts(classCode, request);
        } else {
            return includeCount ?
                    this.searchByClassAndCommodityWithCounts(classCode, commodityCode, request) :
                    this.searchByClassAndCommodityWithoutCounts(classCode, commodityCode, request);
        }
    }

    /**
     * Searches for product records based on sub department.
     *
     * @param departmentCode    A department to search for records on.
     * @param subDepartmentCode A sub department to search for records on.
     * @param request           Holds paging information about the request.
     * @param includeCount      the include count
     * @return An object containing the product records based on search criteria.
     */
    public PageableResult<ProductMaster> findBySubDepartments(String departmentCode, String subDepartmentCode,
                                                              Pageable request, boolean includeCount) {
        if (StringUtils.isBlank(departmentCode)) {
            throw new IllegalArgumentException(NULL_DEPARTMENT_ERROR);
        }
        if (request == null) {
            throw new IllegalArgumentException(NULL_PAGE_ERROR);
        }

        if(StringUtils.isBlank(subDepartmentCode)){
            return includeCount ? this.searchByDepartmentWithCounts(departmentCode, request) :
                    this.searchByDepartmentWithoutCounts(departmentCode, request);
        } else {
            return includeCount ?
                    this.searchByDepartmentAndSubDepartmentWithCounts(departmentCode, subDepartmentCode, request) :
                    this.searchByDepartmentAndSubDepartmentWithoutCounts(departmentCode, subDepartmentCode, request);
        }
    }

    /**
     * Returns the Hits count with match, non-match and non-match products in the input
     *
     * @param productIds the products
     * @return Hits hits
     */
    public Hits findHitsByProducts(List<Long> productIds) {
        List<ProductMaster> pmList = this.productInfoRepositoryWithCounts.findAll(productIds);
        List<Long> productList = pmList.stream().map(ProductMaster::getProdId).collect(Collectors.toList());
        return Hits.calculateHits(productIds, productList);
    }

	/**
	 * Returns a page of product master data for an MRT by item code.
	 *
	 * @param itemCode The MRT item code look for data about.
	 * @param includeCount Whether or not to include total record and page counts.
	 * @param pg The page of data to pull.
	 * @param ps The number of records being asked for.
	 * @return A list of product data.
	 */
	public PageableResult<ProductMaster> findByMrtItemCode(Long itemCode, boolean includeCount, int pg, int ps) {
		Pageable request = new PageRequest(pg, ps, this.toSort());
		return includeCount ? this.searchByMrtItemCodeWithCounts(itemCode, request) :
				this.searchByMrtItemCodeWithoutCounts(itemCode, request);
	}

	/**
	 * Returns a page of product master data for an MRT by case pack UPC.
	 *
	 * @param casePack The MRT case pack UPC look for data about.
	 * @param includeCount Whether or not to include total record and page counts.
	 * @param pg The page of data to pull.
	 * @param ps The number of records being asked for.
	 * @return A list of product data.
	 */
	public PageableResult<ProductMaster> findByMrtCasePack(Long casePack, boolean includeCount, int pg, int ps) {
		Pageable request = new PageRequest(pg, ps, this.toSort());
		return includeCount ? this.searchByMrtCasePackWithCounts(casePack, request) :
				this.searchByMrtCasePackWithoutCounts(casePack, request);
	}

	/**
	 * Returns a page of product master data for a vendor.
	 * @param vendorNumber The vendor number to search for.
	 * @param includeCount Whether or not to include total record and page counts.
	 * @param pg The page of data to pull.
	 * @param ps The number of records being asked for.
	 * @return A list of product data.
	 */
	public PageableResult<ProductMaster> findByVendorNumber(int vendorNumber, boolean includeCount, int pg, int ps) {
		Pageable request = new PageRequest(pg, ps, this.toSort());
		return includeCount ? this.searchByVendorWithCounts(vendorNumber, request) :
				this.searchByVendorWithoutCounts(vendorNumber, request);
	}

	/**
	 * Handles the search when the user does not want counts for a vendor.
	 *
	 * @param vendorNumber The vendor number to search for.
	 * @param request The request with page and page size
	 * @return An object containing the product records based on search criteria.
	 */
	private PageableResult<ProductMaster> searchByVendorWithoutCounts(int vendorNumber, Pageable request) {
		List<ProductMaster> productMasters =
				this.productInfoRepository.
						findExistsByProdItemsItemMasterVendorLocationItemsLocationApVendorNumberAndProdItemsItemMasterVendorLocationItemsLocationApTypeCodeIn(
								vendorNumber, new ArrayList<>(Arrays.asList(Location.WAREHOUSE_AP_TYPE_CODE, Location.DSD_AP_TYPE_CODE)), request);
		return new PageableResult<>(request.getPageNumber(), productMasters);
	}

	/**
	 * Handles the search when the user does wants counts for a vendor.
	 *
	 * @param vendorNumber The vendor number to search for.
	 * @param request The request with page and page size
	 * @return An object containing the product records based on search criteria.
	 */
	private PageableResult<ProductMaster> searchByVendorWithCounts(int vendorNumber, Pageable request) {
		Page<ProductMaster> data =
				this.productInfoRepositoryWithCounts.
						findExistsByProdItemsItemMasterVendorLocationItemsLocationApVendorNumberAndProdItemsItemMasterVendorLocationItemsLocationApTypeCodeIn(
								vendorNumber, new ArrayList<>(Arrays.asList(Location.WAREHOUSE_AP_TYPE_CODE, Location.DSD_AP_TYPE_CODE)), request);

		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Handles the search when the user wants counts for an MRT item code.
	 *
	 * @param itemCode An item code to search for records on.
	 * @param request The request with page and page size
	 * @return An object containing the product records based on search criteria.
	 */
	private PageableResult<ProductMaster> searchByMrtItemCodeWithCounts(Long itemCode, Pageable request) {
		Page<ProductMaster> data =
				this.productInfoRepositoryWithCounts.findByProdItemsItemMasterKeyItemCodeAndProdItemsItemMasterMrt(itemCode, true, request);

		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Handles the search when the user does not need counts for an MRT item code.
	 *
	 * @param itemCode An item code to search for records on.
	 * @param request The request with page and page size
	 * @return An object containing the product records based on search criteria.
	 */
	private PageableResult<ProductMaster> searchByMrtItemCodeWithoutCounts(Long itemCode, Pageable request) {
		List<ProductMaster> productMasters =
				this.productInfoRepository.findByProdItemsItemMasterKeyItemCodeAndProdItemsItemMasterMrt(itemCode, true, request);
		return new PageableResult<>(request.getPageNumber(), productMasters);
	}

	/**
	 * Handles the search when the user wants counts for an MRT case pack.
	 *
	 * @param casePack A case pack UPC to search for records on.
	 * @param request The request with page and page size
	 * @return An object containing the product records based on search criteria.
	 */
	private PageableResult<ProductMaster> searchByMrtCasePackWithCounts(Long casePack, Pageable request) {
		Page<ProductMaster> data =
				this.productInfoRepositoryWithCounts.findByProdItemsItemMasterCaseUpcAndProdItemsItemMasterMrt(casePack, true, request);

		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Handles the search when the user does not need counts for an MRT case pack.
	 *
	 * @param casePack A case pack UPC to search for records on.
	 * @param request The request with page and page size
	 * @return An object containing the product records based on search criteria.
	 */
	private PageableResult<ProductMaster> searchByMrtCasePackWithoutCounts(Long casePack, Pageable request) {
		List<ProductMaster> productMasters =
				this.productInfoRepository.findByProdItemsItemMasterCaseUpcAndProdItemsItemMasterMrt(casePack, true, request);
		return new PageableResult<>(request.getPageNumber(), productMasters);
	}

    /**
     * Handles the search when the user wants counts.
     *
     * @param productIds A list of product IDs to search for records on.
     * @param request The request with page and page size
     * @return An object containing the product records based on search criteria.
     */
    private PageableResult<ProductMaster> searchByProductIdsWithCounts(List<Long> productIds, Pageable request) {

        Page<ProductMaster> data = this.productInfoRepositoryWithCounts.findByProdIdIn(productIds, request);

        return new PageableResult<>(request.getPageNumber(),
                data.getTotalPages(),
                data.getTotalElements(),
                data.getContent());

    }

    /**
     * Returns product data for a given product ID.
     *
     * @param productIds The product ID to search for.
     * @param request The request with page and page size
     * @return An object containing the product records based on search criteria.
     */
    private PageableResult<ProductMaster> searchByProductIdWithoutCounts(List<Long> productIds, Pageable request) {

        List<ProductMaster> productMasters = this.productInfoRepository.findByProdIdIn(productIds, request);
        return new PageableResult<>(request.getPageNumber(), productMasters);
    }

    /**
     * Find by upc without counts pageable result.
     *
     * @param upcs    upc to search for records on.
     * @param request The request with page and page size
     * @return the pageable result
     */
    private PageableResult<ProductMaster> searchByUpcsWithoutCounts(List<Long> upcs, Pageable request) {
        List<ProductMaster> data = this.productInfoRepository.findBySellingUnitsUpcIn(upcs, request);

        return new PageableResult<>(request.getPageNumber(), data);
    }

    /**
     * Find by item code without counts pageable result.
     *
     * @param itemCodes    The list of itemcodes to search for records on.
     * @param itemCodeRequest The request with page and page size
     * @return the pageable result
     */
    private PageableResult<ProductMaster> searchByItemCodesWithoutCounts(List<Long> itemCodes, Pageable itemCodeRequest) {
        List<ProductMaster> data = this.productInfoRepository.
				findByProdItemsItemMasterKeyItemCodeInAndProdItemsItemMasterKeyItemType(itemCodes,
						ItemMasterKey.WAREHOUSE, itemCodeRequest);

        return new PageableResult<>(itemCodeRequest.getPageNumber(), data);
    }

    /**
     * Find by upc withcounts pageable result.
     *
     * @param upcs    upc to search for records on.
     * @param request The request with page and page size
     * @return the pageable result
     */
    private PageableResult<ProductMaster> searchByUpcsWithCounts(List<Long> upcs, Pageable request) {
        Page<ProductMaster> data = this.productInfoRepositoryWithCounts.findBySellingUnitsUpcIn(upcs, request);

        return new PageableResult<>(request.getPageNumber(),
                data.getTotalPages(),
                data.getTotalElements(),
                data.getContent());
    }

    /**
     * Find by item codes with counts pageable result.
     *
     * @param itemCodes    The list of item codes to search for records on.
     * @param itemCodeRequest The request with page and page size
     * @return the pageable result
     */
    private PageableResult<ProductMaster> searchByItemCodesWithCounts(List<Long> itemCodes, Pageable itemCodeRequest) {
        Page<ProductMaster> data = this.productInfoRepositoryWithCounts.
				findByProdItemsItemMasterKeyItemCodeInAndProdItemsItemMasterKeyItemType(
						itemCodes, ItemMasterKey.WAREHOUSE, itemCodeRequest);

        return new PageableResult<>(itemCodeRequest.getPageNumber(),
                data.getTotalPages(),
                data.getTotalElements(),
                data.getContent());
    }

    /**
     * Handles the department search when the user does not want counts.
     *
     * @param departmentCode A department to search for records on.
     * @param request The request with page and page size
     * @return An object containing the product records based on search criteria.
     */
    private PageableResult<ProductMaster> searchByDepartmentWithoutCounts(String departmentCode, Pageable request) {
        List<ProductMaster> data =
                this.productInfoRepository.findByDepartmentCode(departmentCode, request);
        return new PageableResult<>(request.getPageNumber(), data);
    }

    /**
     * Handles the department and subDepartment search when the user wants counts.
     *
     * @param departmentCode A department to search for records on.
     * @param subDepartmentCode A subDepartment to search for records on.
     * @param request The request with page and page size
     * @return An object containing the product records based on search criteria.
     */
    private PageableResult<ProductMaster> searchByDepartmentAndSubDepartmentWithCounts(String departmentCode,
                                                                                            String subDepartmentCode,
                                                                                            Pageable request) {
        Page<ProductMaster> data = this.productInfoRepositoryWithCounts.
                findByDepartmentCodeAndSubDepartmentCode(departmentCode, subDepartmentCode, request);
        return new PageableResult<>(request.getPageNumber(),
                data.getTotalPages(),
                data.getTotalElements(),
                data.getContent());
    }

    /**
     * Handles the department search when the user wants counts.
     *
     * @param departmentCode A department to search for records on.
     * @param request The request with page and page size
     * @return An object containing the product records based on search criteria.
     */
    private PageableResult<ProductMaster> searchByDepartmentWithCounts(String departmentCode, Pageable request) {
        Page<ProductMaster> data =
                this.productInfoRepositoryWithCounts.findByDepartmentCode(departmentCode, request);
        return new PageableResult<>(request.getPageNumber(),
                data.getTotalPages(),
                data.getTotalElements(),
                data.getContent());
    }

    /**
     * Handles the class search when the user wants counts.
     *
     * @param classCode A class code to search for records on.
     * @param request The request with page and page size
     * @return An object containing the product records based on search criteria.
     */
    private PageableResult<ProductMaster> searchByClassWithCounts(int classCode, Pageable request) {
        Page<ProductMaster> data= this.productInfoRepositoryWithCounts.findByClassCode(classCode, request);

        return new PageableResult<>(request.getPageNumber(),
                data.getTotalPages(),
                data.getTotalElements(),
                data.getContent());
    }

    /**
     * Handles the department and subDepartment search when the user does not want counts.
     *
     * @param departmentCode A department to search for records on.
     * @param subDepartmentCode A subDepartment to search for records on.
     * @param request The request with page and page size
     * @return An object containing the product records based on search criteria.
     */
    private PageableResult<ProductMaster> searchByDepartmentAndSubDepartmentWithoutCounts(String departmentCode,
                                                                                               String subDepartmentCode,
                                                                                               Pageable request) {
        List<ProductMaster> data = this.productInfoRepository.
                findByDepartmentCodeAndSubDepartmentCode(departmentCode, subDepartmentCode, request);
        return new PageableResult<ProductMaster>(request.getPageNumber(), data);
    }

    /**
     * Handles the class search when the user does not want counts.
     *
     * @param classCode A class code to search for records on.
     * @param request The request with page and page size
     * @return An object containing the product records based on search criteria.
     */
    private PageableResult<ProductMaster> searchByClassWithoutCounts(int classCode, Pageable request) {

        List<ProductMaster> data = this.productInfoRepository.findByClassCode(classCode, request);

        return new PageableResult<>(request.getPageNumber(), data);
    }

    /**
     * Handles the class and commodity search when the user wants counts.
     *
     * @param classCode A class code to search for records on.
     * @param commodityCode A commodity code to search for records on.
     * @param request The request with page and page size
     * @return An object containing the product records based on search criteria.
     */
    private PageableResult<ProductMaster> searchByClassAndCommodityWithCounts(int classCode, int commodityCode,
                                                                                   Pageable request) {
        Page<ProductMaster> data = this.productInfoRepositoryWithCounts.
                        findByClassCodeAndCommodityCode(classCode, commodityCode, request);

        return new PageableResult<>(request.getPageNumber(),
                data.getTotalPages(),
                data.getTotalElements(),
                data.getContent());
    }

    /**
     * Handles the class and commodity search when the user does not want counts.
     *
     * @param classCode A class code to search for records on.
     * @param commodityCode A commodity code to search for records on.
     * @param request The request with page and page size
     * @return An object containing the product records based on search criteria.
     */
    private PageableResult<ProductMaster> searchByClassAndCommodityWithoutCounts(int classCode, int commodityCode,
                                                                                      Pageable request) {
        List<ProductMaster> data = this.productInfoRepository.
                        findByClassCodeAndCommodityCode(classCode, commodityCode, request);

        return new PageableResult<>(request.getPageNumber(), data);
    }

    /**
     * Sets the ProductInfoRepositoryWithCounts for the object to use.
     *
     * @param repository The ProductInfoRepositoryWithCounts for the object to use.
     */
    public void setProductInfoRepositoryWithCounts(ProductInfoRepositoryWithCounts repository) {
        this.productInfoRepositoryWithCounts = repository;
    }

    /**
     * Utility function to take a column and direction as defined by the enums in this class and convert it into
     * a Spring Sort.
     * @return A Spring Sort object.
     */
    private Sort toSort() {
        return ProductMaster.getDefaultSort();
    }

    /**
     * Searches for product records based on a list of product descriptions.
     *
     * @param descriptions The list of descriptions to search for records on.
     * @param includeCount Set to true to include page and record counts.
     * @param page         The page you are looking for.
     * @param pageSize     The maximum number of records to return.
     * @return An object containing the product records based on search criteria.
     */
    public PageableResult<ProductMaster> findByProductDescription(String descriptions,
                                                         boolean includeCount, int page,
                                                         int pageSize) {

        Pageable request = new PageRequest(page, pageSize, this.toSort());

        return includeCount ? this.searchByProductDescriptionWithCounts(descriptions.toUpperCase().trim(), request) :
                this.searchByProductDescriptionWithoutCounts(descriptions.toUpperCase().trim(), request);
    }

    /**
     * Find by product descriptions with counts pageable result.
     *
     * @param descriptions          The list of descriptions to search for records on.
     * @param descriptionRequest    Request The request with page and page size
     * @return the pageable result
     */
    private PageableResult<ProductMaster> searchByProductDescriptionWithCounts(String descriptions, Pageable descriptionRequest) {
        Page<ProductMaster> dataOne = this.productInfoRepositoryWithCounts.findByDescription(String.format(DESCRIPTION_REGEX,descriptions), descriptionRequest);

        return new PageableResult<>(descriptionRequest.getPageNumber(),
                dataOne.getTotalPages(),
                dataOne.getTotalElements(),
                dataOne.getContent());
    }

    /**
     * Find by item code without counts pageable result.
     *
     * @param descriptions    The list of descriptions to search for records on.
     * @param descriptionRequest The request with page and page size
     * @return the pageable result
     */
    private PageableResult<ProductMaster> searchByProductDescriptionWithoutCounts(String descriptions, Pageable descriptionRequest) {
        List<ProductMaster> data = this.productInfoRepository.findByDescription(String.format(DESCRIPTION_REGEX, descriptions), descriptionRequest);
        return new PageableResult<>(descriptionRequest.getPageNumber(), data);
    }
}
