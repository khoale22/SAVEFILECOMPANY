package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

/**
 * Hanldes searching for products by fulfilment channels.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class FulfilmentChannelPredicateBuilder implements PredicateBuilder {

	/**
	 * Returns a subquery that joins product_master to prod_flfl_chnl and contrains on fulfilment channel.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A subquery that joins product_master to prod_flfl_chnl and contrains on fulfilment channel.
	 */
	@Override
	public ExistsClause<ProductFullfilmentChanel> buildPredicate(CriteriaBuilder criteriaBuilder,
																 Root<? extends ProductMaster> pmRoot,
																 CriteriaQuery<? extends ProductMaster> queryBuilder,
																 ProductSearchCriteria productSearchCriteria,
																 ParameterExpression<String> sessionIdBindVariable) {

		// See if there is an entry in the custom searches for fulfilment channel
		if (productSearchCriteria.getCustomSearchEntries() == null) {
			return null;
		}

		CustomSearchEntry fulfilmentChannelEntry = null;
		for (CustomSearchEntry c : productSearchCriteria.getCustomSearchEntries()) {
			if (c.getType() == CustomSearchEntry.FULFILLMENT_CHANNEL) {
				fulfilmentChannelEntry = c;
			}
		}
		if (fulfilmentChannelEntry == null || fulfilmentChannelEntry.getFulfillmentChannel() == null) {
			return null;
		}

		Subquery<ProductFullfilmentChanel> productExists = queryBuilder.subquery(ProductFullfilmentChanel.class);
		Root<ProductFullfilmentChanel> pfcRoot = productExists.from(ProductFullfilmentChanel.class);
		productExists.select(pfcRoot.get("key").get("productId"));

		Predicate[] constraints = new Predicate[3];

		// Join product_master to prod_flfl_chnl
		constraints[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId),
				pfcRoot.get(ProductFullfilmentChanel_.key).get(ProductFullfilmentChanelKey_.productId));

		// Constrain on sales and fulfilment channle
		constraints[1] = criteriaBuilder.equal(
				pfcRoot.get(ProductFullfilmentChanel_.key).get(ProductFullfilmentChanelKey_.salesChanelCode),
				fulfilmentChannelEntry.getFulfillmentChannel().getKey().getSalesChannelCode());
		constraints[2] = criteriaBuilder.equal(
				pfcRoot.get(ProductFullfilmentChanel_.key).get(ProductFullfilmentChanelKey_.fullfillmentChanelCode),
				fulfilmentChannelEntry.getFulfillmentChannel().getKey().getFulfillmentChannelCode());

		productExists.where(constraints);

		return new ExistsClause<>(productExists);
	}
}
