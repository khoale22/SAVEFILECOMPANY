/*
 * NutritionUpdatesTaskPredicateBuilder.java
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class helps to build predicates for Nutrition Updates based on basic conditions searchCriteria.
 *
 * @author vn70529
 * @since 2.26.0
 */
@Service
public class NutritionUpdatesTaskPredicateBuilder {

    private static final String PRODUCT_ID = "productId";
    private static final String PROD_ID = "prodId";
    private static final char TRIM_VALUE = '0';


    /**
     * Used to build predicates(WHERE clause) for fetching Nutrition updates alerts. Makes a list of predicates based on the
     * filter condition input. Add finally compose them into a single Predicate joined by AND condition.
     *
     * @param root            The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder    JPA query builder used to construct the sub-query.
     * @param criteriaBuilder Used to construct the various parts of the SQL statement.
     * @return
     */
    protected Predicate buildPredicate(Root<AlertStaging> root, CriteriaQuery<AlertStaging> queryBuilder, CriteriaBuilder criteriaBuilder, ProductSearchCriteria searchCriteria) {
        Specification<AlertStaging> spec = buildSpecification(searchCriteria);
        return spec.toPredicate(root, queryBuilder, criteriaBuilder);
    }

    /**
     * Gives structure to the predicates to be built for searching Nutrition updates.
     *
     * @param searchCriteria the search Criteria.
     * @return the Specification.
     */
    private Specification<AlertStaging> buildSpecification(ProductSearchCriteria searchCriteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Search by alertTypeCD
            predicates.add(cb.equal(root.get(AlertStaging_.alertTypeCD), AlertStaging.AlertTypeCD.GENESIS_AP.getName()));
            // Search by alertStatusCD.
            predicates.add(cb.equal(root.get(AlertStaging_.alertStatusCD), AlertStaging.AlertStatusCD.ACTIVE.getName()));
            if (searchCriteria.hasProductSearch()) {
                // Search by the list of product ids
                predicates.add(buildProductIdPredicate(root, cb, searchCriteria));
            } else if (searchCriteria.hasUpcSearch()) {
                // Search by the list of upcs.
                predicates.add(cb.exists(buildUpcSubQuery(root, query, cb, searchCriteria)));
            } else if (searchCriteria.hasItemCodeSearch()) {
                // Search by the list of item codes.
                predicates.add(cb.exists(buildItemCodeSubQuery(root, query, cb, searchCriteria)));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Builds predicate to fetch alert referenced product with matching item code.
     *
     * @param root            The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder    JPA query builder used to construct the sub-query.
     * @param criteriaBuilder criteria builder, used to construct the various parts of the SQL statement.
     * @param searchCriteria  the search Criteria.
     * @return the Subquery by item code
     */
    private Subquery<ProdItem> buildItemCodeSubQuery(Root<AlertStaging> root, CriteriaQuery<?> queryBuilder, CriteriaBuilder criteriaBuilder, ProductSearchCriteria searchCriteria) {
        // Create prod item sub query.
        Subquery<ProdItem> prodItemSubQuery = queryBuilder.subquery(ProdItem.class);
        Root<ProdItem> prodItemRoot = prodItemSubQuery.from(ProdItem.class);
        prodItemSubQuery.select(prodItemRoot.get(PRODUCT_ID));
        // Create condition.
        Predicate[] predicates = new Predicate[3];
        predicates[0] = prodItemRoot.get(ProdItem_.itemCode).as(Long.class).in(searchCriteria.getItemCodes());
        predicates[1] = criteriaBuilder.equal(prodItemRoot.get(ProdItem_.key).get(ProdItemKey_.itemType), ProdItemKey.WAREHOUSE);
        predicates[2] = criteriaBuilder.equal(prodItemRoot.get(ProdItem_.key).get(ProdItemKey_.productId),
                criteriaBuilder.trim(CriteriaBuilder.Trimspec.LEADING, TRIM_VALUE, root.get(AlertStaging_.alertKey)));
        prodItemSubQuery.where(criteriaBuilder.and(predicates));
        return prodItemSubQuery;
    }

    /**
     * Builds predicate to fetch alert referenced product with matching upc.
     *
     * @param root            The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder    JPA query builder used to construct the sub-query.
     * @param criteriaBuilder criteria builder, used to construct the various parts of the SQL statement.
     * @param searchCriteria  the search Criteria.
     * @return the Subquery by upc
     */
    private Subquery<SellingUnit> buildUpcSubQuery(Root<AlertStaging> root, CriteriaQuery<?> queryBuilder, CriteriaBuilder criteriaBuilder, ProductSearchCriteria searchCriteria) {
       // Create selling unit sub query
        Subquery<SellingUnit> sellingUnitSubQuery = queryBuilder.subquery(SellingUnit.class);
        Root<SellingUnit> sellingUnitRoot = sellingUnitSubQuery.from(SellingUnit.class);
        sellingUnitSubQuery.select(sellingUnitRoot.get(PROD_ID));
        // Create condition.
        Predicate[] predicates = new Predicate[2];
        predicates[0] = sellingUnitRoot.get(SellingUnit_.upc).in(searchCriteria.getUpcs());
        predicates[1] = criteriaBuilder.equal(sellingUnitRoot.get(SellingUnit_.prodId),
                criteriaBuilder.trim(CriteriaBuilder.Trimspec.LEADING, TRIM_VALUE, root.get(AlertStaging_.alertKey)));
        sellingUnitSubQuery.where(criteriaBuilder.and(predicates));
        return sellingUnitSubQuery;
    }

    /**
     * Builds predicate to fetch alert referenced product with matching product id.
     *
     * @param root            The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param criteriaBuilder criteria builder, used to construct the various parts of the SQL statement.
     * @return filter by product id predicate.
     */
    private Predicate buildProductIdPredicate(Root<AlertStaging> root, CriteriaBuilder criteriaBuilder, ProductSearchCriteria searchCriteria) {
        List<String> productIds = new ArrayList<>();
        // Convert the list of product ids as string
        for (Long productId : searchCriteria.getProductIds()) {
            productIds.add(String.valueOf(productId));
        }
        List<Predicate> predicates = null;
        if (searchCriteria.getProductIds() != null && !searchCriteria.getProductIds().isEmpty()) {
            predicates = new ArrayList<>();
            for (Long attribute : searchCriteria.getProductIds()) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.trim(CriteriaBuilder.Trimspec.LEADING, TRIM_VALUE, root.get(AlertStaging_.alertKey)), attribute));
            }
        }
        return predicates != null ? criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])) : null;
    }
}
