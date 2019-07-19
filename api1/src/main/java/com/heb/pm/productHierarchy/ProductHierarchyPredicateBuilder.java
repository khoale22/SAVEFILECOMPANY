/*
 * ProductHierarchyPredicateBuilder.java
 *
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.productHierarchy;

import com.heb.pm.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class helps to build predicates for product hierarchy based on keyword and exclude list.
 *
 * @author vn70529
 * @since 2.33.0
 */
@Service
public class ProductHierarchyPredicateBuilder {
    /**
     * Percent char for like search
     */
    private static final String LIKE_REGEX_EXPRESSION = "%%%s%%";
    /**
     * Bracket for searching by display name with format: <name>[<code>]
     */
    private static final String LEFT_BRACKET_CHAR = "[";
    private static final String RIGHT_BRACKET_CHAR = "]";
    private static final String ONE_SPACE_CHAR = " ";
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
     * Used to build predicates(WHERE clause) for fetching sub commodity. Makes a list of predicates based on the
     * filter condition input and exclude list. Add finally compose them into a single Predicate joined by AND condition.
     *
     * @param root            The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder    JPA query builder used to construct the sub-query.
     * @param criteriaBuilder Used to construct the various parts of the SQL statement.
     * @param keyword         the key word to search
     * @return Predicate(WHERE clause)
     */
    public Predicate buildSubCommodityPredicate(Root<SubCommodity> root, CriteriaQuery<SubCommodity> queryBuilder,
                                                CriteriaBuilder criteriaBuilder, String keyword) {
        return buildSubCommoditySpecification(keyword).toPredicate(root, queryBuilder, criteriaBuilder);
    }

    /**
     * Used to build predicates(WHERE clause) for fetching ClassCommodity. Makes a list of predicates based on the
     * filter condition input and exclude list. Add finally compose them into a single Predicate joined by AND condition.
     *
     * @param root                    The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder            JPA query builder used to construct the sub-query.
     * @param criteriaBuilder         Used to construct the various parts of the SQL statement.
     * @param keyword                 the key word to search
     * @param excludeClsCommodityKeys the exclude list of class commodity keys that doesn't want to include them in search results.
     * @return Predicate(WHERE clause)
     */
    public Predicate buildClassCommodityPredicate(Root<ClassCommodity> root, CriteriaQuery<ClassCommodity> queryBuilder,
                                                  CriteriaBuilder criteriaBuilder, String keyword, List<ClassCommodityKey> excludeClsCommodityKeys) {
        return buildClassCommoditySpecification(keyword, excludeClsCommodityKeys).toPredicate(root, queryBuilder, criteriaBuilder);
    }

    /**
     * Used to build predicates(WHERE clause) for fetching ItemClass. Makes a list of predicates based on the
     * filter condition input and exclude list. Add finally compose them into a single Predicate joined by AND condition.
     *
     * @param root                The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder        JPA query builder used to construct the sub-query.
     * @param criteriaBuilder     Used to construct the various parts of the SQL statement.
     * @param keyword             the key word to search
     * @param excludeItemClassIds the exclude list of item class keys that doesn't want to include them in search results.
     * @return Predicate(WHERE clause)
     */
    public Predicate buildItemClassSpecification(Root<ItemClass> root, CriteriaQuery<ItemClass> queryBuilder,
                                                 CriteriaBuilder criteriaBuilder, String keyword, List<Integer> excludeItemClassIds) {
        return buildItemClassSpecification(keyword, excludeItemClassIds).toPredicate(root, queryBuilder, criteriaBuilder);
    }

    /**
     * Used to build predicates(WHERE clause) for fetching SubDepartment. Makes a list of predicates based on the
     * filter condition input and exclude list. Add finally compose them into a single Predicate joined by AND condition.
     *
     * @param root               The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder       JPA query builder used to construct the sub-query.
     * @param criteriaBuilder    Used to construct the various parts of the SQL statement.
     * @param keyword            the key word to search.
     * @param excludeSubDeptKeys the exclude list of sub department keys that doesn't want to include them in search results.
     * @return Predicate(WHERE clause)
     */
    public Predicate buildSubDeptSpecification(Root<SubDepartment> root, CriteriaQuery<SubDepartment> queryBuilder,
                                               CriteriaBuilder criteriaBuilder, String keyword, List<SubDepartmentKey> excludeSubDeptKeys) {
        return buildSubDepartmentSpecification(keyword, excludeSubDeptKeys).toPredicate(root, queryBuilder, criteriaBuilder);
    }

    /**
     * Used to build predicates(WHERE clause) for fetching Department. Makes a list of predicates based on the
     * filter condition input and exclude list. Add finally compose them into a single Predicate joined by AND condition.
     *
     * @param root            The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder    JPA query builder used to construct the sub-query.
     * @param criteriaBuilder Used to construct the various parts of the SQL statement.
     * @param keyword         the key word to search.
     * @param excludeDeptKeys the exclude list of department keys that doesn't want to include them in search results.
     * @return Predicate(WHERE clause)
     */
    public Predicate buildDepartmentSpecification(Root<Department> root, CriteriaQuery<Department> queryBuilder,
                                                  CriteriaBuilder criteriaBuilder, String keyword, List<SubDepartmentKey> excludeDeptKeys) {
        return buildDepartmentSpecification(keyword, excludeDeptKeys).toPredicate(root, queryBuilder, criteriaBuilder);
    }

    /**
     * Used to build predicates(WHERE clause) for fetching SubDepartment. Makes a list of predicates based on the
     * the department code and sub deparment code. Add finally compose them into a single Predicate joined by AND condition.
     *
     * @param root              The root from clause of the main query (this will be used to grab the criteria to join the sub-query to).
     * @param queryBuilder      JPA query builder used to construct the sub-query.
     * @param departmentCode    the department code.
     * @param subDepartmentCode the sub department code.
     * @return Predicate(WHERE clause)
     */
    public Predicate buildSubDeptSpecification(Root<SubDepartment> root, CriteriaQuery<SubDepartment> queryBuilder,
                                               CriteriaBuilder criteriaBuilder, Integer departmentCode, String subDepartmentCode) {
        return buildSubDepartmentSpecification(departmentCode, subDepartmentCode).toPredicate(root, queryBuilder, criteriaBuilder);
    }

    /**
     * Gives structure to the predicates to be built for searching sub commodity by keyword.
     *
     * @param keyword the keyword for search.
     * @return the Specification.
     */
    private Specification<SubCommodity> buildSubCommoditySpecification(String keyword) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Build predicates for searching by display name as format: <name>[<sub commodityCode>].
            Expression<String> name = cb.trim(root.get(SubCommodity_.name));
            Expression<String> subComCode = cb.trim(root.get(SubCommodity_.subCommodityCode).as(String.class));
            predicates.add(cb.like(cb.upper(this.createDisplayNameExpression(name, subComCode, cb)), this.formatKeywordForLikeSearch(keyword, cb), ESCAPE_WILDCARDS_CHAR));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Gives structure to the predicates to be built for searching class commodity by keyword and exclude class commodity keys.
     *
     * @param keyword                 the keyword for search.
     * @param excludeClsCommodityKeys the list of class commodity key that excludes in searching time.
     * @return the Specification for search ClassCommodity.
     */
    private Specification<ClassCommodity> buildClassCommoditySpecification(String keyword, List<ClassCommodityKey> excludeClsCommodityKeys) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Build predicates for searching by display name as format: <name>[<commodityCode>].
            Expression<String> name = cb.trim(root.get(ClassCommodity_.name));
            Expression<String> commodityCode = cb.trim(root.get(ClassCommodity_.commodityCode).as(String.class));
            predicates.add(cb.like(cb.upper(this.createDisplayNameExpression(name, commodityCode, cb)), this.formatKeywordForLikeSearch(keyword, cb), ESCAPE_WILDCARDS_CHAR));
            predicates.add(cb.greaterThan(root.get(ClassCommodity_.commodityCode), 0));

            // add conditions for ignore class commodity that existing search results.
            if (excludeClsCommodityKeys != null && !excludeClsCommodityKeys.isEmpty()) {
                for (ClassCommodityKey item : excludeClsCommodityKeys) {
                    Predicate predicate = cb.notEqual(root.get(ClassCommodity_.key).get(ClassCommodityKey_.classCode), item.getClassCode());
                    predicates.add(cb.or(predicate, cb.notEqual(root.get(ClassCommodity_.key).get(ClassCommodityKey_.commodityCode), item.getCommodityCode())));
                }
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Gives structure to the predicates to be built for searching item class by keyword and exclude list of item class ids.
     *
     * @param keyword             the keyword for search.
     * @param excludeItemClassIds the list of item class ids that excludes in searching time.
     * @return the Specification for search item class.
     */
    private Specification<ItemClass> buildItemClassSpecification(String keyword, List<Integer> excludeItemClassIds) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Build predicates for searching by display name as format: <itemClassDescription>[<itemClassCode>].
            Expression<String> itemClassDescription = cb.trim(root.get(ItemClass_.itemClassDescription));
            Expression<String> itemClassCode = cb.trim(root.get(ItemClass_.itemClassCode).as(String.class));
            predicates.add(cb.like(cb.upper(this.createDisplayNameExpression(itemClassDescription, itemClassCode, cb)),
                    formatKeywordForLikeSearch(keyword, cb), ESCAPE_WILDCARDS_CHAR));
            // add conditions for ignore item class that existing search results.
            if (excludeItemClassIds != null && !excludeItemClassIds.isEmpty()) {
                predicates.add(cb.not(root.get(ItemClass_.itemClassCode).in(excludeItemClassIds)));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Gives structure to the predicates to be built for searching SubDepartment by keyword and exclude list of SubDepartmentKeys
     *
     * @param keyword            the keyword for search.
     * @param excludeSubDeptKeys the list of SubDepartment key that excludes in searching time.
     * @return the Specification for search SubDepartment.
     */
    private Specification<SubDepartment> buildSubDepartmentSpecification(String keyword, List<SubDepartmentKey> excludeSubDeptKeys) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Build predicates for searching by display name as format: <name>[<subDepartment>].
            Expression<String> name = cb.trim(root.get(SubDepartment_.name));
            Expression<String> departmentCode = cb.trim(root.get(SubDepartment_.key).get(SubDepartmentKey_.department));
            Expression<String> subDepartmentCode = cb.trim(root.get(SubDepartment_.key).get(SubDepartmentKey_.subDepartment));
            predicates.add(cb.like(cb.upper(this.createDisplayNameExpression(name, departmentCode, subDepartmentCode, cb)),
                    this.formatKeywordForLikeSearch(keyword, cb), ESCAPE_WILDCARDS_CHAR));
            predicates.add(cb.notEqual(root.get(SubDepartment_.key).get(SubDepartmentKey_.subDepartment), ONE_SPACE_CHAR));

            // add conditions for ignore class sub department that existing search results.
            if (excludeSubDeptKeys != null && !excludeSubDeptKeys.isEmpty()) {
                for (SubDepartmentKey item : excludeSubDeptKeys) {
                    Predicate predicate = cb.notEqual(root.get(SubDepartment_.key).get(SubDepartmentKey_.department), item.getDepartment());
                    predicates.add(cb.or(predicate, cb.notEqual(root.get(SubDepartment_.key).get(SubDepartmentKey_.subDepartment), item.getSubDepartment())));
                }
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Gives structure to the predicates to be built for get SubDepartment by departmentCode and subDepartmentCode
     *
     * @param departmentCode    the keyword for search.
     * @param subDepartmentCode the list of SubDepartment key that excludes in searching time.
     * @return the Specification for search SubDepartment.
     */
    private Specification<SubDepartment> buildSubDepartmentSpecification(Integer departmentCode, String subDepartmentCode) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(cb.trim(root.get(SubDepartment_.key).get(SubDepartmentKey_.department)).as(Integer.class), departmentCode));
            predicates.add(cb.equal(root.get(SubDepartment_.key).get(SubDepartmentKey_.subDepartment), subDepartmentCode));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Gives structure to the predicates to be built for searching Department by key word and exclude list of DepartmentKeys
     *
     * @param keyword         the keyword for search.
     * @param excludeDeptKeys the list of Department key that excludes in searching time.
     * @return the Specification for search Department.
     */
    private Specification<Department> buildDepartmentSpecification(String keyword, List<SubDepartmentKey> excludeDeptKeys) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Build predicates for searching by display name as format: <name>[<department>].
            Expression<String> name = cb.trim(root.get(Department_.name));
            Expression<String> departmentCode = cb.trim(root.get(Department_.key).get(SubDepartmentKey_.department));
            Expression<String> subDepartmentCode = root.get(Department_.key).get(SubDepartmentKey_.subDepartment);
            predicates.add(cb.like(cb.upper(this.createDisplayNameExpression(name, departmentCode, subDepartmentCode, cb)),
                    this.formatKeywordForLikeSearch(keyword, cb), ESCAPE_WILDCARDS_CHAR));

            // add conditions for ignore department that existing search results.
            if (excludeDeptKeys != null && !excludeDeptKeys.isEmpty()) {
                for (SubDepartmentKey item : excludeDeptKeys) {
                    Predicate predicate = cb.notEqual(root.get(Department_.key).get(SubDepartmentKey_.department), item.getDepartment());
                    predicates.add(cb.or(predicate, cb.notEqual(root.get(Department_.key).get(SubDepartmentKey_.subDepartment), item.getSubDepartment())));
                }
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Create display name expression for searching by format: <name>[<code>]
     *
     * @param name            the name expression.
     * @param code            the code expression.
     * @param criteriaBuilder the criteria builder.
     * @return the display name expression with format <name>[<code>].
     */
    private Expression<String> createDisplayNameExpression(Expression<String> name, Expression<String> code, CriteriaBuilder criteriaBuilder) {
        Expression<String> displayName = criteriaBuilder.concat(name, LEFT_BRACKET_CHAR);
        displayName = criteriaBuilder.concat(displayName, code);
        displayName = criteriaBuilder.concat(displayName, RIGHT_BRACKET_CHAR);
        return displayName;
    }

    /**
     * Create display name expression for searching by format: <name>[<dept code><sub dept code>]
     *
     * @param name            the name expression.
     * @param deptCode        the department code expression.
     * @param subDeptCode     the sub department code expression.
     * @param criteriaBuilder the criteria builder.
     * @return the display name expression with format <name>[<code>].
     */
    private Expression<String> createDisplayNameExpression(Expression<String> name, Expression<String> deptCode,
                                                           Expression<String> subDeptCode, CriteriaBuilder criteriaBuilder) {
        Expression<String> displayName = criteriaBuilder.concat(name, LEFT_BRACKET_CHAR);
        displayName = criteriaBuilder.concat(displayName, deptCode);
        displayName = criteriaBuilder.concat(displayName, subDeptCode);
        displayName = criteriaBuilder.concat(displayName, RIGHT_BRACKET_CHAR);
        return displayName;
    }

    /**
     * Get format keyword for like search.
     *
     * @param keyword the keyword to search.
     * @return the keyword for like search.
     */
    private Expression<String> formatKeywordForLikeSearch(String keyword, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.literal(String.format(LIKE_REGEX_EXPRESSION, escapeWildcards(keyword)));
    }

    /**
     * Escape wildcard for keyword to search.
     *
     * @param keyword the keyword to search
     * @return the keyword after escaped.
     */
    private static String escapeWildcards(String keyword) {
        keyword = keyword.replaceAll(PERCENT_WILDCARDS_CHAR, String.format("%s%s", ESCAPE_WILDCARDS_CHAR, PERCENT_WILDCARDS_CHAR));
        return keyword.replaceAll(UNDERSCORE_WILDCARDS_CHAR, String.format("%s%s", ESCAPE_WILDCARDS_CHAR, UNDERSCORE_WILDCARDS_CHAR));
    }
}
