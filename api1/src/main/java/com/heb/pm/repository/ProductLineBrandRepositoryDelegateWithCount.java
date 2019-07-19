package com.heb.pm.repository;

import com.heb.pm.entity.ProductLineBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author d116773
 * @since
 */
public interface ProductLineBrandRepositoryDelegateWithCount extends ProductLineBrandRepositoryDelegateCommon {

	Page<ProductLineBrand> findAllBySearchedBrand(@Param("searchedBrand") String searchedBrand,
												  Pageable pageRequest);

	Page<ProductLineBrand> findAllBySearchedLine(@Param("searchedLine")String searchedLine,
												 Pageable pageRequest);

	Page<ProductLineBrand> findAllBySearchedLineAndSearchedBrand(@Param("searchedBrand")String searchedBrand,
																 @Param("searchedLine")String searchedLine,
																 Pageable pageRequest);


}
