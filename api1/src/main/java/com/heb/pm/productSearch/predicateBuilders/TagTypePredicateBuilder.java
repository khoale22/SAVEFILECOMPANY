package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.GoodsProduct;
import com.heb.pm.entity.GoodsProduct_;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.ProductMaster_;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

/**
 * Handles searching by tax category.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class TagTypePredicateBuilder implements PredicateBuilder {

    /**
     * Constructs a subquery joining to goods_prod and constraining against the tax category.
     *
     * @param criteriaBuilder Used to construct the various parts of the SQL statement.
     * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
     *               sub-query to).
     * @param queryBuilder JPA query builder used to construct the sub-query.
     * @param productSearchCriteria The user's search criteria.
     * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
     * @return A subquery joining to goods_prod and constraining against the tax category.
     */
    @Override
    public ExistsClause<GoodsProduct> buildPredicate(CriteriaBuilder criteriaBuilder,
                                                     Root<? extends ProductMaster> pmRoot,
                                                     CriteriaQuery<? extends ProductMaster> queryBuilder,
                                                     ProductSearchCriteria productSearchCriteria,
                                                     ParameterExpression<String> sessionIdBindVariable) {

        // See if there is an entry in the custom searches for tax category
        if (productSearchCriteria.getCustomSearchEntries() == null) {
            return null;
        }

        CustomSearchEntry tagTypeEntry = null;
        for (CustomSearchEntry c : productSearchCriteria.getCustomSearchEntries()) {
            if (c.getType() == CustomSearchEntry.TAG_TYPE) {
                tagTypeEntry = c;
            }
        }
        if (tagTypeEntry == null || tagTypeEntry.getTagType() == null) {
            return null;
        }

        Subquery<GoodsProduct> productExist = queryBuilder.subquery(GoodsProduct.class);
        Root<GoodsProduct> gpRoot = productExist.from(GoodsProduct.class);
        productExist.select(gpRoot.get("prodId"));

        Predicate[] criteria = new Predicate[2];

        // Join product master to Goods product
        criteria[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId), gpRoot.get(GoodsProduct_.prodId));

        // Constrain on self manufactured
        criteria[1] = criteriaBuilder.equal(gpRoot.get(GoodsProduct_.tagType),
                tagTypeEntry.getTagType().getTagTypeCode());

        productExist.where(criteria);

        return new ExistsClause<>(productExist);
    }
}
