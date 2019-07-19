/*
 * ProductUpdatesTaskPredicateBuilder.java
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * This class helps to build predicates for Product Updates (updates, review and new product) based on basic conditions
 * (like task type, task status, sales channel) and filter conditions (like update reason, show on site yes/no).
 *
 * @author vn40486
 * @since 2.17.0
 */
@Service
public class ProductUpdatesTaskPredicateBuilder {

    private static final String LIKE = "%";
    private static final String SHOW_ON_SITE_YES = "Y";
    private static final String COMMA = ",";
    private static final String KEY_ATTRIBUTE = "key";
    private static final String PRODUCT_ID_ATTRIBUTE = "productId";
    private static final String PROD_ID_ATTRIBUTE = "prodId";


    /**
     * Used to build predicates(WHERE clause) for fetching product updates alerts. Makes a list of predicates based on the
     * filter condition input. Add finally compose them into a single Predicate joined by AND condition.
     *
     * @param root            The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder    JPA query builder used to construct the sub-query.
     * @param criteriaBuilder Used to construct the various parts of the SQL statement.
     * @param alertType       alert type like PRUPD/PRRVW/NWPRD
     * @param alertStatus     Active/CLOSED
     * @param assignee        alert assigned to user.
     * @param attributes      list of update reasons, where each reason is identified by it's attribute code.
     * @param showOnSite      show on site Y/N.
     * @param salesChannels   list of sales channels filter.
     * @return the predicate
     */
    protected Predicate buildPredicate(Root<AlertStaging> root, CriteriaQuery<AlertStaging> queryBuilder,
                                     CriteriaBuilder criteriaBuilder, String alertType, String alertStatus,
                                     String assignee, List<Long> attributes, String showOnSite, String salesChannels) {
        Specification<AlertStaging> spec = buildSpecification(alertType, alertStatus, assignee, attributes, showOnSite, salesChannels);
        return spec.toPredicate(root, queryBuilder, criteriaBuilder);
    }

    /**
     * Gives structure to the predicates to be built for searchign product updates.
     *
     * @param alertTypeCD    alert type like PRUPD/PRRVW/NWPRD
     * @param alertStatusCD  Active/CLOSED
     * @param assignedUserID alert assigned to user.
     * @param alertDataTxt   list of update reasons, where each reason is identified by it's attribute code.
     * @param showOnSite     show on site Y/N.
     * @param salesChannels  list of sales channels filter.
     * @return the Specification.
     */
    private Specification<AlertStaging> buildSpecification(String alertTypeCD, String alertStatusCD,
                                                           String assignedUserID, List<Long> alertDataTxt, String showOnSite, String salesChannels) {
        return new Specification<AlertStaging>() {
            @Override
            public Predicate toPredicate(Root<AlertStaging> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get(AlertStaging_.alertTypeCD), alertTypeCD));
                predicates.add(cb.equal(root.get(AlertStaging_.alertStatusCD), alertStatusCD));
                if(assignedUserID != null && !assignedUserID.trim().isEmpty()) {
                    predicates.add(cb.equal(cb.upper(root.get(AlertStaging_.assignedUserID)), assignedUserID.toUpperCase()));
                }
                if(alertDataTxt != null && !alertDataTxt.isEmpty()) {
                    predicates.add(buildAttributesPredicate(alertDataTxt, root, cb));
                }
                if(showOnSite != null && !showOnSite.trim().isEmpty()) {
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
                predicates.add(cb.exists(buildProductMasterPredicate(root, query, cb)));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    /**
     * Build predicate matching the input attributes (updates reasons).
     * @param attributes update reasons referenced by respective attribute ids.
     * @param root The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param cb criteria builder, used to construct the various parts of the SQL statement.
     * @return predicate alerts with matching update reason attributes.
     */
    private Predicate buildAttributesPredicate(List<Long> attributes, Root<AlertStaging> root, CriteriaBuilder cb) {
        List<Predicate> predicates = null;
        if(attributes != null && !attributes.isEmpty()) {
            predicates = new ArrayList<>();
            for (Long attribute : attributes) {
                predicates.add(cb.like(root.<String>get(AlertStaging_.alertDataTxt), LIKE.concat(String.valueOf(attribute)).concat(LIKE)));
            }
        }
        return predicates != null ? cb.or(predicates.toArray(new Predicate[predicates.size()])) : null;
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
    private Subquery<ProductOnline> buildShowOnSitePredicate(String showOnSite, Root<AlertStaging> root,
                                                                CriteriaQuery<?> queryBuilder, CriteriaBuilder cb) {
        Subquery<ProductOnline> productOnlineExist = queryBuilder.subquery(ProductOnline.class);
        Root<ProductOnline> poRoot = productOnlineExist.from(ProductOnline.class);
        productOnlineExist.select(poRoot.get(KEY_ATTRIBUTE).get(PRODUCT_ID_ATTRIBUTE));

        Predicate[] predicates = new Predicate[4];
        predicates[0] = cb.equal(poRoot.get(ProductOnline_.key).get(ProductOnlineKey_.productId),
        		root.get(AlertStaging_.alertKey).as(Long.class));
        predicates[1] = cb.equal(poRoot.get(ProductOnline_.key).get(ProductOnlineKey_.saleChannelCode),
                SalesChannel.SALES_CHANNEL_HEB_COM);
        predicates[2] = cb.equal(poRoot.get(ProductOnline_.showOnSite), true);
        predicates[3] = cb.greaterThan(poRoot.get(ProductOnline_.expirationDate),LocalDate.now());
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
    private Subquery<ProductFullfilmentChanel> buildFulfillmentChannelPredicate(String salesChannels, Root<AlertStaging> root,
                                                                                CriteriaQuery<?> queryBuilder, CriteriaBuilder cb) {
        Subquery<ProductFullfilmentChanel> productFulfillmentChannelExist = queryBuilder.subquery(ProductFullfilmentChanel.class);
        Root<ProductFullfilmentChanel> poRoot = productFulfillmentChannelExist.from(ProductFullfilmentChanel.class);
        productFulfillmentChannelExist.select(poRoot.get(KEY_ATTRIBUTE).get(PRODUCT_ID_ATTRIBUTE));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(poRoot.get(ProductFullfilmentChanel_.key).get(ProductFullfilmentChanelKey_.productId),
                root.get(AlertStaging_.alertKey).as(Long.class)));
        List<Predicate> predicates1 = new ArrayList<>();
        if (!StringUtils.isEmpty(salesChannels)) {
            List<String> salesChannelCodes = Arrays.asList(salesChannels.split(COMMA));
            for (String salesChannel : salesChannelCodes) {
                predicates1.add(cb.equal(cb.trim(poRoot.get(ProductFullfilmentChanel_.key).get(ProductFullfilmentChanelKey_.salesChanelCode)), salesChannel.trim()));
            }
        }
        predicates.add(cb.or(predicates1.toArray(new Predicate[predicates1.size()])));
        productFulfillmentChannelExist.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return productFulfillmentChannelExist;
    }

    /**
     * Builds predicate to fetch alert referenced product exist in product master.
     *
     * @param root          The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder  JPA query builder used to construct the sub-query.
     * @param cb            criteria builder, used to construct the various parts of the SQL statement.
     * @return predicate alerts with matching product master.
     */
    private Subquery<ProductMaster> buildProductMasterPredicate(Root<AlertStaging> root,
                                                                CriteriaQuery<?> queryBuilder, CriteriaBuilder cb) {
        Subquery<ProductMaster> productMasterExist = queryBuilder.subquery(ProductMaster.class);
        Root<ProductMaster> poRoot = productMasterExist.from(ProductMaster.class);
        productMasterExist.select(poRoot.get(PROD_ID_ATTRIBUTE));
        Predicate[] predicates = new Predicate[1];
        predicates[0] = cb.equal(poRoot.get(ProductMaster_.prodId),
                root.get(AlertStaging_.alertKey).as(Long.class));
        productMasterExist.where(cb.and(predicates));

        return productMasterExist;
    }
}
