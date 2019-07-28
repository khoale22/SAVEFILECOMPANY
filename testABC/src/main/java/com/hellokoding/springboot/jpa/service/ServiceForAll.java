package com.hellokoding.springboot.jpa.service;


import com.hellokoding.springboot.jpa.book.ProductBrand;
import com.hellokoding.springboot.jpa.book.ProductBrandCostOwner;
import com.hellokoding.springboot.jpa.metamodelEntity.ProductBrandCostOwner_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;

@Service
public class ServiceForAll {

    @Autowired
    private EntityManager entityManager;

    String PROD_BRND = "productBrand";
    String PROD_BRND_DES = "productBrandDescription";
    String PROD_BRND_ID = "productBrandId";
    /**
     * Holds the property name of cost owner.
     */
    String CST_OWNR = "costOwner";
    String CST_OWNR_NM = "costOwnerName";
    String CST_OWNR_ID = "costOwnerId";
    /**
     * Holds the property name of top 2 top.
     */
    String T2T = "topToTop";
    String T2T_NM = "topToTopName";
    String T2T_ID = "topToTopId";
    String PERCENT_SIGN = "%";

  /*  public List<ProductBrandCostOwner> findCriteria(){
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductBrandCostOwner> criteriaQuery = criteriaBuilder.createQuery(ProductBrandCostOwner.class);
        Root<ProductBrandCostOwner> productBrandCostOwnerRoot = criteriaQuery.from(ProductBrandCostOwner.class);

      *//*  Test test = new Test();
        List<Predicate> predicates = test.buildQueryCondition( criteriaBuilder, productBrandCostOwnerRoot);

*//*
    }*/

}
