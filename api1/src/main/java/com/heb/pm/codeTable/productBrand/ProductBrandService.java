/*
 *  ProductBrandService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.productBrand;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.entity.*;
import com.heb.pm.entity.ProductBrand;
import com.heb.pm.repository.ProductBrandRepository;
import com.heb.pm.repository.ProductBrandRepositoryWithCount;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * Holds all business logic related to code table product brand information.
 *
 * @author vn00602
 * @since 2.12.0
 */
@Service
public class ProductBrandService {
	@CoreEntityManager
	@Autowired
	private EntityManager entityManager;

    @Autowired
    private ProductBrandRepository productBrandRepository;

    @Autowired
    private ProductBrandRepositoryWithCount productBrandRepositoryWithCount;

	private static final String JPA_CRITERIA_WILDCARD = "%";

    private LazyObjectResolver<Iterable<ProductBrand>> productBrandLazyObjectResolver = new ProductBrandResolver();

    /**
     * Resolver for Product brand object.
     */
    private class ProductBrandResolver implements LazyObjectResolver<Iterable<ProductBrand>> {

        /**
         * Resolves all of the associate product brand tiers information.
         *
         * @param productBrands the product brand page to fetch the product brand tiers for.
         */
        @Override
        public void fetch(Iterable<ProductBrand> productBrands) {
            productBrands.forEach((productBrand) -> productBrand.getProductBrandTier().getProductBrandTierCode());
		}
    }

    /**
     * Find all product brands with pagination.
     *
     * @param page     the page number.
     * @param pageSize the page size.
     * @param ownBrand the show own brand only checkbox.
     * @return the page of product brands.
     */
    public PageableResult<ProductBrand> findAllProductBrands(int page, int pageSize, boolean ownBrand, boolean includeCount) {
        Pageable pageRequest = new PageRequest(page, pageSize);
        PageableResult<ProductBrand> results;
        if (includeCount) {
            Page<ProductBrand> productBrands = this.findAllProductBrandsWithCount(ownBrand, pageRequest);
            this.productBrandLazyObjectResolver.fetch(productBrands);
            results = new PageableResult<>(pageRequest.getPageNumber(), productBrands.getTotalPages(),
                    productBrands.getTotalElements(), productBrands.getContent());
        } else {
            List<ProductBrand> productBrands = this.findAllProductBrandsWithoutCount(ownBrand, pageRequest);
            this.productBrandLazyObjectResolver.fetch(productBrands);
            results = new PageableResult<>(pageRequest.getPageNumber(), productBrands);
        }
        return results;
    }

    /**
     * Filter product brands with pagination.
     *
     * @param page         the page number.
     * @param pageSize     the page size.
     * @param productBrand the product brand description to search.
     * @param brandTier    the product brand tier name to search.
     * @param ownBrand     the show own brand only checkbox.
     * @return the page of product brands.
     */
    public PageableResult<ProductBrand> filterProductBrands(int page, int pageSize, String productBrand,
                                                            String brandTier, boolean ownBrand, boolean includeCount) {
        Pageable pageRequest = new PageRequest(page, pageSize);
        PageableResult<ProductBrand> results;
        if (includeCount) {
            Page<ProductBrand> productBrands = this.filterProductBrandsWithCount(productBrand, brandTier, ownBrand, pageRequest);
            this.productBrandLazyObjectResolver.fetch(productBrands);
            results = new PageableResult<>(pageRequest.getPageNumber(), productBrands.getTotalPages(),
                    productBrands.getTotalElements(), productBrands.getContent());
        } else {
            List<ProductBrand> productBrands = this.filterProductBrandsWithoutCount(productBrand, brandTier, ownBrand, pageRequest);
            this.productBrandLazyObjectResolver.fetch(productBrands);
            results = new PageableResult<>(pageRequest.getPageNumber(), productBrands);
        }
        return results;
    }

	/**
	 * Find product brand by id or description.
	 * @param page page number
	 * @param pageSize the page size
	 * @param searchText the text to find by
	 * @return
	 */
	public PageableResult<ProductBrand> findProductBrandsByText(int page,int pageSize, String searchText){
		PageableResult<ProductBrand> results;
        // Get the objects needed to build the query.
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		// Builds the criteria for the main query.
		CriteriaQuery<ProductBrand> queryBuilder = criteriaBuilder.createQuery(ProductBrand.class);
		// Select from product brand
		Root<ProductBrand> pmRoot = queryBuilder.from(ProductBrand.class);
        // Build query.
		queryBuilder.select(pmRoot);

		Expression<String> literal = criteriaBuilder.literal(JPA_CRITERIA_WILDCARD.concat(searchText.toUpperCase()).concat(JPA_CRITERIA_WILDCARD));
		queryBuilder.where(criteriaBuilder.or(
		        criteriaBuilder.like(criteriaBuilder.upper(pmRoot.get(ProductBrand_.productBrandDescription)),literal),
                criteriaBuilder.like(criteriaBuilder.upper(pmRoot.get(ProductBrand_.productBrandId).as(String.class)),literal)
                ));
		queryBuilder.orderBy(criteriaBuilder.desc(criteriaBuilder.selectCase()
				.when(criteriaBuilder.like(criteriaBuilder.upper(criteriaBuilder.trim(pmRoot.get(ProductBrand_.productBrandDescription))),
                        searchText.toUpperCase().concat(JPA_CRITERIA_WILDCARD)),1)
                .when(criteriaBuilder.equal(criteriaBuilder.trim(pmRoot.get(ProductBrand_.productBrandId).as(String.class)), searchText.toUpperCase()),1)
				.otherwise(0)),criteriaBuilder.asc(pmRoot.get(ProductBrand_.productBrandDescription)));
		TypedQuery<ProductBrand> pmTQuery = this.entityManager.createQuery(queryBuilder);
		pmTQuery.setFirstResult(page).setMaxResults(pageSize);
		List<ProductBrand> productBrands = pmTQuery.getResultList();
		results = new PageableResult<>(page, productBrands);
		this.productBrandLazyObjectResolver.fetch(results.getData());
		return results;
	}
	/**
     * Find all product brands with pagination.
     *
     * @param pageRequest the page request for pagination.
     * @param ownBrand    the checkbox show own brand only.
     * @return the page of product brands.
     */
    private Page<ProductBrand> findAllProductBrandsWithCount(boolean ownBrand, Pageable pageRequest) {
        Page<ProductBrand> productBrands;
        if (!ownBrand) {
            // get all product brands with pagination.
            productBrands = this.productBrandRepositoryWithCount.findAll(pageRequest);
        } else {
            // get all product brands with tier code <> Unassigned [0].
            productBrands = this.productBrandRepositoryWithCount.findByProductBrandTierProductBrandTierCodeNot(
                    ProductBrandController.TIER_CODE_UNASSIGNED, pageRequest);
        }
        return productBrands;
    }

    /**
     * Filter product brands with pagination.
     *
     * @param pageRequest  the page request for pagination.
     * @param productBrand the product brand description to search.
     * @param brandTier    the product brand tier name to search.
     * @param ownBrand     the checkbox show own brand only.
     * @return the page of product brands.
     */
    private Page<ProductBrand> filterProductBrandsWithCount(String productBrand, String brandTier,
                                                            boolean ownBrand, Pageable pageRequest) {
        Page<ProductBrand> productBrands;
        if (!StringUtils.isEmpty(productBrand) && StringUtils.isEmpty(brandTier)) {
            if (!ownBrand) {
                // get all product brands filter by product brand description.
                productBrands = this.productBrandRepositoryWithCount.findByProductBrand(
                        productBrand, pageRequest);
            } else {
                // get all product brands filter by product brand description and tier code <> Unassigned [0].
                productBrands = this.productBrandRepositoryWithCount.findByProductBrandAndOwnBrand(
                        productBrand, ProductBrandController.TIER_CODE_UNASSIGNED, pageRequest);
            }
        } else if (StringUtils.isEmpty(productBrand) && !StringUtils.isEmpty(brandTier)) {
            if (!ownBrand) {
                // get all product brands filter by product brand tier name.
                productBrands = this.productBrandRepositoryWithCount.findByProductBrandTierProductBrandNameIgnoreCaseContaining(
                        brandTier, pageRequest);
            } else {
                // get all product brands filter by product brand tier name and tier code <> Unassigned [0].
                productBrands = this.productBrandRepositoryWithCount.findByProductBrandTierProductBrandNameIgnoreCaseContainingAndProductBrandTierProductBrandTierCodeNot(
                        brandTier, ProductBrandController.TIER_CODE_UNASSIGNED, pageRequest);
            }
        } else {
            if (!ownBrand) {
                // get all product brands filter by product brand description and product brand tier name.
                productBrands = this.productBrandRepositoryWithCount.findByProductBrandAndBrandTier(
                        productBrand, brandTier, pageRequest);
            } else {
                // get all product brands filter by product brand description and product brand tier name and tier code <> Unassigned [0].
                productBrands = this.productBrandRepositoryWithCount.findByProductBrandAndBrandTierAndOwnBrand(
                        productBrand, brandTier, ProductBrandController.TIER_CODE_UNASSIGNED, pageRequest);
            }
        }
        return productBrands;
    }

    /**
     * Find all product brands without count.
     *
     * @param pageRequest the page request for pagination.
     * @param ownBrand    the checkbox show own brand only.
     * @return the page of product brands.
     */
    private List<ProductBrand> findAllProductBrandsWithoutCount(boolean ownBrand, Pageable pageRequest) {
        List<ProductBrand> productBrands;
        if (!ownBrand) {
            // get all product brands with pagination.
            productBrands = this.productBrandRepository.findAll(pageRequest).getContent();
        } else {
            // get all product brands with tier code <> Unassigned [0].
            productBrands = this.productBrandRepository.findByProductBrandTierProductBrandTierCodeNot(
                    ProductBrandController.TIER_CODE_UNASSIGNED, pageRequest);
        }
        return productBrands;
    }

    /**
     * Filter product brands without count.
     *
     * @param pageRequest  the page request for pagination.
     * @param productBrand the product brand description to search.
     * @param brandTier    the product brand tier name to search.
     * @param ownBrand     the checkbox show own brand only.
     * @return the page of product brands.
     */
    private List<ProductBrand> filterProductBrandsWithoutCount(String productBrand, String brandTier,
                                                               boolean ownBrand, Pageable pageRequest) {
        List<ProductBrand> productBrands;
        if (!StringUtils.isEmpty(productBrand) && StringUtils.isEmpty(brandTier)) {
            if (!ownBrand) {
                // get all product brands filter by product brand description.
                productBrands = this.productBrandRepository.findByProductBrand(
                        productBrand, pageRequest);
            } else {
                // get all product brands filter by product brand description and tier code <> Unassigned [0].
                productBrands = this.productBrandRepository.findByProductBrandAndOwnBrand(
                        productBrand, ProductBrandController.TIER_CODE_UNASSIGNED, pageRequest);
            }
        } else if (StringUtils.isEmpty(productBrand) && !StringUtils.isEmpty(brandTier)) {
            if (!ownBrand) {
                // get all product brands filter by product brand tier name.
                productBrands = this.productBrandRepository.findByProductBrandTierProductBrandNameIgnoreCaseContaining(
                        brandTier, pageRequest);
            } else {
                // get all product brands filter by product brand tier name and tier code <> Unassigned [0].
                productBrands = this.productBrandRepository.findByProductBrandTierProductBrandNameIgnoreCaseContainingAndProductBrandTierProductBrandTierCodeNot(
                        brandTier, ProductBrandController.TIER_CODE_UNASSIGNED, pageRequest);
            }
        } else {
            if (!ownBrand) {
                // get all product brands filter by product brand description and product brand tier name.
                productBrands = this.productBrandRepository.findByProductBrandAndBrandTier(
                        productBrand, brandTier, pageRequest);
            } else {
                // get all product brands filter by product brand description and product brand tier name and tier code <> Unassigned [0].
                productBrands = this.productBrandRepository.findByProductBrandAndBrandTierAndOwnBrand(
                        productBrand, brandTier, ProductBrandController.TIER_CODE_UNASSIGNED, pageRequest);
            }
        }
        return productBrands;
    }
}
