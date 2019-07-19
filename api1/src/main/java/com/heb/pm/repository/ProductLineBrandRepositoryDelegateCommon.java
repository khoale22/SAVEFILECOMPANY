package com.heb.pm.repository;

public interface ProductLineBrandRepositoryDelegateCommon {

    /**
     * The base query to use when searching without count.
     */
    String BASE_NON_COUNT_SEARCH = "select pbl ";

    /**
     * The base query to use when searching without count.
     */
    String BASE_NON_COUNT_DISTINCT_SEARCH = "select distinct pbl ";

    /**
     * The base query to use when searching with count.
     */
    String BASE_COUNT_SEARCH = "select count(pbl.key.brandId) ";

    /**
     * The base query to use when searching by class.
     */
    String BASE_BRAND_SEARCH = "from ProductLineBrand pbl join pbl.productBrand pb " +
            "where pb.productBrandId = pbl.key.brandId ";

    /**
     * The base query to use when searching by class.
     */
    String BASE_PRODUCT_LINE_SEARCH = "from ProductLineBrand pbl join pbl.productLine pl " +
            "where pl.id = pbl.key.lineCode ";

    /**
     * The base query to use when searching by class.
     */
    String BASE_PRODUCT_LINE_AND_BRAND_SEARCH = "from ProductLineBrand pbl join pbl.productLine pl join pbl.productBrand pb " +
            "where pb.productBrandId = pbl.key.brandId " +
            "and pl.id = pbl.key.lineCode ";


    /**
     * Add this as a predicate when looking for active items.
     */
    //DB2Oracle Changes vn00907 ,added trim
    String SEARCHED_BRAND_PREDICATE = "and (" +
            "to_char(pbl.key.brandId) like :searchedBrand " +
            "or upper(pbl.productBrand.productBrandDescription) like :searchedBrand) ";

    /**
     * Add this as a predicate when looking for active items.
     */
    //DB2Oracle Changes vn00907 ,added trim
    String SEARCHED_LINE_PREDICATE = "and (" +
            "pbl.key.lineCode like :searchedLine or " +
            "upper(pbl.productLine.description) like :searchedLine) ";


}
