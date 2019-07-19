/*
 * ImageChangeOnDatePredicateBuilder
 *
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;

/**
 * Builds predicates to search product based on change on date of image.
 *
 * @author vn70529
 * @version 2.39.0
 */
@Service
public class ImageChangeOnDatePredicateBuilder implements PredicateBuilder {
    private static final String UPC_PROPERTY_NAME = "upc";

    /**
     * Builds a predicate to search for products based on change on date (aud_rec_cre8_ts) of image.
     *
     * @param criteriaBuilder       Used to construct the various parts of the SQL statement.
     * @param pmRoot                The root from clause of the main query (this will be used to grab the criteria to join the
     *                              sub-query to).
     * @param queryBuilder          JPA query builder used to construct the sub-query.
     * @param productSearchCriteria The user's search criteria.
     * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
     * @return A sub-query that will return products based on item add date.
     */
    @Override
    public ExistsClause<SellingUnit> buildPredicate(CriteriaBuilder criteriaBuilder, Root<? extends ProductMaster> pmRoot,
                                                    CriteriaQuery<? extends ProductMaster> queryBuilder,
                                                    ProductSearchCriteria productSearchCriteria,
                                                    ParameterExpression<String> sessionIdBindVariable) {

        // See if there is an entry in the custom searches, then the app go to next step to check the image change on date is checked or not.
        if (productSearchCriteria.getCustomSearchEntries() == null) {
            return null;
        }

        CustomSearchEntry imageChangeOnDateEntry = null;
        for (CustomSearchEntry customSearchEntry : productSearchCriteria.getCustomSearchEntries()) {
            // Find custom search entry of image change on date.
            if (customSearchEntry.getType() == CustomSearchEntry.IMAGE_CHANGE_ON_DATE) {
                imageChangeOnDateEntry = customSearchEntry;
                break;
            }
        }
        // Check the image change on entry, if it is not exists, then stop searching it.
        if (imageChangeOnDateEntry == null || imageChangeOnDateEntry.getDateComparator() == null) {
            return null;
        }

        if (imageChangeOnDateEntry.getOperator() == CustomSearchEntry.BETWEEN) {
            // If search operator is between, then it needs to check the end date. If it is not exists, the stop search.
            if (imageChangeOnDateEntry.getEndDateComparator() == null) {
                return null;
            }
        }

        // We'll be adding a sub-query to prod_scn_codes
        Subquery<SellingUnit> sellingUnitSubQuery = queryBuilder.subquery(SellingUnit.class);
        Root<SellingUnit> sellingUnitRoot = sellingUnitSubQuery.from(SellingUnit.class);
        sellingUnitSubQuery.select(sellingUnitRoot.get(UPC_PROPERTY_NAME));

        // Add a join between Selling Unit and the ProductScanImageURIAudit.
        ListJoin<SellingUnit, ProductScanImageURIAudit> joinProductScanImageURIAudit = sellingUnitRoot.join(SellingUnit_.productScanImageURIAudits);

        // Add a join from product_master to prod_scn_codes
        Predicate[] criteria = new Predicate[2];
        criteria[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId), sellingUnitRoot.get(SellingUnit_.prodId));
        // Add the constraint for change on date
        switch (imageChangeOnDateEntry.getOperator()) {
            case CustomSearchEntry.EQUAL:
                // Since we can't programmatically remove the time part of the date, use a between with the
                // requested date and that date + 1
                criteria[1] = criteriaBuilder.between(
                        joinProductScanImageURIAudit.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.changedOn),
                        imageChangeOnDateEntry.getDateComparator().atStartOfDay(),
                        imageChangeOnDateEntry.getDateComparator().plusDays(1).atStartOfDay());
                break;
            case CustomSearchEntry.GREATER_THAN:
                // greater than or equal to the beginning of the day that the user was selected.
                criteria[1] = criteriaBuilder.greaterThanOrEqualTo(
                        joinProductScanImageURIAudit.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.changedOn),
                        imageChangeOnDateEntry.getDateComparator().atStartOfDay());
                break;
            case CustomSearchEntry.LESS_THAN:
                // Less than or equal to the end of selected day that user was selected.
                criteria[1] = criteriaBuilder.lessThan(
                        joinProductScanImageURIAudit.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.changedOn),
                        imageChangeOnDateEntry.getDateComparator().plusDays(1).atStartOfDay());
                break;
            case CustomSearchEntry.ONE_WEEK:
                // Greater than one week ago.
                LocalDateTime today = LocalDateTime.now();
                LocalDateTime oneWeekAgo = today.minusDays(7);
                criteria[1] = criteriaBuilder.greaterThanOrEqualTo(
                        joinProductScanImageURIAudit.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.changedOn),
                        oneWeekAgo);
                break;
            case CustomSearchEntry.BETWEEN:
                // Have to look at the beginning of the start and the end of the day  that the user was selected.
                criteria[1] = criteriaBuilder.between(
                        joinProductScanImageURIAudit.get(ProductScanImageURIAudit_.key).get(ProductScanImageURIAuditKey_.changedOn),
                        imageChangeOnDateEntry.getDateComparator().atStartOfDay(),
                        imageChangeOnDateEntry.getEndDateComparator().plusDays(1).atStartOfDay());
        }

        sellingUnitSubQuery.where(criteria);

        return new ExistsClause<>(sellingUnitSubQuery);
    }
}
