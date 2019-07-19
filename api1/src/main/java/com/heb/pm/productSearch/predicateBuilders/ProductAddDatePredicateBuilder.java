package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.ProductMaster_;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

/**
 * Handles searching by product add date.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class ProductAddDatePredicateBuilder implements PredicateBuilder  {


	/**
	 * Builds a where clause that constrains product master cre8_ts.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param productSearchCriteria The user's search criteria.
	 * @return A list of predicates that will constrain product master.
	 */
	@Override
	public List<Predicate> buildBasicWhereClause(CriteriaBuilder criteriaBuilder,
											 Root<? extends ProductMaster> pmRoot,
											 ProductSearchCriteria productSearchCriteria) {

		// See if there is an entry in the custom searches for item add date
		if (productSearchCriteria.getCustomSearchEntries() == null) {
			return null;
		}

		CustomSearchEntry productAddEntry = null;
		for (CustomSearchEntry c : productSearchCriteria.getCustomSearchEntries()) {
			if (c.getType() == CustomSearchEntry.PRODUCT_ADD_DATE) {
				productAddEntry = c;
				break;
			}
		}
		if (productAddEntry == null) {
			return null;
		}

		List<Predicate> whereClause = new LinkedList<>();

		// Add the constraint for add date
		switch (productAddEntry.getOperator()) {
			case CustomSearchEntry.EQUAL:
				/* using atStartOfDay to convert LocalDate to LocalDateTime at 0hrs. */
				whereClause.add(criteriaBuilder.between(pmRoot.get(ProductMaster_.createDate),
						productAddEntry.getDateComparator().atStartOfDay(), productAddEntry.getDateComparator().plusDays(1).atStartOfDay()));
				break;
			case CustomSearchEntry.GREATER_THAN:
				whereClause.add(criteriaBuilder.greaterThanOrEqualTo(pmRoot.get(ProductMaster_.createDate),
						productAddEntry.getDateComparator().atStartOfDay()));
				break;
			case CustomSearchEntry.LESS_THAN:
				whereClause.add(criteriaBuilder.lessThanOrEqualTo(pmRoot.get(ProductMaster_.createDate),
						productAddEntry.getDateComparator().atStartOfDay()));
				break;
			case CustomSearchEntry.ONE_WEEK:
				LocalDate today = LocalDate.now();
				LocalDate oneWeekAgo = today.minus(7, ChronoUnit.DAYS);
				whereClause.add(criteriaBuilder.greaterThanOrEqualTo(pmRoot.get(ProductMaster_.createDate),
						oneWeekAgo.atStartOfDay()));
				break;
			case CustomSearchEntry.BETWEEN:
				whereClause.add(criteriaBuilder.between(pmRoot.get(ProductMaster_.createDate),
						productAddEntry.getDateComparator().atStartOfDay(), LocalDateTime.of(productAddEntry.getEndDateComparator(), LocalTime.MAX)));
		}

		return  whereClause;
	}
}
