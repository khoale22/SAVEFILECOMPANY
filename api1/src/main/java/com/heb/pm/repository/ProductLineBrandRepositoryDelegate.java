package com.heb.pm.repository;

import com.heb.pm.entity.ProductLineBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductLineBrandRepositoryDelegate extends ProductLineBrandRepositoryDelegateCommon {

    List<ProductLineBrand> findAllBySearchedBrand(@Param("searchedBrand") String searchedBrand,
                                                  Pageable pageRequest);

    List<ProductLineBrand> findAllBySearchedLine(@Param("searchedLine") String searchedLine,
                                                 Pageable pageRequest);

    List<ProductLineBrand> findAllBySearchedLineAndSearchedBrand(@Param("searchedBrand") String searchedBrand,
                                                                 @Param("searchedLine") String searchedLine,
                                                                 Pageable pageRequest);
}
