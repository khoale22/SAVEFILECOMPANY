package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

/**
 * Handles searching by eCommerceBrand.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class EcommerceBrandPredicateBuilder implements PredicateBuilder {

	/**
	 * Constructs a subquery that joins product_master to mst_dta_extn_attr and constrains on eCommerce Brand. It only
	 * includes source system 13.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A subquery that joins product_master to mst_dta_extn_attr and constrains on eCommerce Brand.
	 */
	@Override
	public ExistsClause<MasterDataExtensionAttribute> buildPredicate(CriteriaBuilder criteriaBuilder,
										  Root<? extends ProductMaster> pmRoot,
										  CriteriaQuery<? extends ProductMaster> queryBuilder,
										  ProductSearchCriteria productSearchCriteria,
										  ParameterExpression<String> sessionIdBindVariable) {

		// See if there is an entry in the custom searches for eCommerce description
		if (productSearchCriteria.getCustomSearchEntries() == null) {
			return null;
		}

		CustomSearchEntry brandEntry = null;
		for (CustomSearchEntry c : productSearchCriteria.getCustomSearchEntries()) {
			if (c.getType() == CustomSearchEntry.ECOMMERCE_BRAND) {
				brandEntry = c;
			}
		}
		if (brandEntry == null || brandEntry.getStringComparator() == null) {
			return null;
		}

		Subquery<MasterDataExtensionAttribute> productExists =
				queryBuilder.subquery(MasterDataExtensionAttribute.class);
		Root<MasterDataExtensionAttribute> mdRoot = productExists.from(MasterDataExtensionAttribute.class);
		productExists.select(mdRoot.get("key").get("id"));

		Predicate[] constraints = new Predicate[5];

		// Add a constraint for the attribute ID
		if (!productSearchCriteria.getSearchAllExtendedAttributes()) {
			constraints[0] = criteriaBuilder.equal(
					mdRoot.get(MasterDataExtensionAttribute_.key).get(MasterDataExtensionAttributeKey_.attributeId),
					Attribute.BRAND);
		}

		// Add a constraint for the key type
		constraints[1] = criteriaBuilder.equal(mdRoot.get(
				MasterDataExtensionAttribute_.key).get(MasterDataExtensionAttributeKey_.itemProdIdCode),
				MasterDataExtensionAttributeKey.PRODUCT_KEY_TYPE);

		// Add a constraint for what the user entered
		String regex = String.format("%%%s%%", brandEntry.getStringComparator().toUpperCase());
		constraints[2] = criteriaBuilder.like(
				criteriaBuilder.upper(mdRoot.get(MasterDataExtensionAttribute_.attributeValueText)), regex);

		// Add a constraint for the source system.
		constraints[3] = criteriaBuilder.equal(
				mdRoot.get(MasterDataExtensionAttribute_.key).get(MasterDataExtensionAttributeKey_.dataSourceSystem),
				SourceSystem.SOURCE_SYSTEM_ECOMMERCE);

		// Add a join between mst_dta_extn_attr and product_master
		constraints[4] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId),
				mdRoot.get(MasterDataExtensionAttribute_.key).get(MasterDataExtensionAttributeKey_.id));

		productExists.where(constraints);

		return new ExistsClause<>(productExists);
	}
}
