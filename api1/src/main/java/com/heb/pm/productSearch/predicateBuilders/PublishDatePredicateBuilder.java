package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Handles searching by publish date.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class PublishDatePredicateBuilder implements PredicateBuilder {

	/**
	 * Constructs a subquery that joins publctn_aud to product_master and constrains on dates.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A subquery that joins publctn_aud to product_master and constrains on dates.
	 */
	@Override
	public ExistsClause<PublicationAudit> buildPredicate(CriteriaBuilder criteriaBuilder,
										  Root<? extends ProductMaster> pmRoot,
										  CriteriaQuery<? extends ProductMaster> queryBuilder,
										  ProductSearchCriteria productSearchCriteria,
										  ParameterExpression<String> sessionIdBindVariable) {

		// See if there is an entry in the custom searches for publication date
		if (productSearchCriteria.getCustomSearchEntries() == null) {
			return null;
		}

		CustomSearchEntry publishDateEntry = null;
		for (CustomSearchEntry c : productSearchCriteria.getCustomSearchEntries()) {
			if (c.getType() == CustomSearchEntry.PUBLISH_DATE) {
				publishDateEntry = c;
			}
		}

		if (publishDateEntry == null || publishDateEntry.getDateComparator() == null) {
			return null;
		}

		if (publishDateEntry.getOperator() == CustomSearchEntry.BETWEEN) {
			if (publishDateEntry.getEndDateComparator() == null) {
				return null;
			}
		}

		Subquery<PublicationAudit> productExist = queryBuilder.subquery(PublicationAudit.class);
		Root<PublicationAudit> paRoot = productExist.from(PublicationAudit.class);
		productExist.select(paRoot.get("key").get("itemProductId"));

		Predicate[] criteria = new Predicate[3];

		// Join product master to publication audit
		criteria[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId),
				paRoot.get(PublicationAudit_.key).get(PublicationAuditKey_.itemProductId));

		// Constrain on type product
		criteria[1] = criteriaBuilder.equal(
				paRoot.get(PublicationAudit_.key).get(PublicationAuditKey_.itemProductKeyCode),
				PublicationAuditKey.PRODUCT_KEY_TYPE);

		// Add the constraint for add date
		switch (publishDateEntry.getOperator()) {
			case CustomSearchEntry.EQUAL:
				// Since we can't programmatically remove the time part of the date, use a between with the
				// requested date and that date + 1
				criteria[2] = criteriaBuilder.between(
						paRoot.get(PublicationAudit_.key).get(PublicationAuditKey_.publishDate),
						publishDateEntry.getDateComparator().atStartOfDay(),
						publishDateEntry.getDateComparator().plusDays(1).atStartOfDay());
				break;
			case CustomSearchEntry.GREATER_THAN:
				// greater than or equal to the beginning of the day the user chose.
				criteria[2] = criteriaBuilder.greaterThanOrEqualTo(
						paRoot.get(PublicationAudit_.key).get(PublicationAuditKey_.publishDate),
						publishDateEntry.getDateComparator().atStartOfDay());
				break;
			case CustomSearchEntry.LESS_THAN:
				// Less than the beginning of the day after the day the user selected.
				criteria[2] = criteriaBuilder.lessThan(
						paRoot.get(PublicationAudit_.key).get(PublicationAuditKey_.publishDate),
						publishDateEntry.getDateComparator().plusDays(1).atStartOfDay());
				break;
			case CustomSearchEntry.ONE_WEEK:
				// Greater than one week ago.
				LocalDate today = LocalDate.now();
				LocalDate oneWeekAgo = today.minusDays(7);
				criteria[2] = criteriaBuilder.greaterThanOrEqualTo(
						paRoot.get(PublicationAudit_.key).get(PublicationAuditKey_.publishDate),
						oneWeekAgo.atStartOfDay());
				break;
			case CustomSearchEntry.BETWEEN:
				// Have to look at the beginning of the start and the beginning of the day the user selected.
				criteria[2] = criteriaBuilder.between(
						paRoot.get(PublicationAudit_.key).get(PublicationAuditKey_.publishDate),
						publishDateEntry.getDateComparator().atStartOfDay(),
						publishDateEntry.getEndDateComparator().atStartOfDay());
		}

		productExist.where(criteria);

		return new ExistsClause<>(productExist);
	}
}
