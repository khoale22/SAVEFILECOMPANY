/*
 *  CheckStatusPredicateBuilder
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload.checkstatus;


import com.heb.pm.entity.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This class helps to build predicates for TransactionTracker based on basic conditions searchCriteria.
 *
 * @author vn70529
 * @since 2.34.0
 */
@Service
public class CheckStatusPredicateBuilder {
    /**
     * Percent char for like search
     */
    private static final String LIKE_REGEX_EXPRESSION = "%%%s%%";
    /**
     * Holds the character to escape wildcard
     */
    private static final char ESCAPE_WILDCARDS_CHAR = '\\';
    /**
     * Holds the percent wildcard.
     */
    private static final String PERCENT_WILDCARDS_CHAR = "\\%";
    /**
     * Holds the under score wildcard
     */
    private static final String UNDERSCORE_WILDCARDS_CHAR = "\\_";
    /**
     * tracking id field name.
     */
    private static final String TRACKING_ID_FIELD_NAME = "trackingId";

    /**
     * Used to build predicates(WHERE clause) for fetching transaction tracker. Makes a list of predicates based on the
     * filter condition input.
     *
     * @param root            The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder    JPA query builder used to construct the sub-query.
     * @param criteriaBuilder Used to construct the various parts of the SQL statement.
     * @param requestId       the request id.
     * @param attribute       the attribute of tracking.
     * @param description     the description of tracking.
     * @param startDate       the start date tracking.
     * @param endDate         the end date tracking.
     * @param userId          the user created tracking.
     * @return Predicate
     */
    protected Predicate buildPredicate(Root<TransactionTracker> root, CriteriaQuery<?> queryBuilder,
                                       CriteriaBuilder criteriaBuilder, String requestId, String attribute, String description, String startDate, String endDate, String userId) {
        Specification<TransactionTracker> spec = buildSpecification(requestId, attribute, description, startDate, endDate, userId);
        return spec.toPredicate(root, queryBuilder, criteriaBuilder);
    }

    /**
     * Gives structure to the predicates to be built for searching TransactionTracker.
     *
     * @param requestId   the request id.
     * @param attribute   the attribute of tracking.
     * @param description the description of tracking.
     * @param startDate   the start date tracking.
     * @param endDate     the end date tracking.
     * @param userId      the user created tracking.
     * @return Specification<TransactionTracker>
     */
    private Specification<TransactionTracker> buildSpecification(String requestId, String attribute, String description, String startDate, String endDate, String userId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //add clause query for file name.
            Expression<String> inExpression = root.get(TransactionTracker_.fileNm);
            Predicate inPredicate = inExpression.in(CheckStatusService.getListOfDefaultFileName());
            predicates.add(cb.not(inPredicate));
            //create sub query CandidateWorkRequest for TransactionTracker.
            Subquery<CandidateWorkRequest> subquery = buildSubPredicate(root, query, cb);
            predicates.add(cb.exists(subquery));
            //add clause query tracking id if request id filter is not blank.
            if (StringUtils.isNotBlank(requestId)) {
                predicates.add(cb.like(root.get(TransactionTracker_.trackingId).as(String.class), this.formatKeywordForLikeSearch(requestId.toUpperCase(), cb), ESCAPE_WILDCARDS_CHAR));
            }
            //add clause query attribute if attribute filter is not blank.
            if (StringUtils.isNotBlank(attribute)) {
                String attributeFilter = attribute == null ? StringUtils.EMPTY : attribute.trim().toUpperCase();
                predicates.add(cb.like(cb.upper(root.get(TransactionTracker_.fileNm)), this.formatKeywordForLikeSearch(attributeFilter, cb), ESCAPE_WILDCARDS_CHAR));
            }
            //add clause query description if description filter is not blank.
            if (StringUtils.isNotBlank(description)) {
                String descriptionFilter = description == null ? StringUtils.EMPTY : description.trim().toUpperCase();
                predicates.add(cb.like(cb.upper(root.get(TransactionTracker_.fileDes)), this.formatKeywordForLikeSearch(descriptionFilter, cb), ESCAPE_WILDCARDS_CHAR));
            }
            //add clause query start date if startDate filter is not blank.
            if (StringUtils.isNotBlank(startDate)) {
                LocalDate startDateSearch = LocalDate.parse(startDate);
                predicates.add(cb.greaterThanOrEqualTo(root.get(TransactionTracker_.createDate), startDateSearch.atStartOfDay()));
            }

            //add clause query end date if endDate filter is not blank.
            if (StringUtils.isNotBlank(endDate)) {
                LocalDate endDateSearch = LocalDate.parse(endDate);
                predicates.add(cb.lessThan(root.get(TransactionTracker_.createDate), endDateSearch.plusDays(1).atStartOfDay()));
            }

            //add clause query user created if userId filter is not blank.
            if (StringUtils.isNotBlank(userId)) {
                predicates.add(cb.equal(cb.lower(root.get(TransactionTracker_.userId)), userId.toLowerCase()));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Builds predicate to fetch CandidateWorkRequest referenced TransactionTracker with matching tracking id.
     *
     * @param root            The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder    JPA query builder used to construct the sub-query.
     * @param cb criteria builder, used to construct the various parts of the SQL statement.
     * @return Subquery<CandidateWorkRequest>
     */
    private Subquery<CandidateWorkRequest> buildSubPredicate(Root<TransactionTracker> root,
                                                             CriteriaQuery<?> queryBuilder, CriteriaBuilder cb) {
        Subquery<CandidateWorkRequest> candidateWorkRequestSubquery = queryBuilder.subquery(CandidateWorkRequest.class);
        Root<CandidateWorkRequest> candidateWorkRequestRoot = candidateWorkRequestSubquery.from(CandidateWorkRequest.class);
        candidateWorkRequestSubquery.select(candidateWorkRequestRoot.get(TRACKING_ID_FIELD_NAME));

        Predicate[] predicates = new Predicate[2];
        predicates[0] = cb.equal(candidateWorkRequestRoot.get(CandidateWorkRequest_.trackingId),
                root.get(TransactionTracker_.trackingId));
        //add clause query for source system .
        Expression<Integer> inExpression = candidateWorkRequestRoot.get(CandidateWorkRequest_.sourceSystem);
        predicates[1] = inExpression.in(CheckStatusService.getListOfDefaultResource());;
        candidateWorkRequestSubquery.where(cb.and(predicates));

        return candidateWorkRequestSubquery;
    }

    /**
     * Get format keyword for like search.
     *
     * @param keyword the keyword to search.
     * @return the keyword for like search.
     */
    public Expression<String> formatKeywordForLikeSearch(String keyword, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.literal(String.format(LIKE_REGEX_EXPRESSION, escapeWildcards(keyword)));
    }

    /**
     * Escape wildcard for keyword to search.
     *
     * @param keyword the keyword to search
     * @return the keyword after escaped.
     */
    private String escapeWildcards(String keyword) {
        keyword = keyword.replaceAll(PERCENT_WILDCARDS_CHAR, String.format("%s%s", ESCAPE_WILDCARDS_CHAR, PERCENT_WILDCARDS_CHAR));
        return keyword.replaceAll(UNDERSCORE_WILDCARDS_CHAR, String.format("%s%s", ESCAPE_WILDCARDS_CHAR, UNDERSCORE_WILDCARDS_CHAR));
    }
}
