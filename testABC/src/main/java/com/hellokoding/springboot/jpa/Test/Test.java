/*
package com.hellokoding.springboot.jpa.Test;

import com.hellokoding.springboot.jpa.book.ProductBrand;
import com.hellokoding.springboot.jpa.book.ProductBrandCostOwner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class Test {

    public List<Predicate> buildQueryCondition(CriteriaBuilder criteriaBuilder, Root<ProductBrandCostOwner> productBrandCostOwnerRoot){
        Path path;
        path = criteriaBuilder.treat(productBrandCostOwnerRoot.get("productBrand", ProductBrand.class));
        criteriaBuilder.or(
                criteriaBuilder.equal(path.get(PROD_BRND_ID).as(String.class), 1)
        );
        return null;
    }
}
*/
