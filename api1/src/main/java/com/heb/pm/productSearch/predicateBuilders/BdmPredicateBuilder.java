package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.*;
import com.heb.pm.productSearch.ProductSearchCriteria;
import com.heb.pm.repository.SearchCriteriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Handles searching by BDM.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
class BdmPredicateBuilder implements PredicateBuilder {

	@Autowired
	private SearchCriteriaRepository repository;

	/**
	 * Adds BDMs to the temp table.
	 *
	 * @param productSearchCriteria The user's product search criteria.
	 */
	@Override
	@CoreTransactional
	public void populateTempTable(ProductSearchCriteria productSearchCriteria) {

		if (productSearchCriteria.getBdmCodes() == null && productSearchCriteria.getBdm() == null) {
			return;
		}

		List<SearchCriteria> tempTableData = new LinkedList<>();

		if (productSearchCriteria.getBdmCodes() != null) {
			// Searching by BDM, we may get a list of BDMs
			productSearchCriteria.getBdmCodes().forEach((c) -> {
				SearchCriteria searchCriteria = new SearchCriteria();
				searchCriteria.setUniqueId(PredicateBuilderUtil.getUniqueId());
				searchCriteria.setSessionId(productSearchCriteria.getSessionId());
				searchCriteria.setBdmCode(c);
				tempTableData.add(searchCriteria);
			});
		}

		// Or we may get a singular BDM object.
		if (productSearchCriteria.getBdm() != null) {
			SearchCriteria searchCriteria = new SearchCriteria();
			searchCriteria.setUniqueId(PredicateBuilderUtil.getUniqueId());
			searchCriteria.setSessionId(productSearchCriteria.getSessionId());
			searchCriteria.setBdmCode(productSearchCriteria.getBdm().getBdmCode());
			tempTableData.add(searchCriteria);
		}

		this.repository.save(tempTableData);
	}

	/**
	 * Return a sub-query to join product master to commodity to the temp table constraining on BDM.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A sub-query to join product master to commodity to the temp table constraining on BDM.
	 */
	@Override
	public ExistsClause<ClassCommodity> buildPredicate(CriteriaBuilder criteriaBuilder, Root<? extends ProductMaster> pmRoot,
									  CriteriaQuery<? extends ProductMaster> queryBuilder,
									  ProductSearchCriteria productSearchCriteria,
									  ParameterExpression<String> sessionIdBindVariable) {

		if (productSearchCriteria.getBdmCodes() == null && productSearchCriteria.getBdm() == null) {
			return null;
		}

		// We'l be adding sub-query to class-commodity
		Subquery<ClassCommodity> productExists = queryBuilder.subquery(ClassCommodity.class);
		Root<ClassCommodity> ccRoot = productExists.from(ClassCommodity.class);
		productExists.select(ccRoot.get("name"));

		// Join class commodity and the temp table
		ListJoin<ClassCommodity, SearchCriteria> join = ccRoot.join(ClassCommodity_.searchCriteria);

		// Add a join from product_master to class_commodity
		Predicate[] pmToCcJoin = new Predicate[3];
		pmToCcJoin[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.classCode),
				ccRoot.get(ClassCommodity_.classCode));
		pmToCcJoin[1] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.commodityCode),
				ccRoot.get(ClassCommodity_.commodityCode));

		// Add the bind variable for session ID.
		pmToCcJoin[2] = criteriaBuilder.equal(join.get(SearchCriteria_.sessionId), sessionIdBindVariable);

		productExists.where(pmToCcJoin);

		return new ExistsClause<>(productExists);
	}
}
