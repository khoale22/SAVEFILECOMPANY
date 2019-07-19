package com.heb.pm.repository;

import com.heb.pm.entity.ProductLineBrand;
import com.heb.pm.entity.ProductLineBrandKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface ProductLineBrandRepositoryWithCount extends JpaRepository<ProductLineBrand, ProductLineBrandKey>, ProductLineBrandCommon, ProductLineBrandRepositoryDelegateWithCount {

    @Query(value=BASE_NON_COUNT_SEARCH+BASE_BRAND_SEARCH+SEARCHED_BRAND_PREDICATE, countQuery = BASE_COUNT_SEARCH+BASE_BRAND_SEARCH+SEARCHED_BRAND_PREDICATE)
    Page<ProductLineBrand> findAllBySearchedBrand(@Param("searchedBrand")String searchedBrand, Pageable pageRequest);

    @Query(value=BASE_NON_COUNT_SEARCH+BASE_PRODUCT_LINE_SEARCH+SEARCHED_LINE_PREDICATE, countQuery = BASE_COUNT_SEARCH+BASE_PRODUCT_LINE_SEARCH+SEARCHED_LINE_PREDICATE)
    Page<ProductLineBrand> findAllBySearchedLine(@Param("searchedLine")String searchedLine, Pageable pageRequest);

    @Query(value=BASE_NON_COUNT_SEARCH+BASE_PRODUCT_LINE_AND_BRAND_SEARCH+SEARCHED_LINE_PREDICATE+SEARCHED_BRAND_PREDICATE, countQuery = BASE_COUNT_SEARCH+BASE_PRODUCT_LINE_AND_BRAND_SEARCH+SEARCHED_LINE_PREDICATE+SEARCHED_BRAND_PREDICATE)
    Page<ProductLineBrand> findAllBySearchedLineAndSearchedBrand(@Param("searchedBrand")String searchedBrand,
                                                                 @Param("searchedLine")String searchedLine,
                                                                 Pageable pageRequest);

    @Query(value = "select productLineBrand from ProductLineBrand productLineBrand")
    Page<ProductLineBrand> findAllByPage(Pageable pageable);

}
