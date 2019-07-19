package com.heb.pm.repository;

import com.heb.util.jpa.PageableResult;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Builds a dynamic query from a simple predicate builder, and only makes a count query if the
 * caller declares as first search.
 *
 * @author m314029
 * @since 2.21.0
 */
public class SimpleCriteriaBuilder <T extends Serializable> {

	private final EntityManager entityManager;
	private final Class<T> classType;

	public SimpleCriteriaBuilder(EntityManager entityManager, Class<T> classType){
		this.entityManager = entityManager;
		this.classType = classType;
	}

	/**
	 * This gets a pageable result
	 *
	 * @param page Page requested (cannot be null).
	 * @param pageSize Number of records per page (cannot be null).
	 * @param sort How to sort data (can be null).
	 * @param firstSearch Identifies first fetch where count query will also be ran for pagination (can be null -- will
	 *                    default to false).
	 * @param predicateBuilder Simple predicate builder for the given class that defines a where clause for a SQL
	 *                        statement (can be null).
	 * @return The requested page.
	 */
	public PageableResult<T> getPageableResult(
			int page, int pageSize, Sort sort, Boolean firstSearch,
			SimplePredicateBuilder<T> predicateBuilder){
		if(this.entityManager == null || this.classType == null){
			throw new IllegalArgumentException("Please call constructor for SimpleCriteriaBuilder first.");
		}

		CriteriaBuilder criteriaBuilder =  this.entityManager.getCriteriaBuilder();

		// Builds the criteria for the main query
		CriteriaQuery<T> queryBuilder = criteriaBuilder.createQuery(this.classType);

		// Select from attribute meta data.
		Root<T> root = queryBuilder.from(this.classType);

		// Build the where clause
		Predicate[] whereClause = null;
		if(predicateBuilder != null) {
			whereClause = this.getWhereClause(criteriaBuilder, root, predicateBuilder);
			queryBuilder.where(criteriaBuilder.and(whereClause));
		}

		// Add the sort
		if(sort != null) {
			queryBuilder.orderBy(this.getSort(criteriaBuilder, root, sort));
		}

		// Get the query
		TypedQuery<T> query = this.entityManager.createQuery(queryBuilder);

		// Sets the first row to grab and the maximum number to grab for pagination.
		query.setFirstResult(page * pageSize).setMaxResults(pageSize);

		// Execute the query.
		List<T> results = query.getResultList();

		// If the user requested counts, build and execute that query.
		if (firstSearch) {
			long count;
			int pageCount;

			// It's a new query
			CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
			// Same from and where, just wrapping a count around it.
			countQuery.select(criteriaBuilder.count(countQuery.from(this.classType)));
			if(whereClause != null) {
				countQuery.where(whereClause);
			}

			// Run the query
			TypedQuery<Long> countTQuery = this.entityManager.createQuery(countQuery);

			count = countTQuery.getSingleResult();

			// Calculate how many pages of data there are.
			pageCount = (int) count / pageSize;
			pageCount += (int) count % pageSize == 0 ? 0 : 1;

			// Return results plus count
			return new PageableResult<>(page, pageCount, count, results);
		} else {
			// Return only results
			return new PageableResult<>(page, results);
		}
	}

	/**
	 * Builds the WHERE clause for the main and count query.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param root The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to.
	 * @param predicateBuilder The user's search criteria.
	 * @return The WHERE clause for the main and count query.
	 */
	private Predicate[] getWhereClause(CriteriaBuilder criteriaBuilder, Root<T> root, SimplePredicateBuilder<T> predicateBuilder) {

		List<Predicate> exists = predicateBuilder.buildWhereClause(root, criteriaBuilder);
		return exists.toArray(new Predicate[exists.size()]);
	}

	/**
	 * Converts the Sort as defined in 'getPageableResult' method parameters into the one needed by JPA criteria query.
	 *
	 * @param criteriaBuilder The CriteriaBuilder to use.
	 * @param root The root object for the query.

	 * @return A list of sort orders to be added to the order by clause.
	 */
	private List<Order> getSort(CriteriaBuilder criteriaBuilder, Root<T> root, Sort sort) {

		List<Order> sortOrders = new LinkedList<>();

		sort.iterator().forEachRemaining((s) -> {
			if (s.isAscending()) {
				sortOrders.add(criteriaBuilder.asc(root.get(s.getProperty())));
			}
		});

		return sortOrders;
	}
}
