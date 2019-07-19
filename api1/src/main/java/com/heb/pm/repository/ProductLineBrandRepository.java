package com.heb.pm.repository;

import com.heb.pm.entity.ProductLineBrand;
import com.heb.pm.entity.ProductLineBrandKey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductLineBrandRepository extends JpaRepository<ProductLineBrand, ProductLineBrandKey>, ProductLineBrandCommon, ProductLineBrandRepositoryDelegate {



    @Query(value=BASE_NON_COUNT_SEARCH+BASE_BRAND_SEARCH+SEARCHED_BRAND_PREDICATE)
    List<ProductLineBrand> findAllBySearchedBrand(@Param("searchedBrand")String searchedBrand, Pageable pageRequest);

    @Query(value=BASE_NON_COUNT_SEARCH+BASE_PRODUCT_LINE_SEARCH+SEARCHED_LINE_PREDICATE)
    List<ProductLineBrand> findAllBySearchedLine(@Param("searchedLine")String searchedLine, Pageable pageRequest);

    @Query(value=BASE_NON_COUNT_SEARCH+BASE_PRODUCT_LINE_AND_BRAND_SEARCH+SEARCHED_LINE_PREDICATE+SEARCHED_BRAND_PREDICATE)
    List<ProductLineBrand> findAllBySearchedLineAndSearchedBrand(@Param("searchedBrand")String searchedBrand,
                                                                 @Param("searchedLine")String searchedLine,
                                                                 Pageable pageRequest);

    @Query(value = "select productLineBrand from ProductLineBrand productLineBrand")
    List<ProductLineBrand> findAllByPage(Pageable pageable);

    /**
     * Find first ProductLineBrand with BrandId.
     * @param productBrandId    the productBrandId to search
     * @return ProductLineBrand.
     */
    ProductLineBrand findFirstByKeyBrandId(Long productBrandId);

}
