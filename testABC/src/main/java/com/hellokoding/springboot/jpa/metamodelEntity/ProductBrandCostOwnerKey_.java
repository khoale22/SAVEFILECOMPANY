package com.hellokoding.springboot.jpa.metamodelEntity;

import com.hellokoding.springboot.jpa.book.ProductBrandCostOwnerKey;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductBrandCostOwnerKey.class)
public abstract class ProductBrandCostOwnerKey_ {

	public static volatile SingularAttribute<ProductBrandCostOwnerKey, Long> productBrandId;
	public static volatile SingularAttribute<ProductBrandCostOwnerKey, Integer> costOwnerId;

}

