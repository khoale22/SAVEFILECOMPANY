/*
 * EcommerceTaskPredicateBuilder.java
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class helps to build predicates for fetch products under an ecommerce task based on basic conditions(like
 * tracking id, product status, sales channel) and filter conditions (like show on site yes/no).
 *
 * @author vn40486
 * @since 2.17.0
 */
@Service
public class EcommerceTaskPredicateBuilder {

    private static final String SHOW_ON_SITE_YES = "Y";
    private static final String COMMA = ",";
    private static final String KEY_ATTRIBUTE = "key";
    private static final String PRODUCT_ID_ATTRIBUTE = "productId";



    /**
     * Used to build predicates(WHERE clause) for fetching products under the task. Makes a list of predicates based on the
     * filter condition input. Add finally compose them into a single Predicate joined by AND condition.
     *
     * @param root  The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder JPA query builder used to construct the sub-query.
     * @param criteriaBuilder Used to construct the various parts of the SQL statement.
     * @param trackingId tracking of the alert/task.
     * @param assignee alert assigned to user.
     * @param showOnSite show on site Y/N.
     * @return
     */
    protected Predicate buildPredicate(Root<CandidateWorkRequest> root, CriteriaQuery<CandidateWorkRequest> queryBuilder,
                                       CriteriaBuilder criteriaBuilder, Long trackingId, String assignee, String showOnSite, String salesChannels) {
        Specification<CandidateWorkRequest> spec = buildSpecification(trackingId, assignee, showOnSite, salesChannels);
        return spec.toPredicate(root, queryBuilder, criteriaBuilder);
    }

    /**
     * Gives structure to the predicates to be built for searchign product updates.
     * @param trackingId tracking of the alert/task.
     * @param assignedUserID alert assigned to user.
     * @param showOnSite show on site Y/N.
     * @return
     */
    private Specification<CandidateWorkRequest> buildSpecification(Long trackingId, String assignedUserID, String showOnSite, String salesChannels) {
        return new Specification<CandidateWorkRequest>() {
            @Override
            public Predicate toPredicate(Root<CandidateWorkRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get(CandidateWorkRequest_.trackingId), trackingId));
                predicates.add(cb.equal(root.get(CandidateWorkRequest_.status), CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD));
                if(assignedUserID != null && !assignedUserID.trim().isEmpty()) {
                    predicates.add(cb.equal(cb.upper(root.get(CandidateWorkRequest_.lastUpdateUserId)), assignedUserID.toUpperCase()));
                }
                if(StringUtils.isNotBlank(showOnSite)) {
                    Subquery<ProductOnline> showOnSiteSubquery = buildShowOnSitePredicate(showOnSite, root, query, cb);
                    if(showOnSite.equalsIgnoreCase(SHOW_ON_SITE_YES)) {
                        predicates.add(cb.exists(showOnSiteSubquery));
                    } else {
                        predicates.add(cb.not(cb.exists(showOnSiteSubquery)));
                    }
                }
                if(salesChannels != null && !salesChannels.isEmpty()) {
                    predicates.add(cb.exists(buildFulfillmentChannelPredicate(salesChannels, root, query, cb)));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    /**
     * Builds predicate to fetch alert referenced product with matching show on site. show on site status is decided by
     * a set of parameters like record exist with valid effective and expiration dates.
     * @param showOnSite show on site Y/N
     * @param root The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder JPA query builder used to construct the sub-query.
     * @param cb criteria builder, used to construct the various parts of the SQL statement.
     * @return filter by show on site predicate.
     */
    private Subquery<ProductOnline> buildShowOnSitePredicate(String showOnSite, Root<CandidateWorkRequest> root,
    														 CriteriaQuery<?> queryBuilder, CriteriaBuilder cb) {
    	Subquery<ProductOnline> productOnlineExist = queryBuilder.subquery(ProductOnline.class);
    	Root<ProductOnline> poRoot = productOnlineExist.from(ProductOnline.class);
    	productOnlineExist.select(poRoot.get(KEY_ATTRIBUTE).get(PRODUCT_ID_ATTRIBUTE));

    	Predicate[] predicates = new Predicate[6];
    	predicates[0] = cb.equal(poRoot.get(ProductOnline_.key).get(ProductOnlineKey_.productId),
    			root.get(CandidateWorkRequest_.productId));
    	predicates[1] = cb.equal(poRoot.get(ProductOnline_.key).get(ProductOnlineKey_.saleChannelCode),
    			SalesChannel.SALES_CHANNEL_HEB_COM);
    	predicates[2] = cb.equal(poRoot.get(ProductOnline_.showOnSite), true);
    	predicates[3] = cb.lessThanOrEqualTo(poRoot.get(ProductOnline_.key).get(ProductOnlineKey_.effectiveDate),LocalDate.now());
    	predicates[4] = cb.greaterThan(poRoot.get(ProductOnline_.expirationDate),LocalDate.now());
    	predicates[5] = cb.equal(poRoot.get(ProductOnline_.key).get(ProductOnlineKey_.effectiveDate), 
    			this.getLatestEffectiveDate(root, queryBuilder, cb));
    	productOnlineExist.where(cb.and(predicates));

    	return productOnlineExist;
    }

    /**
     * Builds predicate to get latest effective date with matching product id, sale channel code and show on site status.
     * @param root The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder JPA query builder used to construct the sub-query.
     * @param cb criteria builder, used to construct the various parts of the SQL statement.
     * @return filter by show on site predicate.
     */
    private Subquery<LocalDate> getLatestEffectiveDate(Root<CandidateWorkRequest> root,
                                                       CriteriaQuery<?> queryBuilder, CriteriaBuilder cb) {
        Subquery<LocalDate> productOnlineExist = queryBuilder.subquery(LocalDate.class);
        Root<ProductOnline> poRoot = productOnlineExist.from(ProductOnline.class);
        Expression maxExpr = cb.greatest(poRoot.get(ProductOnline_.key).get(ProductOnlineKey_.effectiveDate));
        productOnlineExist.select(maxExpr);

        Predicate[] predicates = new Predicate[3];
        predicates[0] = cb.equal(poRoot.get(ProductOnline_.key).get(ProductOnlineKey_.productId),
                root.get(CandidateWorkRequest_.productId));
        predicates[1] = cb.equal(poRoot.get(ProductOnline_.key).get(ProductOnlineKey_.saleChannelCode),
                SalesChannel.SALES_CHANNEL_HEB_COM);
        predicates[2] = cb.equal(poRoot.get(ProductOnline_.showOnSite), true);
        productOnlineExist.where(cb.and(predicates));

        return productOnlineExist;
    }

    /**
     * Builds predicate to fetch alert referenced product with fulfillment channel matching sales channels.
     *
     * @param salesChannels the list of sales channel filter.
     * @param root          The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder  JPA query builder used to construct the sub-query.
     * @param cb            criteria builder, used to construct the various parts of the SQL statement.
     * @return predicate alerts with matching fulfillment channel.
     */
    private Subquery<ProductFullfilmentChanel> buildFulfillmentChannelPredicate(String salesChannels, Root<CandidateWorkRequest> root,
                                                                                CriteriaQuery<?> queryBuilder, CriteriaBuilder cb) {
        Subquery<ProductFullfilmentChanel> productFulfillmentChannelExist = queryBuilder.subquery(ProductFullfilmentChanel.class);
        Root<ProductFullfilmentChanel> poRoot = productFulfillmentChannelExist.from(ProductFullfilmentChanel.class);
        productFulfillmentChannelExist.select(poRoot.get(KEY_ATTRIBUTE).get(PRODUCT_ID_ATTRIBUTE));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(poRoot.get(ProductFullfilmentChanel_.key).get(ProductFullfilmentChanelKey_.productId),
                root.get(CandidateWorkRequest_.productId)));
        List<Predicate> predicates1 = new ArrayList<>();
        if (!org.apache.commons.lang.StringUtils.isEmpty(salesChannels)) {
            List<String> salesChannelCodes = Arrays.asList(salesChannels.split(COMMA));
            for (String salesChannel : salesChannelCodes) {
                predicates1.add(cb.equal(cb.trim(poRoot.get(ProductFullfilmentChanel_.key).get(ProductFullfilmentChanelKey_.salesChanelCode)), salesChannel.trim()));
            }
        }
        predicates.add(cb.or(predicates1.toArray(new Predicate[predicates1.size()])));
        productFulfillmentChannelExist.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return productFulfillmentChannelExist;
    }
}
