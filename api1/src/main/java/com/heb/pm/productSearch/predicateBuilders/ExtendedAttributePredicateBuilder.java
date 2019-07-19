package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.ProductSearchCriteria;

import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

/**
 * Handles searching by descriptions in mst_dta_extn_attr.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class ExtendedAttributePredicateBuilder implements PredicateBuilder {

	/**
	 * Build predicates when the user us searching against mst_dta_extn_attr. It handles two cases. The first is
	 * against any text attribute. The second is only the display name. In all cases, it will only return products
	 * that are published to source 13.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A predicate that joins product master to mst_dta_extn_attr and constrains on the text attribue.
	 */
	@Override
	public ExistsClause<MasterDataExtensionAttribute> buildPredicate(CriteriaBuilder criteriaBuilder,
																	 Root<? extends ProductMaster> pmRoot,
									  CriteriaQuery<? extends ProductMaster> queryBuilder,
									  ProductSearchCriteria productSearchCriteria,
									  ParameterExpression<String> sessionIdBindVariable) {

		if (!(productSearchCriteria.getSearchByDisplayName() || productSearchCriteria.getSearchAllExtendedAttributes())) {
			return null;
		}

		Subquery<MasterDataExtensionAttribute> productExists =
				queryBuilder.subquery(MasterDataExtensionAttribute.class);
		Root<MasterDataExtensionAttribute> mdRoot = productExists.from(MasterDataExtensionAttribute.class);
		productExists.select(mdRoot.get("key").get("id"));

		int predicateCount = productSearchCriteria.getSearchAllExtendedAttributes() ? 4 : 5;

		Predicate[] constraints = new Predicate[predicateCount];

		int currentPredicate = 0;

		// Add a constraint for the attribute ID if the user did not check searching by all attributes.
		if (!productSearchCriteria.getSearchAllExtendedAttributes()) {
			constraints[currentPredicate++] = criteriaBuilder.equal(
					mdRoot.get(MasterDataExtensionAttribute_.key).get(MasterDataExtensionAttributeKey_.attributeId),
					Attribute.DISPLAY_NAME);
		}

		// Add a constraint for the key type
		constraints[currentPredicate++] = criteriaBuilder.equal(mdRoot.get(
				MasterDataExtensionAttribute_.key).get(MasterDataExtensionAttributeKey_.itemProdIdCode),
				MasterDataExtensionAttributeKey.PRODUCT_KEY_TYPE);

		// Add a constraint for what the user entered
		String regex = String.format("%%%s%%", productSearchCriteria.getDescription().toUpperCase());
		constraints[currentPredicate++] = criteriaBuilder.like(
				criteriaBuilder.upper(mdRoot.get(MasterDataExtensionAttribute_.attributeValueText)), regex);

		// Add a constraint for the source system.
		constraints[currentPredicate++] = criteriaBuilder.equal(
				mdRoot.get(MasterDataExtensionAttribute_.key).get(MasterDataExtensionAttributeKey_.dataSourceSystem),
				SourceSystem.SOURCE_SYSTEM_ECOMMERCE);

		// Add a join between mst_dta_extn_attr and product_master
		constraints[currentPredicate] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId),
				mdRoot.get(MasterDataExtensionAttribute_.key).get(MasterDataExtensionAttributeKey_.id));

		productExists.where(constraints);

		return new ExistsClause<>(productExists);
	}
}
