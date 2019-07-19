package com.heb.pm.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Simple predicate builder to be used in conjunction with SimpleCriteriaBuilder to facilitate dynamic queries.
 *
 * @author m314029
 * @since 2.21.0
 */
public interface SimplePredicateBuilder<T> {
	List<Predicate> buildWhereClause(Root<T> root, CriteriaBuilder builder);
}
